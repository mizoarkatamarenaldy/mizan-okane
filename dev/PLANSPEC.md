# PLANSPEC v0.3.0 (sedang dikerjakan)

## Susun ulang dokumen dev per versi

Tujuan: Memecah CHANGELOG dan PLANSPEC menjadi per versi rilis supaya file aktif kecil dan agent tidak cepat kehabisan kuota. Item ini hanya mengubah dokumen di dev/, tidak menyentuh kode.

Tempat dan tampilan:
- Tidak ada elemen tampilan baru.

Izin dari pemilik:
- Item ini boleh memindahkan entri lama di dev/CHANGELOG.md dan bagian lama di dev/PLANSPEC.md, termasuk isi dev/arsip/, tanpa mengubah teksnya. Aturan "Jangan ubah entri lama" hanya dilonggarkan untuk pemindahan.
- Item ini boleh membaca seluruh isi dev/arsip/ dan boleh mengubah dev/RULES.md hanya pada bagian yang disebut di bawah.
- Item ini boleh membuat lebih dari satu commit, sesuai urutan di bawah.

Struktur akhir:
- dev/arsip/v0.1.0/CHANGELOG.md dan PLANSPEC.md: semua entri dan spek sampai rilis v0.1.0.
- dev/arsip/v0.2.0/CHANGELOG.md dan PLANSPEC.md: semua entri dan spek setelah v0.1.0 sampai rilis v0.2.0.
- dev/CHANGELOG.md dan dev/PLANSPEC.md tetap ada sebagai file aktif untuk versi berikutnya. Baris pertama masing-masing berjudul "CHANGELOG v0.3.0 (sedang dikerjakan)" dan "PLANSPEC v0.3.0 (sedang dikerjakan)". Format entri tidak berubah. Isinya hanya entri dan spek yang dibuat setelah tag v0.2.0, termasuk spek item ini.
- Setelah selesai, tidak boleh ada file arsip di luar folder versi.

Cara menentukan versi:
- Batasnya adalah tag git v0.1.0 dan v0.2.0. Lihat dengan `git tag` dan `git log`, hanya membaca. Dilarang membuat, mengubah, atau menghapus tag. Kalau salah satu tag tidak ada, berhenti dan laporkan.
- Entri CHANGELOG sampai commit tag v0.1.0 masuk v0.1.0. Entri sesudahnya sampai commit tag v0.2.0 masuk v0.2.0. Entri sesudah tag v0.2.0 tetap di file aktif. Pakai hash commit atau tanggal di entri.
- Bagian PLANSPEC mengikuti versi entri CHANGELOG yang namanya sama dengan judul bagian itu. Spek item ini sendiri tetap di file aktif.
- Entri atau bagian yang versinya tidak bisa ditentukan: jangan menebak. Taruh di paling bawah file arsip v0.1.0 di bawah judul "Belum dipastikan" dan sebutkan di laporan.
- Urutan di dalam tiap file tidak berubah. Di CHANGELOG entri terbaru di atas, di PLANSPEC bagian terbaru di bawah.

Urutan commit (judul commit memakai format "dev: nama singkat" untuk item ini, dua baris trailer tetap):
1. `git mv` file CHANGELOG di dev/arsip/ ke dev/arsip/v0.1.0/CHANGELOG.md. Perbarui path di dev/RULES.md kalau disebut, di commit yang sama.
2. `git mv` file PLANSPEC di dev/arsip/ ke dev/arsip/v0.1.0/PLANSPEC.md. Perbarui path di dev/RULES.md kalau disebut, di commit yang sama.
3. `git mv` dev/CHANGELOG.md ke dev/arsip/v0.2.0/CHANGELOG.md.
4. `git mv` dev/PLANSPEC.md ke dev/arsip/v0.2.0/PLANSPEC.md.
5. Pecah isi menurut aturan versi di atas. Bagian setelah tag v0.1.0 dari kedua file v0.1.0 dipindah ke file v0.2.0: di CHANGELOG ditempel di bawah entri yang sudah ada, di PLANSPEC ditempel di atas bagian yang sudah ada. Entri dan spek setelah tag v0.2.0 dikeluarkan dari file v0.2.0. Lalu buat dev/CHANGELOG.md dan dev/PLANSPEC.md baru untuk v0.3.0 yang berisi judul versi dan isi yang dikeluarkan tadi.
6. Rapikan dev/PLAN.md dan perbarui dev/RULES.md sesuai dua bagian di bawah.

Merapikan dev/PLAN.md:
- Bagi menjadi "## v0.1.0 (dirilis 22 Okt 2026)", "## v0.2.0 (dirilis 6 Nov 2026)", dan "## v0.3.0 (sedang dikerjakan)". Item yang selesai sampai tag v0.1.0 masuk bagian v0.1.0. Item yang selesai setelahnya sampai tag v0.2.0 masuk bagian v0.2.0. Item yang belum tercentang, termasuk item baru ini, masuk bagian v0.3.0. Gunakan versi hasil langkah penentuan di atas.
- Teks tiap item tidak boleh diubah, karena judulnya harus sama dengan judul spek. Hanya posisinya yang pindah.
- Sub-item yang dikerjakan di versi lain dari induknya boleh dipindah ke bagian versinya sebagai item sendiri. Item induk "Konversi Kotlin" dicentang kalau semua sub-itemnya sudah tercentang.
- Di dalam bagian v0.1.0, daftar Tahap 5 boleh diberi sub judul tema. Tiap item tetap satu baris checkbox.
- Tambahkan satu baris di bawah judul PLAN.md: "Spek tiap versi yang sudah rilis ada di dev/arsip/vX.Y.Z/PLANSPEC.md".
- Seragamkan penebalan teks: hapus tanda ** dari judul item di daftar.
- Bagian "Keputusan terkunci" dan "Ditunda" tidak berubah.
- Item baru ke depan selalu ditambah di paling bawah bagian versi yang sedang dikerjakan.

Mengubah dev/RULES.md (hanya empat hal ini):
- Kalimat tentang dev/arsip/ diganti: arsip ada di dev/arsip/vX.Y.Z/, tidak dibaca kecuali Mizan memintanya atau fitur yang dikerjakan menyentuh fitur lama; dalam kasus itu buka hanya bagian yang terkait.
- Tambahkan aturan rilis, berlaku hanya kalau Mizo memintanya lewat prompt: pindahkan dev/CHANGELOG.md lalu dev/PLANSPEC.md ke dev/arsip/vX.Y.Z/ dengan `git mv`, satu pemindahan per commit. Lalu buat file aktif baru berjudul versi berikutnya, berisi hanya entri dan spek setelah tag rilis itu. Agent tidak boleh memutuskan sendiri kapan rilis.
- Sebutan "Tahap 5" dan "Rencana setelah Tahap 5" diganti sesuai judul baru di PLAN.md. Aturan "SATU fitur atau item per sesi" berlaku untuk semua item, bukan hanya Tahap 5.
- Kalimat "Tahap 5 baru dicentang setelah semua fiturnya tercentang" diganti jadi: item induk baru dicentang setelah semua sub-itemnya tercentang.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Jalankan `git ls-files dev/`. Harus ada dev/arsip/v0.1.0/ dan dev/arsip/v0.2.0/ masing-masing berisi CHANGELOG.md dan PLANSPEC.md, ditambah dev/CHANGELOG.md dan dev/PLANSPEC.md aktif, dan tidak ada file arsip lain.
- Hitung jumlah entri CHANGELOG dan jumlah bagian PLANSPEC (baris berawalan "## ") sebelum dan sesudah pemecahan. Jumlah gabungan keempat file arsip dan file aktif sesudahnya harus sama dengan sebelumnya. Tulis angkanya di laporan.
- Hitung baris checkbox di dev/PLAN.md sebelum dan sesudah. Jumlahnya sama, selain satu item baru ini dan sub-item yang dipindah.
- Cari kata "Tahap 5" di dev/RULES.md. Tidak boleh tersisa.
- `git status --short` bersih dan tidak ada perubahan di luar dev/.
