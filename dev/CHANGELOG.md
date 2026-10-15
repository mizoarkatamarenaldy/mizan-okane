# CHANGELOG

Catat setiap fitur atau perbaikan yang selesai. Entri terbaru di atas.

## Format
```
## YYYY-MM-DD: nama singkat
- Ditambah: ...
- Diperbaiki: ...
- Diubah: ...
```

## 2026-10-15: Logo aplikasi
- Ditambah: Berkas logo sumber `assets/icon-only.png`.
- Ditambah: Berkas ikon web dan PWA baru `icons/favicon-32x32.png` dan `icons/apple-touch-icon.png`.
- Ditambah: Tautan favicon 32x32 dan apple-touch-icon 180x180 pada tag `<head>` di `index.html`.
- Diubah: Memperbarui ikon PWA standar 192x192 dan 512x512 purpose "any" (`icons/icon-192x192.png`, `icons/icon-512x512.png`) serta ikon maskable 192x192 dan 512x512 dengan logo di tengah 80% dan latar belakang warna sudut sumber (`icons/icon-192x192-maskable.png`, `icons/icon-512x512-maskable.png`).
- Diubah: Versi service worker dinaikkan ke `mizan-v44` dan menambahkan ikon favicon serta apple-touch-icon ke daftar precache `ASSETS_TO_CACHE` (`sw.js`).
- Diubah: Menambahkan dan mencentang item di `dev/PLAN.md` serta menambahkan spesifikasi di `dev/PLANSPEC.md`.

## 2026-10-15: Beranda: sembunyikan budget dan dompet bila tidak perlu
- Ditambah: Penanda ID `wallets-section` dan `budget-section` serta kelas bawaan `hidden` pada elemen kartu dompet dan kartu budget di Beranda (`index.html`).
- Ditambah: Referensi DOM `walletsSection` dan `budgetSection` untuk mengatur visibilitas kartu di Beranda secara dinamis (`js/app.js`).
- Diubah: Menyembunyikan deretan kartu dompet di Beranda jika jumlah dompet aktif kurang dari 2 (hanya 1 dompet atau belum ada), dan menampilkannya hanya jika ada 2 dompet atau lebih (`js/app.js`).
- Diubah: Menyembunyikan seluruh kartu Budget di Beranda jika belum ada kategori dengan batas budget yang tersimpan, dan menampilkannya hanya jika minimal satu budget kategori telah diatur (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v43` (`sw.js`).
- Diubah: Menambahkan dan mencentang item di `dev/PLAN.md` serta menambahkan spesifikasi fitur di `dev/PLANSPEC.md`.

## 2026-10-15: Mata uang opsional
- Ditambah: Template prompt untuk instruksi Antigravity (`dev/PROMPT.md`).
- Ditambah: Fungsi `getAvailableCurrencies` untuk mendeteksi ketersediaan mata uang (Rupiah selalu tersedia, Yen jika ada minimal satu dompet Yen aktif yang belum terhapus, Dolar jika ada minimal satu dompet Dolar aktif yang belum terhapus) (`js/app.js`).
- Ditambah: Atribut ID pada elemen pemilih mata uang di kartu budget Beranda (`budget-currency-control`), halaman Statistik (`stats-currency-bar`), dan halaman Timbangan (`timbangan-currency-bar`) (`index.html`).
- Diubah: Menyembunyikan tombol Yen dan Dolar jika pengguna belum memiliki dompet aktif dengan mata uang tersebut, serta menyembunyikan kontrol pemilih mata uang sama sekali jika hanya Rupiah yang tersedia di kartu budget Beranda, halaman Statistik, dan halaman Timbangan (`js/app.js`, `index.html`).
- Diubah: Mengembalikan pilihan mata uang secara otomatis ke Rupiah jika mata uang yang sedang dipilih menjadi tidak tersedia (misalnya saat dompet terakhir dengan mata uang tersebut dihapus) (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v42` (`sw.js`).
- Diubah: Memperbarui aturan kerja larangan perintah build dan alur staging di `dev/RULES.md`, penambahan dan pencentangan item di `dev/PLAN.md`, serta spesifikasi fitur di `dev/PLANSPEC.md`.

## 2026-10-15: Navigasi bulan di kartu saldo Beranda
- Ditambah: Pemilih bulan di kartu saldo Beranda di atas tulisan "TOTAL SALDO" dengan tombol bulan sebelumnya, nama bulan dan tahun di tengah (format Indonesia penuh, contoh: "Oktober 2026"), serta tombol bulan sesudahnya (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Penonaktifan tombol bulan sesudahnya jika bulan yang tampil adalah bulan berjalan, serta tombol bulan sebelumnya yang tidak dibatasi (`js/app.js`).
- Ditambah: Fungsi `getBerandaMonthSummary` untuk menghitung posisi saldo akhir bulan terpilih (akumulasi seluruh transaksi dari awal hingga akhir bulan terpilih) per mata uang (Rupiah, Yen, USD), serta total pemasukan dan pengeluaran khusus pada bulan terpilih dengan mengabaikan transaksi terhapus (`js/app.js`).
- Diubah: Pilihan bulan kembali ke bulan berjalan setiap kali aplikasi dibuka tanpa disimpan ke penyimpanan lokal, serta bagian "Timbangan Bulan Ini" tetap tidak diubah (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v41` (`sw.js`).

## 2026-10-15: Perketat petunjuk baca RULES dan PLANSPEC
- Diubah: Memperbarui baris petunjuk di `dev/PLAN.md` tepat di bawah judul agar mewajibkan membaca `dev/RULES.md` sebelum mengerjakan apa pun serta merujuk spesifikasi ke `dev/PLANSPEC.md` dan desain ke `dev/DESIGN.md`.
- Diubah: Memperbarui aturan kerja di `dev/RULES.md` mengenai kewajiban membaca `dev/RULES.md` dan `dev/PLAN.md` di awal sesi, membaca `dev/PLANSPEC.md` hanya pada item yang dikerjakan, ketentuan satu baris checkbox di `dev/PLAN.md` dengan spek di `dev/PLANSPEC.md`, serta prioritas spek jika berbeda dengan `dev/PLAN.md`.

## 2026-10-15: Rapikan dokumen ke folder dev
- Ditambah: Folder `dev/` sebagai tempat terpusat untuk dokumen rencana, aturan, dan spesifikasi.
- Ditambah: Berkas `dev/RULES.md` memuat aturan kerja dan alur commit/push untuk agent yang dipindahkan dari `PLAN.md` dengan penyesuaian jalur file dan aturan dua tempat untuk penulisan fitur baru.
- Ditambah: Berkas `dev/PLANSPEC.md` memuat spesifikasi lengkap dan sub-poin dari setiap item tahap dengan judul yang sama persis dengan judul singkat di `dev/PLAN.md`.
- Diubah: Memindahkan `PLAN.md`, `CHANGELOG.md`, dan `DESIGN.md` ke folder `dev/` (`dev/PLAN.md`, `dev/CHANGELOG.md`, `dev/DESIGN.md`) menggunakan `git mv`.
- Diubah: Berkas `dev/PLAN.md` dirampingkan hanya menyisakan judul, petunjuk berkas, keputusan terkunci, versi 1, dan daftar tahap ringkas (judul singkat dan checkbox) tanpa mengubah status centang.

## 2026-10-15: Tahap 5: Pengaturan per kategori (submenu)
- Ditambah: Restrukturisasi halaman Pengaturan menjadi daftar kelompok baris rapi dengan ikon garis outline, nama kelompok, penjelasan satu baris di bawahnya, dan panah kanan tanpa kartu bersarang (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Submenu terpisah untuk masing-masing kelompok pengaturan: Tampilan (tema, format jam), Kategori (pengeluaran, pemasukan, sub kategori), Dompet (kelola dompet, mata uang), Budget (batas pengeluaran bulanan), Data (ekspor CSV/JSON, impor JSON, data contoh), Akun dan sinkron (Google Drive), Tentang (kebijakan privasi, syarat dan ketentuan), dan Zona bahaya (hapus semua data) (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Header pada tiap submenu dengan tombol kembali (target sentuh minimal 44x44px) dan judul kelompok yang mengembalikan tampilan ke daftar utama Pengaturan (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Bagian "Zona Bahaya" diletakkan di paling bawah dan terpisah dari kelompok lain dengan penanda warna merah (#FF453A) sesuai DESIGN.md (`index.html`, `css/style.css`).
- Diperbaiki: Penanganan tombol back Android (`window.handleAndroidBack`) dari submenu kembali ke daftar utama Pengaturan, dan dari daftar Pengaturan mengikuti navigasi tab yang sudah ada tanpa merusak perilaku web/PWA (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v40` (`sw.js`).
- Diubah: `PLAN.md` menambahkan dan mencentang item "Pengaturan per kategori" di Tahap 5 (`PLAN.md`).

## 2026-10-15: Tahap 5: Timbangan
- Ditambah: Halaman baru "Timbangan" sesuai nama Mizan yang menampilkan keseimbangan keuangan dan simulasi belanja tanpa mengubah skema data (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Fitur "Timbangan bulan ini" dengan visualisasi timbangan garis outline SVG (non-emoji, miring statis tanpa animasi sesuai selisih pemasukan dan pengeluaran), pemilih bulan, rincian per sisi, dan label status "Surplus Rp ..." atau "Defisit Rp ..." (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Fitur kalkulator "Timbang sebelum beli" untuk menguji dampak belanja terhadap saldo dompet yang dipilih, sisa budget kategori (hanya untuk kategori ber-budget pada mata uang Rupiah), dan estimasi timbangan bulan ini setelah beli (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Label hasil kalkulator "Aman" (>20%), "Pas" (0-20%), atau "Berat" (lewat budget atau saldo minus/defisit) yang selalu disertai teks penjelas status, bukan hanya warna (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Dukungan multi mata uang (Rupiah, Yen, Dolar) pada halaman Timbangan dengan bawaan Rupiah dan pembatasan budget hanya untuk Rupiah (`index.html`, `js/app.js`).
- Ditambah: Akses navigasi tab desktop dan tautan langsung dari Beranda dengan navigasi bawah HP tetap 4 item (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Tampilan keadaan kosong (empty state) berbahasa Indonesia saat belum ada transaksi pada bulan yang dipilih (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v39` (`sw.js`).
- Diubah: `PLAN.md` menambahkan dan mencentang item "Timbangan" di Tahap 5 (`PLAN.md`).

## 2026-10-15: Tahap 5: Sub kategori
- Ditambah: Struktur data `subcategories` pada model kategori di IndexedDB, mendukung sub kategori satu tingkat opsional yang terpisah untuk pemasukan dan pengeluaran (`js/db.js`).
- Ditambah: Antarmuka pengelolaan sub kategori di bawah tiap kategori (tambah, ubah nama, hapus) pada Pengaturan beserta dialog konfirmasi web UI berbahasa Indonesia (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Pemilihan chip sub kategori opsional pada formulir tambah/ubah transaksi yang muncul dinamis setelah kategori dipilih dan dapat dikosongkan (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Tampilan "Kategori > Sub" pada baris transaksi di Riwayat dan Beranda (`js/app.js`).
- Ditambah: Rincian sub kategori pada diagram donut di halaman Statistik saat kategori disentuh atau dipilih (`css/style.css`, `js/app.js`).
- Ditambah: Kolom baru `subkategori` di paling kanan pada ekspor CSV dengan kolom lama tetap tidak berubah (`js/app.js`).
- Ditambah: Dukungan field opsional `subcategory` pada ekspor dan impor cadangan JSON, serta keamanan membaca data cadangan lama tanpa subcategory (`js/app.js`).
- Diubah: Penghapusan sub kategori mempertahankan data transaksi lama dan menampilkannya sebagai kategori utama saja, serta penghapusan kategori ikut menghapus daftar sub kategorinya (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v38` (`sw.js`).
- Diubah: `PLAN.md` menambahkan dan mencentang item "Sub kategori" di Tahap 5 (`PLAN.md`).

## 2026-10-14: Tahap 5: Mata uang per dompet, tahap 2: tambah USD dengan desimal
- Ditambah: Dukungan mata uang "USD" (Dolar) pada dompet di samping IDR dan JPY, serta opsi Dolar pada form tambah dompet di Pengaturan (`index.html`, `js/db.js`, `js/sync.js`, `js/app.js`).
- Ditambah: Format angka mata uang USD (`$ `) dengan pemisah ribuan titik (`.`) dan desimal koma (`,`) 2 digit (contoh `$ 1.234,56`), dengan data disimpan sebagai angka dibulatkan 2 desimal (`js/app.js`).
- Ditambah: Input jumlah mendukung koma desimal hanya untuk USD (maksimal 2 digit desimal di belakang koma, inputmode decimal di HP) dan konversi koma/titik otomatis saat mengetik atau menempel (`js/app.js`).
- Ditambah: Pilihan mata uang Dolar (USD) pada halaman Statistik dan Budget; budget tetap dibatasi hanya untuk Rupiah dengan pesan informatif saat memilih USD (`index.html`, `js/app.js`).
- Ditambah: Generator data contoh (dummy) mendukung nominal acak USD dengan 2 desimal (`js/app.js`).
- Diubah: Logika impor JSON menjaga nilai desimal transaksi USD tanpa membulatkan ke bilangan bulat (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v37` (`sw.js`).
- Diubah: `PLAN.md` mencentang item "Mata uang per dompet, tahap 2: tambah USD dengan desimal" (`PLAN.md`).

## 2026-10-14: Tahap 5: Mata uang per dompet, tahap 1: Rupiah dan Yen
- Ditambah: Field `currency` pada setiap dompet dengan pilihan "IDR" (Rupiah) atau "JPY" (Yen), dengan nilai bawaan dompet lama otomatis IDR (`js/db.js`, `js/sync.js`, `js/app.js`).
- Ditambah: Pilihan mata uang saat menambah dompet baru di Pengaturan serta badge mata uang (IDR / JPY) pada daftar dompet (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Prefix mata uang dinamis pada formulir transaksi (Rp atau ¥) yang menyesuaikan secara otomatis dengan mata uang dompet yang dipilih (`index.html`, `js/app.js`).
- Ditambah: Format angka khusus mata uang Yen (`¥ `) tanpa desimal berdampingan dengan Rupiah (`Rp `) (`js/app.js`).
- Ditambah: Tampilan Total Saldo dan ringkasan bulan (pemasukan/pengeluaran) menampilkan satu angka per mata uang saat terdapat beberapa mata uang aktif (`css/style.css`, `js/app.js`).
- Ditambah: Pilihan mata uang (IDR / JPY) pada halaman Statistik dan Budget dengan nilai bawaan Rupiah; budget pengeluaran dibatasi hanya untuk mata uang Rupiah dan menampilkan pesan informatif saat beralih ke Yen (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Pembatasan ketat transfer hanya antar dompet dengan mata uang yang sama, baik pada dropdown pemilihan dompet tujuan maupun pada validasi simpan transaksi (`js/app.js`).
- Diubah: Generator data contoh (dummy) menyesuaikan besaran nominal dan membatasi transfer antar dompet semata uang (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v36` (`sw.js`).
- Diubah: `PLAN.md` menambahkan item tahap 1 dan 2, lalu mencentang item tahap 1 ("Mata uang per dompet, tahap 1: Rupiah dan Yen") (`PLAN.md`).

## 2026-10-14: Tahap 5: Hapus semua data
- Ditambah: Bagian "Zona Bahaya" di halaman Pengaturan dengan tombol "Hapus Semua Data" yang menggunakan warna merah (#FF453A) dari tabel Warna makna (`index.html`, `css/style.css`).
- Ditambah: Dialog konfirmasi dua langkah (`delete-all-dialog`) berbahasa Indonesia yang memuat peringatan detail, penawaran tombol "Ekspor JSON (Cadangan)", input ketik "HAPUS", dan tombol konfirmasi yang baru aktif setelah pengguna mengetik "HAPUS" (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Logika penghapusan `performDeleteAllData` yang melakukan soft delete pada semua transaksi asli yang belum terhapus dengan `updatedAt` waktu sekarang agar ikut tersinkron ke perangkat lain dan Google Drive, membuang transaksi contoh (dummy) langsung dari IndexedDB tanpa soft delete, serta mempertahankan kategori, dompet, budget, dan tema (`js/app.js`).
- Ditambah: Fungsi `exportBackupJson` untuk mengekspor cadangan JSON yang dapat dijalankan langsung dari dalam dialog konfirmasi sebelum penghapusan maupun melalui tombol di Pengaturan (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v35` (`sw.js`).
- Diubah: `PLAN.md` mencentang entri "Hapus semua data" di Tahap 5 (`PLAN.md`).


## 2026-10-14: Tahap 5: Data contoh (dummy)
- Ditambah: Tombol "Isi Data Contoh" di Pengaturan untuk menghasilkan 40 transaksi acak (pemasukan, pengeluaran, dan transfer) dalam rentang 2 bulan terakhir yang tersebar di dompet dan kategori yang ada, masing-masing dengan penanda `dummy: true` (`index.html`, `js/app.js`).
- Ditambah: Tombol "Hapus Data Contoh" di Pengaturan yang membuang seluruh transaksi bertanda dummy secara langsung dari penyimpanan lokal tanpa soft delete, menjaga data asli tetap utuh (`index.html`, `js/app.js`).
- Ditambah: Dialog konfirmasi web UI (`confirm-dialog`) berbahasa Indonesia untuk konfirmasi pengisian dan penghapusan data contoh (`js/app.js`).
- Diubah: Ekspor CSV dan ekspor JSON mengecualikan transaksi bertanda dummy sehingga data cadangan hanya memuat data asli (`js/app.js`).
- Diubah: Logika sinkronisasi Drive mengabaikan transaksi dummy agar tidak terunggah ke Drive, dan proses penarikan atau penggabungan dari Drive mempertahankan data dummy lokal tanpa menimpanya (`js/sync.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v34` (`sw.js`).
- Diubah: `PLAN.md` menambahkan dua entri baru di Tahap 5 dan mencentang entri "Data contoh (dummy)" (`PLAN.md`).

## 2026-10-14: Tahap 5: Desain ulang v2.5
- Diperbaiki: Seluruh dialog bawaan browser (`alert` dan `confirm`) yang memicu teks/tombol bahasa Inggris diganti dengan sistem notifikasi toast (`showNotification`) dan dialog konfirmasi kustom terintegrasi (`confirm-dialog`) berbahasa Indonesia (`js/app.js`).
- Diperbaiki: Tombol aksi transaksi dan judul modal diseragamkan ke bahasa Indonesia ("Ubah transaksi", "Ubah Transaksi", "Ubah Transfer") (`js/app.js`).
- Diperbaiki: Menghapus seluruh warna hardcoded di luar variabel CSS tema pada navigasi desktop, tombol sinkron header, dan tombol utama/bahaya, serta menambahkan token `--btn-danger-text` (`css/style.css`).
- Diubah: Versi service worker dinaikkan ke `mizan-v33` (`sw.js`).
- Diubah: `PLAN.md` mencentang sub-item "v2.5" dan item induk "Desain ulang v2 mengikuti DESIGN.md" (`PLAN.md`).

## 2026-10-14: Tahap 5: Desain ulang v2.4
- Diubah: Halaman Statistik didesain ulang dengan pemilih bulan kustom berlabel teks Indonesia ("Okt 2026") dan tombol navigasi panah kiri/kanan outline konsisten menggantikan input bulan bawaan browser (`index.html`, `js/app.js`, `css/style.css`).
- Diubah: Grafik bulanan dan diagram donut per kategori menggunakan tombol geser segmen (Pengeluaran / Pemasukan) dengan visual yang selaras dengan formulir transaksi tanpa bayangan dekoratif (`index.html`, `css/style.css`).
- Diubah: Diagram donut dan grafik bulanan hanya menggunakan palet token DESIGN.md (pemasukan hijau accent, pengeluaran merah #FF453A, transfer biru #4A9EFF, warning amber #F5A623, border, surface-raised, text-secondary) disertai rincian label teks, persentase, dan angka uang tabular-nums agar informasi tidak mengandalkan warna semata (`js/app.js`, `css/style.css`).
- Diubah: Keadaan kosong pada grafik bulanan dan diagram donut diseragamkan menjadi satu kalimat singkat dan satu tombol aksi ("Tambah Transaksi") (`js/app.js`, `css/style.css`).
- Diubah: Halaman Pengaturan ditata ulang menjadi kartu per kelompok (Tampilan, Format Jam, Dompet, Kategori Transaksi, Budget Pengeluaran Bulanan, Data dan Akun) tanpa kartu di dalam kartu, dipisahkan garis tipis atau spasi bersih di dalam kartu, serta tombol aksi konsisten (tombol utama/simpan berlatar accent, tombol hapus berbahaya memakai warna pengeluaran berlabel teks jelas) (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Halaman Kebijakan Privasi (`privasi.html`) dan Syarat dan Ketentuan (`syarat.html`) diselaraskan tampilannya dengan token warna DESIGN.md, tipografi, lebar maksimal 640px di tengah, tombol kembali outline SVG, dan dukungan tema terang/gelap tanpa mengubah isi teks aslinya (`privasi.html`, `syarat.html`, `css/style.css`).
- Diubah: Versi service worker dinaikkan ke `mizan-v32` (`sw.js`).
- Diubah: `PLAN.md` mencentang sub-item "v2.4" di bawah "Desain ulang v2 mengikuti DESIGN.md" (`PLAN.md`).

## 2026-10-14: Tahap 5: Desain ulang v2.3
- Diubah: Halaman Riwayat didesain ulang dengan pengelompokan transaksi per tanggal ("Hari ini", "Kemarin", "12 Okt", dst.) disertai total saldo harian (dengan tanda +, −, atau Rp 0) pada header grup (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Tiap baris transaksi menampilkan ikon kategori garis (outline) konsisten tanpa emoji, nama kategori dan catatan, jumlah rata kanan dengan tanda (+ untuk pemasukan hijau accent, − untuk pengeluaran merah, dan transfer biru tanpa tanda dengan label "Transfer"), serta dompet dan waktu pada baris kecil 12px text-secondary, dengan angka uang tabular-nums (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Keadaan kosong (empty state) pada halaman Riwayat diseragamkan menjadi satu kalimat singkat dan satu tombol aksi ("Tambah Transaksi") (`js/app.js`, `css/style.css`).
- Diubah: Formulir tambah/edit transaksi didesain ulang menjadi bottom sheet di HP dan dialog tengah di PC dengan radius 8px, tanpa bayangan, dan tanpa kartu bersarang (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Urutan komponen formulir disusun ulang sesuai DESIGN.md: jumlah (besar, keypad angka inputmode="numeric") → tipe (tombol geser segmen: Pengeluaran / Pemasukan / Transfer) → kategori (chip) → dompet → tanggal dan waktu → catatan → tombol Simpan selebar form dengan warna accent (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Pemilih tanggal dan waktu menggunakan antarmuka kustom berbahasa Indonesia (dropdown hari, nama bulan Indonesia, tahun, serta pemilih jam dan menit 24/12 jam) menggantikan widget native browser yang berbahasa Inggris, dengan format penyimpanan data (YYYY-MM-DD, HH:mm 24 jam, angka numerik) dan aturan validasi waktu wajib diisi tetap dipertahankan (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v31` (`sw.js`).
- Diubah: `PLAN.md` mencentang sub-item "v2.3" di bawah "Desain ulang v2 mengikuti DESIGN.md" (`PLAN.md`).

## 2026-10-14: Tahap 5: Desain ulang v2.2
- Diubah: Halaman Beranda didesain ulang dengan urutan Total Saldo hero di tengah (32px, bold, tabular-nums) diikuti ringkasan pemasukan (+, hijau accent) dan pengeluaran (−, merah) dua kolom kecil (18px, semibold, tabular-nums) tanpa kartu bersarang (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Saldo per dompet dipisahkan menjadi baris kartu geser horizontal ke samping dengan nama dompet (12px abu-abu), saldo tabular-nums, dan dompet bersaldo 0 ditampilkan redup (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Pemilih bulan budget menggunakan navigasi panah kiri/kanan kustom dan label teks Indonesia (contoh "Okt 2026"), sepenuhnya menghapus input bulan native browser (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Budget ringkas per kategori menampilkan nama kategori di kiri, "Rp terpakai dari Rp budget" di kanan, bar progres tipis 6px, serta persentase dan status dinamis (hijau aman, amber >= 80% dengan teks sisa, merah >= 100% dengan teks "Lewat Rp ...") tanpa kartu di dalam kartu (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Daftar transaksi terakhir pada Beranda ditata sebagai baris daftar bergaris pemisah tipis tanpa kartu di dalam kartu (`index.html`, `css/style.css`).
- Diubah: Tanda minus transaksi dan pengeluaran diseragamkan memakai unicode minus ("−") U+2212 (`js/app.js`).
- Diubah: Keadaan kosong (empty state) pada dompet, budget, dan transaksi terakhir diseragamkan menjadi satu kalimat singkat dan satu tombol aksi (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v30` (`sw.js`).
- Diubah: `PLAN.md` mencentang sub-item "v2.2" di bawah "Desain ulang v2 mengikuti DESIGN.md" (`PLAN.md`).

## 2026-10-13: Tahap 5: Desain ulang v2.1
- Ditambah: Ikon status sinkron kecil di header dengan popover penjelasan teks yang hanya muncul saat disentuh atau diklik (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Header aplikasi didesain ulang menjadi tipis (56px) dengan nama app "Mizan Okane" (18px) satu baris di kiri dan ikon status sinkron di kanan (`index.html`, `css/style.css`).
- Diubah: Navigasi utama tetap 4 item (Beranda, Riwayat, Statistik, Pengaturan) menggunakan bar bawah di HP (ikon outline + label) dan tab di atas di PC dengan target sentuh ergonomis minimal 44x44px dan indikator fokus `:focus-visible` jelas (`index.html`, `css/style.css`).
- Diubah: Status sinkronisasi di header memakai variasi warna dan ikon garis: abu-abu saat belum terhubung, centang accent saat tersinkron, dan peringatan amber saat gagal atau butuh perhatian (`js/app.js`, `css/style.css`).
- Diubah: Menghapus banner sinkronisasi menetap pada halaman Beranda (`index.html`, `css/style.css`).
- Diubah: Tombol tambah transaksi melayang (FAB) diposisikan di kanan bawah di atas bar bawah tanpa bayangan dekoratif sesuai panduan `DESIGN.md` (`css/style.css`).
- Diubah: Tata letak menghormati area aman (safe area) Android pada status bar dan gesture bar (`css/style.css`).
- Diubah: Versi service worker dinaikkan ke `mizan-v29` (`sw.js`).
- Diubah: `PLAN.md` mencentang sub-item "v2.1" di bawah "Desain ulang v2 mengikuti DESIGN.md" (`PLAN.md`).

## 2026-10-13: Tahap 5: Desain ulang v2.0
- Ditambah: Menaruh `DESIGN.md` sebagai panduan arah desain aplikasi di root repository (`DESIGN.md`).
- Diubah: Menerapkan token warna palet gelap utama (`#0F1117`, `#181A24`, `#1F2232`, `#2A2D3A`, `#E4E4E8`, `#9194A1`, `#2ECC8F`) dan palet terang opsi (`#F5F5F7`, `#FFFFFF`, `#F0F0F2`, `#D8D8DC`, `#1A1A1E`, `#6B6D78`, `#24A574`) serta warna semantik uang (pemasukan hijau accent, pengeluaran merah `#FF453A`, transfer biru `#4A9EFF`, budget bar) melalui variabel CSS (`css/style.css`).
- Diubah: Menerapkan tipografi `DESIGN.md` (Total Saldo 32px bold, ringkasan 18px semibold, judul kartu 16px semibold, isi 14px, label kecil 12px, judul app di header 18px satu baris, dan angka uang tabular-nums) serta radius 8px untuk kartu/dialog dan tanpa bayangan (`css/style.css`).
- Diubah: Warna tema `theme-color` diselaraskan ke `#0F1117` pada mode gelap dan `#F5F5F7` pada mode terang di seluruh dokumen web dan handler tema (`index.html`, `privasi.html`, `syarat.html`, `js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v28` untuk memastikan pembaruan cache aset di PWA dan APK, serta menyinkronkan aset web ke Capacitor Android (`sw.js`).
- Diubah: `PLAN.md` mencentang sub-item "v2.0" di bawah "Desain ulang v2 mengikuti DESIGN.md" (`PLAN.md`).

## 2026-10-13: Tahap 5: Desain ulang tampilan dengan skill anti-slop
- Diubah: Desain ulang menyeluruh antarmuka web sesuai prinsip anti-slop (dials ENERGY 1 / RHYTHM 2 / MOTION 1) dengan palet finansial tenang (hijau zamrud/pinus `#1b6348` di mode terang dan `#34d399` di mode gelap, latar batu pasir hangat `#f7f6f2` dan slate obsidian `#111417`, tanpa gradien ungu-biru generik atau glow neon) (`css/style.css`, `index.html`, `privasi.html`, `syarat.html`).
- Diubah: Tipografi diperjelas dengan angka tabular (`tabular-nums`) pada saldo, riwayat transaksi, budget, dan statistik agar angka keuangan tersusun rapi secara vertikal (`css/style.css`).
- Diubah: Spasi, ritme layout, dan target sentuh interaktif ditingkatkan ke standar ergonomis minimum 44x44px pada seluruh tombol, tab navigasi, kontrol filter, dan formulir di HP maupun PC (`css/style.css`).
- Diubah: Indikator fokus keyboard (`:focus-visible`) diseragamkan dengan kontras tinggi untuk seluruh kontrol interaktif tanpa menghilangkan outline aksesibilitas (`css/style.css`).
- Diubah: Warna meta `theme-color` diselaraskan ke palet baru di mode terang (`#1b6348`) dan mode gelap (`#111417`) pada seluruh halaman (`index.html`, `privasi.html`, `syarat.html`).
- Diubah: Versi service worker dinaikkan ke `mizan-v27` untuk memperbarui cache aset PWA secara offline (`sw.js`).
- Diubah: `PLAN.md` menambahkan dan mencentang item "Desain ulang tampilan dengan skill anti-slop (hanya tampilan: warna, tipografi, spasi, layout, komponen; tidak mengubah logika, data, atau fitur)" pada Tahap 5 (`PLAN.md`).

## 2026-10-12: Tahap 5: Perbaikan APK: ekspor CSV dan ekspor JSON berfungsi di APK
- Ditambah: Instalasi dan konfigurasi plugin Capacitor Filesystem (`@capacitor/filesystem`) dan Share (`@capacitor/share`) untuk menangani penyimpanan berkas dan berbagi berkas native di Android APK (`package.json`, `package-lock.json`, `android/capacitor.settings.gradle`, `android/app/capacitor.build.gradle`).
- Ditambah: Konfigurasi jalur penyimpanan berkas FileProvider di Android untuk mendukung pembagian berkas dari cache dan files internal (`android/app/src/main/res/xml/file_paths.xml`).
- Ditambah: Komponen notifikasi toast UI (`#toast-notification`) yang responsif untuk HP dan PC dengan dukungan tema terang/gelap serta varian status (sukses, error, peringatan) untuk memberi umpan balik visual langsung kepada pengguna (`index.html`, `css/style.css`, `js/app.js`).
- Diperbaiki: Ekspor JSON dan CSV di APK kini menyimpan berkas ke cache perangkat via `@capacitor/filesystem` lalu membuka share sheet Android via `@capacitor/share` sehingga berkas dapat langsung disimpan ke perangkat atau dibagikan ke aplikasi lain, mengatasi masalah tombol ekspor tidak bereaksi sama sekali di Android WebView (`js/app.js`).
- Diubah: Versi web/PWA tetap mempertahankan alur unduh berkas Blob yang ada, format struktur JSON dan nama kolom header CSV tetap konsisten tanpa perubahan, serta ekspor tetap berfungsi penuh tanpa memerlukan login Google (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v26` untuk memperbarui cache aset PWA secara offline (`sw.js`).
- Diubah: `PLAN.md` mencentang item "Perbaikan APK: ekspor CSV dan ekspor JSON berfungsi di APK" pada Tahap 5 (`PLAN.md`).

## 2026-10-12: Tahap 5: Perbaikan APK: tombol back Android
- Ditambah: Penanganan tombol back Android native via AndroidX `OnBackPressedCallback` pada `MainActivity.java` yang mengarahkan navigasi mundur WebView dan menutup aplikasi secara aman (`finish()`) hanya saat berada di halaman utama (`android/app/src/main/java/com/mizoar/mizanokane/MainActivity.java`).
- Ditambah: Pelacakan riwayat navigasi tab (`tabHistory`) dan fungsi handler global `window.handleAndroidBack` di `js/app.js` untuk menutup dialog konfirmasi hapus atau form transaksi layar sendiri saat tombol back ditekan, serta kembali ke tab sebelumnya hingga ke Beranda (`js/app.js`).
- Diperbaiki: Tautan kembali pada halaman Kebijakan Privasi dan Syarat & Ketentuan yang sebelumnya mengarah ke `index.html` (halaman depan) diperbaiki menjadi `index.html#pengaturan` agar kembali ke tab Pengaturan (`privasi.html`, `syarat.html`).
- Diubah: `sw.js` naik ke versi `mizan-v25` untuk memperbarui cache aset PWA secara offline (`sw.js`).
- Diubah: `PLAN.md` mencentang item "Perbaikan APK: tombol back Android" pada Tahap 5 (`PLAN.md`).

## 2026-10-12: Tahap 5: APK Item B: bungkus jadi APK dengan Capacitor
- Ditambah: Konfigurasi Capacitor untuk membungkus web app menjadi aplikasi Android native (`capacitor.config.json`, `package.json`, `package-lock.json`).
- Ditambah: Proyek Android native Capacitor lengkap dengan package ID `com.mizoar.mizanokane`, nama aplikasi "Mizan Okane", manifest, Gradle wrapper, serta resource ikon dan splash screen (`android/`).
- Ditambah: Skrip `scripts/copy-www.js` dan npm script `build:www` untuk menyalin aset web (`index.html`, `privasi.html`, `syarat.html`, `manifest.webmanifest`, `sw.js`, folder `css`, `icons`, `js`) ke dalam folder `www` sebelum sinkronisasi Capacitor (`scripts/copy-www.js`, `package.json`).
- Diubah: Menambahkan `www/` ke daftar abaikan Git agar folder artefak build web tidak masuk ke repository (`.gitignore`).
- Diubah: `PLAN.md` mencentang item "APK Item B: bungkus jadi APK dengan Capacitor" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Mode gelap
- Ditambah: Bagian pengaturan "Tampilan" pada halaman Pengaturan dengan tiga pilihan tema (Ikuti sistem, Terang, Gelap) yang disimpan lokal di perangkat tanpa sinkron ke Drive (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Dukungan mode gelap menyeluruh berbasis variabel CSS untuk semua komponen aplikasi: bilah navigasi desktop & mobile, kartu ringkasan saldo, formulir tambah/edit, daftar riwayat transaksi, dialog konfirmasi hapus, pengaturan dompet/kategori/budget, serta warna garis, label, sumbu pada grafik bulanan dan diagram donut per kategori (`css/style.css`, `js/app.js`).
- Ditambah: Deteksi preferensi tema perangkat otomatis (`prefers-color-scheme`) saat opsi "Ikuti sistem" aktif yang langsung menyesuaikan tema tanpa reload halaman (`js/app.js`, `privasi.html`, `syarat.html`).
- Ditambah: Pencegahan kilatan tema terang (FOUC) lewat skrip inisialisasi di `<head>` sebelum CSS dimuat dan pembaruan dinamis meta `theme-color` agar bar status HP ikut menyesuaikan tema aktif (`index.html`, `privasi.html`, `syarat.html`).
- Ditambah: Dukungan mode gelap yang serasi pada halaman kebijakan privasi dan syarat & ketentuan yang otomatis mengikuti preferensi tema (`privasi.html`, `syarat.html`, `css/style.css`).
- Diubah: `sw.js` naik ke versi `mizan-v24` untuk memperbarui cache aset PWA secara offline (`sw.js`).
- Diubah: `PLAN.md` mencentang fitur "Mode gelap (dark mode) dengan pilihan Ikuti sistem / Terang / Gelap di Pengaturan" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Perbaikan UI/UX multi halaman
- Ditambah: Struktur antarmuka multi halaman dengan 4 tab utama (Beranda, Riwayat, Statistik, Pengaturan) yang responsif: bilah navigasi bawah (bottom navigation bar) tetap di layar ponsel (HP) dan bilah navigasi atas (top bar) pada layar komputer (PC) (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Halaman Beranda memuat total saldo, pemasukan, pengeluaran, saldo per dompet, ringkasan budget per kategori, 5 transaksi terakhir dengan tombol pintas "Lihat Semua", dan satu baris status sinkronisasi ringkas (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Halaman Riwayat memuat filter dompet dan daftar transaksi lengkap yang tetap mendukung pengeditan dan penghapusan transaksi (`index.html`, `js/app.js`).
- Ditambah: Halaman Statistik memuat grafik pengeluaran/pemasukan bulanan dan diagram donut per kategori (`index.html`, `css/style.css`).
- Ditambah: Halaman Pengaturan memuat pengaturan dompet, pengaturan kategori & budget, pengaturan format jam (24 jam / 12 jam), serta bagian terpadu "Data dan Akun" berisi tombol Login Google, Sinkron, Logout, Ekspor JSON, Impor JSON, Ekspor CSV, Install Aplikasi, dan tautan ke Kebijakan Privasi serta Syarat dan Ketentuan (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Tombol mengambang (Floating Action Button / FAB) '+' untuk membuka formulir transaksi di layar sendiri, yang dipakai bersama untuk tambah dan edit transaksi tanpa mengubah validasi waktu wajib dan format ribuan (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Header lama yang padat tombol digantikan oleh header aplikasi bersih dan navigasi modern (`index.html`, `css/style.css`).
- Diubah: `sw.js` naik ke versi `mizan-v23` untuk memperbarui cache aset PWA secara offline (`sw.js`).
- Diubah: `PLAN.md` mencentang item "Perbaikan UI/UX multi halaman" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Halaman syarat dan ketentuan
- Ditambah: Halaman statis syarat dan ketentuan berbahasa Indonesia (`syarat.html`) dengan tampilan responsif untuk HP dan PC yang memuat ketentuan aplikasi pencatat keuangan pribadi gratis tanpa jaminan (as is), penegasan bukan layanan/nasihat keuangan dan tanggung jawab pengguna, penjelasan penyimpanan data lokal IndexedDB dan Google Drive appDataFolder tanpa server pihak ketiga, tanggung jawab pencadangan data mandiri via ekspor, login Google opsional, larangan penggunaan melanggar hukum, hak pembaruan syarat, tautan ke kebijakan privasi, kontak via GitHub Issues tanpa nama/email asli, dan tanggal pembaruan.
- Ditambah: Tautan ke halaman syarat dan ketentuan pada bagian footer aplikasi bersebelahan dengan tautan kebijakan privasi (`index.html`).
- Diubah: `sw.js` mendaftarkan `syarat.html` ke daftar aset cache dan naik ke versi `mizan-v22` agar halaman dapat diakses secara offline (`sw.js`).
- Diubah: `PLAN.md` menambahkan dan mencentang fitur "Halaman syarat dan ketentuan" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Halaman kebijakan privasi
- Ditambah: Halaman statis kebijakan privasi berbahasa Indonesia (`privasi.html`) yang memuat rincian penyimpanan data di IndexedDB & Google Drive `appDataFolder`, ketiadaan server pihak ketiga, rincian OAuth scope `drive.appdata`, ketiadaan iklan/pelacak/analitik, panduan hapus data & cabut izin akun Google, klausul kepatuhan Google API Services User Data Policy, tautan kontak via GitHub Issues, dan tanggal pembaruan.
- Ditambah: Tautan ke halaman kebijakan privasi pada bagian footer aplikasi (`index.html`).
- Ditambah: Styling halaman kebijakan privasi dan footer aplikasi yang responsif untuk tampilan HP dan PC (`css/style.css`).
- Diubah: `sw.js` mendaftarkan `privasi.html` ke daftar aset cache dan naik ke versi `mizan-v21` agar halaman dapat diakses secara offline (`sw.js`).
- Diubah: `PLAN.md` mencentang fitur "Halaman kebijakan privasi" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Mode tanpa login
- Ditambah: Pengujian otomatis komprehensif mode tanpa login untuk memastikan seluruh alur fitur berjalan normal tanpa akun Google (tambah dompet, tambah kategori pengeluaran dan pemasukan, tambah transaksi, transfer antar dompet, edit transaksi, hapus transaksi soft delete dengan modal konfirmasi, grafik batang bulanan, diagram donut kategori, ekspor CSV, ekspor JSON, dan impor JSON).
- Dipastikan: Status sinkronisasi netral ("Belum login. Data hanya tersimpan di perangkat ini.") tanpa pesan error dan tanpa runtime exception.
- Diubah: `PLAN.md` mencentang fitur "Mode tanpa login" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Impor JSON
- Ditambah: Tombol "Impor JSON" pada bilah sinkronisasi dan elemen input berkas untuk memulihkan data dari file cadangan JSON (`index.html`, `js/app.js`).
- Ditambah: Logika penggabungan data cadangan dengan aturan konflik waktu ubah (`updatedAt`) terbaru menang untuk transaksi, kategori, budget, dan daftar dompet (`js/app.js`).
- Ditambah: Dukungan penuh kompatibilitas transaksi lama tanpa `walletId` yang otomatis aman diimpor dan dipetakan ke Dompet Utama tanpa merusak data (`js/app.js`).
- Ditambah: Validasi struktur data cadangan, sanitasi transaksi, serta notifikasi hasil impor transaksi baru dan transaksi yang diperbarui (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v20` untuk memperbarui cache aset (`sw.js`).
- Diubah: `PLAN.md` mencentang fitur "Impor JSON" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Diagram donut per kategori
- Ditambah: Bagian antarmuka diagram donut per kategori dengan navigasi bulan (tombol bulan sebelumnya/berikutnya dan input bulan) serta toggle pilihan jenis transaksi Pengeluaran atau Pemasukan (`index.html`).
- Ditambah: Visualisasi diagram donut SVG berbasis proporsi kategori untuk bulan yang dipilih dengan lingkaran donut interaktif, label total/kategori di tengah, dan daftar rincian kategori beserta persentase dan nominal uang (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Interaksi klik, hover, dan navigasi keyboard (Tab, Enter, Spasi, Escape) pada segmen donut dan daftar rincian untuk menyorot kategori dan menampilkan detail transaksi (`js/app.js`, `css/style.css`).
- Diubah: Pemfilteran transaksi diagram donut secara ketat mengecualikan transfer antar dompet, transaksi terhapus, transaksi bernilai nol, serta membatasi hanya pada bulan dan jenis transaksi yang dipilih (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v19` untuk memperbarui cache aset (`sw.js`).
- Diubah: `PLAN.md` mencentang fitur diagram donut per kategori untuk bulan yang dipilih pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Angka uang pakai titik pemisah ribuan
- Ditambah: Fungsi pemformat bersama `formatNumber`, `parseAmount`, dan `formatCurrency` untuk menampilkan angka dan nilai uang dengan pemisah ribuan titik sesuai format Indonesia (`js/app.js`).
- Ditambah: Penanganan otomatis pemisah ribuan pada input jumlah uang di form tambah, form edit, form transfer antar dompet, dan form budget (`index.html`, `js/app.js`).
- Ditambah: Penjagaan posisi kursor saat mengetik atau mengedit angka di tengah dan dukungan tempel (paste) nilai uang berformat titik atau dengan simbol lain (`js/app.js`).
- Diubah: Field input uang menggunakan `type="text"` dengan `inputmode="numeric"` agar titik ribuan dapat tampil saat mengetik (`index.html`, `js/app.js`).
- Diubah: Form edit transaksi dan pilihan budget menampilkan jumlah lama yang telah diformat dengan titik ribuan (`js/app.js`).
- Diubah: Tampilan seluruh nominal uang (daftar transaksi, total saldo, saldo per dompet, budget dan sisa budget, label dan tooltip grafik) konsisten menggunakan titik pemisah ribuan (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v18` untuk memperbarui cache aset (`sw.js`).
- Diubah: `PLAN.md` mencatat dan mencentang perbaikan angka uang pakai titik pemisah ribuan pada Tahap 5 (`PLAN.md`).

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
