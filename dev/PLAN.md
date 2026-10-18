# PLAN.md: mizan-okane

WAJIB baca dev/RULES.md dulu sebelum mengerjakan apa pun. Spek lengkap tiap item ada di dev/PLANSPEC.md, desain ada di dev/DESIGN.md.

Aplikasi money manager pribadi. Jalan di HP dan PC, data sinkron lewat Google Drive.

## Keputusan terkunci
- Bentuk: PWA (web app), bisa di-install di HP dan PC.
- Penyimpanan utama: IndexedDB (lokal, jalan offline).
- Sinkron: file JSON di Google Drive `appDataFolder`, login Google OAuth.
- Tidak ada server pihak ketiga. Data hanya di perangkat dan Drive milik pemilik.
- Konflik data: transaksi punya ID unik dan waktu ubah, yang terbaru menang.
- Transaksi dihapus dengan penanda "terhapus" (soft delete), bukan dibuang langsung.
- Hosting: GitHub Pages.
- Harus ada tombol ekspor JSON manual sebagai cadangan.
- APK: dibungkus dengan Capacitor, file web dimasukkan ke dalam APK (tidak membuka situs). Versi PWA di GitHub Pages tetap ada dan tetap jadi sumber kode. Login Google di APK memakai jalur native Android, sedangkan data tetap di Drive `appDataFolder`. Dirilis lewat GitHub Releases, bukan Play Store.

## Versi 1: fitur minimal
Catat pemasukan dan pengeluaran, kategori, lihat riwayat. Itu saja.

## Tahap
- [x] **Tahap 1: Lokal dulu**
- [x] **Tahap 2: Jadi PWA**
- [x] **Tahap 3: Login Google dan sinkron Drive**
- [x] **Tahap 4: Aturan konflik data**
- [x] **Tahap 5: Fitur tambahan**
  - [x] Ekspor CSV
  - [x] Jam:menit pada transaksi
  - [x] Perbaikan: login Google lebih mulus
  - [x] Perbaikan: konfirmasi hapus lewat UI web
  - [x] Pisah kategori pemasukan dan pengeluaran
  - [x] Budget per kategori
  - [x] Grafik pengeluaran bulanan
  - [x] Beberapa dompet
  - [x] Transfer antar dompet
  - [x] Ganti label "Jam (opsional)" jadi "Waktu"
  - [x] Pilihan format jam 12 jam (AM/PM) atau 24 jam
  - [x] Perbaikan: hapus tombol "Kosongkan" di field Waktu
  - [x] Perbaikan: field Waktu wajib diisi di form tambah dan edit
  - [x] Perbaikan: angka uang pakai titik pemisah ribuan
  - [x] Grafik pemasukan bulanan
  - [x] Diagram donut per kategori untuk bulan yang dipilih
  - [x] Mode tanpa login
  - [x] Impor JSON
  - [x] Mode gelap
  - [x] Halaman kebijakan privasi
  - [x] Halaman syarat dan ketentuan
  - [x] Perbaikan UI/UX multi halaman
  - [x] APK Item B: bungkus jadi APK dengan Capacitor
  - [x] Perbaikan APK: tombol back Android
  - [x] Perbaikan APK: ekspor CSV dan ekspor JSON berfungsi di APK
  - [x] Desain ulang tampilan dengan skill anti-slop
  - [x] Desain ulang v2 mengikuti DESIGN.md
    - [x] v2.0: Taruh DESIGN.md di root repo, terapkan token warna dan tipografi lewat variabel CSS, lalu pastikan tampilan benar-benar berubah di web dan APK
    - [x] v2.1: Header tipis, navigasi
    - [x] v2.2: Beranda
    - [x] v2.3: Riwayat dikelompokkan per tanggal dan form tambah/edit transaksi
    - [x] v2.4: Statistik, Pengaturan, Kebijakan Privasi, Syarat dan Ketentuan
    - [x] v2.5: Cek akhir
  - [x] Mata uang per dompet, tahap 1: Rupiah dan Yen
  - [x] Mata uang per dompet, tahap 2: tambah USD dengan desimal
  - [x] Sub kategori
  - [x] Timbangan
  - [x] Data contoh (dummy)
  - [x] Hapus semua data
  - [x] Pengaturan per kategori
  - [x] Navigasi bulan di kartu saldo Beranda
  - [x] Mata uang opsional
  - [x] Beranda: sembunyikan budget dan dompet bila tidak perlu
  - [x] Logo aplikasi
  - [x] Audit logika matematis
  - [x] Backup lengkap
  - [x] Hapus dompet
  - [x] Reset setelan pabrik
  - [x] Data contoh lengkap
  - [x] Klik dompet untuk filter riwayat
  - [x] Filter bulan di Riwayat
  - [x] Warna kartu saldo multi mata uang
  - [x] Pencarian riwayat
  - [x] Catatan di bawah angka
  - [x] Tombol Timbangan di tengah navigasi
  - [x] Tentang Pengembang
  - [x] Dompet dipilih manual di form transaksi dan transfer
  - [x] Geser urutan dompet dan kategori
  - [x] Penyesuaian saldo di Timbangan
  - [x] Perbaikan UI: tombol tambah, banner data contoh, header budget
  - [x] Audit UI: temuan desain dan aksesibilitas
  - [x] Perbaikan UI: target sentuh minimal 44 px
  - [x] Perbaikan UI: semantik dan keyboard
  - [x] Perbaikan UI: polish kecil
  - [x] Perbaikan UI: grafik bulanan Statistik
  - [x] Ikon neraca di header
  - [x] Statistik: satu bulan terpilih
  - [x] Statistik: tab Harian, Bulanan, Tahunan
  - [x] Geser urutan budget
  - [x] Budget per sub kategori
  - [x] Ikon kategori kustom
  - [x] Ikon sub kategori
  - [x] Perbaikan: dialog Pilih Ikon muncul sendiri di Pengaturan
  - [x] Kategori Equilibrium

## Rencana setelah Tahap 5
Kerjakan berurutan dari atas. Item yang menyentuh "Keputusan terkunci" butuh prompt dengan klausa "Izin dari pemilik".
- [x] Pemisahan folder
- [x] Revisi UI PC
- [x] Perbaikan UI PC: Riwayat dan Timbangan
- [x] Perbaikan UI PC 2A: CSS Timbangan
- [x] Perbaikan UI PC 3: Beranda
- [x] Perbaikan UI PC 4: Timbangan tanpa scroll
- [x] Perbaikan UI PC 5: Detail diagram Statistik lewat klik
- [ ] Konversi Tauri
- [ ] Konversi Kotlin
- [ ] APK rilis
- [ ] APK Item A: login Google native
- [ ] Persiapan rilis publik

## Ditunda
Tidak dikerjakan sampai pemilik memintanya.
- [ ] Rollover budget