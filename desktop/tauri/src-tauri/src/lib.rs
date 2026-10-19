use std::collections::HashMap;
use std::io::{BufRead, BufReader, Write};
use std::net::TcpListener;
use std::path::PathBuf;
use std::time::{Duration, Instant};

use base64::prelude::*;
use sha2::{Digest, Sha256};
use tauri::{webview::NewWindowResponse, Manager, WebviewWindowBuilder};

#[derive(serde::Deserialize, Default)]
struct OauthConfigFile {
    #[serde(default)]
    client_id: Option<String>,
    #[serde(default)]
    client_secret: Option<String>,
    #[serde(default)]
    installed: Option<InstalledConfig>,
}

#[derive(serde::Deserialize, Default)]
struct InstalledConfig {
    #[serde(default)]
    client_id: Option<String>,
    #[serde(default)]
    client_secret: Option<String>,
}

struct DesktopOauthConfig {
    client_id: String,
    client_secret: String,
}

#[derive(serde::Serialize, serde::Deserialize, Clone)]
pub struct AuthResult {
    pub access_token: String,
    pub expires_in: u64,
    pub email: Option<String>,
}

#[derive(serde::Serialize, serde::Deserialize, Default, Clone)]
struct StoredAuth {
    pub refresh_token: String,
    #[serde(default)]
    pub email: Option<String>,
}

#[derive(serde::Deserialize, Default)]
struct GoogleTokenResponse {
    #[serde(default)]
    access_token: String,
    #[serde(default)]
    expires_in: Option<u64>,
    #[serde(default)]
    refresh_token: Option<String>,
}

fn load_desktop_oauth_config(app: &tauri::AppHandle) -> Result<DesktopOauthConfig, String> {
    let mut candidates = Vec::new();

    let manifest_dir = PathBuf::from(env!("CARGO_MANIFEST_DIR"));
    candidates.push(manifest_dir.join("oauth-desktop.json"));

    if let Ok(cwd) = std::env::current_dir() {
        candidates.push(cwd.join("desktop/tauri/src-tauri/oauth-desktop.json"));
        candidates.push(cwd.join("src-tauri/oauth-desktop.json"));
        candidates.push(cwd.join("oauth-desktop.json"));
    }

    if let Ok(app_data) = app.path().app_data_dir() {
        candidates.push(app_data.join("oauth-desktop.json"));
    }

    if let Ok(exe_path) = std::env::current_exe() {
        if let Some(parent) = exe_path.parent() {
            candidates.push(parent.join("oauth-desktop.json"));
        }
    }

    let mut found_content: Option<String> = None;
    for path in &candidates {
        if path.is_file() {
            if let Ok(content) = std::fs::read_to_string(path) {
                if !content.trim().is_empty() {
                    found_content = Some(content);
                    break;
                }
            }
        }
    }

    let content = found_content.ok_or_else(|| {
        "File konfigurasi OAuth desktop (oauth-desktop.json) tidak ditemukan atau masih kosong. Silakan salin oauth-desktop.example.json menjadi oauth-desktop.json di folder desktop/tauri/src-tauri/ dan isi client_id serta client_secret milik Anda.".to_string()
    })?;

    let parsed: OauthConfigFile = serde_json::from_str(&content).map_err(|e| {
        format!("Format berkas oauth-desktop.json tidak valid: {}", e)
    })?;

    let client_id = parsed
        .installed
        .as_ref()
        .and_then(|inst| inst.client_id.clone())
        .or(parsed.client_id)
        .unwrap_or_default()
        .trim()
        .to_string();

    let client_secret = parsed
        .installed
        .as_ref()
        .and_then(|inst| inst.client_secret.clone())
        .or(parsed.client_secret)
        .unwrap_or_default()
        .trim()
        .to_string();

    if client_id.is_empty() {
        return Err("Nilai client_id di file oauth-desktop.json masih kosong. Silakan isi Client ID bertipe Desktop Anda.".into());
    }

    Ok(DesktopOauthConfig {
        client_id,
        client_secret,
    })
}

fn get_tokens_file_path(app: &tauri::AppHandle) -> Result<PathBuf, String> {
    let app_data = app
        .path()
        .app_data_dir()
        .map_err(|e| format!("Gagal mengakses folder data aplikasi: {}", e))?;
    std::fs::create_dir_all(&app_data)
        .map_err(|e| format!("Gagal membuat folder data aplikasi: {}", e))?;
    Ok(app_data.join("auth-tokens.json"))
}

fn read_stored_auth(app: &tauri::AppHandle) -> Result<Option<StoredAuth>, String> {
    let path = get_tokens_file_path(app)?;
    if !path.exists() {
        return Ok(None);
    }
    let content = std::fs::read_to_string(&path)
        .map_err(|e| format!("Gagal membaca token tersimpan: {}", e))?;
    if content.trim().is_empty() {
        return Ok(None);
    }
    let auth: StoredAuth = serde_json::from_str(&content)
        .map_err(|e| format!("Format token tersimpan tidak valid: {}", e))?;
    if auth.refresh_token.trim().is_empty() {
        return Ok(None);
    }
    Ok(Some(auth))
}

fn save_stored_auth(app: &tauri::AppHandle, auth: &StoredAuth) -> Result<(), String> {
    let path = get_tokens_file_path(app)?;
    let content = serde_json::to_string_pretty(auth)
        .map_err(|e| format!("Gagal memformat token: {}", e))?;
    std::fs::write(&path, content)
        .map_err(|e| format!("Gagal menyimpan token ke data aplikasi: {}", e))?;
    Ok(())
}

fn delete_stored_auth(app: &tauri::AppHandle) -> Result<(), String> {
    let path = get_tokens_file_path(app)?;
    if path.exists() {
        let _ = std::fs::remove_file(&path);
    }
    Ok(())
}

fn open_browser(url: &str) -> Result<(), String> {
    #[cfg(target_os = "windows")]
    {
        std::process::Command::new("rundll32")
            .args(["url.dll,FileProtocolHandler", url])
            .spawn()
            .map_err(|e| format!("Gagal membuka browser sistem: {}", e))?;
        Ok(())
    }
    #[cfg(target_os = "macos")]
    {
        std::process::Command::new("open")
            .arg(url)
            .spawn()
            .map_err(|e| format!("Gagal membuka browser sistem: {}", e))?;
        Ok(())
    }
    #[cfg(target_os = "linux")]
    {
        std::process::Command::new("xdg-open")
            .arg(url)
            .spawn()
            .map_err(|e| format!("Gagal membuka browser sistem: {}", e))?;
        Ok(())
    }
    #[cfg(not(any(target_os = "windows", target_os = "macos", target_os = "linux")))]
    {
        Err("Platform tidak didukung untuk membuka browser secara otomatis".into())
    }
}

#[tauri::command]
async fn tauri_login_google(app: tauri::AppHandle) -> Result<AuthResult, String> {
    let config = load_desktop_oauth_config(&app)?;

    let mut verifier_bytes = [0u8; 32];
    getrandom::fill(&mut verifier_bytes)
        .map_err(|e| format!("Gagal menghasilkan nilai acak PKCE: {}", e))?;
    let code_verifier = BASE64_URL_SAFE_NO_PAD.encode(verifier_bytes);

    let mut hasher = Sha256::new();
    hasher.update(code_verifier.as_bytes());
    let code_challenge = BASE64_URL_SAFE_NO_PAD.encode(hasher.finalize());

    let mut state_bytes = [0u8; 16];
    getrandom::fill(&mut state_bytes)
        .map_err(|e| format!("Gagal menghasilkan nilai acak state: {}", e))?;
    let state = BASE64_URL_SAFE_NO_PAD.encode(state_bytes);

    let listener = TcpListener::bind("127.0.0.1:0")
        .map_err(|e| format!("Gagal menjalankan server loopback lokal: {}", e))?;
    let port = listener
        .local_addr()
        .map_err(|e| format!("Gagal mendapatkan port loopback: {}", e))?
        .port();
    let redirect_uri = format!("http://127.0.0.1:{}", port);

    let mut auth_url = url::Url::parse("https://accounts.google.com/o/oauth2/v2/auth")
        .map_err(|e| e.to_string())?;
    auth_url
        .query_pairs_mut()
        .append_pair("client_id", &config.client_id)
        .append_pair("redirect_uri", &redirect_uri)
        .append_pair("response_type", "code")
        .append_pair("scope", "https://www.googleapis.com/auth/drive.appdata")
        .append_pair("code_challenge", &code_challenge)
        .append_pair("code_challenge_method", "S256")
        .append_pair("state", &state)
        .append_pair("access_type", "offline")
        .append_pair("prompt", "consent");

    open_browser(auth_url.as_str())?;

    listener
        .set_nonblocking(true)
        .map_err(|e| format!("Gagal mengatur listener loopback: {}", e))?;

    let start = Instant::now();
    let timeout = Duration::from_secs(180);
    let mut auth_code = None;
    let mut auth_error = None;

    loop {
        let (mut stream, _) = match listener.accept() {
            Ok(res) => res,
            Err(ref e) if e.kind() == std::io::ErrorKind::WouldBlock => {
                if start.elapsed() > timeout {
                    return Err(
                        "Waktu tunggu login Google di browser habis (timeout). Silakan coba lagi."
                            .into(),
                    );
                }
                std::thread::sleep(Duration::from_millis(100));
                continue;
            }
            Err(e) => return Err(format!("Gagal menerima koneksi dari browser: {}", e)),
        };

        let mut reader = BufReader::new(&stream);
        let mut request_line = String::new();
        if reader.read_line(&mut request_line).is_err() || request_line.is_empty() {
            continue;
        }

        let parts: Vec<&str> = request_line.split_whitespace().collect();
        if parts.len() < 2 {
            continue;
        }
        let target = parts[1];

        if target.starts_with("/favicon.ico") {
            let response = "HTTP/1.1 404 Not Found\r\nContent-Length: 0\r\nConnection: close\r\n\r\n";
            let _ = stream.write_all(response.as_bytes());
            let _ = stream.flush();
            continue;
        }

        let query = if let Some(pos) = target.find('?') {
            &target[pos + 1..]
        } else {
            ""
        };

        let parsed_params: HashMap<String, String> = url::form_urlencoded::parse(query.as_bytes())
            .into_owned()
            .collect();

        if let Some(err) = parsed_params.get("error") {
            auth_error = Some(err.clone());
            let html = "<!DOCTYPE html><html><head><meta charset='utf-8'><title>Login Dibatalkan</title><style>body{font-family:sans-serif;text-align:center;padding:50px;background:#12131a;color:#f0f0f0;}h1{color:#e53e3e;}p{color:#a0aec0;font-size:16px;}</style></head><body><h1>Login Dibatalkan</h1><p>Anda dapat menutup halaman ini dan kembali ke aplikasi Mizan Okane.</p></body></html>";
            let response = format!(
                "HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=utf-8\r\nContent-Length: {}\r\nConnection: close\r\n\r\n{}",
                html.len(),
                html
            );
            let _ = stream.write_all(response.as_bytes());
            let _ = stream.flush();
            break;
        }

        if let Some(code) = parsed_params.get("code") {
            let returned_state = parsed_params.get("state").map(|s| s.as_str()).unwrap_or("");
            if returned_state != state {
                auth_error = Some("Parameter state tidak cocok (kemungkinan verifikasi gagal). Silakan coba lagi.".into());
            } else {
                auth_code = Some(code.clone());
            }

            let html = "<!DOCTYPE html><html><head><meta charset='utf-8'><title>Login Berhasil</title><style>body{font-family:sans-serif;text-align:center;padding:50px;background:#12131a;color:#f0f0f0;}h1{color:#48bb78;}p{color:#a0aec0;font-size:16px;}</style></head><body><h1>Login Berhasil</h1><p>Anda dapat menutup tab ini dan kembali ke aplikasi Mizan Okane.</p><script>setTimeout(function(){window.close();},2000);</script></body></html>";
            let response = format!(
                "HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=utf-8\r\nContent-Length: {}\r\nConnection: close\r\n\r\n{}",
                html.len(),
                html
            );
            let _ = stream.write_all(response.as_bytes());
            let _ = stream.flush();
            break;
        }
    }

    drop(listener);

    if let Some(err) = auth_error {
        return Err(format!("Login Google dibatalkan atau ditolak ({})", err));
    }
    let code = auth_code.ok_or_else(|| "Kode otorisasi tidak ditemukan dari Google.".to_string())?;

    let client = reqwest::Client::new();
    let mut params = HashMap::new();
    params.insert("client_id", config.client_id.clone());
    if !config.client_secret.is_empty() {
        params.insert("client_secret", config.client_secret.clone());
    }
    params.insert("code", code);
    params.insert("code_verifier", code_verifier);
    params.insert("grant_type", "authorization_code".to_string());
    params.insert("redirect_uri", redirect_uri);

    let res = client
        .post("https://oauth2.googleapis.com/token")
        .form(&params)
        .send()
        .await
        .map_err(|e| format!("Gagal menghubungi server token Google: {}", e))?;

    if !res.status().is_success() {
        let error_text = res.text().await.unwrap_or_default();
        return Err(format!("Google menolak penukaran token: {}", error_text));
    }

    let token_data: GoogleTokenResponse = res
        .json()
        .await
        .map_err(|e| format!("Gagal membaca respons token Google: {}", e))?;

    if token_data.access_token.trim().is_empty() {
        return Err("Access token tidak ditemukan dalam respons Google.".into());
    }

    let email = match client
        .get("https://www.googleapis.com/drive/v3/about?fields=user(emailAddress)")
        .bearer_auth(&token_data.access_token)
        .send()
        .await
    {
        Ok(email_res) if email_res.status().is_success() => {
            let about: serde_json::Value = email_res.json().await.unwrap_or_default();
            about
                .get("user")
                .and_then(|u| u.get("emailAddress"))
                .and_then(|e| e.as_str())
                .map(|s| s.to_string())
        }
        _ => None,
    };

    if let Some(ref rt) = token_data.refresh_token {
        let stored = StoredAuth {
            refresh_token: rt.clone(),
            email: email.clone(),
        };
        save_stored_auth(&app, &stored)?;
    }

    Ok(AuthResult {
        access_token: token_data.access_token,
        expires_in: token_data.expires_in.unwrap_or(3600),
        email,
    })
}

#[tauri::command]
async fn tauri_get_access_token(app: tauri::AppHandle) -> Result<Option<AuthResult>, String> {
    let mut stored = match read_stored_auth(&app)? {
        Some(s) => s,
        None => return Ok(None),
    };

    let config = match load_desktop_oauth_config(&app) {
        Ok(c) => c,
        Err(_) => return Ok(None),
    };

    let client = reqwest::Client::new();
    let mut params = HashMap::new();
    params.insert("client_id", config.client_id);
    if !config.client_secret.is_empty() {
        params.insert("client_secret", config.client_secret);
    }
    params.insert("refresh_token", stored.refresh_token.clone());
    params.insert("grant_type", "refresh_token".to_string());

    let res = client
        .post("https://oauth2.googleapis.com/token")
        .form(&params)
        .send()
        .await
        .map_err(|e| format!("Gagal memperbarui token Google: {}", e))?;

    if !res.status().is_success() {
        let status = res.status();
        let error_body = res.text().await.unwrap_or_default();
        if status.as_u16() == 400 || status.as_u16() == 401 {
            let _ = delete_stored_auth(&app);
            return Err("Sesi Google berakhir atau izin dicabut. Silakan login ulang.".into());
        }
        return Err(format!("Gagal memperbarui token Google: {}", error_body));
    }

    let token_data: GoogleTokenResponse = res
        .json()
        .await
        .map_err(|e| format!("Gagal membaca respons pembaruan token: {}", e))?;

    if let Some(new_rt) = token_data.refresh_token {
        stored.refresh_token = new_rt;
        let _ = save_stored_auth(&app, &stored);
    }

    Ok(Some(AuthResult {
        access_token: token_data.access_token,
        expires_in: token_data.expires_in.unwrap_or(3600),
        email: stored.email,
    }))
}

#[tauri::command]
fn tauri_logout_google(app: tauri::AppHandle) -> Result<(), String> {
    delete_stored_auth(&app)?;
    Ok(())
}

#[cfg_attr(mobile, tauri::mobile_entry_point)]
pub fn run() {
    tauri::Builder::default()
        .invoke_handler(tauri::generate_handler![
            tauri_login_google,
            tauri_get_access_token,
            tauri_logout_google
        ])
        .setup(|app| {
            let window_config = app
                .config()
                .app
                .windows
                .iter()
                .find(|w| w.label == "main")
                .ok_or("Konfigurasi window 'main' tidak ditemukan")?;

            WebviewWindowBuilder::from_config(app.handle(), window_config)?
                .on_new_window(|url, _features| {
                    if url.scheme() == "https" && url.host_str() == Some("accounts.google.com") {
                        NewWindowResponse::Allow
                    } else {
                        NewWindowResponse::Deny
                    }
                })
                .build()?;

            Ok(())
        })
        .run(tauri::generate_context!())
        .expect("error while running tauri application");
}
