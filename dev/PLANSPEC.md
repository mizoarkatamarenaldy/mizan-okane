# PLANSPEC.md: mizan-okane

Spesifikasi lengkap tiap item di daftar Tahap PLAN.md.

## Tahap 1: Lokal dulu
(tanpa Drive)
- Form tambah transaksi (tanggal, jumlah, kategori, catatan, tipe)
- Daftar transaksi dan total saldo
- Edit dan hapus transaksi
- Simpan di IndexedDB
- Tampilan rapi di HP dan PC

## Tahap 2: Jadi PWA
- Bisa di-install ke homescreen
- Bisa dibuka offline
- Deploy ke GitHub Pages, tes dari HP

## Tahap 3: Login Google dan sinkron Drive
- Buat project Google Cloud, aktifkan Drive API, buat OAuth Client ID
- Tombol login Google
- Simpan dan ambil JSON dari `appDataFolder`
- Sinkron otomatis saat aplikasi dibuka dan saat ada perubahan

## Tahap 4: Aturan konflik data
- ID unik + waktu ubah per transaksi
- Gabungkan data dua perangkat, yang terbaru menang
- Soft delete
- Tes: ubah data di HP dan PC saat offline, lalu online-kan keduanya

## Tahap 5: Fitur tambahan
(pilih sesuai kebutuhan)

## Ekspor CSV
Ekspor CSV

## Jam:menit pada transaksi
(field opsional `time` format "HH:mm", form otomatis terisi jam sekarang, transaksi lama tetap aman)

## Perbaikan: login Google lebih mulus
(tanpa popup terblokir)

## Perbaikan: konfirmasi hapus lewat UI web
(ganti confirm bawaan browser)

## Pisah kategori pemasukan dan pengeluaran
(dua daftar kategori terpisah, bisa diatur lewat pengaturan, ikut sinkron)

## Budget per kategori
Budget per kategori

## Grafik pengeluaran bulanan
Grafik pengeluaran bulanan

## Beberapa dompet
Beberapa dompet

## Rollover budget
(opsional per kategori)

## Transfer antar dompet
(pindah uang dari satu dompet ke dompet lain, tidak dihitung sebagai pemasukan atau pengeluaran, saldo kedua dompet ikut berubah, ikut sinkron, tidak masuk budget dan grafik pengeluaran)

## Ganti label "Jam (opsional)" jadi "Waktu"
(hanya teks label di form, field tetap boleh kosong, nama kolom CSV `jam` tidak diubah)

## Pilihan format jam 12 jam (AM/PM) atau 24 jam
(pengaturan per perangkat, data tetap disimpan "HH:mm" 24 jam, hanya input dan tampilan yang berubah)

## Perbaikan: hapus tombol "Kosongkan" di field Waktu
(hanya elemen UI dan kodenya, field dan data tidak diubah)

## Perbaikan: field Waktu wajib diisi di form tambah dan edit
(data lama tanpa waktu tetap aman ditampilkan)

## Perbaikan: angka uang pakai titik pemisah ribuan
(di input dan di semua tampilan, data tetap disimpan sebagai angka biasa)

## Grafik pemasukan bulanan
(bisa dipilih lewat toggle Pengeluaran / Pemasukan, transfer antar dompet tidak dihitung)

## Diagram donut per kategori untuk bulan yang dipilih
(pilihan pemasukan atau pengeluaran, transfer antar dompet tidak dihitung)

## Mode tanpa login
(cek semua fitur jalan tanpa login Google, status sinkron netral "Belum terhubung", tidak ada pesan error)

## Impor JSON
(pulihkan data dari file ekspor, gabung dengan aturan updatedAt terbaru menang, transaksi lama tanpa walletId tetap aman)

## Mode gelap
(dark mode) dengan pilihan Ikuti sistem / Terang / Gelap di Pengaturan

## Halaman kebijakan privasi
(satu halaman statis di GitHub Pages, ditautkan dari aplikasi)

## Halaman syarat dan ketentuan
(satu halaman statis di GitHub Pages, ditautkan dari aplikasi di samping tautan kebijakan privasi)

## Perbaikan UI/UX multi halaman
(dikerjakan paling akhir, butuh peta halaman dulu)

## APK Item B: bungkus jadi APK dengan Capacitor
(package ID com.mizoar.mizanokane, nama app "Mizan Okane", webDir www; file web masuk ke APK, offline tanpa bergantung GitHub Pages). Folder www adalah salinan hasil skrip build:www dan masuk .gitignore, root repo tetap jadi sumber kode. Build APK release, keystore, dan password dikerjakan pemilik di Android Studio; agent tidak menyentuh keystore atau password. File yang termasuk tahap ini: folder android/, package.json, package-lock.json, scripts/copy-www.js, capacitor.config.json, .gitignore. Tidak ada keystore, token, atau data asli di repo.

## Perbaikan APK: tombol back Android
(di halaman dalam, back kembali ke halaman sebelumnya, bukan keluar app; dari halaman Kebijakan Privasi dan Syarat dan Ketentuan, back kembali ke Pengaturan; di halaman utama, back keluar app; perilaku versi web/PWA tidak berubah; cara teknisnya ditentukan di Implementation Plan)

## Perbaikan APK: ekspor CSV dan ekspor JSON berfungsi di APK
(file tersimpan atau bisa dibagikan lewat share sheet Android, dan ada notifikasi sukses atau gagal di UI; pakai plugin Capacitor Filesystem dan Share; versi web tetap memakai cara unduh yang sekarang; nama kolom CSV dan format JSON tidak berubah; ekspor tetap jalan tanpa login Google)

## Desain ulang tampilan dengan skill anti-slop
(hanya tampilan: warna, tipografi, spasi, layout, komponen; tidak mengubah logika, data, atau fitur)

## Desain ulang v2 mengikuti DESIGN.md
(hanya tampilan; tidak mengubah logika, data, sinkron, atau fitur; perubahan markup dan JS untuk keperluan tampilan boleh; file yang boleh disentuh: css/style.css, index.html, privasi.html, syarat.html, js/app.js hanya bagian tampilan, sw.js hanya naik versi, dan DESIGN.md; dikerjakan satu sub-item per sesi; dicentang setelah semua sub-itemnya tercentang)

## v2.0: Taruh DESIGN.md di root repo, terapkan token warna dan tipografi lewat variabel CSS, lalu pastikan tampilan benar-benar berubah di web dan APK
(cek cache service worker)

## v2.1: Header tipis, navigasi
(bar bawah di HP, tab atas di PC), tombol tambah mengambang, status sinkron jadi ikon

## v2.2: Beranda
(Total Saldo hero, ringkasan bulan, dompet geser, pemilih bulan custom berbahasa Indonesia, budget ringkas)

## v2.3: Riwayat dikelompokkan per tanggal dan form tambah/edit transaksi
(bottom sheet di HP)

## v2.4: Statistik, Pengaturan, Kebijakan Privasi, Syarat dan Ketentuan
Statistik, Pengaturan, Kebijakan Privasi, Syarat dan Ketentuan

## v2.5: Cek akhir
(semua teks Indonesia, mode terang dan gelap, tes di HP)

## APK rilis: unggah APK ke GitHub Releases
(dikerjakan pemilik setelah dua perbaikan APK di atas selesai dan APK dibuild ulang; APK tidak dimasukkan ke repo lewat git add)

## APK Item A: login Google native untuk APK
(plugin Google Sign-In untuk Capacitor, OAuth Client ID tipe Android, token Drive `appDataFolder` didapat lewat jalur native; logika sinkron dan aturan konflik tidak berubah; versi web tetap memakai login yang sekarang; dikerjakan setelah APK rilis, dites di HP asli)

## Mata uang per dompet, tahap 1: Rupiah dan Yen
(field `currency` di dompet, nilai "IDR" atau "JPY", dompet lama otomatis IDR; transaksi ikut mata uang dompetnya, tanpa konversi dan tanpa kurs; Total Saldo dan ringkasan bulan tampil satu angka per mata uang; Statistik dan Budget punya pilihan mata uang, default Rupiah, budget hanya Rupiah; transfer hanya antar dompet semata uang; format angka mengikuti mata uang; sinkron, ekspor, dan impor tetap jalan, dompet lama tanpa currency tetap aman; ikut DESIGN.md)

## Mata uang per dompet, tahap 2: tambah USD dengan desimal
(agent cek dulu cara simpan angka; dibulatkan 2 desimal; input dan tampilan mendukung koma desimal hanya untuk USD; data Rupiah dan Yen tidak berubah; format CSV dan JSON tidak berubah)

## Sub kategori
(setiap kategori boleh punya sub kategori, misalnya Makanan > Sarapan, Makan siang, Jajan; sub kategori opsional, transaksi boleh hanya punya kategori utama; berlaku terpisah untuk pemasukan dan pengeluaran, sama seperti kategori; hanya satu tingkat, tidak ada sub dari sub; field opsional `subcategory` di transaksi, transaksi lama tanpa field ini tetap aman; ikut sinkron, ekspor, dan impor; ikut DESIGN.md)
- Pengaturan: kelola sub kategori di bawah tiap kategori (tambah, ubah nama, hapus)
- Form tambah/edit transaksi: setelah pilih kategori, muncul chip sub kategori (opsional, bisa dikosongkan)
- Riwayat: tampil "Kategori > Sub" di baris transaksi
- Budget: tetap per kategori utama, pengeluaran sub kategori dihitung ke kategori induknya
- Statistik dan donut: tetap per kategori utama; sub kategori tampil sebagai rincian saat kategori disentuh
- Hapus sub kategori: transaksi yang memakainya tidak hilang, tampil sebagai kategori utama saja
- Hapus kategori: sub kategorinya ikut terhapus dari daftar, transaksi lama tetap aman
- Ekspor CSV: tambah kolom baru di paling kanan (`subkategori`), kolom lama tidak diubah. Format JSON: tambah field, field lama tidak diubah
- Impor JSON dari backup lama (tanpa subcategory) tetap jalan

## Timbangan
(halaman baru "Timbangan" sesuai nama Mizan; ikut DESIGN.md; hanya tampilan dan hitungan dari data yang sudah ada; tidak ada field data baru; tidak mengubah sinkron, ekspor, impor, atau aturan konflik)
- Timbangan bulan ini: gambar timbangan garis (outline, bukan emoji), sisi kiri pemasukan, sisi kanan pengeluaran, lengan miring sesuai selisih (miring statis, tanpa animasi). Label teks di bawahnya: "Surplus Rp ..." atau "Defisit Rp ...". Ikut pemilih bulan yang sudah ada
- Kalkulator "Timbang sebelum beli": input harga dan pilihan kategori pengeluaran. Hasil: saldo dompet setelah beli (pilih dompet), sisa budget kategori setelah beli (hanya kalau kategori punya budget), timbangan bulan ini setelah beli, dan satu label hasil
- Label hasil: "Aman" kalau sisa budget masih di atas 20%, "Pas" kalau 0 sampai 20%, "Berat" kalau lewat budget atau saldo dompet jadi minus. Kalau kategori tidak punya budget, label hanya melihat saldo dompet dan selisih timbangan. Status selalu ditemani teks, tidak hanya warna
- Kalkulator tidak menyimpan apa pun dan tidak membuat transaksi
- Transfer antar dompet tidak dihitung
- Per mata uang (Rupiah, Yen, USD), default Rupiah, dengan pilihan mata uang seperti di Statistik; budget tetap hanya Rupiah
- Halaman bisa dibuka lewat navigasi atau tautan dari Beranda, ditentukan agent yang paling sesuai DESIGN.md (navigasi bawah di HP tetap 4 item)
- Jalan juga tanpa login Google. Semua teks berbahasa Indonesia. Data kosong tampil sebagai keadaan kosong sesuai DESIGN.md

## Persiapan rilis publik
(dikerjakan paling akhir, karena butuh link unduh, screenshot, dan fitur final; README berisi cara pakai, cara install, catatan peringatan "belum diverifikasi Google", dan cara hapus data; pastikan tidak ada keystore, token, atau data asli di repo)

## Data contoh (dummy)
tombol di Pengaturan untuk mengisi data contoh (sekitar 40 transaksi acak dalam 2 bulan terakhir: pemasukan, pengeluaran, dan transfer, tersebar di dompet dan kategori yang sudah ada). Setiap transaksi contoh diberi penanda field opsional `dummy: true`. Data contoh hanya tinggal di perangkat: tidak diunggah ke Drive, tidak ikut ekspor CSV dan JSON, dan data dari Drive tidak menimpanya. Ada tombol terpisah "Hapus data contoh" yang hanya membuang transaksi bertanda dummy (boleh dibuang langsung, bukan soft delete, karena tidak pernah masuk Drive). Data asli tidak boleh tersentuh. Ada dialog konfirmasi lewat UI web. Jalan juga tanpa login Google. Aturan sinkron dan konflik untuk data asli tidak berubah.

## Hapus semua data
tombol di Pengaturan, di bagian "Zona bahaya". Semua transaksi asli di-soft delete (bukan dibuang), jadi penghapusan ikut sinkron ke perangkat lain dan Drive. Data contoh (dummy) ikut dibuang. Kategori, dompet, budget, dan pengaturan tema tidak ikut terhapus. Konfirmasi dua langkah: dialog peringatan, lalu pengguna mengetik "HAPUS". Dialog menawarkan tombol Ekspor JSON dulu sebagai cadangan. Ada toast sukses atau gagal. Jalan juga tanpa login Google.

## Pengaturan per kategori
(halaman Pengaturan dipecah jadi daftar kelompok, tiap kelompok membuka submenu sendiri; hanya tampilan dan navigasi; tidak mengubah logika, data, sinkron, ekspor, impor, atau aturan konflik; semua fitur Pengaturan yang sudah ada tetap ada dan tetap jalan; ikut DESIGN.md)
- Halaman utama Pengaturan: daftar baris kelompok (ikon garis, nama kelompok, penjelasan satu baris di bawahnya, panah kanan). Tidak ada kartu di dalam kartu.
- Kelompok diatur agent dari isi Pengaturan yang ada sekarang. Usulan: Tampilan (tema, format jam), Kategori, Dompet, Data (ekspor CSV dan JSON, impor JSON, data contoh), Akun dan sinkron, Tentang (kebijakan privasi, syarat dan ketentuan), Zona bahaya (hapus semua data).
- Zona bahaya tampil paling bawah dan terpisah dari kelompok lain.
- Tiap submenu punya header dengan tombol kembali dan judul kelompok. Tombol kembali di header membawa ke daftar Pengaturan.
- Tombol back Android: dari submenu kembali ke daftar Pengaturan, dari daftar Pengaturan ikut aturan back yang sudah ada. Kebijakan Privasi dan Syarat dan Ketentuan tetap kembali ke Pengaturan. Perilaku web/PWA tidak rusak.
- Semua teks berbahasa Indonesia. Target sentuh minimal 44x44px.
- Berjalan juga tanpa login Google.

## Navigasi bulan di kartu saldo Beranda
1. Di kartu saldo Beranda, tambahkan pemilih bulan di atas tulisan "TOTAL SALDO": tombol bulan sebelumnya, nama bulan dan tahun di tengah (contoh: "Oktober 2026"), tombol bulan sesudahnya.
2. Saat pertama dibuka, bulan yang tampil adalah bulan berjalan.
3. "Total Saldo" untuk bulan terpilih = saldo posisi akhir bulan itu, yaitu jumlah semua transaksi dari awal sampai akhir bulan terpilih, dihitung per mata uang (Rupiah, Yen, USD) seperti tampilan sekarang. Transaksi dengan penanda "terhapus" tidak dihitung. Transfer antar dompet tetap diperlakukan seperti perhitungan saldo yang sudah ada.
4. "Pemasukan" dan "Pengeluaran" di kartu yang sama juga mengikuti bulan terpilih.
5. Tombol bulan sesudahnya dinonaktifkan kalau bulan yang tampil sudah bulan berjalan. Tombol bulan sebelumnya tidak dibatasi.
6. Kalau sudah ada fungsi atau komponen pemilih bulan di halaman lain (misalnya Statistik), pakai ulang, jangan buat baru.
7. Bagian "Timbangan Bulan Ini" tidak diubah.
8. Pilihan bulan tidak perlu disimpan, tiap buka aplikasi kembali ke bulan berjalan.
