# PLANSPEC.md: mizan-okane

Arsip sampai v0.1.0 (2026-10-22): [dev/arsip/PLANSPEC-sampai-v0.1.0.md](arsip/PLANSPEC-sampai-v0.1.0.md).

Spesifikasi lengkap tiap item di daftar Tahap PLAN.md.

## Rollover budget
(opsional per kategori)

## APK rilis: unggah APK ke GitHub Releases
Catatan: ditunda sampai Konversi Kotlin selesai. Yang dirilis nanti APK hasil Kotlin, bukan APK Capacitor. Login Google native dikerjakan sebagai bagian Konversi Kotlin.
(dikerjakan pemilik setelah dua perbaikan APK di atas selesai dan APK dibuild ulang; APK tidak dimasukkan ke repo lewat git add)

## Persiapan rilis publik
(dikerjakan paling akhir, karena butuh link unduh, screenshot, dan fitur final; README berisi cara pakai, cara install, catatan peringatan "belum diverifikasi Google", dan cara hapus data; pastikan tidak ada keystore, token, atau data asli di repo)

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

## Kotlin 9D: Audit data dan reset

Tujuan: Memastikan bagian Pengaturan, data, CSV, mode contoh, dan reset di Kotlin berperilaku sama dengan web. Bagian ini paling berisiko ke data pemilik, jadi diaudit lebih dulu.

Tidak ada elemen tampilan baru.

Aturan:
- Folder kerja: mobile/. Acuan: desktop/src (web) dan dev/SCHEMA.md. Jangan ubah desktop/ dan mobile/capacitor/. Kalau web dan Kotlin beda, web yang benar.
- Dilarang menambah fitur. Hanya samakan perilaku Kotlin dengan web.
- Ketidaksamaan yang jelas diperbaiki di mobile/kotlin. Temuan yang tidak pasti jangan diubah, catat saja di entri CHANGELOG.
- Dilarang menjalankan build, gradlew, atau test. Pengecekan test dilakukan dengan membaca kode.
- Audit hal-hal berikut:
  - #31: Dialog "ketik HAPUS" dua langkah di SubmenuBahayaScreen dibandingkan web (app.js sekitar baris 9580 sampai 9675). Kalau web tidak meminta mengetik, hapus langkah itu di Kotlin.
  - #32 dan #40: Cakupan resetToDefaults() dibandingkan resetDatabase di db.js (sekitar baris 840 sampai 874). Termasuk flag data contoh di MetaEntity dan semua kunci demo_*.
  - #33: Pastikan web memang punya "Reset setelan pabrik" yang terpisah dari "Hapus semua data".
  - #36: ic_sun.xml dan ic_danger.xml valid, dan semua R.string baru dari 7A ada di strings.xml.
  - #37: Setelah reset, tema kembali ke bawaan web dan tampilan langsung ikut berubah.
  - #38: Saat mode contoh aktif, semua penulisan masuk ke penyimpanan demo, bukan data asli.
  - #39: Data demo disimpan sebagai string di MetaEntity (demo_mode, demo_transactions, dan seterusnya). Cek serialisasi dan ukuran, samakan dengan web.
  - #41: Baca JsonBackupManager sampai habis (sebelumnya hanya sampai baris 100). Cek JsonBackupManagerTest dan fixture-nya masih cocok dengan InMemoryMizanRepository.
  - #42: Impor ditolak saat mode contoh. Pesan dan teks dialog konfirmasi sama dengan web (app.js sekitar baris 9110 sampai 9295).
  - #43: CSV dibandingkan web (app.js sekitar baris 9325 sampai 9385): transaksi terhapus, transfer, flag dummy, urutan baris, escape, angka USD dan Yen.
  - #44: SampleData.kt dibandingkan db.js: 25 transaksi, 4 dompet, kategori, budget, flag dummy.
  - #45: "Muat ulang data contoh", "Keluar dari data contoh", dan DemoModeBanner memang ada di web.
  - #48: Semua fungsi interface MizanRepository terimplementasi di RoomMizanRepository dan InMemoryMizanRepository dengan signature sama. withTransaction (room-ktx) dipakai benar. flatMapLatest punya @OptIn(ExperimentalCoroutinesApi::class).
  - #49: SubmenuDataScreen: CreateDocument (mime type), OpenDocument (array mime), rememberLauncherForActivityResult, serta baca dan tulis stream di Dispatchers.IO dan ditutup.
  - #50: RoomMizanRepository.kt (sekitar 593 baris) tidak ada kode dobel atau terpotong. Cek fungsi getReal*.
  - #51: PengaturanScreen.kt baris 1 sampai 50 dibaca ulang setelah edit terakhir.
  - #52: URL Tentang Pengembang sama dengan web.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor. Format tetap mengikuti dev/SCHEMA.md dan tidak boleh diubah.

Cek manual:
- Aplikasi berisi data asli, mode contoh mati. Pemilik membuka "Hapus semua data". Dialog meminta konfirmasi persis seperti web, dan setelah selesai data asli kosong.
- Mode contoh aktif, pemilik menambah transaksi. Transaksi itu hanya ada di data contoh. Setelah keluar dari mode contoh, data asli tidak berubah.
- Mode contoh aktif, pemilik mencoba impor JSON. Impor ditolak dengan pesan yang sama seperti web.
- Pemilik menjalankan "Reset setelan pabrik". Tema kembali ke bawaan web dan flag serta kunci demo_* bersih.
- Pemilik ekspor CSV dari data yang berisi transaksi terhapus, transfer, dan dompet USD atau Yen. Isi dan urutan baris sama dengan CSV dari web.

## Kotlin 9B: Audit dompet, kategori, budget

Tujuan: Memastikan pengelolaan dompet, kategori, sub kategori, dan budget di Kotlin berperilaku sama dengan web, terutama saat menyimpan dan menghapus data.

Tidak ada elemen tampilan baru.

Aturan:
- Folder kerja: mobile/. Acuan: desktop/src (web) dan dev/SCHEMA.md. Jangan ubah desktop/ dan mobile/capacitor/. Kalau web dan Kotlin beda, web yang benar.
- Dilarang menambah fitur. Hanya samakan perilaku Kotlin dengan web.
- Ketidaksamaan yang jelas diperbaiki di mobile/kotlin. Temuan yang tidak pasti jangan diubah, catat saja di entri CHANGELOG.
- Dilarang menjalankan build, gradlew, atau test. Pengecekan dilakukan dengan membaca kode.
- Audit hal-hal berikut:
  - #3: Semua @Insert di WalletDao, CategoryDao, dan BudgetDao memakai OnConflictStrategy.REPLACE. Menghapus dompet, kategori, sub kategori, dan budget benar-benar menghapus barisnya dari database. Cek lewat saveWallets, saveCategories, dan saveBudgets.
  - #4: Aturan "minimal 1 dompet aktif" saat hapus dompet sama dengan web.
  - #7: KelolaKategoriScreen.kt tidak ada kode dobel atau terpotong. Berkas ini dulu sempat terpotong karena error API 500, jadi baca sampai habis.
  - #9: Hapus kategori menghapus transaksi dan budget terkait dengan cara yang sama seperti web. Ikon kategori tersimpan dengan id string yang sama seperti web.
  - #10: Baca KelolaBudgetScreen.kt (sekitar 1097 baris) sampai habis. Tidak ada kode dobel atau terpotong, dan perilakunya sama dengan web.
  - #11: Fungsi calculateBudgetSectionSummary dipanggil dengan nama dan signature yang benar di semua pemanggil.
  - #12: Urutan budget. Cek apakah yang digeser urutan budget atau urutan kategori, bandingkan dengan web dan dev/SCHEMA.md. Cek juga apakah sub kategori bisa digeser di web, lalu samakan di Kotlin.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor. Format tetap mengikuti dev/SCHEMA.md dan tidak boleh diubah.

Cek manual:
- Pemilik punya 2 dompet aktif. Hapus satu dompet, berhasil. Hapus dompet terakhir, hasilnya sama dengan web.
- Pemilik punya kategori yang berisi transaksi dan budget. Hapus kategori itu. Transaksi dan budget terkait ikut terhapus seperti di web, dan setelah aplikasi ditutup lalu dibuka lagi datanya tidak muncul kembali.
- Pemilik menghapus satu sub kategori. Setelah aplikasi dibuka ulang, sub kategori itu tetap hilang.
- Pemilik menggeser urutan budget. Setelah aplikasi dibuka ulang, urutannya bertahan dan sama dengan cara web menyimpannya.
- Pemilik memilih ikon kustom untuk satu kategori. Setelah dibuka ulang, ikon yang sama tampil.

## Kotlin 9C: Audit Timbangan dan Statistik

Tujuan: Memastikan layar Timbangan dan Statistik di mobile/kotlin sama dengan versi web. Acuan: desktop/src dan dev/SCHEMA.md. Dilarang menambah fitur. Ketidaksamaan yang jelas diperbaiki di mobile/kotlin. Yang ragu tidak diubah, cukup dicatat di entri CHANGELOG.

Tempat dan tampilan:
- Tidak ada elemen tampilan baru. Perbaikan tampilan hanya untuk menyamakan dengan web dan dev/DESIGN.md.

Aturan:
- Sudah terbukti beres, jangan diaudit lagi: versi KSP, MizanColors.danger dan budgetWarn, JsonUnquotedLiteral dan OptIn, laporan 4B, 5A, 5B, 6A.
- Hanya audit item di bawah ini. Jangan menyentuh Beranda, Riwayat, form transaksi, dompet, kategori, budget, pengaturan, data, atau reset.

Daftar audit:
- #13 TimbanganScreen.kt (sekitar 1196 baris): semua R.string yang dipakai ada di strings.xml.
- #15 Semua R.string dan R.drawable yang dipakai di kode 5A, 5B, 6A, 6B ada.
- #16 Transaksi penyesuaian Equilibrium: id kategori sama dengan web. Surplus dicatat sebagai pemasukan, defisit sebagai pengeluaran. Sub kategori chip (Bunga, Cashback, Koreksi, Biaya admin, Lupa catat) punya id yang sama dengan web. Penyimpanan lewat saveTransaction. TimbanganScreen memanggil calculateWalletBalances.
- #18 Bandingkan dengan web: kartu "Timbang Sebelum Beli", tombol "Tambah Transaksi" di Statistik kosong, bar detail periode terpilih, batas periode berjalan, jendela geser 3 sampai 6 bulan. Kalau web tidak punya, catat di CHANGELOG, jangan dihapus tanpa yakin.
- #19 Aturan hitung Statistik sama dengan web (desktop/src/app.js sekitar baris 3040 sampai 3550): transaksi Equilibrium ikut dihitung, transfer dikeluarkan.
- #20 Baca ulang penuh StatistikScreen.kt, StatistikCalculations.kt, MainActivity.kt, strings.xml (edit terakhir di 6A belum dibaca ulang).
- #21 Teks di Canvas (StatistikChartCanvas, TimbanganScaleCanvas): memakai TextMeasurer/drawText, atau nativeCanvas dengan impor yang benar.
- #22 Uang.formatCompactCurrency: periksa hasil untuk angka kecil, angka besar, dan USD. Fungsi lama tidak berubah perilakunya.
- #25 StatistikScreen.kt (sekitar 1685 baris): cari kode dobel atau terpotong.
- #27 formatDonutPercent, getTxStatsCategory, getTxSubcategory dibanding web (desktop/src/app.js sekitar baris 4040 sampai 4060). Total persen donut harus 100.
- #29 Semua R.string baru dari 6B ada. Teks di tengah donut digambar di Canvas.
- #30 getCategoriesFlow dan getSettingsFlow di StatistikScreen: collectAsState dan tipe datanya cocok.
- Warna donut: hex DONUT_* sama dengan dev/DESIGN.md.
- #28 Grep Double|Float|BigDecimal di StatistikDonutCalculations.kt harus kosong. Kalau tidak kosong, perbaiki ke Long/Int sesuai aturan uang di RULES.md dan SCHEMA.md. Float hanya boleh untuk Canvas.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual (dilakukan pemilik setelah build, bukan oleh Agy):
- Buka Timbangan, timbang satu dompet, buat penyesuaian surplus lalu defisit. Hasil: transaksi pemasukan dan pengeluaran Equilibrium muncul di Riwayat dengan sub kategori yang dipilih, saldo dompet sesuai.
- Buka Statistik dengan data contoh, pindah antar tab dan geser bulan. Hasil: angka dan persen donut sama dengan web, total persen 100, teks di grafik dan tengah donut tampil.
- Buka Statistik dengan data kosong. Hasil: tampil tombol "Tambah Transaksi" seperti di web.


## Kotlin 9A: Audit Beranda, Riwayat, Form Transaksi, Komponen Umum

Tujuan: Memastikan layar Beranda, Riwayat, form transaksi, dan komponen umum di mobile/kotlin sama dengan versi web. Acuan: desktop/src dan dev/SCHEMA.md. Dilarang menambah fitur. Ketidaksamaan yang jelas diperbaiki di mobile/kotlin. Yang ragu tidak diubah, cukup dicatat di entri CHANGELOG.

Tempat dan tampilan:
- Tidak ada elemen tampilan baru. Perbaikan tampilan hanya untuk menyamakan dengan web dan dev/DESIGN.md.

Aturan:
- Sudah terbukti beres, jangan diaudit lagi: versi KSP, MizanColors.danger dan budgetWarn, JsonUnquotedLiteral dan OptIn, laporan 4B, 5A, 5B, 6A, serta seluruh item Gelombang B, C, dan D.
- Hanya audit item di bawah ini. Jangan menyentuh Timbangan, Statistik, dompet, kategori, budget, pengaturan, data, atau reset, kecuali file yang sama memang tercantum di daftar.

Daftar audit:
- #5 Baca ulang penuh file yang belum dibaca ulang sejak Kotlin 3: TransactionItemRow, WalletBalanceItem, MonthSelector, BudgetCard, CustomDropdown, BerandaScreen, RiwayatScreen, MainActivity, dan sisa TransactionFormScreen. Cari kode dobel atau terpotong, import hilang, R.string atau R.drawable yang tidak ada, dan tipe yang tidak cocok.
- #6 Beranda: kartu dompet disembunyikan kalau jumlah dompet kurang dari 2. Cocokkan dengan web.
- #8 File yang diedit sebagian di 4B: TransactionFormScreen, BerandaScreen, RiwayatScreen, MainActivity, CustomDropdown. Pastikan edit terakhir tidak meninggalkan kode setengah jadi.
- #14 Duplikat AmountInputField (di ui/components dan di TransactionFormScreen). Pastikan hanya satu yang dipakai dan perilakunya sama dengan input nominal di web. Kalau salah satu tidak dipakai sama sekali, hapus yang tidak dipakai. Kalau keduanya dipakai dan berbeda, catat di CHANGELOG, jangan digabung tanpa yakin.
- #17 Ikon neraca di header: cek apakah web punya. Kalau web punya dan Kotlin belum, atau sebaliknya, samakan dengan web. Kalau ragu, catat.
- #34 MainActivity: rute SubmenuTampilan, SubmenuBahaya, KelolaDompet, KelolaKategori, KelolaBudget terhubung ke layar yang benar. isFullScreen dan tombol back berperilaku benar untuk tiap rute.
- #35 formatTimeDisplay(timeStr, timeFormat): semua pemanggil lama sudah memakai pengaturan format jam (12 jam atau 24 jam) dari settings, tidak ada yang masih memakai nilai tetap.
- Untuk semua item, aturan uang tetap: nominal pakai Long/Int, jangan ada Double, Float, atau BigDecimal di logika uang. Float hanya boleh untuk Canvas.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual (dilakukan pemilik setelah build, bukan oleh Agy):
- Buka Beranda dengan 1 dompet lalu dengan 2 dompet atau lebih. Hasil: kartu dompet hanya tampil saat dompet 2 atau lebih.
- Tambah transaksi dari form, isi nominal dan waktu, simpan. Hasil: transaksi muncul di Riwayat dengan format jam sesuai pengaturan.
- Ganti format jam di Pengaturan, buka Beranda dan Riwayat. Hasil: semua jam ikut berubah.
- Buka tiap submenu Pengaturan lalu tekan back. Hasil: kembali ke layar sebelumnya tanpa keluar aplikasi.

## Kotlin 9E: Audit Lintas File

Tujuan: Menyapu bersih sisa masalah lintas file di mobile/kotlin lewat grep dan pembacaan kode, sebagai penutup audit Kotlin 9. Acuan: desktop/src dan dev/SCHEMA.md. Dilarang menambah fitur. Ketidaksamaan atau kesalahan yang jelas diperbaiki di mobile/kotlin. Yang ragu tidak diubah, cukup dicatat di entri CHANGELOG.

Tempat dan tampilan:
- Tidak ada elemen tampilan baru.

Aturan:
- Sudah terbukti beres, jangan diaudit ulang: versi KSP, MizanColors.danger dan budgetWarn, JsonUnquotedLiteral dan OptIn, serta seluruh item Gelombang A, B, C, dan D yang sudah selesai.
- Hanya kerjakan tiga item di bawah ini. Jangan membaca ulang layar satu per satu.

Daftar audit:
- #23 Grep Double|Float|BigDecimal di semua file di folder domain dan ui/components (mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/). Hasil harus kosong, kecuali Float yang dipakai khusus untuk Canvas (sudut, jari-jari, koordinat gambar). Kalau ada Double, Float, atau BigDecimal di logika uang (nominal, saldo, total, persen yang mempengaruhi angka uang), perbaiki ke Long atau Int sesuai aturan uang di RULES.md dan SCHEMA.md. Kalau ada pemakaian di luar uang yang bukan Canvas dan kamu ragu, jangan diubah, catat di CHANGELOG lengkap dengan nama file dan barisnya.
- #47 Periksa mobile/kotlin/app/src/main/res/values/strings.xml:
  - apostrof (') tanpa escape (harus \')
  - tanda % tanpa penulisan %% (kecuali format argumen seperti %1$s, %1$d)
  - tanda & tanpa penulisan &amp;
  - nama string dobel (satu name dipakai lebih dari sekali)
  Perbaiki yang jelas salah. Jangan mengubah teks yang sudah benar dan jangan mengganti nama string yang sudah dipakai di kode.
- Pola inisialisasi seperti Kotlin 10C: buka hanya bagian Kotlin 10C di dev/PLANSPEC.md dan entri Kotlin 10C di dev/CHANGELOG.md (pengecualian dari aturan 5 entri teratas, hanya untuk memahami polanya). Cari object atau class lain di mobile/kotlin yang punya pola serupa, yaitu daftar atau properti yang diinisialisasi di dalam sealed class atau companion object dan bisa memicu urutan inisialisasi bermasalah atau crash. Perbaiki hanya kalau polanya jelas sama dengan 10C. Kalau mirip tapi ragu, catat di CHANGELOG.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual (dilakukan pemilik setelah build, bukan oleh Agy):
- Build ulang dan buka aplikasi. Hasil: tidak ada error resource atau crash saat start.
- Buka Beranda, Riwayat, Statistik, dan Timbangan dengan data contoh. Hasil: semua angka uang tampil sama seperti sebelumnya dan tidak ada teks aneh atau terpotong akibat perbaikan strings.xml.

## Form transaksi ikut filter dompet

Tujuan: Saat Riwayat sedang difilter ke satu dompet (misalnya lewat klik dompet di Beranda), tombol tambah transaksi langsung memakai dompet itu, jadi pengguna tidak perlu memilih dompet lagi.

Tempat dan tampilan:
- Di halaman Riwayat, tombol tambah (plus) yang sudah ada membuka form tambah transaksi dengan field Dompet sudah terisi dompet yang sedang difilter.
- Tidak ada tombol, teks, atau elemen baru.

Aturan:
- Kalau filter dompet di Riwayat aktif, field Dompet di form tambah transaksi terisi dompet itu.
- Pengguna tetap bisa mengganti dompet secara manual di form, seperti sekarang.
- Kalau tidak ada filter dompet (semua dompet), form berperilaku seperti sebelumnya.
- Mata uang di form mengikuti dompet yang terisi, memakai aturan mata uang per dompet yang sudah ada.
- Berlaku hanya untuk form tambah transaksi dari Riwayat. Form edit, form transfer, dan filter dompet tidak diubah.
- Fitur ini sengaja tidak mengubah filter dompet itu sendiri dan tidak menyimpan dompet terakhir.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Di Beranda klik dompet "A" lalu masuk ke Riwayat terfilter dompet A. Tekan tombol plus. Hasil: field Dompet di form berisi A.
- Dari kondisi yang sama, ganti dompet di form ke B lalu simpan. Hasil: transaksi tersimpan di dompet B.
- Hapus filter dompet di Riwayat (semua dompet), lalu tekan plus. Hasil: form berperilaku seperti sebelumnya.
- Filter dompet bermata uang Yen, tekan plus. Hasil: form memakai Yen.
- Buka form edit dan form transfer saat filter aktif. Hasil: tidak ada perubahan perilaku.

## Kotlin 11: Form transaksi ikut filter dompet

Tujuan: Menyamakan perilaku versi Android dengan versi web. Saat Riwayat sedang difilter ke satu dompet, tombol tambah transaksi langsung memakai dompet itu.

Tidak ada elemen tampilan baru.

Aturan:
- Perilaku harus sama dengan bagian "Form transaksi ikut filter dompet" versi web di dev/PLANSPEC.md. Kalau ragu, web yang jadi acuan.
- Kalau filter dompet di Riwayat aktif, field Dompet di form tambah transaksi terisi dompet itu.
- Pengguna tetap bisa mengganti dompet secara manual di form.
- Kalau tidak ada filter dompet (semua dompet), form berperilaku seperti sebelumnya.
- Mata uang di form mengikuti dompet yang terisi, memakai aturan mata uang per dompet yang sudah ada.
- Berlaku hanya untuk form tambah transaksi dari Riwayat. Form edit, form transfer, dan filter dompet tidak diubah.
- Fitur ini sengaja tidak mengubah filter dompet itu sendiri, tidak menyimpan dompet terakhir, dan tidak menambah fitur yang tidak ada di web.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Di Beranda tekan dompet "A" lalu masuk ke Riwayat terfilter dompet A. Tekan tombol tambah. Hasil: field Dompet di form berisi A.
- Dari kondisi yang sama, ganti dompet di form ke B lalu simpan. Hasil: transaksi tersimpan di dompet B.
- Hapus filter dompet di Riwayat (semua dompet), lalu tekan tambah. Hasil: form berperilaku seperti sebelumnya.
- Filter dompet bermata uang Yen, tekan tambah. Hasil: form memakai Yen.
- Buka form edit dan form transfer saat filter aktif. Hasil: tidak ada perubahan perilaku.



## Kotlin 10I: Perbaikan label navigasi Timbangan

Tujuan: Label "Timbangan" di bawah tombol bulat tengah pada navigasi bawah versi Kotlin terpotong, hanya sebagian huruf yang terlihat. Label harus tampil utuh seperti label tab lain.

Tempat dan tampilan:
- Bar navigasi bawah (BottomNavigationBar), tombol tengah Timbangan. Muncul di semua halaman, bukan hanya halaman Timbangan.
- Gejala: tombol hijau bulat menonjol ke atas, tapi teks "Timbangan" di bawahnya terpotong di tepi bawah bar sehingga yang terlihat hanya potongan atas huruf berwarna hijau.
- Hasil yang diinginkan: teks "Timbangan" terlihat penuh, ukuran dan gaya teks sama dengan label Beranda, Riwayat, Statistik, Pengaturan, dan sejajar dengan label-label itu di garis dasar yang sama.
- Tombol bulat tetap menonjol ke atas seperti sekarang. Warna, ukuran tombol, dan ikon tidak berubah.
- Tinggi bar boleh disesuaikan seperlunya saja supaya label muat, tanpa mengubah tampilan tab lain.

Aturan:
- Cari penyebab terpotongnya label di komponen navigasi bawah, lalu perbaiki di penyebabnya, jangan ditutup dengan trik sementara.
- Label tidak boleh dipotong atau ditimpa oleh area gesture bar atau tombol navigasi sistem Android (perhatikan window insets).
- Label di mode aktif (hijau) dan tidak aktif sama-sama harus utuh.
- Tidak menambah warna, font, bayangan, atau animasi.
- Sengaja TIDAK mengubah isi halaman Timbangan, dropdown kategori, atau komponen lain di luar navigasi bawah.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka aplikasi, masuk ke tab Timbangan: label "Timbangan" terlihat penuh di bawah tombol bulat, tidak terpotong.
- Pindah ke tab Beranda, Riwayat, Statistik, Pengaturan: label "Timbangan" tetap utuh dan semua label sejajar di garis dasar yang sama.
- Coba dengan navigasi gesture dan navigasi 3 tombol: label tidak tertutup area sistem.
- Coba mode gelap dan mode terang: tampilan sama benar.

## Animasi Timbangan (Desktop)

Tujuan: Membuat gambar timbangan di halaman Timbangan bergerak halus saat ditampilkan atau saat nilainya berubah, supaya terasa hidup dan sesuai identitas "Mizan".

Tempat dan tampilan:
- Halaman Timbangan di desktop/src, pada gambar timbangan Bulan Ini (pemasukan vs pengeluaran) yang sudah ada.
- Tidak ada teks, tombol, atau kartu baru.

Aturan:
- Saat halaman Timbangan dibuka, lengan timbangan bergerak dari posisi seimbang (datar) ke posisi miring sesuai kondisi yang sudah ada sekarang.
- Saat nilai berubah (ganti bulan lewat navigasi bulan, atau data transaksi berubah), lengan bergerak dari posisi lama ke posisi baru, bukan lompat langsung.
- Arah dan besar kemiringan memakai logika yang sudah ada. Fitur ini tidak mengubah cara kemiringan dihitung.
- Animasi hanya berlaku di gambar timbangan Bulan Ini. Elemen lain di halaman Timbangan dan halaman lain tidak dianimasikan.
- Izin dari pemilik: boleh menambah animasi pada gambar timbangan sesuai spek ini, walau animasinya tidak tercantum di dev/DESIGN.md. Dilarang menambah animasi di tempat lain, dan dilarang menambah warna, font, atau bayangan baru.
- Tidak ada pustaka animasi baru. Pakai CSS atau JavaScript yang sudah ada di proyek.
- Fitur ini sengaja TIDAK mengubah logika perhitungan, tampilan nominal, dan Kalkulator "Timbang sebelum beli".

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka halaman Timbangan dari halaman lain: lengan bergerak halus ke posisi miring yang sesuai, tanpa lompat.
- Di Timbangan, ganti bulan lewat navigasi bulan: lengan bergerak dari posisi lama ke posisi baru.
- Tambah satu transaksi pengeluaran besar lalu buka Timbangan: kemiringan sesuai dan animasinya jalan.
- Buka halaman lain (Beranda, Riwayat, Statistik, Pengaturan): tidak ada animasi baru.

## Animasi Timbangan (Kotlin)

Tujuan: Menyamakan animasi gambar timbangan di versi Kotlin dengan versi web di desktop/src, supaya perilaku kedua versi sama.

Tempat dan tampilan:
- Layar Timbangan di mobile/kotlin/, pada gambar timbangan Bulan Ini (pemasukan vs pengeluaran) yang sudah ada.
- Tidak ada teks, tombol, atau kartu baru.

Aturan:
- Saat layar Timbangan dibuka, lengan timbangan bergerak dari posisi seimbang (datar) ke posisi miring sesuai kondisi yang sudah ada sekarang.
- Saat nilai berubah (ganti bulan lewat navigasi bulan, atau data transaksi berubah), lengan bergerak dari posisi lama ke posisi baru, bukan lompat langsung.
- Arah, besar kemiringan, durasi, dan jenis gerak mengikuti versi web di desktop/src. Kalau ragu, web yang jadi acuan.
- Arah dan besar kemiringan memakai logika yang sudah ada. Fitur ini tidak mengubah cara kemiringan dihitung.
- Animasi hanya berlaku di gambar timbangan Bulan Ini. Elemen lain di layar Timbangan dan layar lain tidak dianimasikan.
- Izin dari pemilik: boleh menambah animasi pada gambar timbangan sesuai spek ini, walau animasinya tidak tercantum di dev/DESIGN.md. Dilarang menambah animasi di tempat lain, dan dilarang menambah warna, font, atau bayangan baru.
- Pakai API animasi bawaan Jetpack Compose yang sudah tersedia di proyek. Tidak ada dependensi baru di build.gradle.
- Fitur ini sengaja TIDAK mengubah logika perhitungan, tampilan nominal, dan Kalkulator "Timbang sebelum beli".
- Dilarang menjalankan ./gradlew, gradle, atau perintah build apa pun. Pemilik yang membangun.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka layar Timbangan dari layar lain: lengan bergerak halus ke posisi miring yang sesuai, tanpa lompat.
- Di Timbangan, ganti bulan lewat navigasi bulan: lengan bergerak dari posisi lama ke posisi baru.
- Tambah satu transaksi pengeluaran besar lalu buka Timbangan: kemiringan sesuai dan animasinya jalan.
- Bandingkan dengan versi web: arah, kemiringan, dan kesan geraknya sama.
- Buka layar lain (Beranda, Riwayat, Statistik, Pengaturan): tidak ada animasi baru.

## Pintasan mode gelap (Desktop)

Tujuan: pemilik bisa ganti mode gelap dan terang langsung dari header, tanpa masuk ke Pengaturan.

Tempat dan tampilan:
- Header, pojok kanan atas, satu tombol ikon di sebelah kiri ikon cloud (status sinkron).
- Tombol tampil di semua halaman yang memakai header.
- Ikon menunjukkan mode yang akan dituju: tampil ikon matahari saat mode gelap aktif, ikon bulan saat mode terang aktif.
- Tombol punya label aksesibilitas "Ganti ke mode terang" atau "Ganti ke mode gelap" sesuai mode yang akan dituju.
- Target sentuh minimal 44 px.

Aturan:
- Satu ketukan langsung mengganti tema, tanpa dialog.
- Memakai mekanisme dan penyimpanan tema yang sudah ada di Pengaturan, bukan yang baru.
- Pengaturan tema di Pengaturan dan tombol ini selalu menunjukkan keadaan yang sama, tanpa perlu muat ulang halaman.
- Pilihan tetap tersimpan setelah aplikasi ditutup dan dibuka lagi.
- Tidak menambah pilihan tema baru dan tidak mengubah isi halaman Pengaturan.
- Tidak mengubah posisi atau perilaku ikon cloud.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Kondisi awal mode terang. Ketuk tombol di header. Hasil: tampilan jadi gelap dan ikon berubah jadi matahari.
- Kondisi awal mode gelap. Ketuk tombol di header. Hasil: tampilan jadi terang dan ikon berubah jadi bulan.
- Ganti tema lewat tombol, lalu buka Pengaturan. Hasil: pilihan tema di Pengaturan sama dengan tampilan sekarang.
- Ganti tema lewat Pengaturan, lalu lihat header. Hasil: ikon tombol ikut berubah.
- Ganti tema lewat tombol, tutup dan buka lagi aplikasi. Hasil: tema tetap seperti pilihan terakhir.
- Lihat header di lebar jendela sempit dan lebar. Hasil: tombol dan ikon cloud sama-sama terlihat, tidak saling menimpa.

## Pintasan mode gelap (Kotlin)

Tujuan: pemilik bisa ganti mode gelap dan terang langsung dari header aplikasi Android, tanpa masuk ke Pengaturan, dengan perilaku yang sama seperti versi web.

Tempat dan tampilan:
- Header, pojok kanan atas, satu tombol ikon di sebelah kiri ikon cloud (status sinkron).
- Tombol tampil di semua layar yang memakai header.
- Ikon menunjukkan mode yang akan dituju: tampil ikon matahari saat mode gelap aktif, ikon bulan saat mode terang aktif.
- Tombol punya deskripsi aksesibilitas "Ganti ke mode terang" atau "Ganti ke mode gelap" sesuai mode yang akan dituju.
- Target sentuh minimal 44 px.

Aturan:
- Satu ketukan langsung mengganti tema, tanpa dialog.
- Memakai mekanisme dan penyimpanan tema yang sudah ada di Pengaturan Kotlin, bukan yang baru.
- Pengaturan tema di Pengaturan dan tombol ini selalu menunjukkan keadaan yang sama, tanpa perlu menutup dan membuka layar.
- Pilihan tetap tersimpan setelah aplikasi ditutup dan dibuka lagi.
- Tidak menambah pilihan tema baru dan tidak mengubah isi layar Pengaturan.
- Tidak mengubah posisi atau perilaku ikon cloud.
- Tidak menambah perilaku yang tidak ada di versi web.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Kondisi awal mode terang. Ketuk tombol di header. Hasil: tampilan jadi gelap dan ikon berubah jadi matahari.
- Kondisi awal mode gelap. Ketuk tombol di header. Hasil: tampilan jadi terang dan ikon berubah jadi bulan.
- Ganti tema lewat tombol, lalu buka Pengaturan. Hasil: pilihan tema di Pengaturan sama dengan tampilan sekarang.
- Ganti tema lewat Pengaturan, lalu lihat header. Hasil: ikon tombol ikut berubah.
- Ganti tema lewat tombol, tutup aplikasi sepenuhnya, lalu buka lagi. Hasil: tema tetap seperti pilihan terakhir.
- Lihat header di layar sempit. Hasil: tombol dan ikon cloud sama-sama terlihat, tidak saling menimpa.

## Arsip CHANGELOG dan PLANSPEC

Tujuan: dev/CHANGELOG.md (sekitar 220 KB) dan dev/PLANSPEC.md (sekitar 255 KB) terlalu besar, sehingga membaca keduanya menghabiskan jatah kerja Agy. Isi lama dipindah ke arsip supaya file aktif kecil, dan arsip hanya dibuka kalau perlu.

Tidak ada elemen tampilan baru.

Aturan:
- Izin dari pemilik: untuk item ini kamu boleh membuat folder dev/arsip/, memindahkan isi lama dari dev/CHANGELOG.md dan dev/PLANSPEC.md ke sana, dan menambah satu aturan baru di dev/RULES.md.
- Titik potong adalah rilis v0.1.0 (2026-10-22). Entri CHANGELOG bertanggal 2026-10-22 atau lebih lama dipindah ke dev/arsip/CHANGELOG-sampai-v0.1.0.md. Entri bertanggal setelah 2026-10-22 tetap di dev/CHANGELOG.md.
- Untuk dev/PLANSPEC.md, pindahkan ke dev/arsip/PLANSPEC-sampai-v0.1.0.md semua bagian yang itemnya sudah tercentang di dev/PLAN.md dan entri CHANGELOG-nya bertanggal 2026-10-22 atau lebih lama. Bagian yang sudah tercentang tapi tidak punya entri CHANGELOG (misalnya Tahap 1 sampai 5) ikut dipindah. Bagian yang belum tercentang, termasuk bagian "Arsip CHANGELOG dan PLANSPEC" ini sendiri, tetap di dev/PLANSPEC.md.
- Format tetap sama. Setiap file arsip memakai judul, blok format, dan format entri yang sama seperti file aslinya. Isi entri dan bagian disalin persis, tidak diringkas, tidak diubah satu karakter pun, dengan urutan yang sama seperti aslinya (CHANGELOG terbaru di atas, PLANSPEC urutan aslinya).
- File aktif dev/CHANGELOG.md dan dev/PLANSPEC.md tetap memakai judul dan blok format aslinya, ditambah satu baris penunjuk ke file arsipnya tepat di bawah judul.
- Tambahkan satu aturan di dev/RULES.md: baca hanya dev/CHANGELOG.md dan dev/PLANSPEC.md yang aktif. Folder dev/arsip/ tidak dibaca, kecuali Mizo memintanya atau fitur yang dikerjakan menyentuh fitur lama; dalam kasus itu buka hanya bagian yang terkait.
- Tidak mengubah kode, tidak menjalankan build.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Jumlah entri CHANGELOG di file aktif ditambah di arsip sama dengan 138 (sebelum entri baru item ini). Jumlah bagian PLANSPEC di file aktif ditambah di arsip sama dengan 137 (sebelum bagian baru item ini).
- Pilih tiga entri acak dari arsip dan bandingkan dengan versi lama di riwayat git: isinya identik.
- dev/CHANGELOG.md aktif hanya berisi entri bertanggal setelah 2026-10-22 dan entri item ini, dan ukurannya jauh lebih kecil dari sebelumnya.
- dev/RULES.md memuat aturan baru soal arsip.

## Widget Kotlin: ringkasan saldo

Tujuan: Menampilkan pemasukan, pengeluaran, dan saldo bulan berjalan langsung di layar utama HP, supaya pemilik bisa melihat ringkasan tanpa membuka aplikasi.

Tempat dan tampilan:
- Widget layar utama Android (Jetpack Glance), satu ukuran, muncul di daftar widget dengan nama "Mizan Okane".
- Isi widget tiga baris: "Pemasukan", "Pengeluaran", "Saldo", masing-masing dengan angkanya.
- Nama bulan berjalan tampil kecil di bagian atas widget.
- Warna dan font hanya dari dev/DESIGN.md.

Aturan:
- Angka widget sama persis dengan kartu saldo Beranda Kotlin untuk bulan berjalan, termasuk cara menangani mata uang. Pakai logika hitung yang sudah ada, jangan tulis logika hitung baru.
- Widget hanya membaca data dari Room. Widget tidak menulis atau mengubah data apa pun.
- Widget diperbarui setelah transaksi ditambah, diubah, atau dihapus di aplikasi.
- Mengetuk widget membuka aplikasi.
- Bila belum ada transaksi bulan ini, ketiga angka tampil sebagai nol.
- Fitur ini sengaja TIDAK punya tombol tambah transaksi, tidak punya pengaturan widget, dan tidak punya versi desktop.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor. dev/SCHEMA.md tidak diubah.

Cek manual:
- Pasang widget di layar utama saat ada transaksi bulan ini. Hasil: pemasukan, pengeluaran, dan saldo sama dengan kartu saldo Beranda bulan berjalan.
- Dengan widget terpasang, tambah satu pengeluaran di aplikasi lalu kembali ke layar utama. Hasil: angka pengeluaran dan saldo di widget ikut berubah.
- Ketuk widget. Hasil: aplikasi terbuka.
- Pasang widget saat tidak ada transaksi bulan ini. Hasil: ketiga angka nol, widget tidak error.

## Kotlin 10J: Perbaikan build widget

Tujuan: Memperbaiki dua error kompilasi yang dilaporkan pemilik saat build release, supaya widget dari item "Widget Kotlin: ringkasan saldo" bisa dibangun.

Tidak ada elemen tampilan baru.

Aturan:
- Perbaiki hanya dua error ini, keduanya di mobile/kotlin/app/src/main/java/.../BalanceSummaryWidget.kt:
  - Baris 21: Unresolved reference 'defaultWeight'
  - Baris 222: Unresolved reference 'ColorProvider'
- Cari penyebab di kode, lalu perbaiki dengan perubahan sekecil mungkin.
- Jangan ubah perilaku, tampilan, atau logika hitung widget.
- Jangan perbaiki peringatan atau pesan lain di log build, termasuk soal 'android.disallowKotlinSourceSets' dan versi Gradle.
- Jangan ubah file lain kecuali perbaikannya memang menuntut itu. Kalau menuntut, sebutkan alasannya di entri CHANGELOG.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Pemilik membangun ulang proyek. Hasil: dua error di atas tidak muncul lagi.
- Kalau muncul error baru, pemilik melaporkannya terpisah. Jangan diperbaiki di sesi ini.

## Kotlin 10K: Perbaikan build widget 2

Tujuan: Memperbaiki empat error kompilasi yang dilaporkan pemilik saat build release, supaya widget bisa dibangun.

Tidak ada elemen tampilan baru.

Aturan:
- Perbaiki hanya empat error ini di mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/widget/BalanceSummaryWidget.kt, baris 49 sampai 52 (TextColor, TextSecondaryColor, IncomeColor, ExpenseColor): "None of the following candidates is applicable: fun ColorProvider(color: Color), fun ColorProvider(resId: Int)".
- Penyebab: di Glance, tipe ColorProvider ada di androidx.glance.unit, sedangkan fungsi ColorProvider(day, night) ada di androidx.glance.color. Keduanya bernama sama dan butuh import sendiri-sendiri. Import androidx.glance.unit.ColorProvider tetap dipertahankan, lalu tambahkan import androidx.glance.color.ColorProvider.
- Jangan ubah perilaku, tampilan, warna, atau logika hitung widget.
- Jangan perbaiki peringatan atau pesan lain di log build.
- Jangan ubah file lain.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Pemilik membangun ulang proyek. Hasil: empat error di atas tidak muncul lagi.
- Kalau muncul error baru, pemilik melaporkannya terpisah. Jangan diperbaiki di sesi ini.

## Kotlin 10L: Perbaikan kursor input nominal

Tujuan: Saat mengetik nominal di aplikasi Kotlin, kursor selalu tetap di akhir angka walau titik pemisah ribuan muncul. Sekarang kursor melompat ke tengah angka sehingga sering salah input.

Tidak ada elemen tampilan baru.

Aturan:
- Folder kerja: mobile/. Hanya ubah file di mobile/kotlin/. Jangan ubah desktop/ dan mobile/capacitor/.
- Setiap kali angka diformat ulang dengan titik pemisah ribuan, kursor ditaruh di akhir teks.
- Bug yang diperbaiki: mengetik 1, 0, 0, 0 menghasilkan "1.00|0" (kursor di depan angka 0 terakhir). Yang benar: "1.000|".
- Berlaku untuk semua kolom input nominal di aplikasi Kotlin yang memakai pemisah ribuan.
- Hasil format angka dan nilai yang disimpan tidak berubah. Hanya posisi kursor yang diperbaiki.
- Sengaja TIDAK dilakukan: mengubah format angka, menambah fitur memindahkan kursor ke tengah angka, atau mengubah perilaku di versi web.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka form tambah transaksi, ketik 1, 0, 0, 0 satu per satu. Hasil tiap ketikan: "1|", "10|", "100|", "1.000|". Kursor selalu di akhir.
- Dari "1.000|", ketik 0 lalu 0. Hasil: "10.000|" lalu "100.000|", kursor di akhir.
- Dari "100.000|", tekan hapus satu kali. Hasil: "10.000|", kursor di akhir.
- Ulangi di kolom nominal lain (transfer, budget, penyesuaian saldo) dan hasilnya sama.

## Kotlin 10M: Transisi menu

Tujuan: mengganti animasi fade bawaan saat pindah antar menu di bar navigasi bawah versi Kotlin dengan animasi geser horizontal. Fade bawaan membuat dua halaman sempat tampak bertumpuk sehingga terasa aneh.

Izin dari pemilik: item ini boleh menambah animasi transisi antar tab di mobile/kotlin/, walaupun dev/DESIGN.md menetapkan MOTION 1 dan RULES.md melarang animasi di luar DESIGN.md serta fitur yang tidak ada di web. Izin ini hanya untuk transisi antar tab yang dijelaskan di bawah, tidak untuk animasi lain. dev/DESIGN.md tidak diubah.

Tempat dan tampilan:
- Berlaku hanya saat pindah antar item di bar navigasi bawah (sesuai item yang ada di kode).
- Urutan tab diambil dari urutan item di bar navigasi bawah, dari kiri ke kanan.
- Pindah ke tab di kanan: halaman baru masuk dari kanan, halaman lama keluar ke kiri. Pindah ke tab di kiri: sebaliknya.
- Durasi 250 ms, easing standar bawaan Compose. Tanpa fade, tanpa perubahan skala.
- Halaman yang bergeser memakai warna latar bg dari token DESIGN.md supaya halaman di belakangnya tidak tembus pandang saat bergeser.

Aturan:
- Arah geser ditentukan oleh posisi tab asal dibanding tab tujuan, bukan oleh jarak. Pindah dari tab pertama langsung ke tab terakhir tetap satu geseran, tidak melewati tab di tengah.
- Mengetuk tab yang sedang aktif tidak memicu animasi.
- Tombol back Android yang kembali ke tab sebelumnya memakai aturan arah yang sama.
- Kalau animasi sistem Android dimatikan, pindah tab terjadi instan tanpa animasi.
- Yang diubah hanya animasi transisinya. Logika navigasi, penyimpanan posisi scroll, dan isi tiap halaman tidak berubah.
- Sengaja TIDAK dilakukan: mengubah transisi layar non-tab (form transaksi, dialog, submenu Pengaturan), menyentuh animasi Timbangan yang sudah ada, atau mengubah desktop/.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Di Beranda, ketuk tab di kanannya: halaman baru masuk dari kanan, halaman lama keluar ke kiri, tanpa fade, tanpa halaman bertumpuk.
- Dari tab paling kanan, ketuk tab paling kiri: satu geseran ke arah kanan layar (halaman baru masuk dari kiri), tidak melewati tab di tengah.
- Ketuk tab yang sedang aktif: tidak ada animasi.
- Matikan animasi sistem Android (Opsi Pengembang, skala animasi mati), pindah tab: instan.
- Buka form transaksi dari tab mana pun: transisinya sama seperti sebelum item ini.

## Kotlin 10N: Animasi Timbangan gravitasi dan goyang

Tujuan: Timbangan di Kotlin terasa seperti timbangan fisik. Saat halaman dibuka, lengannya naik turun lalu berhenti di sisi yang paling berat. Saat HP digoyang, lengannya ikut bergerak lalu berhenti lagi di sisi yang paling berat.

Tempat dan tampilan:
- Halaman Timbangan di mobile/kotlin/. Tidak ada tombol, kartu, atau teks baru. Yang berubah hanya gerak lengan dan piringan Timbangan.

Aturan:
- Saat halaman Timbangan dibuka, lengan mulai dari posisi miring acak lalu berayun naik turun dengan ayunan yang makin mengecil, dan berhenti di sudut akhir sesuai berat. Sudut akhir sama dengan sudut yang dipakai animasi Timbangan sekarang.
- Ayunan memakai gerak pegas teredam (gravitasi dan redaman), bukan gerak linear. Lengan boleh melewati sudut akhir sedikit lalu kembali, sampai diam.
- Saat HP digoyang (dibaca dari sensor akselerometer), lengan terayun mengikuti kuat goyangan, lalu meredam dan berhenti di sudut akhir yang sama sesuai berat.
- Goyangan yang lemah di bawah ambang tidak memicu gerak. Goyangan beruntun tidak boleh membuat animasi menumpuk atau patah, gerak baru melanjutkan dari posisi lengan saat itu.
- Piringan tetap menggantung dan mengikuti gerak lengan.
- Pendengar sensor hanya aktif selama halaman Timbangan terbuka. Sensor dilepas saat halaman ditutup atau aplikasi ke latar belakang.
- Sudut akhir tetap dihitung dari berat seperti sekarang. Fitur ini tidak mengubah cara berat dihitung.
- Fitur ini sengaja TIDAK menambah izin Android baru, tidak menambah suara atau getar, dan tidak mengubah halaman selain Timbangan.
- Izin dari pemilik: fitur ini hanya ada di Kotlin dan boleh berbeda dari versi web di desktop/src. Jangan ubah desktop/ dan mobile/capacitor/.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka Timbangan saat sisi pemasukan lebih berat: lengan berayun naik turun beberapa kali lalu berhenti miring ke sisi pemasukan.
- Buka Timbangan saat sisi pengeluaran lebih berat: lengan berhenti miring ke sisi pengeluaran.
- Buka Timbangan lalu goyang HP: lengan terayun, lalu berhenti lagi di sudut yang sama seperti sebelum digoyang.
- Goyang HP tiga kali berturut-turut dengan cepat: gerak tetap halus, tidak patah, dan akhirnya berhenti di sudut yang benar.
- Pindah ke halaman lain lalu goyang HP: tidak ada gerak dan tidak ada error. Kembali ke Timbangan: animasi buka berjalan lagi.
- Saat berat kedua sisi sama: lengan berayun lalu berhenti lurus.

## Animasi Timbangan gravitasi dan seret (Desktop)

Tujuan: Timbangan di desktop terasa seperti timbangan fisik. Saat halaman dibuka, lengannya naik turun lalu berhenti di sisi yang paling berat. Saat lengan diseret lalu dilepas, lengannya berayun lalu berhenti lagi di sisi yang paling berat.

Tempat dan tampilan:
- Halaman Timbangan di desktop/src. Tidak ada tombol, kartu, atau teks baru. Yang berubah hanya gerak lengan dan piringan Timbangan.

Aturan:
- Saat halaman Timbangan dibuka, lengan mulai dari posisi miring acak lalu berayun naik turun dengan ayunan yang makin mengecil, dan berhenti di sudut akhir sesuai berat. Sudut akhir sama dengan sudut yang dipakai animasi Timbangan sekarang.
- Ayunan memakai gerak pegas teredam (gravitasi dan redaman), bukan gerak linear. Lengan boleh melewati sudut akhir sedikit lalu kembali, sampai diam.
- Lengan bisa diseret dengan pointer (mouse atau sentuh). Selama diseret, lengan mengikuti pointer dalam batas sudut maksimum Timbangan.
- Saat dilepas, lengan berayun dengan redaman lalu berhenti di sudut akhir yang sama sesuai berat.
- Seret baru saat ayunan masih jalan melanjutkan dari posisi lengan saat itu, tanpa patah atau menumpuk animasi.
- Piringan tetap menggantung dan mengikuti gerak lengan.
- Event pointer hanya dipasang selama halaman Timbangan terbuka dan dilepas saat halaman ditutup.
- Sudut akhir tetap dihitung dari berat seperti sekarang. Fitur ini tidak mengubah cara berat dihitung.
- Fitur ini sengaja TIDAK menambah suara, tidak menambah tombol, dan tidak mengubah halaman selain Timbangan.
- Izin dari pemilik: interaksi seret ini boleh ditambahkan di desktop/src walaupun belum ada di versi lain. Jangan ubah mobile/.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka Timbangan saat sisi pemasukan lebih berat: lengan berayun naik turun beberapa kali lalu berhenti miring ke sisi pemasukan.
- Buka Timbangan saat sisi pengeluaran lebih berat: lengan berhenti miring ke sisi pengeluaran.
- Seret lengan ke sisi yang ringan lalu lepas: lengan berayun, lalu berhenti lagi di sisi yang berat.
- Seret dan lepas tiga kali berturut-turut dengan cepat: gerak tetap halus, tidak patah, dan akhirnya berhenti di sudut yang benar.
- Pindah ke halaman lain lalu kembali ke Timbangan: animasi buka berjalan lagi dan tidak ada error di konsol.
- Saat berat kedua sisi sama: lengan berayun lalu berhenti lurus.

## Kotlin 10O: Perbaikan widget gagal dimuat

Tujuan: Widget ringkasan saldo di layar utama HP Infinix Note 30 menampilkan tulisan "Masalah memuat widget" dan tidak menampilkan isinya. Cari penyebabnya di kode, lalu perbaiki supaya widget tampil normal.

Tempat dan tampilan:
- Tidak ada elemen tampilan baru.

Aturan:
- Buka hanya bagian "Widget Kotlin: ringkasan saldo" di dev/PLANSPEC.md.
- Telusuri dari kode di mobile/kotlin/: kelas widget (BalanceSummaryWidget.kt), kelas receiver widget, deklarasi receiver di AndroidManifest.xml, file XML appwidget-provider di res/xml, dan dependency Glance di build.gradle.kts.
- Periksa hal berikut dan perbaiki yang salah: XML appwidget-provider punya android:initialLayout yang valid; receiver di manifest memakai nama kelas yang benar, punya intent-filter APPWIDGET_UPDATE, dan punya meta-data android.appwidget.provider yang menunjuk file XML yang benar; versi dependency Glance cocok satu sama lain; provideGlance tidak melempar exception.
- Kalau pembacaan data di provideGlance gagal atau datanya kosong, widget tidak boleh crash. Tampilkan keadaan yang sama dengan data kosong.
- Perbaiki hanya penyebab yang ditemukan. Tulis penyebabnya (nama file dan baris) di entri CHANGELOG dan di laporan akhir.
- Tidak mengubah logika hitung ringkasan saldo, tampilan widget, atau format data.
- Tidak mengubah desktop/ dan mobile/capacitor/. Tidak menjalankan ./gradlew atau perintah build apa pun.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Mizo build ulang APK, hapus widget lama dari layar utama Infinix Note 30, lalu pasang lagi dari daftar widget. Hasil yang diharapkan: widget menampilkan ringkasan saldo, tanpa tulisan "Masalah memuat widget".
- Dengan widget terpasang, tambah satu transaksi di aplikasi. Hasil yang diharapkan: angka di widget ikut berubah.
- Restart HP. Hasil yang diharapkan: widget tetap tampil normal.

## Perbaikan UI PC 14: Field Harga Barang Timbangan

Tujuan: Memperbaiki field "Harga Barang (Rp)" di kartu "Timbang Sebelum Beli" pada halaman Timbangan versi desktop, karena tampilannya tabrakan dengan label dan field di bawahnya.

Tempat dan tampilan:
- Halaman Timbangan versi desktop (desktop/src), kartu "Timbang Sebelum Beli", field "Harga Barang (Rp)".
- Saat ini awalan "Rp" tampil lebih besar dari teks lain dan berada di luar kotak input. Angka "0" turun ke bawah awalan. Kotak input menimpa label "Kategori Pengeluaran" di bawahnya.
- Setelah diperbaiki, awalan "Rp" dan angka berada di dalam satu kotak input, sejajar secara vertikal di tengah kotak.
- Ukuran font awalan "Rp" sama dengan ukuran font angka di dalam input.
- Tinggi kotak input sama dengan tinggi dropdown "Kategori Pengeluaran" dan "Dompet Sumber Dana".
- Jarak antara label, kotak input, dan label berikutnya sama dengan jarak antar field lain di kartu itu. Tidak ada elemen yang saling menimpa.
- Lebar kotak input sama dengan lebar dropdown di bawahnya.

Aturan:
- Perbaikan hanya pada CSS dan susunan elemen field Harga Barang. Cara kerja input tidak berubah: pemisah ribuan, format angka, dan simulasi tetap seperti sekarang.
- Dibandingkan dengan field uang di form transaksi desktop, gaya field ini harus terasa konsisten. Pakai gaya yang sudah ada, jangan buat gaya baru.
- Tidak mengubah halaman lain, kartu "Timbangan Bulan Ini", navigasi bulan, atau ukuran timbangan.
- Tidak mengubah apa pun di mobile/.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka Timbangan di desktop dengan jendela lebar. Field Harga Barang tampil satu kotak utuh, "Rp" dan "0" sejajar, tidak menimpa label "Kategori Pengeluaran".
- Ketik 1500000 di field itu. Tampil "1.500.000", posisi "Rp" tidak bergeser, dan hasil simulasi di bawah tetap benar.
- Kecilkan lalu besarkan jendela desktop. Field tidak tabrakan dengan elemen lain di lebar mana pun.
- Mode gelap dan mode terang sama-sama rapi.

## Perbaikan UI PC 15: Ukuran Timbangan

Tujuan: Memperbesar gambar timbangan di kartu "Timbangan Bulan Ini" pada halaman Timbangan versi desktop, supaya mengisi ruang kosong di kartu dan terlihat pas.

Tempat dan tampilan:
- Halaman Timbangan versi desktop (desktop/src), kartu "Timbangan Bulan Ini" di sisi kanan.
- Saat ini gambar timbangan terlalu kecil dan ada banyak ruang kosong di sekitarnya, antara header kartu (judul dan navigasi bulan) dan bagian di bawahnya (angka Pemasukan, Pengeluaran, dan kotak Defisit atau Surplus).
- Setelah diperbaiki, gambar timbangan diperbesar sampai mengisi ruang kosong itu, berada di tengah secara horizontal dan vertikal pada area antara header kartu dan angka Pemasukan dan Pengeluaran.
- Proporsi gambar timbangan tidak berubah (tidak melar atau gepeng).
- Ukuran gambar mengikuti ukuran area kartu, jadi tetap pas saat jendela desktop dibesarkan atau dikecilkan.
- Gambar tidak terpotong, tidak menimpa header kartu, dan tidak menimpa angka Pemasukan dan Pengeluaran di bawahnya.
- Halaman tetap tanpa scroll seperti hasil "Perbaikan UI PC 4: Timbangan tanpa scroll".

Aturan:
- Perubahan hanya pada ukuran dan penempatan gambar timbangan lewat CSS atau atribut ukuran gambarnya.
- Animasi timbangan (miring, gravitasi, seret) tetap berfungsi dan perilakunya tidak berubah.
- Tidak mengubah kartu "Timbang Sebelum Beli" di kiri, navigasi bulan, kartu Equilibrium, atau teks dan angka di kartu kanan.
- Tidak mengubah apa pun di mobile/.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka Timbangan di desktop dengan jendela lebar. Timbangan terlihat besar dan mengisi ruang kartu, tidak ada ruang kosong yang mencolok di atas atau di bawahnya.
- Kecilkan lalu besarkan jendela. Timbangan ikut menyesuaikan, tidak terpotong, tidak menimpa teks, dan halaman tidak muncul scroll.
- Pilih bulan yang berbeda sampai timbangan miring ke kiri, ke kanan, dan seimbang. Ketiga kondisi tampil benar dan animasinya tetap jalan.
- Mode gelap dan mode terang sama-sama rapi.

## Perbaikan UI PC 16: Batas Bulan Timbangan

Tujuan: Menyamakan navigasi bulan di halaman Timbangan versi desktop dengan Beranda, Riwayat, dan Statistik, yang sudah tidak bisa membuka bulan setelah bulan kalender saat ini. Timbangan satu-satunya halaman yang masih bisa membuka bulan depan.

Tempat dan tampilan:
- Halaman Timbangan versi desktop (desktop/src), kartu "Timbangan Bulan Ini", navigasi bulan dengan tombol panah kiri dan kanan di samping label bulan (contoh: "Nov 2026").
- Tombol panah kanan (bulan berikutnya) nonaktif ketika bulan yang ditampilkan sama dengan bulan kalender saat ini.
- Gaya dan cara menonaktifkan tombol sama persis dengan navigasi bulan di Beranda, Riwayat, atau Statistik. Tiru yang sudah ada di sana, jangan buat cara atau gaya baru.
- Tombol panah kiri (bulan sebelumnya) tidak berubah.

Aturan:
- Bulan saat ini diambil dari tanggal perangkat, dengan cara yang sama seperti di halaman lain.
- Bulan terjauh yang bisa ditampilkan adalah bulan saat ini. Bulan-bulan sebelumnya tetap bisa dibuka tanpa batas.
- Contoh: jika sekarang November 2026, pengguna bisa melihat November 2026 dan bulan sebelumnya, tetapi tidak bisa melihat Desember 2026.
- Bulan yang tampil pertama kali saat halaman dibuka tidak berubah dari perilaku sekarang.
- Perubahan hanya pada navigasi bulan di halaman Timbangan. Halaman lain tidak diubah.
- Tidak mengubah apa pun di mobile/.

Data:
- Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor.

Cek manual:
- Buka Timbangan di bulan berjalan. Label bulan menunjukkan bulan ini dan tombol panah kanan tampil nonaktif, sama seperti di Beranda. Klik tombol itu, tidak ada yang berubah.
- Klik panah kiri satu kali. Label pindah ke bulan sebelumnya dan tombol panah kanan aktif kembali.
- Dari bulan sebelumnya, klik panah kanan. Label kembali ke bulan berjalan dan tombol panah kanan nonaktif lagi.
- Bandingkan tombol nonaktif di Timbangan dengan di Beranda. Tampilannya sama di mode gelap dan mode terang.

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
