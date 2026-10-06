# PLAN.md: mizan-okane

Aplikasi money manager pribadi. Jalan di HP dan PC, data sinkron lewat Google Drive.

## Aturan untuk agent (baca ini dulu)
1. Baca file ini sebelum mengerjakan fitur apa pun.
2. Kerjakan SATU tahap per sesi. Jangan lompat ke tahap berikutnya.
3. Bagian "Keputusan terkunci" tidak boleh diubah tanpa izin pemilik.
4. Setelah fitur selesai, tambahkan entri di `CHANGELOG.md` dan centang tahap di bawah.
5. Jangan pernah menaruh data transaksi asli di repo. Repo ini publik, isinya hanya kode dan dokumen.

## Keputusan terkunci
- Bentuk: PWA (web app), bisa di-install di HP dan PC.
- Penyimpanan utama: IndexedDB (lokal, jalan offline).
- Sinkron: file JSON di Google Drive `appDataFolder`, login Google OAuth.
- Tidak ada server pihak ketiga. Data hanya di perangkat dan Drive milik pemilik.
- Konflik data: transaksi punya ID unik dan waktu ubah, yang terbaru menang.
- Transaksi dihapus dengan penanda "terhapus" (soft delete), bukan dibuang langsung.
- Hosting: GitHub Pages.
- Harus ada tombol ekspor JSON manual sebagai cadangan.

## Versi 1: fitur minimal
Catat pemasukan dan pengeluaran, kategori, lihat riwayat. Itu saja.

## Tahap
- [x] **Tahap 1: Lokal dulu (tanpa Drive)**
  - Form tambah transaksi (tanggal, jumlah, kategori, catatan, tipe)
  - Daftar transaksi dan total saldo
  - Edit dan hapus transaksi
  - Simpan di IndexedDB
  - Tampilan rapi di HP dan PC
- [ ] **Tahap 2: Jadi PWA**
  - Bisa di-install ke homescreen
  - Bisa dibuka offline
  - Deploy ke GitHub Pages, tes dari HP
- [ ] **Tahap 3: Login Google dan sinkron Drive**
  - Buat project Google Cloud, aktifkan Drive API, buat OAuth Client ID
  - Tombol login Google
  - Simpan dan ambil JSON dari `appDataFolder`
  - Sinkron otomatis saat aplikasi dibuka dan saat ada perubahan
- [ ] **Tahap 4: Aturan konflik data**
  - ID unik + waktu ubah per transaksi
  - Gabungkan data dua perangkat, yang terbaru menang
  - Soft delete
  - Tes: ubah data di HP dan PC saat offline, lalu online-kan keduanya
- [ ] **Tahap 5: Fitur tambahan (pilih sesuai kebutuhan)**
  - Budget per kategori
  - Grafik pengeluaran bulanan
  - Ekspor CSV
  - Beberapa dompet

## Catatan
- Tambahkan tombol ekspor JSON manual sebelum mengerjakan Tahap 4.
