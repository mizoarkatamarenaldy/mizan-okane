fn main() {
    tauri_build::try_build(
        tauri_build::Attributes::new().app_manifest(
            tauri_build::AppManifest::new().commands(&[
                "tauri_login_google",
                "tauri_get_access_token",
                "tauri_logout_google",
            ]),
        ),
    )
    .expect("failed to run tauri-build");
}
