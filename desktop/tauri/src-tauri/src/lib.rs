use tauri::{webview::NewWindowResponse, WebviewWindowBuilder};

#[cfg_attr(mobile, tauri::mobile_entry_point)]
pub fn run() {
    tauri::Builder::default()
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

