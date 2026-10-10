# CHANGELOG

Catat setiap fitur atau perbaikan yang selesai. Entri terbaru di atas.

## Format
```
## YYYY-MM-DD: nama singkat
- Ditambah: ...
- Diperbaiki: ...
- Diubah: ...
```

## 2026-10-10: Tahap 5: Perbaikan login Google lebih mulus
- Ditambah: Penanda boolean `googleLoggedIn` pada store `meta` IndexedDB untuk mencatat perangkat yang pernah login Google tanpa menyimpan token (`js/db.js`, `js/sync.js`).
- Ditambah: Status netral "Ketuk di mana saja untuk lanjut sinkron" dan listener ketukan pertama pengguna saat aplikasi dibuka jika perangkat sudah terhubung tapi belum memiliki token di memori (`js/sync.js`, `js/app.js`).
- Ditambah: Pemanggilan `ensureToken` di dalam handler interaksi pengguna (`handleSubmit` dan `handleDelete`) untuk memperbarui token jika sudah habis masa berlakunya (`js/sync.js`, `js/app.js`).
- Diperbaiki: Mencegah popup Google terblokir dengan tidak lagi memanggil permintaan token otomatis saat aplikasi baru dibuka (`js/sync.js`).
- Diubah: Penghapusan penanda login di store `meta` saat pengguna menekan tombol Logout (`js/sync.js`, `js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v6` untuk memperbarui cache aset.
- Diubah: `PLAN.md` menambahkan dan mencentang fitur "Perbaikan: login Google lebih mulus (tanpa popup terblokir)" pada Tahap 5.

## 2026-10-10: Tahap 5: Jam:menit pada transaksi
- Ditambah: Field opsional `time` (format "HH:mm") pada data transaksi, input jam pada form transaksi (`index.html`, `js/app.js`), dan dukungan gaya input waktu (`css/style.css`).
- Ditambah: Pengisian otomatis input jam dengan waktu sekarang untuk transaksi baru, serta dukungan edit/kosongkan jam (`js/app.js`).
- Ditambah: Tampilan jam di samping tanggal pada daftar riwayat transaksi untuk transaksi yang memiliki jam (`js/app.js`).
- Diubah: Pengurutan riwayat transaksi dan ekspor CSV berdasarkan tanggal lalu jam terbaru (`js/app.js`).
- Diubah: Penambahan kolom `jam` setelah kolom `tanggal` pada ekspor CSV (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v5` untuk memperbarui cache aset.
- Diubah: `PLAN.md` mencentang fitur "Jam:menit pada transaksi" pada Tahap 5.

## 2026-10-10: Tahap 5: Revisi PLAN.md
- Diubah: `PLAN.md` pada bagian "Aturan kerja" (pengerjaan satu fitur per sesi untuk Tahap 5 dan aturan bahwa Tahap 5 baru dicentang setelah semua fiturnya tercentang), penambahan kotak centang pada tiap fitur Tahap 5 beserta opsi fitur jam:menit transaksi, dan penghapusan bagian "Catatan".

## 2026-10-10: Tahap 5: Ekspor CSV
- Ditambah: Tombol "Ekspor CSV" di samping tombol "Ekspor JSON" pada antarmuka sinkronisasi (`index.html`, `js/app.js`).
- Ditambah: Fitur ekspor data transaksi aktif (tanpa soft delete) ke file CSV berformat `mizan-okane-transaksi-YYYY-MM-DD.csv`, diurutkan dari tanggal terbaru, dengan kolom tanggal, tipe, kategori, jumlah, dan catatan (`js/app.js`).
- Ditambah: Escape karakter CSV (koma, tanda kutip, baris baru), penambahan UTF-8 BOM untuk kompatibilitas Excel, serta penanganan jika belum ada transaksi (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v4` untuk memperbarui cache aset.
- Diubah: `PLAN.md` mencentang fitur Ekspor CSV pada Tahap 5.

## 2026-10-08: Tahap 4: Aturan konflik data
- Ditambah: Tombol "Ekspor JSON" di antarmuka sinkronisasi untuk backup manual (`index.html`, `js/app.js`).
- Diubah: Logika sinkronisasi di `js/sync.js` untuk menggabungkan data transaksi dari dua perangkat (lokal dan Drive) berdasarkan waktu ubah (`updatedAt`) yang terbaru, dan mencakup transaksi dengan tanda soft delete.
- Diubah: Menghapus UI dan logika resolusi konflik ("Pakai data Drive / Pakai data perangkat ini") dari `index.html`, `js/app.js`, dan `js/sync.js`.
- Diubah: `sw.js` naik ke versi `mizan-v3` untuk memperbarui cache.


## 2026-10-06: Tahap 3: Login Google dan sinkron Drive
- Ditambah: `js/sync.js`, login Google lewat Google Identity Services (token flow, tanpa client secret dan tanpa server). Access token hanya disimpan di memori.
- Ditambah: Simpan dan ambil satu file JSON (`mizan-okane-data.json`) di `appDataFolder` lewat Drive API v3.
- Ditambah: Sinkron otomatis saat app dibuka, saat kembali online, dan sekitar 1,5 detik setelah ada perubahan data.
- Ditambah: Bar sinkron di `index.html` (status, tombol Login Google, Sinkron, Logout) dan gayanya di `css/style.css`.
- Ditambah: Pilihan "Pakai data Drive" atau "Pakai data perangkat ini" kalau data di Drive dan di perangkat sama-sama berubah. Gabung per transaksi menunggu Tahap 4.
- Diubah: `js/db.js` naik ke versi 2 dengan store `meta` untuk status sinkron per perangkat (bukan token).
- Diubah: `js/app.js` menandai perubahan setelah simpan atau hapus, dan memulai sinkron setelah data lokal tampil.
- Diubah: `sw.js` cache `mizan-v2`, menyertakan `js/sync.js`, dan tidak lagi menangani request ke googleapis.com, accounts.google.com, request lintas origin, maupun request selain GET.

## 2026-10-06: Tahap 2: PWA
- Ditambah: Konfigurasi PWA (manifest.webmanifest, sw.js, ikon).
- Ditambah: Link manifest dan service worker di index.html dan js/app.js.
- Ditambah: Tombol "Install Aplikasi" dengan event beforeinstallprompt.

## 2026-10-06: Tahap 1: Lokal
- Diperbaiki: Syntax error pada file `js/app.js` terkait karakter escape di dalam template literal yang menyebabkan error gagal muat.
- Ditambah: Antarmuka dan logika aplikasi lokal (index.html, style.css, app.js).
- Ditambah: Penyimpanan data di perangkat menggunakan IndexedDB (db.js).
- Ditambah: Fitur CRUD transaksi sederhana dengan soft-delete.
- Ditambah: Format rupiah dan perhitungan saldo.

## 2026-10-06: Proyek dimulai
- Ditambah: `PLAN.md` dan `CHANGELOG.md`
