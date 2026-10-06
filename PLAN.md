# PLAN.md: mizan-okane

Aplikasi money manager pribadi. Jalan di HP dan PC, data sinkron lewat Google Drive.

## Aturan untuk agent (baca ini dulu)

### Aturan kerja
- Baca PLAN.md sebelum mengerjakan apa pun.
- Kerjakan SATU tahap per sesi. Jangan lompat ke tahap berikutnya.
- Jangan ubah file di luar tahap yang sedang dikerjakan.
- Bagian "Keputusan terkunci" tidak boleh diubah tanpa izin pemilik.
- Jangan pernah menaruh data transaksi asli, token, kunci, atau file `.env` di repo. Repo ini publik, isinya hanya kode dan dokumen.
- Di akhir tahap, centang tahap di PLAN.md dan tambahkan satu entri di paling atas `CHANGELOG.md` sesuai format di file itu. Jangan ubah entri lama. Kalau tidak yakin tanggalnya, tulis "tidak tercatat", jangan menebak.
- Di awal sesi, baca hanya 5 entri paling atas CHANGELOG.md, jangan seluruh file.

### Alur commit dan push
Sebelum mulai kerja, jalankan `git branch --show-current`. Hasilnya harus `main`. Kalau bukan, berhenti: jangan pindah branch, jangan buat branch, jangan commit, jangan push. Laporkan nama branch yang aktif ke pemilik. Kalau selama sesi muncul branch atau worktree baru yang dibuat otomatis oleh tool, berhenti dan laporkan juga, jangan push.

Setelah tahap selesai:
1. Jalankan `git status --short`. Kalau ada file yang muncul tapi bukan bagian dari tahap ini (selain PLAN.md dan CHANGELOG.md), berhenti: jangan commit, jangan push, laporkan ke pemilik.
2. Tambahkan entri di CHANGELOG.md. Cantumkan file yang diubah berdasarkan hasil `git status --short`.
3. Stage file dengan menyebut namanya satu per satu. Dilarang `git add -A` dan `git add .`
4. Commit dengan pesan lengkap: judul singkat dengan format "tahap N: nama singkat", isi penjelasan, lalu dua baris trailer di bawah, dipisah satu baris kosong dari isi pesan.

   Co-authored-by: Claude <noreply@anthropic.com>
   Co-authored-by: Gemini (Antigravity) <200291788+gemini-code-assist@users.noreply.github.com>
5. Jalankan `git push` ke branch yang sedang aktif. Dilarang force push, dilarang membuat atau menghapus branch, dilarang mengubah git config.
6. Kalau commit atau push gagal, jangan coba cara lain. Berhenti dan laporkan error-nya ke pemilik.
7. Di akhir, kabari file apa saja yang diubah dan hash commit-nya.

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
