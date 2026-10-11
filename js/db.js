const DB_NAME = 'mizan-okane-db';
const DB_VERSION = 2;
const STORE_NAME = 'transactions';
// Status sinkron per perangkat (revisi lokal, id file Drive, versi terakhir).
// Tidak pernah menyimpan token.
const META_STORE = 'meta';

let db;

export function initDB() {
  return new Promise((resolve, reject) => {
    const request = indexedDB.open(DB_NAME, DB_VERSION);

    request.onerror = (event) => {
      reject(event.target.error);
    };

    request.onsuccess = (event) => {
      db = event.target.result;
      resolve(db);
    };

    request.onupgradeneeded = (event) => {
      db = event.target.result;
      if (!db.objectStoreNames.contains(STORE_NAME)) {
        db.createObjectStore(STORE_NAME, { keyPath: 'id' });
      }
      if (!db.objectStoreNames.contains(META_STORE)) {
        db.createObjectStore(META_STORE);
      }
    };
  });
}

export function saveTransaction(tx) {
  return new Promise((resolve, reject) => {
    const transaction = db.transaction([STORE_NAME], 'readwrite');
    const store = transaction.objectStore(STORE_NAME);
    const request = store.put(tx);

    request.onsuccess = () => resolve(tx);
    request.onerror = (event) => reject(event.target.error);
  });
}

export function getAllTransactions() {
  return new Promise((resolve, reject) => {
    const transaction = db.transaction([STORE_NAME], 'readonly');
    const store = transaction.objectStore(STORE_NAME);
    const request = store.getAll();

    request.onsuccess = (event) => resolve(event.target.result);
    request.onerror = (event) => reject(event.target.error);
  });
}

// Ganti seluruh isi store transaksi dalam satu transaksi IndexedDB,
// supaya tidak ada keadaan setengah terisi kalau gagal di tengah.
export function replaceAllTransactions(list) {
  return new Promise((resolve, reject) => {
    const transaction = db.transaction([STORE_NAME], 'readwrite');
    const store = transaction.objectStore(STORE_NAME);
    store.clear();
    for (const tx of list) {
      store.put(tx);
    }
    transaction.oncomplete = () => resolve();
    transaction.onerror = (event) => reject(event.target.error);
    transaction.onabort = (event) => reject(event.target.error);
  });
}

export function getMeta(key) {
  return new Promise((resolve, reject) => {
    const transaction = db.transaction([META_STORE], 'readonly');
    const request = transaction.objectStore(META_STORE).get(key);
    request.onsuccess = (event) => resolve(event.target.result);
    request.onerror = (event) => reject(event.target.error);
  });
}

export function setMeta(key, value) {
  return new Promise((resolve, reject) => {
    const transaction = db.transaction([META_STORE], 'readwrite');
    const request = transaction.objectStore(META_STORE).put(value, key);
    request.onsuccess = () => resolve(value);
    request.onerror = (event) => reject(event.target.error);
  });
}

export function deleteMeta(key) {
  return new Promise((resolve, reject) => {
    const transaction = db.transaction([META_STORE], 'readwrite');
    const request = transaction.objectStore(META_STORE).delete(key);
    request.onsuccess = () => resolve();
    request.onerror = (event) => reject(event.target.error);
  });
}

export const DEFAULT_CATEGORIES = {
  income: ['Gaji', 'Bonus', 'Hadiah', 'Lainnya'],
  expense: ['Makan', 'Transport', 'Hiburan', 'Belanja', 'Tagihan', 'Lainnya'],
  updatedAt: 0
};

export function getCategories() {
  return new Promise((resolve, reject) => {
    const transaction = db.transaction([META_STORE], 'readonly');
    const request = transaction.objectStore(META_STORE).get('categories');
    request.onsuccess = (event) => {
      const stored = event.target.result;
      if (stored && Array.isArray(stored.income) && Array.isArray(stored.expense)) {
        resolve(stored);
      } else {
        const initial = {
          income: [...DEFAULT_CATEGORIES.income],
          expense: [...DEFAULT_CATEGORIES.expense],
          updatedAt: 0
        };
        const writeTx = db.transaction([META_STORE], 'readwrite');
        writeTx.objectStore(META_STORE).put(initial, 'categories');
        writeTx.oncomplete = () => resolve(initial);
        writeTx.onerror = () => resolve(initial);
      }
    };
    request.onerror = (event) => reject(event.target.error);
  });
}

export function saveCategories(categories) {
  return new Promise((resolve, reject) => {
    const toSave = {
      income: Array.isArray(categories.income) ? categories.income : [...DEFAULT_CATEGORIES.income],
      expense: Array.isArray(categories.expense) ? categories.expense : [...DEFAULT_CATEGORIES.expense],
      updatedAt: typeof categories.updatedAt === 'number' ? categories.updatedAt : Date.now()
    };
    const transaction = db.transaction([META_STORE], 'readwrite');
    const request = transaction.objectStore(META_STORE).put(toSave, 'categories');
    request.onsuccess = () => resolve(toSave);
    request.onerror = (event) => reject(event.target.error);
  });
}

