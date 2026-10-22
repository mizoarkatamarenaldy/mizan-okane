# Mizan Okane

Money manager pribadi untuk mencatat pemasukan dan pengeluaran. Jalan di Android, Windows, dan web, data bisa disinkronkan lewat Google Drive milikmu sendiri.

*Mizan* berarti keseimbangan, *okane* berarti uang. Tujuannya sederhana: menjaga pemasukan dan pengeluaran tetap seimbang.

## Status
Pre-release **v0.1.0**. Aplikasi sudah bisa dipakai, tapi tampilan di Android masih dalam penyempurnaan karena baru dikonversi ke Kotlin. Rencana dan progres ada di [`dev/PLAN.md`](dev/PLAN.md) dan [`dev/CHANGELOG.md`](dev/CHANGELOG.md).

## Unduh
- **Android (APK):** lewat [GitHub Releases](https://github.com/mizoarkatamarenaldy/mizan-okane/releases/tag/v0.1.0). Tidak tersedia di Play Store.
- **Windows:** lewat [GitHub Releases](https://github.com/mizoarkatamarenaldy/mizan-okane/releases/tag/v0.1.0).
- **Web (PWA):** [Buka di browser](https://mizoarkatamarenaldy.github.io/mizan-okane/). Bisa di-install di HP dan PC.

## Fitur
- Catat pemasukan dan pengeluaran, lengkap dengan waktu
- Kategori dan sub kategori, dengan ikon kustom
- Beberapa dompet, transfer antar dompet, dan mata uang per dompet (Rupiah, Yen, USD)
- Budget per kategori dan sub kategori
- Riwayat dengan pencarian dan filter bulan
- Statistik harian, bulanan, dan tahunan, termasuk diagram per kategori
- Timbangan: ringkasan keseimbangan pemasukan dan pengeluaran
- Mode gelap
- Ekspor CSV dan JSON, impor JSON
- Jalan offline
- Bisa dipakai tanpa login

## Cara kerja data
- Data disimpan lokal di perangkat (IndexedDB di web, Room di Android).
- Sinkron opsional ke folder khusus aplikasi di Google Drive milikmu (`appDataFolder`) lewat Google Sign-In.
- Tidak ada server pihak ketiga, tidak ada iklan, tidak ada analitik.
- Repo ini hanya berisi kode dan dokumen, tidak berisi data keuangan.
- Format data dicatat di [`dev/SCHEMA.md`](dev/SCHEMA.md).

## Struktur repo
- `desktop/`: versi web (PWA) dan Windows (Tauri)
- `mobile/kotlin/`: aplikasi Android native (Kotlin, Jetpack Compose, Room)
- `dev/`: rencana, aturan, desain, skema data, dan changelog

## Teknologi
- Web: HTML, CSS, JavaScript, IndexedDB, GitHub Pages
- Windows: Tauri
- Android: Kotlin, Jetpack Compose, Room
- Sinkron: Google Drive API dan Google OAuth

## Privasi
Kebijakan privasi: [Privasi](https://mizoarkatamarenaldy.github.io/mizan-okane/privasi.html)

## Dukung Pengembang
Aplikasi ini gratis dan dikembangkan sendiri (dan AI seperti Claude juga Gemini). Kalau terasa bermanfaat, kamu bisa mendukung lewat:

<a href="https://ko-fi.com/mizoarkatamarenaldy">
  <img src="https://ko-fi.com/img/githubbutton_sm.svg" height="40" alt="Dukung di Ko-fi" />
</a>
<a href="https://trakteer.id/MizoArkatamaRenaldy">
  <img src="https://edge-cdn.trakteer.id/images/embed/trbtn-red-1.png?v=24-01-2025" height="40" alt="Trakteer Saya" />
</a>

## Pembuat
Dibuat oleh [Mizo Arkatama Renaldy](mizoarkatamarenaldy.github.io).
