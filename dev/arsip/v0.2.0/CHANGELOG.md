# CHANGELOG

Arsip sampai v0.1.0 (2026-10-22): [dev/arsip/CHANGELOG-sampai-v0.1.0.md](arsip/CHANGELOG-sampai-v0.1.0.md).

Catat setiap fitur atau perbaikan yang selesai. Entri terbaru di atas.

## Format
```
## YYYY-MM-DD: nama singkat
- Ditambah: ...
- Diperbaiki: ...
- Diubah: ...
```

## 2026-11-06: Perbaikan UI PC 16: Batas Bulan Timbangan
- Diperbaiki: Menyamakan navigasi bulan di kartu "Timbangan Bulan Ini" pada halaman Timbangan desktop (`desktop/src/index.html`, `desktop/src/js/app.js`) dengan Beranda, Riwayat, dan Statistik agar tombol panah kanan (bulan berikutnya) nonaktif ketika bulan yang ditampilkan sama dengan bulan kalender saat ini, mencegah navigasi ke bulan setelah bulan berjalan, mempertahankan tombol panah kiri untuk melihat bulan-bulan sebelumnya tanpa batas, serta menyelaraskan gaya dan atribut nonaktif (`disabled`, `aria-disabled="true"`) secara konsisten.
- Diubah: Menambahkan rencana, spesifikasi, dan mencentang item "Perbaikan UI PC 16: Batas Bulan Timbangan" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).

## 2026-11-06: Perbaikan UI PC 15: Ukuran Timbangan
- Diperbaiki: Memperbesar gambar timbangan SVG di kartu "Timbangan Bulan Ini" pada halaman Timbangan desktop (`desktop/src/css/style.css`) agar mengisi ruang kosong antara header kartu dan bagian angka ringkasan pemasukan/pengeluaran secara responsif dan pas, memusatkan gambar timbangan secara vertikal dan horizontal pada area tersebut tanpa mengubah proporsi (rasio 320:180), menjaga agar gambar tidak menimpa teks maupun terpotong pada berbagai ukuran jendela desktop, serta memastikan halaman tetap pas tanpa scroll.
- Diubah: Menambahkan rencana, spesifikasi, dan mencentang item "Perbaikan UI PC 15: Ukuran Timbangan" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).

## 2026-11-06: Perbaikan UI PC 14: Field Harga Barang Timbangan
- Diperbaiki: Field "Harga Barang (Rp)" pada kartu "Timbang Sebelum Beli" di halaman Timbangan desktop (`desktop/src/css/style.css`), menyatukan awalan "Rp" dan angka input ke dalam satu kotak input utuh dengan perataan vertikal di tengah, menyamakan ukuran font awalan "Rp" (14px, semibold) dengan angka input, menyesuaikan tinggi kotak (40px di PC, 44px di mobile/layar sempit) serta lebarnya agar persis sama dengan dropdown "Kategori Pengeluaran" dan "Dompet Sumber Dana", dan merapikan jarak antar-elemen agar tidak saling menimpa pada semua ukuran jendela.
- Diubah: Menambahkan rencana, spesifikasi, dan mencentang item "Perbaikan UI PC 14: Field Harga Barang Timbangan" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).

## 2026-11-06: Kotlin 10O: Perbaikan widget gagal dimuat
- Diperbaiki: Menggunakan `ImageProvider(R.drawable.bg_widget)` pada modifier `.background(...)` kartu widget di `mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/widget/BalanceSummaryWidget.kt` baris 169 (sekarang baris 216) alih-alih resource ID mentah `R.drawable.bg_widget` yang sebelumnya salah dipetakan ke overload `@ColorRes` Int sehingga melempar exception `Resources$NotFoundException` saat RemoteViews di-inflate oleh launcher HP Infinix Note 30 dan memunculkan tulisan "Masalah memuat widget" (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/widget/BalanceSummaryWidget.kt`).
- Diperbaiki: Membungkus inisialisasi basis data Room `AppDatabase.getInstance` dan pembacaan/kalkulasi ringkasan saldo di `provideGlance` dalam blok proteksi `try ... catch (_: Throwable)` pada `mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/widget/BalanceSummaryWidget.kt` baris 58-140 (sekarang baris 60-205) dengan fallback ke keadaan data kosong yang aman (`+Rp 0`, `−Rp 0`, `Rp 0`) serta label default agar widget tidak melempar exception dan tidak crash saat cold boot atau ketika data kosong/gagal dibaca (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/widget/BalanceSummaryWidget.kt`).
- Diubah: Menambahkan rencana, spesifikasi, dan mencentang item "Kotlin 10O: Perbaikan widget gagal dimuat" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).

## 2026-11-06: Animasi Timbangan gravitasi dan seret (Desktop)
- Ditambah: Gerak pegas teredam fisik (damping ratio 0.28 dan stiffness 65) pada lengan timbangan bulan ini di desktop yang dimulai dari posisi kemiringan acak saat halaman Timbangan dibuka, lalu berayun naik turun dan meredam hingga berhenti di sudut akhir sesuai berat pemasukan vs pengeluaran (`desktop/src/js/app.js`).
- Ditambah: Interaksi seret pointer (mouse dan sentuh) pada gambar timbangan yang mengikuti posisi kursor/jari secara responsif dalam batas sudut maksimum Timbangan, mendukung seretan beruntun tanpa patah atau menumpuk animasi, serta berayun dengan redaman saat dilepas hingga kembali ke sudut akhir (`desktop/src/js/app.js`, `desktop/src/css/style.css`).
- Diubah: Memasang event listener pointer dan window blur hanya selama halaman Timbangan aktif dan melepasnya secara bersih saat berpindah ke halaman lain (`desktop/src/js/app.js`).
- Diubah: Menambahkan rencana, spesifikasi, dan mencentang item "Animasi Timbangan gravitasi dan seret (Desktop)" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).

## 2026-11-06: Kotlin 10N: Animasi Timbangan gravitasi dan goyang
- Ditambah: Animasi timbangan fisik berbasis gerak pegas teredam (`spring` dengan damping ratio 0.28 dan stiffness 65) pada gambar timbangan Kotlin, dimulai dari kemiringan acak saat dibuka lalu berayun naik turun beberapa kali sebelum diam di sudut keseimbangan sesuai berat (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/ui/screens/TimbanganScreen.kt`).
- Ditambah: Respon sensor akselerometer (`Sensor.TYPE_ACCELEROMETER`) yang mengayunkan lengan timbangan mengikuti kuat goyangan HP dengan filter pemisah gravitasi dan ambang batas getaran lemah, mendukung goyangan beruntun yang mulus dari posisi saat itu, serta melepas listener saat berpindah halaman atau aplikasi ke latar belakang (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/ui/screens/TimbanganScreen.kt`).
- Diubah: Menambahkan rencana, spesifikasi, dan mencentang item "Kotlin 10N: Animasi Timbangan gravitasi dan goyang" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).

## 2026-11-06: Kotlin 10M: Transisi menu
- Diubah: Mengganti animasi fade antar tab bar navigasi bawah dengan animasi geser horizontal (`slideIntoContainer` dan `slideOutOfContainer`) berdurasi 250 ms sesuai arah relatif tab (masuk dari kanan ke kiri saat menuju tab di kanan, dan sebaliknya), tanpa fade atau perubahan skala, serta memberikan latar `bg` dari token `MizanTheme.colors.bg` pada layar agar tidak tembus pandang saat transisi (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/MainActivity.kt`).
- Diubah: Menambahkan rencana, spesifikasi, dan mencentang item "Kotlin 10M: Transisi menu" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).

## 2026-11-05: Kotlin 10L: Perbaikan kursor input nominal
- Diperbaiki: Menggunakan `TextFieldValue` dengan seleksi kursor selalu berada di akhir teks (`TextRange(formatted.length)`) pada komponen `AmountInputField` sehingga kursor tidak lagi melompat ke tengah angka saat titik pemisah ribuan muncul ketika mengetik atau menghapus nominal transaksi, transfer, penyesuaian saldo, dan simulasi timbangan (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/ui/components/AmountInputField.kt`).
- Diperbaiki: Memperbaiki posisi kursor pada ketiga kolom input budget (edit inline budget kategori, edit inline budget sub kategori, dan form modal atur budget) di `KelolaBudgetScreen` menggunakan `TextFieldValue` agar kursor konsisten tetap di akhir angka (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/ui/screens/KelolaBudgetScreen.kt`).
- Diubah: Menambahkan rencana, spesifikasi, dan mencentang item "Kotlin 10L: Perbaikan kursor input nominal" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).

## 2026-11-05: Kotlin 10K: Perbaikan build widget 2
- Diperbaiki: Menambahkan import `androidx.glance.color.ColorProvider` di samping `androidx.glance.unit.ColorProvider` pada `BalanceSummaryWidget.kt` agar fungsi `ColorProvider(day, night)` dapat diselesaikan oleh kompilator Kotlin (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/widget/BalanceSummaryWidget.kt`).
- Diubah: Kenaikan versi aplikasi desktop ke 0.2.0 untuk persiapan rilis atas izin pemilik (`desktop/tauri/package.json`, `desktop/tauri/src-tauri/Cargo.toml`, `desktop/tauri/src-tauri/tauri.conf.json`).
- Diubah: Menambahkan rencana, spesifikasi, dan mencentang item "Kotlin 10K: Perbaikan build widget 2" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).

## 2026-11-05: Kotlin 10J: Perbaikan build widget
- Diperbaiki: Mengubah import `androidx.glance.color.ColorProvider` menjadi `androidx.glance.unit.ColorProvider` pada `BalanceSummaryWidget.kt` agar tipe parameter `ColorProvider` dapat diselesaikan oleh kompilator Kotlin (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/widget/BalanceSummaryWidget.kt`).
- Diperbaiki: Menghapus import `androidx.glance.layout.defaultWeight` yang tidak valid karena `defaultWeight()` merupakan fungsi ekstensi lingkup (`ColumnScope` dan `RowScope`) sehingga tidak memerlukan import fungsi tingkat paket (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/widget/BalanceSummaryWidget.kt`).
- Diubah: Kenaikan `versionCode` dari 1 menjadi 2 dan `versionName` dari "0.1.0" menjadi "0.2.0" untuk persiapan rilis atas izin pemilik (`mobile/kotlin/app/build.gradle.kts`).
- Diubah: Menambahkan rencana, spesifikasi, dan mencentang item "Kotlin 10J: Perbaikan build widget" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).

## 2026-11-05: Widget Kotlin: ringkasan saldo
- Ditambah: Dependensi Jetpack Glance `androidx.glance:glance-appwidget` dan `androidx.glance:glance-material3` versi 1.1.1 pada file dependensi Gradle (`mobile/kotlin/gradle/libs.versions.toml`, `mobile/kotlin/app/build.gradle.kts`).
- Ditambah: Implementasi widget layar utama Android satu ukuran berbasis Jetpack Glance `BalanceSummaryWidget` yang menampilkan nama bulan berjalan kecil di bagian atas serta tiga baris ringkasan saldo: "Pemasukan", "Pengeluaran", dan "Saldo" dengan warna dan tipografi sesuai `dev/DESIGN.md` (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/widget/BalanceSummaryWidget.kt`).
- Ditambah: Broadcast receiver `BalanceSummaryWidgetReceiver` serta konfigurasi XML widget provider `balance_summary_widget_info.xml` dan pendaftaran pada manifest aplikasi dengan label "Mizan Okane" (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/widget/BalanceSummaryWidgetReceiver.kt`, `mobile/kotlin/app/src/main/res/xml/balance_summary_widget_info.xml`, `mobile/kotlin/app/src/main/AndroidManifest.xml`).
- Ditambah: Drawable background kartu widget `bg_widget.xml` serta token warna mode terang dan gelap `widget_surface` dan `widget_border` dengan radius 8dp dan border 1px tanpa bayangan (`mobile/kotlin/app/src/main/res/drawable/bg_widget.xml`, `mobile/kotlin/app/src/main/res/values/colors.xml`, `mobile/kotlin/app/src/main/res/values-night/colors.xml`).
- Ditambah: Resource string `label_saldo` dan `widget_saldo_description` (`mobile/kotlin/app/src/main/res/values/strings.xml`).
- Diubah: `RoomMizanRepository` dan `MainActivity` memicu pembaruan widget secara otomatis setiap kali transaksi ditambah, diubah, atau dihapus, serta saat aplikasi ditutup/kembali ke layar utama HP (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/data/repository/RoomMizanRepository.kt`, `mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/MainActivity.kt`).
- Diubah: Menambahkan rencana, spesifikasi, dan mencentang item "Widget Kotlin: ringkasan saldo" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).

## 2026-11-05: Arsip CHANGELOG dan PLANSPEC
- Ditambah: Folder arsip `dev/arsip/` beserta file arsip `dev/arsip/CHANGELOG-sampai-v0.1.0.md` dan `dev/arsip/PLANSPEC-sampai-v0.1.0.md` untuk menyimpan riwayat lama hingga titik potong rilis v0.1.0 bertanggal 2026-10-22 (`dev/arsip/CHANGELOG-sampai-v0.1.0.md`, `dev/arsip/PLANSPEC-sampai-v0.1.0.md`).
- Ditambah: Aturan baru pada `dev/RULES.md` bahwa agen hanya membaca `dev/CHANGELOG.md` dan `dev/PLANSPEC.md` yang aktif, serta tidak membaca folder `dev/arsip/` kecuali diminta pemilik atau terkait fitur lama (`dev/RULES.md`).
- Ditambah: Baris penunjuk ke file arsip tepat di bawah judul pada `dev/CHANGELOG.md` dan `dev/PLANSPEC.md` aktif (`dev/CHANGELOG.md`, `dev/PLANSPEC.md`).
- Diubah: Memindahkan 126 entri lama bertanggal 2026-10-22 atau lebih lama ke `dev/arsip/CHANGELOG-sampai-v0.1.0.md` sehingga ukuran file aktif mengecil drastis (`dev/CHANGELOG.md`).
- Diubah: Memindahkan 121 bagian spesifikasi tercentang bertanggal 2026-10-22 atau lebih lama ke `dev/arsip/PLANSPEC-sampai-v0.1.0.md` dan mempertahankan bagian belum tercentang serta entri setelah 2026-10-22 di file aktif (`dev/PLANSPEC.md`).
- Diubah: Menambahkan rencana, spesifikasi, dan mencentang item "Arsip CHANGELOG dan PLANSPEC" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).

## 2026-11-05: Pintasan mode gelap (Kotlin)
- Ditambah: Tombol pintasan ganti mode gelap dan terang di header aplikasi Android dengan target sentuh minimal 44 px dan deskripsi aksesibilitas sesuai mode yang akan dituju (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/ui/components/AppHeader.kt`, `mobile/kotlin/app/src/main/res/values/strings.xml`).
- Ditambah: Vector drawable ikon bulan garis `ic_moon.xml` yang konsisten dengan ikon matahari `ic_sun.xml` (`mobile/kotlin/app/src/main/res/drawable/ic_moon.xml`).
- Ditambah: Alur pergantian tema langsung sekali ketuk di header yang tersinkronisasi dua arah seketika dengan Pengaturan dan tersimpan permanen lewat Room `SettingsEntity` (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/MainActivity.kt`, `mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/ui/components/AppHeader.kt`).
- Diubah: Menambahkan rencana, spesifikasi, dan mencentang item "Pintasan mode gelap (Kotlin)" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).

## 2026-11-05: Pintasan mode gelap (Desktop)
- Ditambah: Tombol pintasan ganti mode gelap dan terang di header pojok kanan atas, di sebelah kiri ikon cloud status sinkron, dengan target sentuh minimal 44 px dan label aksesibilitas sesuai mode yang akan dituju (`desktop/src/index.html`, `desktop/src/css/style.css`).
- Ditambah: Ikon dinamis matahari saat mode gelap aktif dan ikon bulan saat mode terang aktif menggunakan ikon garis konsisten (`desktop/src/index.html`, `desktop/src/css/style.css`).
- Ditambah: Sinkronisasi instan antara tombol header dan pilihan tema di Pengaturan menggunakan `persistAndApplyTheme` tanpa perlu muat ulang halaman (`desktop/src/js/app.js`).
- Diubah: Menambahkan rencana, spesifikasi, dan mencentang item "Pintasan mode gelap (Desktop)" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).

## 2026-11-05: Animasi Timbangan (Kotlin)
- Ditambah: Animasi halus pergerakan lengan timbangan Bulan Ini dari posisi seimbang (datar, dy = 0) ke posisi kemiringan target saat layar Timbangan dibuka menggunakan API animasi bawaan Jetpack Compose `Animatable`, `tween` durasi 450 ms, dan perlambatan kubik `EaseOutCubic` (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/ui/screens/TimbanganScreen.kt`).
- Ditambah: Transisi kemiringan dinamis dan mulus dari posisi lama ke posisi baru saat berganti bulan lewat navigasi bulan, saat data transaksi berubah, atau saat pilihan mata uang berganti (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/ui/screens/TimbanganScreen.kt`).
- Diubah: Komponen gambar timbangan `TimbanganScaleCanvas` menggerakkan lengan dan kedua piringan timbangan sesuai nilai animasi kemiringan tanpa mengubah logika perhitungan `calculateScaleTiltDy` (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/ui/screens/TimbanganScreen.kt`).
- Diubah: Menambahkan rencana, spesifikasi, dan mencentang item "Animasi Timbangan (Kotlin)" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).

## 2026-11-05: Animasi Timbangan (Desktop)
- Ditambah: Animasi halus pergerakan lengan timbangan Bulan Ini menggunakan requestAnimationFrame dengan perlambatan kubik (ease-out cubic) dari posisi seimbang (datar) saat halaman Timbangan dibuka (`desktop/src/js/app.js`).
- Ditambah: Transisi kemiringan dinamis dari posisi lama ke posisi baru saat berganti bulan pada pemilih bulan, mengubah pilihan mata uang, atau saat data transaksi diperbarui (`desktop/src/js/app.js`).
- Diubah: `renderTimbanganScale` memperbarui nilai nominal piringan timbangan dan teks ringkasan (verdict) langsung pada DOM SVG yang ada tanpa membuang dan membuat ulang elemen SVG setiap render (`desktop/src/js/app.js`).
- Diubah: Deteksi pembukaan tab Timbangan pada `switchTab` untuk memicu animasi awal dan membatalkan animasi jika berpindah ke halaman lain (`desktop/src/js/app.js`).
- Diubah: Menambahkan rencana, spesifikasi, dan mencentang item "Animasi Timbangan (Desktop)" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).

## 2026-11-05: Kotlin 10I: Perbaikan label navigasi Timbangan (belum dibuild)
- Diperbaiki: Menyesuaikan tinggi bar navigasi bawah dari 60.dp menjadi 72.dp dan merapikan perataan vertikal pada komponen navigasi agar label "Timbangan" tampil utuh tanpa terpotong batas layout (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/ui/components/BottomNavigationBar.kt`).
- Diperbaiki: Menyelaraskan garis dasar (baseline) teks label seluruh 5 tab navigasi bawah (Beranda, Riwayat, Timbangan, Statistik, Pengaturan) menggunakan perataan bawah `Arrangement.Bottom` dan padding bawah 8.dp yang seragam (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/ui/components/BottomNavigationBar.kt`).
- Diubah: Menambahkan rencana, spesifikasi, dan mencentang item "Kotlin 10I: Perbaikan label navigasi Timbangan" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).

## 2026-11-01: Kotlin 11: Form transaksi ikut filter dompet
- Ditambah: Parameter opsional `initialWalletId` pada `TransactionFormScreen` untuk mengisi dompet awal form tambah transaksi saat dibuka dengan filter dompet aktif (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/ui/screens/TransactionFormScreen.kt`).
- Diubah: State `formInitialWalletId` di `MainActivity` menyimpan dompet aktif dari filter Riwayat saat tombol tambah diklik dan meneruskannya ke form tambah transaksi (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/MainActivity.kt`).
- Diubah: Menambahkan rencana, spesifikasi, dan mencentang item "Kotlin 11: Form transaksi ikut filter dompet" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).

## 2026-11-01: Form transaksi ikut filter dompet
- Ditambah: Helper `getInitialWalletForAdd` untuk mendeteksi filter dompet aktif saat form tambah transaksi dibuka dari halaman Riwayat (`desktop/src/js/app.js`).
- Diubah: `resetForm` menerima parameter opsional `initialWalletId` untuk mengisi dompet awal dan menyesuaikan mata uang form secara otomatis saat filter dompet di Riwayat sedang aktif (`desktop/src/js/app.js`).
- Diubah: Tombol tambah (plus) `btnFabAdd` dan tombol tambah transaksi pada state kosong Riwayat `btn-empty-riwayat-add` mengisi dompet awal form tambah transaksi sesuai filter dompet Riwayat yang aktif (`desktop/src/js/app.js`).
- Diubah: Menambahkan rencana, spesifikasi, dan mencentang item "Form transaksi ikut filter dompet" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).

## 2026-10-25: Kotlin 9E: Audit Lintas File (belum dibuild)
- Diperbaiki:
  - #23: Menghapus seluruh penggunaan tipe `Double` dan pemanggilan `.toDouble()` pada `TimbanganCalculations.kt`. Perhitungan kemiringan lengan timbangan `calculateScaleTiltDy` diubah ke aritmatika integer `Long` murni dengan pembulatan terdekat (`diff * maxTilt +/- total / 2L`) tanpa floating point. Perhitungan `remainingPercent` pada simulasi belanja "Timbang Sebelum Beli" diubah ke integer `Long` murni dengan pembulatan presisi. Perbandingan ambang surplus/saldo pas-pasan 20% (`<= 0.20`) diubah dari pembagian `Double` ke perkalian silang integer `Long` murni (`newMonthDiff * 100L <= monthIncome * 20L` dan `newWalletBal * 100L <= walletBal * 20L`) sesuai aturan representasi uang di `RULES.md` dan `dev/SCHEMA.md` (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/domain/TimbanganCalculations.kt`).
- Diverifikasi:
  - #23: Grep `Double|Float|BigDecimal` pada seluruh berkas di folder `domain` dan `ui/components` terverifikasi bersih; tidak ada lagi `Double` atau `BigDecimal`. Seluruh kemunculan `Float` yang tersisa pada folder tersebut terbukti khusus untuk Canvas (sudut awal `startAngle`, sudut sapuan `sweepAngle`, dan kalkulasi `fullSweep` pada diagram donut di `StatistikDonutCalculations.kt`).
  - #47: Berkas resource `strings.xml` telah diperiksa menyeluruh terhadap empat aturan: apostrof (\') tanpa escape (0 temuan salah; satu-satunya apostrof pada teks bahasa Inggris di baris 282 sudah ber-escape `\'`), tanda % tanpa penulisan %% (0 temuan salah; seluruh tanda persen merupakan penentu format argumen resmi seperti `%1$s`, `%2$s`, dan `%1$d`), tanda & tanpa entitas XML (0 temuan salah; seluruh karakter ampersand di tag string menggunakan entitas `&#8230;` dan selebihnya berada di komentar XML), serta nama string dobel (0 nama string dobel dari total 298 string). Teks dan nama string dipertahankan utuh tanpa perubahan yang tidak perlu.
  - Pola inisialisasi Kotlin 10C: Seluruh `companion object`, `sealed class`, dan deklarasi `object` di `mobile/kotlin` telah dipindai dan diverifikasi; tidak ditemukan inisialisasi daftar atau properti di dalam `companion object` / `sealed class` yang mereferensikan instance turunan yang belum siap. Satu-satunya sealed class `Screen` telah menggunakan delegasi `get() = bottomNavItems` dengan lazy initialization dari perbaikan Kotlin 10C, dan companion object lainnya hanya berupa konstanta preferensi atau fungsi helper pabrik DTO stateless.
- Catatan temuan yang ragu dan tidak diubah:
  - #23: Parameter `onDrag: (dragAmountY: Float) -> Unit` pada baris 25 `ReorderDragHandle.kt` menggunakan tipe `Float`. Pemakaian ini bukan merupakan logika uang, melainkan callback gesture sentuh Android Jetpack Compose (`Offset.y` delta piksel) untuk interaksi seret-geser pada daftar dompet dan kategori, sehingga tetap dipertahankan sesuai aturan Compose.
- Diubah: Menambahkan spesifikasi dan mencentang item "Kotlin 9E: Audit Lintas File" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).
- Catatan kompilasi: Seluruh berkas Kotlin yang diubah telah diverifikasi kebenaran sintaks hingga baris terakhir. Proyek ini belum dibuild melalui gradle sesuai aturan RULES.md ("belum dibuild"), dan pengujian ("ditunda, dijalankan pemilik").

## 2026-10-25: Kotlin 9A: Audit Beranda, Riwayat, Form Transaksi, Komponen Umum (belum dibuild)
- Diperbaiki:
  - #14: Menghapus duplikasi fungsi privat `AmountInputField` pada `TransactionFormScreen.kt` dan menggantikannya dengan impor komponen bersama `com.mizoar.mizanokane.ui.components.AmountInputField` yang dipakai di seluruh aplikasi; membersihkan import yang tidak terpakai (`KeyboardOptions`, `KeyboardType`, `FontFamily`, `TextStyle`) (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/ui/screens/TransactionFormScreen.kt`).
  - #5: Mengubah teks hardcoded pada kondisi dompet kosong di `BerandaScreen.kt` agar menggunakan resource string `R.string.empty_dompet` (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/ui/screens/BerandaScreen.kt`).
  - #5: Mengubah `contentDescription` tombol navigasi bulan sebelumnya dan bulan berikutnya pada `MonthSelector.kt` agar menggunakan resource string `R.string.nav_prev_month` dan `R.string.nav_next_month` (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/ui/components/MonthSelector.kt`).
- Diverifikasi:
  - #5: Seluruh file komponen dan layar yang belum dibaca penuh sejak Kotlin 3 (`TransactionItemRow.kt`, `WalletBalanceItem.kt`, `MonthSelector.kt`, `BudgetCard.kt`, `CustomDropdown.kt`, `BerandaScreen.kt`, `RiwayatScreen.kt`, `MainActivity.kt`, dan `TransactionFormScreen.kt`) telah dibaca ulang secara menyeluruh; tidak ditemukan kode dobel, blok terpotong, resource `R.string` atau `R.drawable` yang hilang, maupun ketidakcocokan tipe data.
  - #6: Tampilan kartu/deretan saldo dompet pada `BerandaScreen.kt` terverifikasi disembunyikan jika jumlah dompet aktif kurang dari 2 (`if (activeWallets.size >= 2)`), identik dengan perilaku versi web (`if (activeWallets.length < 2) walletsSection.classList.add('hidden')`).
  - #8: File yang diedit sebagian pada Kotlin 4B (`TransactionFormScreen.kt`, `BerandaScreen.kt`, `RiwayatScreen.kt`, `MainActivity.kt`, `CustomDropdown.kt`) terverifikasi selesai sepenuhnya dan tidak menyisakan kode setengah jadi; dukungan ikon kustom dan subkategori pada formulir serta dropdown berjalan utuh.
  - #17: Ikon neraca (scale balance) di header terkonfirmasi ada di versi web (`desktop/src/index.html` pada elemen `.app-brand-icon`) dan versi Kotlin sudah memiliki dan menampilkannya melalui `AppHeader.kt` (`R.drawable.ic_neraca`) dengan path data SVG yang identik.
  - #34: Navigasi rute `SubmenuTampilan`, `SubmenuBahaya`, `KelolaDompet`, `KelolaKategori`, dan `KelolaBudget` di `MainActivity.kt` terhubung ke layar yang tepat; seluruhnya terdaftar pada kondisi `isFullScreen` sehingga header dan navigation bar disembunyikan, dan tombol back (baik in-screen maupun sistem Android via `BackHandler`) memanggil `navController.popBackStack()` untuk kembali ke menu Pengaturan tanpa keluar aplikasi.
  - #35: Seluruh pemanggilan format waktu tampilan pada `TransactionItemRow.kt` (dipanggil di `BerandaScreen.kt` dan `RiwayatScreen.kt`) serta pada `TransactionFormScreen.kt` terverifikasi menggunakan pengaturan format jam dinamis (`settings.timeFormat != "12"`) dari repository, tanpa ada nilai statis/konstan.
  - Aturan uang: Seluruh representasi dan kalkulasi nominal uang pada seluruh berkas yang diaudit menggunakan integer `Long` murni (satuan sen), tanpa tipe `Double`, `Float`, atau `BigDecimal`.
- Catatan temuan yang ragu dan tidak diubah: Tidak ada temuan yang meragukan; seluruh butir audit telah diselaraskan dengan versi web dan spesifikasi desain.
- Diubah: Menambahkan spesifikasi dan mencentang item "Kotlin 9A: Audit Beranda, Riwayat, Form Transaksi, Komponen Umum" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).
- Catatan kompilasi: Seluruh berkas Kotlin yang diubah telah diverifikasi kebenaran sintaks hingga baris terakhir. Proyek ini belum dibuild melalui gradle sesuai aturan RULES.md ("belum dibuild"), dan pengujian ("ditunda, dijalankan pemilik").

## 2026-10-25: Kotlin 9C: Audit Timbangan dan Statistik (belum dibuild)
- Diperbaiki:
  - #28: Menghapus seluruh penggunaan tipe `Double` dan konversi `.toDouble()` pada `StatistikDonutCalculations.kt`. Properti `percentageDouble` di `DonutCategoryItem` dihapus karena redundan dengan `percentageFormatted`, fungsi `formatDonutPercent` diubah menggunakan perhitungan integer `Long` murni (satuan seperseribu / tenths of percent) dengan pembulatan presisi tanpa konversi uang ke `Double`, serta konstanta keliling lingkaran dan celah busur dikonversi ke `Float` khusus penggambaran Canvas (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/domain/StatistikDonutCalculations.kt`).
- Diverifikasi:
  - #13: Seluruh 31 string unik (`R.string.*`) yang dipakai di `TimbanganScreen.kt` (32 kemunculan) telah diverifikasi dan seluruhnya tersedia lengkap di `strings.xml`.
  - #15: Seluruh `R.string.*` dan `R.drawable.*` yang digunakan pada kode 5A, 5B, 6A, dan 6B (`KelolaBudgetScreen.kt`, `TimbanganScreen.kt`, `AdjustBalanceDialog.kt`, `AmountInputField.kt`, `StatistikScreen.kt`, `StatistikDonutCalculations.kt`, `TimbanganCalculations.kt`, `StatistikCalculations.kt`) terbukti lengkap dan valid tanpa ada resource yang hilang.
  - #16: Transaksi penyesuaian saldo Equilibrium terkonfirmasi identik dengan web (`desktop/src/js/app.js`): kategori bernilai `"Equilibrium"`, surplus dicatat dengan nominal positif dan ditampilkan sebagai pemasukan (`+` dengan warna aksen hijau), defisit dicatat dengan nominal negatif dan ditampilkan sebagai pengeluaran (`−` dengan warna merah pengeluaran), chip subkategori memiliki ID sama persis dengan web (`Bunga`, `Cashback`, `Koreksi` untuk surplus; `Biaya admin`, `Lupa catat`, `Koreksi` untuk defisit), penyimpanan dilakukan melalui `repository.saveTransaction`, dan `TimbanganScreen` memanggil `calculateWalletBalances` untuk mendapatkan saldo terkini.
  - #18: Seluruh lima elemen tampilan diverifikasi memang ada di versi web: kartu "Timbang Sebelum Beli" (`index.html` baris 346 & `app.js` baris 4317), tombol "Tambah Transaksi" pada grafik dan donut kosong (`app.js` baris 3083, 3129, 4003), bar detail periode terpilih (`app.js` baris 3102 & 4048), batas periode navigasi yang tidak boleh melewati periode berjalan (`app.js` baris 3820-3849), serta jendela geser grafik bulanan 3 sampai 6 bulan (`app.js` baris 3165-3170).
  - #19: Aturan kalkulasi statistik terbukti sama persis dengan web (`desktop/src/js/app.js` baris 3040-3550): transaksi transfer dikeluarkan (`tx.type != "transfer"`), transaksi penyesuaian (Equilibrium) ikut dihitung via `isTxMatchingStatsType` (surplus masuk pemasukan, defisit masuk pengeluaran), dan nominal efektif dihitung dengan `Math.abs` via `getTxEffectiveAmount`.
  - #20: Berkas `StatistikScreen.kt`, `StatistikCalculations.kt`, `MainActivity.kt`, dan `strings.xml` telah dibaca ulang secara penuh; struktur sintaks dan penataan composable rapi dan valid.
  - #21: Teks di Canvas pada `StatistikChartCanvas` terkonfirmasi menggunakan `drawIntoCanvas` dan `canvas.nativeCanvas.drawText` dengan impor resmi Compose yang benar (`androidx.compose.ui.graphics.drawscope.drawIntoCanvas`, `androidx.compose.ui.graphics.nativeCanvas`, `androidx.compose.ui.graphics.toArgb`). Pada `TimbanganScaleCanvas`, Canvas menggambar geometri timbangan dan teks nilai ditampilkan melalui composable layout luar.
  - #22: Fungsi `Uang.formatCompactCurrency` telah diuji perilakunya untuk angka 0, angka kecil (desimal koma USD 49,90), satuan ribuan (`1,5 rb`), jutaan (`1,5 jt`), miliaran (`1,5 M`), dan triliunan (`2,5 T`); seluruhnya konsisten dengan perilaku web tanpa mengubah fungsi lama.
  - #25: Seluruh 1685 baris `StatistikScreen.kt` telah diaudit; tidak ditemukan kode dobel maupun blok composable yang terpotong.
  - #27: Fungsi `getTxStatsCategory` dan `getTxSubcategory` terbukti identik dengan web (`desktop/src/js/app.js` baris 583-593 dan 635-641). Total busur sudut donut terakumulasi tepat 360 derajat (100% lingkaran).
  - #29: Seluruh 16 `R.string` baru untuk diagram donut dari Kotlin 6B terbukti ada di `strings.xml`.
  - #30: Pengambilan `getCategoriesFlow()` dan `getSettingsFlow()` pada `StatistikScreen.kt` menggunakan `collectAsState(initial = null)` dengan penanganan nullable yang konsisten di seluruh layar.
  - Warna donut: Palet warna `DONUT_EXPENSE_DARK`, `DONUT_EXPENSE_LIGHT`, `DONUT_INCOME_DARK`, dan `DONUT_INCOME_LIGHT` terkonfirmasi memakai nilai hex yang cocok 100% dengan `dev/DESIGN.md` dan `desktop/src/js/app.js`.
- Catatan temuan yang ragu dan tidak diubah:
  - #29: Teks di tengah diagram donut diimplementasikan menggunakan composable `Column` yang dipusatkan di atas `DonutChartCanvas` di dalam `Box(contentAlignment = Alignment.Center)` (bukan langsung via `drawText` di Canvas). Pendekatan ini mempertahankan dukungan penuh MaterialTheme, tipografi dinamis, pemotongan teks ellipsis otomatis, dan aksesibilitas, serta secara visual identik dengan `<g class="donut-center-group">` pada SVG web sehingga tetap dipertahankan.
- Diubah: Menambahkan spesifikasi dan mencentang item "Kotlin 9C: Audit Timbangan dan Statistik" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).
- Catatan kompilasi: Berkas Kotlin yang diubah telah diverifikasi kebenaran sintaks hingga baris terakhir. Proyek ini belum dibuild melalui gradle sesuai aturan RULES.md ("belum dibuild"), dan pengujian ("ditunda, dijalankan pemilik").

## 2026-10-25: Kotlin 9B: Audit dompet, kategori, budget (belum dibuild)
- Ditambah: Fungsi alias `calculateBudgetSectionSummary` di `BerandaCalculations.kt` yang mendelegasikan ke `getBudgetSectionSummary` dengan signature yang sama persis agar kedua nama fungsi tersedia dan valid bagi seluruh pemanggil (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/domain/BerandaCalculations.kt`).
- Diverifikasi: Seluruh butir audit spek Kotlin 9B telah diperiksa terhadap kode web `desktop/src/` dan `dev/SCHEMA.md`:
  - #3: Seluruh anotasi `@Insert` pada `WalletDao`, `CategoryDao`, dan `BudgetDao` terkonfirmasi menggunakan `OnConflictStrategy.REPLACE`. Pada `RoomMizanRepository`, penghapusan kategori dan subkategori melalui `saveCategories` menjalankan transaksi `deleteAllCategories` dan `deleteAllSubcategories` sebelum memasukkan entitas yang tersisa sehingga baris data terhapus benar-benar hilang dari database. Hal serupa pada `saveBudgets` yang menjalankan `deleteAllBudgets` dan `deleteAllSubcategoryBudgets`. Pada dompet, `markWalletDeleted` menandai `deleted = 1` (soft delete) selaras dengan web dan `dev/SCHEMA.md`, serta `saveWallets` menggunakan `insertAll` yang mempertahankan data yang telah di-soft-delete.
  - #4: Penjagaan "minimal 1 dompet aktif" pada `KelolaDompetScreen.kt` terbukti identik dengan web (`app.js` baris 7008), di mana jika `activeWallets.size <= 1`, aplikasi menolak penghapusan dengan toast peringatan string `warn_min_one_wallet` ("Dompet tidak bisa dihapus karena minimal harus ada satu dompet.").
  - #7: Berkas `KelolaKategoriScreen.kt` dibaca utuh seluruh 1452 baris; tidak ditemukan kode dobel maupun terpotong, seluruh blok composable tertutup lengkap.
  - #9: Penghapusan kategori pada web (`app.js` baris 6555 `performDeleteCategory`) dan Kotlin (`KelolaKategoriScreen.kt` baris 131-178) terkonfirmasi sama: menghapus kategori dari struktur kategori dan menghapus budget kategori/subkategori terkait tanpa menghapus transaksi riil. Pada penghapusan subkategori, transaksi terkait dihapus nilai subkategorinya (`tx.copy(subcategory = null)`), bukan menghapus transaksinya. Sebanyak 41 string ID ikon kategori kustom di `CategoryIcons.kt` terverifikasi cocok penuh dengan `CATEGORY_ICON_THEMES` di web.
  - #10: Berkas `KelolaBudgetScreen.kt` dibaca utuh seluruh 1097 baris; struktur kode lengkap tanpa bagian terpotong/dobel dan perilakunya (validasi total subkategori tidak melebihi kategori induk, badge orphan, edit inline, konfirmasi hapus) selaras dengan web.
  - #11: Fungsi `calculateBudgetSectionSummary` / `getBudgetSectionSummary` terverifikasi dipanggil dengan parameter lengkap `(budgets, categories, transactions, targetMonthKey, wallets)` pada `BerandaScreen.kt`.
  - #12: Penggeseran urutan budget terbukti mengubah properti `order` pada daftar kategori pengeluaran (`categories.expense`) lalu menyimpannya via `saveCategories`, identik dengan web (`app.js` baris 7572-7619) dan sesuai `dev/SCHEMA.md` (budget tidak memiliki field order). Di web, subkategori pada Kelola Kategori dapat digeser, sedangkan subkategori pada Kelola Budget tidak dapat digeser (hanya ubah/hapus); perilaku di Kotlin pada `KelolaKategoriScreen.kt` dan `KelolaBudgetScreen.kt` sudah menyamai web.
- Diubah: Menambahkan spesifikasi dan mencentang item "Kotlin 9B: Audit dompet, kategori, budget" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).
- Catatan kompilasi: Berkas Kotlin yang diubah telah diverifikasi kebenaran sintaks hingga baris terakhir. Proyek ini belum dibuild melalui gradle sesuai aturan RULES.md ("belum dibuild"), dan pengujian ("ditunda, dijalankan pemilik").

## 2026-10-25: Kotlin 9D: Audit data dan reset (belum dibuild)
- Diperbaiki: Operasi SAF (Storage Access Framework) pada `SubmenuDataScreen.kt` untuk ekspor berkas JSON (`CreateDocument("application/json")`), ekspor CSV (`CreateDocument("text/csv")`), dan impor JSON (`OpenDocument()` dengan mime `application/json` dan `text/*`) agar pembacaan dan penulisan stream dilakukan di `Dispatchers.IO` melalui `withContext(Dispatchers.IO)` dan stream ditutup otomatis dengan ekstensi `.use { ... }` (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/ui/screens/SubmenuDataScreen.kt`).
- Diperbaiki: Pembersihan data demo pada `clearSampleData()` dan `resetToDefaults()` di `InMemoryMizanRepository.kt` agar mereset `demoCategoriesData` dan `demoBudgetsData` ke nilai sample bawaan serta menghapus semua kunci `demo_*` (`demo_wallets`, `demo_categories`, `demo_budgets`, `demo_transactions`, `demo_mode`) dari `metaMap`, selaras dengan perilaku `RoomMizanRepository.kt` dan `desktop/src/js/db.js` (`mobile/kotlin/app/src/main/java/com/mizoar/mizanokane/data/repository/InMemoryMizanRepository.kt`).
- Diperbaiki: Pengujian `testResetToDefaults()` pada `JsonBackupManagerTest.kt` ditambahkan verifikasi bahwa mode contoh aktif dibersihkan dan semua kunci demo hilang setelah `resetToDefaults()` (`mobile/kotlin/app/src/test/java/com/mizoar/mizanokane/data/JsonBackupManagerTest.kt`).
- Diverifikasi: Seluruh 17 butir audit spek Kotlin 9D telah diaudit terhadap acuan web `desktop/src/` dan `dev/SCHEMA.md`:
  - #31: Dialog "ketik HAPUS" dua langkah di `SubmenuBahayaScreen.kt` dikonfirmasi identik dengan web (`app.js` baris 9623 dan `index.html` baris 1181) sehingga tetap dipertahankan.
  - #32 & #40: Cakupan `resetToDefaults()` di `RoomMizanRepository.kt` telah dibandingkan dengan `resetDatabase()` di `db.js`: seluruh transaksi dihapus, kategori, budget, dompet, dan pengaturan dikembalikan ke default awal, `localRev = 0`, serta seluruh kunci `demo_*` dan flag `demo_mode` dibersihkan dalam satu transaksi Room.
  - #33: Dikonfirmasi web tidak memiliki aksi "Reset setelan pabrik" yang terpisah dari "Hapus semua data"; keduanya adalah aksi yang sama di Zona Bahaya (`btn-delete-all-data`).
  - #36: `ic_sun.xml` dan `ic_danger.xml` diverifikasi sebagai XML vektor yang valid, serta seluruh `R.string.*` dari 7A dan 7B terbukti ada dan valid di `strings.xml`.
  - #37: Setelah `resetToDefaults()`, tema kembali ke "system" dan `MainActivity.kt` langsung merekomposisi tema aplikasi secara reaktif.
  - #38 & #39: Saat mode demo aktif, seluruh mutasi data masuk ke entitas demo di `MetaEntity` dan tidak mengubah data riil. Ukuran serialisasi JSON string di SQLite ringan (~4-5 KB) dan DTO sesuai skema web.
  - #41: `JsonBackupManager.kt` dibaca utuh 363 baris; logika merge dan LWW sesuai web, dan `JsonBackupManagerTest.kt` cocok dengan fixture `mizan-okane-backup-sample.json`.
  - #42: Impor ditolak saat mode demo dengan pesan identik web di `JsonBackupManager` dan `SubmenuDataScreen`.
  - #43: `CsvExporter.kt` cocok penuh dengan web: filter transaksi non-deleted dan non-dummy, dompet transfer format `A -> B`, pengurutan tanggal/jam/createdAt descending, escaping karakter kutip/koma/newline, format nominal angka USD/Yen/IDR via `Uang.kePlainString`, serta header UTF-8 BOM.
  - #44: `SampleData.kt` identik dengan `db.js`: 4 dompet, kategori lengkap, 5 budget, dan 25 transaksi berflag `dummy = true`.
  - #45: Konfirmasi bahwa "Muat ulang data contoh", "Keluar dari data contoh", dan banner penanda mode contoh memang ada di web.
  - #48: Semua 34 fungsi `MizanRepository` terimplementasi di `RoomMizanRepository` dan `InMemoryMizanRepository` dengan signature identik; `withTransaction` dan `@OptIn(ExperimentalCoroutinesApi::class)` digunakan dengan benar.
  - #49: SAF `SubmenuDataScreen` CreateDocument/OpenDocument dan stream `Dispatchers.IO` ditutup dengan aman.
  - #50: `RoomMizanRepository.kt` (593 baris) utuh tanpa kode terpotong atau dobel; fungsi `getReal*` independen dari demo.
  - #51: Baris 1-50 `PengaturanScreen.kt` dibaca ulang dan rapi.
  - #52: URL tautan Tentang Pengembang sama dengan web (`https://mizoarkatamarenaldy.github.io`).
- Diubah: Menambahkan spesifikasi dan mencentang item "Kotlin 9D: Audit data dan reset" di `dev/PLAN.md` dan `dev/PLANSPEC.md` (`dev/PLAN.md`, `dev/PLANSPEC.md`).
- Catatan kompilasi: Berkas Kotlin yang diubah telah diverifikasi kebenaran sintaks hingga baris terakhir. Proyek ini belum dibuild melalui gradle sesuai aturan RULES.md ("belum dibuild"), dan pengujian ("ditunda, dijalankan pemilik").
