import { initDB, saveTransaction, getAllTransactions } from './db.js';
import { initSync, markDirty, signIn, signOut, syncNow, ensureToken } from './sync.js';

// DOM Elements
const formTitle = document.getElementById('form-title');
const txForm = document.getElementById('tx-form');
const txIdInput = document.getElementById('tx-id');
const txTypeInputs = document.getElementsByName('type');
const txDateInput = document.getElementById('tx-date');
const txTimeInput = document.getElementById('tx-time');
const txAmountInput = document.getElementById('tx-amount');
const txCategoryInput = document.getElementById('tx-category');
const txNoteInput = document.getElementById('tx-note');
const btnCancel = document.getElementById('btn-cancel');

const txListContainer = document.getElementById('tx-list');
const totalBalanceEl = document.getElementById('total-balance');
const totalIncomeEl = document.getElementById('total-income');
const totalExpenseEl = document.getElementById('total-expense');

const confirmDialog = document.getElementById('confirm-dialog');
const btnConfirmCancel = document.getElementById('btn-confirm-cancel');
const btnConfirmDelete = document.getElementById('btn-confirm-delete');

const chartContainer = document.getElementById('chart-container');
const chartSummaryText = document.getElementById('chart-summary-text');
const chartDetail = document.getElementById('chart-detail');

let currentTransactions = [];
let pendingDeleteId = null;

// Formatter
const currencyFormatter = new Intl.NumberFormat('id-ID', {
    style: 'currency',
    currency: 'IDR',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0
});

function formatDate(dateString) {
    const options = { day: 'numeric', month: 'short', year: 'numeric' };
    return new Date(dateString).toLocaleDateString('id-ID', options);
}

function formatDateTime(tx) {
    const dateFormatted = formatDate(tx.date);
    return tx.time ? `${dateFormatted} ${tx.time}` : dateFormatted;
}

function getCurrentTimeString() {
    const now = new Date();
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    return `${hours}:${minutes}`;
}

async function loadData() {
    try {
        const allTx = await getAllTransactions();
        currentTransactions = allTx.filter(tx => !tx.deleted);
        // Sort by date descending, then by time descending, then by createdAt descending
        currentTransactions.sort((a, b) => {
            if (a.date !== b.date) {
                return a.date > b.date ? -1 : 1;
            }
            const timeA = a.time || '';
            const timeB = b.time || '';
            if (timeA !== timeB) {
                return timeA > timeB ? -1 : 1;
            }
            return (b.createdAt || 0) - (a.createdAt || 0);
        });
        render();
    } catch (error) {
        console.error("Gagal memuat data", error);
        alert("Terjadi kesalahan saat memuat data.");
    }
}

const INDO_MONTHS_SHORT = [
    'Jan', 'Feb', 'Mar', 'Apr', 'Mei', 'Jun',
    'Jul', 'Agu', 'Sep', 'Okt', 'Nov', 'Des'
];
const INDO_MONTHS_FULL = [
    'Januari', 'Februari', 'Maret', 'April', 'Mei', 'Juni',
    'Juli', 'Agustus', 'September', 'Oktober', 'November', 'Desember'
];

function formatCompactCurrency(value) {
    if (value === 0) return '0';
    if (value >= 1_000_000_000) {
        const val = value / 1_000_000_000;
        return (val % 1 === 0 ? val.toFixed(0) : val.toFixed(1).replace('.', ',')) + ' M';
    }
    if (value >= 1_000_000) {
        const val = value / 1_000_000;
        return (val % 1 === 0 ? val.toFixed(0) : val.toFixed(1).replace('.', ',')) + ' jt';
    }
    if (value >= 1_000) {
        const val = value / 1_000;
        return (val % 1 === 0 ? val.toFixed(0) : val.toFixed(1).replace('.', ',')) + ' rb';
    }
    return String(value);
}

function getNiceCeiling(maxVal) {
    if (maxVal <= 0) return 100000;
    const target = maxVal * 1.15;
    const exp = Math.floor(Math.log10(target));
    const factor = Math.pow(10, exp);
    const fraction = target / factor;
    let niceFraction;
    if (fraction <= 1) niceFraction = 1;
    else if (fraction <= 2) niceFraction = 2;
    else if (fraction <= 2.5) niceFraction = 2.5;
    else if (fraction <= 5) niceFraction = 5;
    else niceFraction = 10;
    return niceFraction * factor;
}

function renderExpenseChart(transactions) {
    if (!chartContainer) return;

    // Filter hanya transaksi pengeluaran aktif (tidak terhapus) dengan jumlah > 0
    const expenseTx = (transactions || []).filter(tx => tx.type === 'expense' && !tx.deleted && tx.amount > 0);

    if (expenseTx.length === 0) {
        chartContainer.innerHTML = '<p class="chart-empty">Belum ada transaksi pengeluaran untuk ditampilkan.</p>';
        if (chartSummaryText) chartSummaryText.textContent = '';
        if (chartDetail) {
            chartDetail.textContent = '';
            chartDetail.classList.add('hidden');
        }
        return;
    }

    if (chartDetail) {
        chartDetail.classList.remove('hidden');
    }

    // Kelompokkan per bulan (YYYY-MM)
    const monthData = new Map();
    for (const tx of expenseTx) {
        if (!tx.date) continue;
        const match = tx.date.match(/^(\d{4})-(\d{1,2})/);
        if (!match) continue;
        const year = match[1];
        const month = String(parseInt(match[2], 10)).padStart(2, '0');
        const key = `${year}-${month}`;
        const item = monthData.get(key) || { total: 0, count: 0 };
        item.total += tx.amount;
        item.count += 1;
        monthData.set(key, item);
    }

    if (monthData.size === 0) {
        chartContainer.innerHTML = '<p class="chart-empty">Belum ada transaksi pengeluaran untuk ditampilkan.</p>';
        if (chartSummaryText) chartSummaryText.textContent = '';
        if (chartDetail) {
            chartDetail.textContent = '';
            chartDetail.classList.add('hidden');
        }
        return;
    }

    const rawKeys = Array.from(monthData.keys()).sort();
    const minKey = rawKeys[0];
    const maxKey = rawKeys[rawKeys.length - 1];

    const [minY, minM] = minKey.split('-').map(Number);
    const [maxY, maxM] = maxKey.split('-').map(Number);
    const span = (maxY - minY) * 12 + (maxM - minM) + 1;

    let displayMonths = [];
    if (span <= 12) {
        let curY = minY;
        let curM = minM;
        while (curY < maxY || (curY === maxY && curM <= maxM)) {
            displayMonths.push(`${curY}-${String(curM).padStart(2, '0')}`);
            curM++;
            if (curM > 12) {
                curM = 1;
                curY++;
            }
        }
    } else {
        displayMonths = rawKeys;
    }

    let totalExpenseAll = 0;
    let maxMonthExpense = 0;
    let monthsWithExpense = 0;

    for (const key of displayMonths) {
        const item = monthData.get(key);
        const amt = item ? item.total : 0;
        totalExpenseAll += amt;
        if (amt > maxMonthExpense) {
            maxMonthExpense = amt;
        }
        if (amt > 0) {
            monthsWithExpense++;
        }
    }

    const avgExpense = monthsWithExpense > 0 ? Math.round(totalExpenseAll / monthsWithExpense) : 0;
    if (chartSummaryText) {
        chartSummaryText.textContent = `Rata-rata: ${currencyFormatter.format(avgExpense)} / bulan`;
    }

    const ceiling = getNiceCeiling(maxMonthExpense);
    const midCeiling = ceiling / 2;

    const svgHeight = 220;
    const paddingTop = 26;
    const paddingBottom = 34;
    const paddingLeft = 56;
    const paddingRight = 16;
    const plotHeight = svgHeight - paddingTop - paddingBottom;
    const baselineY = paddingTop + plotHeight;

    const n = displayMonths.length;
    const isScrollable = n > 6;
    const slotWidth = isScrollable ? 56 : (520 - paddingLeft - paddingRight) / n;
    const svgWidth = isScrollable ? (paddingLeft + paddingRight + n * slotWidth) : 520;

    function updateDetail(key) {
        if (!chartDetail) return;
        const [yStr, mStr] = key.split('-');
        const mNum = parseInt(mStr, 10);
        const yNum = parseInt(yStr, 10);
        const fullLabel = `${INDO_MONTHS_FULL[mNum - 1]} ${yNum}`;
        const item = monthData.get(key);
        const total = item ? item.total : 0;
        const count = item ? item.count : 0;

        chartDetail.innerHTML = `
            <span class="chart-detail-month">${fullLabel}:</span>
            <strong class="chart-detail-amount">${currencyFormatter.format(total)}</strong>
            <span class="chart-detail-count">${count > 0 ? `(${count} transaksi)` : '(tidak ada pengeluaran)'}</span>
        `;
    }

    const latestKey = displayMonths[displayMonths.length - 1];
    updateDetail(latestKey);

    let svgHtml = `
        <svg class="expense-chart"
             viewBox="0 0 ${svgWidth} ${svgHeight}"
             width="${isScrollable ? svgWidth + 'px' : '100%'}"
             height="${isScrollable ? svgHeight + 'px' : 'auto'}"
             role="img"
             aria-label="Grafik total pengeluaran per bulan">
            <line class="chart-grid-line" x1="${paddingLeft}" y1="${paddingTop}" x2="${svgWidth - paddingRight}" y2="${paddingTop}" />
            <text class="chart-axis-label" x="${paddingLeft - 8}" y="${paddingTop + 4}" text-anchor="end">${formatCompactCurrency(ceiling)}</text>

            <line class="chart-grid-line" x1="${paddingLeft}" y1="${paddingTop + plotHeight / 2}" x2="${svgWidth - paddingRight}" y2="${paddingTop + plotHeight / 2}" />
            <text class="chart-axis-label" x="${paddingLeft - 8}" y="${paddingTop + plotHeight / 2 + 4}" text-anchor="end">${formatCompactCurrency(midCeiling)}</text>

            <line class="chart-axis-line" x1="${paddingLeft}" y1="${baselineY}" x2="${svgWidth - paddingRight}" y2="${baselineY}" />
            <text class="chart-axis-label" x="${paddingLeft - 8}" y="${baselineY + 4}" text-anchor="end">0</text>
    `;

    displayMonths.forEach((key, index) => {
        const cx = paddingLeft + (index + 0.5) * slotWidth;
        const barWidth = Math.min(36, Math.max(14, slotWidth * 0.52));
        const barX = cx - barWidth / 2;
        const item = monthData.get(key);
        const amount = item ? item.total : 0;
        const rawBarHeight = ceiling > 0 ? (amount / ceiling) * plotHeight : 0;
        const barHeight = amount > 0 ? Math.max(3, rawBarHeight) : 0;
        const barY = baselineY - barHeight;

        const [yStr, mStr] = key.split('-');
        const mNum = parseInt(mStr, 10);
        const shortName = INDO_MONTHS_SHORT[mNum - 1];
        const shortYear = yStr.slice(2);
        const shortLabel = `${shortName} '${shortYear}`;
        const fullLabel = `${INDO_MONTHS_FULL[mNum - 1]} ${yStr}`;
        const isSelected = key === latestKey;

        const labelY = Math.max(16, barY - 6);
        const topLabel = amount > 0 ? formatCompactCurrency(amount) : '';

        svgHtml += `
            <g class="chart-bar-group ${isSelected ? 'is-selected' : ''}"
               data-key="${key}"
               tabindex="0"
               role="button"
               aria-label="${fullLabel}: ${currencyFormatter.format(amount)}">
                <rect class="chart-hit-area" x="${paddingLeft + index * slotWidth}" y="0" width="${slotWidth}" height="${svgHeight}" fill="transparent" />
                <rect class="chart-bar" x="${barX}" y="${barY}" width="${barWidth}" height="${barHeight}" rx="3" ry="3" />
                ${topLabel ? `<text class="chart-bar-value" x="${cx}" y="${labelY}" text-anchor="middle">${topLabel}</text>` : ''}
                <text class="chart-month-label" x="${cx}" y="${baselineY + 18}" text-anchor="middle">${shortLabel}</text>
            </g>
        `;
    });

    svgHtml += `</svg>`;
    chartContainer.innerHTML = svgHtml;

    const groups = chartContainer.querySelectorAll('.chart-bar-group');
    groups.forEach(group => {
        const onSelect = () => {
            groups.forEach(g => g.classList.remove('is-selected'));
            group.classList.add('is-selected');
            updateDetail(group.dataset.key);
        };

        group.addEventListener('click', onSelect);
        group.addEventListener('mouseenter', onSelect);
        group.addEventListener('focus', onSelect);
        group.addEventListener('keydown', (e) => {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                onSelect();
            }
        });
    });

    if (isScrollable) {
        requestAnimationFrame(() => {
            chartContainer.scrollLeft = chartContainer.scrollWidth;
        });
    }
}

function render() {
    txListContainer.innerHTML = '';
    
    let totalIncome = 0;
    let totalExpense = 0;

    currentTransactions.forEach(tx => {
        if (tx.type === 'income') {
            totalIncome += tx.amount;
        } else {
            totalExpense += tx.amount;
        }

        const txEl = document.createElement('div');
        txEl.className = 'tx-item';
        
        const sign = tx.type === 'income' ? '+' : '-';
        const amountClass = tx.type === 'income' ? 'income' : 'expense';

        txEl.innerHTML = `
            <div class="tx-info">
                <div class="tx-header">
                    <span class="tx-category">${tx.category}</span>
                    <span class="tx-date">${formatDateTime(tx)}</span>
                </div>
                ${tx.note ? `<div class="tx-note">${tx.note}</div>` : ''}
                <div class="tx-amount ${amountClass}">${sign} ${currencyFormatter.format(tx.amount)}</div>
            </div>
            <div class="tx-actions">
                <button class="btn-icon btn-edit" data-id="${tx.id}">Edit</button>
                <button class="btn-icon btn-delete" data-id="${tx.id}">Hapus</button>
            </div>
        `;
        
        txListContainer.appendChild(txEl);
    });

    const totalBalance = totalIncome - totalExpense;
    totalBalanceEl.textContent = currencyFormatter.format(totalBalance);
    totalIncomeEl.textContent = currencyFormatter.format(totalIncome);
    totalExpenseEl.textContent = currencyFormatter.format(totalExpense);

    renderExpenseChart(currentTransactions);

    // Attach event listeners to generated buttons
    document.querySelectorAll('.btn-edit').forEach(btn => {
        btn.addEventListener('click', (e) => handleEdit(e.target.dataset.id));
    });
    document.querySelectorAll('.btn-delete').forEach(btn => {
        btn.addEventListener('click', (e) => handleDelete(e.target.dataset.id));
    });
}

function resetForm() {
    txForm.reset();
    txIdInput.value = '';
    // Default date to today
    const today = new Date();
    // Offset for local timezone
    const localToday = new Date(today.getTime() - (today.getTimezoneOffset() * 60000)).toISOString().split('T')[0];
    txDateInput.value = localToday;
    txTimeInput.value = getCurrentTimeString();
    
    formTitle.textContent = 'Tambah Transaksi';
    btnCancel.classList.add('hidden');
    // Default to expense
    txTypeInputs[0].checked = true;
}

async function handleSubmit(e) {
    e.preventDefault();

    const amount = parseInt(txAmountInput.value, 10);
    if (isNaN(amount) || amount <= 0) {
        alert("Jumlah harus berupa angka lebih dari 0.");
        return;
    }

    // Minta token dalam handler interaksi pengguna bila token sudah habis
    ensureToken();

    let selectedType = 'expense';
    for (const radio of txTypeInputs) {
        if (radio.checked) {
            selectedType = radio.value;
            break;
        }
    }

    const now = Date.now();
    const id = txIdInput.value || crypto.randomUUID();
    
    // Check if it's an update to preserve createdAt
    let createdAt = now;
    if (txIdInput.value) {
        const existingTx = currentTransactions.find(t => t.id === txIdInput.value);
        if (existingTx) {
            createdAt = existingTx.createdAt;
        }
    }

    const tx = {
        id,
        type: selectedType,
        amount,
        category: txCategoryInput.value.trim(),
        date: txDateInput.value,
        note: txNoteInput.value.trim(),
        createdAt,
        updatedAt: now,
        deleted: false
    };

    const timeValue = txTimeInput.value.trim();
    if (timeValue) {
        tx.time = timeValue;
    }

    try {
        await saveTransaction(tx);
        resetForm();
        await loadData();
        markDirty().catch(err => console.error("Gagal menandai perubahan untuk sinkron", err));
    } catch (error) {
        console.error("Gagal menyimpan transaksi", error);
        alert("Terjadi kesalahan saat menyimpan data.");
    }
}

function handleEdit(id) {
    const tx = currentTransactions.find(t => t.id === id);
    if (!tx) return;

    txIdInput.value = tx.id;
    for (const radio of txTypeInputs) {
        radio.checked = (radio.value === tx.type);
    }
    txDateInput.value = tx.date;
    txTimeInput.value = tx.time || '';
    txAmountInput.value = tx.amount;
    txCategoryInput.value = tx.category;
    txNoteInput.value = tx.note || '';

    formTitle.textContent = 'Edit Transaksi';
    btnCancel.classList.remove('hidden');
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

function handleDelete(id) {
    if (!id) return;
    pendingDeleteId = id;
    if (confirmDialog && typeof confirmDialog.showModal === 'function') {
        confirmDialog.showModal();
    }
}

async function performDelete(id) {
    // Minta token dalam handler interaksi pengguna bila token sudah habis
    ensureToken();

    try {
        const allTx = await getAllTransactions();
        const tx = allTx.find(t => t.id === id);
        
        if (tx) {
            tx.deleted = true;
            tx.updatedAt = Date.now();
            await saveTransaction(tx);
            await loadData();
            markDirty().catch(err => console.error("Gagal menandai perubahan untuk sinkron", err));
        }
    } catch (error) {
        console.error("Gagal menghapus transaksi", error);
        alert("Terjadi kesalahan saat menghapus data.");
    }
}

// Event Listeners
txForm.addEventListener('submit', handleSubmit);
btnCancel.addEventListener('click', resetForm);

if (confirmDialog) {
    btnConfirmCancel.addEventListener('click', () => {
        confirmDialog.close();
    });

    btnConfirmDelete.addEventListener('click', async () => {
        const id = pendingDeleteId;
        if (!id) {
            confirmDialog.close();
            return;
        }

        btnConfirmDelete.disabled = true;
        try {
            await performDelete(id);
        } finally {
            btnConfirmDelete.disabled = false;
            confirmDialog.close();
        }
    });

    confirmDialog.addEventListener('close', () => {
        pendingDeleteId = null;
    });

    // Fallback ketukan di luar kotak untuk browser yang belum mendukung closedby="any"
    if (!('closedBy' in HTMLDialogElement.prototype)) {
        confirmDialog.addEventListener('click', (event) => {
            if (event.target !== confirmDialog) return;
            const rect = confirmDialog.getBoundingClientRect();
            const isInside = (
                rect.top <= event.clientY &&
                event.clientY <= rect.top + rect.height &&
                rect.left <= event.clientX &&
                event.clientX <= rect.left + rect.width
            );
            if (!isInside) {
                confirmDialog.close();
            }
        });
    }
}

// --- Sinkron Google Drive ---
const syncStatusEl = document.getElementById('sync-status');
const btnLogin = document.getElementById('btn-login');
const btnLogout = document.getElementById('btn-logout');
const btnSyncNow = document.getElementById('btn-sync-now');
const btnExport = document.getElementById('btn-export');
const btnExportCsv = document.getElementById('btn-export-csv');

function formatTime(ts) {
    return new Date(ts).toLocaleTimeString('id-ID', { hour: '2-digit', minute: '2-digit' });
}

function renderSyncState(s) {
    const linked = !!s.email && s.status !== 'signed-out';
    const busy = s.status === 'syncing';
    const who = s.email ? ` (${s.email})` : '';

    const views = {
        'signed-out': ['', 'Belum login. Data hanya tersimpan di perangkat ini.'],
        'connecting': ['', 'Menyambung ke Google...'],
        'standby': ['', s.message || 'Ketuk di mana saja untuk lanjut sinkron.'],
        'waiting-tap': ['', s.message || 'Ketuk di mana saja untuk lanjut sinkron.'],
        'syncing': ['', `Menyinkronkan dengan Drive${who}...`],
        'synced': ['ok', `Tersinkron dengan Drive${who}${s.lastSyncAt ? `, pukul ${formatTime(s.lastSyncAt)}` : ''}.`],
        'offline': ['warn', 'Offline. Perubahan tetap tersimpan di perangkat dan dikirim saat online lagi.'],
        'needs-login': ['warn', `${s.message || 'Sesi Google berakhir.'} Ketuk Sinkron untuk lanjut.`],
        'error': ['error', `Gagal sinkron. ${s.message || ''} Data di perangkat aman, ketuk Sinkron untuk coba lagi.`]
    };
    const [tone, text] = views[s.status] || views['signed-out'];

    // Pesan gagal login saat belum terhubung tetap ditampilkan.
    syncStatusEl.textContent = s.status === 'signed-out' && s.message ? `${s.message} ${text}` : text;
    syncStatusEl.dataset.tone = s.status === 'signed-out' && s.message ? 'error' : tone;

    btnLogin.classList.toggle('hidden', linked);
    btnLogin.disabled = busy;
    btnLogout.classList.toggle('hidden', !linked);
    btnSyncNow.classList.toggle('hidden', !linked);
    btnSyncNow.disabled = busy;
}

btnLogin.addEventListener('click', () => {
    if (!navigator.onLine) {
        renderSyncState({ status: 'signed-out', message: 'Perlu koneksi internet untuk login.' });
        return;
    }
    signIn();
});

btnLogout.addEventListener('click', async () => {
    if (confirm('Logout dari Google? Data di perangkat ini tetap ada, tapi tidak disinkronkan lagi sampai login ulang.')) {
        await signOut();
    }
});

btnSyncNow.addEventListener('click', () => syncNow({ interactive: true }));

btnExport.addEventListener('click', async () => {
    try {
        const allTx = await getAllTransactions();
        const data = {
            app: 'mizan-okane',
            schemaVersion: 1,
            exportedAt: new Date().toISOString(),
            transactions: allTx
        };
        const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `mizan-okane-backup-${new Date().toISOString().split('T')[0]}.json`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(url);
    } catch (error) {
        console.error("Gagal mengekspor data", error);
        alert("Terjadi kesalahan saat mengekspor data.");
    }
});

function escapeCSVField(value) {
    if (value === null || value === undefined) {
        return '';
    }
    const str = String(value);
    if (str.includes('"') || str.includes(',') || str.includes('\n') || str.includes('\r')) {
        return `"${str.replace(/"/g, '""')}"`;
    }
    return str;
}

function getLocalDateString() {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

btnExportCsv.addEventListener('click', async () => {
    try {
        const allTx = await getAllTransactions();
        const activeTx = allTx.filter(tx => !tx.deleted);

        if (activeTx.length === 0) {
            alert('Belum ada transaksi untuk diekspor.');
            return;
        }

        // Urutkan dari tanggal terbaru, lalu jam terbaru, lalu createdAt terbaru
        activeTx.sort((a, b) => {
            if (a.date !== b.date) {
                return a.date > b.date ? -1 : 1;
            }
            const timeA = a.time || '';
            const timeB = b.time || '';
            if (timeA !== timeB) {
                return timeA > timeB ? -1 : 1;
            }
            return (b.createdAt || 0) - (a.createdAt || 0);
        });

        const headers = ['tanggal', 'jam', 'tipe', 'kategori', 'jumlah', 'catatan'];
        const rows = [headers.join(',')];

        for (const tx of activeTx) {
            const row = [
                escapeCSVField(tx.date),
                escapeCSVField(tx.time || ''),
                escapeCSVField(tx.type),
                escapeCSVField(tx.category),
                escapeCSVField(tx.amount),
                escapeCSVField(tx.note || '')
            ];
            rows.push(row.join(','));
        }

        const csvContent = rows.join('\r\n');
        const BOM = '\uFEFF';
        const blob = new Blob([BOM + csvContent], { type: 'text/csv;charset=utf-8;' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `mizan-okane-transaksi-${getLocalDateString()}.csv`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(url);
    } catch (error) {
        console.error("Gagal mengekspor CSV", error);
        alert("Terjadi kesalahan saat mengekspor CSV.");
    }
});

// Init
async function init() {
    try {
        await initDB();
        resetForm(); // Set default date
        await loadData();
    } catch (error) {
        console.error("Gagal inisialisasi database", error);
        alert("Browser Anda tidak mendukung penyimpanan lokal atau terjadi kesalahan.");
        return;
    }

    // Sinkron dimulai setelah data lokal tampil, jadi app tetap jalan walau offline atau belum login.
    initSync({ onState: renderSyncState, onRemoteApplied: loadData })
        .catch(err => console.error("Gagal memulai sinkron", err));
}

init();

// --- PWA & Service Worker ---
if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
        navigator.serviceWorker.register('./sw.js')
            .then(reg => console.log('Service Worker terdaftar!', reg))
            .catch(err => console.error('Service Worker gagal mendaftar!', err));
    });
}

// Install Prompt Logic
let deferredPrompt;
const btnInstall = document.getElementById('btn-install');

window.addEventListener('beforeinstallprompt', (e) => {
    e.preventDefault();
    deferredPrompt = e;
    if (btnInstall) {
        btnInstall.classList.remove('hidden');
    }
});

if (btnInstall) {
    btnInstall.addEventListener('click', async () => {
        if (deferredPrompt) {
            deferredPrompt.prompt();
            const { outcome } = await deferredPrompt.userChoice;
            console.log(`Pilihan user untuk install: ${outcome}`);
            deferredPrompt = null;
            btnInstall.classList.add('hidden');
        }
    });
}

window.addEventListener('appinstalled', () => {
    console.log('Aplikasi berhasil diinstal');
    if (btnInstall) {
        btnInstall.classList.add('hidden');
    }
});

/* 
Catatan PWA untuk iOS:
Safari di iOS tidak mendukung event `beforeinstallprompt`.
Untuk "Add to Home Screen" di iPhone/iPad, pengguna harus membuka 
menu "Share" pada browser Safari, lalu memilih "Add to Home Screen".
*/
