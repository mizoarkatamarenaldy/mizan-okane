# mizan-okane

Money manager pribadi berbasis PWA. Jalan di HP dan PC, bisa offline, data sinkron otomatis lewat Google Drive.

*Mizan* berarti keseimbangan, *okane* berarti uang. Tujuannya sederhana: menjaga pemasukan dan pengeluaran tetap seimbang.

## Status
Dalam pengembangan. Rencana dan progres ada di [`PLAN.md`](PLAN.md) dan [`CHANGELOG.md`](CHANGELOG.md).

## Fitur (target versi 1)
- Catat pemasukan dan pengeluaran
- Kategori transaksi
- Riwayat transaksi dan total saldo
- Jalan offline
- Sinkron antar perangkat lewat Google Drive

## Cara kerja data
- Data disimpan lokal di perangkat (IndexedDB).
- Sinkron ke folder khusus aplikasi di Google Drive milik pengguna (`appDataFolder`).
- Tidak ada server pihak ketiga.
- Repo ini hanya berisi kode dan dokumen, tidak berisi data keuangan.

## Teknologi
- PWA (HTML, CSS, JavaScript)
- IndexedDB
- Google Drive API dan Google OAuth
- GitHub Pages

## Dokumen
- [`PLAN.md`](PLAN.md): rencana tahap dan keputusan desain
- [`CHANGELOG.md`](CHANGELOG.md): riwayat fitur dan perbaikan

## Pembuat
Dibuat oleh Mizo.
