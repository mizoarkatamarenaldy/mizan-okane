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

