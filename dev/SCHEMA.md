# Dokumentasi Skema Data (dev/SCHEMA.md)

Dokumen ini mencatat format struktur database IndexedDB, format file cadangan JSON (ekspor/impor), format payload sinkronisasi Google Drive, aturan resolusi konflik, dan nomor versi format data aplikasi Mizan Okane.

---

## 1. Versi Format Data
- **Versi Database IndexedDB**: `2` (`DB_VERSION = 2`)
- **Versi Skema Cadangan & Sinkronisasi**: `2` (`version: 2`, `schemaVersion: 2`)
- **Nama Aplikasi**: `mizan-okane`

---

## 2. Struktur Database IndexedDB
- **Nama Database**: `mizan-okane-db` (`DB_NAME = 'mizan-okane-db'`)
- **Object Stores**:

### A. Store `transactions`
- **Kunci utama (keyPath)**: `id` (string)
- **Deskripsi**: Menyimpan seluruh rekaman transaksi lokal (aktif maupun soft-deleted).
- **Struktur Objek Transaksi**:
  | Field | Tipe | Keterangan |
  | :--- | :--- | :--- |
  | `id` | `string` | Pengenal unik transaksi |
  | `type` | `string` | Tipe transaksi: `'income'`, `'expense'`, `'transfer'`, atau `'penyesuaian'` |
  | `amount` | `number` | Nominal uang (maksimal 2 desimal, positif untuk transaksi biasa, tidak nol untuk penyesuaian) |
  | `date` | `string` | Tanggal transaksi dalam format `YYYY-MM-DD` |
  | `time` | `string` | Waktu transaksi dalam format `HH:mm` (opsional pada data versi lama) |
  | `category` | `string` | Kategori induk transaksi (untuk transfer bernilai `'Transfer'`, penyesuaian bernilai `'Equilibrium'`) |
  | `subcategory` | `string` | Sub kategori transaksi (opsional) |
  | `walletId` | `string` | ID dompet terkait transaksi (opsional pada data versi awal) |
  | `toWalletId` | `string` | ID dompet tujuan (khusus tipe `'transfer'`) |
  | `note` | `string` | Catatan transaksi (opsional) |
  | `createdAt` | `number` | Waktu pembuatan (timestamp Unix milidetik) |
  | `updatedAt` | `number` | Waktu perubahan terakhir (timestamp Unix milidetik) |
  | `deleted` | `boolean` | Penanda terhapus lunak (`true` jika dihapus, `false` jika aktif) |
  | `dummy` | `boolean` | Penanda transaksi contoh/demo (`true` jika demo; opsional, tidak disimpan di data riil) |

### B. Store `meta`
- **Kunci utama**: Out-of-line key (string kunci manual tanpa keyPath).
- **Daftar Kunci yang Digunakan**:
  | Kunci | Tipe Nilai | Keterangan |
  | :--- | :--- | :--- |
  | `'categories'` | `object` | Objek kategori riil (`income`, `expense`, `subcategories`, `updatedAt`) |
  | `'budgets'` | `object` | Objek anggaran riil (`items`, `subcategories`, `updatedAt`) |
  | `'wallets'` | `array` | Daftar objek dompet riil |
  | `'settings'` | `object` | Objek pengaturan riil (`theme`, `timeFormat`, `updatedAt`) |
  | `'timeFormat'` | `string` | Format jam aktif (`'12'` atau `'24'`) |
  | `'localRev'` | `number` | Nomor revisi lokal, bertambah setiap ada perubahan data lokal |
  | `'sync'` | `object` | Status sinkronisasi (`{ account, syncedRev, remoteVersion, lastSyncAt }`) |
  | `'demo_transactions'` | `array` | Salinan daftar transaksi saat mode contoh/demo aktif |
  | `'demo_categories'` | `object` | Salinan struktur kategori saat mode contoh/demo aktif |
  | `'demo_budgets'` | `object` | Salinan struktur anggaran saat mode contoh/demo aktif |
  | `'demo_wallets'` | `array` | Salinan daftar dompet saat mode contoh/demo aktif |

---

## 3. Format File Cadangan JSON (Ekspor / Impor)
- **Nama file ekspor bawaan**: `mizan-okane-backup-YYYY-MM-DD.json`
- **Struktur Root Objek**:
  ```json
  {
    "app": "mizan-okane",
    "version": 2,
    "schemaVersion": 2,
    "exportedAt": "2026-10-18T06:00:00.000Z",
    "transactions": [],
    "wallets": [],
    "categories": {},
    "budgets": {},
    "settings": {}
  }
  ```

### Rincian Field File Cadangan:
1. **`app`** (`string`): Tetap `"mizan-okane"`.
2. **`version`** (`number`): `2`.
3. **`schemaVersion`** (`number`): `2`.
4. **`exportedAt`** (`string`): Waktu ekspor dalam format ISO 8601 UTC.
5. **`transactions`** (`array` of `object`): Daftar transaksi riil non-dummy.
   - Field setiap transaksi sesuai rincian store `transactions` di atas (`id`, `type`, `amount`, `date`, `time`, `category`, `subcategory`, `walletId`, `toWalletId`, `note`, `createdAt`, `updatedAt`, `deleted`).
6. **`wallets`** (`array` of `object`):
   - `id` (`string`): Pengenal unik dompet (misalnya `"default"` untuk Dompet Utama).
   - `name` (`string`): Nama dompet.
   - `currency` (`string`): Kode mata uang (`"IDR"`, `"JPY"`, atau `"USD"`).
   - `order` (`number`): Urutan tampilan kartu dompet (0-indexed).
   - `createdAt` (`number`): Timestamp Unix milidetik pembuatan.
   - `updatedAt` (`number`): Timestamp Unix milidetik perubahan terakhir.
   - `deleted` (`boolean`): Penanda hapus lunak dompet.
7. **`categories`** (`object`):
   - `income` (`array` of `object`): Kategori induk pemasukan. Tiap elemen:
     - `name` (`string`): Nama kategori.
     - `order` (`number`): Urutan tampilan (0-indexed).
     - `icon` (`string`, opsional): ID ikon kustom (misal `"gaji"`, `"bonus"`).
   - `expense` (`array` of `object`): Kategori induk pengeluaran. Tiap elemen:
     - `name` (`string`): Nama kategori.
     - `order` (`number`): Urutan tampilan (0-indexed).
     - `icon` (`string`, opsional): ID ikon kustom (misal `"makan"`, `"mobil"`).
   - `subcategories` (`object`):
     - `income` (`object`): Kunci berupa nama kategori induk pemasukan, nilai berupa array of object `{ name, order, icon? }`.
     - `expense` (`object`): Kunci berupa nama kategori induk pengeluaran, nilai berupa array of object `{ name, order, icon? }`.
   - `updatedAt` (`number`): Timestamp Unix milidetik perubahan kategori.
8. **`budgets`** (`object`):
   - `items` (`object`): Pemetaan nama kategori pengeluaran induk ke nominal batas anggaran (`number` bilangan bulat positif): `{ [kategori]: nominal }`.
   - `subcategories` (`object`): Pemetaan bersarang kategori pengeluaran induk -> sub kategori -> nominal anggaran: `{ [kategori]: { [subKategori]: nominal } }`.
   - `updatedAt` (`number`): Timestamp Unix milidetik perubahan budget.
9. **`settings`** (`object`):
   - `theme` (`string`): Pengaturan tema antarmuka (`"light"`, `"dark"`, atau `"system"`).
   - `timeFormat` (`string`): Format tampilan waktu (`"12"` atau `"24"`).
   - `updatedAt` (`number`): Timestamp Unix milidetik perubahan pengaturan.

### Kompatibilitas Impor
- Fungsi impor (`handleImportJson`) mendukung format berkas versi 1 (berupa array transaksi murni tanpa pembungkus objek) maupun versi 2 (berkas objek cadangan lengkap).
- Transaksi dummy pada file impor otomatis diabaikan.
- Transaksi lama tanpa `walletId` tetap dapat dimuat dengan aman.

---

## 4. Format File di Google Drive `appDataFolder`
- **Lokasi Folder**: Google Drive `appDataFolder` (folder tersembunyi khusus aplikasi, tidak tampak di antarmuka web Drive pengguna biasa).
- **Scope OAuth**: `https://www.googleapis.com/auth/drive.appdata`
- **Nama File**: `mizan-okane-data.json` (`FILE_NAME = 'mizan-okane-data.json'`)
- **Struktur Isi Berkas JSON**:
  Format payload dibuat oleh fungsi `buildPayload` di `desktop/src/js/sync.js`:
  ```json
  {
    "app": "mizan-okane",
    "version": 2,
    "schemaVersion": 2,
    "exportedAt": "2026-10-18T06:00:00.000Z",
    "transactions": [],
    "categories": {},
    "budgets": {},
    "wallets": [],
    "settings": {}
  }
  ```
  Struktur objek di dalam `transactions`, `categories`, `budgets`, `wallets`, dan `settings` pada Drive sama persis dengan format file cadangan JSON. Transaksi bertanda dummy tidak pernah diunggah ke Google Drive.

---

## 5. Aturan Resolusi Konflik Data
1. **Identitas Unik**:
   - Setiap transaksi dan dompet memiliki field `id` unik berbasis string.
2. **Waktu Perubahan Terakhir (`updatedAt`)**:
   - Setiap entitas (transaksi, dompet, kategori, budget, settings) memiliki atribut timestamp `updatedAt` dalam milidetik Unix.
3. **Terbaru Menang (Last-Write-Wins)**:
   - **Transaksi**: Saat penggabungan data (sinkron Drive atau impor JSON), transaksi dengan `id` sama dibandingkan. Jika `updatedAt` data baru lebih besar dari data lama, data baru menggantikan data lama. Transaksi dengan `id` yang baru ada di satu sisi akan ditambahkan ke kumpulan transaksi gabungan.
   - **Dompet**: Dompet dengan `id` sama digabungkan berdasarkan `updatedAt` terbesar. Nilai `order` dipertahankan atau diperbarui sesuai urutan entitas terbaru.
   - **Kategori**: Dibandingkan pada level objek kategori utuh berdasarkan `updatedAt`. Objek dengan timestamp lebih besar menggantikan objek yang lama.
   - **Budget**: Dibandingkan pada level objek budget utuh berdasarkan `updatedAt`. Objek dengan timestamp lebih besar menggantikan objek yang lama.
   - **Pengaturan (Settings)**: Dibandingkan berdasarkan `updatedAt`. Objek dengan timestamp lebih baru menggantikan pengaturan yang lama.
4. **Penanda Terhapus (Soft Delete)**:
   - Penghapusan transaksi dan dompet tidak menghapus baris dari penyimpanan seketika, melainkan menyetel properti `deleted: true` dan memperbarui `updatedAt` ke waktu penghapusan.
   - Rekaman dengan `deleted: true` ikut disinkronkan ke Drive dan dicadangkan, sehingga mampu menimpa status aktif pada perangkat lain secara sah berkat `updatedAt` yang lebih mutakhir.
5. **Penanganan Mode Data Contoh (Demo)**:
   - Data contoh memiliki penanda `dummy: true` pada transaksi dan disimpan terpisah di store `meta`.
   - Transaksi contoh disaring dan dilarang untuk diunggah ke Google Drive maupun diekspor ke file cadangan JSON.

---

## 6. Contoh Data Fiktif Cadangan JSON

```json
{
  "app": "mizan-okane",
  "version": 2,
  "schemaVersion": 2,
  "exportedAt": "2026-10-18T06:00:00.000Z",
  "transactions": [
    {
      "id": "tx_contoh_101",
      "type": "income",
      "amount": 5000000,
      "date": "2026-10-01",
      "time": "09:00",
      "category": "Gaji",
      "subcategory": "Gaji Pokok",
      "walletId": "wallet_contoh_bank",
      "note": "Gaji bulanan",
      "createdAt": 1790845200000,
      "updatedAt": 1790845200000,
      "deleted": false
    },
    {
      "id": "tx_contoh_102",
      "type": "expense",
      "amount": 45000,
      "date": "2026-10-02",
      "time": "12:15",
      "category": "Makan",
      "subcategory": "Restoran",
      "walletId": "wallet_contoh_kas",
      "note": "Makan siang",
      "createdAt": 1790943300000,
      "updatedAt": 1790943300000,
      "deleted": false
    },
    {
      "id": "tx_contoh_103",
      "type": "transfer",
      "amount": 500000,
      "date": "2026-10-03",
      "time": "10:00",
      "category": "Transfer",
      "walletId": "wallet_contoh_bank",
      "toWalletId": "wallet_contoh_kas",
      "note": "Tarik tunai ATM",
      "createdAt": 1791021600000,
      "updatedAt": 1791021600000,
      "deleted": false
    },
    {
      "id": "tx_contoh_104",
      "type": "penyesuaian",
      "amount": -5000,
      "date": "2026-10-04",
      "time": "20:00",
      "category": "Equilibrium",
      "subcategory": "Biaya admin",
      "walletId": "wallet_contoh_bank",
      "note": "Biaya administrasi bulanan",
      "createdAt": 1791144000000,
      "updatedAt": 1791144000000,
      "deleted": false
    }
  ],
  "wallets": [
    {
      "id": "wallet_contoh_kas",
      "name": "Dompet Tunai",
      "currency": "IDR",
      "order": 0,
      "createdAt": 1790800000000,
      "updatedAt": 1790800000000,
      "deleted": false
    },
    {
      "id": "wallet_contoh_bank",
      "name": "Rekening Giro",
      "currency": "IDR",
      "order": 1,
      "createdAt": 1790800000000,
      "updatedAt": 1790800000000,
      "deleted": false
    }
  ],
  "categories": {
    "income": [
      { "name": "Gaji", "order": 0, "icon": "gaji" },
      { "name": "Bonus", "order": 1, "icon": "bonus" }
    ],
    "expense": [
      { "name": "Makan", "order": 0, "icon": "makan" },
      { "name": "Transport", "order": 1, "icon": "mobil" }
    ],
    "subcategories": {
      "income": {
        "Gaji": [
          { "name": "Gaji Pokok", "order": 0, "icon": "gaji" }
        ]
      },
      "expense": {
        "Makan": [
          { "name": "Restoran", "order": 0, "icon": "makan" }
        ]
      }
    },
    "updatedAt": 1790800000000
  },
  "budgets": {
    "items": {
      "Makan": 1500000
    },
    "subcategories": {
      "Makan": {
        "Restoran": 500000
      }
    },
    "updatedAt": 1790800000000
  },
  "settings": {
    "theme": "system",
    "timeFormat": "24",
    "updatedAt": 1790800000000
  }
}
```
