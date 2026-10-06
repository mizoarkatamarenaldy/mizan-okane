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
