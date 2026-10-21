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
Catatan: ditunda sampai Konversi Kotlin selesai. Yang dirilis nanti APK hasil Kotlin, bukan APK Capacitor. Login Google native dikerjakan sebagai bagian Konversi Kotlin.
(dikerjakan pemilik setelah dua perbaikan APK di atas selesai dan APK dibuild ulang; APK tidak dimasukkan ke repo lewat git add)

## APK Item A: login Google native untuk APK
Catatan: ditunda sampai Konversi Kotlin selesai. Yang dirilis nanti APK hasil Kotlin, bukan APK Capacitor. Login Google native dikerjakan sebagai bagian Konversi Kotlin.
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

## Mata uang opsional

Tujuan: Yen dan Dolar tidak muncul kalau pengguna belum punya dompet dengan mata uang itu.

Aturan:
- Rupiah selalu tersedia.
- Yen tampil hanya kalau ada minimal satu dompet Yen yang belum terhapus.
- Dolar tampil hanya kalau ada minimal satu dompet Dolar yang belum terhapus.
- Dompet yang sudah ditandai "terhapus" tidak dihitung.
- Berlaku di semua tempat yang punya pilihan mata uang: kartu Budget di Beranda, dan tempat lain yang punya tombol Rupiah/Yen/Dolar. Cari tempat-tempat itu di kode.
- Kalau hanya Rupiah yang tersedia, tombol pemilih mata uang disembunyikan sama sekali.
- Kalau mata uang yang sedang dipilih jadi tidak tersedia (misalnya dompet Yen terakhir dihapus), otomatis kembali ke Rupiah.
- Form buat dompet tetap menawarkan Rupiah, Yen, dan Dolar. Dari sinilah pengguna mengaktifkan mata uang baru.
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.
- Tidak ada elemen tampilan baru, jadi tidak ada warna, font, atau animasi baru di luar dev/DESIGN.md.

Cek manual:
- Data baru tanpa dompet Yen/Dolar: pemilih mata uang tidak muncul.
- Buat dompet Yen: tab Yen muncul. Hapus dompet itu: tab hilang dan tampilan kembali ke Rupiah.
- Buat dompet Dolar: tab Dolar muncul.

## Beranda: sembunyikan budget dan dompet bila tidak perlu

Tujuan: Beranda tidak menampilkan elemen yang tidak berguna. Kartu Budget tidak muncul kalau belum ada budget, dan deretan dompet tidak muncul kalau dompetnya cuma satu.

Tempat dan tampilan:
- Halaman Beranda, kartu "Budget" (judul, navigasi bulan, teks "Belum ada budget yang diatur", tombol "Atur Budget").
- Halaman Beranda, deretan kartu kecil dompet di bawah kartu saldo (contoh: "Cash Rp 0").
- Halaman Pengaturan: kalau halaman "Budget" (tempat memilih kategori dan menyimpan batas bulanan) belum bisa dibuka dari Pengaturan, tambahkan satu baris menu "Budget" yang membuka halaman itu. Pakai gaya baris menu yang sudah ada di Pengaturan. Kalau sudah ada, jangan ubah apa pun di Pengaturan.

Aturan:
- Kartu Budget hanya tampil kalau ada minimal satu budget kategori yang tersimpan. Kalau belum ada, seluruh kartu (termasuk teks kosong dan tombol "Atur Budget") disembunyikan. Karena budget berlaku setiap bulan, aturan ini tidak bergantung pada bulan yang dilihat.
- Halaman Budget harus selalu bisa dibuka dari Pengaturan, supaya budget pertama tetap bisa ditambahkan saat kartu Beranda tersembunyi.
- Deretan kartu dompet di Beranda hanya tampil kalau jumlah dompet 2 atau lebih. Kalau cuma 1 dompet, deretannya disembunyikan.
- Kartu saldo, tombol "Timbangan Bulan Ini", dan elemen Beranda lainnya tidak berubah.
- Halaman lain (Statistik, Riwayat) tidak diubah.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Belum ada budget sama sekali: buka Beranda, kartu Budget tidak muncul. Buka Pengaturan, halaman Budget bisa dibuka.
- Tambah satu budget dari halaman Budget, kembali ke Beranda: kartu Budget muncul. Pindah bulan: kartu tetap tampil.
- Hapus semua budget: kartu Budget hilang lagi.
- Punya 1 dompet: Beranda tidak menampilkan deretan kartu dompet.
- Tambah dompet kedua: deretan kartu dompet muncul. Hapus sampai tersisa 1: deretan hilang lagi.

## Logo aplikasi

Tujuan: Memasang logo aplikasi di sisi web (PWA) supaya ikon install, tab browser, dan layar utama HP tidak lagi memakai ikon bawaan. Ikon APK dikerjakan pemilik secara manual dan bukan bagian item ini.

Tempat dan tampilan:
- Tidak ada elemen tampilan baru di dalam aplikasi. Perubahan hanya pada ikon PWA, favicon, dan ikon layar utama iOS.

Aturan:
- Gambar sumber ada di assets/icon-only.png (PNG 1024x1024, sudah disiapkan pemilik). Jangan diubah atau ditimpa.
- Buat ikon PNG ukuran 192x192 dan 512x512 untuk purpose "any".
- Buat ikon maskable ukuran 192x192 dan 512x512. Logo diperkecil sekitar 80% dan ditaruh di tengah, sisanya diisi warna latar yang diambil dari piksel sudut gambar sumber.
- Buat favicon PNG 32x32 dan apple-touch-icon 180x180, lalu tautkan keduanya di index.html.
- Daftarkan semua ikon di manifest. Kalau sudah ada entri ikon lama, ganti dengan yang baru.
- Naikkan versi cache service worker. Kalau service worker punya daftar file precache, masukkan file ikon baru ke daftar itu.
- Cari sendiri lokasi manifest, index.html, dan service worker di folder sumber web. Jangan edit www/.
- Untuk membuat ukuran-ukuran itu, pakai alat yang sudah ada di komputer atau perintah sekali jalan. Jangan tambah dependency di package.json dan jangan commit skrip pembuatnya.
- Jangan sentuh android/. Jangan jalankan build apa pun.
- Folder assets/ dan file ikon baru adalah bagian dari item ini, jadi boleh di-stage satu per satu lewat git add sesuai alur RULES.md.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka situs di browser, lihat tab: favicon baru muncul (muat ulang keras kalau masih ikon lama).
- Di DevTools tab Application > Manifest: semua ikon terdaftar tanpa error, dan pratinjau maskable tidak terpotong.
- Di HP atau PC yang sudah install PWA lama: setelah dibuka dua kali, ikon dan cache ter-update ke versi baru.

## Audit logika matematis

Tujuan: Memeriksa semua perhitungan angka di aplikasi supaya hasilnya benar dan konsisten, lalu memperbaiki kesalahan yang jelas. Pemilik paling sering memakai Rupiah dan merasa ada yang janggal, jadi Rupiah diperiksa paling teliti. Aplikasi keuangan tidak boleh salah hitung.

Tempat dan tampilan:
- Tidak ada elemen tampilan baru.

Aturan:
- Buka hanya bagian "Mata uang per dompet", "Transfer antar dompet", "Budget per kategori", dan "Timbangan" di dev/PLANSPEC.md sebagai acuan perilaku yang benar.
- Periksa kode di bagian berikut:
  1. Saldo: saldo dompet dan saldo total per bulan (pemasukan dikurangi pengeluaran). Transaksi berpenanda "terhapus" tidak boleh ikut dihitung.
  2. Transfer antar dompet: total semua dompet tidak berubah akibat transfer, dan transfer tidak dihitung sebagai pemasukan atau pengeluaran di statistik.
  3. Multi mata uang, dengan prioritas utama Rupiah, lalu Yen, lalu USD. Periksa:
     - Rupiah dan Yen: tanpa desimal. Angka disimpan dan dijumlahkan sebagai bilangan bulat, tidak ada ekor pecahan.
     - Input angka dengan titik pemisah ribuan: "1.500.000" harus terbaca 1500000, bukan 1,5. Periksa juga saat edit transaksi lama (angka ditampilkan dengan titik lalu dibaca ulang), saat tempel angka, dan saat ada koma.
     - Angka besar (miliaran) tidak rusak, tidak berubah jadi notasi ilmiah, dan tidak tampil NaN.
     - Saldo negatif tampil dan terhitung benar.
     - USD: desimal tidak punya ekor aneh (misalnya 0,1 + 0,2).
     - Mata uang yang berbeda tidak boleh dijumlahkan jadi satu angka.
  4. Budget per kategori: sisa budget, persentase terpakai, kategori dengan sub kategori, dan kasus budget bernilai 0 (tidak boleh ada pembagian dengan nol).
  5. Grafik bulanan dan diagram donut: jumlah per bulan benar, persentase donut berjumlah 100 persen, dan bulan tanpa data tidak membuat error atau NaN.
  6. Batas bulan: transaksi tanggal 1 pukul 00:00 dan tanggal terakhir pukul 23:59 masuk ke bulan yang benar, termasuk pada format jam 12 jam dan 24 jam, serta tidak bergeser karena zona waktu.
  7. Timbangan: perhitungannya sesuai spek bagian "Timbangan".
  8. Impor JSON dan ekspor CSV/JSON: angka yang diekspor lalu diimpor kembali hasilnya sama persis.
- Kalau menemukan kesalahan yang jelas dan perbaikannya kecil, langsung perbaiki.
- Kalau temuan butuh perubahan struktur data atau keputusan perilaku, jangan diperbaiki. Catat saja di laporan akhir.
- Jangan membuat file baru di repo. Jangan menambah fitur. Jangan merapikan kode yang tidak berkaitan dengan perhitungan.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Rupiah: buat transaksi 1.500.000, 25.000, dan 750. Saldo harus tepat 1.525.750. Buka transaksi 1.500.000 untuk diedit lalu simpan tanpa mengubah angka, nilainya harus tetap 1.500.000.
- Rupiah: input 2.000.000.000 (dua miliar). Tampil benar, saldo benar, tidak ada NaN.
- Rupiah: saldo dompet jadi negatif karena pengeluaran lebih besar. Tampil dan terhitung benar.
- Yen: transaksi 1.280 dan 450 di dompet Yen. Saldo 1.730, tanpa desimal.
- Dompet Rupiah dan dompet Yen sama-sama punya saldo. Keduanya tidak dijumlahkan jadi satu angka.
- USD: 3 transaksi 0,10, 0,20, 12,35. Saldo tepat 12,65.
- Transfer Rp 100.000 dari dompet A ke B. Saldo A turun, B naik 100.000, saldo total tetap, statistik pemasukan dan pengeluaran tidak berubah.
- Hapus satu transaksi. Saldo, budget, grafik, dan donut mengabaikannya.
- Kategori dengan budget 0 atau tanpa budget: tidak ada tulisan NaN atau Infinity.
- Transaksi tanggal 31 pukul 23:59 dan tanggal 1 bulan berikutnya pukul 00:00 masuk ke bulan masing-masing.
- Ekspor JSON, impor kembali, bandingkan semua saldo. Hasilnya harus sama.

## Backup lengkap

Tujuan: ekspor dan impor JSON memuat semua yang dibuat pengguna, bukan transaksi saja, supaya backup bisa mengembalikan aplikasi seutuhnya.

Tempat dan tampilan:
- Tidak ada elemen tampilan baru. Tombol ekspor dan impor JSON yang ada tetap dipakai.

Aturan:
- File ekspor memuat: transaksi, dompet (termasuk mata uang dan status arsip), kategori, sub kategori, budget, dan pengaturan (tema, format jam, mata uang opsional, dan pengaturan lain yang tersimpan).
- Tulis nomor versi format di file ekspor.
- Backup lama tanpa nomor versi tetap bisa diimpor.
- Impor tetap memakai aturan konflik yang sudah ada (ID unik, waktu ubah terbaru menang).
- Data contoh tidak pernah ikut diekspor.

Data:
- Struktur file ekspor berubah. Periksa apakah file sinkron Drive sudah memuat dompet, kategori, sub kategori, budget, dan pengaturan, lalu samakan isinya dengan ekspor.

Cek manual:
- Buat dompet, kategori, dan budget sendiri, ekspor JSON, pakai "Hapus semua data", impor file tadi: semuanya kembali.
- Impor file backup lama (tanpa nomor versi): berhasil tanpa error.

## Hapus dompet

Tujuan: pengguna bisa menghapus dompet tanpa kehilangan riwayat transaksinya.

Tempat dan tampilan:
- Aksi hapus pada pengaturan dompet, dengan konfirmasi lewat UI web yang sudah dipakai di aplikasi. Teks konfirmasi menyebut nama dompet dan sisa saldonya.

Aturan:
- Dompet tidak dibuang, tetapi diberi penanda terarsip (soft delete, sama seperti transaksi).
- Riwayat transaksi dompet terarsip tetap tampil dengan nama dompetnya dan label "dihapus".
- Dompet terarsip tidak muncul di pilihan dompet saat tambah transaksi dan transfer, dan tidak muncul sebagai kartu dompet di Beranda.
- Total Saldo tidak menghitung dompet terarsip. Pemasukan dan Pengeluaran per bulan tetap menghitung riwayatnya.
- Status arsip ikut sinkron Drive, ekspor, dan impor.
- Sengaja TIDAK ada fitur memulihkan dompet di fitur ini.

Data:
- Dompet mendapat penanda terarsip. Berpengaruh ke sinkron Drive, ekspor, dan impor.

Cek manual:
- Hapus dompet yang punya transaksi: riwayatnya tetap ada dengan label "dihapus", kartu dompet hilang dari Beranda, Total Saldo berkurang sebesar saldo dompet itu.
- Ekspor lalu impor di perangkat lain: dompet tetap terarsip.

## Reset setelan pabrik

Tujuan: mengubah perilaku tombol "Hapus semua data" menjadi mengembalikan aplikasi ke keadaan awal seperti baru dipasang. Menggantikan perilaku item lama "Hapus semua data".

Tempat dan tampilan:
- Tombol "Hapus semua data" yang sudah ada di Pengaturan tetap di tempatnya. Teks konfirmasi menyebut bahwa transaksi, dompet, kategori, sub kategori, budget, dan pengaturan ikut terhapus, dan menyarankan ekspor backup dulu.

Aturan:
- Setelah reset: tidak ada transaksi, dompet, kategori, sub kategori, dan budget buatan pengguna, dan semua pengaturan kembali ke bawaan awal.
- Jika sedang login Google, data di Drive appDataFolder juga dikosongkan supaya data lama tidak kembali lewat sinkron. Login Google tidak diputus.
- Jika tidak login, hanya data lokal yang dikosongkan.
- Konfirmasi lewat UI web yang sudah ada.

Data:
- Tidak ada perubahan struktur data. Berpengaruh ke sinkron Drive (file dikosongkan).

Cek manual:
- Reset saat login: data kosong di aplikasi, muat ulang, data lama tidak kembali dari Drive.
- Reset saat tidak login: aplikasi kembali ke keadaan awal.

## Data contoh lengkap

Tujuan: mengganti data contoh lama (angka acak) dengan satu set tetap yang lengkap, supaya bisa mencoba semua fitur. Menggantikan perilaku item lama "Data contoh (dummy)".

Tempat dan tampilan:
- Tombol data contoh yang sudah ada di Pengaturan tetap di tempatnya. Tambahkan tombol keluar dari mode data contoh yang kembali ke data asli.

Aturan:
- Satu set tetap, hasilnya sama setiap kali: beberapa dompet (Rupiah, Yen, USD), kategori dan sub kategori, budget, transaksi pemasukan dan pengeluaran, dan transfer antar dompet satu mata uang.
- Data contoh disimpan terpisah dari data asli, jadi data asli tidak tertimpa.
- Data contoh tidak pernah disinkronkan ke Drive, tidak ikut ekspor, dan tidak ikut impor.
- Tampilkan penanda jelas bahwa aplikasi sedang dalam mode data contoh.
- Tidak boleh berisi data asli milik pemilik.

Data:
- Tidak ada perubahan struktur data. Tidak ada pengaruh ke sinkron Drive, ekspor, atau impor karena data contoh dipisah.

Cek manual:
- Aktifkan data contoh saat login Google: tidak ada yang masuk ke Drive, data asli utuh setelah keluar dari mode data contoh.
- Aktifkan dua kali: isinya identik.

## Klik dompet untuk filter riwayat

Tujuan: kartu dompet di Beranda bisa diklik untuk melihat riwayat dompet itu saja.

Tempat dan tampilan:
- Kartu dompet di Beranda menjadi bisa diklik. Kartu terpilih ditandai dengan gaya yang sudah ada di dev/DESIGN.md.

Aturan:
- Klik kartu dompet mengatur dropdown dompet di Riwayat ke dompet itu. Jika Riwayat ada di halaman terpisah, pindah ke halaman Riwayat dengan filter terpasang.
- Klik kartu yang sama lagi mengembalikan ke "Semua Dompet".
- Transfer muncul di kedua dompet (keluar di asal, masuk di tujuan).
- Tidak ada logika filter baru, hanya memakai dropdown dompet yang ada.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Klik kartu Cash: riwayat hanya transaksi Cash. Klik lagi: semua dompet.
- Lakukan transfer Cash ke Bank: transaksi muncul di riwayat Cash dan Bank.

## Filter bulan di Riwayat

Tujuan: melihat riwayat per bulan, dengan ringkasan pemasukan dan pengeluaran bulan itu.

Tempat dan tampilan:
- Dropdown bulan di sebelah dropdown "Semua Dompet" di Riwayat Transaksi, dengan opsi "Semua bulan".
- Ringkasan Pemasukan (hijau) dan Pengeluaran (merah) di samping judul Riwayat Transaksi.

Aturan:
- Bulan dipakai bersama dengan navigasi bulan di kartu saldo Beranda: mengganti bulan di salah satu tempat mengubah yang lain. Default bulan yang sedang aktif.
- Opsi "Semua bulan" hanya berlaku di Riwayat. Memilihnya tidak mengubah apa pun di Beranda, karena Beranda hanya dashboard. Bulan di kartu saldo Beranda tetap bulan terakhir yang dipilih.
- Ringkasan mengikuti filter bulan dan filter dompet sekaligus.
- Jika lebih dari satu mata uang, ringkasan ditampilkan per mata uang.
- Transfer tidak dihitung sebagai pemasukan atau pengeluaran.
- Angka ringkasan harus sama dengan angka di kartu saldo untuk bulan dan dompet yang sama.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor. Pilihan filter tidak disimpan.

Cek manual:
- Pilih Oktober 2026: hanya transaksi Oktober tampil, ringkasan sama dengan kartu saldo Oktober.
- Pilih "Semua bulan": semua transaksi tampil.
- Pilih "Semua bulan" lalu buka Beranda: kartu saldo tetap menampilkan bulan sebelumnya dan tidak berubah.

## Warna kartu saldo multi mata uang

Tujuan: menyamakan warna kartu saldo Beranda saat ada lebih dari satu mata uang dengan saat satu mata uang.

Tempat dan tampilan:
- Kartu saldo di Beranda: Total Saldo putih, Pemasukan hijau, Pengeluaran merah, untuk setiap baris mata uang. Susunan baris per mata uang tetap.

Aturan:
- Hanya mengubah warna, memakai warna yang sudah ada di dev/DESIGN.md. Jangan tambah warna baru.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Aktifkan Rupiah, Yen, dan USD: semua baris Total Saldo putih, Pemasukan hijau, Pengeluaran merah, sama seperti tampilan satu mata uang.

## Pencarian riwayat

Tujuan: mencari transaksi dengan cepat, terutama untuk data hasil impor yang banyak.

Tempat dan tampilan:
- Tombol search di Riwayat Transaksi yang membuka kolom pencarian.

Aturan:
- Mencari di catatan, nama kategori, nama sub kategori, nominal, dan nama dompet.
- Bekerja bersama filter dompet dan filter bulan, dan ringkasan pemasukan dan pengeluaran mengikuti hasil pencarian.
- Pencarian tidak membedakan huruf besar kecil.
- Riwayat pencarian tidak disimpan.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Cari "dapur": hanya transaksi dengan kata itu di catatan atau kategori tampil.
- Cari saat filter dompet Cash aktif: hanya hasil dari Cash.

## Catatan di bawah angka

Tujuan: catatan transaksi tampil lebih lega dan terbaca penuh walau panjang.

Tempat dan tampilan:
- Di baris transaksi, catatan tampil di baris sendiri di bawah baris yang berisi kategori dan nominal.
- Jika catatan lebih panjang dari lebar yang tersedia, teks bergeser pelan agar bisa dibaca seluruhnya.

Aturan:
- Jika pengguna mengaktifkan pengaturan kurangi animasi di perangkat (prefers-reduced-motion), teks tidak bergeser, tetapi dipotong dengan "...".
- Catatan pendek tidak bergeser.
- Izin dari pemilik: boleh menambah satu animasi geser teks ini di luar dev/DESIGN.md, dan boleh menambahkan satu baris tentangnya di dev/DESIGN.md. Tidak boleh menambah animasi, warna, font, atau bayangan lain.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Transaksi dengan catatan pendek: tampil statis di bawah baris kategori dan nominal.
- Transaksi dengan catatan sangat panjang: teks bergeser. Dengan kurangi animasi aktif: teks terpotong "...".

## Tombol Timbangan di tengah navigasi

Tujuan: akses cepat ke halaman Timbangan lewat tombol menonjol di tengah navigasi bar.

Tempat dan tampilan:
- Tombol Timbangan di tengah navigasi bar, lebih besar dan sedikit menonjol dari bar, dengan ikon timbangan yang sudah ada. Membuka halaman Timbangan.

Aturan:
- Menu navigasi lain dibagi seimbang di kiri dan kanan tombol tengah.
- Jika Timbangan sudah ada sebagai menu biasa di navigasi, pindahkan menjadi tombol tengah ini, jangan ada dua.
- Izin dari pemilik: boleh menambah gaya tombol menonjol (ukuran dan posisi) memakai warna dan token yang sudah ada di dev/DESIGN.md, dan boleh menambahkan satu baris tentangnya di dev/DESIGN.md. Tidak boleh menambah warna, font, bayangan, atau animasi baru.
- Perilaku tombol back Android yang sudah diperbaiki tidak boleh berubah.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Tombol tampil di tengah di web dan APK, membuka halaman Timbangan, navigasi lain tetap berfungsi.
- Tombol back Android dari halaman Timbangan kembali seperti sebelumnya.

## Tentang Pengembang

Tujuan: mengarahkan pengguna ke web portofolio pembuat aplikasi.

Tempat dan tampilan:
- Baris menu baru "Tentang Pengembang" di Pengaturan, di bagian yang sama dengan Kebijakan Privasi dan Syarat dan Ketentuan.

Aturan:
- Membuka https://mizoarkatamarenaldy.github.io di browser, bukan di dalam aplikasi. Web: tab baru dengan rel noopener. APK: browser bawaan HP.
- Pakai nama panggung "Mizo Arkatama Renaldy" saja. Jangan tulis nama asli di mana pun di repo.
- Tidak ada halaman baru.
- Jika butuh plugin Capacitor baru, tulis di laporan akhir agar pemilik menjalankan build sendiri. Jangan jalankan build apa pun.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Di web: ketuk "Tentang Pengembang", web portofolio terbuka di tab baru.
- Di APK: web portofolio terbuka di browser HP, bukan di dalam aplikasi.

## Dompet dipilih manual di form transaksi dan transfer

Tujuan: Mencegah transaksi atau transfer masuk ke dompet yang salah karena dompet terisi otomatis dari pilihan sebelumnya. Pengguna harus memilih dompet sendiri setiap kali.

Tempat dan tampilan:
- Form tambah transaksi (pemasukan dan pengeluaran): kolom dompet awalnya kosong dengan teks petunjuk "Pilih dompet".
- Form transfer antar dompet: kolom dompet asal dan dompet tujuan sama-sama awalnya kosong dengan teks petunjuk "Pilih dompet".
- Tombol Simpan tidak bisa dipakai (nonaktif) selama ada dompet yang belum dipilih.
- Pakai kontrol pemilih dompet yang sudah ada, tidak ada elemen tampilan baru.

Aturan:
- Setiap kali form tambah transaksi atau form transfer dibuka, semua kolom dompet kosong. Tidak ada pilihan yang diingat dari transaksi atau transfer sebelumnya, dari sesi sebelumnya, atau dari filter dompet di Riwayat.
- Setelah disimpan, form kembali ke keadaan kosong, termasuk dompet.
- Form tambah transaksi: kalau pengguna hanya punya satu dompet, dompet itu langsung terpilih.
- Form transfer: tidak ada pemilihan otomatis, aturan yang sudah ada (dompet asal dan tujuan harus beda, mata uang harus sama) tidak diubah.
- Form edit transaksi dan edit transfer tidak berubah: tetap menampilkan dompet milik data itu.
- Berlaku sama di mode data contoh dan mode tanpa login.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Punya dua dompet atau lebih. Buka form tambah transaksi: dompet kosong, Simpan nonaktif. Pilih Tunai, simpan. Buka form tambah lagi: dompet kembali kosong.
- Di Riwayat, klik kartu dompet Bank untuk memfilter, lalu buka form tambah transaksi: dompet tetap kosong, tidak otomatis Bank.
- Buka form transfer: dompet asal dan tujuan kosong, Simpan nonaktif. Pilih keduanya, isi nominal, simpan. Buka form transfer lagi: keduanya kembali kosong.
- Punya satu dompet saja. Buka form tambah transaksi: dompet itu sudah terpilih dan Simpan bisa dipakai setelah field lain terisi.
- Edit transaksi dan edit transfer lama: dompet aslinya tampil dan tidak bisa dikosongkan.

## Geser urutan dompet dan kategori

Tujuan: Pengguna bisa mengatur sendiri urutan dompet, kategori, dan sub kategori dengan menggeser, misalnya memindah dompet B ke atas dompet A atau sebaliknya.

Tempat dan tampilan:
- Di daftar dompet, daftar kategori pemasukan, daftar kategori pengeluaran, dan daftar sub kategori di Pengaturan, setiap baris punya ikon garis tiga (≡) di sisi kanan sebagai pegangan geser.
- Baris hanya bisa digeser lewat ikon ≡. Menyentuh bagian lain baris tidak menggeser, supaya tidak bentrok dengan scroll.
- Saat digeser, baris yang dipegang tampil terangkat dan tempat tujuannya terlihat jelas. Pakai warna dan gaya yang sudah ada di dev/DESIGN.md, tanpa warna, bayangan, atau animasi baru.
- Harus jalan dengan sentuhan (HP dan APK) dan dengan mouse (PC).

Aturan:
- Urutan baru langsung tersimpan setelah baris dilepas.
- Urutan dipakai di semua tempat yang menampilkan daftar itu: kartu dompet di Beranda, pilihan dompet di form, daftar kategori di form, daftar budget, dan Pengaturan.
- Sub kategori hanya bisa digeser di dalam kategori induknya, tidak bisa pindah ke kategori lain.
- Kategori pemasukan dan kategori pengeluaran punya urutan sendiri-sendiri.
- Dompet, kategori, atau sub kategori baru masuk di urutan paling bawah.
- Data lama yang belum punya urutan memakai urutan yang berlaku sekarang, tanpa berubah tampilannya sampai pengguna menggeser.
- Menghapus dompet atau kategori tidak mengacak urutan sisanya.
- Fitur ini sengaja tidak menambah tombol naik/turun atau pengurutan otomatis (abjad, saldo, dll).
- Di mode data contoh, geser urutan boleh dicoba dan hanya berlaku di data contoh, tidak menyentuh data asli.

Data:
- Dompet, kategori, dan sub kategori mendapat field urutan. Field ini ikut disimpan di IndexedDB, ikut sinkron Drive memakai aturan sinkron yang sudah ada (tanpa aturan konflik baru), ikut ekspor dan impor JSON, dan ikut backup lengkap.
- Impor JSON dari backup lama yang tidak punya field urutan tetap berhasil, dengan urutan mengikuti urutan data di file.
- Reset setelan pabrik mengembalikan urutan ke bawaan.
- Ekspor CSV tidak berubah.

Cek manual:
- Punya dompet A dan B. Geser B ke atas A lewat ikon ≡: urutan di Pengaturan berubah, kartu Beranda dan pilihan dompet di form tambah transaksi ikut berubah. Tutup dan buka lagi app: urutan tetap.
- Geser A kembali ke atas B: urutan kembali seperti semula.
- Di daftar kategori pengeluaran, geser satu kategori ke urutan paling atas: daftar kategori di form dan di budget ikut berubah. Daftar kategori pemasukan tidak terpengaruh.
- Di kategori yang punya sub kategori, geser sub kategori di dalam kategori itu: urutan berubah. Sub kategori kategori lain tidak terpengaruh dan tidak bisa dipindah antar kategori.
- Di HP, scroll daftar dengan menyentuh bagian baris selain ≡: daftar hanya scroll, tidak ada baris yang bergeser.
- Tambah dompet baru: muncul di paling bawah.
- Ekspor backup JSON, reset setelan pabrik, impor lagi: urutan hasil geser kembali seperti sebelum reset.
- Impor file backup lama tanpa field urutan: impor berhasil dan urutan sesuai urutan di file.
- Login Google di dua perangkat, geser urutan di satu perangkat, sinkron: perangkat lain menampilkan urutan yang sama.

## Penyesuaian saldo di Timbangan

Tujuan: Pengguna bisa menyamakan saldo dompet di app dengan saldo riil (rekening bank, e-wallet, uang di saku) saat ada selisih yang tidak diinput, misalnya bunga atau cashback. Selisih dicatat sebagai transaksi penyesuaian yang tidak mengotori surplus/defisit gaya hidup.

Tempat dan tampilan:
- Halaman Timbangan: tambah bagian "Sesuaikan saldo" berisi daftar dompet. Tiap baris menampilkan nama dompet, saldo di app (sesuai mata uang dompet), dan tombol "Sesuaikan".
- Klik "Sesuaikan" membuka dialog dengan teks "Berapa saldo riil sekarang?", satu kolom nominal, dan tampilan saldo di app sebagai pembanding.
- Setelah nominal diisi, dialog menampilkan selisih (plus atau minus) sebelum disimpan, misalnya "Selisih: +Rp12.500". Pengguna harus menekan tombol konfirmasi untuk menyimpan.
- Kalau selisih plus, ada pilihan kategori: Bunga, Cashback, Koreksi. Kalau selisih minus, kategori otomatis Koreksi, tanpa pilihan.
- Kalau selisih 0, tampil pesan "Saldo sudah sama" dan tidak ada transaksi yang dibuat.
- Halaman Timbangan: tambah satu baris terpisah bernama "Equilibrium" di bawah timbangan bulan ini, berisi total penyesuaian pada bulan yang sedang ditampilkan Timbangan (plus hijau, minus merah, per mata uang bila lebih dari satu). Baris ini tidak ikut menggerakkan timbangan.
- Di Riwayat, transaksi penyesuaian muncul dengan label "Penyesuaian" dan nama kategorinya (Bunga, Cashback, atau Koreksi).
- Tampilan baru wajib mengikuti dev/DESIGN.md.

Aturan:
- Penyesuaian adalah tipe transaksi baru, terpisah dari pemasukan, pengeluaran, dan transfer.
- Penyesuaian mengubah saldo dompet, tetapi TIDAK dihitung di: surplus/defisit Timbangan, grafik pemasukan dan pengeluaran, diagram donut, budget per kategori, ringkasan Pemasukan/Pengeluaran di Riwayat, dan angka pemasukan/pengeluaran di kartu saldo Beranda. Perlakuannya mirip transfer, tetapi tipenya berbeda dan tidak boleh digabung dengan transfer.
- Nominal penyesuaian memakai mata uang dompetnya, termasuk desimal untuk USD.
- Tanggal dan waktu transaksi penyesuaian adalah saat dikonfirmasi.
- Transaksi penyesuaian hanya bisa dihapus (soft delete seperti transaksi lain), tidak bisa diedit nominalnya. Menghapusnya mengembalikan saldo dompet seperti sebelum penyesuaian.
- Bunga, Cashback, Koreksi adalah daftar tetap, tidak muncul di daftar kategori pemasukan atau pengeluaran biasa dan tidak bisa dipilih di form transaksi biasa.
- Fitur ini sengaja TIDAK punya: deteksi selisih otomatis, indikator timbangan miring, pengingat, atau penyesuaian massal untuk banyak dompet sekaligus.
- Kalkulator "Timbang sebelum beli" dan timbangan bulan ini yang sudah ada tidak diubah.
- Mode data contoh: fitur boleh dicoba dan hanya berlaku di data contoh. Mode tanpa login berjalan sama.

Data:
- Tipe transaksi baru "penyesuaian" dengan kategori penyesuaian (Bunga, Cashback, atau Koreksi). Ikut disimpan di IndexedDB, ikut sinkron Drive memakai aturan konflik yang sudah ada (tanpa aturan baru), ikut ekspor dan impor JSON, dan ikut backup lengkap.
- Ekspor CSV memuat transaksi penyesuaian dengan tipe "Penyesuaian" dan kategorinya.
- Impor JSON dari backup lama tanpa tipe ini tetap berhasil.
- Hitungan saldo dompet, total saldo, dan audit logika matematis harus memasukkan penyesuaian dengan benar.

Cek manual:
- Dompet Krombank saldo di app Rp100.000. Klik "Sesuaikan", isi 112.500: dialog menampilkan selisih +Rp12.500 dan pilihan Bunga/Cashback/Koreksi. Pilih Bunga, konfirmasi: saldo dompet jadi Rp112.500, di Riwayat muncul "Penyesuaian: Bunga".
- Setelah itu cek surplus/defisit Timbangan, grafik, donut, budget, ringkasan Riwayat, dan kartu saldo Beranda: semuanya tidak berubah. Baris "Equilibrium" menampilkan +Rp12.500 hijau.
- Sesuaikan lagi dengan nominal lebih kecil dari saldo app (misalnya 110.000): selisih -Rp2.500, kategori otomatis Koreksi tanpa pilihan, saldo jadi Rp110.000, baris Equilibrium bulan itu menjadi +Rp10.000.
- Isi nominal yang sama dengan saldo di app: tampil "Saldo sudah sama", tidak ada transaksi baru.
- Hapus transaksi penyesuaian di Riwayat: saldo dompet kembali ke nilai sebelum penyesuaian, baris Equilibrium ikut berubah.
- Dompet Yen dan dompet USD: nominal dan selisih memakai mata uang dompet masing-masing, USD menerima desimal.
- Ganti bulan di Timbangan: baris Equilibrium mengikuti bulan yang ditampilkan.
- Ekspor backup JSON, reset setelan pabrik, impor lagi: transaksi penyesuaian kembali dan saldo benar. Impor file backup lama tanpa penyesuaian: berhasil.
- Login Google di dua perangkat, buat penyesuaian di satu perangkat, sinkron: perangkat lain menampilkan saldo dan transaksi yang sama.

## Perbaikan UI: tombol tambah, banner data contoh, header budget

Tujuan: merapikan tiga masalah layout di tampilan HP. Ini perbaikan tampilan saja, tidak mengubah logika, data, atau fitur.

Bagian 1: Tombol + tidak menutupi konten
- Masalah: tombol + mengambang menutupi teks "Terpakai" di kartu Budget, tombol hapus di transaksi terakhir, dan baris paling bawah di Riwayat.
- Perbaikan: beri ruang kosong di bawah isi semua halaman (Beranda, Riwayat, Timbangan, Statistik, Pengaturan) sehingga konten paling bawah bisa digulir sampai benar-benar di atas tombol + dan navigasi bawah.
- Posisi dan ukuran tombol + tidak diubah.

Bagian 2: Banner "Mode Data Contoh" jadi strip tipis
- Masalah: banner memakai sekitar seperlima layar di setiap halaman.
- Perbaikan: jadikan satu baris tipis dengan tinggi sekitar setengah dari sekarang. Tulisan "Mode Data Contoh" dan tombol "Keluar" tetap ada. Subteks "Data asli aman" boleh dipendekkan atau dihilangkan bila tidak muat dalam satu baris.
- Fungsi tombol Keluar tidak berubah.

Bagian 3: Header kartu Budget dirapikan
- Masalah: judul Budget, pilihan mata uang (Rupiah, Yen, Dolar), dan navigasi bulan bertumpuk dan tidak rapi. Teks "Terpakai" terpotong. Ada tulisan dobel "Sisa: Sisa Rp ...".
- Perbaikan: judul Budget dan pilihan mata uang dalam satu baris, navigasi bulan di baris berikutnya. Ubah "Sisa: Sisa Rp 4.100.000" menjadi "Sisa Rp 4.100.000". Pastikan "Total Budget", "Terpakai", dan "Sisa" terbaca penuh di layar sempit, tidak terpotong.

Larangan khusus tugas ini
- Jangan menambah warna, font, bayangan, atau animasi di luar dev/DESIGN.md.
- Jangan mengubah logika perhitungan, penyimpanan, sinkron, atau isi data.
- Jangan menyentuh bagian tampilan lain di luar tiga bagian di atas.

Kriteria selesai
- Di layar HP, tidak ada teks atau tombol yang tertutup tombol + di kelima halaman.
- Banner Mode Data Contoh satu baris.
- Header Budget rapi, tidak ada teks terpotong atau kata dobel.
- Tampilan tetap sesuai token di dev/DESIGN.md.

## Audit UI: temuan desain dan aksesibilitas

Tujuan: menghasilkan daftar temuan masalah desain dan aksesibilitas di aplikasi, untuk disaring pemilik. Tidak ada perubahan kode aplikasi.

Cara kerja
- Gunakan skill "Design review" dan "Accessibility audit" untuk menyusun temuan. Skill "Redesign existing projects" hanya dipakai sebagai referensi cara menilai, bukan untuk menulis ulang tampilan.
- Periksa sumber kode tampilan di repo (HTML, CSS, JS komponen). Jangan membaca atau menyentuh folder www/ dan android/.
- Nilai terhadap dev/DESIGN.md sebagai standar resmi. Skill tidak boleh dijadikan alasan untuk menambah warna, font, bayangan, atau animasi di luar DESIGN.md.
- Halaman yang diperiksa: Beranda, Riwayat, form tambah dan edit transaksi, Timbangan, Statistik, Pengaturan.

Fokus pemeriksaan
- Kontras teks (terutama teks abu-abu kecil di atas latar gelap) terhadap WCAG AA.
- Ukuran area sentuh tombol dan ikon (target minimal 44 px).
- Label dan nama aksesibel untuk tombol yang hanya berisi ikon (edit, hapus, navigasi bulan, tombol +).
- Urutan fokus dan keterbacaan di layar sempit.
- Ikon kategori: apakah tiap kategori bisa dibedakan.
- Halaman Statistik: tampilan saat data sedikit, dan keberadaan legenda diagram donut.
- Halaman Timbangan: kejelasan istilah, termasuk baris "Equilibrium".
- Konsistensi antar halaman terhadap token di DESIGN.md.

Format keluaran: ditulis LANGSUNG di balasan chat, BUKAN ke file di repo. Tabel dengan kolom:
No | Halaman | Temuan | Tingkat (tinggi/sedang/rendah) | Bukti (nama file dan baris atau selector) | Usulan perbaikan | Bentrok dengan DESIGN.md? (ya/tidak, jelaskan singkat) | Perkiraan ukuran kerja (kecil/sedang/besar)
- Maksimal 15 temuan, urut dari tingkat tertinggi.
- Tulis hanya temuan yang punya bukti di kode, jangan menebak.
- Di bawah tabel, tulis satu paragraf pendek: temuan mana yang menurutmu paling layak dikerjakan lebih dulu, dan temuan mana yang butuh keputusan pemilik karena menyentuh DESIGN.md.

Larangan khusus tugas ini
- Jangan mengubah file apa pun selain dev/PLAN.md, dev/PLANSPEC.md, dan dev/CHANGELOG.md.
- Jangan membuat file laporan baru di repo.
- Jangan memperbaiki temuan apa pun, sekecil apa pun.
- Jangan menyarankan penggantian gaya visual total. Usulan harus berupa perbaikan terarah.

Kriteria selesai
- Tabel temuan sudah ada di balasan chat sesuai format di atas.
- Tidak ada perubahan di file aplikasi.
- git status --short hanya menampilkan dev/PLAN.md, dev/PLANSPEC.md, dan dev/CHANGELOG.md.

## Perbaikan UI: target sentuh minimal 44 px

Tujuan: semua tombol yang disebut di bawah punya area sentuh minimal 44 x 44 px, sesuai hasil audit UI.

Catatan umum
- Nomor baris di bawah hanya perkiraan dan bisa bergeser. Cari ulang lewat nama selector di css/style.css, jangan percaya nomor baris.
- Ukuran yang dihitung adalah ukuran akhir yang berlaku setelah semua aturan CSS lain (termasuk media query dan aturan yang lebih spesifik) diperhitungkan, bukan hanya satu aturan.
- Ubah hanya properti ukuran (width, height, min-width, min-height, padding bila memang itu yang menentukan ukuran). Jangan ubah warna, font, bayangan, atau animasi.

Item 1: .btn-month-nav (sekitar L1228)
- Audit menulis 28 px, tapi di screenshot tombol terlihat lebih besar. Jadi VERIFIKASI DULU.
- Cari semua aturan CSS yang mengenai .btn-month-nav (termasuk yang menimpa) dan tentukan ukuran akhir lebar dan tingginya.
- Kalau ukuran akhir sudah 44 x 44 atau lebih, JANGAN ubah apa pun untuk item ini. Cukup laporkan hasil verifikasinya di balasan chat.
- Kalau salah satu sisi masih di bawah 44 px, naikkan jadi 44 px pada aturan yang benar-benar menentukan ukuran akhir. Pastikan kartu saldo di Beranda tidak rusak tata letaknya.

Item 2: .btn-edit-subcat dan .btn-delete-subcat
- min-height 40 px jadi 44 px.

Item 3: .btn-timbangan-back
- 40 x 40 px jadi 44 x 44 px.

Item 4: .btn-search-clear
- 40 x 40 px jadi 44 x 44 px.
- Ukuran ikon di dalamnya tetap 16 px.

Larangan khusus tugas ini
- Jangan menyentuh temuan audit lain (kontras, aria-label, fokus, ikon kategori, grafik, dan seterusnya). Itu sesi lain.
- Jangan mengubah js/app.js dan index.html.
- Jangan membaca atau menyentuh folder www/ dan android/. Jangan build.
- Jangan menambah warna, font, bayangan, atau animasi di luar dev/DESIGN.md.

Kriteria selesai
- Item 2 sampai 4 berukuran akhir minimal 44 px. Item 1 sudah diverifikasi dan diubah hanya bila perlu.
- Balasan chat berisi ringkasan: ukuran akhir .btn-month-nav sebelum perubahan, dan apakah diubah atau tidak.
- git status --short hanya menampilkan css/style.css, dev/PLAN.md, dev/PLANSPEC.md, dan dev/CHANGELOG.md.

## Perbaikan UI: semantik dan keyboard

Tujuan: memperbaiki 5 temuan aksesibilitas dari audit UI yang menyangkut semantik HTML, fokus keyboard, dan nama aksesibel.

Catatan umum
- Nomor baris hanya perkiraan dan bisa bergeser. Cari ulang lewat selector, id, atau nama fungsi, jangan percaya nomor baris.
- Jangan ubah logika bisnis, alur data, atau teks yang terlihat oleh pengguna.

Item 1: role="listitem" pada .settings-menu-row (index.html)
- Hapus atribut role="listitem" dari semua tombol .settings-menu-row (ada 8).
- Periksa wadah induknya. Kalau wadah itu punya role="list", hapus juga role="list" tersebut, karena wadah list tanpa listitem tidak valid.
- Jangan membungkus dengan elemen baru dan jangan mengubah class.

Item 2: indikator fokus di form transaksi (css/style.css)
- Tambahkan aturan :focus-visible untuk .type-segment-btn dan .category-chip.
- Pakai gaya fokus yang sudah ada di css/style.css untuk tombol lain (salin pola dan token yang sama). Jangan menambah warna, ketebalan, atau bayangan baru di luar dev/DESIGN.md.

Item 3: aria-label tombol Ubah dan Hapus di 5 transaksi terakhir Beranda (js/app.js)
- Cari fungsi yang merender 5 transaksi terakhir di Beranda (sekitar L3225 dan L3229).
- Ubah aria-label tombol Ubah dan Hapus supaya menyertakan nama kategori transaksi.
- Format kalimat WAJIB sama persis dengan yang dipakai di daftar Riwayat. Cari dulu pola di Riwayat, lalu ikuti.
- Gunakan nama kategori yang sama seperti yang tampil di baris transaksi itu. Jangan menyisipkan data lain.

Item 4: select tersembunyi #tx-category (index.html)
- Tambahkan tabindex="-1" dan aria-hidden="true" pada elemen #tx-category.
- Pastikan pemilihan kategori lewat .category-chip tetap bekerja seperti sebelumnya.

Item 5: segmen SVG donut (js/app.js)
- Cari kode yang membuat segmen SVG diagram donut di Statistik.
- Hapus tabindex dan role="button" dari segmen itu. Legenda donut jadi satu-satunya perhentian keyboard.
- Handler klik atau sentuh pada segmen dibiarkan. Jangan dihapus.
- Pastikan legenda donut masih bisa difokus dan dioperasikan lewat keyboard seperti sebelumnya. Kalau ternyata belum bisa, JANGAN diperbaiki di sesi ini, cukup laporkan di balasan chat.

Larangan khusus tugas ini
- Jangan menyentuh temuan audit lain (target sentuh, ikon kategori, subteks Equilibrium, efek redup dompet, grafik bulanan, kontras, focus trap). Itu sesi lain.
- Jangan membaca atau menyentuh folder www/ dan android/. Jangan build.
- Jangan menambah warna, font, bayangan, atau animasi di luar dev/DESIGN.md.

Kriteria selesai
- Kelima item sudah dikerjakan sesuai spek.
- Balasan chat berisi ringkasan singkat per item: file dan selector atau fungsi yang diubah, dan hasil pengecekan pada Item 1 (apakah wadah punya role="list") dan Item 5 (apakah legenda bisa difokus).
- git status --short hanya menampilkan index.html, css/style.css, js/app.js, dev/PLAN.md, dev/PLANSPEC.md, dan dev/CHANGELOG.md.

## Perbaikan UI: polish kecil

Tujuan: mengerjakan 3 temuan audit UI yang sifatnya polish kecil: ikon kategori Makan, subteks di label Equilibrium, dan efek redup dompet bersaldo 0.

Catatan umum
- Nomor baris hanya perkiraan dan bisa bergeser. Cari ulang lewat nama fungsi, selector, atau teks label, jangan percaya nomor baris.
- Jangan menambah warna, font, bayangan, atau animasi di luar dev/DESIGN.md. Pakai token dan class yang sudah ada.

Item 1: ikon kategori Makan (js/app.js)
- Cari fungsi getCategoryIconSvg() (sekitar L1381).
- Tambahkan case 'makan' supaya ikon kategori Makan berbeda dari ikon Lainnya (default).
- Ikuti pola ikon yang sudah ada di fungsi itu: viewBox, ketebalan garis, gaya garis, dan cara pewarnaan (currentColor atau token yang sama). Jangan membuat gaya ikon baru.
- Bentuk ikon: sederhana dan mudah dikenali sebagai makanan (misalnya sendok dan garpu). Pilih satu bentuk saja.
- Periksa cara fungsi mencocokkan nama kategori (huruf besar atau kecil, spasi). Pastikan case 'makan' benar-benar cocok dengan nama kategori Makan yang dipakai aplikasi. Jangan mengubah cara pencocokan untuk case lain.

Item 2: subteks di label Equilibrium (index.html)
- Cari label "Equilibrium" di halaman Timbangan (sekitar L306).
- Label "Equilibrium" JANGAN diganti nama dan teksnya JANGAN diubah.
- Tambahkan subteks kecil di bawah label dengan teks persis: Penyesuaian saldo bulan ini
- Pakai class teks sekunder atau teks kecil yang sudah ada di css/style.css untuk subteks serupa di aplikasi. Kalau tidak ada yang cocok, buat satu class baru yang hanya memakai token warna dan ukuran font yang sudah ada di dev/DESIGN.md.
- Pastikan tata letak baris tidak rusak di layar sempit (sekitar 360 px): tidak ada teks terpotong atau menumpuk.

Item 3: efek redup dompet saldo 0 (css/style.css)
- Cari aturan .wallet-balance-item.is-zero (sekitar L1047).
- Ubah efek redupnya supaya hanya berlaku pada angka saldo dan border item. Teks nama dompet harus kembali ke warna dan opasitas normal sehingga kontrasnya tetap terbaca.
- Kalau efek redup sekarang dipasang lewat opacity pada seluruh item, ganti dengan pewarnaan atau opasitas yang hanya mengenai elemen angka saldo dan border, memakai token yang sudah ada di dev/DESIGN.md.
- Jangan ubah tampilan dompet yang saldonya tidak 0.

Larangan khusus tugas ini
- Jangan menyentuh temuan audit lain (target sentuh, semantik, fokus, grafik bulanan, kontras tema terang, focus trap). Itu sesi lain atau ditunda.
- Jangan mengganti nama label atau teks lain yang terlihat pengguna selain subteks di Item 2.
- Jangan membaca atau menyentuh folder www/ dan android/. Jangan build.
- Jangan menambah warna, font, bayangan, atau animasi di luar dev/DESIGN.md.

Kriteria selesai
- Ketiga item sudah dikerjakan sesuai spek.
- Balasan chat berisi ringkasan singkat per item: file dan fungsi atau selector yang diubah, bentuk ikon Makan yang dipilih, dan class yang dipakai untuk subteks Equilibrium.
- git status --short hanya menampilkan index.html, css/style.css, js/app.js, dev/PLAN.md, dev/PLANSPEC.md, dan dev/CHANGELOG.md.

## Perbaikan UI: grafik bulanan Statistik

Tujuan: grafik bulanan di Statistik tetap terlihat seperti grafik (bukan satu batang tunggal) saat data baru sedikit, dengan menampilkan jendela bulan yang tetap dan bulan tanpa data bernilai 0.

Catatan umum
- Nomor baris hanya perkiraan dan bisa bergeser. Cari ulang lewat nama fungsi, jangan percaya nomor baris.
- Jangan menambah warna, font, bayangan, atau animasi di luar dev/DESIGN.md. Pakai gaya batang dan label yang sudah ada.

Item 1: jendela bulan di renderMonthlyChart (js/app.js, sekitar L1893)
- Cari fungsi renderMonthlyChart dan pahami dulu cara ia menentukan bulan yang ditampilkan.
- Aturan jendela yang baru:
  - Jendela selalu berakhir di bulan aktif (bulan yang sedang dipilih di Statistik).
  - Panjang jendela = jumlah bulan dari bulan data paling awal (yang relevan untuk grafik itu) sampai bulan aktif, dihitung inklusif, lalu dibatasi minimal 3 dan maksimal 6 bulan.
  - Contoh: data baru ada di bulan aktif saja, jendela 3 bulan (2 bulan sebelumnya + bulan aktif). Data sudah 10 bulan, jendela 6 bulan terakhir.
  - Bulan di dalam jendela yang tidak punya transaksi bernilai 0 dan tetap digambar sebagai bulan (label tetap muncul, batang 0 boleh tidak terlihat).
- Urutan bulan tetap dari paling lama ke paling baru, seperti sekarang.
- Lintas pergantian tahun harus benar (misal bulan aktif Februari, jendela 3 bulan = Desember, Januari, Februari).
- Skala sumbu dan tinggi batang tetap dihitung dari nilai yang tampil di jendela. Pastikan tidak ada pembagian dengan 0 saat semua bulan bernilai 0.
- Pastikan label bulan tidak menumpuk atau terpotong di layar sempit (sekitar 360 px) untuk jendela 6 bulan.

Larangan khusus tugas ini
- JANGAN ubah logika data: cara transaksi dijumlahkan per bulan, filter dompet, mata uang, kategori, transfer, dan soft delete tidak boleh berubah. Yang diubah hanya penentuan daftar bulan yang ditampilkan dan pengisian 0 untuk bulan kosong.
- Kalau grafik pemasukan bulanan memakai fungsi lain, JANGAN diubah. Kalau ternyata memakai fungsi renderMonthlyChart yang sama, perubahan otomatis berlaku untuk keduanya dan cukup dilaporkan di balasan chat.
- Jangan menyentuh temuan audit lain (kontras tema terang, focus trap, dan lainnya).
- Jangan mengubah index.html dan css/style.css. Kalau perbaikan label sempit ternyata mustahil tanpa CSS, JANGAN diubah, cukup laporkan di balasan chat.
- Jangan membaca atau menyentuh folder www/ dan android/. Jangan build.
- Jangan menambah warna, font, bayangan, atau animasi di luar dev/DESIGN.md.

Kriteria selesai
- Jendela bulan mengikuti aturan di atas, dan bulan tanpa data bernilai 0.
- Balasan chat berisi ringkasan singkat: cara lama menentukan bulan, cara baru, dan apakah grafik pemasukan ikut berubah.
- git status --short hanya menampilkan js/app.js, dev/PLAN.md, dev/PLANSPEC.md, dan dev/CHANGELOG.md.

## Ikon neraca di header

Tujuan: Menambahkan ikon neraca (timbangan) kecil di samping tulisan "Mizan Okane" di header, supaya identitas aplikasi lebih terasa sesuai namanya.

Tempat dan tampilan:
- Di header aplikasi (header tipis), di sebelah kiri tulisan "Mizan Okane", sejajar secara vertikal dengan tulisan itu.
- Berupa ikon timbangan dua piringan sederhana, digambar sebagai SVG inline (bukan file gambar baru, bukan memakai logo di assets/).
- Ukuran mengikuti tinggi teks judul header (sekitar 20 sampai 24 px). Jarak ke tulisan secukupnya mengikuti skala spasi di dev/DESIGN.md.
- Warna ikon memakai token warna yang sudah ada di dev/DESIGN.md (warna yang sama dengan teks judul header atau warna aksen), dan harus terlihat jelas di mode terang dan mode gelap.
- Ikon bersifat dekoratif: beri aria-hidden="true", tidak bisa diklik, tidak mengubah fungsi header, navigasi, atau tombol yang sudah ada.

Aturan:
- Hanya menambah ikon di samping tulisan. Tulisan "Mizan Okane" tidak diubah.
- Header tidak boleh jadi lebih tinggi dari sebelumnya, dan tidak boleh ada elemen header yang bergeser keluar layar di lebar HP kecil.
- Tidak mengubah file logo di assets/, ikon PWA, atau ikon APK.
- Tidak menambah warna, font, bayangan, atau animasi di luar dev/DESIGN.md.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka aplikasi di lebar HP: ikon timbangan muncul di kiri tulisan "Mizan Okane", sejajar dan tidak terpotong, tinggi header sama seperti sebelumnya.
- Ganti ke mode gelap lalu kembali ke mode terang: ikon tetap terlihat jelas di keduanya.
- Pindah ke halaman lain (Riwayat, Statistik, Pengaturan, Timbangan): ikon tetap tampil di header dan tidak mengganggu tombol atau navigasi yang ada.

## Statistik: satu bulan terpilih

Tujuan: Grafik pemasukan, grafik pengeluaran, dan diagram donut di halaman Statistik selalu menunjuk bulan yang sama. Sekarang pilihan bulannya terpisah, sehingga bisa terjadi grafik menyorot satu bulan sementara donut menampilkan bulan lain.

Tempat dan tampilan:
- Halaman Statistik. Satu navigasi bulan di bagian atas halaman, memakai pola navigasi bulan yang sudah ada di kartu saldo Beranda (tombol sebelumnya, nama bulan dan tahun, tombol berikutnya). Gunakan ulang komponen atau gaya yang sudah ada kalau memungkinkan.
- Navigasi bulan lama yang khusus untuk donut dihapus, diganti navigasi bersama ini.
- Diagram donut dan ringkasan kategori di bawahnya menampilkan data bulan yang dipilih di navigasi bersama.
- Grafik batang pemasukan bulanan dan grafik batang pengeluaran bulanan tetap menampilkan deretan bulan seperti sekarang, tetapi batang bulan yang dipilih diberi penanda yang jelas (misalnya warna batang penuh sementara batang lain lebih pudar, memakai token warna di dev/DESIGN.md).
- Kalau bulan yang dipilih berada di luar rentang bulan yang sedang tampil di grafik, rentang grafik digeser supaya memuat bulan itu.

Aturan:
- Bulan awal saat halaman Statistik dibuka adalah bulan berjalan.
- Mengubah bulan di navigasi bersama memperbarui grafik pemasukan, grafik pengeluaran, dan donut sekaligus.
- Perilaku per mata uang tetap seperti sekarang, tidak ada konversi nilai antar mata uang.
- Transfer antar dompet tetap tidak dihitung sebagai pemasukan atau pengeluaran, seperti aturan yang sudah ada.
- Grafik batang tidak bisa diklik untuk memilih bulan. Hal itu sengaja TIDAK dilakukan di item ini.
- Tidak menambah tab Harian, Bulanan, atau Tahunan. Itu item terpisah.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka Statistik: navigasi bulan tampil di atas dan menunjuk bulan berjalan. Donut dan batang yang dipenanda sama-sama bulan itu.
- Tekan tombol sebelumnya sampai bulan Oktober: donut berubah ke data Oktober, batang Oktober di kedua grafik diberi penanda, dan angka di donut sama dengan angka batang Oktober.
- Pilih bulan yang jauh ke belakang (di luar rentang grafik awal): rentang grafik bergeser dan batang bulan itu tetap terlihat dengan penanda.
- Pindah ke halaman lain lalu kembali ke Statistik: bulan awal kembali ke bulan berjalan, tidak error.
- Mode gelap: penanda batang tetap jelas dan teks tetap terbaca.

## Statistik: tab Harian, Bulanan, Tahunan

Tujuan: Pengguna bisa melihat statistik pemasukan dan pengeluaran dalam tiga skala waktu: per hari, per bulan, dan per tahun. Item ini melanjutkan "Statistik: satu bulan terpilih", jadi buka bagian itu di dev/PLANSPEC.md untuk melihat perilaku navigasi bulan bersama yang sudah ada.

Tempat dan tampilan:
- Halaman Statistik. Tiga tab di bagian atas halaman: "Harian", "Bulanan", "Tahunan". Tab "Bulanan" aktif saat halaman dibuka.
- Di bawah tab ada navigasi periode (tombol sebelumnya, label periode, tombol berikutnya) memakai komponen navigasi bulan yang sudah ada.
- Tab Bulanan: persis perilaku sekarang (navigasi bulan, grafik batang pemasukan dan pengeluaran per bulan dengan penanda bulan terpilih, donut untuk bulan terpilih). Tidak ada perubahan di tab ini.
- Tab Harian: navigasi bulan. Grafik batang pemasukan dan grafik batang pengeluaran menampilkan satu batang per tanggal, dari tanggal 1 sampai tanggal terakhir bulan terpilih. Hari tanpa transaksi tampil sebagai batang nol. Donut dan ringkasan kategori menampilkan bulan terpilih.
- Tab Tahunan: navigasi tahun (label hanya tahun, misalnya "2026"). Grafik batang pemasukan dan pengeluaran menampilkan satu batang per tahun, mulai dari tahun transaksi paling awal sampai tahun berjalan, dengan penanda pada tahun terpilih. Donut dan ringkasan kategori menampilkan tahun terpilih.
- Tab memakai semantik tab yang benar (role tablist, tab, tabpanel, aria-selected) dan bisa dipakai dengan keyboard sesuai perbaikan semantik yang sudah ada. Target sentuh tiap tab minimal 44 px.

Aturan:
- Bulan dan tahun terpilih disimpan dalam satu state bersama. Pindah dari Bulanan atau Harian ke Tahunan memakai tahun dari bulan terpilih. Mengganti tahun di tab Tahunan menggeser tahun pada state itu dan bulannya tetap. Pindah kembali ke Bulanan atau Harian memakai bulan yang sama dengan tahun hasil pilihan terakhir.
- Saat halaman Statistik dibuka kembali setelah pindah halaman, tab kembali ke Bulanan dan periode kembali ke bulan berjalan.
- Tombol berikutnya tidak bisa melewati bulan berjalan (tab Bulanan dan Harian) atau tahun berjalan (tab Tahunan).
- Tanggal transaksi dibaca dengan cara yang sama seperti fitur lain di aplikasi (tanggal lokal), supaya total harian cocok dengan Riwayat.
- Perilaku per mata uang tetap seperti sekarang, tidak ada konversi nilai antar mata uang.
- Transfer antar dompet tetap tidak dihitung sebagai pemasukan atau pengeluaran.
- Total donut harus sama dengan jumlah semua batang pada periode yang sama di grafik.
- Tidak ada kustomisasi rentang tanggal bebas, tidak ada ekspor grafik, dan batang tidak bisa diklik. Hal itu sengaja TIDAK dilakukan di item ini.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka Statistik: tab Bulanan aktif dan tampilan sama seperti sebelum item ini.
- Pindah ke tab Harian pada bulan yang punya transaksi: batang muncul per tanggal, hari kosong bernilai nol, dan jumlah semua batang pengeluaran sama dengan total pengeluaran bulan itu di donut dan di ringkasan Riwayat bulan itu.
- Di tab Harian, tekan tombol sebelumnya: bulan berganti dan jumlah batang menyesuaikan panjang bulan (misalnya 28 atau 29 untuk Februari, 30 atau 31 untuk bulan lain).
- Pindah ke tab Tahunan: label periode berupa tahun, batang per tahun muncul dengan penanda pada tahun terpilih, donut menampilkan total setahun. Tombol berikutnya tidak bisa melewati tahun berjalan.
- Di tab Tahunan, pindah ke tahun sebelumnya lalu kembali ke tab Bulanan: bulan yang tampil adalah bulan yang sama di tahun hasil pilihan terakhir.
- Pindah ke halaman lain lalu kembali ke Statistik: tab kembali ke Bulanan pada bulan berjalan.
- Tes dengan data contoh lengkap dan dengan data kosong: tidak ada error, grafik kosong tampil sebagai nol atau keadaan kosong yang sudah ada.
- Mode gelap: semua tab terbaca jelas, penanda batang tetap terlihat.

## Geser urutan budget

Tujuan: Pengguna bisa mengubah urutan budget per kategori dengan menggeser lewat ikon garis tiga (≡), supaya budget yang paling penting ada di atas.

Tempat dan tampilan:
- Di daftar budget per kategori, tempat pengguna mengatur budget. Tiap baris budget mendapat ikon garis tiga (≡) sebagai pegangan geser.
- Pegangan geser memakai mekanisme dan tampilan yang sama dengan fitur "Geser urutan dompet dan kategori". Jangan membuat mekanisme geser baru.
- Tidak ada teks baru. Target sentuh pegangan minimal 44 px.

Aturan:
- Urutan budget mengikuti urutan kategori yang sudah ada. Menggeser budget mengubah urutan kategori itu sendiri, sama seperti menggeser di daftar kategori.
- Budget hanya tampil untuk kategori yang punya budget. Kategori tanpa budget tidak muncul di daftar budget, tetapi posisinya di urutan kategori tidak berubah.
- Urutan baru juga terlihat di tempat lain yang menampilkan budget (misalnya Beranda), tanpa pegangan geser di sana.
- Tidak ada urutan budget yang terpisah dari urutan kategori.
- Fitur ini sengaja TIDAK menyentuh sub kategori dan tidak mengubah nilai budget.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor. Memakai field urutan kategori yang sudah ada.

Cek manual:
- Kondisi awal: ada tiga kategori dengan budget (A, B, C). Aksi: geser C ke paling atas. Hasil: urutan di daftar budget jadi C, A, B, dan urutan di daftar kategori ikut berubah.
- Kondisi awal: ada kategori tanpa budget di antara A dan B. Aksi: geser B ke atas A. Hasil: kategori tanpa budget tidak muncul di daftar budget dan tidak rusak.
- Kondisi awal: urutan sudah digeser. Aksi: tutup lalu buka lagi aplikasi, atau sinkron di perangkat lain. Hasil: urutan tetap sama.
- Aksi: geser lewat layar sentuh di HP, bukan hanya mouse. Hasil: geser berjalan lancar dan halaman tidak ikut tergulung.

## Budget per sub kategori

Tujuan: Pengguna bisa mengatur budget untuk sub kategori, sebagai bagian dari budget kategori induknya, supaya pengeluaran bisa dipantau lebih rinci.

Tempat dan tampilan:
- Pengaturan budget sub kategori ada di tempat yang sama dengan pengaturan budget kategori, dengan cara pengisian yang sama.
- Di daftar budget, sub kategori yang punya budget tampil tepat di bawah kategori induknya, dengan sedikit menjorok ke dalam. Bentuk bar dan angka sama seperti budget kategori.
- Sub kategori tidak punya pegangan geser. Urutan sub kategori memakai urutan yang sudah ada.
- Pesan galat memakai gaya pesan galat yang sudah ada di aplikasi. Teks pesan:
  - Kalau total budget sub kategori melebihi budget induk: "Total budget sub kategori tidak boleh melebihi budget kategori."
  - Kalau kategori induk belum punya budget: "Atur budget kategori dulu."
- Tampilan baru wajib mengikuti dev/DESIGN.md.

Aturan:
- Budget sub kategori bersifat opsional.
- Sub kategori hanya bisa diberi budget kalau kategori induknya sudah punya budget.
- Total budget semua sub kategori dalam satu induk tidak boleh melebihi budget induk. Aturan ini dicek saat menyimpan budget sub kategori, saat mengubah budget induk, dan saat menghapus budget induk.
- Kalau pengguna menurunkan atau menghapus budget induk sampai di bawah total budget sub kategorinya, simpan ditolak dengan pesan galat di atas. Jangan ubah atau hapus budget sub kategori diam-diam.
- Pengeluaran sub kategori tetap terhitung di budget induk. Budget sub kategori hanya menghitung transaksi yang memakai sub kategori itu. Transaksi induk tanpa sub kategori hanya terhitung di budget induk.
- Kalau sub kategori dihapus, budget sub kategori itu ikut terhapus.
- Perlakuan lain (berlaku sama tiap bulan, penanganan mata uang, transfer tidak dihitung) mengikuti budget kategori yang sudah ada.
- Fitur ini sengaja TIDAK menambah rollover, tidak menambah fitur geser untuk sub kategori, dan tidak mengubah cara budget kategori dihitung.

Data:
- Ada tambahan data budget pada sub kategori. Ikuti cara budget kategori disimpan, termasuk aturan konflik "yang terbaru menang" dan penanda terhapus.
- Data lama tanpa budget sub kategori harus tetap valid dan terbaca, tanpa migrasi paksa.
- Budget sub kategori ikut sinkron Drive, ekspor JSON, impor JSON, backup lengkap, dan reset setelan pabrik, sama seperti budget kategori.
- Impor JSON dari versi lama (tanpa budget sub kategori) tetap berhasil.

Cek manual:
- Kondisi awal: kategori Makan punya budget 1.000.000, sub kategori Kopi belum punya budget. Aksi: atur budget Kopi 200.000. Hasil: tersimpan, tampil di bawah Makan.
- Kondisi awal: Makan 1.000.000, Kopi 600.000. Aksi: atur budget sub kategori lain (Snack) 500.000. Hasil: ditolak, muncul pesan "Total budget sub kategori tidak boleh melebihi budget kategori."
- Kondisi awal: Makan 1.000.000, Kopi 200.000. Aksi: ubah budget Makan jadi 100.000. Hasil: ditolak dengan pesan galat, budget Kopi tidak berubah.
- Kondisi awal: kategori Transport belum punya budget. Aksi: atur budget sub kategori di bawah Transport. Hasil: ditolak dengan pesan "Atur budget kategori dulu."
- Kondisi awal: Makan punya budget, Kopi punya budget. Aksi: catat pengeluaran 50.000 di sub kategori Kopi. Hasil: terhitung di bar Kopi dan di bar Makan.
- Aksi: catat pengeluaran di Makan tanpa sub kategori. Hasil: terhitung hanya di bar Makan.
- Aksi: hapus sub kategori Kopi. Hasil: budget Kopi hilang dari daftar budget.
- Aksi: ekspor JSON lalu impor lagi di aplikasi bersih. Hasil: budget sub kategori kembali utuh. Impor file JSON lama tanpa budget sub kategori tetap berhasil.

## Ikon kategori kustom

Tujuan: Pengguna bisa memilih ikon untuk tiap kategori dari set ikon bawaan yang dikelompokkan sesuai tema, supaya kategori lebih mudah dikenali sekilas. Budget memakai ikon kategori yang sama.

Tempat dan tampilan:
- Di pengaturan kategori, saat menambah dan mengedit kategori, ada pemilih ikon. Ikon yang sedang dipilih tampil di samping field nama, dan menekannya membuka daftar ikon.
- Daftar ikon dikelompokkan per tema, misalnya Makanan, Transportasi, Belanja, Rumah, Kesehatan, Pendidikan, Hiburan, Keuangan, dan Lainnya. Tiap kelompok diberi judul kecil.
- Ikon kategori tampil di samping nama kategori di semua tempat nama kategori ditampilkan: daftar kategori, form tambah dan edit transaksi, Riwayat, Statistik, dan daftar budget.
- Di daftar budget, ikon dibaca dari kategori, bukan disimpan sendiri di budget. Mengganti ikon kategori otomatis mengganti ikon di budget.
- Sub kategori tidak punya ikon sendiri dan tidak menampilkan ikon.
- Set ikon dibuat sebagai SVG inline di kode, tanpa library ikon baru dan tanpa memuat apa pun dari internet, supaya tetap jalan offline. Gaya ikon (ketebalan garis, ukuran) mengikuti ikon yang sudah ada dan dev/DESIGN.md.
- Warna ikon memakai warna teks yang sudah ada di DESIGN.md. Target sentuh pemilih ikon minimal 44 px, dan tiap ikon di daftar punya label teks untuk pembaca layar.
- Tampilan baru wajib mengikuti dev/DESIGN.md.

Aturan:
- Pilihan ikon bersifat opsional. Kategori tanpa ikon yang dipilih menampilkan satu ikon netral bawaan.
- Kategori lama (yang dibuat sebelum fitur ini) otomatis tampil dengan ikon netral dan tetap valid tanpa migrasi paksa.
- Kalau ID ikon pada data tidak dikenal (misalnya datang dari versi aplikasi lain), tampilkan ikon netral dan jangan error.
- Ikon hanya mengubah tampilan. Nama, urutan, budget, dan perhitungan kategori tidak berubah.
- Fitur ini sengaja TIDAK menyediakan unggah gambar sendiri, tidak mendukung emoji bebas, dan tidak menambah ikon untuk sub kategori atau dompet.

Data:
- Ada tambahan satu field ikon pada kategori, berisi ID ikon berupa teks (bukan gambar). Ikuti cara kategori disimpan, termasuk aturan konflik "yang terbaru menang".
- Field ikon ikut sinkron Drive, ekspor JSON, impor JSON, dan backup lengkap. Reset setelan pabrik mengembalikan ke kondisi tanpa ikon pilihan.
- Impor JSON dari versi lama (tanpa field ikon) tetap berhasil.
- Kategori pada data contoh lengkap diberi ikon yang sesuai, dan data contoh tetap tidak ikut sinkron, ekspor, atau impor.

Cek manual:
- Kondisi awal: kategori Makan belum punya ikon pilihan. Aksi: buka pengaturan kategori, pilih ikon dari kelompok Makanan. Hasil: ikon tampil di daftar kategori, form transaksi, Riwayat, Statistik, dan daftar budget.
- Kondisi awal: Makan punya budget dan ikon. Aksi: ganti ikon Makan. Hasil: ikon di daftar budget ikut berubah tanpa mengatur budget lagi.
- Kondisi awal: kategori lama tanpa ikon pilihan. Hasil: tampil ikon netral dan tidak ada error.
- Aksi: ekspor JSON, reset setelan pabrik, lalu impor lagi. Hasil: ikon kategori kembali sesuai pilihan.
- Aksi: impor file JSON lama tanpa field ikon. Hasil: impor berhasil, kategori memakai ikon netral.
- Aksi: sinkron ke perangkat lain. Hasil: ikon pilihan tampil sama di perangkat itu.
- Aksi: buka di tema gelap dan terang, di HP. Hasil: ikon terbaca jelas dan pemilih ikon bisa digulir dengan nyaman.

## Ikon sub kategori

Tujuan: Sub kategori bisa punya ikon sendiri, dipilih dari set ikon bawaan yang sama dengan ikon kategori, supaya sub kategori mudah dikenali sekilas.

Tempat dan tampilan:
- Di pengaturan sub kategori, saat menambah dan mengedit sub kategori, ada pemilih ikon yang sama dengan pemilih ikon kategori (ikon terpilih di samping field nama, menekannya membuka daftar ikon per tema).
- Ikon sub kategori tampil di samping nama sub kategori di semua tempat nama sub kategori ditampilkan: daftar sub kategori, form tambah dan edit transaksi, Riwayat, Statistik, dan daftar budget.
- Di daftar budget, ikon dibaca dari sub kategori, bukan disimpan sendiri di budget. Mengganti ikon sub kategori otomatis mengganti ikon di budget.
- Pakai set ikon, gaya, warna, label pembaca layar, dan target sentuh minimal 44 px yang sama dengan item "Ikon kategori kustom". Jangan tambah ikon baru ke set kecuali perlu.
- Tampilan baru wajib mengikuti dev/DESIGN.md.

Aturan:
- Pilihan ikon sub kategori bersifat opsional.
- Sub kategori tanpa ikon pilihan menampilkan ikon kategori induknya. Kalau induk juga belum punya ikon pilihan, tampilkan ikon netral bawaan.
- Kalau ikon induk diganti, sub kategori yang belum punya ikon pilihan ikut berubah mengikuti induk.
- Kalau ID ikon pada data tidak dikenal, tampilkan ikon induk atau ikon netral dan jangan error.
- Ikon hanya mengubah tampilan. Nama, urutan, budget, dan perhitungan sub kategori tidak berubah.
- Fitur ini sengaja TIDAK menyediakan unggah gambar, tidak mendukung emoji bebas, dan tidak menambah ikon untuk dompet.

Data:
- Ada tambahan satu field ikon pada sub kategori, berisi ID ikon berupa teks, sama seperti field ikon pada kategori. Ikuti cara sub kategori disimpan, termasuk aturan konflik "yang terbaru menang".
- Field ikon ikut sinkron Drive, ekspor JSON, impor JSON, dan backup lengkap. Reset setelan pabrik mengembalikan ke kondisi tanpa ikon pilihan.
- Data lama dan impor JSON dari versi lama (tanpa field ikon sub kategori) tetap valid dan berhasil, tanpa migrasi paksa.
- Sub kategori pada data contoh lengkap diberi ikon yang sesuai. Data contoh tetap tidak ikut sinkron, ekspor, atau impor.

Cek manual:
- Kondisi awal: sub kategori Kopi belum punya ikon pilihan, induknya Makan punya ikon. Hasil: Kopi tampil dengan ikon Makan.
- Aksi: pilih ikon sendiri untuk Kopi. Hasil: ikon tampil di daftar sub kategori, form transaksi, Riwayat, Statistik, dan daftar budget.
- Kondisi awal: Kopi punya budget dan ikon sendiri. Aksi: ganti ikon Kopi. Hasil: ikon di daftar budget ikut berubah tanpa mengatur budget lagi.
- Kondisi awal: Kopi tanpa ikon pilihan. Aksi: ganti ikon Makan. Hasil: ikon Kopi ikut berubah mengikuti Makan.
- Aksi: ekspor JSON, reset setelan pabrik, lalu impor lagi. Hasil: ikon sub kategori kembali sesuai pilihan.
- Aksi: impor file JSON lama tanpa field ikon sub kategori. Hasil: impor berhasil, sub kategori memakai ikon induk.
- Aksi: sinkron ke perangkat lain. Hasil: ikon sub kategori sama di perangkat itu.

## Perbaikan: dialog Pilih Ikon muncul sendiri di Pengaturan

Tujuan: Menghilangkan dialog "Pilih Ikon" yang tampil permanen di halaman Pengaturan tanpa diminta, supaya dialog itu hanya muncul saat pengguna menekan pemilih ikon.

Gejala:
- Di halaman Pengaturan (menu utama, dengan daftar Akun dan Sinkron, Tentang, Zona Bahaya), sebuah kotak "Pilih Ikon" tampil di bagian bawah halaman, di bawah kartu Zona Bahaya, di sebelah tombol tambah.
- Kotak itu hanya berisi judul "Pilih Ikon", tombol silang (X), dan tombol "Batal". Daftar ikon tidak tampil.
- Tombol silang dan tombol Batal tidak berfungsi, jadi kotak tidak bisa ditutup.
- Kalau pemilih ikon ditekan lewat jalur normal (di pengaturan kategori atau sub kategori), jendela pemilih ikon yang benar terbuka dan berfungsi. Jadi yang rusak adalah satu dialog tambahan yang tampil tanpa dibuka.

Tempat dan tampilan:
- Tidak ada elemen tampilan baru. Hasil akhirnya: halaman Pengaturan tidak menampilkan kotak "Pilih Ikon" kecuali pemilih ikon sedang dibuka.

Aturan:
- Cari penyebabnya dulu, jangan menebak. Periksa semua tempat yang membuat dialog "Pilih Ikon" (pemilih ikon kategori dan pemilih ikon sub kategori). Kemungkinan: ada dialog yang dirender di halaman Pengaturan tanpa kondisi tersembunyi, ada elemen dialog ganda, atau aturan CSS menimpa atribut tersembunyi.
- Perbaiki akar masalahnya sehingga dialog tersembunyi saat tidak dibuka, bukan sekadar menyembunyikan lewat patch visual.
- Pastikan hanya ada satu mekanisme pemilih ikon untuk kategori dan sub kategori, tanpa dialog duplikat.
- Pemilih ikon yang benar tetap berfungsi seperti sebelumnya: membuka dari ikon di samping field nama, menampilkan daftar ikon per tema, dan bisa ditutup lewat X, Batal, dan memilih ikon.
- Periksa juga halaman lain yang mungkin kena masalah yang sama (Beranda, Riwayat, Statistik, Timbangan), tapi hanya perbaiki yang berkaitan dengan dialog pemilih ikon.
- Fitur ini sengaja TIDAK mengubah desain pemilih ikon, set ikon, atau perilaku lain.
- Tidak boleh mengubah file di luar yang berkaitan dengan dialog pemilih ikon.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Aksi: buka Pengaturan dari Beranda. Hasil: tidak ada kotak "Pilih Ikon" di halaman.
- Aksi: buka Pengaturan, masuk ke pengaturan kategori, tekan ikon kategori. Hasil: pemilih ikon terbuka dengan daftar ikon, bisa dipilih dan bisa ditutup.
- Aksi: sama, tapi untuk sub kategori. Hasil: sama seperti di atas.
- Aksi: tutup pemilih ikon lewat X dan lewat Batal, lalu kembali ke halaman Pengaturan. Hasil: tidak ada kotak "Pilih Ikon" yang tertinggal.
- Aksi: muat ulang aplikasi langsung di halaman Pengaturan, di tema gelap dan terang, di lebar layar HP dan PC. Hasil: tidak ada kotak "Pilih Ikon" yang muncul.

## Kategori Equilibrium

Tujuan: Penyesuaian saldo (Equilibrium) punya kategori yang jelas, dan hasilnya ikut terhitung di Statistik. Defisit juga dapat pilihan penyebab, sama seperti surplus.

Tempat dan tampilan:
- Dialog "Berapa saldo riil sekarang?" di Timbangan: saat selisih negatif, tampil bagian "Kategori Penyesuaian" dengan tiga pilihan: Biaya admin, Lupa catat, Koreksi. Gayanya sama dengan pilihan di surplus. Default terpilih: Koreksi.
- Pilihan saat selisih positif tetap: Bunga, Cashback, Koreksi.
- Tidak ada elemen tampilan lain yang baru. Baris Equilibrium di Timbangan tetap seperti sekarang.

Aturan:
- Equilibrium adalah kategori induk tetap bawaan aplikasi. Ada satu di daftar kategori pemasukan (untuk surplus) dan satu di daftar kategori pengeluaran (untuk defisit).
- Sub kategorinya adalah pilihan di dialog: pemasukan = Bunga, Cashback, Koreksi; pengeluaran = Biaya admin, Lupa catat, Koreksi.
- Penyesuaian surplus dihitung sebagai pemasukan di Statistik, penyesuaian defisit sebagai pengeluaran, keduanya di bawah kategori induk Equilibrium.
- Di grafik dan donut Statistik hanya induk Equilibrium yang muncul, sebagai satu irisan, sama seperti kategori lain. Sub kategori tidak dipecah di grafik.
- Equilibrium dan sub kategorinya tidak bisa diedit, dihapus, digeser, atau diberi ikon kustom di Pengaturan, dan tidak muncul sebagai pilihan di form tambah/edit transaksi biasa.
- Equilibrium tidak punya budget dan tidak muncul di daftar budget.
- Angka Surplus/Defisit di kartu Timbangan Bulan Ini tidak berubah: tetap hanya dari transaksi biasa, Equilibrium tetap baris terpisah.
- Penyesuaian lama yang belum punya label dianggap Koreksi.
- Hal yang sengaja TIDAK dilakukan: "Lupa catat" tidak mengarahkan ke form pengeluaran, tetap satu baris penyesuaian.

Data:
- Equilibrium adalah kategori bawaan dari kode, jangan disimpan sebagai kategori buatan pengguna di daftar kategori. Kalau perlu menambah field label sub kategori pada data penyesuaian, pastikan data lama tetap terbaca, dan pastikan sinkron Drive, ekspor, dan impor JSON tetap aman. Tulis hasilnya di entri CHANGELOG.

Cek manual:
- Dompet Kas Tunai saldo di app Rp 700.000, isi saldo riil 500.000: muncul pilihan Biaya admin, Lupa catat, Koreksi dengan Koreksi terpilih. Pilih Lupa catat, konfirmasi. Di Statistik bulan itu, pengeluaran bertambah Rp 200.000 di kategori Equilibrium.
- Isi saldo riil 800.000: muncul Bunga, Cashback, Koreksi. Pilih Bunga, konfirmasi. Di Statistik, pemasukan bertambah Rp 100.000 di kategori Equilibrium.
- Di donut Statistik, Equilibrium muncul sebagai satu irisan, bukan per sub kategori.
- Di Pengaturan kategori, Equilibrium tidak bisa diedit, dihapus, atau digeser. Di form tambah transaksi, Equilibrium tidak ada di daftar kategori.
- Kartu Surplus di Timbangan tidak berubah setelah penyesuaian, baris Equilibrium tetap menampilkan angka bersihnya.

## Pemisahan folder

Tujuan: Memisahkan repo menjadi tiga folder kerja (dev/, desktop/, mobile/) supaya UI PC dan mobile bisa dikerjakan sendiri-sendiri, tanpa mengubah perilaku aplikasi dan tanpa mengubah alamat situs GitHub Pages.

Tempat dan tampilan:
- Tidak ada elemen tampilan baru.

Peta folder (semua path dari root repo):
- Pindah ke desktop/src/: index.html, privasi.html, syarat.html, sw.js, manifest.webmanifest, css/, js/, icons/
- Pindah ke mobile/capacitor/: android/, capacitor.config.json, package.json, package-lock.json, scripts/, assets/
- Tetap di root: .gitignore, LICENSE, README.md, dev/
- Baru di root: .github/workflows/pages.yml
- mobile/capacitor/ bersifat sementara: proyek Capacitor dibekukan dan hanya untuk tes pemilik. Folder ini diganti proyek Kotlin saat item "Konversi Kotlin". Jangan membuat proyek Kotlin atau Tauri sekarang.

Aturan:
- Urutan commit, satu pemindahan per commit. Pesan commit berformat "pisah folder: nama singkat" lengkap dengan dua trailer Co-authored-by sesuai RULES.md. Push satu kali setelah ketiga commit selesai.
  1. "pisah folder: web ke desktop/src": `git mv` semua file web, lalu tambah .github/workflows/pages.yml.
  2. "pisah folder: Capacitor ke mobile/capacitor": `git mv` semua file Capacitor, ubah path di scripts/copy-www.js, perbarui dev/RULES.md dan dev/PROMPT.md di commit yang sama.
  3. "pisah folder: SCHEMA dan dokumen": buat dev/SCHEMA.md, tambah entri CHANGELOG.md, centang item di PLAN.md.
- Isi file web tidak berubah sama sekali. Setelah commit 1, `git diff --cached -M --stat` harus menunjukkan hanya rename dengan kemiripan 100%. Kalau ada file web yang isinya berubah, berhenti dan laporkan.
- pages.yml: jalan saat push ke main dan bisa dijalankan manual (workflow_dispatch). Isinya mengunggah folder desktop/src sebagai artifact Pages lalu deploy, memakai action resmi GitHub, tanpa langkah build. Isi desktop/src menjadi akar situs, sehingga alamat tetap https://mizoarkatamarenaldy.github.io/mizan-okane/. Pemilik yang mengganti sumber Pages di Settings, bukan kamu.
- mobile/capacitor/: capacitor.config.json tetap memakai webDir "www" (relatif ke folder itu). Di scripts/copy-www.js, ubah hanya path sumber supaya membaca dari ../../desktop/src (relatif ke mobile/capacitor) dan menulis ke mobile/capacitor/www. Baca file itu dulu. Kalau ia memakai daftar file, pertahankan daftar yang sama. Jangan menjalankannya.
- Jangan ubah isi android/. Periksa android/build.gradle, android/app/build.gradle, android/capacitor.settings.gradle, dan android/gradle.properties. Referensi "../node_modules" tetap valid karena node_modules dibuat pemilik bersebelahan dengan android/. Kalau ada path lain yang rusak akibat pemindahan, laporkan ke pemilik, jangan diperbaiki sendiri.
- .gitignore tidak diubah.
- Perubahan dev/RULES.md, hanya ini:
  - Di "Aturan kerja", pada baris larangan build, ganti penyebutan `www/` dan `android/` menjadi `mobile/capacitor/www/` dan `mobile/capacitor/android/`.
  - Di "Aturan setelah pemisahan folder", ganti bullet terakhir dengan: "Larangan build berlaku untuk mobile/capacitor/www/, mobile/capacitor/android/, dan folder hasil build apa pun di desktop/ dan mobile/. Pemilik yang membangun. Sampai Konversi Kotlin selesai, mobile/capacitor/ dibekukan."
  - Tambah bullet: "Alamat situs GitHub Pages, origin, dan OAuth Client ID tidak boleh berubah. Isi desktop/src adalah akar situs, dan semua path di dalamnya tetap relatif."
  - Tambah bullet: "Setiap perubahan format data wajib memperbarui dev/SCHEMA.md di commit yang sama."
- Perubahan dev/PROMPT.md, hanya satu baris di bagian "Catatan untuk Claude": ganti penyebutan folder www/ dan android/ menjadi mobile/capacitor/www/ dan mobile/capacitor/android/.
- dev/SCHEMA.md dibuat baru, hanya dokumentasi. Baca desktop/src/js/db.js, desktop/src/js/sync.js, dan fungsi ekspor dan impor di desktop/src/js/app.js (hanya baca, jangan ubah). Isinya: (a) struktur database IndexedDB: nama database, object store, kunci; (b) format file JSON ekspor/backup: struktur, nama field, tipe, termasuk dompet, kategori, sub kategori, budget, pengaturan; (c) format file di Google Drive appDataFolder: nama file dan strukturnya; (d) aturan konflik: ID unik, waktu ubah, terbaru menang, penanda terhapus; (e) nomor versi format kalau ada. Tulis hanya yang benar-benar ada di kode. Kalau tidak jelas, tulis "tidak tercatat", jangan menebak. Contoh memakai data fiktif, jangan menaruh data transaksi asli.
- Yang sengaja TIDAK dilakukan: mengubah isi file html, css, atau js; mengubah logika sinkron atau Client ID; menjalankan npm, build, atau cap; membuat folder src-tauri atau proyek Kotlin; mengubah README.md; mengubah "Keputusan terkunci"; menghapus folder lokal yang tidak dilacak git (node_modules, www).

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor. dev/SCHEMA.md hanya mendokumentasikan format yang sudah ada.

Cek manual:
- Setelah push, di GitHub Settings, Pages, pilih sumber "GitHub Actions", lalu jalankan workflow secara manual. Buka https://mizoarkatamarenaldy.github.io/mizan-okane/ : tampilan sama seperti sebelumnya, data lama masih ada, login Google berhasil dan sinkron Drive jalan tanpa mengubah Client ID. PWA yang sudah terpasang di HP dan PC tetap terbuka dan memuat versi baru setelah refresh.
- `git log --follow desktop/src/js/app.js` menampilkan riwayat sebelum pemindahan. `git diff --stat -M pra-pisah-folder HEAD -- desktop/src` menunjukkan rename saja.
- Di mobile/capacitor: hapus node_modules lama di root, jalankan `npm install`, `npm run build:www`, `npx cap sync`, buka mobile/capacitor/android di Android Studio, build APK. APK jalan seperti sebelumnya (offline, data awet, ekspor CSV dan JSON berfungsi).
- Buka dev/SCHEMA.md dan cocokkan dengan satu file ekspor JSON asli: semua field di file ekspor tercatat di SCHEMA.md.


## Revisi UI PC

Tujuan: Di layar PC, tampilan sekarang hanya kolom HP yang ditaruh di tengah, sehingga layar lebar terbuang dan halaman harus di-scroll panjang. Ubah jadi tampilan dashboard yang memakai seluruh lebar layar. Tampilan HP dan APK tidak boleh berubah sama sekali.

Izin dari pemilik:
- Boleh mengubah dev/DESIGN.md hanya di bagian "Tata letak", baris "Satu kolom, max-width 640px, di tengah, juga di layar lebar", menjadi: di HP satu kolom max-width 640px; di PC (lebar 1024 px ke atas) memakai tata letak dashboard sesuai spek "Revisi UI PC". Bagian lain DESIGN.md tidak boleh diubah.
- Kalau sw.js memakai nama versi cache, boleh menaikkan nomor versinya supaya tampilan baru termuat. Tidak ada perubahan lain di sw.js.

Titik pindah:
- Semua perubahan hanya aktif lewat @media (min-width: 1024px). Di bawah 1024 px tampilan harus sama persis seperti sekarang.
- Di layar lebar, lebar isi dibatasi max-width 1600px, di tengah, dengan jarak samping 16px.

Tempat dan tampilan:
- Header dan navigasi: tetap seperti sekarang (nama app di kiri, ikon status sinkron di kanan, tab navigasi di atas). Tinggi header dan navigasi dihitung agar isi halaman pas di sisa tinggi layar.
- Beranda, 3 kolom, tinggi total isi = tinggi layar dikurangi header dan navigasi, halaman tidak scroll panjang:
  - Kolom kiri: kartu saldo (navigasi bulan, Total Saldo, Pemasukan, Pengeluaran) lalu kartu dompet di bawahnya.
  - Kolom tengah: Budget.
  - Kolom kanan: Transaksi Terakhir.
  - Tiap kolom punya scroll sendiri (overflow-y auto) kalau isinya melebihi tinggi kolom.
  - Banner data contoh (kalau tampil) tetap di atas, selebar penuh, di luar 3 kolom.
  - Tombol tambah (+) tetap melayang di pojok kanan bawah.
- Riwayat: pencarian dan filter bulan di satu baris atas. Daftar dikelompokkan per tanggal seperti sekarang, tapi tiap baris dibuat lebar seperti tabel: ikon dan kategori, catatan, dompet, waktu, jumlah (rata kanan, dengan tanda), tombol aksi. Bukan kartu sempit.
- Statistik: grafik disusun 2 kolom. Tab Harian, Bulanan, Tahunan tetap di atas.
- Pengaturan: daftar nama bagian di kiri (klik untuk lompat ke bagian itu), isi bagian di kanan. Hanya tampilan, isi dan perilaku pengaturan tidak berubah.
- Timbangan, Kebijakan Privasi, Syarat dan Ketentuan: konten dibatasi max-width 800px dan ditaruh di tengah.
- Form tambah/edit transaksi dan form transfer: tampil sebagai dialog di tengah layar, lebar sekitar 480px, dengan latar belakang gelap di belakangnya. Esc menutup dialog. Urutan isi form tidak berubah.
- Efek hover pada tombol, baris, dan tab: hanya ganti warna latar ke token surface-raised yang sudah ada. Tanpa animasi.

Aturan:
- Hanya CSS dan penyusunan HTML. Tidak ada perubahan logika, nama fungsi, nama kunci data, atau alur sinkron.
- Warna, font, ukuran teks, radius, dan tanpa bayangan tetap mengikuti dev/DESIGN.md. Tidak ada warna, font, bayangan, atau animasi baru. Latar belakang dialog memakai warna token bg dengan transparansi, bukan warna baru.
- Aturan kartu tetap berlaku: dilarang kartu di dalam kartu.
- Target sentuh minimal 44 px dan indikator fokus keyboard tetap ada.
- Mode terang dan gelap sama-sama harus benar.
- Sengaja TIDAK dilakukan: pintasan keyboard (selain Esc untuk dialog), sidebar navigasi, fitur baru, perubahan tampilan HP.
- Dilarang menjalankan build. Folder mobile/ dan dev/ tidak disentuh, kecuali dev/PLAN.md, dev/CHANGELOG.md, dev/PLANSPEC.md, dan baris "Tata letak" di dev/DESIGN.md sesuai izin di atas.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor. dev/SCHEMA.md tidak berubah.

Cek manual (dilakukan pemilik):
- Buka situs di browser PC lebar 1280x720: Beranda tampil 3 kolom, halaman tidak scroll panjang, kolom yang isinya banyak scroll sendiri.
- Buka di 1920x1080: 3 kolom tetap rapi, isi di tengah dan tidak melebar lewat 1600 px.
- Perkecil jendela ke bawah 1024 px: tampilan kembali sama persis dengan tampilan HP sekarang. Ulangi di Riwayat, Statistik, Pengaturan.
- Klik tombol tambah (+): form tampil sebagai dialog di tengah, Esc menutupnya, transaksi tersimpan seperti biasa.
- Riwayat: baris lebar rapi, pencarian dan filter bulan masih jalan.
- Pengaturan: klik nama bagian di kiri, isi di kanan lompat ke bagian itu.
- Ganti mode terang dan gelap: semua halaman terbaca jelas.
- Login Google dan sinkron Drive masih jalan seperti biasa.

## Konversi Tauri

Tujuan: Membuat aplikasi desktop Windows memakai Tauri yang membuka situs mizan-okane di GitHub Pages dalam jendelanya sendiri, supaya bisa dipasang dan dibuka seperti aplikasi biasa.

Tempat dan tampilan:
- Proyek Tauri ada di desktop/tauri/. Isi desktop/src/ tidak diubah sama sekali.
- Satu jendela, judul "Mizan Okane", ukuran awal 1100 x 750, bisa diubah ukurannya.
- Jendela memuat alamat situs GitHub Pages yang sudah ada (ambil dari konfigurasi atau dokumen repo yang sudah memuatnya, jangan menebak). File web tidak dibawa di dalam aplikasi.
- Ikon aplikasi memakai logo aplikasi yang sudah ada di repo. Jangan buat gambar baru.
- Tidak ada elemen tampilan baru di dalam aplikasi web.

Aturan:
- Target hanya Windows. Tidak ada target macOS, Linux, atau Android.
- Alamat situs, origin, dan OAuth Client ID tidak boleh berubah.
- Mobile/capacitor/ tidak disentuh.
- Tambahkan desktop/tauri/.gitignore yang mengecualikan folder hasil build Tauri (misalnya src-tauri/target/ dan folder dist atau bundle bila ada).
- Jangan menjalankan build atau perintah Tauri apa pun. Pemilik yang membangun.
- Hal yang sengaja TIDAK dilakukan: tidak ada login Google versi native, tidak ada mode offline khusus di luar yang sudah dimiliki situs, tidak ada fitur baru di aplikasi web.
- Di akhir pekerjaan, tulis di CHANGELOG.md perintah persis yang perlu dijalankan pemilik untuk menjalankan dan membangun aplikasi.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor. Data di aplikasi Tauri tersimpan di penyimpanan jendelanya sendiri dan terpisah dari data di browser. Pemindahan data antar keduanya lewat ekspor dan impor JSON yang sudah ada.

Cek manual (dilakukan pemilik):
- Jalankan aplikasi dengan perintah dari CHANGELOG.md. Hasil: jendela "Mizan Okane" terbuka dan menampilkan situs.
- Tambah satu transaksi, tutup aplikasi, buka lagi. Hasil: transaksi masih ada.
- Tekan login Google. Hasil yang diharapkan: login berhasil dan sinkron Drive jalan. Kalau login diblokir atau gagal, catat pesan error-nya apa adanya. Itu bukan kegagalan item ini, tapi bahan keputusan jalur login berikutnya.
- Pakai mode tanpa login. Hasil: semua fitur dasar tetap jalan.

## Konversi Kotlin

Tujuan: Menulis ulang versi mobile (khusus Android) memakai Kotlin di folder mobile/, menggantikan versi Capacitor. Tidak memakai Kotlin Multiplatform. Item ini baru dicatat supaya tidak lupa dan belum dikerjakan.

Aturan:
- Item ini baru boleh dikerjakan setelah "Konversi Tauri" selesai.
- Item ini menyentuh "Keputusan terkunci" (bagian Bentuk PWA, penyimpanan, dan APK dengan Capacitor). Sebelum dikerjakan, pemilik harus memberi prompt tersendiri dengan klausa "Izin dari pemilik" yang menyebut persis bagian mana yang boleh diubah.
- Item ini juga mencakup login Google native dan rilis APK. Spek rincinya wajib menulis nasib item "APK Item A" dan "APK rilis" di dev/PLAN.md.
- Format file di Google Drive harus tetap sama dengan dev/SCHEMA.md supaya data desktop dan mobile saling cocok.
- Spek rinci (apa yang dikonversi, nasib versi PWA dan Capacitor, jalur login Google, nasib data di IndexedDB dan Drive) ditulis pemilik bersama Claude sebelum item dikerjakan, lalu menggantikan bagian ini.

Data:
- Belum ditentukan. Ditulis saat spek rinci dibuat.

Cek manual:
- Belum ditentukan. Ditulis saat spek rinci dibuat.

## Perbaikan UI PC: Riwayat dan Timbangan

Tujuan: Setelah Revisi UI PC, Riwayat di layar PC masih terlihat janggal dan Timbangan masih harus di-scroll. Rapikan keduanya. Tampilan HP dan APK tidak boleh berubah sama sekali.

Izin dari pemilik:
- Bagian ini menggantikan aturan "Timbangan max-width 800px" di spek "Revisi UI PC", hanya untuk Timbangan. Jangan ubah isi bagian "Revisi UI PC" di dev/PLANSPEC.md.
- Boleh menaikkan nomor versi cache di desktop/src/sw.js. Tidak ada perubahan lain di sw.js.

Titik pindah:
- Semua perubahan hanya aktif lewat @media (min-width: 1024px). Di bawah 1024 px tampilan harus sama persis seperti sekarang. Wrapper HTML baru memakai display: contents di bawah 1024 px, seperti pola di Revisi UI PC.

Tempat dan tampilan:
- Riwayat, kotak pencarian: ikon kaca pembesar tidak boleh menimpa teks. Beri padding kiri kolom input cukup besar.
- Riwayat, baris transaksi: satu baris tunggal per transaksi, tingginya sekitar 48 sampai 56 px, bukan dua lapis. Kolom dengan lebar tetap yang sama di semua baris, berurutan dari kiri: ikon kategori, nama kategori (dan sub kategori), catatan (mengisi sisa lebar, terpotong "..." bila panjang), dompet, waktu, jumlah (rata kanan, dengan tanda + atau −, warna sesuai DESIGN.md), tombol aksi (edit dan hapus, lebar tetap di kanan; kalau transaksi tidak punya tombol edit, ruangnya tetap disisakan agar kolom tetap lurus).
- Riwayat, tanpa kartu di dalam kartu: baris tidak punya kotak, border, atau latar sendiri. Pemisah antar baris berupa garis tipis token border. Hover baris: ganti latar ke token surface-raised.
- Riwayat, header grup tanggal: tetap seperti sekarang (tanggal di kiri, total harian di kanan), dengan jarak yang rapi dari baris.
- Riwayat, ringkasan total di atas: susun rapi dalam satu blok, satu mata uang per baris, tidak melebar menumpuk di pojok.
- Timbangan, 3 kolom, tinggi isi pas dengan sisa tinggi layar, halaman tidak scroll panjang:
  - Kolom kiri: kartu "Timbang Sebelum Beli".
  - Kolom tengah: gambar timbangan, Pemasukan dan Pengeluaran, kotak Surplus atau Defisit, dan baris Equilibrium.
  - Kolom kanan: kartu "Sesuaikan Saldo".
  - Lebar kolom sekitar 1 : 1,2 : 1.
  - Tiap kolom punya scroll sendiri (overflow-y auto) kalau isinya melebihi tinggi kolom.
  - Lebar isi dibatasi max-width 1600px di tengah, sama seperti halaman lain.

Aturan:
- Hanya CSS dan penyusunan HTML. Tidak ada perubahan logika, nama fungsi, nama kunci data, atau alur sinkron.
- Warna, font, ukuran teks, radius, dan tanpa bayangan tetap mengikuti dev/DESIGN.md. Tidak ada warna, font, bayangan, atau animasi baru.
- Target sentuh minimal 44 px dan indikator fokus keyboard tetap ada. Mode terang dan gelap sama-sama harus benar.
- Sengaja TIDAK dilakukan: perubahan halaman lain (Beranda, Statistik, Pengaturan, form), fitur baru, perubahan tampilan HP.
- Dilarang menjalankan build. Hanya ubah desktop/ dan dev/PLAN.md, dev/CHANGELOG.md, dev/PLANSPEC.md.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor. dev/SCHEMA.md tidak berubah.

Cek manual (dilakukan pemilik):
- Browser PC 1280x720, buka Riwayat: tiap transaksi satu baris, kolom catatan, dompet, waktu, jumlah lurus di semua baris dan semua grup tanggal. Tidak ada kotak di dalam kartu. Ikon pencarian tidak menimpa teks.
- Riwayat: baris dengan catatan panjang terpotong "..." dan tidak merusak kolom lain. Baris tanpa tombol edit (misalnya Penyesuaian) tetap lurus dengan baris lain.
- Riwayat: pencarian, filter bulan, filter dompet, edit, dan hapus masih jalan.
- Browser PC 1280x720, buka Timbangan: 3 kolom (Timbang Sebelum Beli di kiri, timbangan di tengah, Sesuaikan Saldo di kanan), halaman tidak scroll panjang, kolom yang isinya banyak scroll sendiri.
- Timbangan: timbang sebelum beli dan sesuaikan saldo bekerja seperti biasa.
- Di 1920x1080: Riwayat dan Timbangan rapi, isi tidak melebar lewat 1600 px.
- Perkecil jendela ke bawah 1024 px: Riwayat dan Timbangan kembali sama persis dengan tampilan HP sekarang.
- Ganti mode terang dan gelap: keduanya terbaca jelas.

## Perbaikan UI PC 2A: CSS Timbangan

Tujuan: Merapikan tata letak halaman Timbangan di PC dan memastikan tombol (+) tidak menutup isi, hanya lewat perubahan CSS. Ini potongan pertama dari item "Perbaikan UI PC 2" yang sebelumnya dibatalkan (revert) karena semua tombol di PC tidak bisa diklik.

Tempat dan tampilan:
- Halaman Timbangan di PC (lebar 1024 px ke atas): tiga kolom sejajar dengan lebar 1 : 1,2 : 1 (kiri "Timbang Sebelum Beli", tengah timbangan, kanan "Sesuaikan Saldo"), semua mulai dari tinggi yang sama.
- Scroll ada di tingkat kolom (overflow-y: auto), bukan di tiap kartu. Kartu tidak punya scrollbar sendiri.
- Gambar timbangan SVG tampil utuh dan menyesuaikan lebar kolom tengah.
- Kolom Timbangan, kolom Beranda, dan halaman Statistik di PC diberi padding-bawah 88 px supaya tombol (+) tidak menutup isi paling bawah.
- Tampilan HP dan APK tidak berubah. Semua aturan baru hanya di dalam @media (min-width: 1024px).

Aturan:
- Hanya ubah desktop/src/css/style.css dan desktop/src/sw.js (naikkan versi cache ke mizan-v65, karena mizan-v64 pernah terkirim dan sudah dibatalkan).
- DILARANG mengubah desktop/src/index.html dan desktop/src/js/app.js.
- Kalau header Timbangan tidak bisa merentang satu baris penuh di atas tiga kolom tanpa mengubah index.html, jangan ubah HTML. Berhenti dan laporkan ke pemilik apa yang mentok.
- Jangan sentuh detail kategori di Statistik (itu item terpisah).
- Hover hanya ganti latar ke token surface-raised, tanpa animasi.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka Timbangan di PC 1280x720 dan 1920x1080: tiga kolom sejajar, scroll per kolom, tidak ada scrollbar di dalam kartu.
- Buka Beranda dan Statistik di PC, scroll sampai bawah: isi terakhir tidak tertutup tombol (+).
- Buka DevTools Console di semua halaman: tidak ada error merah.
- Klik semua tombol navigasi, tombol (+), dan satu tombol di tiap halaman: semuanya merespons.
- Buka di lebar HP: tampilan sama seperti sebelum item ini.

## Perbaikan UI PC 3: Beranda

Tujuan: Memperbaiki bug tampilan kartu Budget di Beranda PC dan menukar posisi kolom supaya kartu saldo ada di tengah dan Budget di kiri.

Tempat dan tampilan:
- Beranda di PC (lebar 1024 px ke atas), tiga kolom. Urutan baru dari kiri: Budget, Total Saldo beserta daftar dompet, Transaksi Terakhir.
- Bug yang diperbaiki: kartu Budget tingginya terkunci sehingga baris sub kategori (misalnya Restoran, Kopi & Camilan) keluar dari kartu, dan kartu punya scrollbar sendiri. Perbaikan: kartu tumbuh mengikuti isinya, tanpa scrollbar sendiri.
- Scroll hanya di tingkat kolom (overflow-y: auto), bukan di tiap kartu.
- Tiap kolom diberi padding-bawah 88 px supaya tombol (+) tidak menutup isi paling bawah, termasuk kartu Transaksi Terakhir.
- Tampilan HP dan APK tidak berubah. Semua aturan baru hanya di dalam @media (min-width: 1024px).

Aturan:
- Hanya ubah desktop/src/css/style.css dan desktop/src/sw.js (naikkan satu nomor dari versi cache sekarang. Jangan pakai nomor mizan-v64, karena pernah terkirim dan sudah dibatalkan).
- DILARANG mengubah desktop/src/index.html dan desktop/src/js/app.js. Tukar posisi kolom lewat CSS (grid-template-areas, grid-column, atau order).
- Kalau tukar posisi tidak bisa dilakukan tanpa mengubah index.html, jangan ubah HTML. Berhenti dan laporkan ke pemilik apa yang mentok.
- Urutan elemen di HP tetap seperti sekarang.
- Jangan sentuh halaman Timbangan, Statistik, dan Riwayat.
- Hover hanya ganti latar ke token surface-raised, tanpa animasi.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka Beranda di PC 1366x768 dan 1920x1080 dengan Mode Data Contoh: urutan kolom Budget, Total Saldo, Transaksi Terakhir.
- Di kolom Budget, semua baris sub kategori berada di dalam kartu, tidak ada scrollbar di dalam kartu.
- Scroll tiap kolom sampai bawah: isi terakhir tidak tertutup tombol (+).
- Buka DevTools Console: tidak ada error merah.
- Klik semua tombol navigasi, tombol (+), dan tombol di tiap kolom: semuanya merespons.
- Buka di lebar HP: urutan dan tampilan sama seperti sebelum item ini.

## Perbaikan UI PC 4: Timbangan tanpa scroll

Tujuan: Di PC, halaman Timbangan menampilkan seluruh isinya sekaligus tanpa perlu scroll, supaya tiga kolom bisa dilihat dalam satu layar.

Tempat dan tampilan:
- Halaman Timbangan di PC (lebar 1024 px ke atas). Tiga kolom tetap: kiri "Timbang Sebelum Beli", tengah timbangan, kanan "Sesuaikan Saldo".
- Target: di layar 1366x768 ke atas, semua isi ketiga kolom terlihat tanpa scroll, dengan data contoh aktif (4 dompet).
- Caranya: tampilan lebih padat. Kurangi jarak vertikal antar elemen dan padding kartu memakai token spasi yang sudah ada di dev/DESIGN.md. Tinggi gambar timbangan SVG menyesuaikan tinggi layar (misalnya lewat satuan vh) dan tetap utuh.
- Cadangan: di layar yang lebih pendek dari target, atau bila isi kolom kanan memanjang karena dompet banyak, scroll di tingkat kolom (overflow-y: auto) tetap ada. Tidak ada scrollbar di dalam kartu.
- Padding-bawah 88 px di tiap kolom tetap dipertahankan supaya tombol (+) tidak menutup isi.
- Tampilan HP dan APK tidak berubah. Semua aturan baru hanya di dalam @media (min-width: 1024px).

Aturan:
- Hanya ubah desktop/src/css/style.css dan desktop/src/sw.js (naikkan satu nomor dari versi cache sekarang. Jangan pakai nomor mizan-v64, karena pernah terkirim dan sudah dibatalkan).
- DILARANG mengubah desktop/src/index.html dan desktop/src/js/app.js.
- Kalau target tanpa scroll tidak bisa dicapai tanpa mengubah index.html, jangan ubah HTML. Berhenti dan laporkan ke pemilik apa yang mentok.
- Ukuran font dan tap target minimal 44 px tetap mengikuti dev/DESIGN.md, jangan dikecilkan demi memadatkan.
- Jangan sentuh halaman Beranda, Statistik, dan Riwayat.
- Hover hanya ganti latar ke token surface-raised, tanpa animasi.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka Timbangan di PC 1366x768 dan 1920x1080 dengan Mode Data Contoh: ketiga kolom terlihat utuh, tidak ada scrollbar di kolom maupun kartu.
- Buka di 1280x720: boleh ada scroll di tingkat kolom, tapi tidak di dalam kartu.
- Gambar timbangan tampil utuh, tidak terpotong, dan tombol di bawahnya terlihat.
- Buka DevTools Console: tidak ada error merah.
- Klik semua tombol navigasi, tombol (+), tombol "Sesuaikan", dan pilihan mata uang: semuanya merespons.
- Buka di lebar HP: tampilan sama seperti sebelum item ini.

## Perbaikan UI PC 5: Detail diagram Statistik lewat klik

Tujuan: Kotak rincian kategori di diagram donut Statistik sekarang muncul saat kursor diarahkan dan kadang bermasalah. Ubah supaya rincian hanya muncul setelah diklik.

Tempat dan tampilan:
- Halaman Statistik, diagram donut per kategori beserta daftar kategorinya.
- Klik irisan donut atau baris kategori: kotak rincian kategori itu terbuka. Isi dan letak kotak rincian tidak berubah.
- Klik kategori yang sama sekali lagi: kotak rincian tertutup.
- Klik kategori lain: kotak rincian pindah ke kategori itu.
- Dengan keyboard: irisan dan baris kategori yang sedang fokus bisa dibuka dan ditutup dengan Enter atau Spasi.
- Arahkan kursor saja tidak membuka rincian. Hover pada baris kategori hanya mengganti latar ke token surface-raised lewat CSS yang sudah ada, tanpa animasi.

Aturan:
- Izin dari pemilik: boleh mengubah logika di desktop/src/js/app.js, hanya pada penanganan event kursor dan fokus (mouseenter, mouseleave, mouseover, focus, blur) di irisan donut dan daftar kategori diagram Statistik, serta penanganan klik dan keyboard di tempat yang sama. Bagian lain app.js tidak boleh diubah.
- Hanya ubah desktop/src/js/app.js dan desktop/src/sw.js (naikkan satu nomor dari versi cache sekarang. Jangan pakai nomor mizan-v64, karena pernah terkirim dan sudah dibatalkan).
- DILARANG mengubah desktop/src/index.html dan desktop/src/css/style.css. Kalau perubahan ini ternyata butuh perubahan HTML atau CSS, berhenti dan laporkan ke pemilik apa yang mentok.
- Jangan sentuh halaman Beranda, Timbangan, dan Riwayat, dan jangan sentuh folder mobile/.
- Jangan menambah fitur baru di diagram. Hanya pemicunya yang diganti dari kursor ke klik.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka Statistik di PC dengan Mode Data Contoh. Arahkan kursor ke irisan dan baris kategori: rincian tidak muncul.
- Klik satu kategori: rincian muncul. Klik kategori yang sama lagi: rincian tertutup. Klik kategori lain: rincian pindah.
- Tab ke baris kategori lalu tekan Enter dan Spasi: rincian terbuka dan tertutup.
- Ganti bulan atau tab Harian, Bulanan, Tahunan lalu klik kategori: rincian tetap benar.
- Buka DevTools Console di semua halaman: tidak ada error merah.
- Klik semua tombol navigasi, tombol (+), dan satu tombol di tiap halaman: semuanya merespons.

## Perbaikan UI PC 6: Variasi Riwayat

Tujuan: Halaman Riwayat di PC terasa kosong dan semua baris tampil sama. Beri sedikit variasi tampilan lewat CSS saja, tanpa fitur baru.

Tempat dan tampilan:
- Halaman Riwayat di PC (lebar 1024 px ke atas), daftar transaksi yang dikelompokkan per tanggal.
- Garis aksen di sisi kiri tiap baris transaksi: warna token pemasukan untuk pemasukan, warna token pengeluaran untuk pengeluaran, warna token netral untuk transfer dan penyesuaian saldo. Pakai token warna yang sudah ada di dev/DESIGN.md, tebal garis 3 px.
- Kolom baris dirapikan dengan CSS grid berlebar tetap, supaya catatan, dompet, jam, dan nominal sejajar dari atas ke bawah dan celah kosong di tengah hilang. Kalau markup mengizinkan, nama dompet dan jam ditumpuk dua baris dalam satu kolom. Kalau tidak, tetap dua kolom terpisah berlebar tetap.
- Nama dompet tampil sebagai chip kecil: latar token surface-raised, sudut membulat sesuai token radius yang sudah ada. Berlaku bila nama dompet adalah elemen tersendiri di markup.
- Hover baris: latar berganti ke token surface-raised, tanpa animasi dan tanpa transisi. Tombol edit dan hapus disembunyikan (opacity 0) dan baru terlihat saat hover baris atau saat ada elemen di dalam baris yang menerima fokus keyboard (:focus-within). Ukuran tombol tetap 44 px dan tetap bisa diklik.
- Tampilan HP dan APK tidak berubah. Semua aturan baru hanya di dalam @media (min-width: 1024px).

Aturan:
- Hanya ubah desktop/src/css/style.css dan desktop/src/sw.js (naikkan satu nomor dari versi cache sekarang. Jangan pakai nomor mizan-v64, karena pernah terkirim dan sudah dibatalkan).
- DILARANG mengubah desktop/src/index.html dan desktop/src/js/app.js.
- Kalau ada poin yang tidak bisa dicapai tanpa mengubah HTML atau JS, kerjakan poin lain yang bisa, lalu laporkan ke pemilik poin mana yang mentok dan kenapa. Jangan ubah HTML atau JS.
- Jangan menambah warna, font, bayangan, atau animasi di luar dev/DESIGN.md. Tidak ada transition pada hover.
- Header ringkasan (angka total di kiri atas), header tanggal sticky, dan panel samping TIDAK dikerjakan di item ini.
- Jangan sentuh halaman Beranda, Timbangan, dan Statistik.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka Riwayat di PC 1366x768 dan 1920x1080 dengan Mode Data Contoh: garis aksen hijau, merah, dan netral sesuai jenis transaksi (termasuk baris "Penyesuaian: Koreksi" yang netral).
- Catatan, dompet, jam, dan nominal sejajar vertikal antar baris, tanpa celah kosong yang lebar.
- Arahkan kursor ke baris: latar berganti, tombol edit dan hapus muncul. Pindah kursor keluar: tombol tersembunyi lagi.
- Tab dengan keyboard ke tombol edit atau hapus: tombol terlihat saat fokus.
- Klik edit dan hapus pada satu baris: keduanya merespons seperti sebelumnya.
- Pencarian, filter bulan, dan filter dompet masih bekerja.
- Buka DevTools Console di semua halaman: tidak ada error merah.
- Klik semua tombol navigasi dan tombol (+): semuanya merespons.
- Buka di lebar HP: tampilan sama seperti sebelum item ini, tombol edit dan hapus tetap terlihat.

## Perbaikan UI PC 7: Timbangan

Tujuan: Merapikan halaman Timbangan di tampilan PC. Tombol back dihapus karena tidak penting dan memakan tempat. Ruang yang kosong dipakai supaya kartu kiri muat semua tanpa scroll, dan tiga kartu terlihat sama bentuknya.

Tempat dan tampilan:
- Halaman Timbangan, tampilan PC (desktop/).
- Hapus tombol back (ikon panah kiri) di kiri judul "Timbangan". Judul, subjudul, dan pilihan Mata Uang (Rupiah, Yen, Dolar) tetap ada.
- Kartu kiri "Timbang Sebelum Beli": seluruh isinya (harga barang, kategori, dompet sumber dana, kotak status, dan semua baris ringkasan sampai "Timbangan bulan ini setelah beli") harus terlihat penuh tanpa scroll di dalam kartu pada ukuran jendela PC yang normal. Caranya: pakai ruang yang dibebaskan tombol back, dan rapatkan jarak antar elemen bila perlu. Kalau jendela memang terlalu pendek, scroll di dalam kartu hanya sebagai cadangan.
- Kartu tengah "Timbangan Bulan Ini": samakan bentuknya dengan kartu kiri dan kanan, yaitu posisi atas, tinggi, radius, border, dan padding sama. Tiga kartu berjajar dengan tinggi yang sama. Isi kartu tengah (navigasi bulan, gambar timbangan, pemasukan, pengeluaran, kotak surplus, baris Equilibrium) tetap ada dan ditata rapi di dalam kartu yang lebih tinggi.
- Kartu kanan "Sesuaikan Saldo": ikut tinggi yang sama dengan dua kartu lain. Kalau daftar dompet lebih panjang dari kartu, scroll di dalam kartu.
- Header kartu di ketiganya memakai gaya judul yang konsisten.

Aturan:
- Hanya mengubah tampilan halaman Timbangan di PC.
- Tidak mengubah logika simulasi, perhitungan saldo, budget, Equilibrium, atau penyesuaian saldo.
- Tidak mengubah halaman lain.
- Tidak menambah warna, font, bayangan, atau animasi di luar dev/DESIGN.md.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka Timbangan di PC dengan jendela ukuran normal: tombol back tidak ada, kartu kiri terlihat penuh tanpa scroll di dalamnya.
- Tiga kartu (kiri, tengah, kanan) sejajar di atas dan sama tinggi di bawah.
- Ganti pilihan Mata Uang ke Yen dan Dolar: tampilan tetap rapi di tiga kartu dan angka tetap benar.
- Isi harga barang lalu pilih kategori dan dompet: hasil simulasi sama seperti sebelum perubahan.
- Klik "Sesuaikan" pada satu dompet: dialog berfungsi seperti biasa.
- Ganti bulan di kartu tengah: angka berubah seperti biasa.
- Buka halaman lain (Beranda, Riwayat, Statistik, Pengaturan): tampilannya tidak berubah.

## Perbaikan UI PC 8: Form Tambah Transaksi

Tujuan: Merampingkan form tambah dan edit transaksi di tampilan PC karena sekarang terlalu penuh. Tata letak diubah jadi baris ringkas (label di kiri, nilai di kanan), dan pilihan kategori pindah ke panel pilihan dua kolom.

Tempat dan tampilan:
- Form tambah transaksi dan form edit transaksi, tampilan PC (desktop/). Keduanya memakai tata letak yang sama.
- Pilihan jenis di atas form (Pemasukan, Pengeluaran, Transfer) tetap ada, tampil sebagai tiga tombol sejajar, yang aktif diberi penanda warna jenisnya (hijau, merah, netral sesuai DESIGN.md).
- Isi form berupa baris ringkas. Tiap baris: label di kiri dengan lebar tetap, nilai atau input di kanan, garis tipis pemisah di bawah baris. Tanpa kotak input besar yang berbingkai.
- Urutan baris untuk Pemasukan dan Pengeluaran: Tanggal dan Waktu (satu baris, bersebelahan), Total, Kategori, Dompet, Catatan.
- Urutan baris untuk Transfer: Tanggal dan Waktu (satu baris), Dari, Ke, Total, Catatan.
- Baris Kategori menampilkan nama kategori (dan sub kategori bila dipilih) beserta ikonnya. Kalau belum dipilih, kosong. Klik baris itu membuka panel pilihan berupa dialog (bukan bottom sheet) dengan dua kolom: kolom kiri daftar kategori, kolom kanan daftar sub kategori dari kategori yang sedang disorot. Kategori yang punya sub kategori diberi tanda panah. Menyorot atau mengklik kategori menampilkan sub kategorinya di kanan. Memilih kategori tanpa sub kategori langsung menutup panel dan mengisi baris Kategori. Panel punya judul "Kategori" dan tombol tutup.
- Baris Dompet (dan Dari, Ke di Transfer) menampilkan nama dompet beserta mata uangnya, sama seperti teks di pilihan dompet sekarang. Klik membuka daftar pilihan dompet seperti yang sudah ada, urutan dompet mengikuti urutan di Pengaturan.
- Tombol simpan dan batal tetap di bawah form, memakai gaya yang sudah ada di DESIGN.md.
- Form harus muat di layar PC normal dengan scroll seminimal mungkin.

Aturan:
- Hanya mengubah tata letak dan tampilan form. Semua field yang sudah ada tetap ada, dengan aturan yang sama: Waktu wajib diisi, format jam 12 atau 24 jam mengikuti pengaturan, angka uang memakai titik pemisah ribuan, desimal USD tetap berlaku, dompet kosong dan harus dipilih manual, validasi dan pesan error tidak berubah.
- Aturan sub kategori (wajib atau opsional, kategori Equilibrium tidak muncul di form) tidak berubah, ikuti perilaku yang sudah ada.
- Perilaku simpan, edit, dan transfer tidak berubah.
- Sengaja TIDAK menambah fitur baru: tanpa Berulang/cicil, tanpa Menandai, tanpa Deskripsi terpisah, tanpa lampiran foto, tanpa tombol bintang.
- Tidak mengubah halaman lain.
- Tidak menambah warna, font, bayangan, atau animasi di luar dev/DESIGN.md.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Tambah pengeluaran: isi Total, pilih kategori lewat panel dua kolom, pilih dompet, simpan. Transaksi muncul di Riwayat dengan data yang benar.
- Pilih kategori yang punya sub kategori: sub kategori tampil di kolom kanan, setelah dipilih baris Kategori menampilkan keduanya.
- Pilih kategori tanpa sub kategori: panel langsung menutup dan baris Kategori terisi.
- Kosongkan Waktu lalu simpan: tetap ditolak seperti sebelumnya. Dompet kosong: tetap ditolak.
- Tambah pemasukan dan transfer antar dompet: tata letak baris benar dan hasil tersimpan sama seperti sebelum perubahan.
- Dompet berbeda mata uang (Yen, USD): angka dan desimal tetap benar.
- Edit transaksi lama: form terisi dengan data transaksinya dan tampil dengan tata letak yang sama.
- Buka halaman lain (Beranda, Riwayat, Timbangan, Statistik, Pengaturan): tampilannya tidak berubah.

## Perbaikan UI PC 9: Form Transaksi Lanjutan

Tujuan: Lanjutan dari "Perbaikan UI PC 8". Tombol kategori cepat di bawah form dihapus karena mengganggu dan memenuhi form. Di tampilan PC, dialog dibuat persegi panjang horizontal supaya seluruh isinya terlihat tanpa scroll.

Buka hanya bagian "Perbaikan UI PC 8: Form Tambah Transaksi" di dev/PLANSPEC.md sebagai acuan. Semua aturannya tetap berlaku kecuali yang diubah di bawah ini.

Tempat dan tampilan:
- Dialog tambah transaksi dan dialog edit transaksi (satu komponen yang sama).
- Semua ukuran layar: hapus deretan tombol kategori cepat (Makan, Transport, Tagihan, Belanja, Hiburan, Lainnya) yang tampil di bawah baris Catatan dan di atas tombol simpan. Pemilihan kategori hanya lewat baris Kategori yang membuka panel dua kolom, seperti sekarang.
- Layar lebar (PC): dialog dilebarkan jadi persegi panjang horizontal, dengan tinggi yang cukup sehingga tanpa scroll di dalam dialog pada ukuran jendela PC yang normal. Tata letak isi dua kolom:
  - Baris paling atas: tiga tab jenis (Pengeluaran, Pemasukan, Transfer) selebar dialog.
  - Kolom kiri: Tanggal & Waktu (tanggal dan jam bersebelahan dalam satu baris bila muat) dan Total.
  - Kolom kanan: Kategori dan Dompet.
  - Baris di bawahnya selebar dialog: Catatan.
  - Paling bawah: tombol simpan.
  - Tab Transfer: kolom kiri Tanggal & Waktu dan Total, kolom kanan Dari dan Ke, lalu Catatan selebar dialog.
- Layar sempit (mobile): tata letak vertikal satu kolom tetap seperti sekarang, hanya tombol kategori cepat yang hilang.
- Panel pilihan Kategori (dua kolom) dan daftar pilihan dompet tidak diubah.

Aturan:
- Hanya mengubah tampilan: menghapus tombol kategori cepat dan mengatur ulang tata letak dialog di layar lebar lewat CSS (media query), tanpa mengubah logika.
- Semua field, validasi, dan aturan yang sudah ada tidak berubah: Waktu wajib diisi, format jam 12 atau 24 jam, titik pemisah ribuan, desimal USD, dompet harus dipilih manual, aturan sub kategori.
- Perilaku simpan, edit, dan transfer tidak berubah.
- Sengaja TIDAK menambah fitur baru dan tidak mengubah halaman lain.
- Tidak menambah warna, font, bayangan, atau animasi di luar dev/DESIGN.md.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka dialog tambah transaksi di PC dengan jendela normal: bentuk horizontal, dua kolom, tanpa scroll di dalam dialog, tombol kategori cepat tidak ada.
- Pilih kategori lewat baris Kategori: panel dua kolom berfungsi seperti sebelumnya.
- Tambah pengeluaran, pemasukan, dan transfer: data tersimpan sama seperti sebelum perubahan.
- Kosongkan Waktu atau Dompet lalu simpan: tetap ditolak seperti sebelumnya.
- Edit transaksi lama: dialog terisi dan tampil dengan tata letak yang sama.
- Perkecil jendela ke ukuran HP: tata letak kembali vertikal satu kolom, tanpa tombol kategori cepat.
- Buka halaman lain (Beranda, Riwayat, Timbangan, Statistik, Pengaturan): tampilannya tidak berubah.

## Perbaikan UI PC 10: Kolom Form Transaksi

Tujuan: Dua penyempurnaan kecil di dialog tambah dan edit transaksi. Pilihan bulan di Tanggal & Waktu terpotong, dan baris Kategori tampilannya belum seragam dengan baris Dompet.

Buka hanya bagian "Perbaikan UI PC 9: Form Transaksi Lanjutan" di dev/PLANSPEC.md sebagai acuan. Semua aturannya tetap berlaku kecuali yang diubah di bawah ini.

Tempat dan tampilan:
- Dialog tambah transaksi dan dialog edit transaksi (satu komponen yang sama).
- Tanggal & Waktu: perlebar pilihan bulan supaya nama bulan terpanjang (misalnya "September" dan "November") tampil penuh tanpa terpotong. Pakai ruang kosong di sisi kanan baris tanggal. Pilihan hari dan tahun tidak berubah. Pilihan jam dan menit tetap seperti sekarang.
- Baris Kategori: samakan tampilannya dengan baris Dompet. Label "Kategori" di kiri, di kanan kotak berbingkai dengan tinggi, bingkai, radius, padding, dan tanda panah bawah yang sama dengan kotak Dompet. Isi kotak: ikon dan nama kategori (ditambah sub kategori bila dipilih). Kalau belum dipilih, tampil teks "Pilih kategori" dengan gaya yang sama seperti "Pilih dompet". Tanda panah kanan (>) yang lama dihapus.
- Klik kotak Kategori tetap membuka panel dua kolom yang sudah ada, tanpa perubahan. Panel itu tidak diganti jadi dropdown biasa.
- Sejajarkan tinggi baris Kategori dan Dompet di kolom kanan dengan baris Tanggal & Waktu dan Total di kolom kiri, supaya garis pemisah tiap baris rata.
- Berlaku di layar lebar dan layar sempit.

Aturan:
- Hanya mengubah tampilan lewat CSS dan markup tampilan, tanpa mengubah logika.
- Semua field, validasi, dan aturan yang sudah ada tidak berubah: Waktu wajib diisi, format jam 12 atau 24 jam, titik pemisah ribuan, desimal USD, dompet harus dipilih manual, aturan sub kategori.
- Perilaku simpan, edit, dan transfer tidak berubah. Tab Transfer tidak berubah.
- Sengaja TIDAK menambah fitur baru dan tidak mengubah halaman lain.
- Tidak menambah warna, font, bayangan, atau animasi di luar dev/DESIGN.md.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka dialog tambah transaksi di PC: pilihan bulan menampilkan "Oktober" penuh. Pilih September dan November: tetap tampil penuh.
- Kotak Kategori terlihat seragam dengan kotak Dompet (tinggi, bingkai, panah bawah), dan baris kanan sejajar dengan baris kiri.
- Klik kotak Kategori: panel dua kolom terbuka dan berfungsi seperti sebelumnya. Setelah memilih, kotak menampilkan ikon, kategori, dan sub kategori.
- Kategori belum dipilih: kotak menampilkan "Pilih kategori". Simpan tanpa kategori: tetap ditolak seperti sebelumnya.
- Tambah pengeluaran, pemasukan, dan transfer: data tersimpan sama seperti sebelum perubahan.
- Edit transaksi lama: kotak Kategori terisi dengan benar.
- Perkecil jendela ke ukuran HP: tata letak satu kolom tetap rapi, bulan tidak terpotong.
- Buka halaman lain: tampilannya tidak berubah.

## Perbaikan UI PC 11: Dropdown Kategori

Tujuan: Di tampilan PC, kotak Kategori dan kotak Dompet di form Tambah/Edit Transaksi tampil simetris (font, ukuran, panah, posisi sama). Kategori di tampilan PC diganti dari tombol yang membuka panel dua kolom menjadi dropdown bertingkat.

Cakupan:
- Hanya tampilan PC, yaitu layar selebar breakpoint PC yang sudah dipakai item PC 9. Gunakan breakpoint yang sama, jangan buat yang baru.
- Tampilan di bawah breakpoint itu (mobile) TIDAK diubah sama sekali. Tombol Kategori dan panel dua kolom untuk tampilan mobile tetap ada dan tetap berfungsi seperti sekarang. Jangan hapus kodenya, cukup sembunyikan di tampilan PC.

Tempat dan tampilan (tampilan PC):
- Di form Tambah dan Edit Transaksi, baris Kategori. Dialog panel dua kolom "Kategori" tidak lagi dipakai di PC.
- Kotak Kategori dan kotak Dompet memakai satu set gaya CSS yang sama untuk keadaan tertutup: font, ketebalan font, tinggi, lebar, padding, garis, radius, dan panah. Panah Kategori dan panah Dompet sama bentuk dan sejajar. Dompet tetap elemen select bawaan, hanya gayanya yang disamakan (appearance none dan panah yang sama). Kedua kotak sejajar dengan garis baris masing-masing.
- Klik kotak Kategori membuka daftar kategori di bawah kotak. Kategori yang punya sub kategori punya tanda ">" di kanan barisnya.
- Mengarahkan kursor ke baris kategori bersub membuka daftar sub di sebelah kanannya. Baris kategori itu sendiri tidak langsung memilih kategori.
- Baris pertama di daftar sub adalah "(tanpa sub kategori)", dipilih untuk memilih kategori induknya saja.
- Kategori tanpa sub kategori (misalnya Lainnya) langsung terpilih saat diklik, tanpa tanda ">".
- Tampilan nilai yang terpilih di kotak Kategori mengikuti yang sudah ada sekarang.
- Daftar boleh discroll kalau panjang. Daftar ditutup saat klik di luar atau tekan Escape.

Aturan:
- Sub kategori tetap opsional, sama seperti sekarang.
- Kategori dan sub kategori Equilibrium tetap tidak muncul di daftar, sama seperti sekarang.
- Urutan kategori dan sub kategori mengikuti urutan yang sudah ada di Pengaturan. Ikon kategori dan sub kategori tetap tampil di barisnya.
- Keyboard: Enter atau Spasi membuka daftar, panah atas/bawah pindah baris, panah kanan membuka daftar sub, panah kiri atau Escape menutupnya, Enter memilih.
- Tidak ada perubahan logika penyimpanan, validasi, atau pengisian form saat edit transaksi. Dropdown PC dan tombol mobile mengisi nilai kategori dan sub kategori dengan cara yang sama.
- Yang sengaja TIDAK dilakukan: mengubah tampilan mobile, mengubah Dompet menjadi dropdown buatan, mengubah form transfer, mengubah halaman lain.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka Tambah Transaksi di PC: kotak Kategori dan Dompet punya font, tinggi, panah, dan posisi yang sama persis.
- Klik Kategori, arahkan kursor ke Makan: daftar sub muncul di kanan. Pilih Restoran: kotak menampilkan pilihan itu dan transaksi tersimpan dengan sub kategori Restoran.
- Pilih Makan lewat "(tanpa sub kategori)": transaksi tersimpan hanya dengan kategori Makan.
- Pilih Lainnya: langsung terpilih tanpa daftar sub.
- Edit transaksi lama yang punya sub kategori: kotak Kategori menampilkan pilihan lamanya dengan benar.
- Tes keyboard: Tab ke kotak, buka, pilih dengan panah dan Enter.
- Perkecil jendela sampai di bawah breakpoint PC: tampilan Kategori kembali seperti sebelum item ini (tombol dan panel dua kolom), dan tetap berfungsi.

## Perbaikan UI PC 12: Dropdown Dompet

Tujuan: Di tampilan PC, daftar pilihan Dompet di form Tambah/Edit Transaksi memakai tampilan yang sama persis dengan daftar Kategori dari item PC 11, supaya kedua kotak sinkron.

Cakupan:
- Hanya tampilan PC, yaitu layar selebar breakpoint PC yang sudah dipakai item PC 9 dan PC 11. Gunakan breakpoint yang sama.
- Tampilan di bawah breakpoint itu (mobile) TIDAK diubah. Di sana Dompet tetap memakai select bawaan seperti sekarang.
- Hanya field Dompet di tab Pengeluaran dan Pemasukan. Field dompet di tab Transfer tidak diubah.

Tempat dan tampilan (tampilan PC):
- Field Dompet di form Tambah dan Edit Transaksi. Klik kotak Dompet membuka daftar dompet di bawah kotak.
- Daftar Dompet memakai gaya yang sama dengan daftar Kategori PC 11: warna latar panel, garis, radius sudut, bayangan (kalau ada di daftar Kategori), tinggi baris, padding, font, ketebalan font, warna teks, sorotan baris saat kursor diarahkan, dan keadaan baris terpilih. Pakai ulang kelas CSS atau komponen yang sama dengan daftar Kategori, jangan menyalin gaya baru. Tidak ada warna, font, atau bayangan baru.
- Isi daftar: satu baris per dompet dengan teks seperti sekarang (contoh "Kas Tunai (IDR)"), tanpa ikon, tanpa tanda ">", tanpa daftar kedua di sebelah kanan. Lebar daftar sama dengan lebar kotak Dompet.
- Pilihan kosong "Pilih dompet" tidak muncul sebagai baris di daftar. Kotak tertutup menampilkan teks "Pilih dompet" (warna placeholder, sama seperti "Pilih kategori") selama belum ada dompet yang dipilih.
- Setelah memilih, kotak menampilkan nama dompet terpilih dan daftar menutup. Daftar juga menutup saat klik di luar atau tekan Escape.
- Kotak tertutup Dompet dan Kategori tetap identik (font, tinggi, panah, posisi), seperti hasil PC 11.

Aturan:
- Select Dompet yang sudah ada tetap menjadi sumber nilai. Di tampilan PC select itu disembunyikan secara visual tapi tetap ada di halaman. Dropdown baru mengisi nilai select itu lalu memicu event change, supaya semua logika yang membaca select (validasi, pengisian form saat edit, dompet dipilih manual, mata uang dompet, simpan) berjalan tanpa diubah.
- Dompet tetap kosong secara bawaan dan harus dipilih manual, sama seperti sekarang. Validasi kalau belum dipilih tetap sama.
- Urutan dompet mengikuti urutan di Pengaturan, sama seperti sekarang.
- Keyboard: Enter atau Spasi membuka daftar, panah atas/bawah pindah baris, Enter memilih, Escape menutup.
- Yang sengaja TIDAK dilakukan: mengubah tampilan mobile, mengubah tab Transfer, mengubah logika form, menambah ikon dompet, mengubah halaman lain.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka Tambah Transaksi di PC, klik Kategori lalu Dompet: panel daftar keduanya sama (latar, garis, sudut, font, tinggi baris, sorotan hover).
- Klik Dompet, pilih "Dompet Yen (JPY)": kotak menampilkan pilihan itu, dan label serta format angka Total ikut mata uang Yen seperti sebelum item ini.
- Simpan tanpa memilih dompet: validasi muncul seperti sebelumnya.
- Edit transaksi lama: kotak Dompet menampilkan dompet transaksi itu dengan benar.
- Tes keyboard: Tab ke kotak Dompet, buka dengan Enter, pilih dengan panah dan Enter, tutup dengan Escape.
- Tab Transfer: field dompetnya tidak berubah.
- Perkecil jendela sampai di bawah breakpoint PC: Dompet kembali memakai select bawaan dan tetap berfungsi.

## Perbaikan UI PC 13: Dropdown Dompet Transfer

Tujuan: Di tampilan PC, field "Dari" dan "Ke" di tab Transfer pada form Tambah/Edit Transaksi memakai dropdown yang sama persis dengan field Dompet di tab Pengeluaran dan Pemasukan (item PC 12), supaya semua kotak dompet sinkron.

Cakupan:
- Hanya tampilan PC, yaitu layar selebar breakpoint PC yang sudah dipakai item PC 9, PC 11, dan PC 12. Gunakan breakpoint yang sama.
- Tampilan di bawah breakpoint itu (mobile) TIDAK diubah. Di sana "Dari" dan "Ke" tetap select bawaan.
- Hanya field "Dari" dan "Ke" di tab Transfer. Tab Pengeluaran dan Pemasukan tidak diubah.

Tempat dan tampilan (tampilan PC):
- Kotak "Dari" dan "Ke" memakai komponen dan kelas CSS yang sama dengan dropdown Dompet dari PC 12. Pakai ulang, jangan menyalin gaya baru. Tidak ada warna, font, atau bayangan baru.
- Keadaan tertutup: font, ketebalan font, tinggi, lebar, padding, garis, radius, dan panah sama dengan kotak Dompet dan Kategori. Kotak "Dari" dan "Ke" sejajar dengan garis baris masing-masing dan tidak miring terhadap garis baris.
- Keadaan terbuka: daftar di bawah kotak dengan gaya yang sama dengan daftar Dompet PC 12 (latar panel, garis, radius, tinggi baris, font, sorotan hover, baris terpilih). Lebar daftar sama dengan lebar kotak.
- Satu baris per dompet, teks seperti sekarang (contoh "Kas Tunai (IDR)"), tanpa ikon. Pilihan kosong "Pilih dompet" tidak muncul sebagai baris, kotak tertutup menampilkan "Pilih dompet" dengan warna placeholder selama belum dipilih.
- Setelah memilih, kotak menampilkan nama dompet dan daftar menutup. Daftar juga menutup saat klik di luar atau tekan Escape.
- Hanya satu daftar terbuka dalam satu waktu: membuka "Ke" menutup "Dari", dan sebaliknya.

Aturan:
- Select "Dari" dan "Ke" yang sudah ada tetap menjadi sumber nilai. Di tampilan PC keduanya disembunyikan secara visual tapi tetap ada. Dropdown baru mengisi nilai select lalu memicu event change, supaya seluruh logika transfer yang sudah ada berjalan tanpa diubah.
- Aturan apa pun yang sekarang mempengaruhi isi pilihan di select (misalnya opsi yang dinonaktifkan, disembunyikan, atau difilter menurut pilihan di field lain atau mata uang) tetap berlaku. Karena itu daftar dropdown dibangun dari opsi select yang aktif setiap kali daftar dibuka, dan opsi yang nonaktif tidak bisa dipilih. Jangan menambah, menghapus, atau mengubah aturan itu.
- Dua field tetap kosong secara bawaan dan harus dipilih manual, validasi sama seperti sekarang. Urutan dompet mengikuti urutan di Pengaturan.
- Keyboard: Enter atau Spasi membuka daftar, panah atas/bawah pindah baris, Enter memilih, Escape menutup.
- Nama di lingkup global: semua file JS di desktop/src/ berbagi satu lingkup global, jadi nama const, let, atau function di level atas tidak boleh sama dengan yang sudah ada. Sebelum menulis kode baru, cari dulu dengan `grep -rn "formRowToWallet" desktop/src/` dan lakukan hal yang sama untuk setiap nama baru yang akan dibuat. Fungsi dan kelas dari PC 12 (termasuk formRowToWallet) dipakai ulang, jangan dideklarasikan lagi. Kalau butuh fungsi baru, beri nama yang belum ada di mana pun. Setelah selesai, cari lagi tiap nama level atas yang kamu tambah dan pastikan hanya satu deklarasi.
- Yang sengaja TIDAK dilakukan: mengubah tampilan mobile, mengubah logika transfer, mengubah tab Pengeluaran dan Pemasukan, menambah ikon dompet, mengubah halaman lain.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Muat ulang halaman dengan Ctrl+Shift+R, buka Console (F12): tidak ada tulisan merah, dan tombol di semua halaman bisa dipencet.
- Buka Tambah Transaksi di PC, tab Transfer: kotak "Dari" dan "Ke" identik dengan kotak Dompet di tab Pengeluaran (font, tinggi, panah, posisi) dan sejajar dengan garis baris.
- Buka daftar "Dari" lalu "Ke": panel daftarnya sama dengan daftar Dompet di tab Pengeluaran, dan membuka satu menutup yang lain.
- Pilih "Kas Tunai (IDR)" di "Dari" dan "Rekening Bank (IDR)" di "Ke": transfer tersimpan dan saldo kedua dompet berubah seperti sebelum item ini.
- Coba pilih dompet yang sama atau beda mata uang di "Ke" (sesuai aturan yang sudah ada): perilakunya sama persis seperti sebelum item ini (opsi dinonaktifkan, ditolak, atau pesan yang sama).
- Simpan tanpa memilih dompet: validasi muncul seperti sebelumnya.
- Edit transfer lama: "Dari" dan "Ke" menampilkan dompet yang benar.
- Tes keyboard di kedua kotak.
- Tab Pengeluaran dan Pemasukan: tidak berubah.
- Perkecil jendela sampai di bawah breakpoint PC: "Dari" dan "Ke" kembali ke select bawaan dan tetap berfungsi.

## Perbaikan UI Mobile 1: Form Transaksi Layar Penuh

Tujuan: Di tampilan mobile, form Tambah/Edit Transaksi tidak lagi berupa dialog melayang, tapi tampil layar penuh dengan field tersusun vertikal, supaya lebih lega dan catatan mudah dibaca. Ini versi mobile dari perubahan form di PC 8 sampai PC 10.

Izin dari pemilik: Aturan "tampilan mobile tidak diubah" dari item PC 8 sampai PC 13 dikecualikan untuk item ini. Yang boleh diubah di tampilan mobile hanya markup, CSS, dan JS tampilan untuk form Tambah/Edit Transaksi. Logika, struktur data, dan halaman lain tetap tidak boleh diubah.

Cakupan:
- Hanya tampilan di bawah breakpoint PC yang sudah dipakai item PC 9 sampai PC 13. Gunakan breakpoint yang sama. Tampilan PC TIDAK diubah.
- Hanya form Tambah/Edit Transaksi, semua tab (Pengeluaran, Pemasukan, Transfer).
- Buka hanya bagian "Perbaikan UI PC 8", "Perbaikan UI PC 9", dan "Perbaikan UI PC 10" di dev/PLANSPEC.md sebagai acuan urutan dan pengelompokan field. Susunan horizontal di PC diterjemahkan jadi susunan vertikal satu kolom di mobile, urutan field sama.

Tempat dan tampilan (mobile):
- Tombol tambah dan tombol edit transaksi membuka form yang menutupi seluruh layar, tanpa latar gelap di belakang dan tanpa kotak melayang.
- Bagian atas: bar tipis dengan tombol kembali (panah) di kiri dan judul "Tambah Transaksi" atau "Edit Transaksi". Pakai gaya header yang sudah ada.
- Di bawah bar: tab tipe transaksi, lalu field tersusun vertikal, satu kolom selebar layar, bisa di-scroll.
- Field Catatan dibuat lebih tinggi dan selebar layar, beberapa baris, supaya isi catatan terbaca nyaman.
- Tombol Simpan menempel di bawah layar dan selalu terlihat saat scroll, dengan memperhitungkan safe area. Tombol lain yang sekarang ada di form (misalnya Hapus) tetap ada dengan fungsi yang sama.
- Target sentuh minimal 44 px, seperti aturan yang sudah ada.
- Tidak ada warna, font, bayangan, atau animasi baru.

Aturan:
- Field "Dompet", "Kategori", "Dari", dan "Ke" di mobile tetap select bawaan. Dropdown kustom untuk mobile dikerjakan di item terpisah, jangan dikerjakan di sini.
- Tombol kembali di bar atas dan tombol back Android menutup form dengan perilaku yang sama seperti tombol tutup/batal sekarang. Jangan ubah mekanisme tombol back yang sudah ada.
- Saat keyboard muncul, field yang sedang diisi tetap terlihat dan tidak tertutup keyboard atau tombol Simpan.
- Validasi, penyimpanan, dan logika transfer tidak berubah.
- Yang sengaja TIDAK dilakukan: mengubah tampilan PC, mengubah dropdown, mengubah logika, menambah field, mengubah halaman lain.
- Jangan jalankan build. Folder mobile/capacitor/www/ dan mobile/capacitor/android/ dibangun pemilik.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Perkecil jendela di bawah breakpoint PC, klik tombol tambah: form tampil layar penuh, tanpa kotak melayang, dengan bar atas dan tombol kembali.
- Semua field tersusun vertikal satu kolom di tab Pengeluaran, Pemasukan, dan Transfer, urutannya sama dengan PC.
- Isi catatan panjang: field Catatan tinggi dan seluruh isi terbaca.
- Scroll form: tombol Simpan tetap terlihat di bawah.
- Simpan transaksi baru dan transfer: tersimpan dan saldo berubah seperti sebelumnya.
- Edit transaksi lama: data terisi benar dan tersimpan.
- Tombol kembali menutup form tanpa menyimpan. Di APK, tombol back Android juga menutup form.
- Lebarkan jendela ke ukuran PC: tampilan PC sama persis seperti sebelumnya.
- Console (F12) tidak ada tulisan merah.
- Nama di lingkup global: semua file JS di desktop/src/ berbagi satu lingkup global. Sebelum menulis kode baru, cari dengan grep setiap nama const, let, atau function level atas yang akan dibuat, dan pastikan belum ada. Setelah selesai, cek lagi bahwa tiap nama hanya satu deklarasi.

## Perbaikan UI Mobile 2: Dropdown Kustom Mobile

Tujuan: Di tampilan mobile, field Kategori, Dompet, Dari, dan Ke di form Tambah/Edit Transaksi memakai dropdown kustom yang sama dengan versi PC (PC 11, PC 12, PC 13), supaya tampilan dan perilakunya seragam di PC dan HP.

Izin dari pemilik: Aturan "tampilan mobile tidak diubah" dari item PC 11 sampai PC 13, dan aturan di item Mobile 1 bahwa dropdown mobile tetap select bawaan, dikecualikan untuk item ini. Yang boleh diubah di tampilan mobile hanya markup, CSS, dan JS tampilan untuk empat field tersebut di form Tambah/Edit Transaksi. Logika, struktur data, dan halaman lain tetap tidak boleh diubah.

Cakupan:
- Hanya tampilan di bawah breakpoint PC yang sudah dipakai item PC 9 sampai PC 13. Tampilan PC TIDAK diubah.
- Hanya field Kategori (tab Pengeluaran dan Pemasukan), Dompet (tab Pengeluaran dan Pemasukan), serta Dari dan Ke (tab Transfer).
- Buka hanya bagian "Perbaikan UI PC 11", "Perbaikan UI PC 12", dan "Perbaikan UI PC 13" di dev/PLANSPEC.md sebagai acuan isi daftar, perilaku, dan aturan. Pakai ulang komponen, fungsi, dan kelas CSS dari ketiga item itu. Jangan menyalin gaya baru dan jangan menulis ulang fungsi yang sudah ada.

Tempat dan tampilan (mobile):
- Isi daftar, teks baris, ikon (hanya bila PC 11 memakainya), keadaan kosong, dan pilihan terpilih sama persis dengan versi PC masing-masing field.
- Kotak tertutup selebar field di kolom form, tinggi minimal 44 px, dengan panah dan placeholder seperti versi PC.
- Daftar terbuka tampil tepat di bawah kotak dengan lebar sama dengan kotak. Tinggi daftar dibatasi dan bisa di-scroll di dalam daftar. Tinggi baris minimal 44 px.
- Kalau ruang di bawah kotak tidak cukup, form di-scroll otomatis sampai daftar terlihat penuh. Daftar tidak boleh tertutup tombol Simpan.
- Tidak ada warna, font, bayangan, atau animasi baru.

Aturan:
- Select bawaan tetap menjadi sumber nilai. Di mobile keempat select disembunyikan secara visual tapi tetap ada. Dropdown mengisi nilai select lalu memicu event change, seperti di PC.
- Aturan yang mempengaruhi isi pilihan (opsi dinonaktifkan, disembunyikan, atau difilter menurut field lain atau mata uang) tetap berlaku, seperti di PC 13. Daftar dibangun dari opsi select yang aktif setiap kali dibuka.
- Hanya satu daftar terbuka dalam satu waktu. Daftar menutup saat memilih, saat ketuk di luar, atau saat tekan Escape.
- Mekanisme tombol back Android dan tombol kembali dari Mobile 1 tidak diubah.
- Validasi, penyimpanan, dan logika transfer tidak berubah.
- Yang sengaja TIDAK dilakukan: mengubah tampilan PC, mengubah logika, mengubah field lain (tanggal, waktu, jumlah, catatan), menambah ikon dompet, mengubah halaman lain.
- Jangan jalankan build. Folder mobile/capacitor/www/ dan mobile/capacitor/android/ dibangun pemilik.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Perkecil jendela di bawah breakpoint PC, buka Tambah Transaksi tab Pengeluaran: Kategori dan Dompet tampil sebagai dropdown kustom, isi dan perilakunya sama dengan di PC.
- Pilih kategori dan dompet, simpan: transaksi tersimpan dengan nilai yang benar.
- Tab Transfer: Dari dan Ke dropdown kustom. Dompet yang sama atau beda mata uang diperlakukan sama persis seperti di PC dan seperti sebelum item ini.
- Buka satu daftar lalu daftar lain: yang pertama menutup.
- Ketuk di luar daftar: daftar menutup.
- Field di dekat bagian bawah layar: daftar tetap terlihat penuh dan tidak tertutup tombol Simpan.
- Edit transaksi lama dan transfer lama: nilai tampil benar.
- Simpan tanpa memilih dompet: validasi muncul seperti sebelumnya.
- Tombol kembali dan tombol back Android tetap menutup form.
- Lebarkan jendela ke ukuran PC: tampilan PC sama persis seperti sebelumnya.
- Console (F12) tidak ada tulisan merah.
- Nama di lingkup global: semua file JS di desktop/src/ berbagi satu lingkup global. Sebelum menulis kode baru, cari dengan grep setiap nama const, let, atau function level atas yang akan dibuat, dan pastikan belum ada. Setelah selesai, cek lagi bahwa tiap nama hanya satu deklarasi.

## Saran catatan lama

Tujuan: Saat mengisi Catatan di form transaksi, aplikasi menampilkan saran dari catatan yang pernah dipakai, supaya tidak perlu mengetik ulang.

Tempat dan tampilan:
- Di field Catatan pada form tambah dan edit transaksi (tab Pengeluaran dan Pemasukan), di tampilan HP dan PC.
- Saat pengguna mengetik, muncul daftar saran tepat di bawah field Catatan, berupa panel dengan satu catatan per baris.
- Bagian huruf yang cocok dengan ketikan diberi warna aksen dari dev/DESIGN.md. Huruf lain memakai warna teks biasa.
- Di HP, daftar harus tetap terlihat saat keyboard muncul. Kalau ruang di bawah field tidak cukup, daftar boleh dibuka ke atas atau dibuat bisa digulir di dalam panelnya.
- Di PC, saran bisa dipilih dengan klik, dan juga dengan tombol panah atas bawah lalu Enter. Escape menutup daftar.

Aturan:
- Sumber saran: field catatan dari transaksi yang sudah ada. Jangan simpan daftar saran di tempat lain, hitung saat dibutuhkan.
- Daftar mulai muncul setelah pengguna mengetik minimal 2 huruf.
- Pencocokan tidak membedakan huruf besar dan kecil, dan boleh cocok di bagian mana pun dari catatan (contoh: ketik "aya" memunculkan "Ayam geprek" dan "Mie ayam").
- Catatan yang sama (tanpa membedakan huruf besar kecil dan spasi di pinggir) hanya muncul satu kali.
- Urutan: yang diawali ketikan dulu, lalu yang paling sering dipakai, lalu yang paling baru dipakai.
- Tampilkan paling banyak 8 saran.
- Transaksi yang sudah ditandai terhapus tidak ikut jadi sumber saran.
- Catatan dari transfer antar dompet dan dari penyesuaian saldo (Equilibrium) tidak ikut jadi sumber saran.
- Memilih saran mengisi field Catatan dengan teks itu, menutup daftar, dan tidak mengubah field lain (kategori, dompet, total tetap seperti sebelumnya).
- Kalau tidak ada yang cocok, atau ketikan sudah sama persis dengan satu-satunya saran, daftar tidak ditampilkan.
- Daftar ikut tertutup saat field kehilangan fokus atau form ditutup.
- Hal yang sengaja TIDAK dilakukan: tidak mengisi kategori otomatis dari saran, tidak ada pengaturan baru untuk fitur ini, tidak ada saran di field lain (misalnya Deskripsi).
- Mode data contoh: saran hanya memakai transaksi data contoh yang sedang tampil.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Punya transaksi dengan catatan "Ayam geprek" dan "Mie ayam". Buka form tambah, ketik "ay" di Catatan. Hasil: kedua catatan muncul, "Ayam geprek" di atas karena diawali ketikan, huruf "ay" berwarna.
- Ketik 1 huruf saja. Hasil: tidak ada daftar.
- Pilih salah satu saran. Hasil: field Catatan terisi, daftar menutup, field lain tidak berubah.
- Hapus transaksi berisi catatan "Mie ayam", lalu ketik "mie". Hasil: "Mie ayam" tidak muncul.
- Buat dua transaksi dengan catatan "nasi goreng" dan "Nasi Goreng". Ketik "nasi". Hasil: hanya satu saran.
- Di HP dengan keyboard terbuka, ketik di Catatan. Hasil: daftar tetap terlihat dan bisa dipilih.
- Di PC, gunakan panah bawah, Enter, dan Escape pada daftar. Hasil: sesuai aturan di atas.
- Buka form edit transaksi lama, ubah catatan. Hasil: saran muncul sama seperti di form tambah.
- Masuk mode data contoh, ketik di Catatan. Hasil: saran berasal dari data contoh saja.

## Perbaikan Tauri: popup login Google

Tujuan: Tombol login Google di jendela Tauri "Mizan Okane" saat ini hanya menampilkan "Jendela login Google diblokir browser. Ketuk tombolnya lagi." karena popup tidak terbuka. Perbaiki supaya popup login Google bisa terbuka di jendela Tauri.

Tempat dan tampilan:
- Tidak ada elemen tampilan baru.

Aturan:
- Folder kerja: desktop/tauri/. Jangan ubah desktop/src.
- Cari tahu versi Tauri yang dipakai, lalu pakai cara resmi versi itu untuk menangani permintaan jendela baru (new window / window.open) dari halaman web.
- Izinkan jendela baru HANYA untuk alamat milik Google yang dipakai alur login (accounts.google.com). Alamat lain tetap ditolak.
- Alamat situs GitHub Pages, origin, dan OAuth Client ID tidak boleh berubah.
- Jangan tambah jalur login lain (misalnya membuka browser sistem atau deep link). Itu bukan bagian item ini.
- Kalau setelah popup terbuka Google menampilkan "disallowed_useragent" atau menolak login, jangan coba cara lain. Berhenti dan laporkan.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Jalankan npm run dev dari desktop/tauri (oleh pemilik). Ketuk tombol login Google. Hasil yang diharapkan: popup Google terbuka dan pesan "diblokir browser" tidak muncul.
- Setelah login berhasil, tutup dan buka lagi aplikasi. Hasil yang diharapkan: transaksi tetap ada.
- Mode tanpa login tetap berfungsi seperti sebelumnya.

## Login Google Tauri: loopback

Tujuan: Login Google di jendela Tauri "Mizan Okane" memakai browser sistem dengan redirect loopback (127.0.0.1), bukan popup di WebView2, karena Google tidak menyukai login di webview tertanam dan bisa menolaknya kapan saja.

Izin dari pemilik: Item ini boleh (a) memakai OAuth Client ID baru bertipe Desktop yang khusus dipakai di aplikasi Tauri, (b) mengubah logika login di desktop/src HANYA dengan cabang yang aktif saat berjalan di dalam Tauri, dan (c) mengubah desktop/tauri/ serta .gitignore. Yang tetap tidak boleh berubah: alamat situs GitHub Pages, origin, OAuth Client ID web yang sudah ada, dan perilaku login di browser biasa.

Tempat dan tampilan:
- Tidak ada elemen tampilan baru. Tombol login Google yang sudah ada dipakai apa adanya.

Aturan:
- Di dalam Tauri, tombol login memanggil sisi Rust. Rust membuka browser sistem ke halaman persetujuan Google, menjalankan server lokal sementara di 127.0.0.1 dengan port bebas, menangkap kode otorisasi, menukarnya jadi token memakai PKCE, lalu menyerahkan access token ke halaman web. Setelah itu server lokal ditutup.
- Scope sama dengan login web yang sudah ada (akses Drive appDataFolder). Jangan tambah scope.
- Refresh token disimpan di folder data aplikasi Tauri milik pengguna, jangan di repo. Access token yang habis diperbarui otomatis lewat refresh token supaya sinkron Drive tetap jalan.
- Client ID dan client secret Desktop dibaca dari file desktop/tauri/src-tauri/oauth-desktop.json yang dimasukkan ke .gitignore. Commit hanya file contoh oauth-desktop.example.json dengan nilai kosong. Jangan pernah menaruh nilai asli di repo, kode, atau CHANGELOG.
- Kalau file oauth-desktop.json tidak ada atau kosong, tampilkan pesan jelas di aplikasi, jangan crash.
- Gunakan cara resmi versi Tauri yang dipakai, termasuk izin (capability) agar halaman dari situs GitHub Pages boleh memanggil perintah ini. Izinkan hanya untuk origin situs itu.
- Di browser biasa dan GitHub Pages, jalur login lama tetap dipakai tanpa perubahan. Kode popup Tauri dari item sebelumnya jangan dihapus.
- Jangan tambah jalur login lain, jangan ubah mode tanpa login.
- Kalau ada langkah yang tidak bisa diselesaikan tanpa melanggar aturan di atas, berhenti dan laporkan, jangan cari jalan lain.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor. dev/SCHEMA.md tidak perlu diubah.

Cek manual:
- Pemilik mengisi oauth-desktop.json, lalu menjalankan npm run dev dari desktop/tauri. Ketuk login Google. Hasil: browser sistem terbuka ke halaman Google, setelah setuju kembali ke aplikasi dalam keadaan sudah login, tanpa pesan "diblokir".
- Tutup dan buka lagi aplikasi. Hasil: masih login dan transaksi tetap ada.
- Tambah transaksi di Tauri, cek sinkron ke perangkat lain lewat Drive.
- Hapus oauth-desktop.json sementara. Hasil: muncul pesan jelas, aplikasi tidak crash.
- Mode tanpa login di Tauri dan login di browser biasa tetap berfungsi seperti sebelumnya.

## Perbaikan Tauri: fitur form reqwest

Tujuan: Item "Login Google Tauri: loopback" gagal kompilasi di desktop/tauri/src-tauri/src/lib.rs (baris sekitar 353 dan 427) dengan error E0599: method `form` tidak ditemukan pada `RequestBuilder`. Perbaiki supaya kompilasi berhasil.

Tempat dan tampilan:
- Tidak ada elemen tampilan baru.

Aturan:
- Folder kerja: desktop/tauri/. Jangan ubah desktop/src.
- Buka desktop/tauri/src-tauri/Cargo.toml dan lihat versi reqwest yang dipakai. Di reqwest versi baru, method `form` hanya tersedia kalau fitur `form` dinyalakan. Nyalakan fitur `form` pada dependensi reqwest di Cargo.toml. Fitur lain yang sudah ada jangan dihapus.
- Jangan ubah logika di lib.rs, jangan ganti library HTTP, dan jangan tulis ulang cara kirim form.
- Perubahan pada desktop/tauri/src-tauri/Cargo.lock yang muncul karena fitur ini boleh ikut di-commit.
- Kalau fitur `form` tidak ada atau bukan penyebabnya, berhenti dan laporkan versi reqwest serta isi bagian dependensinya, jangan cari jalan lain.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor. dev/SCHEMA.md tidak perlu diubah.

Cek manual:
- Pemilik menjalankan npm run dev dari desktop/tauri. Hasil yang diharapkan: tidak ada error E0599 dan jendela "Mizan Okane" muncul.
- Ketuk login Google. Hasil yang diharapkan: browser sistem terbuka ke halaman Google.

## Kotlin 1: Kerangka

Tujuan: menyiapkan kerangka aplikasi Android native di mobile/kotlin/ yang bisa dibuild pemilik dan jalan di HP, dengan tema dari DESIGN.md dan navigasi kosong. Ini fondasi untuk item Kotlin berikutnya.

Tempat dan tampilan:
- Header tipis berisi tulisan "Mizan Okane" dengan ikon neraca di sampingnya, sama seperti header web. Ikon dibuat sebagai vector drawable dari ikon web di desktop/src.
- Bilah navigasi bawah dengan lima tab, label dan urutan sama persis dengan web: Beranda, Riwayat, Timbangan, Statistik, Pengaturan. Tombol Timbangan di tengah dan menonjol, seperti di web.
- Tiap tab menampilkan halaman kosong yang hanya memuat judul halamannya.
- Baca desktop/src hanya untuk mencocokkan label, urutan, dan ikon navigasi. Jangan ubah isi desktop/.
- Warna dan tipografi diambil dari dev/DESIGN.md, dipasang lewat tema Compose (Color, Type, Theme). Sediakan skema terang dan gelap, ikut pengaturan sistem untuk sementara. Tombol ganti tema dikerjakan di item Kotlin lain.

Aturan:
- Nama aplikasi di launcher: "Mizan Okane". Package: com.mizoar.mizanokane. Semua teks tampilan dalam bahasa Indonesia dan ditaruh di strings.xml.
- Navigasi memakai Navigation Compose. Tombol back sistem: dari tab selain Beranda kembali ke Beranda, dari Beranda keluar aplikasi.
- Dependensi hanya yang dibutuhkan kerangka: Compose, Material 3, Navigation Compose, lifecycle. Material 3 hanya sebagai komponen dasar, semua warna, font, dan bentuk wajib dari DESIGN.md.
- Kalau font di DESIGN.md butuh file font, pakai hanya yang berlisensi bebas (misalnya OFL) dan taruh di res/font. Kalau ragu soal lisensi, jangan taruh file apa pun dan laporkan ke pemilik.
- Pastikan .gitignore (di root repo atau mobile/kotlin/) mencakup semua folder dan file hasil build yang dilarang di dev/RULES.md. Tambahkan yang belum ada.
- Ikon launcher tidak diubah di item ini.
- Sengaja TIDAK dilakukan: Room atau database apa pun, Hilt atau injeksi dependensi, jaringan, login Google, data contoh, fitur apa pun dari web, dan perubahan di desktop/ atau mobile/capacitor/.
- Jangan menjalankan Gradle atau perintah build apa pun. Jangan membuat atau mengubah gradlew dan gradle-wrapper.jar.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Pemilik build dan install di HP Android 14: aplikasi terbuka tanpa crash, nama di launcher "Mizan Okane".
- Ada lima tab sesuai urutan web dengan Timbangan di tengah dan menonjol. Tiap tab diketuk menampilkan judul halamannya.
- Tombol back dari Riwayat kembali ke Beranda, dari Beranda keluar aplikasi.
- Ganti HP ke mode gelap lalu terang: warna mengikuti tema DESIGN.md dan tidak ada warna di luar itu.
- `git status --short` setelah sesi tidak memuat folder build, local.properties, atau file keystore.


## Kotlin 2: Data

Tujuan: membuat lapisan data aplikasi Kotlin di mobile/kotlin/ (penyimpanan lokal Room, model, serta impor dan ekspor JSON) yang formatnya sama persis dengan versi web, supaya backup JSON dari web bisa diimpor di Kotlin dan sebaliknya.

Tempat dan tampilan:
- Tidak ada elemen tampilan baru. Lapisan data belum disambungkan ke halaman mana pun. Tombol impor dan ekspor dikerjakan di item Pengaturan.

Aturan:
- Sumber kebenaran format data adalah dev/SCHEMA.md. Baca seluruh file itu. Untuk perilaku logika data, baca kode web di desktop/src hanya pada bagian yang berhubungan dengan data: penyimpanan, ekspor JSON, impor JSON, dan pembuatan kategori bawaan. Jangan ubah isi desktop/.
- Buat entity Room untuk semua jenis data yang ada di dev/SCHEMA.md (misalnya transaksi, dompet, kategori, sub kategori, budget, pengaturan, penyesuaian saldo, sesuai isi SCHEMA.md). Nama field, tipe, dan arti tiap field mengikuti SCHEMA.md. Jangan menambah field yang tidak ada di sana.
- Setiap data yang punya ID unik, waktu ubah, dan penanda terhapus (soft delete) di SCHEMA.md wajib menyimpannya persis sama. Jangan menghapus baris secara fisik untuk data yang di web memakai soft delete.
- Tipe angka uang mengikuti SCHEMA.md dan cara web menyimpannya, termasuk USD dengan desimal. Dilarang memakai Float atau Double untuk uang kecuali SCHEMA.md memang menyebutnya. Kalau SCHEMA.md tidak jelas, berhenti, jangan menebak, dan laporkan ke pemilik.
- Kategori induk tetap "Equilibrium" beserta sub kategorinya diperlakukan persis seperti di web (dibuat, disimpan, atau dihitung virtual sesuai kode web). Ikuti web.
- Buat DAO dan satu repository untuk operasi dasar: baca, simpan, ubah, tandai terhapus, dan baca semua data. Jangan buat logika hitung saldo, budget, Timbangan, atau statistik. Itu dikerjakan di item lain.
- Buat fungsi ekspor JSON (database ke teks JSON) dan fungsi impor JSON (teks JSON ke database) yang memakai format dan aturan penggabungan yang sama dengan web. Baca perilaku impor web di desktop/src dan tiru persis, termasuk aturan kalau ID yang sama sudah ada. Kalau aturannya tidak jelas, berhenti dan laporkan.
- Buat fungsi "hapus semua dan kembalikan ke bawaan" di level data saja (tanpa tombol), mengikuti perilaku reset setelan pabrik di web untuk data lokal. Pembersihan Drive TIDAK dikerjakan di sini.
- Gunakan kotlinx.serialization untuk JSON, Room dengan KSP untuk database. Versi KSP harus cocok dengan versi Kotlin yang sudah terpasang di proyek: baca versi Kotlin di version catalog (gradle/libs.versions.toml), pilih versi KSP yang kompatibel dengannya, dan tulis versi yang dipilih beserta alasannya di CHANGELOG. Semua versi dikunci eksplisit di version catalog, tidak boleh memakai "+" atau versi dinamis.
- Database Room versi 1, exportSchema = true, folder skema di mobile/kotlin/app/schemas/ ikut di-commit.
- Taruh kode data dalam package terpisah di bawah com.mizoar.mizanokane (misalnya data), terpisah dari kode tampilan Kotlin 1. Jangan ubah file tampilan dari Kotlin 1. Yang boleh diubah dari item sebelumnya hanya file Gradle untuk menambah dependensi dan plugin.
- Buat unit test JVM biasa (bukan instrumented test) untuk fungsi JSON: baca file contoh dari folder test resources, impor, ekspor kembali, lalu bandingkan hasilnya. File contoh JSON wajib data fiktif buatan sendiri yang strukturnya sesuai SCHEMA.md, mencakup setiap jenis data, USD desimal, data terhapus, dan sub kategori. Dilarang memakai data transaksi asli milik pemilik di repo.
- Sengaja TIDAK dilakukan: sinkron Drive, login Google, aturan gabung konflik antar perangkat, UI apa pun, ViewModel, injeksi dependensi (Hilt), dan perubahan di desktop/ atau mobile/capacitor/.

Karena tidak ada build antar item sampai akhir rangkaian Kotlin, kodenya harus benar saat dikompilasi pertama kali. Sebelum melaporkan selesai, baca ulang setiap file Kotlin yang kamu tulis atau ubah dan periksa: import, nama fungsi dan class yang dipanggil benar-benar ada, tipe dan parameter cocok, anotasi Room valid, dan semua resource yang dirujuk sudah dibuat. Jangan memakai API yang kamu tidak yakin ada di versi dependensi yang terpasang. Jangan menjalankan Gradle atau perintah build apa pun. Tulis di CHANGELOG bagian yang kamu tidak yakin bisa dikompilasi.

Data:
- Tidak ada perubahan format data. Item ini hanya membaca dan menulis format yang sudah ada di dev/SCHEMA.md. Kalau ternyata ada ketidakcocokan antara SCHEMA.md dan perilaku web, jangan ubah salah satunya, laporkan ke pemilik. Sinkron Drive belum disentuh.

Cek manual (ditunda, dijalankan pemilik setelah build pertama di akhir rangkaian Kotlin):
- Jalankan unit test JSON: semua lulus, dan data hasil impor lalu ekspor sama dengan file contoh.
- Ekspor JSON dari web (data contoh) lalu impor lewat fungsi Kotlin: jumlah transaksi, dompet, kategori, sub kategori, dan budget sama, termasuk data yang bertanda terhapus.
- Nilai USD desimal tidak berubah setelah impor dan ekspor.
- `git status --short` setelah sesi tidak memuat folder build, local.properties, atau file keystore.


## Kotlin 3: Beranda, Riwayat, form transaksi

Tujuan: Menulis ulang tiga layar inti versi web ke Jetpack Compose: Beranda, Riwayat, dan form tambah/edit transaksi (pemasukan dan pengeluaran), memakai data dari Kotlin 2. Perilaku harus sama dengan web di desktop/src.

Sumber acuan (web adalah acuan, baca seperlunya, jangan ubah):
- Buka hanya bagian-bagian ini di dev/PLANSPEC.md: v2.2 Beranda; v2.3 Riwayat dan form tambah/edit transaksi; Navigasi bulan di kartu saldo Beranda; Beranda: sembunyikan budget dan dompet bila tidak perlu; Warna kartu saldo multi mata uang; Klik dompet untuk filter riwayat; Filter bulan di Riwayat; Pencarian riwayat; Catatan di bawah angka; Dompet dipilih manual di form transaksi dan transfer; Perbaikan UI Mobile 1: Form Transaksi Layar Penuh; Perbaikan UI Mobile 2: Dropdown Kustom Mobile; Saran catatan lama; Mata uang opsional.
- Baca juga kode di desktop/src yang terkait ketiga layar itu, terutama rumus saldo, pemasukan, pengeluaran, pengelompokan per tanggal, dan format angka/tanggal/jam. Salin perilakunya, bukan gayanya.
- Tampilan mengikuti dev/DESIGN.md dan tema Compose yang sudah ada dari Kotlin 1. Jangan tambah warna, font, bayangan, atau animasi di luar itu.

Tempat dan tampilan:
- Layar Beranda dan Riwayat mengisi tempatnya di navigasi bawah yang sudah dibuat di Kotlin 1. Cek dulu file navigasi dan MainActivity yang ada, lalu ganti placeholder Beranda dan Riwayat saja. Layar lain tetap placeholder.
- Beranda: kartu saldo dengan navigasi bulan, ringkasan pemasukan dan pengeluaran, kartu dompet (bisa diklik untuk filter Riwayat), bagian budget. Budget dan dompet disembunyikan dengan aturan yang sama seperti web.
- Riwayat: daftar dikelompokkan per tanggal, dropdown dompet, dropdown bulan (opsi "Semua bulan") dengan ringkasan pemasukan dan pengeluaran di samping judul, kolom pencarian, catatan di bawah angka.
- Form tambah/edit transaksi: layar penuh di mobile dengan tab Pemasukan dan Pengeluaran, field Tanggal, Waktu (wajib, mengikuti pilihan 12 jam atau 24 jam dari pengaturan), Dompet (kosong, wajib dipilih manual), Kategori dan sub kategori (opsional), Jumlah (titik pemisah ribuan, USD dengan desimal, Yen tanpa desimal), Catatan dengan saran catatan lama. Hapus transaksi lewat konfirmasi dan memakai soft delete.

Aturan:
- Sebelum menulis kode, buka dan baca MizanRepository.kt, RoomMizanRepository.kt, InMemoryMizanRepository.kt, AppDatabase.kt, Uang.kt, DefaultData.kt, dan semua file di data/model/. Pakai nama class, fungsi, dan field yang benar-benar ada. Kalau butuh fungsi repository yang belum ada (misalnya tambah/ubah/hapus transaksi, ambil daftar sebagai Flow), tambahkan seminimal mungkin di interface dan kedua implementasinya, tanpa logika hitung saldo atau budget di repository.
- Logika hitung (saldo, total bulan, pengelompokan tanggal, filter, pencarian, saran catatan) ditaruh di file Kotlin terpisah berupa fungsi murni di package baru com.mizoar.mizanokane.domain, bukan di dalam composable. Hasilnya harus sama dengan rumus di web. Transfer tidak dihitung sebagai pemasukan atau pengeluaran.
- Semua perhitungan dan tampilan uang lewat helper object Uang. Dilarang memakai Float, Double, atau BigDecimal di luar Uang. Satuan penyimpanan tetap Long seperseratus.
- Transaksi transfer yang sudah ada tetap ditampilkan di Riwayat seperti web, tetapi membuat dan mengedit transfer BUKAN bagian item ini (itu Kotlin 4). Dompet, kategori, mata uang, dan sub kategori hanya dibaca, belum ada layar kelolanya.
- Beranda tidak menampilkan data bila belum ada dompet, sama seperti perilaku web. Jangan tambah layar, fitur, atau animasi yang tidak ada di web.
- Tidak membuat unit test baru di item ini.
- Dilarang menambah dependensi baru kecuali benar-benar perlu (misalnya lifecycle-viewmodel-compose atau navigation-compose bila belum ada). Kalau menambah, versinya dikunci eksplisit di version catalog dan dicatat di CHANGELOG.
- Kode harus benar saat dikompilasi pertama kali. Setelah selesai menulis, baca ulang SEMUA file Kotlin yang kamu tulis atau ubah dan periksa: import lengkap, nama fungsi dan class benar-benar ada, tipe cocok, resource (string, drawable) benar-benar ada. Jangan pakai API yang tidak kamu yakini ada di versi dependensi yang dikunci.
- Dilarang menjalankan ./gradlew, gradle, atau perintah build apa pun. Tulis "belum dibuild" di entri CHANGELOG.
- Dilarang mengubah desktop/, mobile/capacitor/, dan format data (dev/SCHEMA.md).

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor. Skema Room tetap versi 1, jadi folder app/schemas/ tidak boleh berubah.

Cek manual (ditunda, dijalankan pemilik setelah build pertama):
- Buka app dengan data kosong: Beranda tidak menampilkan kartu dompet atau budget, Riwayat kosong.
- Impor JSON backup contoh lewat jalur yang ada (kalau belum ada UI impor, tes lewat InMemory repository dan catat di CHANGELOG): saldo dan ringkasan bulan di Beranda sama dengan yang tampil di web untuk data yang sama.
- Tambah pengeluaran Rp 15.000 di dompet Rupiah, lalu USD 49,90: angka tampil "15.000" dan "49,90" dan tersimpan sebagai 1500000 dan 4990.
- Edit lalu hapus transaksi: hilang dari Riwayat dan saldo Beranda, tetapi tetap ada sebagai soft delete di data.
- Klik kartu dompet di Beranda: Riwayat terfilter ke dompet itu, klik lagi kembali ke Semua Dompet.
- Pilih bulan di Riwayat: ringkasan pemasukan dan pengeluaran ikut bulan itu, transfer tidak dihitung.


## Kotlin 4A: Dompet, transfer, mata uang

Tujuan: Menulis ulang fitur dompet, transfer antar dompet, dan mata uang per dompet dari versi web ke Jetpack Compose, di atas layar Kotlin 3. Perilaku harus sama dengan web di desktop/src. Kategori dan sub kategori BUKAN bagian item ini (itu Kotlin 4B).

Sumber acuan (web adalah acuan, baca seperlunya, jangan ubah):
- Buka hanya bagian-bagian ini di dev/PLANSPEC.md: Beberapa dompet; Transfer antar dompet; Mata uang per dompet, tahap 1: Rupiah dan Yen; Mata uang per dompet, tahap 2: tambah USD dengan desimal; Mata uang opsional; Hapus dompet; Geser urutan dompet dan kategori (hanya bagian dompet); Dompet dipilih manual di form transaksi dan transfer; Perbaikan UI Mobile 1: Form Transaksi Layar Penuh; Perbaikan UI Mobile 2: Dropdown Kustom Mobile.
- Baca juga kode di desktop/src yang terkait: tambah/ubah/hapus dompet, pilihan mata uang, form transfer, tampilan dan edit transfer di Riwayat, geser urutan dompet. Salin perilakunya, bukan gayanya.
- Tampilan mengikuti dev/DESIGN.md dan tema Compose yang sudah ada. Jangan tambah warna, font, bayangan, atau animasi di luar itu.

Tempat dan tampilan:
- Form transaksi layar penuh dari Kotlin 3 mendapat tab ketiga "Transfer" dengan field Dari, Ke (keduanya kosong dan wajib dipilih manual, memakai CustomDropdown yang sudah ada), Jumlah, Tanggal, Waktu, Catatan, sama seperti web. Transfer yang sudah ada di Riwayat bisa diubah dan dihapus lewat form yang sama (mode edit membuka tab Transfer).
- Layar Pengaturan di Kotlin masih placeholder. Di layar itu tambahkan SATU baris "Dompet" yang membuka layar Kelola Dompet. Jangan bangun bagian Pengaturan yang lain, itu Kotlin 7. Tambahkan rute baru di Screen.kt dan hubungkan di MainActivity.
- Layar Kelola Dompet: daftar dompet dengan nama dan mata uang, tombol tambah dompet, ubah nama, hapus dompet, dan geser urutan memakai ikon garis tiga (≡) sebagai pegangan, semuanya mengikuti web (teks, konfirmasi, aturan mata uang, aturan hapus dompet dengan riwayat tetap aman).

Aturan:
- Sebelum menulis kode, buka dan baca ulang file yang akan diubah: MizanRepository.kt, RoomMizanRepository.kt, InMemoryMizanRepository.kt, WalletEntity.kt, TransactionEntity.kt, DefaultData.kt, Uang.kt, Screen.kt, MainActivity.kt, TransactionFormScreen.kt, RiwayatScreen.kt, BerandaCalculations.kt, RiwayatCalculations.kt, dan dev/SCHEMA.md. Pakai nama class, fungsi, dan field yang benar-benar ada. Cara transfer, urutan dompet, dan mata uang dompet disimpan harus persis mengikuti dev/SCHEMA.md dan web.
- Kalau butuh fungsi repository baru (simpan/ubah/hapus dompet, simpan urutan, simpan/hapus transfer), tambahkan seminimal mungkin di interface dan kedua implementasinya, tanpa logika hitung saldo di repository.
- Periksa BerandaCalculations.kt dan RiwayatCalculations.kt: perlakuan transfer (tidak dihitung sebagai pemasukan atau pengeluaran, tetap memindahkan saldo antar dompet, satu mata uang saja) harus sama dengan web. Ubah hanya kalau berbeda dari web.
- Semua perhitungan dan tampilan uang lewat helper object Uang. Dilarang memakai Float, Double, atau BigDecimal di luar Uang. Satuan penyimpanan tetap Long seperseratus.
- Geser urutan: pakai hanya API foundation Compose yang sudah ada (pointerInput dan detectDragGestures pada pegangan ≡), tanpa library atau dependensi baru. Pakai hanya API yang kamu yakini ada di versi Compose yang dikunci.
- Tidak membuat unit test baru. Tidak mengubah kategori, sub kategori, budget, atau layar Statistik dan Timbangan. Dilarang menambah fitur yang tidak ada di web.
- Kode harus benar saat dikompilasi pertama kali. Setelah selesai menulis, baca ulang SEMUA file Kotlin yang kamu tulis atau ubah, sampai baris terakhir, dan periksa: import lengkap, nama fungsi dan class benar-benar ada, tipe cocok, resource (string, drawable) benar-benar ada. Tambahkan drawable atau string baru bila perlu.
- Dilarang menjalankan ./gradlew, gradle, atau perintah build apa pun. Tulis "belum dibuild" di entri CHANGELOG.
- Dilarang mengubah desktop/, mobile/capacitor/, dan format data (dev/SCHEMA.md).

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor. Skema Room tetap versi 1, jadi folder app/schemas/ tidak boleh berubah.

Cek manual (ditunda, dijalankan pemilik setelah build pertama):
- Buat dompet baru dengan mata uang Yen, lalu USD: Yen tanpa desimal, USD dengan 2 desimal, tersimpan sebagai Long seperseratus.
- Transfer Rp 100.000 dari dompet Rupiah A ke dompet Rupiah B: saldo A turun, saldo B naik, pemasukan dan pengeluaran bulan di Beranda tidak berubah.
- Edit lalu hapus transfer dari Riwayat: saldo kedua dompet kembali benar, transfer hilang dari Riwayat.
- Geser urutan dompet lewat ≡: urutan di Kelola Dompet, Beranda, dan dropdown dompet ikut berubah dan tetap setelah app ditutup.
- Hapus dompet yang punya riwayat: perilaku sama dengan web (riwayat tetap aman).


## Kotlin 4B: Kategori, sub kategori, ikon

Tujuan: Menulis ulang pengelolaan kategori, sub kategori, dan ikon kategori dari versi web ke Jetpack Compose, di atas layar Kotlin 3 dan 4A. Perilaku harus sama dengan web di desktop/src. Budget, Timbangan, dan penyesuaian saldo Equilibrium BUKAN bagian item ini (itu Kotlin 5).

Sumber acuan (web adalah acuan, baca seperlunya, jangan ubah):
- Buka hanya bagian-bagian ini di dev/PLANSPEC.md: Pisah kategori pemasukan dan pengeluaran; Sub kategori; Ikon kategori kustom; Ikon sub kategori; Perbaikan: dialog Pilih Ikon muncul sendiri di Pengaturan; Geser urutan dompet dan kategori (hanya bagian kategori); Kategori Equilibrium (hanya aturan bahwa kategori induk Equilibrium dan sub-nya tetap, tidak bisa diedit, dihapus, digeser, atau dipilih di form transaksi); Pengaturan per kategori (hanya bagian kategori).
- Baca juga kode di desktop/src yang terkait: tambah/ubah/hapus kategori dan sub kategori, dialog Pilih Ikon, daftar ikon dan ikon bawaan per kategori (getCategoryIconSvg dan sejenisnya), geser urutan kategori, dan apa yang terjadi pada transaksi dan budget saat kategori atau sub kategori dihapus. Salin perilakunya, bukan gayanya.
- Tampilan mengikuti dev/DESIGN.md dan tema Compose yang sudah ada. Jangan tambah warna, font, bayangan, atau animasi di luar itu.

Tempat dan tampilan:
- Di layar Pengaturan Kotlin (PengaturanScreen.kt, yang sekarang hanya punya baris "Dompet"), tambahkan satu baris "Kategori" di bawahnya yang membuka layar Kelola Kategori. Jangan bangun bagian Pengaturan yang lain, itu Kotlin 7. Tambahkan rute baru di Screen.kt dan hubungkan di MainActivity.
- Layar Kelola Kategori: pisah dua daftar, Pengeluaran dan Pemasukan, seperti web. Tiap kategori punya ikon, nama, tombol tambah sub kategori, ubah, hapus, geser urutan lewat ikon garis tiga (≡), dan daftar sub kategori di bawahnya dengan ikon, ubah, hapus, dan geser. Dialog Pilih Ikon dibuka dari ikon kategori atau sub kategori, daftar ikonnya sama dengan web.
- Kategori induk Equilibrium dan sub-nya tampil sebagai baris tetap sesuai aturan web (tidak ada tombol ubah, hapus, geser) dan tidak muncul di dropdown Kategori pada form transaksi.
- Di Riwayat, Beranda, dan form transaksi, ikon kategori dan sub kategori memakai ikon yang tersimpan (atau ikon bawaan per kategori seperti web), bukan lagi ic_category_default untuk semua. Ubah TransactionItemRow.kt dan dropdown Kategori di TransactionFormScreen.kt seperlunya untuk ini.

Aturan:
- Sebelum menulis kode, buka dan baca ulang file yang akan diubah: MizanRepository.kt, RoomMizanRepository.kt, InMemoryMizanRepository.kt, CategoryEntity.kt, SubcategoryEntity.kt, CategoryData.kt, CategoryDao.kt, DefaultData.kt, Screen.kt, MainActivity.kt, PengaturanScreen.kt, KelolaDompetScreen.kt, TransactionFormScreen.kt, TransactionItemRow.kt, dan dev/SCHEMA.md. Pakai nama class, fungsi, dan field yang benar-benar ada. Cara ikon, urutan, jenis kategori (pemasukan atau pengeluaran), dan sub kategori disimpan harus persis mengikuti dev/SCHEMA.md dan web. Id ikon yang tersimpan harus sama dengan web supaya impor dan ekspor JSON tetap kompatibel.
- Kalau butuh fungsi repository baru (simpan, ubah, hapus kategori dan sub kategori, simpan urutan), tambahkan seminimal mungkin di interface dan kedua implementasinya, tanpa logika hitung. Pastikan @Insert di CategoryDao yang dipakai untuk simpan ulang berstrategi REPLACE.
- Aturan data saat hapus kategori atau sub kategori (nasib transaksi, budget yang ikut hilang, dan sebagainya) harus persis sama dengan web. Jangan menambah aturan baru.
- Ikon: ubah SVG ikon dari web menjadi vector drawable XML (nama ic_cat_<id ikon>) dan buat satu file Kotlin (misalnya CategoryIcons.kt di ui/) berisi pemetaan id ikon ke resource drawable beserta aturan ikon bawaan seperti web. Id yang tidak dikenal jatuh ke ic_category_default. Pastikan setiap drawable yang dirujuk benar-benar dibuat.
- Pegangan geser dari KelolaDompetScreen (WalletReorderHandle dan pola drag-nya) diekstrak menjadi komponen bersama di ui/components dan dipakai ulang oleh Kelola Kategori. Perilaku Kelola Dompet tidak boleh berubah. Tanpa library atau dependensi baru, pakai hanya API foundation Compose yang kamu yakini ada di versi yang dikunci.
- Nama kategori dan sub kategori memakai validasi yang sama dengan web (kosong, duplikat, batas panjang, dan seterusnya).
- Tidak membuat unit test baru. Tidak mengubah budget, Statistik, Timbangan, dompet, atau transfer, kecuali sentuhan kecil yang tak terhindarkan untuk ikon. Dilarang menambah fitur yang tidak ada di web.
- Kode harus benar saat dikompilasi pertama kali. Setelah selesai menulis, baca ulang SEMUA file Kotlin yang kamu tulis atau ubah, sampai baris terakhir, dan periksa: import lengkap, nama fungsi dan class benar-benar ada, tipe cocok, resource (string, drawable) benar-benar ada.
- Dilarang menjalankan ./gradlew, gradle, atau perintah build apa pun. Tulis "belum dibuild" di entri CHANGELOG.
- Dilarang mengubah desktop/, mobile/capacitor/, dan format data (dev/SCHEMA.md).

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor. Skema Room tetap versi 1, jadi folder app/schemas/ tidak boleh berubah.

Cek manual (ditunda, dijalankan pemilik setelah build pertama):
- Buka Pengaturan lalu Kategori: dua daftar (Pengeluaran dan Pemasukan) tampil dengan ikon sesuai data, urutan sama dengan web untuk data yang sama.
- Tambah kategori pengeluaran baru dengan ikon pilihan sendiri: muncul di Kelola Kategori dan di dropdown Kategori form transaksi dengan ikon itu. Tambah sub kategori di bawahnya, ikon sub kategori juga bisa diganti.
- Geser urutan kategori lewat ≡: urutan di Kelola Kategori dan dropdown form ikut berubah dan tetap setelah app ditutup.
- Hapus sub kategori lalu kategori yang punya transaksi: perilaku transaksi dan budget sama dengan web.
- Kategori Equilibrium: tampil sebagai baris tetap tanpa tombol ubah, hapus, geser, dan tidak ada di dropdown Kategori form transaksi.
- Ekspor JSON lalu impor lagi: ikon dan urutan kategori kembali sama.

## Kotlin 5A: Kelola budget

Tujuan: layar untuk mengatur budget per kategori dan per sub kategori pada aplikasi Kotlin, dengan perilaku sama persis dengan versi web. Tampilan budget di Beranda sudah ada sejak Kotlin 3 dan tidak diubah.

Sumber acuan: buka hanya bagian "Budget per kategori", "Budget per sub kategori", dan "Geser urutan budget" di dev/PLANSPEC.md. Perilaku persisnya baca dari kode web di desktop/src (cari fungsi kelola budget di desktop/src/js/app.js). Kalau spek dan kode web berbeda, kode web yang jadi acuan.

Tempat dan tampilan:
- Cek di web di mana kelola budget berada dan teks apa yang tampil, lalu tiru. Kalau di web letaknya di Pengaturan, tambahkan satu baris pintu masuk di PengaturanScreen, tepat di bawah baris "Kategori", mengikuti pola baris "Dompet" dan "Kategori". Tambahkan rute baru di Screen.kt dan hubungkan di MainActivity.kt (tombol kembali dan bottom bar tersembunyi, sama seperti Kelola Kategori).
- Layar baru KelolaBudgetScreen.kt: daftar budget per kategori pengeluaran, sub kategori di bawah kategorinya, form atur nominal, hapus budget, dan geser urutan lewat komponen ReorderDragHandle yang sudah ada (jangan ubah perilakunya).
- Ikon kategori dan sub kategori pakai CategoryIcons.kt yang sudah ada.
- Semua teks pakai strings.xml bahasa Indonesia, sama dengan teks web.
- Ikuti dev/DESIGN.md, jangan tambah warna, font, bayangan, atau animasi di luar itu.

Aturan:
- Perilaku sama dengan web: kategori mana yang boleh punya budget, aturan budget sub kategori terhadap budget kategorinya, validasi nominal, dan efek hapus budget. Semua ikut web, termasuk perlakuan kategori Equilibrium.
- Semua nominal uang disimpan sebagai Long seperseratus dan semua hitung dan tampil lewat object Uang. Dilarang Float, Double, BigDecimal di luar Uang.
- Pakai BudgetEntity, SubcategoryBudgetEntity, BudgetData, BudgetDao, dan getBudgetsFlow yang sudah ada. Cek MizanRepository: kalau belum ada fungsi simpan budget, tambahkan (saveBudgets atau nama senada) di interface, RoomMizanRepository, dan InMemoryMizanRepository. Insert di BudgetDao wajib @Insert(onConflict = REPLACE). Budget yang dihapus harus benar-benar dihapus dari database (delete atau ganti seluruh isi dalam satu transaksi), jangan hanya menimpa.
- Urutan budget tersimpan dan ikut format dev/SCHEMA.md. Format data tidak boleh berubah.
- Tampilan budget di Beranda tidak diubah, kecuali satu-satunya perubahan yang memang diperlukan agar urutan baru terbaca.
- Dilarang menambah fitur yang tidak ada di web. Rollover budget ditunda, jangan dikerjakan.
- TANPA BUILD. Kode harus benar saat dikompilasi pertama kali. Setelah selesai menulis, baca ulang SEMUA file Kotlin yang ditulis atau diubah sampai baris terakhir (import ada, nama fungsi dan class ada, tipe cocok, resource ada). Jangan pakai API yang tidak yakin. Kalau menambah dependensi, kunci versinya eksplisit di libs.versions.toml. Jangan jalankan ./gradlew, gradle, atau perintah build apa pun. Tulis "belum dibuild" di CHANGELOG.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor. Skema Room tetap versi 1.

Cek manual (ditunda, dijalankan pemilik setelah build pertama):
- Atur budget sebuah kategori, lalu cek tampil sama di Beranda dan di layar kelola.
- Atur budget sub kategori, lalu cek aturannya terhadap budget kategori sama dengan web.
- Hapus sebuah budget, lalu cek hilang dari Beranda dan tidak muncul lagi setelah aplikasi dibuka ulang.
- Geser urutan budget, lalu cek urutan bertahan setelah aplikasi dibuka ulang.

## Kotlin 5B: Timbangan dan Equilibrium

Tujuan: layar Timbangan (tombol tengah navigasi) dan penyesuaian saldo dompet lewat kategori Equilibrium pada aplikasi Kotlin, dengan perilaku sama persis dengan versi web.

Sumber acuan: buka hanya bagian "Timbangan", "Penyesuaian saldo di Timbangan", "Kategori Equilibrium", "Tombol Timbangan di tengah navigasi", dan "Ikon neraca di header" di dev/PLANSPEC.md. Perilaku persisnya baca dari kode web di desktop/src (cari Timbangan dan Equilibrium di desktop/src/js/app.js dan desktop/src/index.html). Kalau spek dan kode web berbeda, kode web yang jadi acuan.

Tempat dan tampilan:
- Cek kondisi navigasi Kotlin sekarang (Screen.kt, MainActivity.kt). Kalau tombol tengah Timbangan belum ada atau masih placeholder, buat atau ganti isinya sesuai web. Layar Timbangan tampil dengan bottom bar, sama seperti layar utama lain.
- Ikon neraca di header: ikuti web (letak, fungsi, dan apakah ada). Pakai ic_neraca.xml yang sudah ada.
- Layar baru TimbanganScreen.kt: tampilan saldo per dompet, selisih, dan aksi penyesuaian saldo, persis seperti web. Teks, urutan elemen, dan kondisi kosong ikut web.
- Dialog atau form penyesuaian saldo: ikut web (input saldo sebenarnya, tampil selisih, tombol simpan dan batal).
- Semua teks pakai strings.xml bahasa Indonesia, sama dengan teks web.
- Ikuti dev/DESIGN.md, jangan tambah warna, font, bayangan, atau animasi di luar itu.

Aturan:
- Perilaku sama dengan web: cara hitung saldo dompet, cara hitung selisih, transaksi apa yang dibuat saat penyesuaian (jenis, kategori Equilibrium, nominal, tanggal dan waktu, dompet, catatan), dan perlakuan multi mata uang. Semua ikut web.
- Gunakan kategori Equilibrium yang sudah ada dari 4B. Id dan namanya tetap dan sama dengan web. Jangan ubah perilaku Kelola Kategori dan form transaksi.
- Saldo dihitung lewat calculateWalletBalances yang sudah ada. Jangan tulis ulang rumus saldo.
- Transaksi penyesuaian disimpan lewat saveTransaction yang sudah ada, dengan field dan id sesuai dev/SCHEMA.md.
- Semua nominal uang Long seperseratus dan semua hitung dan tampil lewat object Uang. Dilarang Float, Double, BigDecimal di luar Uang.
- Dilarang menambah fitur yang tidak ada di web. Kalau satu hal belum jelas di web, tiru web, jangan berimprovisasi.
- TANPA BUILD. Kode harus benar saat dikompilasi pertama kali. Setelah selesai menulis, baca ulang SEMUA file Kotlin yang ditulis atau diubah sampai baris terakhir (import ada, nama fungsi dan class ada, tipe cocok, resource ada). Jangan pakai API yang tidak yakin. Kalau menambah dependensi, kunci versinya eksplisit di libs.versions.toml. Jangan jalankan ./gradlew, gradle, atau perintah build apa pun. Tulis "belum dibuild" di CHANGELOG.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor. Skema Room tetap versi 1.

Cek manual (ditunda, dijalankan pemilik setelah build pertama):
- Buka Timbangan lewat tombol tengah navigasi, lalu cek saldo per dompet sama dengan Beranda.
- Sesuaikan saldo sebuah dompet ke angka yang berbeda, lalu cek muncul satu transaksi Equilibrium di Riwayat dan saldo dompet jadi sesuai angka itu.
- Cek di dompet berbeda mata uang (Rupiah, Yen, USD) bahwa nominal dan desimal benar.
- Tutup dan buka ulang aplikasi, lalu cek transaksi Equilibrium dan saldo tetap.

## Kotlin 6A: Statistik, tab dan grafik

Tujuan: layar Statistik pada aplikasi Kotlin dengan tab Harian, Bulanan, Tahunan, pemilih bulan, ringkasan, dan grafik pemasukan dan pengeluaran, dengan perilaku sama persis dengan versi web. Diagram donut per kategori dan detail diagram lewat klik dikerjakan di Kotlin 6B, jangan dikerjakan sekarang.

Sumber acuan: buka hanya bagian "Statistik: satu bulan terpilih", "Statistik: tab Harian, Bulanan, Tahunan", "Grafik pengeluaran bulanan", "Grafik pemasukan bulanan", dan "Perbaikan UI: grafik bulanan Statistik" di dev/PLANSPEC.md. Perilaku persisnya baca dari kode web di desktop/src (cari Statistik di desktop/src/js/app.js dan desktop/src/index.html). Kalau spek dan kode web berbeda, kode web yang jadi acuan.

Tempat dan tampilan:
- Cek kondisi navigasi Kotlin sekarang (Screen.kt, MainActivity.kt, BottomNavigationBar.kt). Kalau tab Statistik belum ada atau masih placeholder, buat atau ganti isinya sesuai web. Layar Statistik tampil dengan bottom bar, sama seperti layar utama lain.
- Layar baru StatistikScreen.kt: urutan elemen, teks, label sumbu, dan kondisi kosong ikut web. Tab Harian, Bulanan, Tahunan dan pemilih bulan ikut web, termasuk tab mana yang memakai pemilih bulan.
- Grafik digambar dengan Canvas Compose bawaan. Dilarang menambah library grafik atau dependensi baru.
- Bagian donut per kategori jangan ditampilkan sekarang. Susun layar supaya 6B bisa menambahkannya sebagai satu composable tambahan, tanpa teks atau placeholder di tempatnya.
- Semua teks pakai strings.xml bahasa Indonesia, sama dengan teks web.
- Ikuti dev/DESIGN.md, jangan tambah warna, font, bayangan, atau animasi di luar itu.

Aturan:
- Perilaku sama dengan web: transaksi mana yang dihitung dan mana yang tidak (transfer, penyesuaian Equilibrium, transaksi terhapus), cara kelompok per hari, bulan, dan tahun, dan perlakuan multi mata uang (cek apakah web memisah per mata uang atau memakai pemilih mata uang, lalu tiru).
- Taruh semua hitungan di file domain baru StatistikCalculations.kt sebagai fungsi murni. Jangan menulis ulang fungsi yang sudah ada (calculateWalletBalances, getRiwayatSummary, filterAndGroupTransactions, DateTimeFormatters). Pakai ulang kalau cocok.
- Semua nominal uang Long seperseratus dan semua hitung dan tampil lewat object Uang. Dilarang Float, Double, BigDecimal untuk nominal. Float hanya boleh untuk ukuran gambar di Canvas, dihitung dari Long di titik terakhir.
- Data diambil dari getAllTransactionsFlow dan getSettingsFlow yang sudah ada. Jangan ubah repository kecuali benar-benar perlu, dan kalau perlu, ubah di interface, RoomMizanRepository, dan InMemoryMizanRepository sekaligus.
- Dilarang menambah fitur yang tidak ada di web. Kalau satu hal belum jelas di web, tiru web, jangan berimprovisasi.
- TANPA BUILD. Kode harus benar saat dikompilasi pertama kali. Setelah selesai menulis, baca ulang SEMUA file Kotlin yang ditulis atau diubah sampai baris terakhir, termasuk setelah setiap edit (import ada, nama fungsi dan class ada, tipe cocok, resource ada, semua R.string dan R.drawable yang dipakai ada). Jangan pakai API yang tidak yakin. Kalau menambah dependensi, kunci versinya eksplisit di libs.versions.toml. Jangan jalankan ./gradlew, gradle, atau perintah build apa pun. Tulis "belum dibuild" di CHANGELOG.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor. Skema Room tetap versi 1.

Cek manual (ditunda, dijalankan pemilik setelah build pertama):
- Buka Statistik, lalu cek total pemasukan dan pengeluaran bulan terpilih sama dengan Beranda dan Riwayat.
- Pindah ke tab Harian, Bulanan, Tahunan, lalu cek angka tiap batang grafik sama dengan jumlah transaksi di Riwayat.
- Ganti bulan, lalu cek grafik dan ringkasan ikut berubah.
- Tambah transfer dan penyesuaian Equilibrium, lalu cek keduanya diperlakukan sama seperti di web.
- Cek dompet berbeda mata uang (Rupiah, Yen, USD) bahwa nominal dan desimal benar.
- Cek kondisi tanpa transaksi: tampil sesuai web.


## Kotlin 6B: Statistik, donut per kategori

Tujuan: menambahkan diagram donut per kategori untuk bulan yang dipilih di layar Statistik Kotlin, beserta detail yang muncul lewat sentuhan, dengan perilaku sama persis dengan versi web. Tab, pemilih bulan, dan grafik batang sudah ada dari Kotlin 6A dan tidak diubah.

Sumber acuan: buka hanya bagian "Diagram donut per kategori untuk bulan yang dipilih" dan "Perbaikan UI PC 5: Detail diagram Statistik lewat klik" di dev/PLANSPEC.md. Perilaku persisnya baca dari kode web di desktop/src (cari donut dan detail diagram di desktop/src/js/app.js, desktop/src/index.html, dan desktop/src/css/style.css). Kalau spek dan kode web berbeda, kode web yang jadi acuan. Kalau web membedakan perilaku klik di PC dan sentuhan di layar kecil, ikuti perilaku layar kecil.

Tempat dan tampilan:
- Di StatistikScreen.kt, tambahkan donut sebagai satu composable baru yang dipanggil di satu tempat, di posisi yang sama seperti di web (cek tab mana yang menampilkannya dan urutannya terhadap grafik batang).
- Composable donut digambar dengan Canvas Compose bawaan (drawArc). Dilarang menambah library grafik atau dependensi baru.
- Isi donut, legenda, teks tengah, dan urutan kategori ikut web: pembagian per kategori dan kategori di luar batas tampil, cara pengelompokan sisa, kondisi kosong, serta tampilan detail saat potongan atau legenda disentuh.
- Perilaku pengeluaran dan pemasukan ikut web (apakah donut mengikuti toggle tipe grafik yang sudah ada, atau punya toggle sendiri).
- Warna potongan hanya dari token di dev/DESIGN.md dan palet yang dipakai web. Dilarang menambah warna baru.
- Ikon dan nama kategori pakai CategoryIcons.kt dan data kategori yang sudah ada (getCategoriesFlow). Kategori Equilibrium diperlakukan sama seperti di web.
- Semua teks pakai strings.xml bahasa Indonesia, sama dengan teks web.
- Ikuti dev/DESIGN.md, jangan tambah warna, font, bayangan, atau animasi di luar itu.

Aturan:
- Perilaku sama dengan web: transaksi mana yang dihitung (transfer, penyesuaian Equilibrium, transaksi terhapus), cara menjumlah per kategori dan sub kategori, pembulatan persentase dan cara menjaga total 100 persen, serta perlakuan multi mata uang (ikut pemilih mata uang yang sudah ada di Statistik).
- Taruh hitungan donut di file domain baru StatistikDonutCalculations.kt sebagai fungsi murni. Pakai ulang isTxMatchingStatsType, getTxEffectiveAmount, dan hitungPersen yang sudah ada. Jangan menulis ulang fungsi yang sudah ada dan jangan ubah perilakunya.
- Semua nominal uang Long seperseratus dan semua hitung dan tampil lewat object Uang. Dilarang Float, Double, BigDecimal untuk nominal. Float hanya boleh untuk sudut dan ukuran gambar di Canvas, dihitung dari Long di titik terakhir.
- Perubahan di StatistikScreen.kt dibatasi pada pemanggilan composable donut dan state detail yang dibutuhkan. Jangan ubah grafik batang, tab, atau pemilih bulan.
- Data diambil dari getAllTransactionsFlow, getCategoriesFlow, dan getSettingsFlow yang sudah ada. Jangan ubah repository.
- Dilarang menambah fitur yang tidak ada di web. Kalau satu hal belum jelas di web, tiru web, jangan berimprovisasi.
- TANPA BUILD. Kode harus benar saat dikompilasi pertama kali. Setelah selesai menulis, baca ulang SEMUA file Kotlin yang ditulis atau diubah sampai baris terakhir, termasuk setelah setiap edit (import ada, nama fungsi dan class ada, tipe cocok, resource ada, semua R.string dan R.drawable yang dipakai ada). Jangan pakai API yang tidak yakin. Kalau menggambar teks di Canvas, pakai API yang yakin benar dan impornya lengkap, atau taruh teks sebagai composable Text di luar Canvas. Kalau menambah dependensi, kunci versinya eksplisit di libs.versions.toml. Jangan jalankan ./gradlew, gradle, atau perintah build apa pun. Tulis "belum dibuild" di CHANGELOG.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor. Skema Room tetap versi 1.

Cek manual (ditunda, dijalankan pemilik setelah build pertama):
- Buka Statistik di bulan yang punya banyak transaksi, lalu cek donut dan legenda sama dengan web, dan total persen 100.
- Sentuh satu potongan atau satu baris legenda, lalu cek detail yang muncul sama dengan web.
- Ganti bulan dan ganti mata uang, lalu cek donut ikut berubah.
- Bulan tanpa transaksi: tampil sesuai web.
- Tambah transaksi Equilibrium dan transfer, lalu cek keduanya diperlakukan sama seperti di web.

## Kotlin 7A: Pengaturan, tampilan dan zona bahaya

Tujuan: Mengganti layar Pengaturan Kotlin yang masih sementara dengan menu Pengaturan yang sama dengan web, lengkap dengan submenu Tampilan dan Zona Bahaya. Submenu Data, Tentang, dan Akun dan Sinkron dikerjakan di item lain.

Sumber acuan (baca, jangan diubah):
- desktop/src/index.html: bagian #page-pengaturan, termasuk semua submenu "tampilan" dan "bahaya".
- desktop/src/js/app.js: logika pengaturan, ganti tema, format jam, hapus semua data, reset setelan pabrik. Cari dengan grep, jangan baca seluruh file.
- Buka hanya bagian yang relevan di dev/PLANSPEC.md: "Mode gelap", "Pilihan format jam", "Hapus semua data", "Reset setelan pabrik". Kalau judulnya berbeda, cari yang paling mirip.
- dev/SCHEMA.md untuk format data.

Tempat dan tampilan:
- Halaman Pengaturan menampilkan daftar baris menu, urutan dan teks judul serta deskripsi sama persis dengan web:
  - Tampilan: "Tema dan format jam"
  - Kategori: "Pengeluaran, pemasukan, dan sub kategori" (membuka layar KelolaKategori yang sudah ada)
  - Dompet: "Kelola dompet dan rekening sumber dana" (membuka KelolaDompet yang sudah ada)
  - Budget: "Batas pengeluaran bulanan per kategori" (membuka KelolaBudget yang sudah ada)
  - Grup terpisah di paling bawah: Zona Bahaya, "Hapus seluruh data transaksi"
- Baris Data, Akun dan Sinkron, dan Tentang BELUM ditampilkan di item ini. Ditambahkan di item lain.
- Submenu Tampilan: isi dan opsi persis seperti di web (tema dan format jam). Teks, urutan, dan label ikuti web.
- Submenu Zona Bahaya: isi aksi, teks peringatan, dan dialog konfirmasi persis seperti di web (hapus semua data dan reset setelan pabrik, kalau memang keduanya ada di sana).
- Ikon baris memakai vector drawable. Buat drawable baru seperlunya, bentuknya meniru ikon SVG di web. Ikon baris Zona Bahaya memakai warna danger yang sudah ada di tema.

Aturan:
- Perilaku sama dengan web. Dilarang menambah opsi, aksi, atau teks yang tidak ada di web.
- Tema: pilihan disimpan lewat settings yang sudah ada (SettingsEntity dan SettingsDao), dan langsung berlaku ke seluruh aplikasi lewat Theme yang sudah ada. Tidak ada warna baru di luar dev/DESIGN.md.
- Format jam: pilihan disimpan di settings, dan semua tampilan jam yang sudah ada (lewat DateTimeFormatters) mengikuti pilihan itu. Kalau DateTimeFormatters belum membaca pengaturan ini, sambungkan seperlunya tanpa mengubah perilaku lain.
- Hapus semua data dan reset setelan pabrik: cakupan data yang dihapus atau dikembalikan, urutan, dan teks konfirmasi sama dengan web. Penghapusan harus benar-benar menghapus baris dari database dalam satu transaksi, bukan hanya menandai atau menimpa. Boleh menambah fungsi minimal di MizanRepository, RoomMizanRepository, InMemoryMizanRepository, dan DAO yang diperlukan. Tidak boleh mengubah fungsi yang sudah ada.
- Dialog konfirmasi destruktif harus berupa dialog Compose, bukan Toast.
- Semua perhitungan dan tampilan uang tetap lewat object Uang. Dilarang Float, Double, BigDecimal di luar Uang.
- Hal yang sengaja TIDAK dilakukan: ekspor atau impor, data contoh, kebijakan privasi, syarat, tentang pengembang, login Google, sinkron Drive.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor. Hanya tambahan fungsi hapus dan reset di repository. Room tetap versi 1, jangan ubah skema.

Cek manual (ditunda, dijalankan pemilik setelah build pertama):
- Buka Pengaturan, baris menu tampil sesuai urutan dan teks di atas. Kategori, Dompet, Budget membuka layar yang benar.
- Ganti tema di Tampilan, seluruh aplikasi berubah, tetap setelah aplikasi ditutup dan dibuka lagi.
- Ganti format jam, jam di Riwayat dan form transaksi ikut berubah.
- Zona Bahaya: dialog konfirmasi muncul dengan teks seperti web. Batal tidak menghapus apa pun. Konfirmasi menghapus data sesuai cakupan web.

## Kotlin 7B: Pengaturan, data dan tentang

Tujuan: Melengkapi menu Pengaturan Kotlin dengan submenu Data dan Tentang, sama dengan web. Baris Akun dan Sinkron dikerjakan di Kotlin 8.

Sumber acuan (baca, jangan diubah):
- desktop/src/index.html: bagian #page-pengaturan, khusus submenu "data" dan "tentang" (cari dengan grep, jangan baca seluruh file). Termasuk halaman atau dialog Kebijakan Privasi, Syarat dan Ketentuan, dan Tentang Pengembang.
- desktop/src/js/app.js dan file JS terkait: logika ekspor CSV, ekspor JSON, impor JSON, data contoh, dan tampilan Tentang. Cari dengan grep.
- Buka hanya bagian yang relevan di dev/PLANSPEC.md: "Ekspor CSV", "Impor JSON", "Backup lengkap", "Data contoh", "Data contoh lengkap", "Halaman kebijakan privasi", "Halaman syarat dan ketentuan", "Tentang Pengembang". Kalau judulnya berbeda, cari yang paling mirip.
- dev/SCHEMA.md untuk format data.
- Kode Kotlin yang sudah ada dan harus dipakai ulang: JsonBackupManager (exportToJson, importFromJson, gabung Last-Write-Wins), DefaultData, MizanRepository, SubmenuTampilanScreen dan SubmenuBahayaScreen (sebagai contoh pola layar submenu), PengaturanScreen, Screen.kt, MainActivity.

Tempat dan tampilan:
- Di PengaturanScreen, tambahkan baris menu dengan urutan, teks judul, deskripsi, dan ikon sama dengan web:
  - Data: "Ekspor CSV dan JSON, impor JSON, data contoh"
  - Tentang: "Kebijakan privasi, syarat ketentuan, dan pengembang"
- Posisi baris mengikuti urutan web: Tampilan, Kategori, Dompet, Budget, Data, (Akun dan Sinkron, belum ditampilkan di item ini), Tentang, lalu grup Zona Bahaya terpisah di bawah.
- Submenu Data: isi, tombol, teks, urutan, dan dialog konfirmasi persis seperti web.
- Submenu Tentang: isi persis seperti web. Kebijakan Privasi, Syarat dan Ketentuan, dan Tentang Pengembang memakai teks yang sama dengan web, disimpan di strings.xml atau file resource teks. Kalau di web teksnya panjang, taruh sebagai resource, jangan ditulis langsung di kode Composable.
- Ikon baris memakai vector drawable. Buat drawable baru seperlunya, bentuknya meniru ikon SVG di web.

Aturan:
- Perilaku sama dengan web. Dilarang menambah opsi, aksi, atau teks yang tidak ada di web.
- Ekspor dan impor di Android memakai Storage Access Framework (ActivityResultContracts.CreateDocument untuk ekspor, OpenDocument untuk impor), bukan izin penyimpanan lama. Nama file ekspor mengikuti web.
- Ekspor JSON memakai JsonBackupManager.exportToJson. Impor JSON memakai JsonBackupManager.importFromJson dengan aturan gabung dan dialog konfirmasi yang sama dengan web. File impor yang tidak valid harus menampilkan pesan error seperti web dan tidak boleh mengubah data apa pun.
- Ekspor CSV: kolom, urutan kolom, format tanggal dan jam, dan format angka sama dengan web. Semua angka uang lewat object Uang. Dilarang Float, Double, BigDecimal di luar Uang.
- Data contoh: isi, jumlah, dan perilaku (termasuk kalau sudah ada data, banner data contoh, dan cara menghapusnya) sama dengan web. Boleh menambah fungsi minimal di MizanRepository, RoomMizanRepository, InMemoryMizanRepository, dan DAO. Tidak boleh mengubah fungsi yang sudah ada. Perubahan data harus dalam satu transaksi.
- Kebijakan Privasi dan Syarat dan Ketentuan: kalau teks web menyebut hal yang khusus web (misalnya PWA atau IndexedDB), salin persis apa adanya dan laporkan di akhir sebagai catatan untuk pemilik, jangan diedit sendiri.
- Hal yang sengaja TIDAK dilakukan: login Google, sinkron Drive, baris Akun dan Sinkron.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor. Format file JSON dan CSV tetap sama dengan web dan dev/SCHEMA.md. Room tetap versi 1, jangan ubah skema. Hanya boleh ada tambahan fungsi repository untuk data contoh bila diperlukan.

Cek manual (ditunda, dijalankan pemilik setelah build pertama):
- Buka Pengaturan, baris Data dan Tentang tampil di posisi yang benar dengan teks seperti web.
- Ekspor JSON, lalu impor file itu di instalasi kosong: semua transaksi, dompet, kategori, budget, dan setelan kembali sama.
- Impor file JSON dari versi web (mizan-okane-backup-sample.json): data masuk dengan benar.
- Impor file yang bukan JSON atau rusak: muncul pesan error, data tidak berubah.
- Ekspor CSV: buka di spreadsheet, kolom dan angka sama dengan hasil web.
- Data contoh: tombol memunculkan data seperti web, dan bisa dihapus seperti web.
- Tentang: Kebijakan Privasi, Syarat dan Ketentuan, dan Tentang Pengembang terbuka dan bisa kembali dengan tombol back.

## Kotlin 10A: Perbaikan build

Tujuan: memperbaiki error kompilasi yang dilaporkan pemilik dari build pertama di Android Studio. Hanya yang tercantum di bawah, tidak lebih.

Tempat dan tampilan:
- Tidak ada elemen tampilan baru. Warna hanya memakai token yang sudah ada di dev/DESIGN.md.

Daftar error (semua di mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/):
1. ui/screens/StatistikScreen.kt baris 530 kolom 61, baris 809 kolom 113, baris 921 kolom 27: Unresolved reference 'danger'.
2. ui/screens/TimbanganScreen.kt baris 686, 687, 688, 689 (kolom 47): Unresolved reference 'budgetWarn'.
3. ui/screens/TimbanganScreen.kt baris 200 kolom 69: Argument type mismatch, actual 'CategoriesData' tapi 'String' diminta. Baca baris 180 sampai 220 dan deklarasi fungsi yang dipanggil di situ (terkait transaksi penyesuaian Equilibrium), lalu perbaiki pemanggilnya supaya tipe argumen cocok. Jangan ubah signature fungsi kalau pemanggil lain ikut rusak. Kalau perlu ubah signature, cek semua pemanggil.

Aturan:
- Dugaan akar masalah 1 dan 2: object MizanColors di ui/theme/Color.kt tidak punya properti danger dan budgetWarn, padahal dipakai. Baca Color.kt dan dev/DESIGN.md dulu.
- danger harus merujuk ke warna token expense yang sudah ada di MizanColors (jangan buat hex baru).
- budgetWarn harus memakai warna peringatan budget yang tertulis di dev/DESIGN.md dan sama dengan warna peringatan budget di web (desktop/src). Kalau DESIGN.md dan web tidak punya warna itu, JANGAN mengarang hex. Berhenti dan laporkan.
- Sebelum menambah properti, grep MizanColors di seluruh mobile/kotlin untuk memastikan tidak ada properti lain yang dipakai tapi belum didefinisikan (misalnya MizanColors.xxx). Kalau ada, laporkan di akhir, jangan perbaiki yang tidak ada di daftar kecuali itu properti warna yang hilang dari Color.kt.
- Jangan sentuh gradle.properties, libs.versions.toml, atau file build lain. Pemilik sudah mengaturnya.
- Jangan refactor, jangan rapikan, jangan ubah perilaku di luar daftar error.
- Dilarang menjalankan ./gradlew, gradle, atau perintah build apa pun. Kode harus benar saat dikompilasi pertama kali. Baca ulang SEMUA file Kotlin yang diubah sampai baris terakhir. Jangan pakai API yang tidak yakin.
- Aturan uang tetap: dilarang Float, Double, BigDecimal di luar object Uang (Float hanya untuk ukuran atau sudut Canvas).
- Error lain yang kamu lihat saat membaca file tapi tidak ada di daftar: catat di laporan akhir, jangan diperbaiki.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Ditunda, dijalankan pemilik setelah build ulang (Build > Make Project). Hasilnya: error 'danger', 'budgetWarn', dan type mismatch di TimbanganScreen.kt:200 hilang. Error baru yang muncul dilaporkan pemilik untuk 10B.

## Kotlin 10B: Matikan minify R8

Tujuan: app langsung force close saat dibuka. Logcat: java.lang.IllegalAccessError: Class kotlin.collections.b extended by class rc is inaccessible, dengan stack trace berisi nama class hasil acak R8 (r8-map-id). Penyebabnya R8 (minify dan pengacakan nama) merusak class Kotlin saat runtime. Perbaikannya mematikan minify dan shrink di build type yang dipakai pemilik, sementara.

Tempat dan tampilan:
- Tidak ada elemen tampilan baru.

Aturan:
- Buka mobile/kotlin/app/build.gradle.kts (atau build.gradle kalau itu yang ada). Di blok buildTypes, untuk build type "release": set isMinifyEnabled = false dan isShrinkResources = false. Kalau ada properti sejenis di build type "debug", pastikan juga false.
- Cek juga mobile/kotlin/gradle.properties: kalau ada pengaturan R8 yang aktif (misalnya android.enableR8.fullMode atau flag sejenis yang mengaktifkan optimasi agresif), laporkan saja di akhir, jangan diubah.
- Jangan hapus file proguard-rules.pro dan jangan ubah isinya.
- Jangan ubah versi dependensi, plugin, libs.versions.toml, atau signingConfigs. Jangan buat atau ubah file keystore.
- Jangan ubah kode Kotlin. Cuma file build.gradle.kts aplikasi.
- Tambahkan komentar satu baris di atas baris yang diubah: "// Minify dimatikan sementara karena R8 merusak kotlin.collections saat runtime (Kotlin 10B). Hidupkan lagi di item APK rilis dengan aturan keep yang benar."
- Dilarang menjalankan ./gradlew, gradle, atau perintah build apa pun. Baca ulang seluruh file build yang diubah sampai baris terakhir, pastikan sintaks Kotlin DSL benar dan tidak ada blok dobel.
- Izin dari pemilik: untuk item ini saja, boleh mengubah satu file build.gradle.kts aplikasi di mobile/kotlin/app/. Tidak boleh mengubah file build lain.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Ditunda, dijalankan pemilik. Pemilik build ulang (Build > Make Project), uninstall app lama dari HP, install ulang. Hasil yang diharapkan: app terbuka ke Beranda tanpa force close.
- Kalau masih force close, pemilik kirim log Logcat baru. Nama class di stack trace sekarang sudah terbaca (tanpa r8-map-id), jadi bisa dilacak ke kode.


