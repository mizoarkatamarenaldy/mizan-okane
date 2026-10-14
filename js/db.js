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

export const DEFAULT_BUDGETS = {
  items: {},
  updatedAt: 0
};

export function getBudgets() {
  return new Promise((resolve, reject) => {
    const transaction = db.transaction([META_STORE], 'readonly');
    const request = transaction.objectStore(META_STORE).get('budgets');
    request.onsuccess = (event) => {
      const stored = event.target.result;
      if (stored && typeof stored === 'object' && stored.items && typeof stored.items === 'object' && !Array.isArray(stored.items)) {
        resolve(stored);
      } else {
        const initial = {
          items: {},
          updatedAt: 0
        };
        const writeTx = db.transaction([META_STORE], 'readwrite');
        writeTx.objectStore(META_STORE).put(initial, 'budgets');
        writeTx.oncomplete = () => resolve(initial);
        writeTx.onerror = () => resolve(initial);
      }
    };
    request.onerror = (event) => reject(event.target.error);
  });
}

export function saveBudgets(budgets) {
  return new Promise((resolve, reject) => {
    const cleanItems = {};
    if (budgets && typeof budgets.items === 'object' && !Array.isArray(budgets.items)) {
      for (const [k, v] of Object.entries(budgets.items)) {
        const num = Number(v);
        if (k && Number.isFinite(num) && num > 0) {
          cleanItems[k] = Math.round(num);
        }
      }
    }
    const toSave = {
      items: cleanItems,
      updatedAt: typeof budgets?.updatedAt === 'number' ? budgets.updatedAt : Date.now()
    };
    const transaction = db.transaction([META_STORE], 'readwrite');
    const request = transaction.objectStore(META_STORE).put(toSave, 'budgets');
    request.onsuccess = () => resolve(toSave);
    request.onerror = (event) => reject(event.target.error);
  });
}

export const DEFAULT_WALLET_ID = 'default';
export const DEFAULT_WALLET_NAME = 'Dompet Utama';

export const DEFAULT_WALLETS = [
  {
    id: DEFAULT_WALLET_ID,
    name: DEFAULT_WALLET_NAME,
    currency: 'IDR',
    createdAt: 0,
    updatedAt: 0,
    deleted: false
  }
];

export function getWallets() {
  return new Promise((resolve, reject) => {
    const transaction = db.transaction([META_STORE], 'readonly');
    const request = transaction.objectStore(META_STORE).get('wallets');
    request.onsuccess = (event) => {
      const stored = event.target.result;
      if (Array.isArray(stored) && stored.length > 0) {
        const clean = stored
          .filter(w => w && typeof w.id === 'string' && typeof w.name === 'string')
          .map(w => ({
            id: String(w.id),
            name: String(w.name).trim(),
            currency: w.currency === 'JPY' ? 'JPY' : 'IDR',
            createdAt: typeof w.createdAt === 'number' ? w.createdAt : 0,
            updatedAt: typeof w.updatedAt === 'number' ? w.updatedAt : 0,
            deleted: !!w.deleted
          }));
        if (clean.length > 0) {
          resolve(clean);
          return;
        }
      }
      const initial = [
        {
          id: DEFAULT_WALLET_ID,
          name: DEFAULT_WALLET_NAME,
          currency: 'IDR',
          createdAt: Date.now(),
          updatedAt: 0,
          deleted: false
        }
      ];
      const writeTx = db.transaction([META_STORE], 'readwrite');
      writeTx.objectStore(META_STORE).put(initial, 'wallets');
      writeTx.oncomplete = () => resolve(initial);
      writeTx.onerror = () => resolve(initial);
    };
    request.onerror = (event) => reject(event.target.error);
  });
}

export function saveWallets(wallets) {
  return new Promise((resolve, reject) => {
    const list = Array.isArray(wallets) ? wallets : [];
    const clean = list.map(w => ({
      id: String(w.id),
      name: String(w.name).trim(),
      currency: w.currency === 'JPY' ? 'JPY' : 'IDR',
      createdAt: typeof w.createdAt === 'number' ? w.createdAt : Date.now(),
      updatedAt: typeof w.updatedAt === 'number' ? w.updatedAt : Date.now(),
      deleted: !!w.deleted
    }));
    const transaction = db.transaction([META_STORE], 'readwrite');
    const request = transaction.objectStore(META_STORE).put(clean, 'wallets');
    request.onsuccess = () => resolve(clean);
    request.onerror = (event) => reject(event.target.error);
  });
}

export const DEFAULT_TIME_FORMAT = '24';

export function getTimeFormat() {
  return new Promise((resolve, reject) => {
    const transaction = db.transaction([META_STORE], 'readonly');
    const request = transaction.objectStore(META_STORE).get('timeFormat');
    request.onsuccess = (event) => {
      const stored = event.target.result;
      resolve(stored === '12' ? '12' : DEFAULT_TIME_FORMAT);
    };
    request.onerror = (event) => reject(event.target.error);
  });
}

export function saveTimeFormat(format) {
  return new Promise((resolve, reject) => {
    const value = format === '12' ? '12' : DEFAULT_TIME_FORMAT;
    const transaction = db.transaction([META_STORE], 'readwrite');
    const request = transaction.objectStore(META_STORE).put(value, 'timeFormat');
    request.onsuccess = () => resolve(value);
    request.onerror = (event) => reject(event.target.error);
  });
}


