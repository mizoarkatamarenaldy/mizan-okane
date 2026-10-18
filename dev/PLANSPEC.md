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

Tujuan: Mengonversi versi desktop (khusus Windows) memakai Tauri di folder desktop/. Item ini baru dicatat supaya tidak lupa dan belum dikerjakan.

Aturan:
- Item ini baru boleh dikerjakan setelah semua fitur Tahap 5 tercentang, serta "Pemisahan folder" dan "Revisi UI PC" selesai.
- Item ini menyentuh "Keputusan terkunci" (bagian APK dan Capacitor). Sebelum dikerjakan, pemilik harus memberi prompt tersendiri dengan klausa "Izin dari pemilik" yang menyebut persis bagian mana yang boleh diubah.
- Spek rinci (target platform, nasib Capacitor, jalur login Google) ditulis pemilik bersama Claude sebelum item dikerjakan, lalu menggantikan bagian ini.

Data:
- Belum ditentukan. Ditulis saat spek rinci dibuat.

Cek manual:
- Belum ditentukan. Ditulis saat spek rinci dibuat.

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

