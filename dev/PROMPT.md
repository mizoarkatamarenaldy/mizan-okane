# PROMPT.md: template prompt untuk Antigravity

Cara pakai: salin isi blok di bawah, ganti semua bagian [KURUNG SIKU], lalu kirim ke Antigravity. Satu sesi satu fitur.

---

Kerjakan item "[NAMA ITEM]". Baca dev/RULES.md dan 5 entri teratas dev/CHANGELOG.md dulu, ikuti semua aturannya.

Sebelum coding, lakukan dua hal ini saja:
1. Di dev/PLAN.md, tambahkan satu baris di paling bawah daftar [TAHAP, contoh: Tahap 5]: "  - [ ] [NAMA ITEM]". Tanpa penjelasan dan tanpa sub-poin.
2. Di akhir dev/PLANSPEC.md, tempel bagian di bawah ini persis apa adanya.

## [NAMA ITEM]

Tujuan: [satu atau dua kalimat: apa yang ingin dicapai dan kenapa].

Tempat dan tampilan:
- [Di halaman mana fitur ini muncul, berupa apa (tombol, kartu, dialog), teks apa yang tampil]
- [Hapus bagian ini kalau tidak ada perubahan tampilan]

Aturan:
- [Aturan perilaku 1]
- [Aturan perilaku 2]
- [Hal yang sengaja TIDAK dilakukan fitur ini]

Data:
- [Ada perubahan struktur data atau tidak. Pengaruh ke sinkron Drive, ekspor, impor. Kalau tidak ada, tulis: "Tidak ada perubahan struktur data, sinkron Drive, ekspor, atau impor."]

Cek manual:
- [Skenario uji 1: kondisi awal, aksi, hasil yang diharapkan]
- [Skenario uji 2]

Setelah itu, kerjakan item "[NAMA ITEM]" sesuai bagian spek tadi. Tampilan baru wajib mengikuti dev/DESIGN.md, jangan tambah warna, font, bayangan, atau animasi di luar itu.

Setelah selesai: centang item di dev/PLAN.md, tambah entri di paling atas dev/CHANGELOG.md, lalu ikuti alur commit dan push di RULES.md. Kabari file yang diubah dan hash commit-nya.

---

## Catatan untuk Claude (kamu), jangan ikut dikirim ke Antigravity
- Kalau fitur tidak mengubah tampilan, hapus bagian "Tempat dan tampilan" dan kalimat soal DESIGN.md, ganti dengan: "Tidak ada elemen tampilan baru."
- Kalau fitur butuh membaca bagian PLANSPEC.md lain (misalnya fitur terkait), tulis eksplisit: "Buka hanya bagian [NAMA] di dev/PLANSPEC.md."
- Kalau butuh mengubah sesuatu yang dilarang RULES.md (misalnya "Keputusan terkunci"), tambahkan klausa "Izin dari pemilik" yang menyebut persis apa yang boleh diubah.
- Jangan minta Antigravity menjalankan build. Folder mobile/capacitor/www/ dan mobile/capacitor/android/ dibangun manual oleh pemilik.