// Sinkron Google Drive (appDataFolder) dengan Google Identity Services token flow.
// IndexedDB tetap sumber utama; modul ini hanya menyalin snapshot ke/dari Drive.
// Access token hanya disimpan di memori (variabel JS), tidak pernah ke storage.
import { getAllTransactions, replaceAllTransactions, getMeta, setMeta, deleteMeta, getCategories, saveCategories, getBudgets, saveBudgets, getWallets, saveWallets } from './db.js';

const CLIENT_ID = '663070681186-pjrnce0fcpjecvbf9o2fj99e59e3s0un.apps.googleusercontent.com';
const SCOPE = 'https://www.googleapis.com/auth/drive.appdata';
const GIS_SRC = 'https://accounts.google.com/gsi/client';
const DRIVE_API = 'https://www.googleapis.com/drive/v3';
const DRIVE_UPLOAD = 'https://www.googleapis.com/upload/drive/v3';
const FILE_NAME = 'mizan-okane-data.json';
const SCHEMA_VERSION = 1;
const SYNC_DEBOUNCE_MS = 1500;
const SILENT_TOKEN_TIMEOUT_MS = 15000;

// Penanda boolean di IndexedDB store meta bahwa perangkat ini pernah login Google.
const META_KEY_LINKED = 'googleLoggedIn';
// Hanya email akun (untuk login_hint dan tampilan), bukan token.
const ACCOUNT_KEY = 'mizan-okane:google-account';

class AuthRequiredError extends Error {}
class ConflictError extends Error {}
class SupersededError extends Error {}

let tokenClient = null;
let gisPromise = null;
let accessToken = null;
let tokenExpiresAt = 0;
let pendingToken = null;
let isGoogleLinked = false;
let cachedLoginHint = '';

let syncing = false;
let syncAgain = false;
let debounceTimer = null;

let state = { status: 'signed-out', email: null, lastSyncAt: null, message: '' };
let listeners = { onState: () => {}, onRemoteApplied: async () => {} };

function setState(patch) {
    state = { ...state, ...patch };
    listeners.onState({ ...state });
}

function getAccount() {
    try {
        return JSON.parse(localStorage.getItem(ACCOUNT_KEY));
    } catch {
        return null;
    }
}

function setAccount(account) {
    if (account) {
        localStorage.setItem(ACCOUNT_KEY, JSON.stringify(account));
    } else {
        localStorage.removeItem(ACCOUNT_KEY);
    }
}

export function isLinked() {
    return isGoogleLinked || !!getAccount();
}

// --- Google Identity Services ---

function injectGisScript() {
    return new Promise((resolve, reject) => {
        const script = document.createElement('script');
        script.src = GIS_SRC;
        script.async = true;
        script.onload = () => resolve();
        script.onerror = () => {
            script.remove();
            reject(new Error('Layanan login Google tidak bisa dimuat. Periksa koneksi internet.'));
        };
        document.head.appendChild(script);
    });
}

function loadGis() {
    if (tokenClient) return Promise.resolve();
    if (gisPromise) return gisPromise;

    const ready = window.google?.accounts?.oauth2 ? Promise.resolve() : injectGisScript();
    gisPromise = ready.then(() => {
        tokenClient = google.accounts.oauth2.initTokenClient({
            client_id: CLIENT_ID,
            scope: SCOPE,
            callback: handleTokenResponse,
            error_callback: handleTokenError
        });
    }).catch((err) => {
        gisPromise = null;
        throw err;
    });

    return gisPromise;
}

function handleTokenResponse(resp) {
    const pending = pendingToken;
    pendingToken = null;
    if (!pending) return;

    if (resp.error) {
        pending.reject(new AuthRequiredError(`Login dibatalkan (${resp.error}).`));
        return;
    }
    if (!google.accounts.oauth2.hasGrantedAllScopes(resp, SCOPE)) {
        pending.reject(new AuthRequiredError('Izin akses Google Drive tidak diberikan. Login lagi dan centang izin Drive.'));
        return;
    }
    accessToken = resp.access_token;
    tokenExpiresAt = Date.now() + (Number(resp.expires_in) || 3600) * 1000;
    pending.resolve(accessToken);
}

function handleTokenError(err) {
    const pending = pendingToken;
    pendingToken = null;
    if (!pending) return;

    const messages = {
        popup_failed_to_open: 'Jendela login Google diblokir browser. Ketuk tombolnya lagi.',
        popup_closed: 'Jendela login Google ditutup sebelum selesai.'
    };
    pending.reject(new AuthRequiredError(messages[err?.type] || 'Login Google gagal.'));
}

function tokenValid() {
    return accessToken && Date.now() < tokenExpiresAt - 60000;
}

// Harus dipanggil langsung di dalam handler klik (sebelum await apa pun)
// supaya popup Google tidak diblokir, terutama di Safari.
function requestToken() {
    if (pendingToken) {
        return pendingToken.promise;
    }

    let resolve, reject;
    const promise = new Promise((res, rej) => { resolve = res; reject = rej; });
    pendingToken = { promise, resolve, reject };

    const open = () => {
        const hint = cachedLoginHint || getAccount()?.email || '';
        tokenClient.requestAccessToken({ prompt: '', login_hint: hint });
    };

    if (tokenClient) {
        open();
    } else {
        loadGis().then(open).catch((err) => {
            pendingToken = null;
            reject(err);
        });
    }
    return promise;
}

export function ensureToken() {
    if (!isLinked() || tokenValid() || !navigator.onLine) return null;
    return requestToken();
}


// --- Drive API v3 ---

async function driveFetch(url, options = {}) {
    if (!tokenValid()) throw new AuthRequiredError('Sesi Google berakhir.');

    let res;
    try {
        res = await fetch(url, {
            ...options,
            headers: { ...(options.headers || {}), Authorization: `Bearer ${accessToken}` }
        });
    } catch {
        throw new Error('Tidak bisa menghubungi Google Drive.');
    }

    if (res.status === 401) {
        accessToken = null;
        throw new AuthRequiredError('Sesi Google berakhir.');
    }
    if (!res.ok) {
        let detail = '';
        try {
            detail = (await res.json())?.error?.message || '';
        } catch { /* body bukan JSON */ }
        throw new Error(`Drive menolak permintaan (${res.status}${detail ? `: ${detail}` : ''}).`);
    }
    return res;
}

async function fetchEmail() {
    const res = await driveFetch(`${DRIVE_API}/about?fields=user(emailAddress)`);
    const data = await res.json();
    return data.user?.emailAddress || null;
}

async function findRemoteFile() {
    const params = new URLSearchParams({
        spaces: 'appDataFolder',
        q: `name = '${FILE_NAME}' and trashed = false`,
        fields: 'files(id,version,modifiedTime)',
        orderBy: 'modifiedTime desc',
        pageSize: '10'
    });
    const res = await driveFetch(`${DRIVE_API}/files?${params}`);
    const data = await res.json();
    return data.files?.[0] || null;
}

async function downloadRemote(fileId) {
    const res = await driveFetch(`${DRIVE_API}/files/${fileId}?alt=media`);
    let data;
    try {
        data = await res.json();
    } catch {
        throw new Error('File sinkron di Drive rusak (bukan JSON).');
    }
    if (!data || !Array.isArray(data.transactions) || data.transactions.some(tx => !tx || typeof tx.id !== 'string')) {
        throw new Error('Format file sinkron di Drive tidak dikenali.');
    }
    return data;
}

function mergeWallets(localWallets, remoteWallets) {
    const merged = new Map();
    if (Array.isArray(remoteWallets)) {
        for (const w of remoteWallets) {
            if (w && typeof w.id === 'string') {
                merged.set(w.id, {
                    id: String(w.id),
                    name: String(w.name || ''),
                    createdAt: typeof w.createdAt === 'number' ? w.createdAt : 0,
                    updatedAt: typeof w.updatedAt === 'number' ? w.updatedAt : 0,
                    deleted: !!w.deleted
                });
            }
        }
    }
    if (Array.isArray(localWallets)) {
        for (const w of localWallets) {
            if (w && typeof w.id === 'string') {
                const existing = merged.get(w.id);
                if (!existing || (w.updatedAt || 0) > (existing.updatedAt || 0)) {
                    merged.set(w.id, {
                        id: String(w.id),
                        name: String(w.name || ''),
                        createdAt: typeof w.createdAt === 'number' ? w.createdAt : 0,
                        updatedAt: typeof w.updatedAt === 'number' ? w.updatedAt : 0,
                        deleted: !!w.deleted
                    });
                }
            }
        }
    }
    return Array.from(merged.values());
}

function buildPayload(transactions, categories, budgets, wallets) {
    const payload = {
        app: 'mizan-okane',
        schemaVersion: SCHEMA_VERSION,
        exportedAt: new Date().toISOString(),
        transactions
    };
    if (categories) {
        payload.categories = categories;
    }
    if (budgets) {
        payload.budgets = budgets;
    }
    if (wallets) {
        payload.wallets = wallets;
    }
    return JSON.stringify(payload);
}

async function uploadRemote(fileId, transactions, categories, budgets, wallets) {
    const content = buildPayload(transactions, categories, budgets, wallets);

    if (fileId) {
        const res = await driveFetch(
            `${DRIVE_UPLOAD}/files/${fileId}?uploadType=media&fields=id,version,modifiedTime`,
            { method: 'PATCH', headers: { 'Content-Type': 'application/json' }, body: content }
        );
        return res.json();
    }

    const boundary = `mizan-${crypto.randomUUID()}`;
    const metadata = { name: FILE_NAME, parents: ['appDataFolder'], mimeType: 'application/json' };
    const body =
        `--${boundary}\r\nContent-Type: application/json; charset=UTF-8\r\n\r\n${JSON.stringify(metadata)}\r\n` +
        `--${boundary}\r\nContent-Type: application/json\r\n\r\n${content}\r\n` +
        `--${boundary}--`;

    const res = await driveFetch(
        `${DRIVE_UPLOAD}/files?uploadType=multipart&fields=id,version,modifiedTime`,
        { method: 'POST', headers: { 'Content-Type': `multipart/related; boundary=${boundary}` }, body }
    );
    return res.json();
}

// --- Status sinkron lokal ---
// localRev naik setiap ada perubahan data lokal. syncedRev = localRev saat
// terakhir kali lokal dan Drive sama. Disimpan di key terpisah supaya
// markDirty tidak menimpa hasil sinkron yang sedang berjalan.

async function getLocalRev() {
    return (await getMeta('localRev')) || 0;
}

async function getSyncMeta() {
    return (await getMeta('sync')) || { account: null, syncedRev: null, remoteVersion: null, lastSyncAt: null };
}

async function saveSyncMeta(meta) {
    await setMeta('sync', meta);
}

// Aturan Tahap 4: Gabung per transaksi.
// - ID sama: waktu ubah (updatedAt) yang lebih baru menang.
// - ID hanya ada di satu sisi: ambil.
// - Soft delete (deleted: true) ikut digabung dan bisa menang jika updatedAt lebih baru.
// Aturan Tahap 5: Kategori, budget, dan dompet ikut digabung (yang terbaru menang berdasarkan updatedAt).
async function runSync(force = null) {
    const account = getAccount();
    let meta = await getSyncMeta();
    if (meta.account !== account?.email) {
        meta = { account: account?.email || null, syncedRev: null, remoteVersion: null, lastSyncAt: null };
    }

    const revAtStart = await getLocalRev();
    const local = await getAllTransactions();
    const localCategories = await getCategories();
    const localBudgets = await getBudgets();
    const localWallets = await getWallets();
    const dirty = meta.syncedRev === null
        ? (local.length > 0 || (localCategories.updatedAt || 0) > 0 || (localBudgets.updatedAt || 0) > 0 || localWallets.some(w => (w.updatedAt || 0) > 0))
        : meta.syncedRev !== revAtStart;

    const remoteFile = await findRemoteFile();

    const push = async (categoriesToPush = localCategories, budgetsToPush = localBudgets, walletsToPush = localWallets) => {
        const saved = await uploadRemote(remoteFile?.id || null, local, categoriesToPush, budgetsToPush, walletsToPush);
        return { ...meta, syncedRev: revAtStart, remoteVersion: saved.version, lastSyncAt: Date.now() };
    };

    const pull = async (remoteData, version) => {
        await replaceAllTransactions(remoteData.transactions);
        if (remoteData.categories && Array.isArray(remoteData.categories.income) && Array.isArray(remoteData.categories.expense)) {
            if ((remoteData.categories.updatedAt || 0) > (localCategories.updatedAt || 0)) {
                await saveCategories(remoteData.categories);
            }
        }
        if (remoteData.budgets && typeof remoteData.budgets.items === 'object') {
            if ((remoteData.budgets.updatedAt || 0) > (localBudgets.updatedAt || 0)) {
                await saveBudgets(remoteData.budgets);
            }
        }
        if (remoteData.wallets && Array.isArray(remoteData.wallets)) {
            const mergedWallets = mergeWallets(localWallets, remoteData.wallets);
            await saveWallets(mergedWallets);
        }
        await listeners.onRemoteApplied();
        return { ...meta, syncedRev: revAtStart, remoteVersion: version, lastSyncAt: Date.now() };
    };

    if (force === 'local') {
        meta = await push(localCategories, localBudgets, localWallets);
    } else if (force === 'remote') {
        if (!remoteFile) throw new Error('File di Drive sudah tidak ada. Ketuk Sinkron untuk mengunggah data perangkat ini.');
        const remoteData = await downloadRemote(remoteFile.id);
        if (remoteData.categories && Array.isArray(remoteData.categories.income) && Array.isArray(remoteData.categories.expense)) {
            await saveCategories(remoteData.categories);
        }
        if (remoteData.budgets && typeof remoteData.budgets.items === 'object') {
            await saveBudgets(remoteData.budgets);
        }
        if (remoteData.wallets && Array.isArray(remoteData.wallets)) {
            const mergedWallets = mergeWallets(localWallets, remoteData.wallets);
            await saveWallets(mergedWallets);
        }
        meta = await pull(remoteData, remoteFile.version);
    } else if (!remoteFile) {
        meta = await push(localCategories, localBudgets, localWallets);
    } else if (remoteFile.version === meta.remoteVersion) {
        if (dirty) {
            meta = await push(localCategories, localBudgets, localWallets);
        } else {
            meta = { ...meta, lastSyncAt: Date.now() };
        }
    } else {
        const remoteData = await downloadRemote(remoteFile.id);
        const remoteHasCats = remoteData.categories && Array.isArray(remoteData.categories.income) && Array.isArray(remoteData.categories.expense);
        const remoteCatTime = remoteHasCats ? (remoteData.categories.updatedAt || 0) : -1;
        const localCatTime = localCategories.updatedAt || 0;
        const remoteCatWins = remoteHasCats && remoteCatTime > localCatTime;
        const finalCategories = remoteCatWins ? remoteData.categories : localCategories;

        const remoteHasBudgets = remoteData.budgets && typeof remoteData.budgets.items === 'object';
        const remoteBudgetTime = remoteHasBudgets ? (remoteData.budgets.updatedAt || 0) : -1;
        const localBudgetTime = localBudgets.updatedAt || 0;
        const remoteBudgetWins = remoteHasBudgets && remoteBudgetTime > localBudgetTime;
        const finalBudgets = remoteBudgetWins ? remoteData.budgets : localBudgets;

        const finalWallets = mergeWallets(localWallets, remoteData.wallets);
        const remoteWalletsNewer = Array.isArray(remoteData.wallets) && remoteData.wallets.some(rw => {
            const lw = localWallets.find(w => w.id === rw.id);
            return !lw || (rw.updatedAt || 0) > (lw.updatedAt || 0);
        });

        if (!dirty) {
            if (remoteCatWins) {
                await saveCategories(remoteData.categories);
            }
            if (remoteBudgetWins) {
                await saveBudgets(remoteData.budgets);
            }
            if (Array.isArray(remoteData.wallets)) {
                await saveWallets(finalWallets);
            }
            meta = await pull(remoteData, remoteFile.version);
        } else if (local.length === 0 && !remoteCatWins && !remoteBudgetWins && !remoteWalletsNewer) {
            const saved = await uploadRemote(remoteFile.id, remoteData.transactions, finalCategories, finalBudgets, finalWallets);
            await replaceAllTransactions(remoteData.transactions);
            await saveCategories(finalCategories);
            await saveBudgets(finalBudgets);
            await saveWallets(finalWallets);
            await listeners.onRemoteApplied();
            meta = { ...meta, syncedRev: revAtStart, remoteVersion: saved.version, lastSyncAt: Date.now() };
        } else if (local.length === 0) {
            if (remoteCatWins) {
                await saveCategories(remoteData.categories);
            }
            if (remoteBudgetWins) {
                await saveBudgets(remoteData.budgets);
            }
            if (Array.isArray(remoteData.wallets)) {
                await saveWallets(finalWallets);
            }
            meta = await pull(remoteData, remoteFile.version);
        } else if (remoteData.transactions.length === 0) {
            meta = await push(finalCategories, finalBudgets, finalWallets);
        } else {
            // Gabung data
            const merged = new Map();
            for (const tx of remoteData.transactions) {
                merged.set(tx.id, tx);
            }
            for (const tx of local) {
                const existing = merged.get(tx.id);
                if (!existing || (tx.updatedAt || 0) > (existing.updatedAt || 0)) {
                    merged.set(tx.id, tx);
                }
            }
            const mergedList = Array.from(merged.values());
            
            // Simpan hasil gabungan ke Drive
            const saved = await uploadRemote(remoteFile.id, mergedList, finalCategories, finalBudgets, finalWallets);
            // Simpan ke lokal
            await replaceAllTransactions(mergedList);
            await saveCategories(finalCategories);
            await saveBudgets(finalBudgets);
            await saveWallets(finalWallets);
            await listeners.onRemoteApplied();
            
            meta = { ...meta, syncedRev: revAtStart, remoteVersion: saved.version, lastSyncAt: Date.now() };
        }
    }

    await saveSyncMeta(meta);
    return meta;
}

// --- API untuk app.js ---

let firstTapListenerActive = false;
export let removeFirstTapListener = () => {};

export function setupFirstTapListener() {
    if (firstTapListenerActive || typeof window === 'undefined') return;
    firstTapListenerActive = true;

    const onFirstTap = (event) => {
        // Jangan jalankan jika pengguna menekan tombol logout atau login
        if (event.target.closest('#btn-logout') || event.target.closest('#btn-login')) {
            return;
        }

        removeFirstTapListener();

        // Tombol sinkron sudah punya handler sendiri
        if (event.target.closest('#btn-sync-now')) {
            return;
        }

        if (isLinked() && !tokenValid() && navigator.onLine) {
            ensureTokenAndSync();
        }
    };

    window.addEventListener('click', onFirstTap, { capture: true });
    removeFirstTapListener = () => {
        window.removeEventListener('click', onFirstTap, { capture: true });
        firstTapListenerActive = false;
    };
}

async function ensureTokenAndSync() {
    if (!tokenValid() && navigator.onLine) {
        requestToken();
        setState({ status: 'connecting', message: '' });
    }
    await syncNow();
}

export async function syncNow({ interactive = false, force = null } = {}) {
    if (!isLinked()) {
        setState({ status: 'signed-out', email: null, message: '' });
        return;
    }

    // Minta token sebelum await apa pun supaya popup tetap dianggap aksi pengguna.
    let tokenPromise = null;
    if (!tokenValid() && interactive && navigator.onLine) {
        tokenPromise = requestToken();
    }

    if (!navigator.onLine) {
        setState({ status: 'offline', message: '' });
        return;
    }
    if (syncing) {
        if (tokenPromise) {
            tokenPromise.then(() => syncNow()).catch(() => {});
        } else {
            syncAgain = true;
        }
        return;
    }

    syncing = true;
    setState({ status: 'syncing', message: '' });

    try {
        if (tokenPromise) {
            await tokenPromise;
        } else if (pendingToken) {
            await pendingToken.promise;
        } else if (!tokenValid()) {
            throw new AuthRequiredError('Sesi Google berakhir.');
        }
        const meta = await runSync(force);
        setState({ status: 'synced', lastSyncAt: meta.lastSyncAt, message: '' });
    } catch (err) {
        if (err instanceof SupersededError) {
            // Permintaan login yang lebih baru sedang berjalan dan akan memicu sinkron sendiri.
        } else if (err instanceof ConflictError) {
            setState({ status: 'conflict', message: '' });
        } else if (err instanceof AuthRequiredError) {
            setState({ status: 'needs-login', message: err.message });
            setupFirstTapListener();
        } else {
            console.error('Sinkron gagal', err);
            setState({ status: navigator.onLine ? 'error' : 'offline', message: err.message });
        }
    } finally {
        syncing = false;
        if (syncAgain) {
            syncAgain = false;
            syncNow();
        }
    }
}

export async function signIn() {
    // requestToken dipanggil sinkron di dalam handler klik.
    const tokenPromise = requestToken();
    setState({ status: 'connecting', message: '' });

    try {
        await tokenPromise;
        const email = await fetchEmail();
        setAccount({ email });
        cachedLoginHint = email || '';
        isGoogleLinked = true;
        await setMeta(META_KEY_LINKED, true);
        setState({ email });
    } catch (err) {
        if (err instanceof SupersededError) return;
        setState({ status: isLinked() ? 'needs-login' : 'signed-out', message: err.message });
        return;
    }
    await syncNow();
}

export async function signOut() {
    // Token dibuang dari memori dan akun dilupakan. Data lokal tidak disentuh.
    accessToken = null;
    tokenExpiresAt = 0;
    clearTimeout(debounceTimer);
    setAccount(null);
    cachedLoginHint = '';
    isGoogleLinked = false;
    removeFirstTapListener();
    await deleteMeta(META_KEY_LINKED);
    setState({ status: 'signed-out', email: null, lastSyncAt: null, message: '' });
}

// Dipanggil setiap kali data lokal berubah (tambah, edit, hapus).
export async function markDirty() {
    await setMeta('localRev', (await getLocalRev()) + 1);
    if (!isLinked()) return;

    if (!navigator.onLine) {
        setState({ status: 'offline', message: '' });
        return;
    }
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(() => syncNow(), SYNC_DEBOUNCE_MS);
}

export async function initSync({ onState, onRemoteApplied }) {
    listeners = { onState, onRemoteApplied };

    window.addEventListener('online', () => {
        if (isLinked()) {
            if (tokenValid()) {
                syncNow();
            } else {
                setState({ status: 'standby', message: 'Ketuk di mana saja untuk lanjut sinkron.' });
                setupFirstTapListener();
            }
        }
    });
    window.addEventListener('offline', () => {
        if (isLinked()) setState({ status: 'offline', message: '' });
    });
    document.addEventListener('visibilitychange', () => {
        if (document.visibilityState === 'visible' && isLinked()) {
            if (tokenValid()) {
                syncNow();
            } else {
                setState({ status: 'standby', message: 'Ketuk di mana saja untuk lanjut sinkron.' });
                setupFirstTapListener();
            }
        }
    });

    const marker = await getMeta(META_KEY_LINKED);
    const account = getAccount();
    const hasMarker = !!marker || !!account;
    isGoogleLinked = hasMarker;

    if (!hasMarker) {
        setState({ status: 'signed-out', email: null, message: '' });
        if (navigator.onLine) loadGis().catch(() => {});
        return;
    }

    if (!marker && account) {
        await setMeta(META_KEY_LINKED, true);
    }

    const meta = await getSyncMeta();
    const email = account?.email || meta?.account || null;
    cachedLoginHint = email || '';
    setState({ email, lastSyncAt: meta.account === email ? meta.lastSyncAt : null });

    if (!navigator.onLine) {
        setState({ status: 'offline', message: '' });
        return;
    }

    // Preload GIS di latar belakang supaya siap saat ada ketukan pengguna
    loadGis().catch(() => {});

    // Saat aplikasi dibuka: jangan meminta token otomatis yang membuka jendela.
    // Tampilkan status netral dan siapkan listener ketukan pertama.
    setState({ status: 'standby', message: 'Ketuk di mana saja untuk lanjut sinkron.' });
    setupFirstTapListener();
}

