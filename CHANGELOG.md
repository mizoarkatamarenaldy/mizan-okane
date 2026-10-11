# CHANGELOG

Catat setiap fitur atau perbaikan yang selesai. Entri terbaru di atas.

## Format
```
## YYYY-MM-DD: nama singkat
- Ditambah: ...
- Diperbaiki: ...
- Diubah: ...
```

## 2026-10-11: Tahap 5: Perbaikan field Waktu wajib diisi
- Ditambah: Elemen pesan error "Waktu wajib diisi" di bawah field Waktu pada formulir transaksi (`index.html`).
- Ditambah: Kelas styling `.form-error-message` untuk menampilkan pesan validasi form dengan warna teks bahaya (`css/style.css`).
- Ditambah: Validasi field Waktu wajib diisi saat simpan transaksi pada form tambah dan form edit (mode 24 jam dan 12 jam) yang menampilkan pesan error inline tanpa alert dan tanpa popup bawaan browser (`js/app.js`).
- Diubah: Pesan error waktu disembunyikan secara otomatis saat formulir di-reset, dibatalkan, atau saat nilai waktu diisi (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v17` untuk memperbarui cache aset (`sw.js`).
- Diubah: `PLAN.md` mencatat dan mencentang perbaikan field Waktu wajib diisi pada Tahap 5 (`PLAN.md`).
## 2026-10-11: Tahap 5: Perbaikan hapus tombol Kosongkan di field Waktu
- Dihapus: Tombol "Kosongkan" di sebelah label "Waktu" pada formulir transaksi beserta elemen pembungkus `.time-label-wrapper` (`index.html`).
- Dihapus: Aturan CSS `.time-label-wrapper` dan `.btn-clear-time` (`css/style.css`).
- Dihapus: Referensi elemen DOM dan event listener untuk `btnClearTime` (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v16` untuk memperbarui cache aset (`sw.js`).
- Diubah: `PLAN.md` mencatat dan mencentang perbaikan hapus tombol "Kosongkan" di field Waktu pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Grafik pemasukan bulanan
- Ditambah: Toggle "Pengeluaran / Pemasukan" pada header grafik bulanan untuk beralih antara grafik pengeluaran dan grafik pemasukan (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Dukungan grafik pemasukan bulanan yang menampilkan total pemasukan per bulan, warna batang hijau, rincian tooltip bulan terpilih, serta perhitungan rata-rata pemasukan per bulan (`js/app.js`, `css/style.css`).
- Diubah: Pemfilteran transaksi pada grafik bulanan mengecualikan transaksi transfer antar dompet dan transaksi yang dihapus (soft delete) baik pada mode pengeluaran maupun pemasukan (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v15` untuk memperbarui cache aset (`sw.js`).
- Diubah: `PLAN.md` mencentang fitur "Grafik pemasukan bulanan (bisa dipilih lewat toggle Pengeluaran / Pemasukan, transfer antar dompet tidak dihitung)" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Pilihan format jam 12 jam (AM/PM) atau 24 jam
- Ditambah: Pengaturan format jam pada bagian pengaturan dengan pilihan 24 jam dan 12 jam (AM/PM), default 24 jam, disimpan per perangkat di store `meta` IndexedDB dan tidak ikut sinkron (`index.html`, `js/db.js`, `js/app.js`, `css/style.css`).
- Ditambah: Antarmuka input waktu mode 12 jam pada formulir transaksi menggunakan kombinasi pilihan jam (01-12), menit (00-59), dan AM/PM, serta tombol "Kosongkan" untuk mengosongkan jam pada kedua mode (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Pengisian otomatis jam sekarang serta pengisian saat edit/kosongkan transaksi yang mendukung mode 24 jam dan 12 jam (`js/app.js`).
- Diubah: Tampilan jam pada riwayat transaksi mengikuti format yang dipilih (24 jam atau 12 jam AM/PM) secara realtime saat pengaturan diubah tanpa perlu memuat ulang halaman (`js/app.js`).
- Diubah: Data transaksi tetap disimpan dan diekspor ke CSV dalam format 24 jam "HH:mm" tanpa mengubah data transaksi lama (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v14` untuk memperbarui cache aset (`sw.js`).
- Diubah: `PLAN.md` mencentang fitur "Pilihan format jam 12 jam (AM/PM) atau 24 jam" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Ganti label Jam (opsional) jadi Waktu
- Diubah: Label input waktu pada formulir transaksi diganti dari "Jam (opsional)" menjadi "Waktu" (`index.html`).
- Diubah: `sw.js` naik ke versi `mizan-v13` untuk memperbarui cache aset (`sw.js`).
- Diubah: `PLAN.md` mencentang fitur "Ganti label 'Jam (opsional)' jadi 'Waktu'" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Transfer antar dompet
- Ditambah: Opsi tipe transaksi "Transfer" pada formulir transaksi untuk memindahkan uang dari satu dompet ke dompet lain (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Dropdown pilihan dompet tujuan ("Ke Dompet") yang otomatis aktif saat tipe transfer dipilih serta penyesuaian label dompet asal menjadi "Dari Dompet" (`index.html`, `js/app.js`).
- Ditambah: Validasi transfer membutuhkan minimal dua dompet aktif dan dompet tujuan tidak boleh sama dengan dompet asal (`js/app.js`).
- Ditambah: Perhitungan saldo per dompet yang berkurang pada dompet asal dan bertambah pada dompet tujuan tanpa mengubah total saldo, total pemasukan, dan total pengeluaran (`js/app.js`).
- Ditambah: Tampilan riwayat transaksi transfer dengan badge alur perpindahan dompet (`Dompet Asal → Dompet Tujuan`), warna jumlah transfer netral, dan tanda alur keluar/masuk saat memfilter dompet tertentu (`js/app.js`, `css/style.css`).
- Ditambah: Filter dompet pada riwayat transaksi yang menampilkan transfer jika dompet yang difilter terlibat baik sebagai dompet asal maupun tujuan (`js/app.js`).
- Ditambah: Dukungan soft delete pada transfer dengan konfirmasi dialog web (`<dialog>`) serta pembaruan otomatis saldo kedua dompet dan sinkronisasi ke Google Drive (`js/app.js`).
- Diubah: Perlindungan hapus dompet yang mencakup pemeriksaan transaksi transfer aktif baik sebagai dompet asal maupun dompet tujuan (`js/app.js`).
- Diubah: Format ekspor CSV untuk transfer menampilkan alur dompet `Dompet Asal -> Dompet Tujuan` pada kolom `dompet` dan kategori `Transfer` (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v12` untuk memperbarui cache aset (`sw.js`).
- Diubah: `PLAN.md` mencentang fitur "Transfer antar dompet" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Beberapa dompet
- Ditambah: Model data dompet dengan dompet default "Dompet Utama" serta fungsi `getWallets` dan `saveWallets` pada store `meta` IndexedDB (`js/db.js`).
- Ditambah: Bagian antarmuka pengaturan dompet di halaman web dengan daftar dompet aktif, formulir penambahan dompet, ubah nama dompet secara inline, dan tombol hapus dompet dengan proteksi transaksi aktif dan konfirmasi dialog web (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Field `walletId` pada formulir transaksi yang mengingat pilihan dompet terakhir yang dipakai serta rincian saldo per dompet pada ringkasan saldo (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Label nama dompet pada riwayat transaksi serta dropdown filter dompet ("Semua Dompet" atau dompet spesifik) (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Dukungan sinkronisasi daftar dompet ke Google Drive (`appDataFolder`) dengan aturan konflik waktu ubah (`updatedAt`) per dompet terbaru menang, serta toleransi terhadap file cadangan Drive lama (`js/sync.js`).
- Diubah: Kompatibilitas pembacaan transaksi lama tanpa `walletId` yang otomatis dianggap milik "Dompet Utama" tanpa menulis ulang data (`js/app.js`).
- Diubah: Ekspor CSV menyertakan kolom `dompet` setelah kolom `jam`, dan ekspor JSON ikut menyertakan daftar `wallets` (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v11` untuk memperbarui cache aset (`sw.js`).
- Diubah: `PLAN.md` mencentang fitur "Beberapa dompet" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Budget per kategori
- Ditambah: Model data budget per kategori pengeluaran dengan fungsi `getBudgets` dan `saveBudgets` pada store `meta` IndexedDB (`js/db.js`).
- Ditambah: Bagian antarmuka budget per kategori di halaman utama dengan pemilihan bulan (default bulan berjalan, tombol bulan sebelumnya/berikutnya, dan input pemilih bulan), ringkasan total budget, kartu status per kategori (budget, terpakai, dan sisa), serta bar progres dengan tiga status warna (normal <80%, peringatan 80-100%, dan merah >100% dengan teks "Lebih Rp ...") (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Bagian pengaturan budget di bawah pengaturan kategori untuk mengisi, mengubah (inline edit), dan mengosongkan/menghapus budget per kategori pengeluaran dengan konfirmasi dialog web (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Dukungan sinkronisasi data budget ke Google Drive (`appDataFolder`) dengan aturan konflik waktu ubah (`updatedAt`) terbaru menang, serta toleransi terhadap file cadangan Drive lama yang belum memiliki data budget (`js/sync.js`).
- Diubah: Ekspor JSON menyertakan objek `budgets` bersama data transaksi dan kategori (`js/app.js`).
- Diubah: Pertahanan nama budget lama saat kategori pengeluaran dihapus atau diganti nama, tetap dapat dilihat dan dihapus di pengaturan budget (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v10` untuk memperbarui cache aset.
- Diubah: `PLAN.md` mencentang fitur "Budget per kategori" pada Tahap 5.

## 2026-10-11: Tahap 5: Pisah kategori pemasukan dan pengeluaran
- Ditambah: Dua daftar kategori default terpisah untuk pemasukan (Gaji, Bonus, Hadiah, Lainnya) dan pengeluaran (Makan, Transport, Hiburan, Belanja, Tagihan, Lainnya), serta fungsi `getCategories` dan `saveCategories` pada IndexedDB (`js/db.js`).
- Ditambah: Bagian antarmuka pengaturan kategori di halaman web dengan daftar kategori pemasukan dan pengeluaran, formulir penambahan kategori, ubah nama kategori secara inline, dan tombol hapus kategori (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Integrasi kotak dialog konfirmasi web (`<dialog>`) yang sudah ada untuk konfirmasi hapus kategori tanpa `confirm()` bawaan browser (`js/app.js`).
- Ditambah: Dukungan sinkronisasi kategori ke Google Drive dengan aturan konflik waktu ubah (`updatedAt`) terbaru menang, sambil mempertahankan pembacaan file cadangan lama yang belum memiliki data kategori (`js/sync.js`).
- Diubah: Pilihan dropdown kategori pada formulir transaksi mengikuti tipe transaksi yang dipilih (berpindah otomatis saat tipe diganti), serta meminta pemilihan kategori yang sesuai saat mengedit transaksi lama berkategori di luar daftar tipenya tanpa mengubah riwayat transaksi lama (`index.html`, `js/app.js`, `css/style.css`).
- Diubah: Ekspor JSON menyertakan objek `categories` cadangan bersama daftar transaksi (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v9` untuk memperbarui cache aset.
- Diubah: `PLAN.md` mencentang fitur "Pisah kategori pemasukan dan pengeluaran" pada Tahap 5.

## 2026-10-11: Tahap 5: Grafik pengeluaran bulanan
- Ditambah: Bagian antarmuka grafik pengeluaran bulanan dengan judul, teks ringkasan rata-rata, rincian bulan aktif, dan area grafik (`index.html`).
- Ditambah: Visualisasi grafik batang SVG tanpa library luar untuk menampilkan total pengeluaran per bulan, responsif di HP dan PC dengan dukungan scroll horizontal jika banyak bulan (`js/app.js`, `css/style.css`).
- Ditambah: Agregasi data pengeluaran per bulan dari transaksi aktif (mengabaikan transaksi soft delete), perhitungan batas sumbu Y otomatis, dan tampilan pesan kosong saat belum ada pengeluaran (`js/app.js`).
- Ditambah: Interaksi klik, sentuh di HP, dan navigasi keyboard pada batang grafik untuk memeriksa rincian pengeluaran per bulan (`js/app.js`, `css/style.css`).
- Diubah: `sw.js` naik ke versi `mizan-v8` untuk memperbarui cache aset.
- Diubah: `PLAN.md` mencentang fitur "Grafik pengeluaran bulanan" pada Tahap 5.

## 2026-10-10: Tahap 5: Konfirmasi hapus transaksi lewat UI web
- Ditambah: Kotak dialog konfirmasi hapus transaksi di dalam halaman menggunakan elemen `<dialog>` dengan teks "Hapus transaksi ini?", tombol "Hapus", dan tombol "Batal" (`index.html`).
- Ditambah: Gaya untuk `.confirm-dialog` dan `.btn-danger` yang selaras dengan tema, responsif untuk HP dan PC, serta mudah diketuk pada layar kecil (`css/style.css`).
- Diperbaiki: Mengganti pemakaian `confirm()` bawaan browser pada hapus transaksi dengan kotak dialog konfirmasi web (`js/app.js`).
- Diubah: Penutupan kotak konfirmasi tanpa menghapus saat menekan tombol "Batal", menekan tombol Escape, atau mengetuk area di luar kotak (`js/app.js`).
- Diubah: Tombol "Hapus" pada dialog menjalankan logika soft delete yang sudah ada lalu menutup kotak (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v7` untuk memperbarui cache aset.
- Diubah: `PLAN.md` menambahkan dan mencentang perbaikan "konfirmasi hapus lewat UI web (ganti confirm bawaan browser)" pada Tahap 5.

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
