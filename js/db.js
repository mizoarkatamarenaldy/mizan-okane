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

const DEMO_MODE_KEY = 'mizan_demo_mode';

export function isDemoMode() {
  try {
    return localStorage.getItem(DEMO_MODE_KEY) === 'true';
  } catch (e) {
    return false;
  }
}

export function setDemoMode(enabled) {
  try {
    if (enabled) {
      localStorage.setItem(DEMO_MODE_KEY, 'true');
    } else {
      localStorage.removeItem(DEMO_MODE_KEY);
    }
  } catch (e) {}
}

export const SAMPLE_WALLETS = [
  {
    id: 'wallet_sample_kas',
    name: 'Kas Tunai',
    currency: 'IDR',
    order: 0,
    createdAt: 1700000000000,
    updatedAt: 1700000000000,
    deleted: false
  },
  {
    id: 'wallet_sample_bank',
    name: 'Rekening Bank',
    currency: 'IDR',
    order: 1,
    createdAt: 1700000000000,
    updatedAt: 1700000000000,
    deleted: false
  },
  {
    id: 'wallet_sample_jpy',
    name: 'Dompet Yen',
    currency: 'JPY',
    order: 2,
    createdAt: 1700000000000,
    updatedAt: 1700000000000,
    deleted: false
  },
  {
    id: 'wallet_sample_usd',
    name: 'Akun USD',
    currency: 'USD',
    order: 3,
    createdAt: 1700000000000,
    updatedAt: 1700000000000,
    deleted: false
  }
];

export const SAMPLE_CATEGORIES = {
  income: [
    { name: 'Gaji', order: 0, icon: 'gaji' },
    { name: 'Bonus', order: 1, icon: 'bonus' },
    { name: 'Investasi', order: 2, icon: 'investasi' },
    { name: 'Lainnya', order: 3, icon: 'titik_tiga' }
  ],
  expense: [
    { name: 'Makan', order: 0, icon: 'makan' },
    { name: 'Transport', order: 1, icon: 'mobil' },
    { name: 'Tagihan', order: 2, icon: 'tagihan' },
    { name: 'Belanja', order: 3, icon: 'belanja' },
    { name: 'Hiburan', order: 4, icon: 'game' },
    { name: 'Lainnya', order: 5, icon: 'titik_tiga' }
  ],
  subcategories: {
    income: {
      'Gaji': [
        { name: 'Gaji Pokok', order: 0 },
        { name: 'Tunjangan', order: 1 }
      ],
      'Bonus': [
        { name: 'Bonus Tahunan', order: 0 },
        { name: 'Komisi', order: 1 }
      ],
      'Investasi': [
        { name: 'Dividen', order: 0 },
        { name: 'Bunga', order: 1 }
      ]
    },
    expense: {
      'Makan': [
        { name: 'Restoran', order: 0 },
        { name: 'Belanja Bahan', order: 1 },
        { name: 'Kopi & Camilan', order: 2 }
      ],
      'Transport': [
        { name: 'Bensin', order: 0 },
        { name: 'Parkir', order: 1 },
        { name: 'Transportasi Umum', order: 2 }
      ],
      'Tagihan': [
        { name: 'Listrik', order: 0 },
        { name: 'Air & IPL', order: 1 },
        { name: 'Internet', order: 2 }
      ],
      'Belanja': [
        { name: 'Kebutuhan Rumah', order: 0 },
        { name: 'Pakaian', order: 1 },
        { name: 'Elektronik', order: 2 }
      ],
      'Hiburan': [
        { name: 'Streaming', order: 0 },
        { name: 'Hobi', order: 1 },
        { name: 'Liburan', order: 2 }
      ]
    }
  },
  updatedAt: 1700000000000
};

export const SAMPLE_BUDGETS = {
  items: {
    'Makan': 2500000,
    'Transport': 800000,
    'Tagihan': 1200000,
    'Belanja': 1500000,
    'Hiburan': 600000
  },
  subcategories: {
    'Makan': {
      'Restoran': 1200000,
      'Kopi & Camilan': 300000
    }
  },
  updatedAt: 1700000000000
};

export function getSampleTransactions(referenceDate = new Date()) {
  const ref = (referenceDate instanceof Date && !isNaN(referenceDate.getTime())) ? referenceDate : new Date();
  const curY = ref.getFullYear();
  const curM = String(ref.getMonth() + 1).padStart(2, '0');

  const prevDate = new Date(curY, ref.getMonth() - 1, 1);
  const prevY = prevDate.getFullYear();
  const prevM = String(prevDate.getMonth() + 1).padStart(2, '0');

  const makeDate = (isPrev, day) => {
    const y = isPrev ? prevY : curY;
    const m = isPrev ? prevM : curM;
    return `${y}-${m}-${String(day).padStart(2, '0')}`;
  };

  const makeTimestamp = (dateStr, timeStr) => {
    return new Date(`${dateStr}T${timeStr}:00`).getTime();
  };

  const rawList = [
    { id: 'sample_tx_01', isPrev: true, day: 1, time: '08:30', type: 'income', amount: 12000000, category: 'Gaji', subcategory: 'Gaji Pokok', walletId: 'wallet_sample_bank', note: 'Gaji bulan lalu' },
    { id: 'sample_tx_02', isPrev: true, day: 5, time: '12:30', type: 'expense', amount: 1800000, category: 'Makan', subcategory: 'Restoran', walletId: 'wallet_sample_bank', note: 'Kuliner & makan luar' },
    { id: 'sample_tx_03', isPrev: true, day: 10, time: '08:15', type: 'expense', amount: 600000, category: 'Transport', subcategory: 'Bensin', walletId: 'wallet_sample_bank', note: 'Bensin & tol' },
    { id: 'sample_tx_04', isPrev: true, day: 15, time: '14:00', type: 'expense', amount: 1050000, category: 'Tagihan', subcategory: 'Listrik', walletId: 'wallet_sample_bank', note: 'Listrik PLN & internet' },
    { id: 'sample_tx_05', isPrev: true, day: 20, time: '16:45', type: 'expense', amount: 900000, category: 'Belanja', subcategory: 'Pakaian', walletId: 'wallet_sample_bank', note: 'Pakaian dinas' },
    { id: 'sample_tx_06', isPrev: true, day: 25, time: '19:20', type: 'expense', amount: 400000, category: 'Hiburan', subcategory: 'Liburan', walletId: 'wallet_sample_bank', note: 'Tiket wahana wisata' },

    { id: 'sample_tx_07', isPrev: false, day: 1, time: '08:00', type: 'income', amount: 100000, category: 'Gaji', subcategory: 'Tunjangan', walletId: 'wallet_sample_jpy', note: 'Uang saku perjalanan dinas' },
    { id: 'sample_tx_08', isPrev: false, day: 1, time: '08:00', type: 'income', amount: 1500.00, category: 'Gaji', subcategory: 'Gaji Pokok', walletId: 'wallet_sample_usd', note: 'Saldo awal akun USD' },

    { id: 'sample_tx_09', isPrev: false, day: 1, time: '08:30', type: 'income', amount: 12000000, category: 'Gaji', subcategory: 'Gaji Pokok', walletId: 'wallet_sample_bank', note: 'Gaji bulanan' },
    { id: 'sample_tx_10', isPrev: false, day: 8, time: '10:00', type: 'income', amount: 250.00, category: 'Investasi', subcategory: 'Dividen', walletId: 'wallet_sample_usd', note: 'Dividen saham luar negeri' },
    { id: 'sample_tx_11', isPrev: false, day: 10, time: '11:15', type: 'income', amount: 2500000, category: 'Bonus', subcategory: 'Komisi', walletId: 'wallet_sample_bank', note: 'Bonus pencapaian proyek' },

    { id: 'sample_tx_12', isPrev: false, day: 2, time: '09:00', type: 'transfer', amount: 1500000, walletId: 'wallet_sample_bank', toWalletId: 'wallet_sample_kas', note: 'Tarik tunai ATM' },

    { id: 'sample_tx_13', isPrev: false, day: 3, time: '10:00', type: 'expense', amount: 450000, category: 'Makan', subcategory: 'Belanja Bahan', walletId: 'wallet_sample_kas', note: 'Belanja mingguan pasar' },
    { id: 'sample_tx_14', isPrev: false, day: 4, time: '07:45', type: 'expense', amount: 250000, category: 'Transport', subcategory: 'Bensin', walletId: 'wallet_sample_kas', note: 'Isi bensin mobil' },
    { id: 'sample_tx_15', isPrev: false, day: 5, time: '12:30', type: 'expense', amount: 125000, category: 'Makan', subcategory: 'Restoran', walletId: 'wallet_sample_bank', note: 'Makan siang bersama tim' },
    { id: 'sample_tx_16', isPrev: false, day: 5, time: '14:20', type: 'expense', amount: 650000, category: 'Tagihan', subcategory: 'Listrik', walletId: 'wallet_sample_bank', note: 'Token listrik PLN' },
    { id: 'sample_tx_17', isPrev: false, day: 6, time: '16:00', type: 'expense', amount: 50000, category: 'Transport', subcategory: 'Parkir', walletId: 'wallet_sample_kas', note: 'Parkir harian kantor' },
    { id: 'sample_tx_18', isPrev: false, day: 7, time: '15:10', type: 'expense', amount: 45000, category: 'Makan', subcategory: 'Kopi & Camilan', walletId: 'wallet_sample_kas', note: 'Kopi sore' },
    { id: 'sample_tx_19', isPrev: false, day: 8, time: '09:00', type: 'expense', amount: 400000, category: 'Tagihan', subcategory: 'Internet', walletId: 'wallet_sample_bank', note: 'Langganan Wi-Fi rumah' },
    { id: 'sample_tx_20', isPrev: false, day: 9, time: '13:45', type: 'expense', amount: 350000, category: 'Belanja', subcategory: 'Kebutuhan Rumah', walletId: 'wallet_sample_bank', note: 'Perlengkapan mandi dan dapur' },
    { id: 'sample_tx_21', isPrev: false, day: 11, time: '20:00', type: 'expense', amount: 180000, category: 'Hiburan', subcategory: 'Streaming', walletId: 'wallet_sample_bank', note: 'Langganan streaming video' },

    { id: 'sample_tx_22', isPrev: false, day: 12, time: '18:30', type: 'expense', amount: 3500, category: 'Makan', subcategory: 'Restoran', walletId: 'wallet_sample_jpy', note: 'Makan malam ramen' },
    { id: 'sample_tx_23', isPrev: false, day: 13, time: '14:00', type: 'expense', amount: 49.90, category: 'Belanja', subcategory: 'Elektronik', walletId: 'wallet_sample_usd', note: 'Aksesoris komputer' },
    { id: 'sample_tx_24', isPrev: false, day: 14, time: '16:00', type: 'expense', amount: 12000, category: 'Belanja', subcategory: 'Pakaian', walletId: 'wallet_sample_jpy', note: 'Oleh-oleh kemeja' },
    { id: 'sample_tx_25', isPrev: false, day: 15, time: '17:30', type: 'expense', amount: 12.99, category: 'Tagihan', subcategory: 'Internet', walletId: 'wallet_sample_usd', note: 'Langganan server cloud' }
  ];

  return rawList.map(item => {
    const dateStr = makeDate(item.isPrev, item.day);
    const ts = makeTimestamp(dateStr, item.time);
    const tx = {
      id: item.id,
      type: item.type,
      amount: item.amount,
      date: dateStr,
      time: item.time,
      walletId: item.walletId,
      note: item.note,
      createdAt: ts,
      updatedAt: ts,
      deleted: false,
      dummy: true
    };
    if (item.type === 'transfer') {
      tx.toWalletId = item.toWalletId;
      tx.category = 'Transfer';
    } else {
      tx.category = item.category;
      if (item.subcategory) {
        tx.subcategory = item.subcategory;
      }
    }
    return tx;
  });
}

export async function activateSampleData(referenceDate = new Date()) {
  const wallets = [...SAMPLE_WALLETS];
  const categories = JSON.parse(JSON.stringify(SAMPLE_CATEGORIES));
  const budgets = JSON.parse(JSON.stringify(SAMPLE_BUDGETS));
  const transactions = getSampleTransactions(referenceDate);

  await setMeta('demo_wallets', wallets);
  await setMeta('demo_categories', categories);
  await setMeta('demo_budgets', budgets);
  await setMeta('demo_transactions', transactions);
  setDemoMode(true);
}

export async function clearSampleData() {
  setDemoMode(false);
  await deleteMeta('demo_wallets');
  await deleteMeta('demo_categories');
  await deleteMeta('demo_budgets');
  await deleteMeta('demo_transactions');
}

export function saveRealTransaction(tx) {
  return new Promise((resolve, reject) => {
    const transaction = db.transaction([STORE_NAME], 'readwrite');
    const store = transaction.objectStore(STORE_NAME);
    const request = store.put(tx);

    request.onsuccess = () => resolve(tx);
    request.onerror = (event) => reject(event.target.error);
  });
}

export function saveTransaction(tx) {
  if (isDemoMode()) {
    return new Promise((resolve, reject) => {
      const transaction = db.transaction([META_STORE], 'readwrite');
      const store = transaction.objectStore(META_STORE);
      const req = store.get('demo_transactions');
      req.onsuccess = () => {
        const list = Array.isArray(req.result) ? [...req.result] : [];
        const idx = list.findIndex(t => t.id === tx.id);
        if (idx >= 0) {
          list[idx] = tx;
        } else {
          list.push(tx);
        }
        const putReq = store.put(list, 'demo_transactions');
        putReq.onsuccess = () => resolve(tx);
        putReq.onerror = (e) => reject(e.target.error);
      };
      req.onerror = (e) => reject(e.target.error);
    });
  }
  return saveRealTransaction(tx);
}

export function getRealTransactions() {
  return new Promise((resolve, reject) => {
    const transaction = db.transaction([STORE_NAME], 'readonly');
    const store = transaction.objectStore(STORE_NAME);
    const request = store.getAll();

    request.onsuccess = (event) => resolve(event.target.result || []);
    request.onerror = (event) => reject(event.target.error);
  });
}

export function getAllTransactions() {
  if (isDemoMode()) {
    return new Promise((resolve, reject) => {
      const transaction = db.transaction([META_STORE], 'readonly');
      const request = transaction.objectStore(META_STORE).get('demo_transactions');
      request.onsuccess = (event) => resolve(Array.isArray(event.target.result) ? event.target.result : []);
      request.onerror = (event) => reject(event.target.error);
    });
  }
  return getRealTransactions();
}

// Ganti seluruh isi store transaksi dalam satu transaksi IndexedDB,
// supaya tidak ada keadaan setengah terisi kalau gagal di tengah.
export function replaceAllRealTransactions(list) {
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

export function replaceAllTransactions(list) {
  if (isDemoMode()) {
    return setMeta('demo_transactions', Array.isArray(list) ? list : []);
  }
  return replaceAllRealTransactions(list);
}

export const DEFAULT_CATEGORIES = {
  income: [
    { name: 'Gaji', order: 0 },
    { name: 'Bonus', order: 1 },
    { name: 'Hadiah', order: 2 },
    { name: 'Lainnya', order: 3 }
  ],
  expense: [
    { name: 'Makan', order: 0 },
    { name: 'Transport', order: 1 },
    { name: 'Hiburan', order: 2 },
    { name: 'Belanja', order: 3 },
    { name: 'Tagihan', order: 4 },
    { name: 'Lainnya', order: 5 }
  ],
  subcategories: {
    income: {},
    expense: {}
  },
  updatedAt: 0
};

export function sanitizeCategoryItem(item, defaultOrder = 0) {
  if (typeof item === 'string') {
    const trimmed = item.trim();
    return trimmed ? { name: trimmed, order: defaultOrder } : null;
  }
  if (item && typeof item === 'object') {
    const name = String(item.name || '').trim();
    if (!name) return null;
    const order = typeof item.order === 'number'
      ? item.order
      : (typeof item.urutan === 'number' ? item.urutan : defaultOrder);
    const result = { name, order };
    if (typeof item.icon === 'string' && item.icon.trim()) {
      result.icon = item.icon.trim();
    }
    return result;
  }
  return null;
}

export function sanitizeCategoryList(list, defaultList = []) {
  if (!Array.isArray(list)) {
    return defaultList.map((item, idx) => sanitizeCategoryItem(item, idx)).filter(Boolean);
  }
  const result = [];
  const seen = new Set();
  list.forEach((item, idx) => {
    const cat = sanitizeCategoryItem(item, idx);
    if (cat && !seen.has(cat.name.toLowerCase())) {
      seen.add(cat.name.toLowerCase());
      result.push(cat);
    }
  });
  result.sort((a, b) => a.order - b.order);
  return result;
}

export function sanitizeSubcategoryList(list) {
  if (!Array.isArray(list)) return [];
  const result = [];
  const seen = new Set();
  list.forEach((item, idx) => {
    const sub = sanitizeCategoryItem(item, idx);
    if (sub && !seen.has(sub.name.toLowerCase())) {
      seen.add(sub.name.toLowerCase());
      result.push({ name: sub.name, order: sub.order });
    }
  });
  result.sort((a, b) => a.order - b.order);
  return result;
}

export function sanitizeCategories(categories) {
  const cleanSubcategories = {
    income: {},
    expense: {}
  };
  if (categories && typeof categories.subcategories === 'object' && categories.subcategories !== null) {
    if (typeof categories.subcategories.income === 'object' && categories.subcategories.income !== null) {
      for (const [cat, subs] of Object.entries(categories.subcategories.income)) {
        cleanSubcategories.income[cat] = sanitizeSubcategoryList(subs);
      }
    }
    if (typeof categories.subcategories.expense === 'object' && categories.subcategories.expense !== null) {
      for (const [cat, subs] of Object.entries(categories.subcategories.expense)) {
        cleanSubcategories.expense[cat] = sanitizeSubcategoryList(subs);
      }
    }
  }
  return {
    income: sanitizeCategoryList(categories?.income, DEFAULT_CATEGORIES.income),
    expense: sanitizeCategoryList(categories?.expense, DEFAULT_CATEGORIES.expense),
    subcategories: cleanSubcategories,
    updatedAt: typeof categories?.updatedAt === 'number' ? categories.updatedAt : Date.now()
  };
}

export function getRealCategories() {
  return new Promise((resolve, reject) => {
    const transaction = db.transaction([META_STORE], 'readonly');
    const request = transaction.objectStore(META_STORE).get('categories');
    request.onsuccess = (event) => {
      const stored = event.target.result;
      if (stored && Array.isArray(stored.income) && Array.isArray(stored.expense)) {
        resolve(sanitizeCategories(stored));
      } else {
        const initial = sanitizeCategories(DEFAULT_CATEGORIES);
        initial.updatedAt = 0;
        const writeTx = db.transaction([META_STORE], 'readwrite');
        writeTx.objectStore(META_STORE).put(initial, 'categories');
        writeTx.oncomplete = () => resolve(initial);
        writeTx.onerror = () => resolve(initial);
      }
    };
    request.onerror = (event) => reject(event.target.error);
  });
}

export function getCategories() {
  if (isDemoMode()) {
    return new Promise((resolve, reject) => {
      const transaction = db.transaction([META_STORE], 'readonly');
      const request = transaction.objectStore(META_STORE).get('demo_categories');
      request.onsuccess = (event) => {
        const stored = event.target.result;
        if (stored && Array.isArray(stored.income) && Array.isArray(stored.expense)) {
          resolve(sanitizeCategories(stored));
        } else {
          resolve(sanitizeCategories(SAMPLE_CATEGORIES));
        }
      };
      request.onerror = (event) => reject(event.target.error);
    });
  }
  return getRealCategories();
}

export function saveRealCategories(categories) {
  return new Promise((resolve, reject) => {
    const toSave = sanitizeCategories(categories);
    const transaction = db.transaction([META_STORE], 'readwrite');
    const request = transaction.objectStore(META_STORE).put(toSave, 'categories');
    request.onsuccess = () => resolve(toSave);
    request.onerror = (event) => reject(event.target.error);
  });
}

export function saveCategories(categories) {
  if (isDemoMode()) {
    return new Promise((resolve, reject) => {
      const toSave = sanitizeCategories(categories);
      const transaction = db.transaction([META_STORE], 'readwrite');
      const request = transaction.objectStore(META_STORE).put(toSave, 'demo_categories');
      request.onsuccess = () => resolve(toSave);
      request.onerror = (event) => reject(event.target.error);
    });
  }
  return saveRealCategories(categories);
}

export const DEFAULT_BUDGETS = {
  items: {},
  subcategories: {},
  updatedAt: 0
};

export function getRealBudgets() {
  return new Promise((resolve, reject) => {
    const transaction = db.transaction([META_STORE], 'readonly');
    const request = transaction.objectStore(META_STORE).get('budgets');
    request.onsuccess = (event) => {
      const stored = event.target.result;
      if (stored && typeof stored === 'object' && stored.items && typeof stored.items === 'object' && !Array.isArray(stored.items)) {
        resolve(sanitizeBudgets(stored));
      } else {
        const initial = {
          items: {},
          subcategories: {},
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

export function getBudgets() {
  if (isDemoMode()) {
    return new Promise((resolve, reject) => {
      const transaction = db.transaction([META_STORE], 'readonly');
      const request = transaction.objectStore(META_STORE).get('demo_budgets');
      request.onsuccess = (event) => {
        const stored = event.target.result;
        if (stored && typeof stored === 'object' && stored.items && typeof stored.items === 'object') {
          resolve(sanitizeBudgets(stored));
        } else {
          resolve(sanitizeBudgets(SAMPLE_BUDGETS));
        }
      };
      request.onerror = (event) => reject(event.target.error);
    });
  }
  return getRealBudgets();
}

export function sanitizeBudgets(budgets) {
  const cleanItems = {};
  if (budgets && typeof budgets.items === 'object' && !Array.isArray(budgets.items)) {
    for (const [k, v] of Object.entries(budgets.items)) {
      const num = Number(v);
      if (k && Number.isFinite(num) && num > 0) {
        cleanItems[k] = Math.round(num);
      }
    }
  }
  const cleanSubcategories = {};
  if (budgets && typeof budgets.subcategories === 'object' && !Array.isArray(budgets.subcategories)) {
    for (const [cat, subs] of Object.entries(budgets.subcategories)) {
      if (subs && typeof subs === 'object' && !Array.isArray(subs)) {
        const cleanSubs = {};
        for (const [subName, subVal] of Object.entries(subs)) {
          const num = Number(subVal);
          if (subName && Number.isFinite(num) && num > 0) {
            cleanSubs[subName] = Math.round(num);
          }
        }
        if (Object.keys(cleanSubs).length > 0) {
          cleanSubcategories[cat] = cleanSubs;
        }
      }
    }
  }
  return {
    items: cleanItems,
    subcategories: cleanSubcategories,
    updatedAt: typeof budgets?.updatedAt === 'number' ? budgets.updatedAt : Date.now()
  };
}

export function saveRealBudgets(budgets) {
  return new Promise((resolve, reject) => {
    const toSave = sanitizeBudgets(budgets);
    const transaction = db.transaction([META_STORE], 'readwrite');
    const request = transaction.objectStore(META_STORE).put(toSave, 'budgets');
    request.onsuccess = () => resolve(toSave);
    request.onerror = (event) => reject(event.target.error);
  });
}

export function saveBudgets(budgets) {
  if (isDemoMode()) {
    return new Promise((resolve, reject) => {
      const toSave = sanitizeBudgets(budgets);
      const transaction = db.transaction([META_STORE], 'readwrite');
      const request = transaction.objectStore(META_STORE).put(toSave, 'demo_budgets');
      request.onsuccess = () => resolve(toSave);
      request.onerror = (event) => reject(event.target.error);
    });
  }
  return saveRealBudgets(budgets);
}

export const DEFAULT_WALLET_ID = 'default';
export const DEFAULT_WALLET_NAME = 'Dompet Utama';

export const DEFAULT_WALLETS = [
  {
    id: DEFAULT_WALLET_ID,
    name: DEFAULT_WALLET_NAME,
    currency: 'IDR',
    order: 0,
    createdAt: 0,
    updatedAt: 0,
    deleted: false
  }
];

export function sanitizeWallets(wallets) {
  const list = Array.isArray(wallets) ? wallets : [];
  const cleaned = list.map((w, idx) => ({
    id: String(w.id),
    name: String(w.name).trim(),
    currency: (w.currency === 'JPY' || w.currency === 'USD') ? w.currency : 'IDR',
    order: typeof w.order === 'number' ? w.order : (typeof w.urutan === 'number' ? w.urutan : idx),
    createdAt: typeof w.createdAt === 'number' ? w.createdAt : Date.now(),
    updatedAt: typeof w.updatedAt === 'number' ? w.updatedAt : Date.now(),
    deleted: !!w.deleted
  }));
  cleaned.sort((a, b) => a.order - b.order);
  return cleaned;
}

export function getRealWallets() {
  return new Promise((resolve, reject) => {
    const transaction = db.transaction([META_STORE], 'readonly');
    const request = transaction.objectStore(META_STORE).get('wallets');
    request.onsuccess = (event) => {
      const stored = event.target.result;
      if (Array.isArray(stored) && stored.length > 0) {
        const clean = sanitizeWallets(stored);
        if (clean.length > 0) {
          resolve(clean);
          return;
        }
      }
      const initial = sanitizeWallets(DEFAULT_WALLETS).map(w => ({
        ...w,
        createdAt: Date.now()
      }));
      const writeTx = db.transaction([META_STORE], 'readwrite');
      writeTx.objectStore(META_STORE).put(initial, 'wallets');
      writeTx.oncomplete = () => resolve(initial);
      writeTx.onerror = () => resolve(initial);
    };
    request.onerror = (event) => reject(event.target.error);
  });
}

export function getWallets() {
  if (isDemoMode()) {
    return new Promise((resolve, reject) => {
      const transaction = db.transaction([META_STORE], 'readonly');
      const request = transaction.objectStore(META_STORE).get('demo_wallets');
      request.onsuccess = (event) => {
        const stored = event.target.result;
        if (Array.isArray(stored) && stored.length > 0) {
          resolve(sanitizeWallets(stored));
        } else {
          resolve(sanitizeWallets(SAMPLE_WALLETS));
        }
      };
      request.onerror = (event) => reject(event.target.error);
    });
  }
  return getRealWallets();
}

export function saveRealWallets(wallets) {
  return new Promise((resolve, reject) => {
    const clean = sanitizeWallets(wallets);
    const transaction = db.transaction([META_STORE], 'readwrite');
    const request = transaction.objectStore(META_STORE).put(clean, 'wallets');
    request.onsuccess = () => resolve(clean);
    request.onerror = (event) => reject(event.target.error);
  });
}

export function saveWallets(wallets) {
  if (isDemoMode()) {
    return new Promise((resolve, reject) => {
      const clean = sanitizeWallets(wallets);
      const transaction = db.transaction([META_STORE], 'readwrite');
      const request = transaction.objectStore(META_STORE).put(clean, 'demo_wallets');
      request.onsuccess = () => resolve(clean);
      request.onerror = (event) => reject(event.target.error);
    });
  }
  return saveRealWallets(wallets);
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
    const store = transaction.objectStore(META_STORE);
    store.put(value, 'timeFormat');
    const req = store.get('settings');
    req.onsuccess = () => {
      const current = req.result || {};
      const updated = {
        theme: (current.theme === 'light' || current.theme === 'dark' || current.theme === 'system') ? current.theme : 'system',
        timeFormat: value,
        updatedAt: Date.now()
      };
      store.put(updated, 'settings');
    };
    transaction.oncomplete = () => resolve(value);
    transaction.onerror = (event) => reject(event.target.error);
  });
}

export const DEFAULT_SETTINGS = {
  theme: 'system',
  timeFormat: '24',
  updatedAt: 0
};

export function getSettings() {
  return new Promise((resolve, reject) => {
    const transaction = db.transaction([META_STORE], 'readonly');
    const request = transaction.objectStore(META_STORE).get('settings');
    request.onsuccess = (event) => {
      const stored = event.target.result;
      if (stored && typeof stored === 'object') {
        resolve({
          theme: (stored.theme === 'light' || stored.theme === 'dark' || stored.theme === 'system') ? stored.theme : 'system',
          timeFormat: stored.timeFormat === '12' ? '12' : DEFAULT_TIME_FORMAT,
          updatedAt: typeof stored.updatedAt === 'number' ? stored.updatedAt : 0
        });
      } else {
        const timeReq = transaction.objectStore(META_STORE).get('timeFormat');
        timeReq.onsuccess = (tEvent) => {
          const tStored = tEvent.target.result;
          resolve({
            theme: 'system',
            timeFormat: tStored === '12' ? '12' : DEFAULT_TIME_FORMAT,
            updatedAt: 0
          });
        };
        timeReq.onerror = () => {
          resolve({
            theme: 'system',
            timeFormat: DEFAULT_TIME_FORMAT,
            updatedAt: 0
          });
        };
      }
    };
    request.onerror = (event) => reject(event.target.error);
  });
}

export function saveSettings(settings) {
  return new Promise((resolve, reject) => {
    const toSave = {
      theme: (settings && (settings.theme === 'light' || settings.theme === 'dark' || settings.theme === 'system')) ? settings.theme : 'system',
      timeFormat: (settings && settings.timeFormat === '12') ? '12' : DEFAULT_TIME_FORMAT,
      updatedAt: (settings && typeof settings.updatedAt === 'number') ? settings.updatedAt : Date.now()
    };
    const transaction = db.transaction([META_STORE], 'readwrite');
    const store = transaction.objectStore(META_STORE);
    store.put(toSave, 'settings');
    store.put(toSave.timeFormat, 'timeFormat');
    transaction.oncomplete = () => resolve(toSave);
    transaction.onerror = (event) => reject(event.target.error);
  });
}

export async function resetDatabase() {
  const initialCategories = sanitizeCategories(DEFAULT_CATEGORIES);
  initialCategories.updatedAt = 0;
  const initialBudgets = {
    items: {},
    subcategories: {},
    updatedAt: 0
  };
  const initialWallets = sanitizeWallets(DEFAULT_WALLETS).map(w => ({
    ...w,
    createdAt: Date.now(),
    updatedAt: 0
  }));
  const initialSettings = {
    theme: 'system',
    timeFormat: DEFAULT_TIME_FORMAT,
    updatedAt: 0
  };

  await clearSampleData();
  await replaceAllRealTransactions([]);
  await saveRealCategories(initialCategories);
  await saveRealBudgets(initialBudgets);
  await saveRealWallets(initialWallets);
  await saveSettings(initialSettings);
  await saveTimeFormat(DEFAULT_TIME_FORMAT);
  await setMeta('localRev', 0);
}



