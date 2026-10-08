// Sinkron Google Drive (appDataFolder) dengan Google Identity Services token flow.
// IndexedDB tetap sumber utama; modul ini hanya menyalin snapshot ke/dari Drive.
// Access token hanya disimpan di memori (variabel JS), tidak pernah ke storage.
import { getAllTransactions, replaceAllTransactions, getMeta, setMeta } from './db.js';

const CLIENT_ID = '663070681186-pjrnce0fcpjecvbf9o2fj99e59e3s0un.apps.googleusercontent.com';
const SCOPE = 'https://www.googleapis.com/auth/drive.appdata';
const GIS_SRC = 'https://accounts.google.com/gsi/client';
const DRIVE_API = 'https://www.googleapis.com/drive/v3';
const DRIVE_UPLOAD = 'https://www.googleapis.com/upload/drive/v3';
const FILE_NAME = 'mizan-okane-data.json';
const SCHEMA_VERSION = 1;
const SYNC_DEBOUNCE_MS = 1500;
const SILENT_TOKEN_TIMEOUT_MS = 15000;

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
    return !!getAccount();
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
        pendingToken.reject(new SupersededError());
        pendingToken = null;
    }

    let resolve, reject;
    const promise = new Promise((res, rej) => { resolve = res; reject = rej; });
    pendingToken = { promise, resolve, reject };

    const open = () => {
        const account = getAccount();
        tokenClient.requestAccessToken({ prompt: '', login_hint: account?.email || '' });
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

function buildPayload(transactions) {
    return JSON.stringify({
        app: 'mizan-okane',
        schemaVersion: SCHEMA_VERSION,
        exportedAt: new Date().toISOString(),
        transactions
    });
}

async function uploadRemote(fileId, transactions) {
    const content = buildPayload(transactions);

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
async function runSync(force = null) {
    const account = getAccount();
    let meta = await getSyncMeta();
    if (meta.account !== account?.email) {
        meta = { account: account?.email || null, syncedRev: null, remoteVersion: null, lastSyncAt: null };
    }

    const revAtStart = await getLocalRev();
    const local = await getAllTransactions();
    const dirty = meta.syncedRev === null ? local.length > 0 : meta.syncedRev !== revAtStart;

    const remoteFile = await findRemoteFile();

    const push = async () => {
        const saved = await uploadRemote(remoteFile?.id || null, local);
        return { ...meta, syncedRev: revAtStart, remoteVersion: saved.version, lastSyncAt: Date.now() };
    };

    const pull = async (remoteData, version) => {
        await replaceAllTransactions(remoteData.transactions);
        await listeners.onRemoteApplied();
        return { ...meta, syncedRev: revAtStart, remoteVersion: version, lastSyncAt: Date.now() };
    };

    if (force === 'local') {
        meta = await push();
    } else if (force === 'remote') {
        if (!remoteFile) throw new Error('File di Drive sudah tidak ada. Ketuk Sinkron untuk mengunggah data perangkat ini.');
        meta = await pull(await downloadRemote(remoteFile.id), remoteFile.version);
    } else if (!remoteFile) {
        meta = await push();
    } else if (remoteFile.version === meta.remoteVersion) {
        if (dirty) {
            meta = await push();
        } else {
            meta = { ...meta, lastSyncAt: Date.now() };
        }
    } else {
        const remoteData = await downloadRemote(remoteFile.id);
        if (!dirty || local.length === 0) {
            meta = await pull(remoteData, remoteFile.version);
        } else if (remoteData.transactions.length === 0) {
            meta = await push();
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
            const saved = await uploadRemote(remoteFile.id, mergedList);
            // Simpan ke lokal
            await replaceAllTransactions(mergedList);
            await listeners.onRemoteApplied();
            
            meta = { ...meta, syncedRev: revAtStart, remoteVersion: saved.version, lastSyncAt: Date.now() };
        }
    }

    await saveSyncMeta(meta);
    return meta;
}

// --- API untuk app.js ---

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
        setState({ email });
    } catch (err) {
        if (err instanceof SupersededError) return;
        setState({ status: isLinked() ? 'needs-login' : 'signed-out', message: err.message });
        return;
    }
    await syncNow();
}

export function signOut() {
    // Token dibuang dari memori dan akun dilupakan. Data lokal tidak disentuh.
    accessToken = null;
    tokenExpiresAt = 0;
    clearTimeout(debounceTimer);
    setAccount(null);
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
        if (isLinked()) syncNow();
    });
    window.addEventListener('offline', () => {
        if (isLinked()) setState({ status: 'offline', message: '' });
    });
    document.addEventListener('visibilitychange', () => {
        if (document.visibilityState === 'visible' && isLinked() && tokenValid()) {
            syncNow();
        }
    });

    const account = getAccount();
    if (!account) {
        setState({ status: 'signed-out', email: null });
        if (navigator.onLine) loadGis().catch(() => {});
        return;
    }

    const meta = await getSyncMeta();
    setState({ email: account.email, lastSyncAt: meta.account === account.email ? meta.lastSyncAt : null });

    if (!navigator.onLine) {
        setState({ status: 'offline' });
        return;
    }

    // Coba ambil token tanpa interaksi saat app dibuka. Kalau browser memblokir
    // popup atau tidak ada jawaban, status jadi "needs-login" dan pemilik cukup ketuk Sinkron.
    setState({ status: 'connecting', message: '' });
    try {
        await loadGis();
        await Promise.race([
            requestToken(),
            new Promise((_, reject) => setTimeout(
                () => reject(new AuthRequiredError('Ketuk Sinkron untuk menyambung ke Google.')),
                SILENT_TOKEN_TIMEOUT_MS
            ))
        ]);
    } catch (err) {
        if (err instanceof SupersededError || tokenValid()) return;
        setState({ status: 'needs-login', message: err.message });
        return;
    }
    await syncNow();
}
