# Aturan untuk agent (baca ini dulu)

## Aturan kerja
- Di awal sesi, baca dev/RULES.md dan dev/PLAN.md. Lalu baca dev/PLANSPEC.md hanya di bagian item yang dikerjakan, jangan seluruh file.
- Spek fitur baru WAJIB ditulis di dev/PLANSPEC.md dengan judul sama seperti item di dev/PLAN.md. Di dev/PLAN.md hanya boleh ada satu baris checkbox plus judul singkat, tanpa kurung penjelasan dan tanpa sub-poin.
- Saat mengerjakan item, ikuti spek di dev/PLANSPEC.md. Kalau spek dan PLAN.md berbeda, spek yang berlaku.
- Kerjakan SATU tahap per sesi. Khusus Tahap 5, kerjakan SATU fitur per sesi. Jangan lompat ke tahap atau fitur lain.
- Jangan ubah file di luar tahap yang sedang dikerjakan.
- Dilarang menjalankan `npm run build`, `npx cap sync`, `npx cap copy`, atau perintah build apa pun. Folder www/ dan android/ hanya dibangun oleh pemilik. Dilarang `git add` pada kedua folder itu. Kalau keduanya muncul di `git status --short`, berhenti dan laporkan.
- Bagian "Keputusan terkunci" tidak boleh diubah tanpa izin pemilik.
- Jangan pernah menaruh data transaksi asli, token, kunci, atau file `.env` di repo. Repo ini publik, isinya hanya kode dan dokumen.
- Di akhir tahap atau fitur, centang tahap atau fitur itu di dev/PLAN.md dan tambahkan satu entri di paling atas `dev/CHANGELOG.md` sesuai format di file itu. Tahap 5 baru dicentang setelah semua fiturnya tercentang. Jangan ubah entri lama. Kalau tidak yakin tanggalnya, tulis "tidak tercatat", jangan menebak.
- Di awal sesi, baca hanya 5 entri paling atas dev/CHANGELOG.md, jangan seluruh file.
- Untuk tugas tampilan, baca dev/DESIGN.md dulu dan ikuti. Jangan menambah warna, font, bayangan, atau animasi di luar dev/DESIGN.md.

## Alur commit dan push
Sebelum mulai kerja, jalankan `git branch --show-current`. Hasilnya harus `main`. Kalau bukan, berhenti: jangan pindah branch, jangan buat branch, jangan commit, jangan push. Laporkan nama branch yang aktif ke pemilik. Kalau selama sesi muncul branch atau worktree baru yang dibuat otomatis oleh tool, berhenti dan laporkan juga, jangan push.

Setelah tahap selesai:
1. Jalankan `git status --short`. Kalau ada file yang muncul tapi bukan bagian dari tahap ini (selain dev/PLAN.md, dev/CHANGELOG.md, dev/PLANSPEC.md, dan dev/RULES.md), berhenti: jangan commit, jangan push, laporkan ke pemilik.
2. Tambahkan entri di dev/CHANGELOG.md. Cantumkan file yang diubah berdasarkan hasil `git status --short`.
3. Stage file dengan menyebut namanya satu per satu. Dilarang `git add -A` dan `git add .`
4. Commit dengan pesan lengkap: judul singkat dengan format "tahap N: nama singkat", isi penjelasan, lalu dua baris trailer di bawah, dipisah satu baris kosong dari isi pesan.

   Co-authored-by: Claude <noreply@anthropic.com>
   Co-authored-by: Gemini (Antigravity) <200291788+gemini-code-assist@users.noreply.github.com>
5. Jalankan `git push` ke branch yang sedang aktif. Dilarang force push, dilarang membuat atau menghapus branch, dilarang mengubah git config.
6. Kalau commit atau push gagal, jangan coba cara lain. Berhenti dan laporkan error-nya ke pemilik.
7. Di akhir, kabari file apa saja yang diubah dan hash commit-nya.
