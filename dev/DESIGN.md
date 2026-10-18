# Mizan Okane: Design Direction

## Design Read
Aplikasi pencatat keuangan pribadi untuk HP (APK dan PWA) dan PC. Dibuka berkali-kali sehari untuk mencatat transaksi dengan cepat. Gaya minimalis, padat informasi, angka uang jadi bintang.
Dial: ENERGY 1 / RHYTHM 1 / MOTION 1

## Pemakai
Pemilik, dan orang lain yang memasang APK. Dipakai satu tangan di HP. Login Google tidak wajib. Semua label harus jelas untuk pengguna baru.

## Mood
Tenang, bersih, serius tapi tidak kaku.

## Tema
Gelap sebagai utama, terang sebagai opsi. Pilihan Ikuti sistem / Terang / Gelap di Pengaturan tetap ada.

### Palet gelap
| Token | Hex | Fungsi |
|---|---|---|
| bg | #0F1117 | Latar utama |
| surface | #181A24 | Kartu, dialog |
| surface-raised | #1F2232 | Elemen terangkat, chip |
| border | #2A2D3A | Garis pemisah |
| text | #E4E4E8 | Teks utama |
| text-secondary | #9194A1 | Teks pendukung |
| accent | #2ECC8F | Aksen tunggal (teal-green) |

### Palet terang
| Token | Hex | Fungsi |
|---|---|---|
| bg | #F5F5F7 | Latar utama |
| surface | #FFFFFF | Kartu, dialog |
| surface-raised | #F0F0F2 | Elemen terangkat, chip |
| border | #D8D8DC | Garis pemisah |
| text | #1A1A1E | Teks utama |
| text-secondary | #6B6D78 | Teks pendukung |
| accent | #24A574 | Aksen (lebih gelap untuk kontras) |

### Warna makna
Warna selalu ditemani tanda atau label teks, tidak pernah berdiri sendiri.
| Makna | Warna | Cara tampil |
|---|---|---|
| Pemasukan | accent | Teks hijau, selalu berawalan "+" |
| Pengeluaran | #FF453A | Teks merah, selalu berawalan "−" |
| Transfer | #4A9EFF | Teks biru, tanpa tanda +/−, ada label "Transfer" |
| Budget aman | accent | Bar hijau |
| Budget hampir habis (80% ke atas) | #F5A623 | Bar amber, teks sisa tetap tampil |
| Budget lewat (100% ke atas) | #FF453A | Bar merah, teks "Lewat Rp ..." |

Aturan pembeda: hijau sebagai aksen hanya berbentuk isi tombol atau tab aktif. Hijau sebagai pemasukan hanya berbentuk teks angka.

## Tipografi
System font stack: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif.
Semua angka uang memakai tabular-nums.
| Elemen | Ukuran |
|---|---|
| Total Saldo | 32px, bold |
| Angka ringkasan (pemasukan, pengeluaran) | 18px, semibold |
| Judul kartu | 16px, semibold |
| Isi | 14px |
| Label kecil, tanggal | 12px, text-secondary |
| Nama app di header | 18px, satu baris |

## Tata letak
- di HP satu kolom max-width 640px; di PC (lebar 1024 px ke atas) memakai tata letak dashboard sesuai spek "Revisi UI PC".
- Header tipis (sekitar 56px): nama app satu baris di kiri, ikon status sinkron di kanan.
- Navigasi: di HP berupa bar bawah (4 item: ikon + label), di PC berupa tab di atas.
- Tombol tambah transaksi mengambang (FAB) di kanan bawah, di atas bar bawah.
- Hormati safe area Android (status bar dan gesture bar).

## Komponen
- **Kartu:** warna surface, border 1px, radius 8px, tanpa bayangan. Dilarang kartu di dalam kartu; pemisah di dalam kartu pakai garis tipis atau spasi.
- **Beranda, urutan:** Total Saldo (hero, di tengah) → Pemasukan dan Pengeluaran bulan ini (dua kolom kecil) → Dompet → Budget ringkas → Transaksi terakhir.
- **Dompet:** baris yang bisa digeser ke samping. Tiap dompet: nama (12px, abu-abu) dan saldo. Dompet saldo 0 tampil lebih redup.
- **Pemilih bulan:** tombol panah kiri/kanan dan label teks Indonesia ("Okt 2026"). Dilarang memakai input bulan bawaan browser.
- **Budget per kategori:** nama kategori di kiri, "Rp terpakai dari Rp budget" di kanan, bar tipis 6px, persen. Warna bar mengikuti tabel Warna makna.
- **Riwayat:** dikelompokkan per tanggal ("Hari ini", "Kemarin", "12 Okt") dengan total harian di header grup. Tiap baris: ikon kategori (garis), nama kategori dan catatan, jumlah rata kanan dengan tanda, dompet dan waktu di baris kecil.
- **Animasi catatan:** teks catatan yang melebihi lebar bergeser pelan agar terbaca penuh; statis dan terpotong "..." bila kurangi animasi aktif.
- **Tombol tengah navigasi (Timbangan):** tombol di tengah bar navigasi dibuat lebih besar dan sedikit menonjol ke atas, memakai token warna yang sudah ada tanpa bayangan atau animasi baru.
- **Form tambah/edit:** di HP berupa bottom sheet, di PC berupa dialog tengah. Urutan: jumlah (besar, keypad angka) → tipe (tombol geser: Pengeluaran / Pemasukan / Transfer) → kategori (chip) → dompet → tanggal dan waktu → catatan. Tombol Simpan selebar form, warna accent.
- **Status sinkron:** ikon kecil di header. Abu-abu = belum terhubung, centang accent = tersinkron, peringatan amber = gagal. Penjelasan teks hanya muncul saat ikon disentuh. Dilarang banner besar yang menetap.
- **Toast:** tetap dipakai untuk sukses, gagal, peringatan.
- **Keadaan kosong:** satu kalimat singkat dan satu tombol aksi.
- **Ikon:** satu set ikon garis (outline) yang konsisten. Dilarang emoji sebagai ikon.

## Keputusan desain
| Keputusan | Alasan |
|---|---|
| Palet sama dengan Shugen Hub | Terbukti enak dipandang, satu keluarga visual |
| Warna hanya untuk makna uang dan status budget | Mata langsung tertuju ke angka yang penting |
| Total Saldo jadi hero, bukan pemasukan/pengeluaran | Itu angka yang paling sering dicari |
| Radius 8px kartu, 6px input | Lembut tanpa terlalu bulat |
| Tanpa bayangan, tanpa animasi dekoratif | MOTION 1, alat kerja cepat |
| Bar navigasi bawah di HP | Terjangkau jempol, terasa seperti aplikasi |
| System font | Memuat instan, native, mudah dibaca |
| Semua teks berbahasa Indonesia | Konsisten, tidak ada teks Inggris bawaan browser |

## Aksesibilitas
Target sentuh minimal 44x44px. Indikator fokus keyboard (:focus-visible) jelas. Kontras teks memenuhi standar di mode terang dan gelap.

## Hindari
- Gradasi ungu/biru
- Bayangan berlebihan pada kartu
- Emoji sebagai ikon
- Animasi dekoratif
- Glassmorphism
- Kartu bersarang (kotak di dalam kotak)
- Lebih dari satu warna aksen
- Teks atau widget bawaan browser yang tampil berbahasa Inggris