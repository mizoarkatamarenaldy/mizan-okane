import { initDB, saveTransaction, getAllTransactions, getCategories, saveCategories, DEFAULT_CATEGORIES, getBudgets, saveBudgets, DEFAULT_BUDGETS, getWallets, saveWallets, DEFAULT_WALLET_ID, DEFAULT_WALLET_NAME, getTimeFormat, saveTimeFormat, DEFAULT_TIME_FORMAT } from './db.js';
import { initSync, markDirty, signIn, signOut, syncNow, ensureToken } from './sync.js';

// DOM Elements
const formTitle = document.getElementById('form-title');
const txForm = document.getElementById('tx-form');
const txIdInput = document.getElementById('tx-id');
const txTypeInputs = document.getElementsByName('type');
const txDateInput = document.getElementById('tx-date');
const txTimeInput = document.getElementById('tx-time');
const timeInput24Container = document.getElementById('time-input-24');
const timeInput12Container = document.getElementById('time-input-12');
const txTimeHourSelect = document.getElementById('tx-time-hour');
const txTimeMinuteSelect = document.getElementById('tx-time-minute');
const txTimeAmpmSelect = document.getElementById('tx-time-ampm');
const txTimeLabel = document.getElementById('tx-time-label');
const txTimeError = document.getElementById('tx-time-error');
const timeFormatRadios = document.getElementsByName('time-format-setting');
const txAmountInput = document.getElementById('tx-amount');
const txWalletLabel = document.getElementById('tx-wallet-label');
const txWalletInput = document.getElementById('tx-wallet');
const formGroupToWallet = document.getElementById('form-group-to-wallet');
const txToWalletInput = document.getElementById('tx-to-wallet');
const formGroupCategory = document.getElementById('form-group-category');
const txCategoryInput = document.getElementById('tx-category');
const txNoteInput = document.getElementById('tx-note');
const btnCancel = document.getElementById('btn-cancel');

const txListContainer = document.getElementById('tx-list');
const filterWalletSelect = document.getElementById('filter-wallet');
const totalBalanceEl = document.getElementById('total-balance');
const totalIncomeEl = document.getElementById('total-income');
const totalExpenseEl = document.getElementById('total-expense');
const walletBalancesContainer = document.getElementById('wallet-balances');

const confirmDialog = document.getElementById('confirm-dialog');
const confirmDialogTitle = document.getElementById('confirm-dialog-title');
const btnConfirmCancel = document.getElementById('btn-confirm-cancel');
const btnConfirmDelete = document.getElementById('btn-confirm-delete');

const chartSectionTitle = document.getElementById('chart-section-title');
const btnChartExpense = document.getElementById('btn-chart-expense');
const btnChartIncome = document.getElementById('btn-chart-income');
const chartContainer = document.getElementById('chart-container');
const chartSummaryText = document.getElementById('chart-summary-text');
const chartDetail = document.getElementById('chart-detail');

const donutSectionTitle = document.getElementById('donut-section-title');
const donutMonthLabel = document.getElementById('donut-month-label');
const btnDonutPrevMonth = document.getElementById('btn-donut-prev-month');
const donutMonthInput = document.getElementById('donut-month-input');
const btnDonutNextMonth = document.getElementById('btn-donut-next-month');
const btnDonutExpense = document.getElementById('btn-donut-expense');
const btnDonutIncome = document.getElementById('btn-donut-income');
const donutDetail = document.getElementById('donut-detail');
const donutContainer = document.getElementById('donut-container');

const budgetMonthLabel = document.getElementById('budget-month-label');
const btnBudgetPrevMonth = document.getElementById('btn-budget-prev-month');
const btnBudgetNextMonth = document.getElementById('btn-budget-next-month');
const budgetMonthInput = document.getElementById('budget-month-input');
const budgetSummaryBanner = document.getElementById('budget-summary-banner');
const budgetListContainer = document.getElementById('budget-list');

const expenseCategoryList = document.getElementById('expense-category-list');
const incomeCategoryList = document.getElementById('income-category-list');
const formAddExpenseCat = document.getElementById('form-add-expense-cat');
const inputAddExpenseCat = document.getElementById('input-add-expense-cat');
const formAddIncomeCat = document.getElementById('form-add-income-cat');
const inputAddIncomeCat = document.getElementById('input-add-income-cat');

const walletSettingsList = document.getElementById('wallet-settings-list');
const formAddWallet = document.getElementById('form-add-wallet');
const inputAddWallet = document.getElementById('input-add-wallet');

const budgetSettingsList = document.getElementById('budget-settings-list');
const formSetBudget = document.getElementById('form-set-budget');
const selectBudgetCat = document.getElementById('select-budget-cat');
const inputBudgetAmount = document.getElementById('input-budget-amount');

let currentTransactions = [];
let currentWallets = [];
let pendingDeleteId = null;
let pendingCategoryDelete = null;
let pendingBudgetDelete = null;
let pendingWalletDelete = null;
let editingCategory = null;
let editingBudgetCategory = null;
let editingWalletId = null;
let currentWalletFilter = 'all';
const LAST_WALLET_KEY = 'mizan-okane:last-wallet-id';
let currentTimeFormat = DEFAULT_TIME_FORMAT;
let currentChartType = 'expense';
let currentDonutType = 'expense';
let selectedDonutMonth = '';
let activeDonutCategory = null;

const DONUT_COLORS = [
    '#2563eb', // Biru
    '#e11d48', // Merah / Rose
    '#059669', // Hijau Zamrud
    '#d97706', // Jingga / Amber
    '#7c3aed', // Ungu / Violet
    '#0891b2', // Sian / Cyan
    '#db2777', // Merah Muda / Pink
    '#4f46e5', // Nila / Indigo
    '#65a30d', // Limau / Lime
    '#c026d3', // Fuchsia
    '#0d9488', // Teal
    '#ea580c'  // Oranye Tua
];

let selectedBudgetMonth = '';
let currentBudgets = {
    items: {},
    updatedAt: 0
};
let currentCategories = {
    income: [...DEFAULT_CATEGORIES.income],
    expense: [...DEFAULT_CATEGORIES.expense],
    updatedAt: 0
};

function escapeHTML(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

function getSelectedType() {
    for (const radio of txTypeInputs) {
        if (radio.checked) return radio.value;
    }
    return 'expense';
}

function populateCategoryOptions(selectedVal = null, oldCustomCategory = null) {
    if (!txCategoryInput) return;
    const type = getSelectedType();
    const list = (currentCategories && currentCategories[type]) ? currentCategories[type] : [];

    let html = '';
    if (oldCustomCategory && !list.includes(oldCustomCategory)) {
        html += `<option value="" disabled selected>Pilih kategori (sebelumnya: ${escapeHTML(oldCustomCategory)})</option>`;
    } else {
        html += `<option value="" disabled ${!selectedVal ? 'selected' : ''}>Pilih kategori</option>`;
    }

    for (const cat of list) {
        const isSelected = selectedVal === cat;
        html += `<option value="${escapeHTML(cat)}" ${isSelected ? 'selected' : ''}>${escapeHTML(cat)}</option>`;
    }

    txCategoryInput.innerHTML = html;

    if (selectedVal && list.includes(selectedVal)) {
        txCategoryInput.value = selectedVal;
    } else {
        txCategoryInput.value = '';
    }
}

function updateTypeUI(type) {
    const isTransfer = type === 'transfer';
    if (txWalletLabel) {
        txWalletLabel.textContent = isTransfer ? 'Dari Dompet' : 'Dompet';
    }
    if (formGroupToWallet) {
        formGroupToWallet.classList.toggle('hidden', !isTransfer);
    }
    if (txToWalletInput) {
        txToWalletInput.required = isTransfer;
    }
    if (formGroupCategory) {
        formGroupCategory.classList.toggle('hidden', isTransfer);
    }
    if (txCategoryInput) {
        txCategoryInput.required = !isTransfer;
    }

    if (isTransfer) {
        populateWalletOptions(txWalletInput ? txWalletInput.value : null, txToWalletInput ? txToWalletInput.value : null);
    } else {
        const currentVal = txCategoryInput ? txCategoryInput.value : '';
        const list = (currentCategories && currentCategories[type]) ? currentCategories[type] : [];
        const keepVal = list.includes(currentVal) ? currentVal : null;
        populateCategoryOptions(keepVal);
    }
}

function getActiveWallets() {
    return (currentWallets || []).filter(w => !w.deleted);
}

function getWalletName(walletId) {
    const id = walletId || DEFAULT_WALLET_ID;
    const found = (currentWallets || []).find(w => w.id === id);
    if (found && found.name) return found.name;
    if (id === DEFAULT_WALLET_ID) return DEFAULT_WALLET_NAME;
    return DEFAULT_WALLET_NAME;
}

function populateWalletOptions(selectedFromVal = null, selectedToVal = null) {
    if (!txWalletInput) return;
    const active = getActiveWallets();
    let targetId = selectedFromVal;
    if (!targetId || !active.some(w => w.id === targetId)) {
        const lastUsed = localStorage.getItem(LAST_WALLET_KEY);
        if (lastUsed && active.some(w => w.id === lastUsed)) {
            targetId = lastUsed;
        } else if (active.some(w => w.id === DEFAULT_WALLET_ID)) {
            targetId = DEFAULT_WALLET_ID;
        } else if (active.length > 0) {
            targetId = active[0].id;
        } else {
            targetId = DEFAULT_WALLET_ID;
        }
    }

    let html = '';
    // Jika dompet yang dipilih sebelumnya sudah di-soft-delete (misal saat edit transaksi lama)
    if (selectedFromVal && !active.some(w => w.id === selectedFromVal)) {
        const oldWalletName = getWalletName(selectedFromVal);
        html += `<option value="${escapeHTML(selectedFromVal)}" selected>${escapeHTML(oldWalletName)} (Dihapus)</option>`;
    }

    for (const w of active) {
        const isSelected = targetId === w.id;
        html += `<option value="${escapeHTML(w.id)}" ${isSelected ? 'selected' : ''}>${escapeHTML(w.name)}</option>`;
    }

    txWalletInput.innerHTML = html;
    if (targetId) {
        txWalletInput.value = targetId;
    }

    if (txToWalletInput) {
        let targetToId = selectedToVal;
        if (!targetToId || !active.some(w => w.id === targetToId) || targetToId === targetId) {
            const other = active.find(w => w.id !== targetId);
            if (other) {
                targetToId = other.id;
            } else {
                targetToId = '';
            }
        }

        let toHtml = `<option value="" disabled ${!targetToId ? 'selected' : ''}>Pilih dompet tujuan</option>`;
        if (selectedToVal && !active.some(w => w.id === selectedToVal)) {
            const oldWalletName = getWalletName(selectedToVal);
            toHtml += `<option value="${escapeHTML(selectedToVal)}" selected>${escapeHTML(oldWalletName)} (Dihapus)</option>`;
        }

        for (const w of active) {
            const isSelected = targetToId === w.id;
            toHtml += `<option value="${escapeHTML(w.id)}" ${isSelected ? 'selected' : ''}>${escapeHTML(w.name)}</option>`;
        }

        txToWalletInput.innerHTML = toHtml;
        if (targetToId) {
            txToWalletInput.value = targetToId;
        }
    }
}

function populateWalletFilterOptions() {
    if (!filterWalletSelect) return;
    const active = getActiveWallets();
    const currentVal = filterWalletSelect.value || currentWalletFilter || 'all';

    let html = `<option value="all" ${currentVal === 'all' ? 'selected' : ''}>Semua Dompet</option>`;
    let hasCurrent = currentVal === 'all';

    for (const w of active) {
        const isSelected = currentVal === w.id;
        if (isSelected) hasCurrent = true;
        html += `<option value="${escapeHTML(w.id)}" ${isSelected ? 'selected' : ''}>${escapeHTML(w.name)}</option>`;
    }

    filterWalletSelect.innerHTML = html;
    if (hasCurrent) {
        filterWalletSelect.value = currentVal;
        currentWalletFilter = currentVal;
    } else {
        filterWalletSelect.value = 'all';
        currentWalletFilter = 'all';
    }
}

// Formatter angka dan uang (titik pemisah ribuan)
export function formatNumber(val) {
    if (val === null || val === undefined || val === '') return '';
    const str = String(val);
    const isNegative = str.startsWith('-');
    let digits = str.replace(/\D/g, '');
    if (!digits) return '';
    digits = digits.replace(/^0+(?=\d)/, '');
    const formatted = digits.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
    return isNegative ? `-${formatted}` : formatted;
}

export function parseAmount(str) {
    if (str === null || str === undefined) return 0;
    const digits = String(str).replace(/\D/g, '');
    return digits ? parseInt(digits, 10) : 0;
}

export function formatCurrency(val) {
    if (val === null || val === undefined || val === '') return 'Rp 0';
    const num = typeof val === 'number' ? Math.round(val) : parseAmount(val);
    if (isNaN(num)) return 'Rp 0';
    if (num < 0) {
        return `-Rp ${formatNumber(Math.abs(num))}`;
    }
    return `Rp ${formatNumber(num)}`;
}

export const currencyFormatter = {
    format(val) {
        return formatCurrency(val);
    }
};

export function formatInputWithCursor(input) {
    if (!input) return;
    const originalValue = input.value;
    const originalCursor = input.selectionStart !== null ? input.selectionStart : originalValue.length;

    const textBeforeCursor = originalValue.slice(0, originalCursor);
    const digitsBeforeCursor = textBeforeCursor.replace(/\D/g, '').length;

    const rawDigits = originalValue.replace(/\D/g, '');
    if (!rawDigits) {
        input.value = '';
        return;
    }

    const cleanDigits = rawDigits.replace(/^0+(?=\d)/, '');
    const formatted = formatNumber(cleanDigits);
    input.value = formatted;

    if (digitsBeforeCursor === 0) {
        input.setSelectionRange(0, 0);
        return;
    }

    const targetDigits = Math.min(digitsBeforeCursor, cleanDigits.length);
    let digitCount = 0;
    let newCursor = formatted.length;

    for (let i = 0; i < formatted.length; i++) {
        if (/\d/.test(formatted[i])) {
            digitCount++;
            if (digitCount === targetDigits) {
                newCursor = i + 1;
                break;
            }
        }
    }

    input.setSelectionRange(newCursor, newCursor);
}

export function attachAmountInputFormatting(input) {
    if (!input) return;

    input.addEventListener('keydown', (e) => {
        if (
            e.key === 'Backspace' ||
            e.key === 'Delete' ||
            e.key === 'Tab' ||
            e.key === 'Escape' ||
            e.key === 'Enter' ||
            e.key === 'ArrowLeft' ||
            e.key === 'ArrowRight' ||
            e.key === 'ArrowUp' ||
            e.key === 'ArrowDown' ||
            e.key === 'Home' ||
            e.key === 'End' ||
            e.ctrlKey ||
            e.metaKey ||
            e.altKey
        ) {
            if (e.key === 'Backspace' && input.selectionStart === input.selectionEnd && input.selectionStart > 0) {
                if (input.value[input.selectionStart - 1] === '.') {
                    e.preventDefault();
                    const pos = input.selectionStart;
                    const val = input.value;
                    input.value = val.slice(0, pos - 2) + val.slice(pos - 1);
                    input.setSelectionRange(pos - 2, pos - 2);
                    formatInputWithCursor(input);
                }
            } else if (e.key === 'Delete' && input.selectionStart === input.selectionEnd && input.selectionStart < input.value.length) {
                if (input.value[input.selectionStart] === '.') {
                    e.preventDefault();
                    const pos = input.selectionStart;
                    const val = input.value;
                    input.value = val.slice(0, pos) + val.slice(pos + 2);
                    input.setSelectionRange(pos, pos);
                    formatInputWithCursor(input);
                }
            }
            return;
        }

        if (e.key.length === 1 && !/^\d$/.test(e.key)) {
            e.preventDefault();
        }
    });

    input.addEventListener('input', () => {
        formatInputWithCursor(input);
    });
}

function formatDate(dateString) {
    const options = { day: 'numeric', month: 'short', year: 'numeric' };
    return new Date(dateString).toLocaleDateString('id-ID', options);
}

export function formatTimeDisplay(timeStr, format = '24') {
    if (!timeStr) return '';
    const match = String(timeStr).trim().match(/^(\d{1,2}):(\d{2})$/);
    if (!match) return timeStr;
    const h24 = parseInt(match[1], 10);
    const m = match[2];
    if (format !== '12') {
        return `${String(h24).padStart(2, '0')}:${m}`;
    }
    const ampm = h24 >= 12 ? 'PM' : 'AM';
    let h12 = h24 % 12;
    if (h12 === 0) h12 = 12;
    return `${String(h12).padStart(2, '0')}:${m} ${ampm}`;
}

function formatDateTime(tx) {
    const dateFormatted = formatDate(tx.date);
    if (!tx.time) return dateFormatted;
    const timeFormatted = formatTimeDisplay(tx.time, currentTimeFormat);
    return `${dateFormatted} ${timeFormatted}`;
}

function getTimeValueFromForm() {
    if (currentTimeFormat === '12') {
        const hVal = txTimeHourSelect ? txTimeHourSelect.value : '';
        const mVal = txTimeMinuteSelect ? txTimeMinuteSelect.value : '';
        if (!hVal || !mVal) {
            return '';
        }
        let h = parseInt(hVal, 10);
        const m = parseInt(mVal, 10);
        const ampm = txTimeAmpmSelect ? txTimeAmpmSelect.value : 'AM';
        if (ampm === 'AM') {
            if (h === 12) h = 0;
        } else if (ampm === 'PM') {
            if (h !== 12) h += 12;
        }
        return `${String(h).padStart(2, '0')}:${String(m).padStart(2, '0')}`;
    }
    const raw = txTimeInput ? txTimeInput.value.trim() : '';
    const match = raw.match(/^(\d{1,2}):(\d{2})$/);
    if (!match) return '';
    const h24 = parseInt(match[1], 10);
    const m = match[2];
    return `${String(h24).padStart(2, '0')}:${m}`;
}

function setTimeValueToForm(timeStr) {
    if (timeStr) {
        const match = String(timeStr).trim().match(/^(\d{1,2}):(\d{2})$/);
        if (match) {
            const h24 = parseInt(match[1], 10);
            const m = match[2];
            if (txTimeInput) {
                txTimeInput.value = `${String(h24).padStart(2, '0')}:${m}`;
            }
            const ampm = h24 >= 12 ? 'PM' : 'AM';
            let h12 = h24 % 12;
            if (h12 === 0) h12 = 12;
            const hStr = String(h12).padStart(2, '0');
            if (txTimeHourSelect) {
                txTimeHourSelect.value = hStr;
                if (!txTimeHourSelect.value) {
                    txTimeHourSelect.value = String(h12);
                }
            }
            if (txTimeMinuteSelect) {
                txTimeMinuteSelect.value = m;
            }
            if (txTimeAmpmSelect) {
                txTimeAmpmSelect.value = ampm;
            }
            return;
        }
    }
    if (txTimeInput) txTimeInput.value = '';
    if (txTimeHourSelect) txTimeHourSelect.value = '';
    if (txTimeMinuteSelect) txTimeMinuteSelect.value = '';
    if (txTimeAmpmSelect) txTimeAmpmSelect.value = 'AM';
}

function showTimeError() {
    if (txTimeError) {
        txTimeError.classList.remove('hidden');
    }
    if (currentTimeFormat === '12') {
        if (txTimeHourSelect && !txTimeHourSelect.value) {
            txTimeHourSelect.focus();
        } else if (txTimeMinuteSelect && !txTimeMinuteSelect.value) {
            txTimeMinuteSelect.focus();
        } else if (txTimeHourSelect) {
            txTimeHourSelect.focus();
        }
    } else if (txTimeInput) {
        txTimeInput.focus();
    }
}

function hideTimeError() {
    if (txTimeError) {
        txTimeError.classList.add('hidden');
    }
}

function applyTimeFormatUI(format) {
    currentTimeFormat = format === '12' ? '12' : '24';
    if (timeFormatRadios) {
        for (const radio of timeFormatRadios) {
            radio.checked = (radio.value === currentTimeFormat);
        }
    }
    if (currentTimeFormat === '12') {
        if (timeInput24Container) timeInput24Container.classList.add('hidden');
        if (timeInput12Container) timeInput12Container.classList.remove('hidden');
        if (txTimeLabel) txTimeLabel.setAttribute('for', 'tx-time-hour');
    } else {
        if (timeInput24Container) timeInput24Container.classList.remove('hidden');
        if (timeInput12Container) timeInput12Container.classList.add('hidden');
        if (txTimeLabel) txTimeLabel.setAttribute('for', 'tx-time');
    }
}

function getCurrentTimeString() {
    const now = new Date();
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    return `${hours}:${minutes}`;
}

async function loadData() {
    try {
        currentTimeFormat = await getTimeFormat();
        applyTimeFormatUI(currentTimeFormat);
        currentCategories = await getCategories();
        currentBudgets = await getBudgets();
        currentWallets = await getWallets();
        renderCategorySettings();
        renderBudgetSettings();
        renderWalletSettings();
        if (getActiveWallets().length < 2 && getSelectedType() === 'transfer') {
            txTypeInputs[0].checked = true;
            updateTypeUI('expense');
        } else {
            updateTypeUI(getSelectedType());
        }
        populateWalletFilterOptions();

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
        renderBudgetSection();
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
    return formatNumber(value);
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

function updateChartTypeUI() {
    const isIncome = currentChartType === 'income';
    if (btnChartExpense) {
        btnChartExpense.classList.toggle('is-active', !isIncome);
        btnChartExpense.setAttribute('aria-pressed', String(!isIncome));
    }
    if (btnChartIncome) {
        btnChartIncome.classList.toggle('is-active', isIncome);
        btnChartIncome.setAttribute('aria-pressed', String(isIncome));
    }
    if (chartSectionTitle) {
        chartSectionTitle.textContent = isIncome ? 'Grafik Pemasukan Bulanan' : 'Grafik Pengeluaran Bulanan';
    }
}

function renderMonthlyChart(transactions) {
    if (!chartContainer) return;

    updateChartTypeUI();

    const isIncome = currentChartType === 'income';
    const typeLabel = isIncome ? 'pemasukan' : 'pengeluaran';

    // Filter transaksi aktif (tidak terhapus) sesuai tipe (pengeluaran atau pemasukan),
    // transfer antar dompet tidak dihitung, dan jumlah > 0
    const chartTx = (transactions || []).filter(tx => 
        tx.type === currentChartType && 
        tx.type !== 'transfer' && 
        !tx.deleted && 
        tx.amount > 0
    );

    if (chartTx.length === 0) {
        chartContainer.innerHTML = `<p class="chart-empty">Belum ada transaksi ${typeLabel} untuk ditampilkan.</p>`;
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
    for (const tx of chartTx) {
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
        chartContainer.innerHTML = `<p class="chart-empty">Belum ada transaksi ${typeLabel} untuk ditampilkan.</p>`;
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

    let totalAmountAll = 0;
    let maxMonthAmount = 0;
    let monthsWithAmount = 0;

    for (const key of displayMonths) {
        const item = monthData.get(key);
        const amt = item ? item.total : 0;
        totalAmountAll += amt;
        if (amt > maxMonthAmount) {
            maxMonthAmount = amt;
        }
        if (amt > 0) {
            monthsWithAmount++;
        }
    }

    const avgAmount = monthsWithAmount > 0 ? Math.round(totalAmountAll / monthsWithAmount) : 0;
    if (chartSummaryText) {
        chartSummaryText.textContent = `Rata-rata: ${currencyFormatter.format(avgAmount)} / bulan`;
    }

    const ceiling = getNiceCeiling(maxMonthAmount);
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
        const amountClass = isIncome ? 'chart-detail-amount income' : 'chart-detail-amount';
        const emptyLabel = isIncome ? '(tidak ada pemasukan)' : '(tidak ada pengeluaran)';

        chartDetail.innerHTML = `
            <span class="chart-detail-month">${fullLabel}:</span>
            <strong class="${amountClass}">${currencyFormatter.format(total)}</strong>
            <span class="chart-detail-count">${count > 0 ? `(${count} transaksi)` : emptyLabel}</span>
        `;
    }

    const latestKey = displayMonths[displayMonths.length - 1];
    updateDetail(latestKey);

    const chartSvgClass = isIncome ? 'expense-chart monthly-chart is-income' : 'expense-chart monthly-chart is-expense';
    const chartAriaLabel = `Grafik total ${typeLabel} per bulan`;

    let svgHtml = `
        <svg class="${chartSvgClass}"
             viewBox="0 0 ${svgWidth} ${svgHeight}"
             width="${isScrollable ? svgWidth + 'px' : '100%'}"
             height="${isScrollable ? svgHeight + 'px' : 'auto'}"
             role="img"
             aria-label="${chartAriaLabel}">
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

function renderExpenseChart(transactions) {
    renderMonthlyChart(transactions);
}

function shiftDonutMonth(delta) {
    if (!selectedDonutMonth) {
        selectedDonutMonth = getLocalYearMonthString();
    }
    const [yStr, mStr] = selectedDonutMonth.split('-');
    let y = parseInt(yStr, 10);
    let m = parseInt(mStr, 10) + delta;
    if (m < 1) {
        m = 12;
        y--;
    } else if (m > 12) {
        m = 1;
        y++;
    }
    selectedDonutMonth = `${y}-${String(m).padStart(2, '0')}`;
    activeDonutCategory = null;
    if (donutMonthInput) {
        donutMonthInput.value = selectedDonutMonth;
    }
    renderDonutChart(currentTransactions);
}

function updateDonutTypeUI() {
    const isIncome = currentDonutType === 'income';
    if (btnDonutExpense) {
        btnDonutExpense.classList.toggle('is-active', !isIncome);
        btnDonutExpense.setAttribute('aria-pressed', String(!isIncome));
    }
    if (btnDonutIncome) {
        btnDonutIncome.classList.toggle('is-active', isIncome);
        btnDonutIncome.setAttribute('aria-pressed', String(isIncome));
    }
    if (donutSectionTitle) {
        donutSectionTitle.textContent = isIncome ? 'Diagram Pemasukan per Kategori' : 'Diagram Pengeluaran per Kategori';
    }
}

function renderDonutChart(transactions) {
    if (!donutContainer) return;

    if (!selectedDonutMonth) {
        selectedDonutMonth = getLocalYearMonthString();
    }

    if (donutMonthInput && donutMonthInput.value !== selectedDonutMonth) {
        donutMonthInput.value = selectedDonutMonth;
    }

    if (donutMonthLabel) {
        const [yStr, mStr] = selectedDonutMonth.split('-');
        const mNum = parseInt(mStr, 10);
        const monthName = INDO_MONTHS_FULL[mNum - 1] || '';
        donutMonthLabel.textContent = `${monthName} ${yStr}`;
    }

    updateDonutTypeUI();

    const isIncome = currentDonutType === 'income';
    const typeLabel = isIncome ? 'pemasukan' : 'pengeluaran';

    // Filter transaksi untuk bulan yang dipilih sesuai tipe (pengeluaran atau pemasukan),
    // transfer antar dompet tidak dihitung, tidak terhapus, dan jumlah > 0
    const monthTx = (transactions || []).filter(tx => {
        if (tx.type !== currentDonutType) return false;
        if (tx.type === 'transfer') return false;
        if (tx.deleted) return false;
        if (!(tx.amount > 0)) return false;
        if (!tx.date) return false;
        const match = tx.date.match(/^(\d{4})-(\d{1,2})/);
        if (!match) return false;
        const year = match[1];
        const month = String(parseInt(match[2], 10)).padStart(2, '0');
        return `${year}-${month}` === selectedDonutMonth;
    });

    if (monthTx.length === 0) {
        donutContainer.innerHTML = `<p class="chart-empty">Belum ada transaksi ${typeLabel} pada bulan ini.</p>`;
        if (donutDetail) {
            donutDetail.textContent = '';
            donutDetail.classList.add('hidden');
        }
        activeDonutCategory = null;
        return;
    }

    // Kelompokkan per kategori
    const categoryMap = new Map();
    let totalAmount = 0;
    for (const tx of monthTx) {
        const cat = (tx.category && tx.category.trim()) ? tx.category.trim() : 'Lainnya';
        const item = categoryMap.get(cat) || { amount: 0, count: 0 };
        item.amount += tx.amount;
        item.count += 1;
        categoryMap.set(cat, item);
        totalAmount += tx.amount;
    }

    if (totalAmount <= 0) {
        donutContainer.innerHTML = `<p class="chart-empty">Belum ada transaksi ${typeLabel} pada bulan ini.</p>`;
        if (donutDetail) {
            donutDetail.textContent = '';
            donutDetail.classList.add('hidden');
        }
        activeDonutCategory = null;
        return;
    }

    const sortedCategories = Array.from(categoryMap.entries())
        .sort((a, b) => b[1].amount - a[1].amount)
        .map(([category, data], index) => {
            const percentage = totalAmount > 0 ? (data.amount / totalAmount) * 100 : 0;
            return {
                category,
                amount: data.amount,
                count: data.count,
                percentage,
                color: DONUT_COLORS[index % DONUT_COLORS.length]
            };
        });

    if (activeDonutCategory && !sortedCategories.some(c => c.category === activeDonutCategory)) {
        activeDonutCategory = null;
    }

    function formatPercent(pct) {
        if (pct < 0.1 && pct > 0) return '< 0.1%';
        return (pct % 1 === 0 ? pct.toFixed(0) : pct.toFixed(1)) + '%';
    }

    function updateDonutDetail(catItem) {
        if (!donutDetail) return;
        donutDetail.classList.remove('hidden');
        const amountClass = isIncome ? 'chart-detail-amount income' : 'chart-detail-amount';
        if (!catItem) {
            donutDetail.innerHTML = `
                <span class="chart-detail-month">Total ${typeLabel}:</span>
                <strong class="${amountClass}">${currencyFormatter.format(totalAmount)}</strong>
                <span class="chart-detail-count">(${sortedCategories.length} kategori, ${monthTx.length} transaksi)</span>
            `;
        } else {
            donutDetail.innerHTML = `
                <span class="chart-detail-month">${escapeHTML(catItem.category)}:</span>
                <strong class="${amountClass}">${currencyFormatter.format(catItem.amount)}</strong>
                <span class="chart-detail-count">(${formatPercent(catItem.percentage)} - ${catItem.count} transaksi)</span>
            `;
        }
    }

    const radius = 75;
    const circumference = 2 * Math.PI * radius;
    const n = sortedCategories.length;
    const gap = n > 1 ? 2 : 0;
    let accumulatedOffset = 0;
    let segmentsHtml = '';

    for (const cat of sortedCategories) {
        const segLength = (cat.amount / totalAmount) * circumference;
        const visibleLength = n > 1 ? Math.max(0.5, segLength - gap) : circumference;
        const dashArray = n > 1 ? `${visibleLength} ${circumference - visibleLength}` : `${circumference} 0`;
        const dashOffset = -accumulatedOffset;
        accumulatedOffset += segLength;

        const percentStr = formatPercent(cat.percentage);
        const isSelected = activeDonutCategory === cat.category;

        segmentsHtml += `
            <circle
                class="donut-segment ${isSelected ? 'is-active' : ''}"
                cx="110"
                cy="110"
                r="75"
                fill="none"
                stroke="${cat.color}"
                stroke-width="34"
                stroke-dasharray="${dashArray}"
                stroke-dashoffset="${dashOffset}"
                transform="rotate(-90 110 110)"
                data-category="${escapeHTML(cat.category)}"
                tabindex="0"
                role="button"
                aria-label="${escapeHTML(cat.category)}: ${currencyFormatter.format(cat.amount)} (${percentStr})">
            </circle>
        `;
    }

    const selectedCatObj = activeDonutCategory
        ? sortedCategories.find(c => c.category === activeDonutCategory)
        : null;

    updateDonutDetail(selectedCatObj);

    const centerTitleText = selectedCatObj
        ? (selectedCatObj.category.length > 13 ? escapeHTML(selectedCatObj.category.slice(0, 11)) + '…' : escapeHTML(selectedCatObj.category))
        : 'Total';
    const centerValueText = selectedCatObj ? formatCompactCurrency(selectedCatObj.amount) : formatCompactCurrency(totalAmount);
    const centerSubText = selectedCatObj
        ? formatPercent(selectedCatObj.percentage)
        : `${sortedCategories.length} kategori`;

    let legendHtml = '<ul class="donut-legend-list" role="list" aria-label="Rincian kategori">';
    for (const cat of sortedCategories) {
        const isSelected = activeDonutCategory === cat.category;
        const percentStr = formatPercent(cat.percentage);
        legendHtml += `
            <li class="donut-legend-item ${isSelected ? 'is-active' : ''}"
                data-category="${escapeHTML(cat.category)}"
                tabindex="0"
                role="button"
                aria-label="${escapeHTML(cat.category)}: ${currencyFormatter.format(cat.amount)} (${percentStr})">
                <div class="donut-legend-main">
                    <span class="donut-legend-color" style="background-color: ${cat.color}"></span>
                    <span class="donut-legend-name" title="${escapeHTML(cat.category)}">${escapeHTML(cat.category)}</span>
                </div>
                <div class="donut-legend-side">
                    <span class="donut-legend-percent">${percentStr}</span>
                    <strong class="donut-legend-amount">${currencyFormatter.format(cat.amount)}</strong>
                </div>
            </li>
        `;
    }
    legendHtml += '</ul>';

    donutContainer.innerHTML = `
        <div class="donut-content">
            <div class="donut-svg-wrapper">
                <svg class="donut-chart-svg ${activeDonutCategory ? 'has-active' : ''}"
                     viewBox="0 0 220 220"
                     role="img"
                     aria-label="Diagram donut per kategori untuk ${typeLabel}">
                    <circle cx="110" cy="110" r="75" fill="none" stroke-width="34" class="donut-bg-ring" />
                    ${segmentsHtml}
                    <g class="donut-center-group" aria-hidden="true">
                        <text x="110" y="98" text-anchor="middle" class="donut-center-title" id="donut-center-title">${centerTitleText}</text>
                        <text x="110" y="118" text-anchor="middle" class="donut-center-value" id="donut-center-value">${centerValueText}</text>
                        <text x="110" y="134" text-anchor="middle" class="donut-center-sub" id="donut-center-sub">${centerSubText}</text>
                    </g>
                </svg>
            </div>
            <div class="donut-legend">
                ${legendHtml}
            </div>
        </div>
    `;

    const segments = donutContainer.querySelectorAll('.donut-segment');
    const legendItems = donutContainer.querySelectorAll('.donut-legend-item');
    const svgEl = donutContainer.querySelector('.donut-chart-svg');
    const centerTitleEl = donutContainer.querySelector('#donut-center-title');
    const centerValueEl = donutContainer.querySelector('#donut-center-value');
    const centerSubEl = donutContainer.querySelector('#donut-center-sub');

    function highlightCategory(catName) {
        const catObj = sortedCategories.find(c => c.category === catName);
        if (catObj) {
            if (centerTitleEl) {
                const displayName = catObj.category.length > 13 ? catObj.category.slice(0, 11) + '…' : catObj.category;
                centerTitleEl.textContent = displayName;
            }
            if (centerValueEl) centerValueEl.textContent = formatCompactCurrency(catObj.amount);
            if (centerSubEl) centerSubEl.textContent = formatPercent(catObj.percentage);
            updateDonutDetail(catObj);
            if (svgEl) svgEl.classList.add('has-active');
            segments.forEach(seg => seg.classList.toggle('is-active', seg.dataset.category === catName));
            legendItems.forEach(item => item.classList.toggle('is-active', item.dataset.category === catName));
        } else {
            if (centerTitleEl) centerTitleEl.textContent = 'Total';
            if (centerValueEl) centerValueEl.textContent = formatCompactCurrency(totalAmount);
            if (centerSubEl) centerSubEl.textContent = `${sortedCategories.length} kategori`;
            updateDonutDetail(null);
            if (svgEl) svgEl.classList.remove('has-active');
            segments.forEach(seg => seg.classList.remove('is-active'));
            legendItems.forEach(item => item.classList.remove('is-active'));
        }
    }

    function toggleCategory(catName) {
        if (activeDonutCategory === catName) {
            activeDonutCategory = null;
            highlightCategory(null);
        } else {
            activeDonutCategory = catName;
            highlightCategory(catName);
        }
    }

    segments.forEach(seg => {
        const catName = seg.dataset.category;
        seg.addEventListener('mouseenter', () => {
            if (!activeDonutCategory) highlightCategory(catName);
        });
        seg.addEventListener('mouseleave', () => {
            if (!activeDonutCategory) highlightCategory(null);
        });
        seg.addEventListener('focus', () => {
            if (!activeDonutCategory) highlightCategory(catName);
        });
        seg.addEventListener('blur', () => {
            if (!activeDonutCategory) highlightCategory(null);
        });
        seg.addEventListener('click', (e) => {
            e.stopPropagation();
            toggleCategory(catName);
        });
        seg.addEventListener('keydown', (e) => {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                toggleCategory(catName);
            } else if (e.key === 'Escape') {
                activeDonutCategory = null;
                highlightCategory(null);
            }
        });
    });

    legendItems.forEach(item => {
        const catName = item.dataset.category;
        item.addEventListener('mouseenter', () => {
            if (!activeDonutCategory) highlightCategory(catName);
        });
        item.addEventListener('mouseleave', () => {
            if (!activeDonutCategory) highlightCategory(null);
        });
        item.addEventListener('focus', () => {
            if (!activeDonutCategory) highlightCategory(catName);
        });
        item.addEventListener('blur', () => {
            if (!activeDonutCategory) highlightCategory(null);
        });
        item.addEventListener('click', (e) => {
            e.stopPropagation();
            toggleCategory(catName);
        });
        item.addEventListener('keydown', (e) => {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                toggleCategory(catName);
            } else if (e.key === 'Escape') {
                activeDonutCategory = null;
                highlightCategory(null);
            }
        });
    });

    donutContainer.addEventListener('click', (e) => {
        if (!e.target.closest('.donut-segment') && !e.target.closest('.donut-legend-item')) {
            if (activeDonutCategory) {
                activeDonutCategory = null;
                highlightCategory(null);
            }
        }
    });
}

function render() {
    txListContainer.innerHTML = '';
    
    let totalIncome = 0;
    let totalExpense = 0;

    const walletBalances = new Map();
    const activeWallets = getActiveWallets();
    for (const w of activeWallets) {
        walletBalances.set(w.id, 0);
    }

    currentTransactions.forEach(tx => {
        const wId = tx.walletId || DEFAULT_WALLET_ID;
        if (tx.type === 'income') {
            totalIncome += tx.amount;
            walletBalances.set(wId, (walletBalances.get(wId) || 0) + tx.amount);
        } else if (tx.type === 'expense') {
            totalExpense += tx.amount;
            walletBalances.set(wId, (walletBalances.get(wId) || 0) - tx.amount);
        } else if (tx.type === 'transfer') {
            const fromId = tx.walletId || DEFAULT_WALLET_ID;
            const toId = tx.toWalletId;
            walletBalances.set(fromId, (walletBalances.get(fromId) || 0) - tx.amount);
            if (toId) {
                walletBalances.set(toId, (walletBalances.get(toId) || 0) + tx.amount);
            }
        }
    });

    const totalBalance = totalIncome - totalExpense;
    totalBalanceEl.textContent = currencyFormatter.format(totalBalance);
    totalIncomeEl.textContent = currencyFormatter.format(totalIncome);
    totalExpenseEl.textContent = currencyFormatter.format(totalExpense);

    if (walletBalancesContainer) {
        let balancesHtml = '';
        for (const w of activeWallets) {
            const bal = walletBalances.get(w.id) || 0;
            balancesHtml += `
                <div class="wallet-balance-item">
                    <span class="wallet-balance-name">${escapeHTML(w.name)}</span>
                    <strong class="wallet-balance-amount">${currencyFormatter.format(bal)}</strong>
                </div>
            `;
        }
        walletBalancesContainer.innerHTML = balancesHtml;
    }

    const filteredTx = currentTransactions.filter(tx => {
        if (currentWalletFilter === 'all') return true;
        if (tx.type === 'transfer') {
            return (tx.walletId || DEFAULT_WALLET_ID) === currentWalletFilter || tx.toWalletId === currentWalletFilter;
        }
        return (tx.walletId || DEFAULT_WALLET_ID) === currentWalletFilter;
    });

    if (filteredTx.length === 0) {
        txListContainer.innerHTML = '<p class="tx-empty">Belum ada transaksi.</p>';
    } else {
        filteredTx.forEach(tx => {
            const txEl = document.createElement('div');
            txEl.className = 'tx-item';
            
            let sign = '';
            let amountClass = '';
            let categoryText = escapeHTML(tx.category || '');
            let walletBadgeText = '';

            if (tx.type === 'transfer') {
                const fromName = getWalletName(tx.walletId);
                const toName = getWalletName(tx.toWalletId);
                categoryText = 'Transfer';
                walletBadgeText = `${escapeHTML(fromName)} &rarr; ${escapeHTML(toName)}`;
                amountClass = 'transfer';
                if (currentWalletFilter === 'all') {
                    sign = '';
                } else if (currentWalletFilter === (tx.walletId || DEFAULT_WALLET_ID)) {
                    sign = '- ';
                } else if (currentWalletFilter === tx.toWalletId) {
                    sign = '+ ';
                }
            } else {
                sign = tx.type === 'income' ? '+ ' : '- ';
                amountClass = tx.type === 'income' ? 'income' : 'expense';
                walletBadgeText = escapeHTML(getWalletName(tx.walletId));
            }

            txEl.innerHTML = `
                <div class="tx-info">
                    <div class="tx-header">
                        <span class="tx-category">${categoryText} <span class="tx-wallet-badge">${walletBadgeText}</span></span>
                        <span class="tx-date">${formatDateTime(tx)}</span>
                    </div>
                    ${tx.note ? `<div class="tx-note">${escapeHTML(tx.note)}</div>` : ''}
                    <div class="tx-amount ${amountClass}">${sign}${currencyFormatter.format(tx.amount)}</div>
                </div>
                <div class="tx-actions">
                    <button class="btn-icon btn-edit" data-id="${tx.id}">Edit</button>
                    <button class="btn-icon btn-delete" data-id="${tx.id}">Hapus</button>
                </div>
            `;
            
            txListContainer.appendChild(txEl);
        });
    }

    renderMonthlyChart(currentTransactions);
    renderBudgetSection();
    renderDonutChart(currentTransactions);

    // Attach event listeners to generated buttons
    document.querySelectorAll('.btn-edit').forEach(btn => {
        btn.addEventListener('click', (e) => handleEdit(e.target.dataset.id));
    });
    document.querySelectorAll('.btn-delete').forEach(btn => {
        btn.addEventListener('click', (e) => handleDelete(e.target.dataset.id));
    });
}

function renderCategorySettings() {
    renderCategoryGroup('expense', expenseCategoryList);
    renderCategoryGroup('income', incomeCategoryList);
}

function renderCategoryGroup(type, container) {
    if (!container) return;
    container.innerHTML = '';
    const list = (currentCategories && currentCategories[type]) ? currentCategories[type] : [];

    if (list.length === 0) {
        const emptyLi = document.createElement('li');
        emptyLi.className = 'category-empty-item';
        emptyLi.textContent = 'Belum ada kategori.';
        container.appendChild(emptyLi);
        return;
    }

    list.forEach(cat => {
        const li = document.createElement('li');
        li.className = 'category-item';

        const isEditing = editingCategory && editingCategory.type === type && editingCategory.name === cat;

        if (isEditing) {
            li.classList.add('editing');
            li.innerHTML = `
                <form class="category-inline-edit-form">
                    <input type="text" class="category-edit-input" value="${escapeHTML(cat)}" required autocomplete="off" aria-label="Ubah nama kategori ${escapeHTML(cat)}">
                    <div class="category-item-actions">
                        <button type="submit" class="btn-icon btn-save-cat">Simpan</button>
                        <button type="button" class="btn-icon btn-cancel-cat">Batal</button>
                    </div>
                </form>
            `;

            const editForm = li.querySelector('.category-inline-edit-form');
            const editInput = li.querySelector('.category-edit-input');
            const btnCancelCat = li.querySelector('.btn-cancel-cat');

            editForm.addEventListener('submit', async (e) => {
                e.preventDefault();
                await handleSaveCategoryName(type, cat, editInput.value.trim());
            });

            btnCancelCat.addEventListener('click', () => {
                editingCategory = null;
                renderCategorySettings();
            });

            editInput.addEventListener('keydown', (e) => {
                if (e.key === 'Escape') {
                    editingCategory = null;
                    renderCategorySettings();
                }
            });

            container.appendChild(li);
            requestAnimationFrame(() => {
                editInput.focus();
                editInput.select();
            });
        } else {
            li.innerHTML = `
                <span class="category-name">${escapeHTML(cat)}</span>
                <div class="category-item-actions">
                    <button type="button" class="btn-icon btn-edit-cat" data-type="${type}" data-name="${escapeHTML(cat)}" aria-label="Ubah nama kategori ${escapeHTML(cat)}">Ubah</button>
                    <button type="button" class="btn-icon btn-delete-cat" data-type="${type}" data-name="${escapeHTML(cat)}" aria-label="Hapus kategori ${escapeHTML(cat)}">Hapus</button>
                </div>
            `;

            li.querySelector('.btn-edit-cat').addEventListener('click', () => {
                editingCategory = { type, name: cat };
                renderCategorySettings();
            });

            li.querySelector('.btn-delete-cat').addEventListener('click', () => {
                handleDeleteCategoryPrompt(type, cat);
            });

            container.appendChild(li);
        }
    });
}

async function handleSaveCategoryName(type, oldName, newName) {
    if (!newName) {
        alert('Nama kategori tidak boleh kosong.');
        return;
    }
    if (newName === oldName) {
        editingCategory = null;
        renderCategorySettings();
        return;
    }

    const list = currentCategories[type] || [];
    const exists = list.some(c => c !== oldName && c.toLowerCase() === newName.toLowerCase());
    if (exists) {
        alert(`Kategori "${newName}" sudah ada di daftar ${type === 'income' ? 'pemasukan' : 'pengeluaran'}.`);
        return;
    }

    ensureToken();

    const idx = list.indexOf(oldName);
    if (idx !== -1) {
        list[idx] = newName;
        currentCategories[type] = list;
        currentCategories.updatedAt = Date.now();
        try {
            await saveCategories(currentCategories);
            editingCategory = null;
            renderCategorySettings();
            renderBudgetSettings();
            renderBudgetSection();
            if (txCategoryInput.value === oldName) {
                populateCategoryOptions(newName);
            } else {
                populateCategoryOptions(txCategoryInput.value);
            }
            markDirty().catch(err => console.error("Gagal menandai perubahan kategori untuk sinkron", err));
        } catch (error) {
            console.error("Gagal mengubah nama kategori", error);
            alert("Terjadi kesalahan saat menyimpan kategori.");
        }
    }
}

async function handleAddCategory(type, inputEl) {
    const name = inputEl.value.trim();
    if (!name) {
        alert('Nama kategori tidak boleh kosong.');
        return;
    }

    const list = currentCategories[type] || [];
    const exists = list.some(c => c.toLowerCase() === name.toLowerCase());
    if (exists) {
        alert(`Kategori "${name}" sudah ada di daftar ${type === 'income' ? 'pemasukan' : 'pengeluaran'}.`);
        return;
    }

    ensureToken();

    list.push(name);
    currentCategories[type] = list;
    currentCategories.updatedAt = Date.now();

    try {
        await saveCategories(currentCategories);
        inputEl.value = '';
        renderCategorySettings();
        renderBudgetSettings();
        populateCategoryOptions(txCategoryInput.value);
        markDirty().catch(err => console.error("Gagal menambah kategori untuk sinkron", err));
    } catch (error) {
        console.error("Gagal menambah kategori", error);
        alert("Terjadi kesalahan saat menyimpan kategori.");
    }
}

function handleDeleteCategoryPrompt(type, name) {
    pendingDeleteId = null;
    pendingCategoryDelete = { type, name };
    if (confirmDialogTitle) {
        confirmDialogTitle.textContent = `Hapus kategori "${name}"?`;
    }
    if (confirmDialog && typeof confirmDialog.showModal === 'function') {
        confirmDialog.showModal();
    }
}

async function performDeleteCategory(type, name) {
    ensureToken();

    const list = currentCategories[type] || [];
    currentCategories[type] = list.filter(c => c !== name);
    currentCategories.updatedAt = Date.now();

    try {
        await saveCategories(currentCategories);
        renderCategorySettings();
        renderBudgetSettings();
        renderBudgetSection();
        if (txCategoryInput.value === name) {
            populateCategoryOptions(null);
        } else {
            populateCategoryOptions(txCategoryInput.value);
        }
        markDirty().catch(err => console.error("Gagal menandai penghapusan kategori untuk sinkron", err));
    } catch (error) {
        console.error("Gagal menghapus kategori", error);
        alert("Terjadi kesalahan saat menghapus kategori.");
    }
}

function renderWalletSettings() {
    if (!walletSettingsList) return;
    walletSettingsList.innerHTML = '';
    const active = getActiveWallets();

    if (active.length === 0) {
        const emptyLi = document.createElement('li');
        emptyLi.className = 'category-empty-item';
        emptyLi.textContent = 'Belum ada dompet.';
        walletSettingsList.appendChild(emptyLi);
        return;
    }

    active.forEach(w => {
        const li = document.createElement('li');
        li.className = 'category-item';

        const isEditing = editingWalletId === w.id;

        if (isEditing) {
            li.classList.add('editing');
            li.innerHTML = `
                <form class="category-inline-edit-form">
                    <input type="text" class="category-edit-input" value="${escapeHTML(w.name)}" required autocomplete="off" aria-label="Ubah nama dompet ${escapeHTML(w.name)}">
                    <div class="category-item-actions">
                        <button type="submit" class="btn-icon btn-save-cat">Simpan</button>
                        <button type="button" class="btn-icon btn-cancel-cat">Batal</button>
                    </div>
                </form>
            `;

            const editForm = li.querySelector('.category-inline-edit-form');
            const editInput = li.querySelector('.category-edit-input');
            const btnCancelEdit = li.querySelector('.btn-cancel-cat');

            editForm.addEventListener('submit', async (e) => {
                e.preventDefault();
                await handleSaveWalletName(w.id, editInput.value.trim());
            });

            btnCancelEdit.addEventListener('click', () => {
                editingWalletId = null;
                renderWalletSettings();
            });

            editInput.addEventListener('keydown', (e) => {
                if (e.key === 'Escape') {
                    editingWalletId = null;
                    renderWalletSettings();
                }
            });

            walletSettingsList.appendChild(li);
            requestAnimationFrame(() => {
                editInput.focus();
                editInput.select();
            });
        } else {
            li.innerHTML = `
                <span class="category-name">${escapeHTML(w.name)}</span>
                <div class="category-item-actions">
                    <button type="button" class="btn-icon btn-edit-wallet" data-id="${escapeHTML(w.id)}" aria-label="Ubah nama dompet ${escapeHTML(w.name)}">Ubah</button>
                    <button type="button" class="btn-icon btn-delete-wallet" data-id="${escapeHTML(w.id)}" aria-label="Hapus dompet ${escapeHTML(w.name)}">Hapus</button>
                </div>
            `;

            li.querySelector('.btn-edit-wallet').addEventListener('click', () => {
                editingWalletId = w.id;
                renderWalletSettings();
            });

            li.querySelector('.btn-delete-wallet').addEventListener('click', () => {
                handleDeleteWalletPrompt(w.id);
            });

            walletSettingsList.appendChild(li);
        }
    });
}

async function handleSaveWalletName(walletId, newName) {
    if (!newName) {
        alert('Nama dompet tidak boleh kosong.');
        return;
    }
    const wallet = currentWallets.find(w => w.id === walletId);
    if (!wallet) return;

    if (newName === wallet.name) {
        editingWalletId = null;
        renderWalletSettings();
        return;
    }

    const exists = getActiveWallets().some(w => w.id !== walletId && w.name.toLowerCase() === newName.toLowerCase());
    if (exists) {
        alert(`Dompet "${newName}" sudah ada.`);
        return;
    }

    ensureToken();

    wallet.name = newName;
    wallet.updatedAt = Date.now();

    try {
        await saveWallets(currentWallets);
        editingWalletId = null;
        render();
        renderWalletSettings();
        populateWalletOptions(txWalletInput.value);
        populateWalletFilterOptions();
        markDirty().catch(err => console.error("Gagal menandai perubahan dompet untuk sinkron", err));
    } catch (error) {
        console.error("Gagal mengubah nama dompet", error);
        alert("Terjadi kesalahan saat menyimpan dompet.");
    }
}

async function handleAddWallet(inputEl) {
    const name = inputEl.value.trim();
    if (!name) {
        alert('Nama dompet tidak boleh kosong.');
        return;
    }

    const exists = getActiveWallets().some(w => w.name.toLowerCase() === name.toLowerCase());
    if (exists) {
        alert(`Dompet "${name}" sudah ada.`);
        return;
    }

    ensureToken();

    const now = Date.now();
    const newWallet = {
        id: crypto.randomUUID(),
        name,
        createdAt: now,
        updatedAt: now,
        deleted: false
    };

    currentWallets.push(newWallet);

    try {
        await saveWallets(currentWallets);
        inputEl.value = '';
        render();
        renderWalletSettings();
        populateWalletOptions(txWalletInput.value);
        populateWalletFilterOptions();
        markDirty().catch(err => console.error("Gagal menambah dompet untuk sinkron", err));
    } catch (error) {
        console.error("Gagal menambah dompet", error);
        alert("Terjadi kesalahan saat menyimpan dompet.");
    }
}

function handleDeleteWalletPrompt(walletId) {
    const wallet = currentWallets.find(w => w.id === walletId);
    if (!wallet) return;

    // Dompet yang masih punya transaksi aktif tidak bisa dihapus.
    const hasActiveTransactions = currentTransactions.some(tx => !tx.deleted && (
        (tx.walletId || DEFAULT_WALLET_ID) === walletId ||
        (tx.type === 'transfer' && tx.toWalletId === walletId)
    ));
    if (hasActiveTransactions) {
        alert(`Dompet "${wallet.name}" tidak bisa dihapus karena masih memiliki transaksi aktif.`);
        return;
    }

    const active = getActiveWallets();
    if (active.length <= 1) {
        alert('Dompet tidak bisa dihapus karena minimal harus ada satu dompet.');
        return;
    }

    pendingDeleteId = null;
    pendingCategoryDelete = null;
    pendingBudgetDelete = null;
    pendingWalletDelete = walletId;

    if (confirmDialogTitle) {
        confirmDialogTitle.textContent = `Hapus dompet "${wallet.name}"?`;
    }
    if (confirmDialog && typeof confirmDialog.showModal === 'function') {
        confirmDialog.showModal();
    }
}

async function performDeleteWallet(walletId) {
    ensureToken();

    const wallet = currentWallets.find(w => w.id === walletId);
    if (wallet) {
        wallet.deleted = true;
        wallet.updatedAt = Date.now();

        try {
            await saveWallets(currentWallets);
            if (localStorage.getItem(LAST_WALLET_KEY) === walletId) {
                localStorage.removeItem(LAST_WALLET_KEY);
            }
            if (currentWalletFilter === walletId) {
                currentWalletFilter = 'all';
            }
            render();
            renderWalletSettings();
            if (getActiveWallets().length < 2 && getSelectedType() === 'transfer') {
                txTypeInputs[0].checked = true;
                updateTypeUI('expense');
            } else {
                updateTypeUI(getSelectedType());
            }
            populateWalletFilterOptions();
            markDirty().catch(err => console.error("Gagal menandai penghapusan dompet untuk sinkron", err));
        } catch (error) {
            console.error("Gagal menghapus dompet", error);
            alert("Terjadi kesalahan saat menghapus dompet.");
        }
    }
}

function getLocalYearMonthString(d = new Date()) {
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, '0');
    return `${y}-${m}`;
}

function getActiveMonthlyExpenses(transactions, targetMonthKey) {
    const categoryTotals = new Map();
    let totalExpense = 0;
    for (const tx of transactions || []) {
        if (tx.type !== 'expense' || tx.deleted || !(tx.amount > 0) || !tx.date) continue;
        const match = tx.date.match(/^(\d{4})-(\d{1,2})/);
        if (!match) continue;
        const year = match[1];
        const month = String(parseInt(match[2], 10)).padStart(2, '0');
        const key = `${year}-${month}`;
        if (key === targetMonthKey) {
            const cat = tx.category || 'Lainnya';
            categoryTotals.set(cat, (categoryTotals.get(cat) || 0) + tx.amount);
            totalExpense += tx.amount;
        }
    }
    return { categoryTotals, totalExpense };
}

function renderBudgetSection() {
    if (!budgetListContainer) return;

    if (!selectedBudgetMonth) {
        selectedBudgetMonth = getLocalYearMonthString();
    }
    if (budgetMonthInput && budgetMonthInput.value !== selectedBudgetMonth) {
        budgetMonthInput.value = selectedBudgetMonth;
    }
    if (budgetMonthLabel) {
        const [yStr, mStr] = selectedBudgetMonth.split('-');
        const mNum = parseInt(mStr, 10);
        const monthName = INDO_MONTHS_FULL[mNum - 1] || '';
        budgetMonthLabel.textContent = `${monthName} ${yStr}`;
    }

    const budgetMap = (currentBudgets && currentBudgets.items) ? currentBudgets.items : {};
    const budgetedCats = Object.keys(budgetMap).filter(cat => typeof budgetMap[cat] === 'number' && budgetMap[cat] > 0);

    if (budgetedCats.length === 0) {
        budgetListContainer.innerHTML = '<p class="budget-empty">Belum ada budget yang diatur. Atur budget di bagian Pengaturan Kategori &amp; Budget di bawah.</p>';
        if (budgetSummaryBanner) {
            budgetSummaryBanner.innerHTML = '';
            budgetSummaryBanner.classList.add('hidden');
        }
        return;
    }

    const activeExpenseList = (currentCategories && currentCategories.expense) ? currentCategories.expense : [];
    const orderedCats = [];
    for (const cat of activeExpenseList) {
        if (budgetedCats.includes(cat)) {
            orderedCats.push(cat);
        }
    }
    const orphanCats = budgetedCats.filter(cat => !activeExpenseList.includes(cat)).sort();
    orderedCats.push(...orphanCats);

    const { categoryTotals } = getActiveMonthlyExpenses(currentTransactions, selectedBudgetMonth);

    let totalBudget = 0;
    let totalTerpakai = 0;
    let listHtml = '';

    for (const cat of orderedCats) {
        const budgetAmount = budgetMap[cat];
        const terpakai = categoryTotals.get(cat) || 0;
        totalBudget += budgetAmount;
        totalTerpakai += terpakai;

        const percent = budgetAmount > 0 ? (terpakai / budgetAmount) * 100 : 0;
        const percentClamped = Math.min(100, Math.max(0, percent));
        const isOver = terpakai > budgetAmount;
        const sisa = Math.max(0, budgetAmount - terpakai);
        const lebih = isOver ? (terpakai - budgetAmount) : 0;

        let toneClass = 'tone-normal';
        if (isOver) {
            toneClass = 'tone-danger';
        } else if (percent >= 80) {
            toneClass = 'tone-warn';
        }

        const formattedBudget = currencyFormatter.format(budgetAmount);
        const formattedTerpakai = currencyFormatter.format(terpakai);
        const statusText = isOver
            ? `Lebih ${currencyFormatter.format(lebih)}`
            : `Sisa ${currencyFormatter.format(sisa)}`;

        listHtml += `
            <div class="budget-card">
                <div class="budget-card-header">
                    <span class="budget-category-name">${escapeHTML(cat)}</span>
                    <span class="budget-percent-badge ${toneClass}">${Math.round(percent)}%</span>
                </div>
                <div class="budget-progress-track" role="progressbar" aria-valuenow="${Math.round(percent)}" aria-valuemin="0" aria-valuemax="100" aria-label="Progres budget ${escapeHTML(cat)}: ${Math.round(percent)}%">
                    <div class="budget-progress-fill ${toneClass}" style="width: ${percentClamped}%;"></div>
                </div>
                <div class="budget-card-details">
                    <div class="budget-detail-col">
                        <span class="budget-detail-label">Budget</span>
                        <span class="budget-detail-value">${formattedBudget}</span>
                    </div>
                    <div class="budget-detail-col">
                        <span class="budget-detail-label">Terpakai</span>
                        <span class="budget-detail-value">${formattedTerpakai}</span>
                    </div>
                    <div class="budget-detail-col">
                        <span class="budget-detail-label">${isOver ? 'Kelebihan' : 'Sisa'}</span>
                        <span class="budget-detail-value ${toneClass}">${statusText}</span>
                    </div>
                </div>
            </div>
        `;
    }

    budgetListContainer.innerHTML = listHtml;

    if (budgetSummaryBanner) {
        const totalSisa = totalBudget - totalTerpakai;
        const totalOver = totalTerpakai > totalBudget;
        const summaryStatus = totalOver
            ? `Lebih ${currencyFormatter.format(totalTerpakai - totalBudget)}`
            : `Sisa ${currencyFormatter.format(Math.max(0, totalSisa))}`;
        const summaryTone = totalOver ? 'tone-danger' : (totalBudget > 0 && totalTerpakai / totalBudget >= 0.8 ? 'tone-warn' : 'tone-normal');

        budgetSummaryBanner.innerHTML = `
            <div class="budget-summary-banner-item">
                <span>Total Budget:</span>
                <strong>${currencyFormatter.format(totalBudget)}</strong>
            </div>
            <div class="budget-summary-banner-item">
                <span>Terpakai:</span>
                <strong>${currencyFormatter.format(totalTerpakai)}</strong>
            </div>
            <div class="budget-summary-banner-item">
                <span>${totalOver ? 'Kelebihan:' : 'Sisa:'}</span>
                <strong class="budget-detail-value ${summaryTone}">${summaryStatus}</strong>
            </div>
        `;
        budgetSummaryBanner.classList.remove('hidden');
    }
}

function shiftBudgetMonth(delta) {
    if (!selectedBudgetMonth) {
        selectedBudgetMonth = getLocalYearMonthString();
    }
    const [yStr, mStr] = selectedBudgetMonth.split('-');
    let y = parseInt(yStr, 10);
    let m = parseInt(mStr, 10) + delta;
    if (m < 1) {
        m = 12;
        y--;
    } else if (m > 12) {
        m = 1;
        y++;
    }
    selectedBudgetMonth = `${y}-${String(m).padStart(2, '0')}`;
    if (budgetMonthInput) {
        budgetMonthInput.value = selectedBudgetMonth;
    }
    renderBudgetSection();
}

function populateBudgetCategoryOptions() {
    if (!selectBudgetCat) return;
    const expenseList = (currentCategories && currentCategories.expense) ? currentCategories.expense : [];
    const budgetMap = (currentBudgets && currentBudgets.items) ? currentBudgets.items : {};

    let html = '<option value="" disabled selected>Pilih kategori pengeluaran</option>';
    for (const cat of expenseList) {
        const hasBudget = typeof budgetMap[cat] === 'number' && budgetMap[cat] > 0;
        const budgetLabel = hasBudget ? ` (Budget: ${currencyFormatter.format(budgetMap[cat])})` : '';
        html += `<option value="${escapeHTML(cat)}">${escapeHTML(cat)}${escapeHTML(budgetLabel)}</option>`;
    }
    selectBudgetCat.innerHTML = html;
}

function renderBudgetSettings() {
    populateBudgetCategoryOptions();
    if (!budgetSettingsList) return;
    budgetSettingsList.innerHTML = '';

    const budgetMap = (currentBudgets && currentBudgets.items) ? currentBudgets.items : {};
    const budgetedCats = Object.keys(budgetMap).filter(cat => typeof budgetMap[cat] === 'number' && budgetMap[cat] > 0);

    if (budgetedCats.length === 0) {
        const emptyLi = document.createElement('li');
        emptyLi.className = 'category-empty-item';
        emptyLi.textContent = 'Belum ada budget yang diatur.';
        budgetSettingsList.appendChild(emptyLi);
        return;
    }

    const activeExpenseList = (currentCategories && currentCategories.expense) ? currentCategories.expense : [];
    const orderedCats = [];
    for (const cat of activeExpenseList) {
        if (budgetedCats.includes(cat)) {
            orderedCats.push(cat);
        }
    }
    const orphanCats = budgetedCats.filter(cat => !activeExpenseList.includes(cat)).sort();
    orderedCats.push(...orphanCats);

    orderedCats.forEach(cat => {
        const li = document.createElement('li');
        li.className = 'category-item';
        const amount = budgetMap[cat];
        const isOrphan = !activeExpenseList.includes(cat);
        const isEditing = editingBudgetCategory === cat;

        if (isEditing) {
            li.classList.add('editing');
            li.innerHTML = `
                <form class="category-inline-edit-form">
                    <input type="text" inputmode="numeric" class="category-edit-input" value="${formatNumber(amount)}" required autocomplete="off" aria-label="Ubah budget kategori ${escapeHTML(cat)}">
                    <div class="category-item-actions">
                        <button type="submit" class="btn-icon btn-save-cat">Simpan</button>
                        <button type="button" class="btn-icon btn-cancel-cat">Batal</button>
                    </div>
                </form>
            `;

            const editForm = li.querySelector('.category-inline-edit-form');
            const editInput = li.querySelector('.category-edit-input');
            const btnCancelEdit = li.querySelector('.btn-cancel-cat');
            attachAmountInputFormatting(editInput);

            editForm.addEventListener('submit', async (e) => {
                e.preventDefault();
                const newAmount = parseAmount(editInput.value);
                await handleSaveBudgetAmount(cat, newAmount);
            });

            btnCancelEdit.addEventListener('click', () => {
                editingBudgetCategory = null;
                renderBudgetSettings();
            });

            editInput.addEventListener('keydown', (e) => {
                if (e.key === 'Escape') {
                    editingBudgetCategory = null;
                    renderBudgetSettings();
                }
            });

            budgetSettingsList.appendChild(li);
            requestAnimationFrame(() => {
                editInput.focus();
                editInput.select();
            });
        } else {
            const orphanBadge = isOrphan ? `<span class="budget-orphan-badge">(tidak ada di daftar kategori)</span>` : '';
            li.innerHTML = `
                <div>
                    <span class="category-name">${escapeHTML(cat)}</span>${orphanBadge}
                    <span class="budget-item-amount">&bull; ${currencyFormatter.format(amount)} / bulan</span>
                </div>
                <div class="category-item-actions">
                    <button type="button" class="btn-icon btn-edit-budget" data-name="${escapeHTML(cat)}" aria-label="Ubah budget ${escapeHTML(cat)}">Ubah</button>
                    <button type="button" class="btn-icon btn-delete-budget" data-name="${escapeHTML(cat)}" aria-label="Hapus budget ${escapeHTML(cat)}">Hapus</button>
                </div>
            `;

            li.querySelector('.btn-edit-budget').addEventListener('click', () => {
                editingBudgetCategory = cat;
                renderBudgetSettings();
            });

            li.querySelector('.btn-delete-budget').addEventListener('click', () => {
                handleDeleteBudgetPrompt(cat);
            });

            budgetSettingsList.appendChild(li);
        }
    });
}

async function handleSaveBudgetAmount(cat, newAmount) {
    if (isNaN(newAmount) || newAmount <= 0) {
        alert('Jumlah budget harus berupa angka lebih dari 0.');
        return;
    }

    ensureToken();

    if (!currentBudgets.items) {
        currentBudgets.items = {};
    }
    currentBudgets.items[cat] = Math.round(newAmount);
    currentBudgets.updatedAt = Date.now();

    try {
        await saveBudgets(currentBudgets);
        editingBudgetCategory = null;
        renderBudgetSettings();
        renderBudgetSection();
        markDirty().catch(err => console.error("Gagal menandai perubahan budget untuk sinkron", err));
    } catch (error) {
        console.error("Gagal mengubah budget", error);
        alert("Terjadi kesalahan saat menyimpan budget.");
    }
}

async function handleSetBudgetSubmit(e) {
    e.preventDefault();
    const cat = selectBudgetCat ? selectBudgetCat.value.trim() : '';
    const amount = inputBudgetAmount ? parseAmount(inputBudgetAmount.value) : 0;

    if (!cat) {
        alert('Pilih kategori pengeluaran.');
        return;
    }
    if (isNaN(amount) || amount <= 0) {
        alert('Jumlah budget harus berupa angka lebih dari 0.');
        return;
    }

    ensureToken();

    if (!currentBudgets.items) {
        currentBudgets.items = {};
    }
    currentBudgets.items[cat] = Math.round(amount);
    currentBudgets.updatedAt = Date.now();

    try {
        await saveBudgets(currentBudgets);
        if (inputBudgetAmount) inputBudgetAmount.value = '';
        if (selectBudgetCat) selectBudgetCat.value = '';
        renderBudgetSettings();
        renderBudgetSection();
        markDirty().catch(err => console.error("Gagal menyimpan budget untuk sinkron", err));
    } catch (error) {
        console.error("Gagal menyimpan budget", error);
        alert("Terjadi kesalahan saat menyimpan budget.");
    }
}

function handleDeleteBudgetPrompt(cat) {
    pendingDeleteId = null;
    pendingCategoryDelete = null;
    pendingBudgetDelete = cat;
    if (confirmDialogTitle) {
        confirmDialogTitle.textContent = `Hapus budget untuk kategori "${cat}"?`;
    }
    if (confirmDialog && typeof confirmDialog.showModal === 'function') {
        confirmDialog.showModal();
    }
}

async function performDeleteBudget(cat) {
    ensureToken();

    if (currentBudgets && currentBudgets.items && currentBudgets.items[cat] !== undefined) {
        delete currentBudgets.items[cat];
        currentBudgets.updatedAt = Date.now();

        try {
            await saveBudgets(currentBudgets);
            renderBudgetSettings();
            renderBudgetSection();
            markDirty().catch(err => console.error("Gagal menandai penghapusan budget untuk sinkron", err));
        } catch (error) {
            console.error("Gagal menghapus budget", error);
            alert("Terjadi kesalahan saat menghapus budget.");
        }
    }
}


function resetForm() {
    txForm.reset();
    txIdInput.value = '';
    // Default date to today
    const today = new Date();
    // Offset for local timezone
    const localToday = new Date(today.getTime() - (today.getTimezoneOffset() * 60000)).toISOString().split('T')[0];
    txDateInput.value = localToday;
    setTimeValueToForm(getCurrentTimeString());
    hideTimeError();
    
    formTitle.textContent = 'Tambah Transaksi';
    btnCancel.classList.add('hidden');
    // Default to expense
    txTypeInputs[0].checked = true;
    updateTypeUI('expense');
    populateWalletOptions();
}

async function handleSubmit(e) {
    e.preventDefault();

    const timeValue = getTimeValueFromForm();
    if (!timeValue) {
        showTimeError();
        return;
    }
    hideTimeError();

    const amount = parseAmount(txAmountInput.value);
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

    const selectedWalletId = (txWalletInput && txWalletInput.value) ? txWalletInput.value : DEFAULT_WALLET_ID;
    localStorage.setItem(LAST_WALLET_KEY, selectedWalletId);

    let selectedToWalletId = null;
    let selectedCategory = '';

    if (selectedType === 'transfer') {
        selectedToWalletId = txToWalletInput ? txToWalletInput.value : '';
        if (!selectedWalletId) {
            alert('Pilih dompet asal.');
            return;
        }
        if (!selectedToWalletId) {
            alert('Pilih dompet tujuan.');
            return;
        }
        if (selectedWalletId === selectedToWalletId) {
            alert('Dompet tujuan harus berbeda dari dompet asal.');
            return;
        }
        selectedCategory = 'Transfer';
    } else {
        selectedCategory = txCategoryInput ? txCategoryInput.value.trim() : '';
        if (!selectedCategory) {
            alert('Pilih kategori.');
            return;
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
        walletId: selectedWalletId,
        category: selectedCategory,
        date: txDateInput.value,
        time: timeValue,
        note: txNoteInput.value.trim(),
        createdAt,
        updatedAt: now,
        deleted: false
    };

    if (selectedType === 'transfer') {
        tx.toWalletId = selectedToWalletId;
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
    setTimeValueToForm(tx.time || '');
    hideTimeError();
    txAmountInput.value = formatNumber(tx.amount);
    txNoteInput.value = tx.note || '';

    updateTypeUI(tx.type);

    if (tx.type === 'transfer') {
        populateWalletOptions(tx.walletId || DEFAULT_WALLET_ID, tx.toWalletId || null);
        formTitle.textContent = 'Edit Transfer';
    } else {
        // Pilihan dompet: gunakan walletId transaksi atau fallback DEFAULT_WALLET_ID
        populateWalletOptions(tx.walletId || DEFAULT_WALLET_ID);

        // Pilihan kategori mengikuti tipe yang dipilih
        const list = (currentCategories && currentCategories[tx.type]) ? currentCategories[tx.type] : [];
        if (list.includes(tx.category)) {
            populateCategoryOptions(tx.category);
        } else {
            // Transaksi lama dengan kategori yang tidak ada di daftar tipenya
            // Minta pengguna memilih kategori dari daftar yang sesuai
            populateCategoryOptions(null, tx.category);
        }
        formTitle.textContent = 'Edit Transaksi';
    }

    btnCancel.classList.remove('hidden');
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

function handleDelete(id) {
    if (!id) return;
    const tx = currentTransactions.find(t => t.id === id);
    pendingDeleteId = id;
    pendingCategoryDelete = null;
    pendingBudgetDelete = null;
    pendingWalletDelete = null;
    if (confirmDialogTitle) {
        confirmDialogTitle.textContent = (tx && tx.type === 'transfer') ? 'Hapus transfer ini?' : 'Hapus transaksi ini?';
    }
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
attachAmountInputFormatting(txAmountInput);
attachAmountInputFormatting(inputBudgetAmount);

txTypeInputs.forEach(radio => {
    radio.addEventListener('change', () => {
        if (radio.value === 'transfer') {
            if (getActiveWallets().length < 2) {
                alert('Transfer membutuhkan minimal 2 dompet. Silakan tambah dompet baru terlebih dahulu di Pengaturan Dompet.');
                txTypeInputs[0].checked = true;
                updateTypeUI('expense');
                return;
            }
        }
        updateTypeUI(radio.value);
    });
});

if (confirmDialog) {
    btnConfirmCancel.addEventListener('click', () => {
        confirmDialog.close();
    });

    btnConfirmDelete.addEventListener('click', async () => {
        const txId = pendingDeleteId;
        const catDelete = pendingCategoryDelete;
        const bgtDelete = pendingBudgetDelete;
        const wltDelete = pendingWalletDelete;

        if (!txId && !catDelete && !bgtDelete && !wltDelete) {
            confirmDialog.close();
            return;
        }

        btnConfirmDelete.disabled = true;
        try {
            if (txId) {
                await performDelete(txId);
            } else if (catDelete) {
                await performDeleteCategory(catDelete.type, catDelete.name);
            } else if (bgtDelete) {
                await performDeleteBudget(bgtDelete);
            } else if (wltDelete) {
                await performDeleteWallet(wltDelete);
            }
        } finally {
            btnConfirmDelete.disabled = false;
            confirmDialog.close();
        }
    });

    confirmDialog.addEventListener('close', () => {
        pendingDeleteId = null;
        pendingCategoryDelete = null;
        pendingBudgetDelete = null;
        pendingWalletDelete = null;
        if (confirmDialogTitle) {
            confirmDialogTitle.textContent = 'Hapus transaksi ini?';
        }
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
        const cats = await getCategories();
        const bgs = await getBudgets();
        const wts = await getWallets();
        const data = {
            app: 'mizan-okane',
            schemaVersion: 1,
            exportedAt: new Date().toISOString(),
            transactions: allTx,
            categories: cats,
            budgets: bgs,
            wallets: wts
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

        const headers = ['tanggal', 'jam', 'dompet', 'tipe', 'kategori', 'jumlah', 'catatan'];
        const rows = [headers.join(',')];

        for (const tx of activeTx) {
            let walletName;
            if (tx.type === 'transfer') {
                walletName = `${getWalletName(tx.walletId)} -> ${getWalletName(tx.toWalletId)}`;
            } else {
                walletName = getWalletName(tx.walletId);
            }
            const row = [
                escapeCSVField(tx.date),
                escapeCSVField(tx.time || ''),
                escapeCSVField(walletName),
                escapeCSVField(tx.type),
                escapeCSVField(tx.category || (tx.type === 'transfer' ? 'Transfer' : '')),
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
        currentCategories = await getCategories();
        currentBudgets = await getBudgets();
        currentWallets = await getWallets();
        selectedBudgetMonth = getLocalYearMonthString();
        selectedDonutMonth = getLocalYearMonthString();
        resetForm(); // Set default date, kategori, dan dompet
        await loadData();
    } catch (error) {
        console.error("Gagal inisialisasi database", error);
        alert("Browser Anda tidak mendukung penyimpanan lokal atau terjadi kesalahan.");
        return;
    }

    if (formAddWallet && inputAddWallet) {
        formAddWallet.addEventListener('submit', (e) => {
            e.preventDefault();
            handleAddWallet(inputAddWallet);
        });
    }

    if (filterWalletSelect) {
        filterWalletSelect.addEventListener('change', () => {
            currentWalletFilter = filterWalletSelect.value;
            render();
        });
    }

    if (txWalletInput) {
        txWalletInput.addEventListener('change', () => {
            if (getSelectedType() === 'transfer' && txToWalletInput) {
                if (txToWalletInput.value === txWalletInput.value) {
                    const active = getActiveWallets();
                    const other = active.find(w => w.id !== txWalletInput.value);
                    if (other) {
                        txToWalletInput.value = other.id;
                    }
                }
            }
        });
    }

    if (txToWalletInput) {
        txToWalletInput.addEventListener('change', () => {
            if (getSelectedType() === 'transfer' && txWalletInput) {
                if (txWalletInput.value === txToWalletInput.value) {
                    const active = getActiveWallets();
                    const other = active.find(w => w.id !== txToWalletInput.value);
                    if (other) {
                        txWalletInput.value = other.id;
                    }
                }
            }
        });
    }

    if (formAddExpenseCat && inputAddExpenseCat) {
        formAddExpenseCat.addEventListener('submit', (e) => {
            e.preventDefault();
            handleAddCategory('expense', inputAddExpenseCat);
        });
    }

    if (formAddIncomeCat && inputAddIncomeCat) {
        formAddIncomeCat.addEventListener('submit', (e) => {
            e.preventDefault();
            handleAddCategory('income', inputAddIncomeCat);
        });
    }

    if (formSetBudget) {
        formSetBudget.addEventListener('submit', handleSetBudgetSubmit);
    }

    if (selectBudgetCat && inputBudgetAmount) {
        selectBudgetCat.addEventListener('change', () => {
            const cat = selectBudgetCat.value;
            const budgetMap = (currentBudgets && currentBudgets.items) ? currentBudgets.items : {};
            if (cat && budgetMap[cat]) {
                inputBudgetAmount.value = formatNumber(budgetMap[cat]);
            } else {
                inputBudgetAmount.value = '';
            }
        });
    }

    if (btnBudgetPrevMonth) {
        btnBudgetPrevMonth.addEventListener('click', () => shiftBudgetMonth(-1));
    }

    if (btnBudgetNextMonth) {
        btnBudgetNextMonth.addEventListener('click', () => shiftBudgetMonth(1));
    }

    if (budgetMonthInput) {
        budgetMonthInput.addEventListener('change', () => {
            if (budgetMonthInput.value) {
                selectedBudgetMonth = budgetMonthInput.value;
                renderBudgetSection();
            }
        });
    }

    if (btnChartExpense) {
        btnChartExpense.addEventListener('click', () => {
            if (currentChartType !== 'expense') {
                currentChartType = 'expense';
                renderMonthlyChart(currentTransactions);
            }
        });
    }

    if (btnChartIncome) {
        btnChartIncome.addEventListener('click', () => {
            if (currentChartType !== 'income') {
                currentChartType = 'income';
                renderMonthlyChart(currentTransactions);
            }
        });
    }

    if (btnDonutPrevMonth) {
        btnDonutPrevMonth.addEventListener('click', () => shiftDonutMonth(-1));
    }

    if (btnDonutNextMonth) {
        btnDonutNextMonth.addEventListener('click', () => shiftDonutMonth(1));
    }

    if (donutMonthInput) {
        donutMonthInput.addEventListener('change', () => {
            if (donutMonthInput.value) {
                selectedDonutMonth = donutMonthInput.value;
                activeDonutCategory = null;
                renderDonutChart(currentTransactions);
            }
        });
    }

    if (btnDonutExpense) {
        btnDonutExpense.addEventListener('click', () => {
            if (currentDonutType !== 'expense') {
                currentDonutType = 'expense';
                activeDonutCategory = null;
                renderDonutChart(currentTransactions);
            }
        });
    }

    if (btnDonutIncome) {
        btnDonutIncome.addEventListener('click', () => {
            if (currentDonutType !== 'income') {
                currentDonutType = 'income';
                activeDonutCategory = null;
                renderDonutChart(currentTransactions);
            }
        });
    }

    if (txTimeInput) {
        const sync24to12 = () => {
            setTimeValueToForm(txTimeInput.value);
            if (txTimeInput.value) {
                hideTimeError();
            }
        };
        txTimeInput.addEventListener('change', sync24to12);
        txTimeInput.addEventListener('input', sync24to12);
    }

    const sync12to24 = () => {
        const timeVal = getTimeValueFromForm();
        if (txTimeInput) {
            txTimeInput.value = timeVal;
        }
        if (timeVal) {
            hideTimeError();
        }
    };
    if (txTimeHourSelect) txTimeHourSelect.addEventListener('change', sync12to24);
    if (txTimeMinuteSelect) txTimeMinuteSelect.addEventListener('change', sync12to24);
    if (txTimeAmpmSelect) txTimeAmpmSelect.addEventListener('change', sync12to24);

    if (timeFormatRadios) {
        for (const radio of timeFormatRadios) {
            radio.addEventListener('change', async (e) => {
                if (e.target.checked) {
                    const newFormat = e.target.value === '12' ? '12' : '24';
                    currentTimeFormat = newFormat;
                    await saveTimeFormat(newFormat);
                    applyTimeFormatUI(newFormat);
                    render();
                }
            });
        }
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
