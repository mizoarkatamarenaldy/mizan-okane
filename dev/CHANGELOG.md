# CHANGELOG

Catat setiap fitur atau perbaikan yang selesai. Entri terbaru di atas.

## Format
```
## YYYY-MM-DD: nama singkat
- Ditambah: ...
- Diperbaiki: ...
- Diubah: ...
```

## 2026-10-18: Perbaikan UI PC 2A: CSS Timbangan
- Diperbaiki: Tata letak 3 kolom sejajar pada halaman Timbangan di layar PC (@media min-width: 1024px) dengan lebar 1 : 1,2 : 1 (kiri: Timbang Sebelum Beli, tengah: Timbangan Bulan Ini, kanan: Sesuaikan Saldo) mulai dari tinggi baris yang sama (`grid-row: 1`) dan header satu baris penuh di atasnya (`desktop/src/css/style.css`).
- Diperbaiki: Scroll halaman Timbangan di PC dipindahkan ke tingkat kolom (`overflow-y: auto`), kartu mengikuti tinggi isi tanpa scrollbar tersendiri (`min-height: auto; height: auto; overflow: visible`), serta gambar timbangan SVG tampil utuh dan ukurannya fleksibel menyesuaikan lebar kolom tengah (`desktop/src/css/style.css`).
- Ditambah: Padding-bawah 88 px pada kolom Timbangan, kolom Beranda, dan halaman Statistik di layar PC agar tombol melayang tambah (+) tidak menutup konten paling bawah saat digulir (`desktop/src/css/style.css`).
- Diubah: Menaikkan versi cache service worker ke `mizan-v65` agar perubahan antarmuka termuat (`desktop/src/sw.js`).
- Diubah: Mencentang item "Perbaikan UI PC 2A: CSS Timbangan" di `dev/PLAN.md` dan menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-18: Perbaikan UI PC: Riwayat dan Timbangan
- Ditambah: Tampilan Riwayat di layar PC dengan baris transaksi satu lapis (tinggi 52px), kolom berurutan dengan lebar tetap (ikon 36px, kategori 180px, catatan 1fr terpotong elipsis, dompet 140px, waktu 80px, jumlah 150px rata kanan, tombol aksi 96px dengan ruang tersisa saat tanpa tombol edit), pemisah garis tipis token border tanpa kartu bersarang, serta blok ringkasan total tersusun rapi satu mata uang per baris (`desktop/src/css/style.css`).
- Ditambah: Tata letak dashboard 3 kolom pada halaman Timbangan di layar PC (@media min-width: 1024px) dengan lebar kolom berbanding 1 : 1,2 : 1 (kiri: Timbang Sebelum Beli, tengah: Timbangan Bulan Ini & Equilibrium, kanan: Sesuaikan Saldo), scroll independen per kolom (overflow-y auto), tinggi pas sisa layar tanpa scroll panjang halaman, dan pembatasan lebar konten maksimal 1600px di tengah (`desktop/src/index.html`, `desktop/src/css/style.css`).
- Diperbaiki: Kotak pencarian Riwayat diberi padding kiri 44px agar ikon kaca pembesar tidak menimpa teks pencarian di layar PC (`desktop/src/css/style.css`).
- Diubah: Menaikkan versi cache service worker ke `mizan-v63` agar pembaruan antarmuka termuat (`desktop/src/sw.js`).
- Diubah: Mencentang item "Perbaikan UI PC: Riwayat dan Timbangan" di `dev/PLAN.md` dan menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-18: Revisi UI PC
- Ditambah: Tata letak dashboard layar lebar (@media min-width: 1024px) dengan lebar konten maksimal 1600px di tengah, tinggi konten pas satu layar tanpa scroll panjang, serta Beranda 3 kolom (kolom kiri: saldo dan dompet; kolom tengah: budget; kolom kanan: transaksi terakhir) dengan overflow scroll mandiri per kolom (`desktop/src/index.html`, `desktop/src/css/style.css`).
- Ditambah: Tampilan Riwayat di layar PC dengan pencarian dan filter bulan pada satu baris atas, serta baris transaksi melebar seperti tabel terstruktur (ikon dan kategori, catatan, dompet, waktu, jumlah rata kanan dengan tanda, tombol aksi) (`desktop/src/index.html`, `desktop/src/css/style.css`).
- Ditambah: Tampilan Statistik di layar PC dengan grafik bar dan diagram donut tersusun berdampingan dalam 2 kolom, tab skala waktu tetap di atas (`desktop/src/index.html`, `desktop/src/css/style.css`).
- Ditambah: Tampilan Pengaturan di layar PC dengan daftar nama bagian di kolom kiri yang langsung menampilkan isi bagian di kolom kanan saat diklik (`desktop/src/css/style.css`).
- Ditambah: Pembatasan lebar maksimal 800px di tengah layar untuk halaman Timbangan, Kebijakan Privasi, dan Syarat dan Ketentuan di layar PC (`desktop/src/css/style.css`).
- Ditambah: Formulir tambah/edit transaksi dan transfer sebagai dialog tengah lebar 480px dengan latar belakang gelap transparan dari token bg, penutupan dengan tombol Esc, urutan field terjaga, dan efek hover instan ke token surface-raised tanpa animasi (`desktop/src/css/style.css`).
- Diubah: Menaikkan versi cache service worker ke `mizan-v62` agar pembaruan tampilan termuat (`desktop/src/sw.js`).
- Diubah: Memperbarui baris tata letak pada `dev/DESIGN.md` untuk mencatat tata letak dashboard di PC (1024px ke atas) sesuai izin dari pemilik (`dev/DESIGN.md`).
- Diubah: Memperbarui spesifikasi lengkap dan mencentang item "Revisi UI PC" (`dev/PLANSPEC.md`, `dev/PLAN.md`).

## 2026-10-18: Pemisahan folder
- Ditambah: Pemisahan struktur repositori menjadi folder kerja `desktop/src/` (file web aplikasi: `index.html`, `privasi.html`, `syarat.html`, `sw.js`, `manifest.webmanifest`, `css/`, `js/`, `icons/`) dan `mobile/capacitor/` (proyek Capacitor: `android/`, `capacitor.config.json`, `package.json`, `package-lock.json`, `scripts/`, `assets/`) dengan riwayat git terjaga penuh.
- Ditambah: Workflow GitHub Actions `.github/workflows/pages.yml` untuk memublikasikan isi `desktop/src/` sebagai artefak GitHub Pages saat push ke branch `main` atau dijalankan manual (`workflow_dispatch`), tanpa langkah build.
- Ditambah: Dokumentasi skema data `dev/SCHEMA.md` yang mencatat struktur IndexedDB, format cadangan JSON, format payload Google Drive `appDataFolder`, aturan resolusi konflik data, dan nomor versi format.
- Diubah: Skrip `mobile/capacitor/scripts/copy-www.js` membaca sumber berkas web dari `../../desktop/src` untuk disalin ke `mobile/capacitor/www`.
- Diubah: Menyesuaikan aturan kerja dan larangan build pada `dev/RULES.md` dan `dev/PROMPT.md` untuk folder `mobile/capacitor/www/` dan `mobile/capacitor/android/` serta menambahkan aturan setelah pemisahan folder.
- Diubah: Mencentang item "Pemisahan folder" di `dev/PLAN.md` dan memperbarui spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-18: Spek item setelah Tahap 5
- Ditambah: Spesifikasi empat item setelah Tahap 5 (Pemisahan folder, Revisi UI PC, Konversi Tauri, dan Konversi Kotlin) di bagian akhir dokumen (`dev/PLANSPEC.md`).
- Diubah: Menambahkan catatan penundaan rilis hingga Konversi Kotlin selesai pada bagian "APK rilis" dan "APK Item A" (`dev/PLANSPEC.md`).

## 2026-10-18: Kategori Equilibrium
- Ditambah: Kategori induk bawaan sistem "Equilibrium" dengan sub kategori pilihan pada penyesuaian saldo (pemasukan = Bunga, Cashback, Koreksi; pengeluaran = Biaya admin, Lupa catat, Koreksi) (`index.html`, `js/app.js`).
- Ditambah: Pilihan kategori penyesuaian pada dialog saldo riil di Timbangan saat selisih bernilai negatif (Biaya admin, Lupa catat, Koreksi) dengan Koreksi sebagai pilihan default aktif (`index.html`, `js/app.js`).
- Ditambah: Perhitungan penyesuaian surplus sebagai pemasukan dan penyesuaian defisit sebagai pengeluaran di bawah kategori induk Equilibrium pada seluruh visualisasi halaman Statistik (grafik harian, bulanan, tahunan, dan diagram donut per kategori) (`js/app.js`).
- Ditambah: Dukungan field `subcategory` pada transaksi penyesuaian dengan kompatibilitas data lama (transaksi penyesuaian tanpa label otomatis dianggap Koreksi), keamanan sinkronisasi Google Drive, ekspor/impor cadangan JSON, serta pencantuman tipe "Penyesuaian" dan kategori "Equilibrium" pada ekspor CSV (`js/app.js`).
- Diubah: Diagram donut Statistik menampilkan Equilibrium sebagai satu irisan utuh dan rincian sub kategori hanya tampil saat irisan atau legenda Equilibrium dipilih (`js/app.js`).
- Diubah: Kategori Equilibrium dan sub kategorinya dilindungi sehingga tidak bisa diedit, dihapus, digeser, atau diberi ikon kustom di Pengaturan, serta tidak muncul pada formulir transaksi biasa maupun daftar budget (`js/app.js`).
- Diubah: Angka surplus/defisit kartu Timbangan bulan ini tetap murni dari transaksi biasa tanpa perubahan, baris Equilibrium tetap menampilkan total bersih penyesuaian secara terpisah (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v61` (`sw.js`).
- Diubah: Mencentang item "Kategori Equilibrium" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-18: Perbaikan: dialog Pilih Ikon muncul sendiri di Pengaturan
- Diperbaiki: Dialog pemilih ikon (`#icon-picker-dialog`) yang tampil permanen di halaman Pengaturan tanpa diminta diperbaiki akar masalahnya dengan menghapus deklarasi `display: flex;` tanpa syarat pada selector `.icon-picker-dialog` yang sebelumnya menimpa aturan semantik bawaan browser `dialog:not([open])`, serta menambahkan aturan eksplisit `.icon-picker-dialog:not([open])` dan `.confirm-dialog:not([open])` dengan `display: none` dan `.icon-picker-dialog[open]` dengan `display: flex; flex-direction: column;` agar dialog tersembunyi sempurna saat ditutup (`css/style.css`).
- Diperbaiki: Penanganan penutupan dialog pemilih ikon diperkuat dengan listener event `close`, pembersihan state runtime callback pemilih ikon, fallback penutupan klik di luar modal (backdrop), serta penutupan dialog saat tombol fisik back Android ditekan (`js/app.js`).
- Diubah: Mencentang item "Perbaikan: dialog Pilih Ikon muncul sendiri di Pengaturan" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-18: Ikon sub kategori
- Ditambah: Pemilih ikon sub kategori saat menambah dan mengedit sub kategori di Pengaturan menggunakan dialog pemilih ikon yang sama dengan kategori, lengkap dengan tombol untuk mereset/mengikuti ikon induk serta target sentuh minimal 44 px (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Tampilan ikon sub kategori di samping nama sub kategori pada seluruh antarmuka aplikasi: daftar sub kategori di Pengaturan, chip sub kategori formulir transaksi, riwayat transaksi (pada baris teks dan badge transaksi), rincian sub kategori di diagram statistik, serta daftar budget di Beranda dan Pengaturan (`css/style.css`, `js/app.js`).
- Ditambah: Dukungan field `icon` pada sub kategori di IndexedDB yang tersimpan, tersinkron Google Drive dengan aturan resolusi konflik yang terbaru menang, serta diekspor/diimpor pada cadangan JSON dengan fallback aman ke ikon induk atau ikon netral (`js/db.js`, `js/app.js`).
- Diubah: Sub kategori tanpa ikon pilihan otomatis menampilkan ikon kategori induknya (atau netral jika induk belum berikon), dan bila ikon induk diganti maka sub kategori tanpa ikon ikut berubah mengikuti induk (`js/app.js`).
- Diubah: Di daftar budget Beranda dan Pengaturan, ikon dibaca langsung secara dinamis dari sub kategori sehingga penggantian ikon sub kategori otomatis langsung memperbarui tampilan budget (`js/app.js`).
- Diubah: Sub kategori pada data contoh lengkap diberi ikon yang sesuai, dan reset setelan pabrik mengembalikan ke kondisi tanpa ikon pilihan (`js/db.js`).
- Diubah: Mencentang item "Ikon sub kategori" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-18: Ikon kategori kustom
- Ditambah: Pemilih ikon kategori (`#icon-picker-dialog`) saat menambah dan mengedit kategori dengan set ikon bawaan berupa SVG inline yang dikelompokkan ke dalam 9 tema (Makanan, Transportasi, Belanja, Rumah, Kesehatan, Pendidikan, Hiburan, Keuangan, Lainnya), lengkap dengan target sentuh minimal 44 px dan label pembaca layar (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Tampilan ikon kategori di samping nama kategori pada seluruh antarmuka aplikasi: daftar kategori di Pengaturan, chip kategori formulir transaksi, riwayat transaksi, legenda & rincian statistik, serta daftar budget di Beranda dan Pengaturan (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Field `icon` pada skema kategori di IndexedDB yang ikut tersimpan, tersinkron Google Drive dengan aturan resolusi konflik yang terbaru menang, serta diekspor/diimpor pada cadangan JSON dengan fallback aman ke ikon netral (`js/db.js`, `js/app.js`).
- Diubah: Daftar budget membaca ikon langsung dari kategori pengeluaran terkait sehingga penggantian ikon kategori otomatis memperbarui ikon pada budget tanpa perlu mengatur ulang budget (`js/app.js`).
- Diubah: Sub kategori tetap tidak memiliki ikon sendiri dan tidak menampilkan ikon (`js/db.js`, `js/app.js`).
- Diubah: Reset setelan pabrik mengembalikan kategori tanpa ikon pilihan, dan data contoh lengkap diberikan ikon kategori yang sesuai (`js/db.js`).
- Diubah: Mencentang item "Ikon kategori kustom" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-18: Budget per sub kategori
- Ditambah: Dukungan pengaturan budget per sub kategori di formulir setel budget dan daftar budget Pengaturan, lengkap dengan pemformatan nominal, edit inline, dan konfirmasi hapus khusus sub kategori (`index.html`, `js/app.js`).
- Ditambah: Penataan visual sub kategori ber-budget di Beranda dan Pengaturan yang tampil menjorok di bawah kategori induknya tanpa pegangan geser (`css/style.css`, `js/app.js`).
- Ditambah: Struktur data `subcategories` pada skema budget di IndexedDB dan data contoh dengan validasi sanitasi, penanganan konflik yang terbaru menang, sinkronisasi Google Drive, serta ekspor/impor cadangan JSON yang kompatibel dengan versi terdahulu (`js/db.js`, `js/app.js`).
- Diubah: Validasi penambahan, perubahan, dan penghapusan budget induk maupun sub kategori: sub kategori hanya bisa diberi budget bila induknya memiliki budget, dan total budget sub kategori tidak boleh melebihi budget induk (`js/app.js`).
- Diubah: Perhitungan pengeluaran bulanan Beranda untuk menghitung transaksi sub kategori pada bar sub kategori dan bar induk secara akurat (`js/app.js`).
- Diubah: Sinkronisasi otomatis penghapusan dan pengubahan nama sub kategori atau kategori dengan data budget terkait (`js/app.js`).
- Diubah: Mencentang item "Budget per sub kategori" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-18: Geser urutan budget
- Ditambah: Pegangan geser berikon garis tiga (≡, `.btn-drag-handle`) pada setiap baris daftar budget per kategori di Pengaturan untuk mengatur urutan budget secara manual (`js/app.js`).
- Ditambah: Mekanisme geser urutan budget menggunakan `enableListDragReorder` yang memperbarui urutan kategori pengeluaran pada IndexedDB, data demo, dan sinkronisasi Google Drive tanpa mengubah struktur data (`js/app.js`).
- Diubah: Posisi kategori pengeluaran tanpa budget tetap dipertahankan dan tidak terganggu saat urutan budget digeser (`js/app.js`).
- Diubah: Urutan baru budget langsung tercermin di semua tempat yang menampilkan budget (termasuk Beranda) tanpa pegangan geser di sana (`js/app.js`).
- Diubah: Mencentang item "Geser urutan budget" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-18: Statistik: tab Harian, Bulanan, Tahunan
- Ditambah: Tiga tab skala waktu ("Harian", "Bulanan", "Tahunan") di bagian atas halaman Statistik dengan role tablist, tab, tabpanel, aria-selected, dukungan navigasi keyboard (panah kiri/kanan, Home, End, Enter/Spasi), dan target sentuh minimal 44 px sesuai spesifikasi (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Tampilan skala Harian dengan batang per tanggal dari tanggal 1 sampai tanggal terakhir bulan terpilih, hari kosong bernilai nol, serta ringkasan dan diagram donut yang sinkron dengan total bulan terpilih (`js/app.js`, `css/style.css`).
- Ditambah: Tampilan skala Tahunan dengan navigasi per tahun, batang per tahun dari tahun data paling awal sampai tahun berjalan dengan penanda tahun terpilih, serta diagram donut dan ringkasan kategori untuk tahun terpilih (`js/app.js`, `css/style.css`).
- Ditambah: State bersama bulan dan tahun yang mempertahankan bulan terpilih saat berpindah ke tab Tahunan dan menggeser tahun, sehingga saat kembali ke Bulanan/Harian bulan tetap sama pada tahun pilihan terakhir (`js/app.js`).
- Diubah: Navigasi periode bersama otomatis menyesuaikan label dan batasan (tidak bisa melewati bulan berjalan pada Harian/Bulanan atau tahun berjalan pada Tahunan) (`js/app.js`).
- Diubah: Membuka kembali halaman Statistik dari halaman lain otomatis mengatur ulang tab ke Bulanan pada bulan berjalan (`js/app.js`).
- Diubah: Mencentang item "Statistik: tab Harian, Bulanan, Tahunan" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-18: Statistik: satu bulan terpilih
- Ditambah: Navigasi bulan bersama di bagian atas halaman Statistik (`.stats-month-bar`) menggunakan pola navigasi kartu saldo Beranda (`#btn-stats-prev-month`, `#stats-month-label`, `#btn-stats-next-month`) yang mengendalikan grafik pemasukan, grafik pengeluaran, dan diagram donut sekaligus (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Penanda visual yang jelas pada grafik batang bulanan di mana batang untuk bulan yang dipilih ditampilkan dengan warna penuh (`opacity: 1`), label nilai di atas batang dan label bulan menjadi tebal (`font-weight: 700`, `var(--text)`), sementara batang bulan lainnya tampil lebih pudar (`opacity: 0.35`, `var(--text-secondary)`) sesuai token `dev/DESIGN.md` pada tema gelap dan terang (`css/style.css`, `js/app.js`).
- Ditambah: Logika pergeseran rentang jendela grafik bulanan (`statsChartWindowStart`, `statsChartWindowEnd`) agar otomatis bergeser memuat bulan terpilih bila pengguna memilih bulan di luar rentang grafik awal (`js/app.js`).
- Diperbaiki: Navigasi bulan khusus lama pada diagram donut dihapus dan digantikan sepenuhnya oleh navigasi bersama di bagian atas (`index.html`, `js/app.js`).
- Diubah: Interaksi klik/hover untuk memilih bulan pada grafik batang dinonaktifkan sesuai spesifikasi (`js/app.js`, `css/style.css`).
- Diubah: Membuka atau kembali ke tab Statistik dari halaman lain otomatis mengatur ulang bulan terpilih ke bulan berjalan (`js/app.js`).
- Diubah: Mencentang item "Statistik: satu bulan terpilih" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-18: Ikon neraca di header
- Ditambah: Ikon neraca (timbangan dua piringan) sederhana berupa SVG inline dengan ukuran 22 x 22 px dan warna aksen `var(--accent)` di sebelah kiri tulisan "Mizan Okane" pada header aplikasi (`index.html`, `css/style.css`).
- Ditambah: Penataan flexbox pada `.app-brand` dengan jarak `gap: 8px` sesuai skala spasi `dev/DESIGN.md`, serta atribut `aria-hidden="true"` dan `pointer-events: none` agar ikon bersifat dekoratif tanpa mengganggu tombol atau navigasi header (`index.html`, `css/style.css`).
- Diubah: Mencentang item "Ikon neraca di header" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-18: Perbaikan UI: grafik bulanan Statistik
- Diperbaiki: Penentuan jendela bulan pada fungsi `renderMonthlyChart` diubah agar selalu berakhir di bulan aktif (bulan yang dipilih di Statistik) dengan panjang jendela dihitung inklusif dari bulan data terawal relevan dan dibatasi minimal 3 serta maksimal 6 bulan (`js/app.js`).
- Diperbaiki: Bulan di dalam rentang jendela yang tidak memiliki transaksi kini tetap digambar dengan nilai 0 dan label nama bulan tetap muncul rapi tanpa pembagian dengan nol (`js/app.js`).
- Diubah: Navigasi pemindah bulan di Statistik kini memicu pembaruan `renderMonthlyChart` sehingga grafik pengeluaran dan pemasukan bulanan tetap sinkron dengan bulan aktif (`js/app.js`).
- Diubah: Mencentang item "Perbaikan UI: grafik bulanan Statistik" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-17: Perbaikan UI: polish kecil
- Ditambah: Ikon kategori Makan baru berwujud sendok dan garpu pada fungsi `getCategoryIconSvg` sehingga kategori default Makan memiliki visual spesifik dan tidak lagi jatuh ke ikon default tag (`js/app.js`).
- Ditambah: Subteks penjelas "Penyesuaian saldo bulan ini" di bawah label Equilibrium pada halaman Timbangan beserta penataan baris fleksibel responsif agar label, subteks, dan nominal tetap rapi dan tidak terpotong di layar sempit (`index.html`, `css/style.css`).
- Diperbaiki: Efek redup kartu dompet bersaldo 0 (.wallet-balance-item.is-zero) diubah sehingga hanya berlaku pada angka saldo dan border kartu menggunakan token DESIGN.md (`var(--surface-raised)` dan `var(--text-secondary)`), sementara nama dompet mempertahankan warna dan opasitas normal agar kontras teks tetap terbaca sesuai standar WCAG AA (`css/style.css`).
- Diubah: Mencentang item "Perbaikan UI: polish kecil" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-17: Perbaikan UI: semantik dan keyboard
- Diperbaiki: Atribut `role="listitem"` pada 8 tombol submenu Pengaturan (.settings-menu-row) dan atribut `role="list"` pada wadah pembungkusnya (.settings-menu-group) dihapus untuk memulihkan semantik alami tombol bagi pembaca layar (`index.html`).
- Ditambah: Aturan fokus keyboard `:focus-visible` untuk tombol pemilihan tipe transaksi (.type-segment-btn) dan tombol chip kategori (.category-chip) di form transaksi menggunakan token aksen resmi `dev/DESIGN.md` (`css/style.css`).
- Diperbaiki: Label aksesibel `aria-label` tombol Ubah dan Hapus pada 5 transaksi terakhir Beranda dan daftar Riwayat kini menyertakan nama kategori transaksi yang sesuai (`js/app.js`).
- Diperbaiki: Elemen select tersembunyi `#tx-category` diberi atribut `tabindex="-1"` dan `aria-hidden="true"` sehingga tidak lagi menjadi perhentian fokus keyboard kosong saat navigasi form (`index.html`).
- Diperbaiki: Atribut `tabindex` dan `role="button"` pada segmen lingkaran SVG diagram donut Statistik dihapus agar perhentian fokus keyboard hanya berada di item legenda diagram (`js/app.js`).
- Diubah: Mencentang item "Perbaikan UI: semantik dan keyboard" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-17: Perbaikan UI: target sentuh minimal 44 px
- Diperbaiki: Area target sentuh tombol aksi sub kategori (.btn-edit-subcat dan .btn-delete-subcat) ditingkatkan tinggi minimalnya dari 40 px menjadi 44 px (`css/style.css`).
- Diperbaiki: Ukuran tombol kembali di halaman Timbangan (.btn-timbangan-back) dan tombol bersihkan pencarian riwayat (.btn-search-clear) ditingkatkan dari 40 x 40 px menjadi 44 x 44 px (`css/style.css`).
- Diubah: Memverifikasi ukuran akhir tombol navigasi bulan (.btn-month-nav) yang telah memenuhi standar minimal 44 x 44 px (`css/style.css`).
- Diubah: Mencentang item "Perbaikan UI: target sentuh minimal 44 px" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-17: Audit UI: temuan desain dan aksesibilitas
- Ditambah: Spesifikasi item audit UI serta pelaksanaan pemeriksaan menyeluruh terhadap standar WCAG AA dan token `dev/DESIGN.md` pada 6 halaman aplikasi (Beranda, Riwayat, form tambah & edit transaksi, Timbangan, Statistik, Pengaturan) tanpa perubahan pada kode aplikasi (`dev/PLANSPEC.md`).
- Diubah: Mencentang item "Audit UI: temuan desain dan aksesibilitas" di `dev/PLAN.md`.

## 2026-10-17: Perbaikan UI: tombol tambah, banner data contoh, header budget
- Diperbaiki: Ruang kosong bawah pada kelima halaman (Beranda, Riwayat, Timbangan, Statistik, Pengaturan) ditingkatkan sehingga konten paling bawah dapat digulir sampai benar-benar di atas tombol tambah melayang (FAB) dan bar navigasi bawah (`css/style.css`).
- Diperbaiki: Banner penanda "Mode Data Contoh" dirampingkan menjadi strip satu baris dengan ketinggian separuh dari sebelumnya, margin vertikal lebih ringkas, dan penyembunyian otomatis teks deskripsi sekunder pada layar sempit tanpa mengubah fungsionalitas tombol Keluar (`css/style.css`).
- Diperbaiki: Tata letak header kartu Budget ditata ulang menjadi dua baris rapi (baris atas: judul Budget dan pilihan mata uang; baris bawah: navigasi bulan) sehingga tidak bertumpuk di layar HP (`index.html`, `css/style.css`).
- Diperbaiki: Teks duplikasi "Sisa: Sisa Rp ..." pada ringkasan budget diperbaiki menjadi "Sisa Rp ...", titik dua pada label dihilangkan, serta diterapkan aturan pencegahan pemotongan teks agar label dan nominal ("Total Budget", "Terpakai", "Sisa") terbaca penuh di layar sempit (`js/app.js`, `css/style.css`).
- Diubah: Versi service worker dinaikkan ke `mizan-v60` (`sw.js`).
- Diubah: Mencentang item "Perbaikan UI: tombol tambah, banner data contoh, header budget" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-17: Penyesuaian saldo di Timbangan
- Ditambah: Bagian "Sesuaikan Saldo" pada halaman Timbangan yang menampilkan daftar dompet aktif, saldo di aplikasi, dan tombol "Sesuaikan" untuk menyamakan saldo aplikasi dengan saldo riil fisik atau rekening bank (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Dialog "Sesuaikan Saldo" dengan pertanyaan "Berapa saldo riil sekarang?", tampilan saldo aplikasi sebagai pembanding, kolom input nominal dengan pemformatan mata uang, perhitungan selisih otomatis (plus/minus), pesan "Saldo sudah sama", serta pilihan kategori (Bunga, Cashback, Koreksi) ketika selisih positif atau otomatis Koreksi ketika negatif (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Baris "Equilibrium" di bawah timbangan bulan ini yang menampilkan total penyesuaian pada bulan yang sedang ditampilkan Timbangan (plus warna hijau, minus warna merah, per mata uang bila lebih dari satu) tanpa memengaruhi kemiringan timbangan (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Tipe transaksi baru `penyesuaian` dengan kategori tetap (Bunga, Cashback, Koreksi) yang tersimpan di IndexedDB, disinkronkan ke Google Drive, diekspor/diimpor pada cadangan JSON dan backup lengkap, serta diekspor pada file CSV dengan tipe "Penyesuaian" (`js/app.js`).
- Ditambah: Dukungan kompatibilitas backward pada validator transaksi `isValidTransaction` agar cadangan dengan transaksi penyesuaian (termasuk nominal negatif) dapat diimpor tanpa kendala, dan cadangan lama tanpa penyesuaian tetap berhasil diimpor (`js/app.js`).
- Diubah: Perhitungan saldo dompet, total saldo kartu Beranda, dan total netto riwayat harian kini memperhitungkan transaksi penyesuaian secara matematis akurat di semua mata uang (IDR, JPY, USD) (`js/app.js`).
- Diubah: Transaksi penyesuaian tidak dihitung pada surplus/defisit Timbangan, diagram donut, grafik statistik, ringkasan pemasukan/pengeluaran Riwayat, budget kategori, serta angka pemasukan/pengeluaran kartu saldo Beranda (`js/app.js`).
- Diubah: Transaksi penyesuaian di Riwayat tampil dengan label "Penyesuaian: [Kategori]", ikon timbangan garis, tanda plus/minus sesuai arah selisih, dan hanya dapat dihapus (soft delete) tanpa opsi ubah nominal (`js/app.js`, `css/style.css`).
- Diubah: Versi service worker dinaikkan ke `mizan-v59` (`sw.js`).
- Diubah: Mencentang item "Penyesuaian saldo di Timbangan" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-17: Geser urutan dompet dan kategori
- Ditambah: Pegangan geser berikon garis tiga (`≡`, `.btn-drag-handle`) pada setiap baris daftar dompet, daftar kategori pemasukan, daftar kategori pengeluaran, dan daftar sub kategori di Pengaturan untuk mengatur urutan secara manual (`css/style.css`, `js/app.js`).
- Ditambah: Mekanisme geser urutan berbasis pointer events (`enableListDragReorder`) yang mendukung interaksi sentuh (layar HP/APK) dan mouse (PC) tanpa mengganggu kemampuan scroll halaman ketika menyentuh area baris di luar ikon pegangan (`js/app.js`, `css/style.css`).
- Ditambah: Gaya visual baris terangkat saat digeser (`.is-dragging`, `var(--surface-raised)`, `border: 1px dashed var(--accent)`) dan penanda tempat tujuan (`.drag-placeholder`) yang sepenuhnya mematuhi token dan panduan `dev/DESIGN.md` tanpa warna, bayangan, atau animasi tambahan (`css/style.css`).
- Ditambah: Properti `order` pada data dompet, kategori, dan sub kategori yang otomatis disimpan ke IndexedDB saat baris dilepas, dipertahankan pada data demo, serta disinkronkan ke Google Drive, ekspor/impor JSON, dan backup lengkap (`js/db.js`, `js/sync.js`, `js/app.js`).
- Ditambah: Logika kompatibilitas mundur (`sanitizeCategories`, `sanitizeWallets`) yang otomatis memberikan urutan berurutan pada data lama tanpa field urutan berdasarkan urutan kemunculannya di penyimpanan atau file cadangan (`js/db.js`, `js/sync.js`, `js/app.js`).
- Diubah: Urutan dompet dan kategori yang telah disesuaikan kini tercermin di seluruh bagian aplikasi: kartu dompet Beranda, pilihan dompet form transaksi & transfer, pilihan kategori form, daftar budget, dan daftar Pengaturan (`js/app.js`).
- Diubah: Kategori baru dan dompet baru secara otomatis ditambahkan pada urutan paling bawah (`js/app.js`).
- Diubah: Reset setelan pabrik mengembalikan urutan kategori dan dompet ke urutan bawaan (`js/db.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v58` (`sw.js`).
- Diubah: Mencentang item "Geser urutan dompet dan kategori" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-17: Dompet dipilih manual di form transaksi dan transfer
- Ditambah: Pengaturan status tombol Simpan (`updateSaveButtonState`) yang otomatis nonaktif selama ada dompet yang belum dipilih pada form tambah transaksi (pemasukan/pengeluaran) maupun transfer antar dompet (`js/app.js`, `index.html`, `css/style.css`).
- Diubah: Pemilihan dompet pada form tambah transaksi dan transfer kini wajib dipilih manual oleh pengguna: kolom dompet (dan ke dompet pada transfer) awalnya kosong dengan teks petunjuk "Pilih dompet", tidak lagi mengingat pilihan dari transaksi/sesi sebelumnya atau filter Riwayat, dan kembali kosong setelah disimpan (`js/app.js`, `index.html`).
- Diubah: Pada form tambah transaksi, jika pengguna hanya memiliki satu dompet aktif, dompet tersebut langsung otomatis terpilih (`js/app.js`).
- Diubah: Pada form transfer, pemilihan otomatis dompet dihilangkan sehingga dompet asal dan tujuan harus dipilih manual secara mandiri dengan aturan validasi antar dompet yang sama tetap terjaga (`js/app.js`).
- Diubah: Pada form edit transaksi dan edit transfer lama, dompet aslinya tetap ditampilkan dan tidak dapat dikosongkan (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v57` (`sw.js`).
- Diubah: Mencentang item "Dompet dipilih manual di form transaksi dan transfer" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-17: Tentang Pengembang
- Ditambah: Baris menu baru "Tentang Pengembang" di Pengaturan pada kartu Tentang yang mengarahkan ke web portofolio pembuat aplikasi `https://mizoarkatamarenaldy.github.io` (`index.html`).
- Ditambah: Penanganan navigasi portofolio pengembang `openDeveloperPortfolio` di `js/app.js` yang membuka URL di tab baru dengan `rel="noopener"` pada web browser, serta memanfaatkan plugin Capacitor `@capacitor/app-launcher` (dengan fallback `@capacitor/browser` dan browser sistem) agar terbuka di browser bawaan HP di luar aplikasi pada mode APK Android.
- Ditambah: Dependensi plugin `@capacitor/app-launcher` pada `package.json` dan `package-lock.json` untuk mendukung pembukaan browser eksternal bawaan perangkat Android.
- Diubah: Pembaruan deskripsi submenu Tentang pada baris menu Pengaturan (`index.html`).
- Diubah: Versi service worker dinaikkan ke `mizan-v56` (`sw.js`).
- Diubah: Mencentang item "Tentang Pengembang" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-17: Tombol Timbangan di tengah navigasi
- Ditambah: Tombol Timbangan menonjol di tengah bar navigasi bawah HP (`.bottom-nav .nav-tab-center-btn`) dengan ikon timbangan di dalam wadah melingkar (`.nav-center-icon-wrap`) yang sedikit menonjol ke atas dari bar navigasi (`index.html`, `css/style.css`).
- Ditambah: Gaya tab tengah pada navigasi desktop (`.desktop-nav .nav-tab-center-btn`) agar Timbangan tampil menonjol dan seimbang di antara menu kiri dan kanan (`index.html`, `css/style.css`).
- Diubah: Penataan menu navigasi kini membagi menu lain secara seimbang di kiri (Beranda, Riwayat) dan di kanan (Statistik, Pengaturan) mengapit tombol Timbangan di tengah, baik pada bar navigasi bawah HP maupun tab desktop (`index.html`).
- Diubah: Dokumentasi panduan desain `dev/DESIGN.md` diperbarui dengan menambahkan aturan tombol tengah navigasi Timbangan (`dev/DESIGN.md`).
- Diubah: Versi service worker dinaikkan ke `mizan-v55` (`sw.js`).
- Diubah: Mencentang item "Tombol Timbangan di tengah navigasi" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-17: Catatan di bawah angka
- Ditambah: Tampilan baris catatan tersendiri (`.tx-item-note-row`) pada item transaksi di bawah baris kategori dan nominal (`js/app.js`, `css/style.css`).
- Ditambah: Animasi geser teks pelan (`@keyframes txNoteScroll`, `.tx-item-note.is-scrolling`) pada catatan transaksi yang melebihi lebar kontainer agar terbaca penuh, dengan dukungan pengaturan kurangi animasi (`prefers-reduced-motion`) di mana teks terpotong rapi dengan "..." tanpa bergerak (`css/style.css`).
- Ditambah: Fungsi pembantu `updateSingleNoteMarquee` dan `checkTxNoteMarquees` dengan pengamatan otomatis via `ResizeObserver` serta pendengar event resize jendela dan perubahan preferensi gerakan layar (`js/app.js`).
- Diubah: Tata letak baris pertama transaksi (`.tx-item-main`) kini menampung kategori di sisi kiri dan kolom nominal (`.tx-item-amount-col`) di sisi kanan secara fleksibel (`js/app.js`, `css/style.css`).
- Diubah: Dokumentasi panduan desain `dev/DESIGN.md` diperbarui dengan menambahkan aturan animasi geser catatan transaksi (`dev/DESIGN.md`).
- Diubah: Versi service worker dinaikkan ke `mizan-v54` (`sw.js`).
- Diubah: Mencentang item "Catatan di bawah angka" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-17: Pencarian riwayat
- Ditambah: Tombol toggle pencarian (`btn-toggle-search`) di area filter Riwayat Transaksi untuk membuka/menutup kolom pencarian (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Kolom pencarian riwayat (`history-search-bar`, `history-search-input`) dengan ikon pencarian dan tombol bersihkan cepat (`btn-clear-search`) serta navigasi keyboard (Escape untuk membersihkan/menutup) (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Fungsi pencarian `matchesHistorySearch`, `openHistorySearch`, dan `closeHistorySearch` di `js/app.js` untuk mencari transaksi berdasarkan catatan, nama kategori, nama sub kategori, nominal (mentah dan terformat), serta nama dompet (asal dan tujuan transfer) tanpa membedakan huruf besar kecil (case-insensitive).
- Diubah: Penyaringan transaksi pada Riwayat di `render()` kini bekerja bersama filter dompet, filter bulan, dan kata kunci pencarian aktif (`js/app.js`).
- Diubah: Ringkasan pemasukan dan pengeluaran Riwayat (`riwayat-summary`) kini secara dinamis menghitung transaksi yang sesuai dengan hasil pencarian lewat pembaruan parameter pada `getRiwayatMonthSummary` dan `renderRiwayatSummary` (`js/app.js`).
- Diubah: Tampilan keadaan kosong di Riwayat menampilkan pesan "Tidak ada transaksi yang cocok." saat pencarian tidak menghasilkan data (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v53` (`sw.js`).
- Diubah: Mencentang item "Pencarian riwayat" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-17: Warna kartu saldo multi mata uang
- Diperbaiki: Penyelarasan warna teks baris kartu saldo Beranda saat terdapat lebih dari satu mata uang aktif sehingga konsisten dengan tampilan satu mata uang: Total Saldo putih (`var(--text)`), Pemasukan hijau (`var(--income)`), dan Pengeluaran merah (`var(--expense)`) untuk setiap baris mata uang (`css/style.css`, `js/app.js`).
- Diubah: Penambahan class pendukung `.summary-income-line` dan `.summary-expense-line` pada baris multi mata uang kartu saldo Beranda (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v52` (`sw.js`).
- Diubah: Mencentang item "Warna kartu saldo multi mata uang" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-17: Filter bulan di Riwayat
- Ditambah: Dropdown filter bulan (`filter-month`) di samping dropdown dompet pada halaman Riwayat Transaksi dengan opsi "Semua bulan" dan daftar bulan berurutan menurun (`index.html`, `js/app.js`).
- Ditambah: Ringkasan pemasukan (warna hijau) dan pengeluaran (warna merah) di samping judul Riwayat Transaksi (`riwayat-summary`) yang mendukung tampilan per mata uang (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Fungsi `getRiwayatMonthSummary`, `populateMonthFilterOptions`, dan `renderRiwayatSummary` di `js/app.js` untuk menghitung dan menampilkan ringkasan riwayat berdasarkan filter bulan dan dompet sekaligus.
- Diubah: Pemilihan bulan di Riwayat terhubung bersama navigasi bulan kartu saldo Beranda: mengubah bulan di salah satu tempat memperbarui tempat lainnya, sedangkan memilih opsi "Semua bulan" hanya berlaku di Riwayat tanpa mengubah dashboard Beranda (`js/app.js`).
- Diubah: Penyaringan transaksi pada Riwayat di `render()` kini menerapkan filter bulan terpilih (`currentHistoryMonthFilter`) dan filter dompet (`currentWalletFilter`) (`js/app.js`).
- Diubah: Penyesuaian tata letak dan tipografi header Riwayat pada `.list-header`, `.list-header-title-wrap`, `.riwayat-summary`, dan `.list-filter` sesuai aturan `dev/DESIGN.md` (`css/style.css`).
- Diubah: Versi service worker dinaikkan ke `mizan-v51` (`sw.js`).
- Diubah: Mencentang item "Filter bulan di Riwayat" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-17: Klik dompet untuk filter riwayat
- Ditambah: Interaktivitas kartu dompet di Beranda yang dapat diklik untuk memfilter daftar transaksi di halaman Riwayat sesuai dompet yang dipilih (`js/app.js`, `css/style.css`).
- Ditambah: Fungsi `handleWalletCardClick` di `js/app.js` untuk mengatur nilai dropdown `filter-wallet` di Riwayat, menyinkronkan status filter, dan berpindah ke tab Riwayat.
- Ditambah: Penanda visual kartu terpilih (`is-selected`) pada kartu dompet di Beranda dengan warna border aksen dan latar surface-raised sesuai `dev/DESIGN.md` (`css/style.css`).
- Diubah: Perilaku klik pada kartu dompet yang sedang aktif terpilih akan mengembalikan pilihan filter ke "Semua Dompet" (`all`) dan menampilkan seluruh transaksi di halaman Riwayat (`js/app.js`).
- Diubah: Elemen kartu dompet dirender sebagai elemen interaktif bertipe tombol (`button`) dengan atribut aksesibilitas `aria-pressed`, `aria-label`, serta indikator fokus `:focus-visible` (`js/app.js`, `css/style.css`).
- Diubah: Versi service worker dinaikkan ke `mizan-v50` (`sw.js`).
- Diubah: Mencentang item "Klik dompet untuk filter riwayat" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-17: Data contoh lengkap
- Ditambah: Generator data contoh satu set tetap (`getSampleTransactions`, `SAMPLE_WALLETS`, `SAMPLE_CATEGORIES`, `SAMPLE_BUDGETS`) di `js/db.js` dengan data lengkap mencakup dompet IDR, JPY, USD, kategori dan sub kategori, budget dengan indikator aman dan peringatan, transaksi pemasukan, pengeluaran, serta transfer antar dompet satu mata uang.
- Ditambah: Pemisahan penyimpanan data contoh di `META_STORE` (`demo_transactions`, `demo_wallets`, `demo_categories`, `demo_budgets`) serta fungsi pengelola mode contoh (`isDemoMode`, `activateSampleData`, `clearSampleData`) di `js/db.js` sehingga data asli pengguna aman dan tidak tertimpa.
- Ditambah: Penanda banner visual jelas (`demo-mode-indicator`) di bawah header aplikasi yang menampilkan status mode data contoh beserta tombol cepat untuk keluar dari mode contoh (`index.html`, `css/style.css`).
- Ditambah: Tombol "Keluar Mode Data Contoh" di Pengaturan pada kartu Data untuk kembali ke data asli kapan saja (`index.html`, `js/app.js`).
- Diubah: Perilaku tombol "Isi Data Contoh" di Pengaturan kini memuat satu set tetap yang identik setiap kali diaktifkan atau dimuat ulang (`js/app.js`).
- Diubah: Isolasi penuh dari Google Drive saat mode contoh aktif dengan menghentikan proses `runSync`, `syncNow`, dan `markDirty` sehingga tidak ada data contoh yang dikirim ke Drive (`js/sync.js`).
- Diubah: Ekspor JSON dan CSV selalu mengambil data asli pengguna lewat `getRealTransactions`, `getRealWallets`, `getRealCategories`, dan `getRealBudgets`, serta impor JSON dicegah saat berada dalam mode data contoh (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v49` (`sw.js`).
- Diubah: Mencentang item "Data contoh lengkap" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.

## 2026-10-17: Reset setelan pabrik
- Ditambah: Fungsi `resetDatabase` di `js/db.js` untuk mengosongkan store transaksi dan mengembalikan kategori, sub kategori, dompet, budget, pengaturan (tema dan format jam), dan nomor revisi lokal ke kondisi awal bawaan.
- Ditambah: Fungsi `clearRemoteData` di `js/sync.js` untuk mengosongkan file cadangan di Google Drive `appDataFolder` dengan menghapus seluruh data transaksi buatan pengguna dan menyetel ulang kategori, dompet, budget, dan pengaturan ke bawaan awal tanpa memutus sesi login Google.
- Diubah: Perilaku `performDeleteAllData` di `js/app.js` yang sebelumnya hanya menandai soft-delete transaksi kini melakukan reset total ke setelan pabrik: membersihkan data transaksi (asli dan dummy), dompet, kategori, sub kategori, budget, pengaturan lokal, mengosongkan data Google Drive jika sedang login, serta memuat ulang UI ke tampilan awal.
- Diubah: Tombol `btn-delete-all-data` selalu membuka dialog konfirmasi tanpa memblokir saat transaksi kosong (`js/app.js`).
- Diubah: Teks deskripsi kartu Zona Bahaya dan dialog konfirmasi `delete-all-dialog` diperbarui untuk menegaskan bahwa transaksi, dompet, kategori, sub kategori, budget, dan pengaturan akan ikut terhapus serta menyarankan pengguna melakukan ekspor cadangan terlebih dahulu (`index.html`).
- Diubah: Versi service worker dinaikkan ke `mizan-v48` (`sw.js`).
- Diubah: Mencentang item "Reset setelan pabrik" di `dev/PLAN.md` serta menambahkan spesifikasinya di `dev/PLANSPEC.md`.


## 2026-10-17: Hapus dompet
- Ditambah: Fungsi pembantu `isWalletDeleted` di `js/app.js` untuk mendeteksi dompet yang terarsip (soft delete).
- Diubah: Dialog konfirmasi penghapusan dompet pada `handleDeleteWalletPrompt` kini menampilkan nama dompet beserta sisa saldonya, serta menghapus batasan pemblokiran penghapusan dompet yang masih memiliki riwayat transaksi (`js/app.js`).
- Diubah: Eksekusi penghapusan dompet pada `performDeleteWallet` memperbarui opsi dompet, filter riwayat, dan menampilkan notifikasi sukses (`js/app.js`).
- Diubah: Riwayat transaksi dompet terarsip di `createTxItemElement` tetap menampilkan nama dompet yang disertai label `(dihapus)` baik untuk transaksi pemasukan/pengeluaran maupun transfer (`js/app.js`).
- Diubah: Perhitungan ringkasan kartu Beranda pada `getBerandaMonthSummary` mengecualikan dompet terarsip dari Total Saldo namun tetap memperhitungkan riwayat transaksi pada ringkasan pemasukan dan pengeluaran per bulan (`js/app.js`).
- Diubah: Deteksi dompet pada transfer antar dompet di `updateTypeUI` mendukung mata uang Rupiah, Yen, dan USD (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v47` (`sw.js`).
- Diubah: Mencentang item "Hapus dompet" di `dev/PLAN.md` serta menambahkan spesifikasi di `dev/PLANSPEC.md`.

## 2026-10-17: Backup lengkap
- Ditambah: Fungsi `getSettings` dan `saveSettings` di `js/db.js` untuk menyimpan dan membaca preferensi pengguna (tema dan format jam) dengan penanda waktu pembaruan `updatedAt` di store `meta` IndexedDB.
- Ditambah: Fungsi `getAppSettings` di `js/app.js` untuk menyatukan pengaturan tema dan format waktu aktif beserta timestamp.
- Diubah: Pembuatan file cadangan ekspor JSON pada `exportBackupJson` ditingkatkan ke format versi 2 (`version: 2` dan `schemaVersion: 2`) yang memuat data lengkap meliputi transaksi pengguna (tanpa data contoh), dompet (termasuk mata uang dan status arsip), kategori, sub kategori, budget, dan pengaturan (`js/app.js`).
- Diubah: Logika impor cadangan JSON pada `handleImportJson` mendukung pemulihan pengaturan aplikasi (tema dan format waktu), mengabaikan data contoh, tetap menjaga kompatibilitas mundur untuk file cadangan lama tanpa nomor versi atau tanpa pengaturan, serta konsisten menerapkan aturan resolusi konflik (ID unik dan updatedAt terbaru menang) (`js/app.js`).
- Diubah: Sinkronisasi Google Drive pada `js/sync.js` disamakan dengan struktur cadangan baru: versi skema dinaikkan ke versi 2 serta payload, pengunggahan, dan resolusi konflik turut menyertakan data pengaturan (`settings`).
- Diubah: Penyesuaian pendengar perubahan tema dan format waktu untuk memicu `saveSettings` dan `markDirty` sinkronisasi Drive (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v46` (`sw.js`).
- Diubah: Mencentang item "Backup lengkap" di `dev/PLAN.md` serta menambahkan spesifikasi di `dev/PLANSPEC.md`.

## 2026-10-17: Audit logika matematis
- Diperbaiki: Penanganan tanda negatif pada pembacaan nominal mata uang IDR dan JPY di fungsi `parseAmount` agar angka negatif seperti `-1.500.000` tidak diubah menjadi positif (`js/app.js`).
- Diperbaiki: Masalah penempelan (paste) atau input angka IDR/JPY berformat koma/sen (misal `1.500.000,00` atau `25.000,50`) di fungsi `parseAmount` dan `formatInputWithCursor` agar pecahan koma dipotong dan tidak keliru mengalikan nilai nominal 100 kali lipat (`js/app.js`).
- Diperbaiki: Pergeseran tanggal dan bulan akibat perbedaan zona waktu pada `formatDate` dengan mengganti parsing `new Date(dateString)` menjadi pembacaan langsung komponen tanggal dari string YYYY-MM-DD (`js/app.js`).
- Diperbaiki: Pencegahan ekor desimal floating point (seperti `0.1 + 0.2`) pada kalkulasi USD serta pembulatan bilangan bulat konsisten untuk IDR dan JPY pada `renderMonthlyChart`, `renderDonutChart`, `getWalletCurrentBalance`, `getTimbanganMonthlyData`, `renderTimbanganCalculator`, `getBerandaMonthSummary`, `render`, dan `getActiveMonthlyExpenses` (`js/app.js`).
- Diperbaiki: Dukungan angka negatif, kelipatan triliun (T), dan pembulatan desimal bersih pada fungsi `formatCompactCurrency` (`js/app.js`).
- Diperbaiki: Pengecekan transaksi berpenanda `tx.deleted` pada kalkulasi akumulasi saldo dompet di fungsi `render()` (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v45` (`sw.js`).
- Diubah: Mencentang item "Audit logika matematis" di `dev/PLAN.md` serta menambahkan spesifikasi di `dev/PLANSPEC.md`.

## 2026-10-17: Catat rencana Tauri, Kotlin, dan pemisahan folder (sesi dokumen saja)
- Ditambah: Bagian "Rencana setelah Tahap 5" di paling bawah `dev/PLAN.md` berisi Konversi Tauri, Konversi Kotlin, dan Pemisahan folder (hanya dicatat dan belum dikerjakan).
- Ditambah: Tiga bagian spesifikasi di akhir berkas `dev/PLANSPEC.md` untuk Konversi Tauri, Konversi Kotlin, dan Pemisahan folder (hanya dicatat dan belum dikerjakan).

## 2026-10-16: Daftarkan rencana fitur baru (sesi dokumen saja)
- Ditambah: 12 baris rencana fitur baru pada daftar Tahap 5 di `dev/PLAN.md`.
- Ditambah: 12 bagian spesifikasi lengkap fitur baru di akhir berkas `dev/PLANSPEC.md`.

## 2026-10-15: Logo aplikasi
- Ditambah: Berkas logo sumber `assets/icon-only.png`.
- Ditambah: Berkas ikon web dan PWA baru `icons/favicon-32x32.png` dan `icons/apple-touch-icon.png`.
- Ditambah: Tautan favicon 32x32 dan apple-touch-icon 180x180 pada tag `<head>` di `index.html`.
- Diubah: Memperbarui ikon PWA standar 192x192 dan 512x512 purpose "any" (`icons/icon-192x192.png`, `icons/icon-512x512.png`) serta ikon maskable 192x192 dan 512x512 dengan logo di tengah 80% dan latar belakang warna sudut sumber (`icons/icon-192x192-maskable.png`, `icons/icon-512x512-maskable.png`).
- Diubah: Versi service worker dinaikkan ke `mizan-v44` dan menambahkan ikon favicon serta apple-touch-icon ke daftar precache `ASSETS_TO_CACHE` (`sw.js`).
- Diubah: Menambahkan dan mencentang item di `dev/PLAN.md` serta menambahkan spesifikasi di `dev/PLANSPEC.md`.

## 2026-10-15: Beranda: sembunyikan budget dan dompet bila tidak perlu
- Ditambah: Penanda ID `wallets-section` dan `budget-section` serta kelas bawaan `hidden` pada elemen kartu dompet dan kartu budget di Beranda (`index.html`).
- Ditambah: Referensi DOM `walletsSection` dan `budgetSection` untuk mengatur visibilitas kartu di Beranda secara dinamis (`js/app.js`).
- Diubah: Menyembunyikan deretan kartu dompet di Beranda jika jumlah dompet aktif kurang dari 2 (hanya 1 dompet atau belum ada), dan menampilkannya hanya jika ada 2 dompet atau lebih (`js/app.js`).
- Diubah: Menyembunyikan seluruh kartu Budget di Beranda jika belum ada kategori dengan batas budget yang tersimpan, dan menampilkannya hanya jika minimal satu budget kategori telah diatur (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v43` (`sw.js`).
- Diubah: Menambahkan dan mencentang item di `dev/PLAN.md` serta menambahkan spesifikasi fitur di `dev/PLANSPEC.md`.

## 2026-10-15: Mata uang opsional
- Ditambah: Template prompt untuk instruksi Antigravity (`dev/PROMPT.md`).
- Ditambah: Fungsi `getAvailableCurrencies` untuk mendeteksi ketersediaan mata uang (Rupiah selalu tersedia, Yen jika ada minimal satu dompet Yen aktif yang belum terhapus, Dolar jika ada minimal satu dompet Dolar aktif yang belum terhapus) (`js/app.js`).
- Ditambah: Atribut ID pada elemen pemilih mata uang di kartu budget Beranda (`budget-currency-control`), halaman Statistik (`stats-currency-bar`), dan halaman Timbangan (`timbangan-currency-bar`) (`index.html`).
- Diubah: Menyembunyikan tombol Yen dan Dolar jika pengguna belum memiliki dompet aktif dengan mata uang tersebut, serta menyembunyikan kontrol pemilih mata uang sama sekali jika hanya Rupiah yang tersedia di kartu budget Beranda, halaman Statistik, dan halaman Timbangan (`js/app.js`, `index.html`).
- Diubah: Mengembalikan pilihan mata uang secara otomatis ke Rupiah jika mata uang yang sedang dipilih menjadi tidak tersedia (misalnya saat dompet terakhir dengan mata uang tersebut dihapus) (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v42` (`sw.js`).
- Diubah: Memperbarui aturan kerja larangan perintah build dan alur staging di `dev/RULES.md`, penambahan dan pencentangan item di `dev/PLAN.md`, serta spesifikasi fitur di `dev/PLANSPEC.md`.

## 2026-10-15: Navigasi bulan di kartu saldo Beranda
- Ditambah: Pemilih bulan di kartu saldo Beranda di atas tulisan "TOTAL SALDO" dengan tombol bulan sebelumnya, nama bulan dan tahun di tengah (format Indonesia penuh, contoh: "Oktober 2026"), serta tombol bulan sesudahnya (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Penonaktifan tombol bulan sesudahnya jika bulan yang tampil adalah bulan berjalan, serta tombol bulan sebelumnya yang tidak dibatasi (`js/app.js`).
- Ditambah: Fungsi `getBerandaMonthSummary` untuk menghitung posisi saldo akhir bulan terpilih (akumulasi seluruh transaksi dari awal hingga akhir bulan terpilih) per mata uang (Rupiah, Yen, USD), serta total pemasukan dan pengeluaran khusus pada bulan terpilih dengan mengabaikan transaksi terhapus (`js/app.js`).
- Diubah: Pilihan bulan kembali ke bulan berjalan setiap kali aplikasi dibuka tanpa disimpan ke penyimpanan lokal, serta bagian "Timbangan Bulan Ini" tetap tidak diubah (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v41` (`sw.js`).

## 2026-10-15: Perketat petunjuk baca RULES dan PLANSPEC
- Diubah: Memperbarui baris petunjuk di `dev/PLAN.md` tepat di bawah judul agar mewajibkan membaca `dev/RULES.md` sebelum mengerjakan apa pun serta merujuk spesifikasi ke `dev/PLANSPEC.md` dan desain ke `dev/DESIGN.md`.
- Diubah: Memperbarui aturan kerja di `dev/RULES.md` mengenai kewajiban membaca `dev/RULES.md` dan `dev/PLAN.md` di awal sesi, membaca `dev/PLANSPEC.md` hanya pada item yang dikerjakan, ketentuan satu baris checkbox di `dev/PLAN.md` dengan spek di `dev/PLANSPEC.md`, serta prioritas spek jika berbeda dengan `dev/PLAN.md`.

## 2026-10-15: Rapikan dokumen ke folder dev
- Ditambah: Folder `dev/` sebagai tempat terpusat untuk dokumen rencana, aturan, dan spesifikasi.
- Ditambah: Berkas `dev/RULES.md` memuat aturan kerja dan alur commit/push untuk agent yang dipindahkan dari `PLAN.md` dengan penyesuaian jalur file dan aturan dua tempat untuk penulisan fitur baru.
- Ditambah: Berkas `dev/PLANSPEC.md` memuat spesifikasi lengkap dan sub-poin dari setiap item tahap dengan judul yang sama persis dengan judul singkat di `dev/PLAN.md`.
- Diubah: Memindahkan `PLAN.md`, `CHANGELOG.md`, dan `DESIGN.md` ke folder `dev/` (`dev/PLAN.md`, `dev/CHANGELOG.md`, `dev/DESIGN.md`) menggunakan `git mv`.
- Diubah: Berkas `dev/PLAN.md` dirampingkan hanya menyisakan judul, petunjuk berkas, keputusan terkunci, versi 1, dan daftar tahap ringkas (judul singkat dan checkbox) tanpa mengubah status centang.

## 2026-10-15: Tahap 5: Pengaturan per kategori (submenu)
- Ditambah: Restrukturisasi halaman Pengaturan menjadi daftar kelompok baris rapi dengan ikon garis outline, nama kelompok, penjelasan satu baris di bawahnya, dan panah kanan tanpa kartu bersarang (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Submenu terpisah untuk masing-masing kelompok pengaturan: Tampilan (tema, format jam), Kategori (pengeluaran, pemasukan, sub kategori), Dompet (kelola dompet, mata uang), Budget (batas pengeluaran bulanan), Data (ekspor CSV/JSON, impor JSON, data contoh), Akun dan sinkron (Google Drive), Tentang (kebijakan privasi, syarat dan ketentuan), dan Zona bahaya (hapus semua data) (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Header pada tiap submenu dengan tombol kembali (target sentuh minimal 44x44px) dan judul kelompok yang mengembalikan tampilan ke daftar utama Pengaturan (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Bagian "Zona Bahaya" diletakkan di paling bawah dan terpisah dari kelompok lain dengan penanda warna merah (#FF453A) sesuai DESIGN.md (`index.html`, `css/style.css`).
- Diperbaiki: Penanganan tombol back Android (`window.handleAndroidBack`) dari submenu kembali ke daftar utama Pengaturan, dan dari daftar Pengaturan mengikuti navigasi tab yang sudah ada tanpa merusak perilaku web/PWA (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v40` (`sw.js`).
- Diubah: `PLAN.md` menambahkan dan mencentang item "Pengaturan per kategori" di Tahap 5 (`PLAN.md`).

## 2026-10-15: Tahap 5: Timbangan
- Ditambah: Halaman baru "Timbangan" sesuai nama Mizan yang menampilkan keseimbangan keuangan dan simulasi belanja tanpa mengubah skema data (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Fitur "Timbangan bulan ini" dengan visualisasi timbangan garis outline SVG (non-emoji, miring statis tanpa animasi sesuai selisih pemasukan dan pengeluaran), pemilih bulan, rincian per sisi, dan label status "Surplus Rp ..." atau "Defisit Rp ..." (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Fitur kalkulator "Timbang sebelum beli" untuk menguji dampak belanja terhadap saldo dompet yang dipilih, sisa budget kategori (hanya untuk kategori ber-budget pada mata uang Rupiah), dan estimasi timbangan bulan ini setelah beli (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Label hasil kalkulator "Aman" (>20%), "Pas" (0-20%), atau "Berat" (lewat budget atau saldo minus/defisit) yang selalu disertai teks penjelas status, bukan hanya warna (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Dukungan multi mata uang (Rupiah, Yen, Dolar) pada halaman Timbangan dengan bawaan Rupiah dan pembatasan budget hanya untuk Rupiah (`index.html`, `js/app.js`).
- Ditambah: Akses navigasi tab desktop dan tautan langsung dari Beranda dengan navigasi bawah HP tetap 4 item (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Tampilan keadaan kosong (empty state) berbahasa Indonesia saat belum ada transaksi pada bulan yang dipilih (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v39` (`sw.js`).
- Diubah: `PLAN.md` menambahkan dan mencentang item "Timbangan" di Tahap 5 (`PLAN.md`).

## 2026-10-15: Tahap 5: Sub kategori
- Ditambah: Struktur data `subcategories` pada model kategori di IndexedDB, mendukung sub kategori satu tingkat opsional yang terpisah untuk pemasukan dan pengeluaran (`js/db.js`).
- Ditambah: Antarmuka pengelolaan sub kategori di bawah tiap kategori (tambah, ubah nama, hapus) pada Pengaturan beserta dialog konfirmasi web UI berbahasa Indonesia (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Pemilihan chip sub kategori opsional pada formulir tambah/ubah transaksi yang muncul dinamis setelah kategori dipilih dan dapat dikosongkan (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Tampilan "Kategori > Sub" pada baris transaksi di Riwayat dan Beranda (`js/app.js`).
- Ditambah: Rincian sub kategori pada diagram donut di halaman Statistik saat kategori disentuh atau dipilih (`css/style.css`, `js/app.js`).
- Ditambah: Kolom baru `subkategori` di paling kanan pada ekspor CSV dengan kolom lama tetap tidak berubah (`js/app.js`).
- Ditambah: Dukungan field opsional `subcategory` pada ekspor dan impor cadangan JSON, serta keamanan membaca data cadangan lama tanpa subcategory (`js/app.js`).
- Diubah: Penghapusan sub kategori mempertahankan data transaksi lama dan menampilkannya sebagai kategori utama saja, serta penghapusan kategori ikut menghapus daftar sub kategorinya (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v38` (`sw.js`).
- Diubah: `PLAN.md` menambahkan dan mencentang item "Sub kategori" di Tahap 5 (`PLAN.md`).

## 2026-10-14: Tahap 5: Mata uang per dompet, tahap 2: tambah USD dengan desimal
- Ditambah: Dukungan mata uang "USD" (Dolar) pada dompet di samping IDR dan JPY, serta opsi Dolar pada form tambah dompet di Pengaturan (`index.html`, `js/db.js`, `js/sync.js`, `js/app.js`).
- Ditambah: Format angka mata uang USD (`$ `) dengan pemisah ribuan titik (`.`) dan desimal koma (`,`) 2 digit (contoh `$ 1.234,56`), dengan data disimpan sebagai angka dibulatkan 2 desimal (`js/app.js`).
- Ditambah: Input jumlah mendukung koma desimal hanya untuk USD (maksimal 2 digit desimal di belakang koma, inputmode decimal di HP) dan konversi koma/titik otomatis saat mengetik atau menempel (`js/app.js`).
- Ditambah: Pilihan mata uang Dolar (USD) pada halaman Statistik dan Budget; budget tetap dibatasi hanya untuk Rupiah dengan pesan informatif saat memilih USD (`index.html`, `js/app.js`).
- Ditambah: Generator data contoh (dummy) mendukung nominal acak USD dengan 2 desimal (`js/app.js`).
- Diubah: Logika impor JSON menjaga nilai desimal transaksi USD tanpa membulatkan ke bilangan bulat (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v37` (`sw.js`).
- Diubah: `PLAN.md` mencentang item "Mata uang per dompet, tahap 2: tambah USD dengan desimal" (`PLAN.md`).

## 2026-10-14: Tahap 5: Mata uang per dompet, tahap 1: Rupiah dan Yen
- Ditambah: Field `currency` pada setiap dompet dengan pilihan "IDR" (Rupiah) atau "JPY" (Yen), dengan nilai bawaan dompet lama otomatis IDR (`js/db.js`, `js/sync.js`, `js/app.js`).
- Ditambah: Pilihan mata uang saat menambah dompet baru di Pengaturan serta badge mata uang (IDR / JPY) pada daftar dompet (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Prefix mata uang dinamis pada formulir transaksi (Rp atau ¥) yang menyesuaikan secara otomatis dengan mata uang dompet yang dipilih (`index.html`, `js/app.js`).
- Ditambah: Format angka khusus mata uang Yen (`¥ `) tanpa desimal berdampingan dengan Rupiah (`Rp `) (`js/app.js`).
- Ditambah: Tampilan Total Saldo dan ringkasan bulan (pemasukan/pengeluaran) menampilkan satu angka per mata uang saat terdapat beberapa mata uang aktif (`css/style.css`, `js/app.js`).
- Ditambah: Pilihan mata uang (IDR / JPY) pada halaman Statistik dan Budget dengan nilai bawaan Rupiah; budget pengeluaran dibatasi hanya untuk mata uang Rupiah dan menampilkan pesan informatif saat beralih ke Yen (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Pembatasan ketat transfer hanya antar dompet dengan mata uang yang sama, baik pada dropdown pemilihan dompet tujuan maupun pada validasi simpan transaksi (`js/app.js`).
- Diubah: Generator data contoh (dummy) menyesuaikan besaran nominal dan membatasi transfer antar dompet semata uang (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v36` (`sw.js`).
- Diubah: `PLAN.md` menambahkan item tahap 1 dan 2, lalu mencentang item tahap 1 ("Mata uang per dompet, tahap 1: Rupiah dan Yen") (`PLAN.md`).

## 2026-10-14: Tahap 5: Hapus semua data
- Ditambah: Bagian "Zona Bahaya" di halaman Pengaturan dengan tombol "Hapus Semua Data" yang menggunakan warna merah (#FF453A) dari tabel Warna makna (`index.html`, `css/style.css`).
- Ditambah: Dialog konfirmasi dua langkah (`delete-all-dialog`) berbahasa Indonesia yang memuat peringatan detail, penawaran tombol "Ekspor JSON (Cadangan)", input ketik "HAPUS", dan tombol konfirmasi yang baru aktif setelah pengguna mengetik "HAPUS" (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Logika penghapusan `performDeleteAllData` yang melakukan soft delete pada semua transaksi asli yang belum terhapus dengan `updatedAt` waktu sekarang agar ikut tersinkron ke perangkat lain dan Google Drive, membuang transaksi contoh (dummy) langsung dari IndexedDB tanpa soft delete, serta mempertahankan kategori, dompet, budget, dan tema (`js/app.js`).
- Ditambah: Fungsi `exportBackupJson` untuk mengekspor cadangan JSON yang dapat dijalankan langsung dari dalam dialog konfirmasi sebelum penghapusan maupun melalui tombol di Pengaturan (`js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v35` (`sw.js`).
- Diubah: `PLAN.md` mencentang entri "Hapus semua data" di Tahap 5 (`PLAN.md`).


## 2026-10-14: Tahap 5: Data contoh (dummy)
- Ditambah: Tombol "Isi Data Contoh" di Pengaturan untuk menghasilkan 40 transaksi acak (pemasukan, pengeluaran, dan transfer) dalam rentang 2 bulan terakhir yang tersebar di dompet dan kategori yang ada, masing-masing dengan penanda `dummy: true` (`index.html`, `js/app.js`).
- Ditambah: Tombol "Hapus Data Contoh" di Pengaturan yang membuang seluruh transaksi bertanda dummy secara langsung dari penyimpanan lokal tanpa soft delete, menjaga data asli tetap utuh (`index.html`, `js/app.js`).
- Ditambah: Dialog konfirmasi web UI (`confirm-dialog`) berbahasa Indonesia untuk konfirmasi pengisian dan penghapusan data contoh (`js/app.js`).
- Diubah: Ekspor CSV dan ekspor JSON mengecualikan transaksi bertanda dummy sehingga data cadangan hanya memuat data asli (`js/app.js`).
- Diubah: Logika sinkronisasi Drive mengabaikan transaksi dummy agar tidak terunggah ke Drive, dan proses penarikan atau penggabungan dari Drive mempertahankan data dummy lokal tanpa menimpanya (`js/sync.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v34` (`sw.js`).
- Diubah: `PLAN.md` menambahkan dua entri baru di Tahap 5 dan mencentang entri "Data contoh (dummy)" (`PLAN.md`).

## 2026-10-14: Tahap 5: Desain ulang v2.5
- Diperbaiki: Seluruh dialog bawaan browser (`alert` dan `confirm`) yang memicu teks/tombol bahasa Inggris diganti dengan sistem notifikasi toast (`showNotification`) dan dialog konfirmasi kustom terintegrasi (`confirm-dialog`) berbahasa Indonesia (`js/app.js`).
- Diperbaiki: Tombol aksi transaksi dan judul modal diseragamkan ke bahasa Indonesia ("Ubah transaksi", "Ubah Transaksi", "Ubah Transfer") (`js/app.js`).
- Diperbaiki: Menghapus seluruh warna hardcoded di luar variabel CSS tema pada navigasi desktop, tombol sinkron header, dan tombol utama/bahaya, serta menambahkan token `--btn-danger-text` (`css/style.css`).
- Diubah: Versi service worker dinaikkan ke `mizan-v33` (`sw.js`).
- Diubah: `PLAN.md` mencentang sub-item "v2.5" dan item induk "Desain ulang v2 mengikuti DESIGN.md" (`PLAN.md`).

## 2026-10-14: Tahap 5: Desain ulang v2.4
- Diubah: Halaman Statistik didesain ulang dengan pemilih bulan kustom berlabel teks Indonesia ("Okt 2026") dan tombol navigasi panah kiri/kanan outline konsisten menggantikan input bulan bawaan browser (`index.html`, `js/app.js`, `css/style.css`).
- Diubah: Grafik bulanan dan diagram donut per kategori menggunakan tombol geser segmen (Pengeluaran / Pemasukan) dengan visual yang selaras dengan formulir transaksi tanpa bayangan dekoratif (`index.html`, `css/style.css`).
- Diubah: Diagram donut dan grafik bulanan hanya menggunakan palet token DESIGN.md (pemasukan hijau accent, pengeluaran merah #FF453A, transfer biru #4A9EFF, warning amber #F5A623, border, surface-raised, text-secondary) disertai rincian label teks, persentase, dan angka uang tabular-nums agar informasi tidak mengandalkan warna semata (`js/app.js`, `css/style.css`).
- Diubah: Keadaan kosong pada grafik bulanan dan diagram donut diseragamkan menjadi satu kalimat singkat dan satu tombol aksi ("Tambah Transaksi") (`js/app.js`, `css/style.css`).
- Diubah: Halaman Pengaturan ditata ulang menjadi kartu per kelompok (Tampilan, Format Jam, Dompet, Kategori Transaksi, Budget Pengeluaran Bulanan, Data dan Akun) tanpa kartu di dalam kartu, dipisahkan garis tipis atau spasi bersih di dalam kartu, serta tombol aksi konsisten (tombol utama/simpan berlatar accent, tombol hapus berbahaya memakai warna pengeluaran berlabel teks jelas) (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Halaman Kebijakan Privasi (`privasi.html`) dan Syarat dan Ketentuan (`syarat.html`) diselaraskan tampilannya dengan token warna DESIGN.md, tipografi, lebar maksimal 640px di tengah, tombol kembali outline SVG, dan dukungan tema terang/gelap tanpa mengubah isi teks aslinya (`privasi.html`, `syarat.html`, `css/style.css`).
- Diubah: Versi service worker dinaikkan ke `mizan-v32` (`sw.js`).
- Diubah: `PLAN.md` mencentang sub-item "v2.4" di bawah "Desain ulang v2 mengikuti DESIGN.md" (`PLAN.md`).

## 2026-10-14: Tahap 5: Desain ulang v2.3
- Diubah: Halaman Riwayat didesain ulang dengan pengelompokan transaksi per tanggal ("Hari ini", "Kemarin", "12 Okt", dst.) disertai total saldo harian (dengan tanda +, −, atau Rp 0) pada header grup (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Tiap baris transaksi menampilkan ikon kategori garis (outline) konsisten tanpa emoji, nama kategori dan catatan, jumlah rata kanan dengan tanda (+ untuk pemasukan hijau accent, − untuk pengeluaran merah, dan transfer biru tanpa tanda dengan label "Transfer"), serta dompet dan waktu pada baris kecil 12px text-secondary, dengan angka uang tabular-nums (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Keadaan kosong (empty state) pada halaman Riwayat diseragamkan menjadi satu kalimat singkat dan satu tombol aksi ("Tambah Transaksi") (`js/app.js`, `css/style.css`).
- Diubah: Formulir tambah/edit transaksi didesain ulang menjadi bottom sheet di HP dan dialog tengah di PC dengan radius 8px, tanpa bayangan, dan tanpa kartu bersarang (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Urutan komponen formulir disusun ulang sesuai DESIGN.md: jumlah (besar, keypad angka inputmode="numeric") → tipe (tombol geser segmen: Pengeluaran / Pemasukan / Transfer) → kategori (chip) → dompet → tanggal dan waktu → catatan → tombol Simpan selebar form dengan warna accent (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Pemilih tanggal dan waktu menggunakan antarmuka kustom berbahasa Indonesia (dropdown hari, nama bulan Indonesia, tahun, serta pemilih jam dan menit 24/12 jam) menggantikan widget native browser yang berbahasa Inggris, dengan format penyimpanan data (YYYY-MM-DD, HH:mm 24 jam, angka numerik) dan aturan validasi waktu wajib diisi tetap dipertahankan (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v31` (`sw.js`).
- Diubah: `PLAN.md` mencentang sub-item "v2.3" di bawah "Desain ulang v2 mengikuti DESIGN.md" (`PLAN.md`).

## 2026-10-14: Tahap 5: Desain ulang v2.2
- Diubah: Halaman Beranda didesain ulang dengan urutan Total Saldo hero di tengah (32px, bold, tabular-nums) diikuti ringkasan pemasukan (+, hijau accent) dan pengeluaran (−, merah) dua kolom kecil (18px, semibold, tabular-nums) tanpa kartu bersarang (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Saldo per dompet dipisahkan menjadi baris kartu geser horizontal ke samping dengan nama dompet (12px abu-abu), saldo tabular-nums, dan dompet bersaldo 0 ditampilkan redup (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Pemilih bulan budget menggunakan navigasi panah kiri/kanan kustom dan label teks Indonesia (contoh "Okt 2026"), sepenuhnya menghapus input bulan native browser (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Budget ringkas per kategori menampilkan nama kategori di kiri, "Rp terpakai dari Rp budget" di kanan, bar progres tipis 6px, serta persentase dan status dinamis (hijau aman, amber >= 80% dengan teks sisa, merah >= 100% dengan teks "Lewat Rp ...") tanpa kartu di dalam kartu (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Daftar transaksi terakhir pada Beranda ditata sebagai baris daftar bergaris pemisah tipis tanpa kartu di dalam kartu (`index.html`, `css/style.css`).
- Diubah: Tanda minus transaksi dan pengeluaran diseragamkan memakai unicode minus ("−") U+2212 (`js/app.js`).
- Diubah: Keadaan kosong (empty state) pada dompet, budget, dan transaksi terakhir diseragamkan menjadi satu kalimat singkat dan satu tombol aksi (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v30` (`sw.js`).
- Diubah: `PLAN.md` mencentang sub-item "v2.2" di bawah "Desain ulang v2 mengikuti DESIGN.md" (`PLAN.md`).

## 2026-10-13: Tahap 5: Desain ulang v2.1
- Ditambah: Ikon status sinkron kecil di header dengan popover penjelasan teks yang hanya muncul saat disentuh atau diklik (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Header aplikasi didesain ulang menjadi tipis (56px) dengan nama app "Mizan Okane" (18px) satu baris di kiri dan ikon status sinkron di kanan (`index.html`, `css/style.css`).
- Diubah: Navigasi utama tetap 4 item (Beranda, Riwayat, Statistik, Pengaturan) menggunakan bar bawah di HP (ikon outline + label) dan tab di atas di PC dengan target sentuh ergonomis minimal 44x44px dan indikator fokus `:focus-visible` jelas (`index.html`, `css/style.css`).
- Diubah: Status sinkronisasi di header memakai variasi warna dan ikon garis: abu-abu saat belum terhubung, centang accent saat tersinkron, dan peringatan amber saat gagal atau butuh perhatian (`js/app.js`, `css/style.css`).
- Diubah: Menghapus banner sinkronisasi menetap pada halaman Beranda (`index.html`, `css/style.css`).
- Diubah: Tombol tambah transaksi melayang (FAB) diposisikan di kanan bawah di atas bar bawah tanpa bayangan dekoratif sesuai panduan `DESIGN.md` (`css/style.css`).
- Diubah: Tata letak menghormati area aman (safe area) Android pada status bar dan gesture bar (`css/style.css`).
- Diubah: Versi service worker dinaikkan ke `mizan-v29` (`sw.js`).
- Diubah: `PLAN.md` mencentang sub-item "v2.1" di bawah "Desain ulang v2 mengikuti DESIGN.md" (`PLAN.md`).

## 2026-10-13: Tahap 5: Desain ulang v2.0
- Ditambah: Menaruh `DESIGN.md` sebagai panduan arah desain aplikasi di root repository (`DESIGN.md`).
- Diubah: Menerapkan token warna palet gelap utama (`#0F1117`, `#181A24`, `#1F2232`, `#2A2D3A`, `#E4E4E8`, `#9194A1`, `#2ECC8F`) dan palet terang opsi (`#F5F5F7`, `#FFFFFF`, `#F0F0F2`, `#D8D8DC`, `#1A1A1E`, `#6B6D78`, `#24A574`) serta warna semantik uang (pemasukan hijau accent, pengeluaran merah `#FF453A`, transfer biru `#4A9EFF`, budget bar) melalui variabel CSS (`css/style.css`).
- Diubah: Menerapkan tipografi `DESIGN.md` (Total Saldo 32px bold, ringkasan 18px semibold, judul kartu 16px semibold, isi 14px, label kecil 12px, judul app di header 18px satu baris, dan angka uang tabular-nums) serta radius 8px untuk kartu/dialog dan tanpa bayangan (`css/style.css`).
- Diubah: Warna tema `theme-color` diselaraskan ke `#0F1117` pada mode gelap dan `#F5F5F7` pada mode terang di seluruh dokumen web dan handler tema (`index.html`, `privasi.html`, `syarat.html`, `js/app.js`).
- Diubah: Versi service worker dinaikkan ke `mizan-v28` untuk memastikan pembaruan cache aset di PWA dan APK, serta menyinkronkan aset web ke Capacitor Android (`sw.js`).
- Diubah: `PLAN.md` mencentang sub-item "v2.0" di bawah "Desain ulang v2 mengikuti DESIGN.md" (`PLAN.md`).

## 2026-10-13: Tahap 5: Desain ulang tampilan dengan skill anti-slop
- Diubah: Desain ulang menyeluruh antarmuka web sesuai prinsip anti-slop (dials ENERGY 1 / RHYTHM 2 / MOTION 1) dengan palet finansial tenang (hijau zamrud/pinus `#1b6348` di mode terang dan `#34d399` di mode gelap, latar batu pasir hangat `#f7f6f2` dan slate obsidian `#111417`, tanpa gradien ungu-biru generik atau glow neon) (`css/style.css`, `index.html`, `privasi.html`, `syarat.html`).
- Diubah: Tipografi diperjelas dengan angka tabular (`tabular-nums`) pada saldo, riwayat transaksi, budget, dan statistik agar angka keuangan tersusun rapi secara vertikal (`css/style.css`).
- Diubah: Spasi, ritme layout, dan target sentuh interaktif ditingkatkan ke standar ergonomis minimum 44x44px pada seluruh tombol, tab navigasi, kontrol filter, dan formulir di HP maupun PC (`css/style.css`).
- Diubah: Indikator fokus keyboard (`:focus-visible`) diseragamkan dengan kontras tinggi untuk seluruh kontrol interaktif tanpa menghilangkan outline aksesibilitas (`css/style.css`).
- Diubah: Warna meta `theme-color` diselaraskan ke palet baru di mode terang (`#1b6348`) dan mode gelap (`#111417`) pada seluruh halaman (`index.html`, `privasi.html`, `syarat.html`).
- Diubah: Versi service worker dinaikkan ke `mizan-v27` untuk memperbarui cache aset PWA secara offline (`sw.js`).
- Diubah: `PLAN.md` menambahkan dan mencentang item "Desain ulang tampilan dengan skill anti-slop (hanya tampilan: warna, tipografi, spasi, layout, komponen; tidak mengubah logika, data, atau fitur)" pada Tahap 5 (`PLAN.md`).

## 2026-10-12: Tahap 5: Perbaikan APK: ekspor CSV dan ekspor JSON berfungsi di APK
- Ditambah: Instalasi dan konfigurasi plugin Capacitor Filesystem (`@capacitor/filesystem`) dan Share (`@capacitor/share`) untuk menangani penyimpanan berkas dan berbagi berkas native di Android APK (`package.json`, `package-lock.json`, `android/capacitor.settings.gradle`, `android/app/capacitor.build.gradle`).
- Ditambah: Konfigurasi jalur penyimpanan berkas FileProvider di Android untuk mendukung pembagian berkas dari cache dan files internal (`android/app/src/main/res/xml/file_paths.xml`).
- Ditambah: Komponen notifikasi toast UI (`#toast-notification`) yang responsif untuk HP dan PC dengan dukungan tema terang/gelap serta varian status (sukses, error, peringatan) untuk memberi umpan balik visual langsung kepada pengguna (`index.html`, `css/style.css`, `js/app.js`).
- Diperbaiki: Ekspor JSON dan CSV di APK kini menyimpan berkas ke cache perangkat via `@capacitor/filesystem` lalu membuka share sheet Android via `@capacitor/share` sehingga berkas dapat langsung disimpan ke perangkat atau dibagikan ke aplikasi lain, mengatasi masalah tombol ekspor tidak bereaksi sama sekali di Android WebView (`js/app.js`).
- Diubah: Versi web/PWA tetap mempertahankan alur unduh berkas Blob yang ada, format struktur JSON dan nama kolom header CSV tetap konsisten tanpa perubahan, serta ekspor tetap berfungsi penuh tanpa memerlukan login Google (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v26` untuk memperbarui cache aset PWA secara offline (`sw.js`).
- Diubah: `PLAN.md` mencentang item "Perbaikan APK: ekspor CSV dan ekspor JSON berfungsi di APK" pada Tahap 5 (`PLAN.md`).

## 2026-10-12: Tahap 5: Perbaikan APK: tombol back Android
- Ditambah: Penanganan tombol back Android native via AndroidX `OnBackPressedCallback` pada `MainActivity.java` yang mengarahkan navigasi mundur WebView dan menutup aplikasi secara aman (`finish()`) hanya saat berada di halaman utama (`android/app/src/main/java/com/mizoar/mizanokane/MainActivity.java`).
- Ditambah: Pelacakan riwayat navigasi tab (`tabHistory`) dan fungsi handler global `window.handleAndroidBack` di `js/app.js` untuk menutup dialog konfirmasi hapus atau form transaksi layar sendiri saat tombol back ditekan, serta kembali ke tab sebelumnya hingga ke Beranda (`js/app.js`).
- Diperbaiki: Tautan kembali pada halaman Kebijakan Privasi dan Syarat & Ketentuan yang sebelumnya mengarah ke `index.html` (halaman depan) diperbaiki menjadi `index.html#pengaturan` agar kembali ke tab Pengaturan (`privasi.html`, `syarat.html`).
- Diubah: `sw.js` naik ke versi `mizan-v25` untuk memperbarui cache aset PWA secara offline (`sw.js`).
- Diubah: `PLAN.md` mencentang item "Perbaikan APK: tombol back Android" pada Tahap 5 (`PLAN.md`).

## 2026-10-12: Tahap 5: APK Item B: bungkus jadi APK dengan Capacitor
- Ditambah: Konfigurasi Capacitor untuk membungkus web app menjadi aplikasi Android native (`capacitor.config.json`, `package.json`, `package-lock.json`).
- Ditambah: Proyek Android native Capacitor lengkap dengan package ID `com.mizoar.mizanokane`, nama aplikasi "Mizan Okane", manifest, Gradle wrapper, serta resource ikon dan splash screen (`android/`).
- Ditambah: Skrip `scripts/copy-www.js` dan npm script `build:www` untuk menyalin aset web (`index.html`, `privasi.html`, `syarat.html`, `manifest.webmanifest`, `sw.js`, folder `css`, `icons`, `js`) ke dalam folder `www` sebelum sinkronisasi Capacitor (`scripts/copy-www.js`, `package.json`).
- Diubah: Menambahkan `www/` ke daftar abaikan Git agar folder artefak build web tidak masuk ke repository (`.gitignore`).
- Diubah: `PLAN.md` mencentang item "APK Item B: bungkus jadi APK dengan Capacitor" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Mode gelap
- Ditambah: Bagian pengaturan "Tampilan" pada halaman Pengaturan dengan tiga pilihan tema (Ikuti sistem, Terang, Gelap) yang disimpan lokal di perangkat tanpa sinkron ke Drive (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Dukungan mode gelap menyeluruh berbasis variabel CSS untuk semua komponen aplikasi: bilah navigasi desktop & mobile, kartu ringkasan saldo, formulir tambah/edit, daftar riwayat transaksi, dialog konfirmasi hapus, pengaturan dompet/kategori/budget, serta warna garis, label, sumbu pada grafik bulanan dan diagram donut per kategori (`css/style.css`, `js/app.js`).
- Ditambah: Deteksi preferensi tema perangkat otomatis (`prefers-color-scheme`) saat opsi "Ikuti sistem" aktif yang langsung menyesuaikan tema tanpa reload halaman (`js/app.js`, `privasi.html`, `syarat.html`).
- Ditambah: Pencegahan kilatan tema terang (FOUC) lewat skrip inisialisasi di `<head>` sebelum CSS dimuat dan pembaruan dinamis meta `theme-color` agar bar status HP ikut menyesuaikan tema aktif (`index.html`, `privasi.html`, `syarat.html`).
- Ditambah: Dukungan mode gelap yang serasi pada halaman kebijakan privasi dan syarat & ketentuan yang otomatis mengikuti preferensi tema (`privasi.html`, `syarat.html`, `css/style.css`).
- Diubah: `sw.js` naik ke versi `mizan-v24` untuk memperbarui cache aset PWA secara offline (`sw.js`).
- Diubah: `PLAN.md` mencentang fitur "Mode gelap (dark mode) dengan pilihan Ikuti sistem / Terang / Gelap di Pengaturan" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Perbaikan UI/UX multi halaman
- Ditambah: Struktur antarmuka multi halaman dengan 4 tab utama (Beranda, Riwayat, Statistik, Pengaturan) yang responsif: bilah navigasi bawah (bottom navigation bar) tetap di layar ponsel (HP) dan bilah navigasi atas (top bar) pada layar komputer (PC) (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Halaman Beranda memuat total saldo, pemasukan, pengeluaran, saldo per dompet, ringkasan budget per kategori, 5 transaksi terakhir dengan tombol pintas "Lihat Semua", dan satu baris status sinkronisasi ringkas (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Halaman Riwayat memuat filter dompet dan daftar transaksi lengkap yang tetap mendukung pengeditan dan penghapusan transaksi (`index.html`, `js/app.js`).
- Ditambah: Halaman Statistik memuat grafik pengeluaran/pemasukan bulanan dan diagram donut per kategori (`index.html`, `css/style.css`).
- Ditambah: Halaman Pengaturan memuat pengaturan dompet, pengaturan kategori & budget, pengaturan format jam (24 jam / 12 jam), serta bagian terpadu "Data dan Akun" berisi tombol Login Google, Sinkron, Logout, Ekspor JSON, Impor JSON, Ekspor CSV, Install Aplikasi, dan tautan ke Kebijakan Privasi serta Syarat dan Ketentuan (`index.html`, `css/style.css`, `js/app.js`).
- Ditambah: Tombol mengambang (Floating Action Button / FAB) '+' untuk membuka formulir transaksi di layar sendiri, yang dipakai bersama untuk tambah dan edit transaksi tanpa mengubah validasi waktu wajib dan format ribuan (`index.html`, `css/style.css`, `js/app.js`).
- Diubah: Header lama yang padat tombol digantikan oleh header aplikasi bersih dan navigasi modern (`index.html`, `css/style.css`).
- Diubah: `sw.js` naik ke versi `mizan-v23` untuk memperbarui cache aset PWA secara offline (`sw.js`).
- Diubah: `PLAN.md` mencentang item "Perbaikan UI/UX multi halaman" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Halaman syarat dan ketentuan
- Ditambah: Halaman statis syarat dan ketentuan berbahasa Indonesia (`syarat.html`) dengan tampilan responsif untuk HP dan PC yang memuat ketentuan aplikasi pencatat keuangan pribadi gratis tanpa jaminan (as is), penegasan bukan layanan/nasihat keuangan dan tanggung jawab pengguna, penjelasan penyimpanan data lokal IndexedDB dan Google Drive appDataFolder tanpa server pihak ketiga, tanggung jawab pencadangan data mandiri via ekspor, login Google opsional, larangan penggunaan melanggar hukum, hak pembaruan syarat, tautan ke kebijakan privasi, kontak via GitHub Issues tanpa nama/email asli, dan tanggal pembaruan.
- Ditambah: Tautan ke halaman syarat dan ketentuan pada bagian footer aplikasi bersebelahan dengan tautan kebijakan privasi (`index.html`).
- Diubah: `sw.js` mendaftarkan `syarat.html` ke daftar aset cache dan naik ke versi `mizan-v22` agar halaman dapat diakses secara offline (`sw.js`).
- Diubah: `PLAN.md` menambahkan dan mencentang fitur "Halaman syarat dan ketentuan" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Halaman kebijakan privasi
- Ditambah: Halaman statis kebijakan privasi berbahasa Indonesia (`privasi.html`) yang memuat rincian penyimpanan data di IndexedDB & Google Drive `appDataFolder`, ketiadaan server pihak ketiga, rincian OAuth scope `drive.appdata`, ketiadaan iklan/pelacak/analitik, panduan hapus data & cabut izin akun Google, klausul kepatuhan Google API Services User Data Policy, tautan kontak via GitHub Issues, dan tanggal pembaruan.
- Ditambah: Tautan ke halaman kebijakan privasi pada bagian footer aplikasi (`index.html`).
- Ditambah: Styling halaman kebijakan privasi dan footer aplikasi yang responsif untuk tampilan HP dan PC (`css/style.css`).
- Diubah: `sw.js` mendaftarkan `privasi.html` ke daftar aset cache dan naik ke versi `mizan-v21` agar halaman dapat diakses secara offline (`sw.js`).
- Diubah: `PLAN.md` mencentang fitur "Halaman kebijakan privasi" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Mode tanpa login
- Ditambah: Pengujian otomatis komprehensif mode tanpa login untuk memastikan seluruh alur fitur berjalan normal tanpa akun Google (tambah dompet, tambah kategori pengeluaran dan pemasukan, tambah transaksi, transfer antar dompet, edit transaksi, hapus transaksi soft delete dengan modal konfirmasi, grafik batang bulanan, diagram donut kategori, ekspor CSV, ekspor JSON, dan impor JSON).
- Dipastikan: Status sinkronisasi netral ("Belum login. Data hanya tersimpan di perangkat ini.") tanpa pesan error dan tanpa runtime exception.
- Diubah: `PLAN.md` mencentang fitur "Mode tanpa login" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Impor JSON
- Ditambah: Tombol "Impor JSON" pada bilah sinkronisasi dan elemen input berkas untuk memulihkan data dari file cadangan JSON (`index.html`, `js/app.js`).
- Ditambah: Logika penggabungan data cadangan dengan aturan konflik waktu ubah (`updatedAt`) terbaru menang untuk transaksi, kategori, budget, dan daftar dompet (`js/app.js`).
- Ditambah: Dukungan penuh kompatibilitas transaksi lama tanpa `walletId` yang otomatis aman diimpor dan dipetakan ke Dompet Utama tanpa merusak data (`js/app.js`).
- Ditambah: Validasi struktur data cadangan, sanitasi transaksi, serta notifikasi hasil impor transaksi baru dan transaksi yang diperbarui (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v20` untuk memperbarui cache aset (`sw.js`).
- Diubah: `PLAN.md` mencentang fitur "Impor JSON" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Diagram donut per kategori
- Ditambah: Bagian antarmuka diagram donut per kategori dengan navigasi bulan (tombol bulan sebelumnya/berikutnya dan input bulan) serta toggle pilihan jenis transaksi Pengeluaran atau Pemasukan (`index.html`).
- Ditambah: Visualisasi diagram donut SVG berbasis proporsi kategori untuk bulan yang dipilih dengan lingkaran donut interaktif, label total/kategori di tengah, dan daftar rincian kategori beserta persentase dan nominal uang (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Interaksi klik, hover, dan navigasi keyboard (Tab, Enter, Spasi, Escape) pada segmen donut dan daftar rincian untuk menyorot kategori dan menampilkan detail transaksi (`js/app.js`, `css/style.css`).
- Diubah: Pemfilteran transaksi diagram donut secara ketat mengecualikan transfer antar dompet, transaksi terhapus, transaksi bernilai nol, serta membatasi hanya pada bulan dan jenis transaksi yang dipilih (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v19` untuk memperbarui cache aset (`sw.js`).
- Diubah: `PLAN.md` mencentang fitur diagram donut per kategori untuk bulan yang dipilih pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Angka uang pakai titik pemisah ribuan
- Ditambah: Fungsi pemformat bersama `formatNumber`, `parseAmount`, dan `formatCurrency` untuk menampilkan angka dan nilai uang dengan pemisah ribuan titik sesuai format Indonesia (`js/app.js`).
- Ditambah: Penanganan otomatis pemisah ribuan pada input jumlah uang di form tambah, form edit, form transfer antar dompet, dan form budget (`index.html`, `js/app.js`).
- Ditambah: Penjagaan posisi kursor saat mengetik atau mengedit angka di tengah dan dukungan tempel (paste) nilai uang berformat titik atau dengan simbol lain (`js/app.js`).
- Diubah: Field input uang menggunakan `type="text"` dengan `inputmode="numeric"` agar titik ribuan dapat tampil saat mengetik (`index.html`, `js/app.js`).
- Diubah: Form edit transaksi dan pilihan budget menampilkan jumlah lama yang telah diformat dengan titik ribuan (`js/app.js`).
- Diubah: Tampilan seluruh nominal uang (daftar transaksi, total saldo, saldo per dompet, budget dan sisa budget, label dan tooltip grafik) konsisten menggunakan titik pemisah ribuan (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v18` untuk memperbarui cache aset (`sw.js`).
- Diubah: `PLAN.md` mencatat dan mencentang perbaikan angka uang pakai titik pemisah ribuan pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Perbaikan field Waktu wajib diisi
- Ditambah: Elemen pesan error "Waktu wajib diisi" di bawah field Waktu pada formulir transaksi (`index.html`).
- Ditambah: Kelas styling `.form-error-message` untuk menampilkan pesan validasi form dengan warna teks bahaya (`css/style.css`).
- Ditambah: Validasi field Waktu wajib diisi saat simpan transaksi pada form tambah dan form edit (mode 24 jam dan 12 jam) yang menampilkan pesan error inline tanpa alert dan tanpa popup bawaan browser (`js/app.js`).
- Diubah: Pesan error waktu disembunyikan secara otomatis saat formulir di-reset, dibatalkan, atau saat nilai waktu diisi (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v17` untuk memperbarui cache aset (`sw.js`).
- Diubah: `PLAN.md` mencatat dan mencentang perbaikan field Waktu wajib diisi pada Tahap 5 (`PLAN.md`).
## 2026-10-11: Tahap 5: Perbaikan hapus tombol Kosongkan di field Waktu
- Dihapus: Tombol "Kosongkan" di sebelah label "Waktu" pada formulir transaksi beserta elemen pembungkus `.time-label-wrapper` (`index.html`).
- Dihapus: Aturan CSS `.time-label-wrapper` dan `.btn-clear-time` (`css/style.css`).
- Dihapus: Referensi elemen DOM dan event listener untuk `btnClearTime` (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v16` untuk memperbarui cache aset (`sw.js`).
- Diubah: `PLAN.md` mencatat dan mencentang perbaikan hapus tombol "Kosongkan" di field Waktu pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Grafik pemasukan bulanan
- Ditambah: Toggle "Pengeluaran / Pemasukan" pada header grafik bulanan untuk beralih antara grafik pengeluaran dan grafik pemasukan (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Dukungan grafik pemasukan bulanan yang menampilkan total pemasukan per bulan, warna batang hijau, rincian tooltip bulan terpilih, serta perhitungan rata-rata pemasukan per bulan (`js/app.js`, `css/style.css`).
- Diubah: Pemfilteran transaksi pada grafik bulanan mengecualikan transaksi transfer antar dompet dan transaksi yang dihapus (soft delete) baik pada mode pengeluaran maupun pemasukan (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v15` untuk memperbarui cache aset (`sw.js`).
- Diubah: `PLAN.md` mencentang fitur "Grafik pemasukan bulanan (bisa dipilih lewat toggle Pengeluaran / Pemasukan, transfer antar dompet tidak dihitung)" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Pilihan format jam 12 jam (AM/PM) atau 24 jam
- Ditambah: Pengaturan format jam pada bagian pengaturan dengan pilihan 24 jam dan 12 jam (AM/PM), default 24 jam, disimpan per perangkat di store `meta` IndexedDB dan tidak ikut sinkron (`index.html`, `js/db.js`, `js/app.js`, `css/style.css`).
- Ditambah: Antarmuka input waktu mode 12 jam pada formulir transaksi menggunakan kombinasi pilihan jam (01-12), menit (00-59), dan AM/PM, serta tombol "Kosongkan" untuk mengosongkan jam pada kedua mode (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Pengisian otomatis jam sekarang serta pengisian saat edit/kosongkan transaksi yang mendukung mode 24 jam dan 12 jam (`js/app.js`).
- Diubah: Tampilan jam pada riwayat transaksi mengikuti format yang dipilih (24 jam atau 12 jam AM/PM) secara realtime saat pengaturan diubah tanpa perlu memuat ulang halaman (`js/app.js`).
- Diubah: Data transaksi tetap disimpan dan diekspor ke CSV dalam format 24 jam "HH:mm" tanpa mengubah data transaksi lama (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v14` untuk memperbarui cache aset (`sw.js`).
- Diubah: `PLAN.md` mencentang fitur "Pilihan format jam 12 jam (AM/PM) atau 24 jam" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Ganti label Jam (opsional) jadi Waktu
- Diubah: Label input waktu pada formulir transaksi diganti dari "Jam (opsional)" menjadi "Waktu" (`index.html`).
- Diubah: `sw.js` naik ke versi `mizan-v13` untuk memperbarui cache aset (`sw.js`).
- Diubah: `PLAN.md` mencentang fitur "Ganti label 'Jam (opsional)' jadi 'Waktu'" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Transfer antar dompet
- Ditambah: Opsi tipe transaksi "Transfer" pada formulir transaksi untuk memindahkan uang dari satu dompet ke dompet lain (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Dropdown pilihan dompet tujuan ("Ke Dompet") yang otomatis aktif saat tipe transfer dipilih serta penyesuaian label dompet asal menjadi "Dari Dompet" (`index.html`, `js/app.js`).
- Ditambah: Validasi transfer membutuhkan minimal dua dompet aktif dan dompet tujuan tidak boleh sama dengan dompet asal (`js/app.js`).
- Ditambah: Perhitungan saldo per dompet yang berkurang pada dompet asal dan bertambah pada dompet tujuan tanpa mengubah total saldo, total pemasukan, dan total pengeluaran (`js/app.js`).
- Ditambah: Tampilan riwayat transaksi transfer dengan badge alur perpindahan dompet (`Dompet Asal → Dompet Tujuan`), warna jumlah transfer netral, dan tanda alur keluar/masuk saat memfilter dompet tertentu (`js/app.js`, `css/style.css`).
- Ditambah: Filter dompet pada riwayat transaksi yang menampilkan transfer jika dompet yang difilter terlibat baik sebagai dompet asal maupun tujuan (`js/app.js`).
- Ditambah: Dukungan soft delete pada transfer dengan konfirmasi dialog web (`<dialog>`) serta pembaruan otomatis saldo kedua dompet dan sinkronisasi ke Google Drive (`js/app.js`).
- Diubah: Perlindungan hapus dompet yang mencakup pemeriksaan transaksi transfer aktif baik sebagai dompet asal maupun dompet tujuan (`js/app.js`).
- Diubah: Format ekspor CSV untuk transfer menampilkan alur dompet `Dompet Asal -> Dompet Tujuan` pada kolom `dompet` dan kategori `Transfer` (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v12` untuk memperbarui cache aset (`sw.js`).
- Diubah: `PLAN.md` mencentang fitur "Transfer antar dompet" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Beberapa dompet
- Ditambah: Model data dompet dengan dompet default "Dompet Utama" serta fungsi `getWallets` dan `saveWallets` pada store `meta` IndexedDB (`js/db.js`).
- Ditambah: Bagian antarmuka pengaturan dompet di halaman web dengan daftar dompet aktif, formulir penambahan dompet, ubah nama dompet secara inline, dan tombol hapus dompet dengan proteksi transaksi aktif dan konfirmasi dialog web (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Field `walletId` pada formulir transaksi yang mengingat pilihan dompet terakhir yang dipakai serta rincian saldo per dompet pada ringkasan saldo (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Label nama dompet pada riwayat transaksi serta dropdown filter dompet ("Semua Dompet" atau dompet spesifik) (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Dukungan sinkronisasi daftar dompet ke Google Drive (`appDataFolder`) dengan aturan konflik waktu ubah (`updatedAt`) per dompet terbaru menang, serta toleransi terhadap file cadangan Drive lama (`js/sync.js`).
- Diubah: Kompatibilitas pembacaan transaksi lama tanpa `walletId` yang otomatis dianggap milik "Dompet Utama" tanpa menulis ulang data (`js/app.js`).
- Diubah: Ekspor CSV menyertakan kolom `dompet` setelah kolom `jam`, dan ekspor JSON ikut menyertakan daftar `wallets` (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v11` untuk memperbarui cache aset (`sw.js`).
- Diubah: `PLAN.md` mencentang fitur "Beberapa dompet" pada Tahap 5 (`PLAN.md`).

## 2026-10-11: Tahap 5: Budget per kategori
- Ditambah: Model data budget per kategori pengeluaran dengan fungsi `getBudgets` dan `saveBudgets` pada store `meta` IndexedDB (`js/db.js`).
- Ditambah: Bagian antarmuka budget per kategori di halaman utama dengan pemilihan bulan (default bulan berjalan, tombol bulan sebelumnya/berikutnya, dan input pemilih bulan), ringkasan total budget, kartu status per kategori (budget, terpakai, dan sisa), serta bar progres dengan tiga status warna (normal <80%, peringatan 80-100%, dan merah >100% dengan teks "Lebih Rp ...") (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Bagian pengaturan budget di bawah pengaturan kategori untuk mengisi, mengubah (inline edit), dan mengosongkan/menghapus budget per kategori pengeluaran dengan konfirmasi dialog web (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Dukungan sinkronisasi data budget ke Google Drive (`appDataFolder`) dengan aturan konflik waktu ubah (`updatedAt`) terbaru menang, serta toleransi terhadap file cadangan Drive lama yang belum memiliki data budget (`js/sync.js`).
- Diubah: Ekspor JSON menyertakan objek `budgets` bersama data transaksi dan kategori (`js/app.js`).
- Diubah: Pertahanan nama budget lama saat kategori pengeluaran dihapus atau diganti nama, tetap dapat dilihat dan dihapus di pengaturan budget (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v10` untuk memperbarui cache aset.
- Diubah: `PLAN.md` mencentang fitur "Budget per kategori" pada Tahap 5.

## 2026-10-11: Tahap 5: Pisah kategori pemasukan dan pengeluaran
- Ditambah: Dua daftar kategori default terpisah untuk pemasukan (Gaji, Bonus, Hadiah, Lainnya) dan pengeluaran (Makan, Transport, Hiburan, Belanja, Tagihan, Lainnya), serta fungsi `getCategories` dan `saveCategories` pada IndexedDB (`js/db.js`).
- Ditambah: Bagian antarmuka pengaturan kategori di halaman web dengan daftar kategori pemasukan dan pengeluaran, formulir penambahan kategori, ubah nama kategori secara inline, dan tombol hapus kategori (`index.html`, `js/app.js`, `css/style.css`).
- Ditambah: Integrasi kotak dialog konfirmasi web (`<dialog>`) yang sudah ada untuk konfirmasi hapus kategori tanpa `confirm()` bawaan browser (`js/app.js`).
- Ditambah: Dukungan sinkronisasi kategori ke Google Drive dengan aturan konflik waktu ubah (`updatedAt`) terbaru menang, sambil mempertahankan pembacaan file cadangan lama yang belum memiliki data kategori (`js/sync.js`).
- Diubah: Pilihan dropdown kategori pada formulir transaksi mengikuti tipe transaksi yang dipilih (berpindah otomatis saat tipe diganti), serta meminta pemilihan kategori yang sesuai saat mengedit transaksi lama berkategori di luar daftar tipenya tanpa mengubah riwayat transaksi lama (`index.html`, `js/app.js`, `css/style.css`).
- Diubah: Ekspor JSON menyertakan objek `categories` cadangan bersama daftar transaksi (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v9` untuk memperbarui cache aset.
- Diubah: `PLAN.md` mencentang fitur "Pisah kategori pemasukan dan pengeluaran" pada Tahap 5.

## 2026-10-11: Tahap 5: Grafik pengeluaran bulanan
- Ditambah: Bagian antarmuka grafik pengeluaran bulanan dengan judul, teks ringkasan rata-rata, rincian bulan aktif, dan area grafik (`index.html`).
- Ditambah: Visualisasi grafik batang SVG tanpa library luar untuk menampilkan total pengeluaran per bulan, responsif di HP dan PC dengan dukungan scroll horizontal jika banyak bulan (`js/app.js`, `css/style.css`).
- Ditambah: Agregasi data pengeluaran per bulan dari transaksi aktif (mengabaikan transaksi soft delete), perhitungan batas sumbu Y otomatis, dan tampilan pesan kosong saat belum ada pengeluaran (`js/app.js`).
- Ditambah: Interaksi klik, sentuh di HP, dan navigasi keyboard pada batang grafik untuk memeriksa rincian pengeluaran per bulan (`js/app.js`, `css/style.css`).
- Diubah: `sw.js` naik ke versi `mizan-v8` untuk memperbarui cache aset.
- Diubah: `PLAN.md` mencentang fitur "Grafik pengeluaran bulanan" pada Tahap 5.

## 2026-10-10: Tahap 5: Konfirmasi hapus transaksi lewat UI web
- Ditambah: Kotak dialog konfirmasi hapus transaksi di dalam halaman menggunakan elemen `<dialog>` dengan teks "Hapus transaksi ini?", tombol "Hapus", dan tombol "Batal" (`index.html`).
- Ditambah: Gaya untuk `.confirm-dialog` dan `.btn-danger` yang selaras dengan tema, responsif untuk HP dan PC, serta mudah diketuk pada layar kecil (`css/style.css`).
- Diperbaiki: Mengganti pemakaian `confirm()` bawaan browser pada hapus transaksi dengan kotak dialog konfirmasi web (`js/app.js`).
- Diubah: Penutupan kotak konfirmasi tanpa menghapus saat menekan tombol "Batal", menekan tombol Escape, atau mengetuk area di luar kotak (`js/app.js`).
- Diubah: Tombol "Hapus" pada dialog menjalankan logika soft delete yang sudah ada lalu menutup kotak (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v7` untuk memperbarui cache aset.
- Diubah: `PLAN.md` menambahkan dan mencentang perbaikan "konfirmasi hapus lewat UI web (ganti confirm bawaan browser)" pada Tahap 5.

## 2026-10-10: Tahap 5: Perbaikan login Google lebih mulus
- Ditambah: Penanda boolean `googleLoggedIn` pada store `meta` IndexedDB untuk mencatat perangkat yang pernah login Google tanpa menyimpan token (`js/db.js`, `js/sync.js`).
- Ditambah: Status netral "Ketuk di mana saja untuk lanjut sinkron" dan listener ketukan pertama pengguna saat aplikasi dibuka jika perangkat sudah terhubung tapi belum memiliki token di memori (`js/sync.js`, `js/app.js`).
- Ditambah: Pemanggilan `ensureToken` di dalam handler interaksi pengguna (`handleSubmit` dan `handleDelete`) untuk memperbarui token jika sudah habis masa berlakunya (`js/sync.js`, `js/app.js`).
- Diperbaiki: Mencegah popup Google terblokir dengan tidak lagi memanggil permintaan token otomatis saat aplikasi baru dibuka (`js/sync.js`).
- Diubah: Penghapusan penanda login di store `meta` saat pengguna menekan tombol Logout (`js/sync.js`, `js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v6` untuk memperbarui cache aset.
- Diubah: `PLAN.md` menambahkan dan mencentang fitur "Perbaikan: login Google lebih mulus (tanpa popup terblokir)" pada Tahap 5.

## 2026-10-10: Tahap 5: Jam:menit pada transaksi
- Ditambah: Field opsional `time` (format "HH:mm") pada data transaksi, input jam pada form transaksi (`index.html`, `js/app.js`), dan dukungan gaya input waktu (`css/style.css`).
- Ditambah: Pengisian otomatis input jam dengan waktu sekarang untuk transaksi baru, serta dukungan edit/kosongkan jam (`js/app.js`).
- Ditambah: Tampilan jam di samping tanggal pada daftar riwayat transaksi untuk transaksi yang memiliki jam (`js/app.js`).
- Diubah: Pengurutan riwayat transaksi dan ekspor CSV berdasarkan tanggal lalu jam terbaru (`js/app.js`).
- Diubah: Penambahan kolom `jam` setelah kolom `tanggal` pada ekspor CSV (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v5` untuk memperbarui cache aset.
- Diubah: `PLAN.md` mencentang fitur "Jam:menit pada transaksi" pada Tahap 5.

## 2026-10-10: Tahap 5: Revisi PLAN.md
- Diubah: `PLAN.md` pada bagian "Aturan kerja" (pengerjaan satu fitur per sesi untuk Tahap 5 dan aturan bahwa Tahap 5 baru dicentang setelah semua fiturnya tercentang), penambahan kotak centang pada tiap fitur Tahap 5 beserta opsi fitur jam:menit transaksi, dan penghapusan bagian "Catatan".

## 2026-10-10: Tahap 5: Ekspor CSV
- Ditambah: Tombol "Ekspor CSV" di samping tombol "Ekspor JSON" pada antarmuka sinkronisasi (`index.html`, `js/app.js`).
- Ditambah: Fitur ekspor data transaksi aktif (tanpa soft delete) ke file CSV berformat `mizan-okane-transaksi-YYYY-MM-DD.csv`, diurutkan dari tanggal terbaru, dengan kolom tanggal, tipe, kategori, jumlah, dan catatan (`js/app.js`).
- Ditambah: Escape karakter CSV (koma, tanda kutip, baris baru), penambahan UTF-8 BOM untuk kompatibilitas Excel, serta penanganan jika belum ada transaksi (`js/app.js`).
- Diubah: `sw.js` naik ke versi `mizan-v4` untuk memperbarui cache aset.
- Diubah: `PLAN.md` mencentang fitur Ekspor CSV pada Tahap 5.

## 2026-10-08: Tahap 4: Aturan konflik data
- Ditambah: Tombol "Ekspor JSON" di antarmuka sinkronisasi untuk backup manual (`index.html`, `js/app.js`).
- Diubah: Logika sinkronisasi di `js/sync.js` untuk menggabungkan data transaksi dari dua perangkat (lokal dan Drive) berdasarkan waktu ubah (`updatedAt`) yang terbaru, dan mencakup transaksi dengan tanda soft delete.
- Diubah: Menghapus UI dan logika resolusi konflik ("Pakai data Drive / Pakai data perangkat ini") dari `index.html`, `js/app.js`, dan `js/sync.js`.
- Diubah: `sw.js` naik ke versi `mizan-v3` untuk memperbarui cache.


## 2026-10-06: Tahap 3: Login Google dan sinkron Drive
- Ditambah: `js/sync.js`, login Google lewat Google Identity Services (token flow, tanpa client secret dan tanpa server). Access token hanya disimpan di memori.
- Ditambah: Simpan dan ambil satu file JSON (`mizan-okane-data.json`) di `appDataFolder` lewat Drive API v3.
- Ditambah: Sinkron otomatis saat app dibuka, saat kembali online, dan sekitar 1,5 detik setelah ada perubahan data.
- Ditambah: Bar sinkron di `index.html` (status, tombol Login Google, Sinkron, Logout) dan gayanya di `css/style.css`.
- Ditambah: Pilihan "Pakai data Drive" atau "Pakai data perangkat ini" kalau data di Drive dan di perangkat sama-sama berubah. Gabung per transaksi menunggu Tahap 4.
- Diubah: `js/db.js` naik ke versi 2 dengan store `meta` untuk status sinkron per perangkat (bukan token).
- Diubah: `js/app.js` menandai perubahan setelah simpan atau hapus, dan memulai sinkron setelah data lokal tampil.
- Diubah: `sw.js` cache `mizan-v2`, menyertakan `js/sync.js`, dan tidak lagi menangani request ke googleapis.com, accounts.google.com, request lintas origin, maupun request selain GET.

## 2026-10-06: Tahap 2: PWA
- Ditambah: Konfigurasi PWA (manifest.webmanifest, sw.js, ikon).
- Ditambah: Link manifest dan service worker di index.html dan js/app.js.
- Ditambah: Tombol "Install Aplikasi" dengan event beforeinstallprompt.

## 2026-10-06: Tahap 1: Lokal
- Diperbaiki: Syntax error pada file `js/app.js` terkait karakter escape di dalam template literal yang menyebabkan error gagal muat.
- Ditambah: Antarmuka dan logika aplikasi lokal (index.html, style.css, app.js).
- Ditambah: Penyimpanan data di perangkat menggunakan IndexedDB (db.js).
- Ditambah: Fitur CRUD transaksi sederhana dengan soft-delete.
- Ditambah: Format rupiah dan perhitungan saldo.

## 2026-10-06: Proyek dimulai
- Ditambah: `PLAN.md` dan `CHANGELOG.md`
