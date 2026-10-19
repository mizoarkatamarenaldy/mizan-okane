# Mizan Okane Desktop (Tauri)

Aplikasi desktop Windows untuk Mizan Okane berbasis Tauri v2 yang membuka situs di GitHub Pages (`https://mizoarkatamarenaldy.github.io/mizan-okane/`) dalam jendela aplikasi mandiri.

## Prasyarat
- Windows 10/11 (dengan Microsoft Edge WebView2 bawaan)
- Node.js & npm
- Rust toolchain (`rustc` & `cargo`)

## Perintah Menjalankan dan Membangun

### Opsi 1: Menggunakan npm (dari folder desktop/tauri)
1. Pasang dependensi CLI:
   ```bash
   npm install
   ```
2. Jalankan mode pengembangan:
   ```bash
   npm run dev
   ```
3. Bangun installer Windows (.exe via NSIS):
   ```bash
   npm run build
   ```
   Installer akan dihasilkan di: `src-tauri/target/release/bundle/nsis/`

### Opsi 2: Menggunakan Cargo langsung (dari folder desktop/tauri/src-tauri)
1. Jalankan aplikasi:
   ```bash
   cargo run
   ```
2. Bangun binary rilis:
   ```bash
   cargo build --release
   ```
