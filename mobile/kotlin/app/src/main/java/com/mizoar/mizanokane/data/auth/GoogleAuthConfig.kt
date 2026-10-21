package com.mizoar.mizanokane.data.auth

/**
 * Konfigurasi autentikasi Google untuk Mizan Okane.
 * OAuth Client ID Web diambil dari desktop/src/js/sync.js.
 * Client ID Android TIDAK ditulis di kode: dikenali Google Play services lewat
 * package name com.mizoar.mizanokane dan SHA-1 signing key.
 */
object GoogleAuthConfig {
    const val WEB_CLIENT_ID = "663070681186-pjrnce0fcpjecvbf9o2fj99e59e3s0un.apps.googleusercontent.com"
    const val DRIVE_APPDATA_SCOPE = "https://www.googleapis.com/auth/drive.appdata"
}
