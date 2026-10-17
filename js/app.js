import { initDB, saveTransaction, getAllTransactions, replaceAllTransactions, getCategories, saveCategories, DEFAULT_CATEGORIES, getBudgets, saveBudgets, DEFAULT_BUDGETS, getWallets, saveWallets, DEFAULT_WALLET_ID, DEFAULT_WALLET_NAME, getTimeFormat, saveTimeFormat, DEFAULT_TIME_FORMAT, getSettings, saveSettings, DEFAULT_SETTINGS, resetDatabase, setMeta, isDemoMode, activateSampleData, clearSampleData, getRealTransactions, getRealCategories, getRealBudgets, getRealWallets } from './db.js';
import { initSync, markDirty, signIn, signOut, syncNow, ensureToken, clearRemoteData, isLinked } from './sync.js';

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
const themeRadios = document.getElementsByName('theme-setting');
const txAmountInput = document.getElementById('tx-amount');
const txAmountLabel = document.getElementById('tx-amount-label');
const txAmountPrefix = document.getElementById('tx-amount-prefix');
const txWalletLabel = document.getElementById('tx-wallet-label');
const txWalletInput = document.getElementById('tx-wallet');
const formGroupToWallet = document.getElementById('form-group-to-wallet');
const txToWalletInput = document.getElementById('tx-to-wallet');
const formGroupCategory = document.getElementById('form-group-category');
const txCategoryInput = document.getElementById('tx-category');
const formGroupSubcategory = document.getElementById('form-group-subcategory');
const txSubcategoryInput = document.getElementById('tx-subcategory');
const subcategoryChipsContainer = document.getElementById('subcategory-chips');
const txNoteInput = document.getElementById('tx-note');
const btnCancel = document.getElementById('btn-cancel');
const btnDeleteFromForm = document.getElementById('btn-delete-from-form');

const formScreen = document.getElementById('form-screen');
const formScreenBackdrop = document.getElementById('form-screen-backdrop');
const categoryChipsContainer = document.getElementById('category-chips');
const txDateDaySelect = document.getElementById('tx-date-day');
const txDateMonthSelect = document.getElementById('tx-date-month');
const txDateYearSelect = document.getElementById('tx-date-year');
const txTimeHour24Select = document.getElementById('tx-time-hour-24');
const txTimeMinute24Select = document.getElementById('tx-time-minute-24');

const txListContainer = document.getElementById('tx-list');
const recentTxListContainer = document.getElementById('recent-tx-list');
const btnSeeAllTx = document.getElementById('btn-see-all-tx');
const btnFabAdd = document.getElementById('btn-fab-add');
const btnFormBack = document.getElementById('btn-form-back');
const settingsSyncStatusEl = document.getElementById('settings-sync-status');

const filterWalletSelect = document.getElementById('filter-wallet');
const berandaMonthLabel = document.getElementById('beranda-month-label');
const btnBerandaPrevMonth = document.getElementById('btn-beranda-prev-month');
const btnBerandaNextMonth = document.getElementById('btn-beranda-next-month');
const totalBalanceEl = document.getElementById('total-balance');
const totalIncomeEl = document.getElementById('total-income');
const totalExpenseEl = document.getElementById('total-expense');
const walletBalancesContainer = document.getElementById('wallet-balances');
const walletsSection = document.getElementById('wallets-section') || document.querySelector('.wallets-section');

const confirmDialog = document.getElementById('confirm-dialog');
const confirmDialogTitle = document.getElementById('confirm-dialog-title');
const btnConfirmCancel = document.getElementById('btn-confirm-cancel');
const btnConfirmDelete = document.getElementById('btn-confirm-delete');

const btnDeleteAllData = document.getElementById('btn-delete-all-data');
const deleteAllDialog = document.getElementById('delete-all-dialog');
const btnDialogExportJson = document.getElementById('btn-dialog-export-json');
const inputConfirmDeleteAll = document.getElementById('input-confirm-delete-all');
const btnDeleteAllCancel = document.getElementById('btn-delete-all-cancel');
const btnDeleteAllConfirm = document.getElementById('btn-delete-all-confirm');

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
const budgetSection = document.getElementById('budget-section') || document.querySelector('.budget-section');

const expenseCategoryList = document.getElementById('expense-category-list');
const incomeCategoryList = document.getElementById('income-category-list');
const formAddExpenseCat = document.getElementById('form-add-expense-cat');
const inputAddExpenseCat = document.getElementById('input-add-expense-cat');
const formAddIncomeCat = document.getElementById('form-add-income-cat');
const inputAddIncomeCat = document.getElementById('input-add-income-cat');

const btnStatsCurrencyIdr = document.getElementById('btn-stats-currency-idr');
const btnStatsCurrencyJpy = document.getElementById('btn-stats-currency-jpy');
const btnStatsCurrencyUsd = document.getElementById('btn-stats-currency-usd');
const statsCurrencyBar = document.getElementById('stats-currency-bar') || document.querySelector('#page-statistik .stats-currency-bar');
const btnBudgetCurrencyIdr = document.getElementById('btn-budget-currency-idr');
const btnBudgetCurrencyJpy = document.getElementById('btn-budget-currency-jpy');
const btnBudgetCurrencyUsd = document.getElementById('btn-budget-currency-usd');
const budgetCurrencyControl = document.getElementById('budget-currency-control') || document.querySelector('.budget-currency-control');

const btnBerandaTimbangan = document.getElementById('btn-beranda-timbangan');
const btnTimbanganBack = document.getElementById('btn-timbangan-back');
const btnTimbanganCurrencyIdr = document.getElementById('btn-timbangan-currency-idr');
const btnTimbanganCurrencyJpy = document.getElementById('btn-timbangan-currency-jpy');
const btnTimbanganCurrencyUsd = document.getElementById('btn-timbangan-currency-usd');
const timbanganCurrencyBar = document.getElementById('timbangan-currency-bar') || document.querySelector('#page-timbangan .stats-currency-bar');
const btnTimbanganPrevMonth = document.getElementById('btn-timbangan-prev-month');
const btnTimbanganNextMonth = document.getElementById('btn-timbangan-next-month');
const timbanganMonthLabel = document.getElementById('timbangan-month-label');
const timbanganScaleContainer = document.getElementById('timbangan-scale-container');

const calcPriceInput = document.getElementById('calc-price-input');
const calcPriceLabel = document.getElementById('calc-price-label');
const calcPricePrefix = document.getElementById('calc-price-prefix');
const calcCategorySelect = document.getElementById('calc-category-select');
const calcWalletSelect = document.getElementById('calc-wallet-select');
const calcStatusBanner = document.getElementById('calc-status-banner');
const calcStatusBadge = document.getElementById('calc-status-badge');
const calcStatusDesc = document.getElementById('calc-status-desc');
const calcResultWallet = document.getElementById('calc-result-wallet');
const calcResultBudgetRow = document.getElementById('calc-result-budget-row');
const calcResultBudget = document.getElementById('calc-result-budget');
const calcResultMonthlyScale = document.getElementById('calc-result-monthly-scale');

const walletSettingsList = document.getElementById('wallet-settings-list');
const formAddWallet = document.getElementById('form-add-wallet');
const inputAddWallet = document.getElementById('input-add-wallet');
const selectAddWalletCurrency = document.getElementById('select-add-wallet-currency');

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
let pendingLogout = false;
let pendingFillDummy = false;
let pendingClearDummy = false;
let editingCategory = null;
let editingSubcategory = null;
let addingSubcategoryFor = null;
let pendingSubcategoryDelete = null;
let editingBudgetCategory = null;
let editingWalletId = null;
let currentWalletFilter = 'all';
const LAST_WALLET_KEY = 'mizan-okane:last-wallet-id';
let currentTimeFormat = DEFAULT_TIME_FORMAT;
let currentChartType = 'expense';
let currentDonutType = 'expense';
let selectedBerandaMonth = '';
let selectedDonutMonth = '';
let activeDonutCategory = null;
let currentStatsCurrency = 'IDR';
let currentBudgetCurrency = 'IDR';
let currentTimbanganCurrency = 'IDR';
let selectedTimbanganMonth = '';

const THEME_STORAGE_KEY = 'mizan-theme';
const VALID_THEMES = ['system', 'light', 'dark'];

function getStoredTheme() {
    try {
        const val = localStorage.getItem(THEME_STORAGE_KEY);
        return VALID_THEMES.includes(val) ? val : 'system';
    } catch (e) {
        return 'system';
    }
}

function resolveIsDark(themeSetting) {
    if (themeSetting === 'dark') return true;
    if (themeSetting === 'light') return false;
    return !!(window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches);
}

function applyTheme(themeSetting) {
    const isDark = resolveIsDark(themeSetting);
    document.documentElement.setAttribute('data-theme', isDark ? 'dark' : 'light');
    const metaThemeColor = document.querySelector('meta[name="theme-color"]');
    if (metaThemeColor) {
        metaThemeColor.setAttribute('content', isDark ? '#0F1117' : '#F5F5F7');
    }
}

function applyThemeUI(themeSetting) {
    if (themeRadios) {
        for (const radio of themeRadios) {
            radio.checked = (radio.value === themeSetting);
        }
    }
    applyTheme(themeSetting);
}

export async function getAppSettings() {
    const dbSettings = await getSettings();
    const storedTheme = getStoredTheme();
    const format = currentTimeFormat || await getTimeFormat();
    return {
        theme: storedTheme,
        timeFormat: format,
        updatedAt: dbSettings.updatedAt || 0
    };
}

// Token Palet DESIGN.md untuk Diagram Donut
// Makna: Pengeluaran #FF453A, Pemasukan accent, Transfer #4A9EFF, Warn #F5A623, dll.
const DONUT_EXPENSE_DARK = ['#FF453A', '#F5A623', '#4A9EFF', '#2ECC8F', '#9194A1', '#2A2D3A', '#1F2232'];
const DONUT_EXPENSE_LIGHT = ['#FF453A', '#F5A623', '#4A9EFF', '#24A574', '#6B6D78', '#D8D8DC', '#F0F0F2'];
const DONUT_INCOME_DARK = ['#2ECC8F', '#4A9EFF', '#F5A623', '#FF453A', '#9194A1', '#2A2D3A', '#1F2232'];
const DONUT_INCOME_LIGHT = ['#24A574', '#4A9EFF', '#F5A623', '#FF453A', '#6B6D78', '#D8D8DC', '#F0F0F2'];

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

const VALID_TABS = ['beranda', 'riwayat', 'statistik', 'timbangan', 'pengaturan'];
let currentActiveTab = 'beranda';
let tabHistory = ['beranda'];
let isNavigatingBack = false;

function getTabFromHash() {
    const raw = window.location.hash.replace('#', '').trim();
    return VALID_TABS.includes(raw) ? raw : 'beranda';
}

function recordTabNavigation(tabId) {
    if (!VALID_TABS.includes(tabId)) {
        tabId = 'beranda';
    }
    if (tabId === 'beranda') {
        tabHistory = ['beranda'];
    } else {
        tabHistory = tabHistory.filter(t => t !== tabId);
        if (tabHistory.length === 0 || tabHistory[0] !== 'beranda') {
            tabHistory.unshift('beranda');
        }
        tabHistory.push(tabId);
    }
    currentActiveTab = tabId;
}

function switchTab(tabId) {
    if (!VALID_TABS.includes(tabId)) {
        tabId = 'beranda';
    }

    currentActiveTab = tabId;

    // Tampilkan panel halaman yang sesuai, sembunyikan yang lain
    document.querySelectorAll('.page-view').forEach(view => {
        const isTarget = view.id === `page-${tabId}`;
        view.classList.toggle('hidden', !isTarget);
    });

    // Perbarui status aktif tombol tab pada desktop nav dan mobile nav
    document.querySelectorAll('.nav-tab-btn').forEach(btn => {
        const isTarget = btn.dataset.tab === tabId;
        btn.classList.toggle('is-active', isTarget);
        btn.setAttribute('aria-selected', String(isTarget));
    });

    if (tabId === 'timbangan') {
        renderTimbangan();
    }

    if (tabId === 'statistik') {
        updateStatsCurrencyUI();
    }

    if (tabId === 'pengaturan') {
        closeSettingsSubmenu();
    }

    window.scrollTo({ top: 0, behavior: 'instant' });
}

let activeSettingsSubmenu = null;

export function openSettingsSubmenu(submenuId) {
    activeSettingsSubmenu = submenuId;
    const mainView = document.getElementById('settings-main-view');
    if (mainView) {
        mainView.classList.add('hidden');
    }
    document.querySelectorAll('.settings-submenu-view').forEach(view => {
        const isMatch = view.dataset.submenuId === submenuId;
        view.classList.toggle('hidden', !isMatch);
    });
    window.scrollTo({ top: 0, behavior: 'instant' });
}

export function closeSettingsSubmenu() {
    activeSettingsSubmenu = null;
    document.querySelectorAll('.settings-submenu-view').forEach(view => {
        view.classList.add('hidden');
    });
    const mainView = document.getElementById('settings-main-view');
    if (mainView) {
        mainView.classList.remove('hidden');
    }
    window.scrollTo({ top: 0, behavior: 'instant' });
}

window.handleAndroidBack = function() {
    // 0. Jika popover status sinkron di header sedang terbuka, tutup popover
    const headerSyncPopover = document.getElementById('header-sync-popover');
    if (headerSyncPopover && !headerSyncPopover.classList.contains('hidden')) {
        headerSyncPopover.classList.add('hidden');
        const headerSyncBtn = document.getElementById('header-sync-btn');
        if (headerSyncBtn) headerSyncBtn.setAttribute('aria-expanded', 'false');
        return true;
    }

    // 1. Jika dialog konfirmasi sedang terbuka, tutup dialog
    if (confirmDialog && confirmDialog.open) {
        confirmDialog.close();
        pendingDeleteId = null;
        pendingCategoryDelete = null;
        pendingBudgetDelete = null;
        pendingWalletDelete = null;
        pendingLogout = false;
        if (btnConfirmDelete) {
            btnConfirmDelete.textContent = 'Hapus';
        }
        if (confirmDialogTitle) {
            confirmDialogTitle.textContent = 'Hapus transaksi ini?';
        }
        return true;
    }

    if (deleteAllDialog && deleteAllDialog.open) {
        deleteAllDialog.close();
        return true;
    }

    // 2. Jika layar formulir (tambah/edit transaksi) sedang terbuka, tutup formulir
    if (formScreen && !formScreen.classList.contains('hidden')) {
        resetForm();
        closeFormScreen();
        return true;
    }

    // 2b. Jika sedang berada di submenu Pengaturan, kembali ke daftar Pengaturan
    if (currentActiveTab === 'pengaturan' && activeSettingsSubmenu) {
        closeSettingsSubmenu();
        return true;
    }

    // 3. Jika sedang berada di tab dalam (selain beranda), kembali ke tab sebelumnya
    if (currentActiveTab !== 'beranda') {
        if (tabHistory.length > 1) {
            tabHistory.pop(); // buang tab aktif saat ini
            const prevTab = tabHistory[tabHistory.length - 1] || 'beranda';
            currentActiveTab = prevTab;
            isNavigatingBack = true;
            window.location.hash = prevTab;
            switchTab(prevTab);
            isNavigatingBack = false;
            return true;
        } else {
            // Fallback jika riwayat kosong tetapi masih di tab dalam
            currentActiveTab = 'beranda';
            tabHistory = ['beranda'];
            isNavigatingBack = true;
            window.location.hash = 'beranda';
            switchTab('beranda');
            isNavigatingBack = false;
            return true;
        }
    }

    // 4. Berada di halaman utama (Beranda) tanpa modal/form terbuka -> biarkan aplikasi keluar
    return false;
};

function openFormScreen() {
    if (!formScreen) return;
    formScreen.classList.remove('hidden');
    if (btnFabAdd) btnFabAdd.classList.add('hidden');
    document.body.classList.add('modal-open');
    if (txAmountInput) {
        setTimeout(() => txAmountInput.focus(), 50);
    }
}

function closeFormScreen() {
    if (!formScreen) return;
    formScreen.classList.add('hidden');
    if (btnFabAdd) btnFabAdd.classList.remove('hidden');
    document.body.classList.remove('modal-open');
}

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

export function getSubcategoriesFor(type, categoryName) {
    if (!currentCategories || !currentCategories.subcategories || !categoryName) return [];
    const group = currentCategories.subcategories[type];
    if (!group || typeof group !== 'object') return [];
    const subs = group[categoryName];
    return Array.isArray(subs) ? subs : [];
}

export function getTxSubcategory(tx) {
    if (!tx || !tx.subcategory || tx.type === 'transfer') return '';
    const subs = getSubcategoriesFor(tx.type, tx.category);
    return subs.includes(tx.subcategory) ? tx.subcategory : '';
}

export function getTxCategoryDisplayText(tx) {
    if (!tx) return '';
    if (tx.type === 'transfer') return 'Transfer';
    const cat = tx.category || '';
    if (!cat) return '';
    const sub = getTxSubcategory(tx);
    return sub ? `${cat} > ${sub}` : cat;
}

export function populateSubcategoryOptions(categoryName, selectedSub = null) {
    if (!formGroupSubcategory || !subcategoryChipsContainer) return;
    const type = getSelectedType();
    const subs = getSubcategoriesFor(type, categoryName);

    if (subs.length === 0) {
        formGroupSubcategory.classList.add('hidden');
        if (txSubcategoryInput) txSubcategoryInput.value = '';
        subcategoryChipsContainer.innerHTML = '';
        return;
    }

    formGroupSubcategory.classList.remove('hidden');
    let targetSub = '';
    if (selectedSub && subs.includes(selectedSub)) {
        targetSub = selectedSub;
    }
    if (txSubcategoryInput) {
        txSubcategoryInput.value = targetSub;
    }

    let chipsHtml = '';
    for (const sub of subs) {
        const isSel = targetSub === sub;
        chipsHtml += `<button type="button" class="category-chip ${isSel ? 'is-selected' : ''}" data-value="${escapeHTML(sub)}" role="radio" aria-checked="${String(isSel)}">${escapeHTML(sub)}</button>`;
    }
    subcategoryChipsContainer.innerHTML = chipsHtml;

    subcategoryChipsContainer.querySelectorAll('.category-chip').forEach(chip => {
        chip.addEventListener('click', () => {
            const val = chip.dataset.value;
            const wasSelected = chip.classList.contains('is-selected');
            if (wasSelected) {
                if (txSubcategoryInput) txSubcategoryInput.value = '';
                chip.classList.remove('is-selected');
                chip.setAttribute('aria-checked', 'false');
            } else {
                if (txSubcategoryInput) txSubcategoryInput.value = val;
                subcategoryChipsContainer.querySelectorAll('.category-chip').forEach(c => {
                    const active = c.dataset.value === val;
                    c.classList.toggle('is-selected', active);
                    c.setAttribute('aria-checked', String(active));
                });
            }
        });
    });
}

function populateCategoryOptions(selectedVal = null, oldCustomCategory = null, selectedSub = null) {
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

    let targetVal = '';
    if (selectedVal && list.includes(selectedVal)) {
        targetVal = selectedVal;
        txCategoryInput.value = selectedVal;
    } else if (oldCustomCategory && !list.includes(oldCustomCategory) && selectedVal === oldCustomCategory) {
        targetVal = oldCustomCategory;
    } else {
        txCategoryInput.value = '';
    }

    // Render Chip Kategori
    if (categoryChipsContainer) {
        let chipsHtml = '';
        if (oldCustomCategory && !list.includes(oldCustomCategory)) {
            const isSel = targetVal === oldCustomCategory;
            chipsHtml += `<button type="button" class="category-chip ${isSel ? 'is-selected' : ''}" data-value="${escapeHTML(oldCustomCategory)}" role="radio" aria-checked="${String(isSel)}">${escapeHTML(oldCustomCategory)}</button>`;
        }
        for (const cat of list) {
            const isSel = targetVal === cat;
            chipsHtml += `<button type="button" class="category-chip ${isSel ? 'is-selected' : ''}" data-value="${escapeHTML(cat)}" role="radio" aria-checked="${String(isSel)}">${escapeHTML(cat)}</button>`;
        }
        categoryChipsContainer.innerHTML = chipsHtml;

        categoryChipsContainer.querySelectorAll('.category-chip').forEach(chip => {
            chip.addEventListener('click', () => {
                const val = chip.dataset.value;
                txCategoryInput.value = val;
                categoryChipsContainer.querySelectorAll('.category-chip').forEach(c => {
                    const active = c.dataset.value === val;
                    c.classList.toggle('is-selected', active);
                    c.setAttribute('aria-checked', String(active));
                });
                populateSubcategoryOptions(val, null);
            });
        });
    }

    populateSubcategoryOptions(targetVal, selectedSub);
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
    if (formGroupSubcategory) {
        formGroupSubcategory.classList.toggle('hidden', isTransfer);
    }
    if (txCategoryInput) {
        txCategoryInput.required = !isTransfer;
    }

    if (isTransfer) {
        if (txSubcategoryInput) {
            txSubcategoryInput.value = '';
        }
        const fromCurr = getWalletCurrency(txWalletInput ? txWalletInput.value : null);
        const active = getActiveWallets();
        const sameCurrWallets = active.filter(w => getWalletCurrency(w.id) === fromCurr);
        if (sameCurrWallets.length < 2) {
            const idrCount = active.filter(w => getWalletCurrency(w.id) === 'IDR').length;
            const jpyCount = active.filter(w => getWalletCurrency(w.id) === 'JPY').length;
            const usdCount = active.filter(w => getWalletCurrency(w.id) === 'USD').length;
            if (idrCount < 2 && jpyCount < 2 && usdCount < 2) {
                showNotification('Transfer butuh minimal 2 dompet dengan mata uang yang sama.', 'warn');
                txTypeInputs[0].checked = true;
                updateTypeUI('expense');
                return;
            } else {
                const candidate = active.find(w => {
                    const c = getWalletCurrency(w.id);
                    return (c === 'IDR' && idrCount >= 2) || (c === 'JPY' && jpyCount >= 2) || (c === 'USD' && usdCount >= 2);
                });
                if (candidate) {
                    populateWalletOptions(candidate.id);
                } else {
                    populateWalletOptions();
                }
            }
        } else {
            populateWalletOptions(txWalletInput ? txWalletInput.value : null, txToWalletInput ? txToWalletInput.value : null);
        }
    } else {
        const currentVal = txCategoryInput ? txCategoryInput.value : '';
        const currentSub = txSubcategoryInput ? txSubcategoryInput.value : null;
        const list = (currentCategories && currentCategories[type]) ? currentCategories[type] : [];
        const keepVal = list.includes(currentVal) ? currentVal : null;
        populateCategoryOptions(keepVal, null, currentSub);
    }
}

function getActiveWallets() {
    return (currentWallets || []).filter(w => !w.deleted);
}

function isWalletDeleted(walletId) {
    const id = walletId || DEFAULT_WALLET_ID;
    const found = (currentWallets || []).find(w => w.id === id);
    return !!(found && found.deleted);
}

function getWalletName(walletId) {
    const id = walletId || DEFAULT_WALLET_ID;
    const found = (currentWallets || []).find(w => w.id === id);
    if (found && found.name) return found.name;
    if (id === DEFAULT_WALLET_ID) return DEFAULT_WALLET_NAME;
    return DEFAULT_WALLET_NAME;
}

export function getAvailableCurrencies() {
    const activeWallets = getActiveWallets();
    const currencies = ['IDR'];
    if (activeWallets.some(w => w.currency === 'JPY')) {
        currencies.push('JPY');
    }
    if (activeWallets.some(w => w.currency === 'USD')) {
        currencies.push('USD');
    }
    return currencies;
}

export function getWalletCurrency(walletId) {
    const id = walletId || DEFAULT_WALLET_ID;
    const found = (currentWallets || []).find(w => w.id === id);
    if (found && found.currency) {
        return (found.currency === 'JPY' || found.currency === 'USD') ? found.currency : 'IDR';
    }
    return 'IDR';
}

export function getTransactionCurrency(tx) {
    if (!tx) return 'IDR';
    return getWalletCurrency(tx.walletId || DEFAULT_WALLET_ID);
}

function updateFormCurrencyUI() {
    const selectedWalletId = txWalletInput ? txWalletInput.value : DEFAULT_WALLET_ID;
    const curr = getWalletCurrency(selectedWalletId);
    if (txAmountPrefix) {
        if (curr === 'USD') {
            txAmountPrefix.textContent = '$';
        } else if (curr === 'JPY') {
            txAmountPrefix.textContent = '¥';
        } else {
            txAmountPrefix.textContent = 'Rp';
        }
    }
    if (txAmountLabel) {
        if (curr === 'USD') {
            txAmountLabel.textContent = 'Jumlah ($)';
        } else if (curr === 'JPY') {
            txAmountLabel.textContent = 'Jumlah (¥)';
        } else {
            txAmountLabel.textContent = 'Jumlah (Rp)';
        }
    }
    if (txAmountInput) {
        txAmountInput.setAttribute('inputmode', curr === 'USD' ? 'decimal' : 'numeric');
        if (txAmountInput.value) {
            if (curr !== 'USD' && txAmountInput.value.includes(',')) {
                txAmountInput.value = txAmountInput.value.split(',')[0];
            }
            formatInputWithCursor(txAmountInput, curr);
        }
    }
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
        const oldCurr = getWalletCurrency(selectedFromVal);
        html += `<option value="${escapeHTML(selectedFromVal)}" selected>${escapeHTML(oldWalletName)} (${oldCurr}) (dihapus)</option>`;
    }

    for (const w of active) {
        const isSelected = targetId === w.id;
        const curr = getWalletCurrency(w.id);
        html += `<option value="${escapeHTML(w.id)}" ${isSelected ? 'selected' : ''}>${escapeHTML(w.name)} (${curr})</option>`;
    }

    txWalletInput.innerHTML = html;
    if (targetId) {
        txWalletInput.value = targetId;
    }

    if (txToWalletInput) {
        // Transfer hanya antar dompet semata uang!
        const fromCurr = getWalletCurrency(targetId);
        const eligibleWallets = active.filter(w => w.id !== targetId && getWalletCurrency(w.id) === fromCurr);

        let targetToId = selectedToVal;
        if (!targetToId || !eligibleWallets.some(w => w.id === targetToId)) {
            targetToId = eligibleWallets.length > 0 ? eligibleWallets[0].id : '';
        }

        let toHtml = '';
        if (eligibleWallets.length === 0) {
            toHtml = `<option value="" disabled selected>Tidak ada dompet lain (${fromCurr})</option>`;
        } else {
            toHtml = `<option value="" disabled ${!targetToId ? 'selected' : ''}>Pilih dompet tujuan</option>`;
            if (selectedToVal && !eligibleWallets.some(w => w.id === selectedToVal) && selectedToVal !== targetId) {
                const oldWalletName = getWalletName(selectedToVal);
                const oldCurr = getWalletCurrency(selectedToVal);
                toHtml += `<option value="${escapeHTML(selectedToVal)}" selected>${escapeHTML(oldWalletName)} (${oldCurr}) (dihapus)</option>`;
            }

            for (const w of eligibleWallets) {
                const isSelected = targetToId === w.id;
                const curr = getWalletCurrency(w.id);
                toHtml += `<option value="${escapeHTML(w.id)}" ${isSelected ? 'selected' : ''}>${escapeHTML(w.name)} (${curr})</option>`;
            }
        }

        txToWalletInput.innerHTML = toHtml;
        if (targetToId) {
            txToWalletInput.value = targetToId;
        }
    }

    updateFormCurrencyUI();
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
        const curr = getWalletCurrency(w.id);
        html += `<option value="${escapeHTML(w.id)}" ${isSelected ? 'selected' : ''}>${escapeHTML(w.name)} (${curr})</option>`;
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

export function handleWalletCardClick(walletId) {
    if (!walletId) return;
    const targetVal = (currentWalletFilter === walletId) ? 'all' : walletId;
    if (filterWalletSelect) {
        filterWalletSelect.value = targetVal;
        currentWalletFilter = filterWalletSelect.value;
    } else {
        currentWalletFilter = targetVal;
    }
    render();
    recordTabNavigation('riwayat');
    window.location.hash = 'riwayat';
    switchTab('riwayat');
}

// Formatter angka dan uang (titik pemisah ribuan)
export function formatNumber(val) {
    if (val === null || val === undefined || val === '') return '';
    if (typeof val === 'number') {
        if (!Number.isFinite(val)) return '';
        const rounded = Math.round(val);
        const isNeg = rounded < 0;
        const absStr = String(Math.abs(rounded));
        const formatted = absStr.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
        return isNeg ? `-${formatted}` : formatted;
    }
    const str = String(val).trim();
    if (!str) return '';
    const isNegative = str.includes('-');
    let clean = str;
    if (clean.includes('.') && clean.includes(',')) {
        if (clean.lastIndexOf(',') > clean.lastIndexOf('.')) {
            clean = clean.slice(0, clean.lastIndexOf(','));
        } else {
            clean = clean.slice(0, clean.lastIndexOf('.'));
        }
    } else if (/,\d{1,2}$/.test(clean)) {
        clean = clean.slice(0, clean.lastIndexOf(','));
    }
    let digits = clean.replace(/\D/g, '');
    if (!digits) return '';
    digits = digits.replace(/^0+(?=\d)/, '');
    const formatted = digits.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
    return (isNegative && formatted !== '0') ? `-${formatted}` : formatted;
}

export function formatAmountForInput(amount, currency = 'IDR') {
    if (amount === null || amount === undefined || amount === '' || isNaN(amount)) return '';
    const num = Number(amount);
    if (currency === 'USD') {
        const rounded = Math.round(num * 100) / 100;
        const str = String(rounded);
        const parts = str.split('.');
        const intFormatted = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');
        if (parts.length > 1) {
            return `${intFormatted},${parts[1]}`;
        }
        return intFormatted;
    }
    return formatNumber(Math.round(num));
}

export function parseAmount(str, currency = 'IDR') {
    if (str === null || str === undefined) return 0;
    const s = String(str).trim();
    if (!s) return 0;

    if (currency === 'USD') {
        let normalized = s;
        if (normalized.includes('.') && normalized.includes(',')) {
            if (normalized.lastIndexOf(',') > normalized.lastIndexOf('.')) {
                normalized = normalized.replace(/\./g, '').replace(',', '.');
            } else {
                normalized = normalized.replace(/,/g, '');
            }
        } else if (normalized.includes(',')) {
            normalized = normalized.replace(',', '.');
        }
        normalized = normalized.replace(/[^0-9.-]/g, '');
        const parsed = parseFloat(normalized);
        if (isNaN(parsed)) return 0;
        return Math.round(parsed * 100) / 100;
    }

    const isNegative = s.includes('-');
    let clean = s;
    if (clean.includes('.') && clean.includes(',')) {
        if (clean.lastIndexOf(',') > clean.lastIndexOf('.')) {
            clean = clean.slice(0, clean.lastIndexOf(','));
        } else {
            clean = clean.slice(0, clean.lastIndexOf('.'));
        }
    } else if (/,\d{1,2}$/.test(clean)) {
        clean = clean.slice(0, clean.lastIndexOf(','));
    }
    const digits = clean.replace(/\D/g, '');
    const val = digits ? parseInt(digits, 10) : 0;
    return (isNegative && val !== 0) ? -val : val;
}

export function formatCurrency(val, currency = 'IDR') {
    const isUsd = currency === 'USD';
    const isJpy = currency === 'JPY';
    const num = typeof val === 'number'
        ? (isUsd ? Math.round(val * 100) / 100 : Math.round(val))
        : parseAmount(val, currency);

    if (val === null || val === undefined || val === '' || isNaN(num)) {
        if (isUsd) return '$ 0,00';
        if (isJpy) return '¥ 0';
        return 'Rp 0';
    }

    const prefix = isUsd ? '$ ' : (isJpy ? '¥ ' : 'Rp ');
    const absNum = Math.abs(num);

    let formattedNumberStr;
    if (isUsd) {
        const fixed = absNum.toFixed(2);
        const [intPart, decPart] = fixed.split('.');
        const intFormatted = intPart.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
        formattedNumberStr = `${intFormatted},${decPart}`;
    } else {
        formattedNumberStr = formatNumber(absNum);
    }

    if (num < 0) {
        return `-${prefix}${formattedNumberStr}`;
    }
    return `${prefix}${formattedNumberStr}`;
}

export const currencyFormatter = {
    format(val, currency = 'IDR') {
        return formatCurrency(val, currency);
    }
};

export function formatInputWithCursor(input, currency = 'IDR') {
    if (!input) return;
    const isUsd = currency === 'USD';
    const originalValue = input.value;
    const originalCursor = input.selectionStart !== null ? input.selectionStart : originalValue.length;

    if (!isUsd) {
        let valToFormat = originalValue;
        if (valToFormat.includes(',') && valToFormat.includes('.')) {
            if (valToFormat.lastIndexOf(',') > valToFormat.lastIndexOf('.')) {
                valToFormat = valToFormat.slice(0, valToFormat.lastIndexOf(','));
            } else {
                valToFormat = valToFormat.slice(0, valToFormat.lastIndexOf('.'));
            }
        } else if (/,\d{1,2}$/.test(valToFormat)) {
            valToFormat = valToFormat.slice(0, valToFormat.lastIndexOf(','));
        }

        const textBeforeCursor = valToFormat.slice(0, originalCursor);
        const digitsBeforeCursor = textBeforeCursor.replace(/\D/g, '').length;

        const rawDigits = valToFormat.replace(/\D/g, '');
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
        return;
    }

    // --- Format Input USD (Mendukung Koma Desimal, maks 2 angka) ---
    if (!originalValue) {
        input.value = '';
        return;
    }

    const hasComma = originalValue.includes(',');
    const hasDot = originalValue.includes('.');

    let intPartRaw = '';
    let decPartRaw = '';
    let isDecimalMode = false;
    let sepIndex = -1;

    if (hasComma) {
        const parts = originalValue.split(',');
        intPartRaw = parts[0];
        decPartRaw = parts.slice(1).join('');
        isDecimalMode = true;
        sepIndex = originalValue.indexOf(',');
    } else if (hasDot && /^\d*\.\d*$/.test(originalValue)) {
        const parts = originalValue.split('.');
        intPartRaw = parts[0];
        decPartRaw = parts.slice(1).join('');
        isDecimalMode = true;
        sepIndex = originalValue.indexOf('.');
    } else {
        intPartRaw = originalValue;
        decPartRaw = '';
    }

    let intDigits = intPartRaw.replace(/\D/g, '');
    if (intDigits.length > 1) {
        intDigits = intDigits.replace(/^0+(?=\d)/, '');
    }

    const decDigits = decPartRaw.replace(/\D/g, '').slice(0, 2);

    if (!intDigits && !isDecimalMode) {
        input.value = '';
        return;
    }

    if (!intDigits && isDecimalMode) {
        intDigits = '0';
    }

    const formattedInt = intDigits.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
    const formatted = isDecimalMode ? `${formattedInt},${decDigits}` : formattedInt;
    input.value = formatted;

    const textBefore = originalValue.slice(0, originalCursor);
    const commaBefore = isDecimalMode && (sepIndex < originalCursor);

    if (!commaBefore) {
        const digitsBefore = textBefore.replace(/\D/g, '').length;
        if (digitsBefore === 0) {
            input.setSelectionRange(0, 0);
            return;
        }
        const targetDigits = Math.min(digitsBefore, intDigits.length);
        let digitCount = 0;
        let newCursor = formattedInt.length;
        for (let i = 0; i < formattedInt.length; i++) {
            if (/\d/.test(formattedInt[i])) {
                digitCount++;
                if (digitCount === targetDigits) {
                    newCursor = i + 1;
                    break;
                }
            }
        }
        input.setSelectionRange(newCursor, newCursor);
    } else {
        const commaPosInFormatted = formatted.indexOf(',');
        const decTextBefore = textBefore.slice(sepIndex + 1);
        const decDigitsBefore = decTextBefore.replace(/\D/g, '').length;
        const newCursor = Math.min(commaPosInFormatted + 1 + decDigitsBefore, formatted.length);
        input.setSelectionRange(newCursor, newCursor);
    }
}

export function attachAmountInputFormatting(input, getCurrencyFn = () => 'IDR') {
    if (!input) return;

    input.addEventListener('keydown', (e) => {
        const curr = typeof getCurrencyFn === 'function' ? getCurrencyFn() : 'IDR';
        const isUsd = curr === 'USD';

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
                    formatInputWithCursor(input, curr);
                }
            } else if (e.key === 'Delete' && input.selectionStart === input.selectionEnd && input.selectionStart < input.value.length) {
                if (input.value[input.selectionStart] === '.') {
                    e.preventDefault();
                    const pos = input.selectionStart;
                    const val = input.value;
                    input.value = val.slice(0, pos) + val.slice(pos + 2);
                    input.setSelectionRange(pos, pos);
                    formatInputWithCursor(input, curr);
                }
            }
            return;
        }

        // Koma atau titik desimal hanya untuk USD
        if (isUsd && (e.key === ',' || e.key === '.')) {
            e.preventDefault();
            const val = input.value;
            const start = input.selectionStart;
            const end = input.selectionEnd;
            const remaining = val.slice(0, start) + val.slice(end);

            if (!remaining.includes(',')) {
                const prefix = (start === 0 && (!val || end === val.length)) ? '0' : '';
                input.value = val.slice(0, start) + prefix + ',' + val.slice(end);
                const newPos = start + prefix.length + 1;
                input.setSelectionRange(newPos, newPos);
                formatInputWithCursor(input, curr);
            }
            return;
        }

        if (e.key.length === 1 && !/^\d$/.test(e.key)) {
            e.preventDefault();
            return;
        }

        // Untuk USD: batasi maksimal 2 desimal di belakang koma
        if (isUsd && /^\d$/.test(e.key)) {
            const val = input.value;
            const start = input.selectionStart;
            const end = input.selectionEnd;
            if (val.includes(',') && start === end) {
                const commaIdx = val.indexOf(',');
                if (start > commaIdx) {
                    const decPart = val.slice(commaIdx + 1);
                    if (decPart.length >= 2) {
                        e.preventDefault();
                        return;
                    }
                }
            }
        }
    });

    input.addEventListener('input', () => {
        const curr = typeof getCurrencyFn === 'function' ? getCurrencyFn() : 'IDR';
        formatInputWithCursor(input, curr);
    });
}

const INDO_MONTHS_NAMES = [
    { val: '01', name: 'Januari' },
    { val: '02', name: 'Februari' },
    { val: '03', name: 'Maret' },
    { val: '04', name: 'April' },
    { val: '05', name: 'Mei' },
    { val: '06', name: 'Juni' },
    { val: '07', name: 'Juli' },
    { val: '08', name: 'Agustus' },
    { val: '09', name: 'September' },
    { val: '10', name: 'Oktober' },
    { val: '11', name: 'November' },
    { val: '12', name: 'Desember' }
];

function initDateSelectOptions() {
    if (!txDateDaySelect || !txDateMonthSelect || !txDateYearSelect) return;

    let dayHtml = '';
    for (let d = 1; d <= 31; d++) {
        const val = String(d).padStart(2, '0');
        dayHtml += `<option value="${val}">${d}</option>`;
    }
    txDateDaySelect.innerHTML = dayHtml;

    let monthHtml = '';
    for (const m of INDO_MONTHS_NAMES) {
        monthHtml += `<option value="${m.val}">${m.name}</option>`;
    }
    txDateMonthSelect.innerHTML = monthHtml;

    const currentYear = new Date().getFullYear();
    let yearHtml = '';
    for (let y = currentYear - 10; y <= currentYear + 2; y++) {
        yearHtml += `<option value="${y}">${y}</option>`;
    }
    txDateYearSelect.innerHTML = yearHtml;

    const onDateSelectChange = () => {
        syncSelectsToDateInput();
    };

    txDateDaySelect.addEventListener('change', onDateSelectChange);
    txDateMonthSelect.addEventListener('change', onDateSelectChange);
    txDateYearSelect.addEventListener('change', onDateSelectChange);
}

function getDaysInMonth(year, month) {
    return new Date(year, month, 0).getDate();
}

function syncSelectsToDateInput() {
    if (!txDateDaySelect || !txDateMonthSelect || !txDateYearSelect || !txDateInput) return;
    const y = parseInt(txDateYearSelect.value, 10) || new Date().getFullYear();
    const m = parseInt(txDateMonthSelect.value, 10) || (new Date().getMonth() + 1);
    const maxDays = getDaysInMonth(y, m);

    let d = parseInt(txDateDaySelect.value, 10) || 1;
    if (d > maxDays) {
        d = maxDays;
        txDateDaySelect.value = String(d).padStart(2, '0');
    }

    const yStr = String(y);
    const mStr = String(m).padStart(2, '0');
    const dStr = String(d).padStart(2, '0');
    txDateInput.value = `${yStr}-${mStr}-${dStr}`;
}

function syncDateInputToSelects(dateStr) {
    if (!txDateDaySelect || !txDateMonthSelect || !txDateYearSelect) return;
    if (!dateStr) return;
    const match = String(dateStr).match(/^(\d{4})-(\d{2})-(\d{2})$/);
    if (!match) return;
    const yStr = match[1];
    const mStr = match[2];
    const dStr = match[3];

    if (!txDateYearSelect.querySelector(`option[value="${yStr}"]`)) {
        const opt = document.createElement('option');
        opt.value = yStr;
        opt.textContent = yStr;
        txDateYearSelect.appendChild(opt);
    }

    txDateYearSelect.value = yStr;
    txDateMonthSelect.value = mStr;
    txDateDaySelect.value = dStr;
}

function init24TimeSelectOptions() {
    if (!txTimeHour24Select || !txTimeMinute24Select) return;

    let hourHtml = '<option value="">Jam</option>';
    for (let h = 0; h < 24; h++) {
        const val = String(h).padStart(2, '0');
        hourHtml += `<option value="${val}">${val}</option>`;
    }
    txTimeHour24Select.innerHTML = hourHtml;

    let minuteHtml = '<option value="">Menit</option>';
    for (let m = 0; m < 60; m++) {
        const val = String(m).padStart(2, '0');
        minuteHtml += `<option value="${val}">${val}</option>`;
    }
    txTimeMinute24Select.innerHTML = minuteHtml;

    const on24SelectChange = () => {
        const h = txTimeHour24Select.value;
        const m = txTimeMinute24Select.value;
        if (h && m) {
            if (txTimeInput) txTimeInput.value = `${h}:${m}`;
            hideTimeError();
        } else {
            if (txTimeInput) txTimeInput.value = '';
        }
    };

    txTimeHour24Select.addEventListener('change', on24SelectChange);
    txTimeMinute24Select.addEventListener('change', on24SelectChange);
}

export function getCategoryIconSvg(categoryName, type) {
    if (type === 'transfer') {
        return `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><polyline points="17 1 21 5 17 9"></polyline><path d="M3 11V9a4 4 0 0 1 4-4h14"></path><polyline points="7 23 3 19 7 15"></polyline><path d="M21 13v2a4 4 0 0 1-4 4H3"></path></svg>`;
    }

    const norm = String(categoryName || '').trim().toLowerCase();
    switch (norm) {
        case 'makanan':
        case 'makanan & minuman':
        case 'kuliner':
            return `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M18 8h1a4 4 0 0 1 0 8h-1"></path><path d="M2 8h16v9a4 4 0 0 1-4 4H6a4 4 0 0 1-4-4V8z"></path><line x1="6" y1="1" x2="6" y2="4"></line><line x1="10" y1="1" x2="10" y2="4"></line><line x1="14" y1="1" x2="14" y2="4"></line></svg>`;
        case 'transportasi':
        case 'transport':
        case 'bensin':
        case 'kendaraan':
            return `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><rect x="3" y="11" width="18" height="9" rx="2"></rect><circle cx="7.5" cy="16.5" r="1.5"></circle><circle cx="16.5" cy="16.5" r="1.5"></circle><path d="M5 11l2-5h10l2 5"></path></svg>`;
        case 'belanja':
        case 'belanjaan':
        case 'shopping':
            return `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"></path><line x1="3" y1="6" x2="21" y2="6"></line><path d="M16 10a4 4 0 0 1-8 0"></path></svg>`;
        case 'tagihan':
        case 'listrik':
        case 'air':
        case 'pulsa':
        case 'internet':
            return `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path><polyline points="14 2 14 8 20 8"></polyline><line x1="16" y1="13" x2="8" y2="13"></line><line x1="16" y1="17" x2="8" y2="17"></line><line x1="10" y1="9" x2="8" y2="9"></line></svg>`;
        case 'hiburan':
        case 'hobi':
        case 'rekreasi':
            return `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="12" r="10"></circle><path d="M8 14s1.5 2 4 2 4-2 4-2"></path><line x1="9" y1="9" x2="9.01" y2="9"></line><line x1="15" y1="9" x2="15.01" y2="9"></line></svg>`;
        case 'gaji':
        case 'upah':
            return `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><rect x="2" y="7" width="20" height="14" rx="2" ry="2"></rect><path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"></path></svg>`;
        case 'bonus':
        case 'hadiah':
        case 'thr':
            return `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><polyline points="20 12 20 22 4 22 4 12"></polyline><rect x="2" y="7" width="20" height="5"></rect><line x1="12" y1="22" x2="12" y2="7"></line><path d="M12 7H7.5a2.5 2.5 0 0 1 0-5C11 2 12 7 12 7z"></path><path d="M12 7h4.5a2.5 2.5 0 0 0 0-5C13 2 12 7 12 7z"></path></svg>`;
        case 'investasi':
        case 'dividen':
        case 'saham':
        case 'reksadana':
            return `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><polyline points="23 6 13.5 15.5 8.5 10.5 1 18"></polyline><polyline points="17 6 23 6 23 12"></polyline></svg>`;
        default:
            if (type === 'income') {
                return `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="12" r="10"></circle><polyline points="12 8 8 12 12 16"></polyline><line x1="16" y1="12" x2="8" y2="12"></line></svg>`;
            }
            return `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"></path><line x1="7" y1="7" x2="7.01" y2="7"></line></svg>`;
    }
}

function formatGroupDate(dateString) {
    if (!dateString) return 'Lainnya';
    const match = dateString.match(/^(\d{4})-(\d{2})-(\d{2})$/);
    if (!match) return dateString;
    const y = parseInt(match[1], 10);
    const m = parseInt(match[2], 10);
    const d = parseInt(match[3], 10);

    const now = new Date();
    const todayY = now.getFullYear();
    const todayM = now.getMonth() + 1;
    const todayD = now.getDate();

    if (y === todayY && m === todayM && d === todayD) {
        return 'Hari ini';
    }

    const yesterday = new Date(now);
    yesterday.setDate(yesterday.getDate() - 1);
    if (y === yesterday.getFullYear() && m === (yesterday.getMonth() + 1) && d === yesterday.getDate()) {
        return 'Kemarin';
    }

    const mShort = INDO_MONTHS_SHORT[m - 1] || '';
    if (y === todayY) {
        return `${d} ${mShort}`;
    }
    return `${d} ${mShort} ${y}`;
}

function formatDate(dateString) {
    if (!dateString) return '';
    const match = String(dateString).match(/^(\d{4})-(\d{2})-(\d{2})$/);
    if (!match) return dateString;
    const y = parseInt(match[1], 10);
    const m = parseInt(match[2], 10);
    const d = parseInt(match[3], 10);
    const mShort = INDO_MONTHS_SHORT[m - 1] || '';
    return `${d} ${mShort} ${y}`;
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
    const hVal = txTimeHour24Select ? txTimeHour24Select.value : '';
    const mVal = txTimeMinute24Select ? txTimeMinute24Select.value : '';
    if (hVal && mVal) {
        return `${hVal}:${mVal}`;
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
            const h24Str = String(h24).padStart(2, '0');
            if (txTimeInput) {
                txTimeInput.value = `${h24Str}:${m}`;
            }
            if (txTimeHour24Select) {
                txTimeHour24Select.value = h24Str;
            }
            if (txTimeMinute24Select) {
                txTimeMinute24Select.value = m;
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
    if (txTimeHour24Select) txTimeHour24Select.value = '';
    if (txTimeMinute24Select) txTimeMinute24Select.value = '';
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
    } else {
        if (txTimeHour24Select && !txTimeHour24Select.value) {
            txTimeHour24Select.focus();
        } else if (txTimeMinute24Select && !txTimeMinute24Select.value) {
            txTimeMinute24Select.focus();
        } else if (txTimeHour24Select) {
            txTimeHour24Select.focus();
        }
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
        if (txTimeLabel) txTimeLabel.setAttribute('for', 'tx-time-hour-24');
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
        applyThemeUI(getStoredTheme());
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
        showNotification("Terjadi kesalahan saat memuat data.", "error");
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
    if (value === 0 || !value) return '0';
    const isNeg = typeof value === 'number' && value < 0;
    const absVal = typeof value === 'number' ? Math.abs(value) : Math.abs(Number(value) || 0);
    const sign = isNeg ? '-' : '';
    if (absVal >= 1_000_000_000_000) {
        const val = absVal / 1_000_000_000_000;
        return sign + (val % 1 === 0 ? val.toFixed(0) : val.toFixed(1).replace('.', ',')) + ' T';
    }
    if (absVal >= 1_000_000_000) {
        const val = absVal / 1_000_000_000;
        return sign + (val % 1 === 0 ? val.toFixed(0) : val.toFixed(1).replace('.', ',')) + ' M';
    }
    if (absVal >= 1_000_000) {
        const val = absVal / 1_000_000;
        return sign + (val % 1 === 0 ? val.toFixed(0) : val.toFixed(1).replace('.', ',')) + ' jt';
    }
    if (absVal >= 1_000) {
        const val = absVal / 1_000;
        return sign + (val % 1 === 0 ? val.toFixed(0) : val.toFixed(1).replace('.', ',')) + ' rb';
    }
    if (typeof absVal === 'number' && absVal % 1 !== 0) {
        return sign + (Math.round(absVal * 100) / 100).toFixed(2).replace('.', ',');
    }
    return formatNumber(value);
}

function getNiceCeiling(maxVal) {
    if (maxVal <= 0) {
        if (currentStatsCurrency === 'USD') return 100;
        if (currentStatsCurrency === 'JPY') return 10000;
        return 100000;
    }
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

function updateStatsCurrencyUI() {
    const avail = getAvailableCurrencies();
    if (!avail.includes(currentStatsCurrency)) {
        currentStatsCurrency = 'IDR';
    }

    const hasJpy = avail.includes('JPY');
    const hasUsd = avail.includes('USD');
    const onlyIdr = avail.length === 1;

    if (statsCurrencyBar) {
        statsCurrencyBar.classList.toggle('hidden', onlyIdr);
    }
    if (btnStatsCurrencyJpy) {
        btnStatsCurrencyJpy.classList.toggle('hidden', !hasJpy);
    }
    if (btnStatsCurrencyUsd) {
        btnStatsCurrencyUsd.classList.toggle('hidden', !hasUsd);
    }

    const isIdr = currentStatsCurrency === 'IDR';
    const isJpy = currentStatsCurrency === 'JPY';
    const isUsd = currentStatsCurrency === 'USD';
    if (btnStatsCurrencyIdr) {
        btnStatsCurrencyIdr.classList.toggle('is-active', isIdr);
        btnStatsCurrencyIdr.setAttribute('aria-pressed', String(isIdr));
    }
    if (btnStatsCurrencyJpy) {
        btnStatsCurrencyJpy.classList.toggle('is-active', isJpy);
        btnStatsCurrencyJpy.setAttribute('aria-pressed', String(isJpy));
    }
    if (btnStatsCurrencyUsd) {
        btnStatsCurrencyUsd.classList.toggle('is-active', isUsd);
        btnStatsCurrencyUsd.setAttribute('aria-pressed', String(isUsd));
    }
}

function updateBudgetCurrencyUI() {
    const avail = getAvailableCurrencies();
    if (!avail.includes(currentBudgetCurrency)) {
        currentBudgetCurrency = 'IDR';
    }

    const hasJpy = avail.includes('JPY');
    const hasUsd = avail.includes('USD');
    const onlyIdr = avail.length === 1;

    if (budgetCurrencyControl) {
        budgetCurrencyControl.classList.toggle('hidden', onlyIdr);
    }
    if (btnBudgetCurrencyJpy) {
        btnBudgetCurrencyJpy.classList.toggle('hidden', !hasJpy);
    }
    if (btnBudgetCurrencyUsd) {
        btnBudgetCurrencyUsd.classList.toggle('hidden', !hasUsd);
    }

    const isIdr = currentBudgetCurrency === 'IDR';
    const isJpy = currentBudgetCurrency === 'JPY';
    const isUsd = currentBudgetCurrency === 'USD';
    if (btnBudgetCurrencyIdr) {
        btnBudgetCurrencyIdr.classList.toggle('is-active', isIdr);
        btnBudgetCurrencyIdr.setAttribute('aria-pressed', String(isIdr));
    }
    if (btnBudgetCurrencyJpy) {
        btnBudgetCurrencyJpy.classList.toggle('is-active', isJpy);
        btnBudgetCurrencyJpy.setAttribute('aria-pressed', String(isJpy));
    }
    if (btnBudgetCurrencyUsd) {
        btnBudgetCurrencyUsd.classList.toggle('is-active', isUsd);
        btnBudgetCurrencyUsd.setAttribute('aria-pressed', String(isUsd));
    }
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
    updateStatsCurrencyUI();

    const isIncome = currentChartType === 'income';
    const typeLabel = isIncome ? 'pemasukan' : 'pengeluaran';

    // Filter transaksi aktif (tidak terhapus) sesuai tipe (pengeluaran atau pemasukan),
    // transfer antar dompet tidak dihitung, jumlah > 0, dan mata uang sesuai pilihan statistik
    const chartTx = (transactions || []).filter(tx => 
        tx.type === currentChartType && 
        tx.type !== 'transfer' && 
        !tx.deleted && 
        tx.amount > 0 &&
        getTransactionCurrency(tx) === currentStatsCurrency
    );

    if (chartTx.length === 0) {
        chartContainer.innerHTML = `
            <div class="empty-state">
                <p class="empty-state-text">Belum ada transaksi ${typeLabel}.</p>
                <button type="button" class="btn-action-outline" id="btn-empty-chart-tx">Tambah Transaksi</button>
            </div>
        `;
        if (chartSummaryText) chartSummaryText.textContent = '';
        if (chartDetail) {
            chartDetail.textContent = '';
            chartDetail.classList.add('hidden');
        }
        const btnEmptyChart = document.getElementById('btn-empty-chart-tx');
        if (btnEmptyChart) {
            btnEmptyChart.addEventListener('click', () => {
                resetForm();
                openFormScreen();
            });
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
        if (currentStatsCurrency === 'USD') {
            item.total = Math.round(item.total * 100) / 100;
        } else {
            item.total = Math.round(item.total);
        }
        item.count += 1;
        monthData.set(key, item);
    }

    if (monthData.size === 0) {
        chartContainer.innerHTML = `
            <div class="empty-state">
                <p class="empty-state-text">Belum ada transaksi ${typeLabel}.</p>
                <button type="button" class="btn-action-outline" id="btn-empty-chart-tx">Tambah Transaksi</button>
            </div>
        `;
        if (chartSummaryText) chartSummaryText.textContent = '';
        if (chartDetail) {
            chartDetail.textContent = '';
            chartDetail.classList.add('hidden');
        }
        const btnEmptyChart = document.getElementById('btn-empty-chart-tx');
        if (btnEmptyChart) {
            btnEmptyChart.addEventListener('click', () => {
                resetForm();
                openFormScreen();
            });
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

    totalAmountAll = currentStatsCurrency === 'USD' ? Math.round(totalAmountAll * 100) / 100 : Math.round(totalAmountAll);
    const avgAmount = monthsWithAmount > 0
        ? (currentStatsCurrency === 'USD' ? Math.round((totalAmountAll / monthsWithAmount) * 100) / 100 : Math.round(totalAmountAll / monthsWithAmount))
        : 0;
    if (chartSummaryText) {
        chartSummaryText.textContent = `Rata-rata: ${currencyFormatter.format(avgAmount, currentStatsCurrency)} / bulan`;
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
            <strong class="${amountClass} tabular-nums">${currencyFormatter.format(total, currentStatsCurrency)}</strong>
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
               aria-label="${fullLabel}: ${currencyFormatter.format(amount, currentStatsCurrency)}">
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

    updateStatsCurrencyUI();

    if (!selectedDonutMonth) {
        selectedDonutMonth = getLocalYearMonthString();
    }

    if (donutMonthInput && donutMonthInput.value !== selectedDonutMonth) {
        donutMonthInput.value = selectedDonutMonth;
    }

    if (donutMonthLabel) {
        const [yStr, mStr] = selectedDonutMonth.split('-');
        const mNum = parseInt(mStr, 10);
        const monthName = INDO_MONTHS_SHORT[mNum - 1] || '';
        donutMonthLabel.textContent = `${monthName} ${yStr}`;
    }

    updateDonutTypeUI();

    const isIncome = currentDonutType === 'income';
    const typeLabel = isIncome ? 'pemasukan' : 'pengeluaran';

    // Filter transaksi untuk bulan yang dipilih sesuai tipe (pengeluaran atau pemasukan),
    // transfer antar dompet tidak dihitung, tidak terhapus, jumlah > 0, dan mata uang sesuai pilihan statistik
    const monthTx = (transactions || []).filter(tx => {
        if (tx.type !== currentDonutType) return false;
        if (tx.type === 'transfer') return false;
        if (tx.deleted) return false;
        if (!(tx.amount > 0)) return false;
        if (!tx.date) return false;
        if (getTransactionCurrency(tx) !== currentStatsCurrency) return false;
        const match = tx.date.match(/^(\d{4})-(\d{1,2})/);
        if (!match) return false;
        const year = match[1];
        const month = String(parseInt(match[2], 10)).padStart(2, '0');
        return `${year}-${month}` === selectedDonutMonth;
    });

    if (monthTx.length === 0) {
        donutContainer.innerHTML = `
            <div class="empty-state">
                <p class="empty-state-text">Belum ada transaksi ${typeLabel} pada bulan ini.</p>
                <button type="button" class="btn-action-outline" id="btn-empty-donut-tx">Tambah Transaksi</button>
            </div>
        `;
        if (donutDetail) {
            donutDetail.textContent = '';
            donutDetail.classList.add('hidden');
        }
        activeDonutCategory = null;
        const btnEmptyDonut = document.getElementById('btn-empty-donut-tx');
        if (btnEmptyDonut) {
            btnEmptyDonut.addEventListener('click', () => {
                resetForm();
                openFormScreen();
            });
        }
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

    for (const [k, item] of categoryMap.entries()) {
        item.amount = currentStatsCurrency === 'USD' ? Math.round(item.amount * 100) / 100 : Math.round(item.amount);
    }
    totalAmount = currentStatsCurrency === 'USD' ? Math.round(totalAmount * 100) / 100 : Math.round(totalAmount);

    if (totalAmount <= 0) {
        donutContainer.innerHTML = `
            <div class="empty-state">
                <p class="empty-state-text">Belum ada transaksi ${typeLabel} pada bulan ini.</p>
                <button type="button" class="btn-action-outline" id="btn-empty-donut-tx">Tambah Transaksi</button>
            </div>
        `;
        if (donutDetail) {
            donutDetail.textContent = '';
            donutDetail.classList.add('hidden');
        }
        activeDonutCategory = null;
        const btnEmptyDonut = document.getElementById('btn-empty-donut-tx');
        if (btnEmptyDonut) {
            btnEmptyDonut.addEventListener('click', () => {
                resetForm();
                openFormScreen();
            });
        }
        return;
    }

    const isDark = resolveIsDark(getStoredTheme());
    const activeDonutPalette = isIncome
        ? (isDark ? DONUT_INCOME_DARK : DONUT_INCOME_LIGHT)
        : (isDark ? DONUT_EXPENSE_DARK : DONUT_EXPENSE_LIGHT);

    const sortedCategories = Array.from(categoryMap.entries())
        .sort((a, b) => b[1].amount - a[1].amount)
        .map(([category, data], index) => {
            const percentage = totalAmount > 0 ? (data.amount / totalAmount) * 100 : 0;
            return {
                category,
                amount: data.amount,
                count: data.count,
                percentage,
                color: activeDonutPalette[index % activeDonutPalette.length]
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
                <strong class="${amountClass} tabular-nums">${currencyFormatter.format(totalAmount, currentStatsCurrency)}</strong>
                <span class="chart-detail-count">(${sortedCategories.length} kategori, ${monthTx.length} transaksi)</span>
            `;
        } else {
            const catTransactions = monthTx.filter(tx => ((tx.category && tx.category.trim()) ? tx.category.trim() : 'Lainnya') === catItem.category);
            const subcatMap = new Map();
            let noSubAmount = 0;
            let noSubCount = 0;
            for (const tx of catTransactions) {
                const sub = getTxSubcategory(tx);
                if (sub) {
                    const item = subcatMap.get(sub) || { amount: 0, count: 0 };
                    item.amount += tx.amount;
                    item.count += 1;
                    subcatMap.set(sub, item);
                } else {
                    noSubAmount += tx.amount;
                    noSubCount += 1;
                }
            }

            for (const [sub, item] of subcatMap.entries()) {
                item.amount = currentStatsCurrency === 'USD' ? Math.round(item.amount * 100) / 100 : Math.round(item.amount);
            }
            noSubAmount = currentStatsCurrency === 'USD' ? Math.round(noSubAmount * 100) / 100 : Math.round(noSubAmount);

            let subcatDetailHtml = '';
            if (subcatMap.size > 0) {
                const sortedSubs = Array.from(subcatMap.entries()).sort((a, b) => b[1].amount - a[1].amount);
                let rowsHtml = '';
                for (const [subName, subData] of sortedSubs) {
                    const subPct = catItem.amount > 0 ? formatPercent((subData.amount / catItem.amount) * 100) : '0%';
                    rowsHtml += `
                        <div class="donut-subcat-row">
                            <span class="donut-subcat-name">${escapeHTML(subName)}</span>
                            <div class="donut-subcat-meta">
                                <span class="donut-subcat-pct tabular-nums">${subPct} (${subData.count})</span>
                                <strong class="donut-subcat-amount tabular-nums">${currencyFormatter.format(subData.amount, currentStatsCurrency)}</strong>
                            </div>
                        </div>
                    `;
                }
                if (noSubAmount > 0) {
                    const noSubPct = catItem.amount > 0 ? formatPercent((noSubAmount / catItem.amount) * 100) : '0%';
                    rowsHtml += `
                        <div class="donut-subcat-row is-no-sub">
                            <span class="donut-subcat-name">Tanpa sub kategori</span>
                            <div class="donut-subcat-meta">
                                <span class="donut-subcat-pct tabular-nums">${noSubPct} (${noSubCount})</span>
                                <strong class="donut-subcat-amount tabular-nums">${currencyFormatter.format(noSubAmount, currentStatsCurrency)}</strong>
                            </div>
                        </div>
                    `;
                }
                subcatDetailHtml = `
                    <div class="donut-subcat-section">
                        <span class="donut-subcat-title">Rincian Sub Kategori</span>
                        <div class="donut-subcat-list">
                            ${rowsHtml}
                        </div>
                    </div>
                `;
            }

            donutDetail.innerHTML = `
                <span class="chart-detail-month">${escapeHTML(catItem.category)}:</span>
                <strong class="${amountClass} tabular-nums">${currencyFormatter.format(catItem.amount, currentStatsCurrency)}</strong>
                <span class="chart-detail-count">(${formatPercent(catItem.percentage)} - ${catItem.count} transaksi)</span>
                ${subcatDetailHtml}
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
                aria-label="${escapeHTML(cat.category)}: ${currencyFormatter.format(cat.amount, currentStatsCurrency)} (${percentStr})">
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
                aria-label="${escapeHTML(cat.category)}: ${currencyFormatter.format(cat.amount, currentStatsCurrency)} (${percentStr})">
                <div class="donut-legend-main">
                    <span class="donut-legend-color" style="background-color: ${cat.color}"></span>
                    <span class="donut-legend-name" title="${escapeHTML(cat.category)}">${escapeHTML(cat.category)}</span>
                </div>
                <div class="donut-legend-side">
                    <span class="donut-legend-percent tabular-nums">${percentStr}</span>
                    <strong class="donut-legend-amount tabular-nums">${currencyFormatter.format(cat.amount, currentStatsCurrency)}</strong>
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
                        <text x="110" y="118" text-anchor="middle" class="donut-center-value tabular-nums" id="donut-center-value">${centerValueText}</text>
                        <text x="110" y="134" text-anchor="middle" class="donut-center-sub tabular-nums" id="donut-center-sub">${centerSubText}</text>
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

/* ==========================================================================
   Timbangan (Mizan) & Kalkulator "Timbang Sebelum Beli"
   ========================================================================== */

function getWalletCurrentBalance(walletId) {
    if (!walletId) return 0;
    let bal = 0;
    for (const tx of currentTransactions) {
        if (tx.deleted) continue;
        const fromId = tx.walletId || DEFAULT_WALLET_ID;
        if (tx.type === 'income' && fromId === walletId) {
            bal += tx.amount;
        } else if (tx.type === 'expense' && fromId === walletId) {
            bal -= tx.amount;
        } else if (tx.type === 'transfer') {
            if (fromId === walletId) bal -= tx.amount;
            if (tx.toWalletId === walletId) bal += tx.amount;
        }
    }
    const curr = getWalletCurrency(walletId);
    if (curr === 'USD') {
        bal = Math.round(bal * 100) / 100;
    } else {
        bal = Math.round(bal);
    }
    return bal;
}

function updateTimbanganCurrencyUI() {
    const avail = getAvailableCurrencies();
    if (!avail.includes(currentTimbanganCurrency)) {
        currentTimbanganCurrency = 'IDR';
    }

    const hasJpy = avail.includes('JPY');
    const hasUsd = avail.includes('USD');
    const onlyIdr = avail.length === 1;

    if (timbanganCurrencyBar) {
        timbanganCurrencyBar.classList.toggle('hidden', onlyIdr);
    }
    if (btnTimbanganCurrencyJpy) {
        btnTimbanganCurrencyJpy.classList.toggle('hidden', !hasJpy);
    }
    if (btnTimbanganCurrencyUsd) {
        btnTimbanganCurrencyUsd.classList.toggle('hidden', !hasUsd);
    }

    const isIdr = currentTimbanganCurrency === 'IDR';
    const isJpy = currentTimbanganCurrency === 'JPY';
    const isUsd = currentTimbanganCurrency === 'USD';

    if (btnTimbanganCurrencyIdr) {
        btnTimbanganCurrencyIdr.classList.toggle('is-active', isIdr);
        btnTimbanganCurrencyIdr.setAttribute('aria-pressed', String(isIdr));
    }
    if (btnTimbanganCurrencyJpy) {
        btnTimbanganCurrencyJpy.classList.toggle('is-active', isJpy);
        btnTimbanganCurrencyJpy.setAttribute('aria-pressed', String(isJpy));
    }
    if (btnTimbanganCurrencyUsd) {
        btnTimbanganCurrencyUsd.classList.toggle('is-active', isUsd);
        btnTimbanganCurrencyUsd.setAttribute('aria-pressed', String(isUsd));
    }

    if (calcPricePrefix) {
        calcPricePrefix.textContent = isUsd ? '$' : (isJpy ? '¥' : 'Rp');
    }
    if (calcPriceLabel) {
        calcPriceLabel.textContent = isUsd ? 'Harga Barang ($)' : (isJpy ? 'Harga Barang (¥)' : 'Harga Barang (Rp)');
    }
    if (calcPriceInput) {
        calcPriceInput.setAttribute('inputmode', isUsd ? 'decimal' : 'numeric');
        if (calcPriceInput.value) {
            if (!isUsd && calcPriceInput.value.includes(',')) {
                calcPriceInput.value = calcPriceInput.value.split(',')[0];
            }
            formatInputWithCursor(calcPriceInput, currentTimbanganCurrency);
        }
    }
}

function shiftTimbanganMonth(delta) {
    if (!selectedTimbanganMonth) {
        selectedTimbanganMonth = getLocalYearMonthString();
    }
    const [yStr, mStr] = selectedTimbanganMonth.split('-');
    let y = parseInt(yStr, 10);
    let m = parseInt(mStr, 10) + delta;
    if (m < 1) {
        m = 12;
        y--;
    } else if (m > 12) {
        m = 1;
        y++;
    }
    selectedTimbanganMonth = `${y}-${String(m).padStart(2, '0')}`;
    renderTimbangan();
}

function getTimbanganMonthlyData(monthKey, currency) {
    let income = 0;
    let expense = 0;
    let txCount = 0;

    for (const tx of currentTransactions) {
        if (tx.deleted || !(tx.amount > 0) || !tx.date) continue;
        if (tx.type === 'transfer') continue; // Transfer antar dompet tidak dihitung
        if (getTransactionCurrency(tx) !== currency) continue;

        const match = tx.date.match(/^(\d{4})-(\d{1,2})/);
        if (!match) continue;
        const key = `${match[1]}-${String(parseInt(match[2], 10)).padStart(2, '0')}`;
        if (key === monthKey) {
            if (tx.type === 'income') {
                income += tx.amount;
                txCount++;
            } else if (tx.type === 'expense') {
                expense += tx.amount;
                txCount++;
            }
        }
    }

    if (currency === 'USD') {
        income = Math.round(income * 100) / 100;
        expense = Math.round(expense * 100) / 100;
    } else {
        income = Math.round(income);
        expense = Math.round(expense);
    }

    const diff = currency === 'USD' ? Math.round((income - expense) * 100) / 100 : Math.round(income - expense);
    return { income, expense, diff, txCount };
}

function generateTimbanganScaleSvg(income, expense, diff, currency) {
    const total = income + expense;
    const maxTilt = 16;
    let dy = 0;
    if (total > 0) {
        const ratio = diff / total; // antara -1 dan +1
        // diff > 0 (surplus, income > expense) -> lengan kiri lebih berat -> miring turun: dy > 0
        // diff < 0 (defisit, expense > income) -> lengan kanan lebih berat -> miring turun: dy < 0
        if (diff > 0) {
            dy = Math.max(3, Math.min(maxTilt, Math.round(ratio * maxTilt)));
        } else if (diff < 0) {
            dy = Math.min(-3, Math.max(-maxTilt, Math.round(ratio * maxTilt)));
        }
    }

    const pivotX = 160;
    const pivotY = 42;
    const leftX = 65;
    const leftY = pivotY + dy;
    const rightX = 255;
    const rightY = pivotY - dy;

    const stringLen = 48;
    const leftSaucerY = leftY + stringLen;
    const rightSaucerY = rightY + stringLen;

    return `
        <svg viewBox="0 0 320 180" class="timbangan-svg" role="img" aria-label="Gambar timbangan garis">
            <!-- Stand dan Tiang Utama -->
            <line x1="110" y1="160" x2="210" y2="160" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" />
            <line x1="${pivotX}" y1="160" x2="${pivotX}" y2="${pivotY}" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" />
            
            <!-- Lengan Timbangan (Beam) miring statis tanpa animasi -->
            <line x1="${leftX}" y1="${leftY}" x2="${rightX}" y2="${rightY}" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" />
            <circle cx="${pivotX}" cy="${pivotY}" r="4.5" fill="var(--surface)" stroke="currentColor" stroke-width="2" />
            <circle cx="${leftX}" cy="${leftY}" r="3" fill="var(--surface)" stroke="currentColor" stroke-width="1.8" />
            <circle cx="${rightX}" cy="${rightY}" r="3" fill="var(--surface)" stroke="currentColor" stroke-width="1.8" />

            <!-- Piring Kiri (Pemasukan) -->
            <line x1="${leftX}" y1="${leftY}" x2="${leftX - 25}" y2="${leftSaucerY}" stroke="var(--text-secondary)" stroke-width="1.5" stroke-linecap="round" />
            <line x1="${leftX}" y1="${leftY}" x2="${leftX + 25}" y2="${leftSaucerY}" stroke="var(--text-secondary)" stroke-width="1.5" stroke-linecap="round" />
            <path d="M ${leftX - 28} ${leftSaucerY} Q ${leftX} ${leftSaucerY + 12} ${leftX + 28} ${leftSaucerY}" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" />

            <!-- Piring Kanan (Pengeluaran) -->
            <line x1="${rightX}" y1="${rightY}" x2="${rightX - 25}" y2="${rightSaucerY}" stroke="var(--text-secondary)" stroke-width="1.5" stroke-linecap="round" />
            <line x1="${rightX}" y1="${rightY}" x2="${rightX + 25}" y2="${rightSaucerY}" stroke="var(--text-secondary)" stroke-width="1.5" stroke-linecap="round" />
            <path d="M ${rightX - 28} ${rightSaucerY} Q ${rightX} ${rightSaucerY + 12} ${rightX + 28} ${rightSaucerY}" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" />
        </svg>
    `;
}

function renderTimbanganScale() {
    if (!timbanganScaleContainer) return;

    if (!selectedTimbanganMonth) {
        selectedTimbanganMonth = getLocalYearMonthString();
    }

    if (timbanganMonthLabel) {
        const [yStr, mStr] = selectedTimbanganMonth.split('-');
        const mNum = parseInt(mStr, 10);
        const monthName = INDO_MONTHS_SHORT[mNum - 1] || '';
        timbanganMonthLabel.textContent = `${monthName} ${yStr}`;
    }

    const { income, expense, diff, txCount } = getTimbanganMonthlyData(selectedTimbanganMonth, currentTimbanganCurrency);

    if (txCount === 0) {
        timbanganScaleContainer.innerHTML = `
            <div class="empty-state">
                <p class="empty-state-text">Belum ada transaksi pada bulan ini.</p>
                <button type="button" class="btn-action-outline" id="btn-empty-timbangan-tx">Tambah Transaksi</button>
            </div>
        `;
        const btnEmptyTimbangan = document.getElementById('btn-empty-timbangan-tx');
        if (btnEmptyTimbangan) {
            btnEmptyTimbangan.addEventListener('click', () => {
                resetForm();
                openFormScreen();
            });
        }
        return;
    }

    const svgHtml = generateTimbanganScaleSvg(income, expense, diff, currentTimbanganCurrency);

    let verdictClass = 'is-surplus';
    let verdictText = '';
    if (diff > 0) {
        verdictClass = 'is-surplus';
        verdictText = `Surplus ${currencyFormatter.format(diff, currentTimbanganCurrency)}`;
    } else if (diff < 0) {
        verdictClass = 'is-defisit';
        verdictText = `Defisit ${currencyFormatter.format(Math.abs(diff), currentTimbanganCurrency)}`;
    } else {
        verdictClass = 'is-surplus';
        verdictText = `Surplus ${currencyFormatter.format(0, currentTimbanganCurrency)}`;
    }

    timbanganScaleContainer.innerHTML = `
        <div class="timbangan-svg-wrap">
            ${svgHtml}
        </div>
        <div class="timbangan-pan-amounts">
            <div class="timbangan-pan-box pan-left">
                <span class="pan-title">Pemasukan</span>
                <strong class="pan-amount tabular-nums">+${currencyFormatter.format(income, currentTimbanganCurrency)}</strong>
            </div>
            <div class="timbangan-pan-box pan-right">
                <span class="pan-title">Pengeluaran</span>
                <strong class="pan-amount tabular-nums">−${currencyFormatter.format(expense, currentTimbanganCurrency)}</strong>
            </div>
        </div>
        <div class="timbangan-verdict-box ${verdictClass}">
            <p class="timbangan-verdict-text tabular-nums">${verdictText}</p>
        </div>
    `;
}

function populateTimbanganCalcOptions() {
    if (calcWalletSelect) {
        const active = getActiveWallets().filter(w => (w.currency || 'IDR') === currentTimbanganCurrency);
        const prevWalletVal = calcWalletSelect.value;
        if (active.length === 0) {
            calcWalletSelect.innerHTML = '<option value="" disabled selected>Belum ada dompet</option>';
        } else {
            let html = '';
            for (const w of active) {
                const bal = getWalletCurrentBalance(w.id);
                html += `<option value="${w.id}">${escapeHTML(w.name)} (${currencyFormatter.format(bal, currentTimbanganCurrency)})</option>`;
            }
            calcWalletSelect.innerHTML = html;
            if (prevWalletVal && active.some(w => w.id === prevWalletVal)) {
                calcWalletSelect.value = prevWalletVal;
            } else if (active.some(w => w.id === DEFAULT_WALLET_ID)) {
                calcWalletSelect.value = DEFAULT_WALLET_ID;
            } else {
                calcWalletSelect.value = active[0].id;
            }
        }
    }

    if (calcCategorySelect) {
        const expenseCats = (currentCategories && currentCategories.expense) ? currentCategories.expense : [];
        const prevCatVal = calcCategorySelect.value;
        if (expenseCats.length === 0) {
            calcCategorySelect.innerHTML = '<option value="" disabled selected>Belum ada kategori</option>';
        } else {
            let html = '';
            const budgetMap = (currentBudgets && currentBudgets.items) ? currentBudgets.items : {};
            for (const cat of expenseCats) {
                const hasBudget = (currentTimbanganCurrency === 'IDR') && typeof budgetMap[cat] === 'number' && budgetMap[cat] > 0;
                const budgetLabel = hasBudget ? ` (Budget: ${currencyFormatter.format(budgetMap[cat], 'IDR')})` : '';
                html += `<option value="${escapeHTML(cat)}">${escapeHTML(cat)}${escapeHTML(budgetLabel)}</option>`;
            }
            calcCategorySelect.innerHTML = html;
            if (prevCatVal && expenseCats.includes(prevCatVal)) {
                calcCategorySelect.value = prevCatVal;
            } else {
                calcCategorySelect.value = expenseCats[0];
            }
        }
    }
}

function renderTimbanganCalculator() {
    if (!calcPriceInput || !calcCategorySelect || !calcWalletSelect) return;

    const rawPrice = parseAmount(calcPriceInput.value || 0, currentTimbanganCurrency);
    const price = isNaN(rawPrice) || rawPrice < 0 ? 0 : rawPrice;

    const selectedWalletId = calcWalletSelect.value;
    const walletBal = getWalletCurrentBalance(selectedWalletId);
    let newWalletBal = walletBal - price;
    if (currentTimbanganCurrency === 'USD') {
        newWalletBal = Math.round(newWalletBal * 100) / 100;
    } else {
        newWalletBal = Math.round(newWalletBal);
    }

    if (calcResultWallet) {
        calcResultWallet.className = newWalletBal < 0 ? 'calc-result-val tabular-nums is-negative' : 'calc-result-val tabular-nums';
        calcResultWallet.textContent = currencyFormatter.format(newWalletBal, currentTimbanganCurrency);
    }

    const selectedCat = calcCategorySelect.value;
    const budgetMap = (currentBudgets && currentBudgets.items) ? currentBudgets.items : {};
    const hasBudget = (currentTimbanganCurrency === 'IDR') && selectedCat && (typeof budgetMap[selectedCat] === 'number') && (budgetMap[selectedCat] > 0);

    let remainingBudget = 0;
    let remainingPercent = 0;

    if (hasBudget) {
        if (calcResultBudgetRow) calcResultBudgetRow.classList.remove('hidden');
        const budgetAmount = budgetMap[selectedCat];
        const { categoryTotals } = getActiveMonthlyExpenses(currentTransactions, selectedTimbanganMonth, 'IDR');
        const currentSpent = categoryTotals.get(selectedCat) || 0;
        const newSpent = currentSpent + price;
        remainingBudget = budgetAmount - newSpent;
        remainingPercent = budgetAmount > 0 ? (remainingBudget / budgetAmount) * 100 : 0;

        if (calcResultBudget) {
            if (remainingBudget >= 0) {
                calcResultBudget.className = 'calc-result-val tabular-nums';
                calcResultBudget.textContent = `${currencyFormatter.format(remainingBudget, 'IDR')} (${Math.round(remainingPercent)}% tersisa)`;
            } else {
                calcResultBudget.className = 'calc-result-val tabular-nums is-negative';
                calcResultBudget.textContent = `Lewat Rp ${formatNumber(Math.abs(remainingBudget))}`;
            }
        }
    } else {
        if (calcResultBudgetRow) calcResultBudgetRow.classList.add('hidden');
    }

    const { income: monthIncome, expense: currentMonthExpense } = getTimbanganMonthlyData(selectedTimbanganMonth, currentTimbanganCurrency);
    let newMonthExpense = currentMonthExpense + price;
    if (currentTimbanganCurrency === 'USD') {
        newMonthExpense = Math.round(newMonthExpense * 100) / 100;
    } else {
        newMonthExpense = Math.round(newMonthExpense);
    }
    let newMonthDiff = monthIncome - newMonthExpense;
    if (currentTimbanganCurrency === 'USD') {
        newMonthDiff = Math.round(newMonthDiff * 100) / 100;
    } else {
        newMonthDiff = Math.round(newMonthDiff);
    }

    if (calcResultMonthlyScale) {
        if (newMonthDiff > 0) {
            calcResultMonthlyScale.className = 'calc-result-val tabular-nums';
            calcResultMonthlyScale.textContent = `Surplus ${currencyFormatter.format(newMonthDiff, currentTimbanganCurrency)}`;
        } else if (newMonthDiff < 0) {
            calcResultMonthlyScale.className = 'calc-result-val tabular-nums is-negative';
            calcResultMonthlyScale.textContent = `Defisit ${currencyFormatter.format(Math.abs(newMonthDiff), currentTimbanganCurrency)}`;
        } else {
            calcResultMonthlyScale.className = 'calc-result-val tabular-nums';
            calcResultMonthlyScale.textContent = `Surplus ${currencyFormatter.format(0, currentTimbanganCurrency)}`;
        }
    }

    let statusBadge = 'Aman';
    let statusDesc = '';

    if (hasBudget) {
        if (remainingBudget < 0 || newWalletBal < 0) {
            statusBadge = 'Berat';
            if (remainingBudget < 0 && newWalletBal < 0) {
                statusDesc = `Lewat budget Rp ${formatNumber(Math.abs(remainingBudget))} dan saldo dompet jadi minus.`;
            } else if (remainingBudget < 0) {
                statusDesc = `Melebihi budget bulanan Rp ${formatNumber(Math.abs(remainingBudget))}.`;
            } else {
                statusDesc = 'Saldo dompet tidak mencukupi (jadi minus).';
            }
        } else if (remainingPercent <= 20) {
            statusBadge = 'Pas';
            statusDesc = `Sisa budget kategori tinggal ${Math.round(remainingPercent)}% (${currencyFormatter.format(remainingBudget, 'IDR')}).`;
        } else {
            statusBadge = 'Aman';
            statusDesc = `Sisa budget masih aman di atas 20% (${Math.round(remainingPercent)}% tersisa).`;
        }
    } else {
        if (newWalletBal < 0 || newMonthDiff < 0) {
            statusBadge = 'Berat';
            if (newWalletBal < 0 && newMonthDiff < 0) {
                statusDesc = `Saldo dompet jadi minus dan timbangan bulan ini defisit ${currencyFormatter.format(Math.abs(newMonthDiff), currentTimbanganCurrency)}.`;
            } else if (newWalletBal < 0) {
                statusDesc = 'Saldo dompet tidak mencukupi (jadi minus).';
            } else {
                statusDesc = `Timbangan bulan ini menjadi defisit ${currencyFormatter.format(Math.abs(newMonthDiff), currentTimbanganCurrency)}.`;
            }
        } else if (
            newMonthDiff === 0 ||
            newWalletBal === 0 ||
            (monthIncome > 0 && (newMonthDiff / monthIncome) <= 0.20) ||
            (walletBal > 0 && (newWalletBal / walletBal) <= 0.20)
        ) {
            statusBadge = 'Pas';
            statusDesc = 'Saldo dompet atau surplus bulan ini pas-pasan (0–20%).';
        } else {
            statusBadge = 'Aman';
            statusDesc = 'Saldo dompet dan surplus timbangan bulan ini aman.';
        }
    }

    if (calcStatusBanner) {
        calcStatusBanner.className = `calc-status-banner status-${statusBadge.toLowerCase()}`;
    }
    if (calcStatusBadge) {
        calcStatusBadge.textContent = statusBadge;
    }
    if (calcStatusDesc) {
        calcStatusDesc.textContent = statusDesc;
    }
}

function renderTimbangan() {
    updateTimbanganCurrencyUI();
    renderTimbanganScale();
    populateTimbanganCalcOptions();
    renderTimbanganCalculator();
}

function createTxItemElement(tx, filterWallet) {
    const txEl = document.createElement('div');
    txEl.className = 'tx-item';
    txEl.dataset.id = tx.id;
    txEl.tabIndex = 0;
    txEl.setAttribute('role', 'button');
    const displayCategory = getTxCategoryDisplayText(tx);
    txEl.setAttribute('aria-label', `Transaksi ${escapeHTML(displayCategory || tx.type)}, ketuk untuk edit`);

    let sign = '';
    let amountClass = '';
    let categoryText = escapeHTML(displayCategory);
    let walletText = '';
    const isTransfer = tx.type === 'transfer';

    if (isTransfer) {
        const fromName = getWalletName(tx.walletId);
        const toName = getWalletName(tx.toWalletId);
        const fromDeleted = isWalletDeleted(tx.walletId);
        const toDeleted = isWalletDeleted(tx.toWalletId);
        const fromText = `${escapeHTML(fromName)}${fromDeleted ? ' (dihapus)' : ''}`;
        const toText = `${escapeHTML(toName)}${toDeleted ? ' (dihapus)' : ''}`;
        categoryText = 'Transfer';
        walletText = `${fromText} &rarr; ${toText}`;
        amountClass = 'transfer';
        sign = ''; // Transfer: teks biru, tanpa tanda +/−, ada label "Transfer"
    } else {
        sign = tx.type === 'income' ? '+' : '−';
        amountClass = tx.type === 'income' ? 'income' : 'expense';
        const wName = escapeHTML(getWalletName(tx.walletId));
        walletText = isWalletDeleted(tx.walletId) ? `${wName} (dihapus)` : wName;
    }

    const timeFormatted = tx.time ? formatTimeDisplay(tx.time, currentTimeFormat) : '';
    const iconSvg = getCategoryIconSvg(tx.category, tx.type);

    txEl.innerHTML = `
        <div class="tx-item-icon ${tx.type}" aria-hidden="true">
            ${iconSvg}
        </div>
        <div class="tx-item-content">
            <div class="tx-item-main">
                <span class="tx-item-category">${categoryText}${isTransfer ? ' <span class="tx-transfer-badge">Transfer</span>' : ''}</span>
                ${tx.note ? `<span class="tx-item-note">&bull; ${escapeHTML(tx.note)}</span>` : ''}
            </div>
            <div class="tx-item-sub">
                <span class="tx-item-wallet">${walletText}</span>
                ${timeFormatted ? `<span class="tx-item-time">&bull; ${escapeHTML(timeFormatted)}</span>` : ''}
            </div>
        </div>
        <div class="tx-item-amount-col">
            <span class="tx-amount ${amountClass} tabular-nums">${sign}${currencyFormatter.format(tx.amount, getTransactionCurrency(tx))}</span>
        </div>
        <div class="tx-actions">
            <button type="button" class="btn-icon btn-edit" data-id="${tx.id}" aria-label="Ubah transaksi" title="Ubah">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M12 20h9"></path><path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"></path></svg>
            </button>
            <button type="button" class="btn-icon btn-delete" data-id="${tx.id}" aria-label="Hapus transaksi" title="Hapus">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path></svg>
            </button>
        </div>
    `;

    // Klik baris atau tekan Enter/Spasi membuka formulir edit
    txEl.addEventListener('click', (e) => {
        if (e.target.closest('.btn-delete')) return;
        handleEdit(tx.id);
        openFormScreen();
    });

    txEl.addEventListener('keydown', (e) => {
        if (e.key === 'Enter' || e.key === ' ') {
            if (e.target.closest('.btn-delete') || e.target.closest('.btn-edit')) return;
            e.preventDefault();
            handleEdit(tx.id);
            openFormScreen();
        }
    });

    return txEl;
}

export function getBerandaMonthSummary(transactions, targetMonthKey, activeCurrencies = ['IDR'], wallets = currentWallets) {
    const totalBalanceByCurrency = new Map();
    const incomeByCurrency = new Map();
    const expenseByCurrency = new Map();

    for (const c of activeCurrencies) {
        totalBalanceByCurrency.set(c, 0);
        incomeByCurrency.set(c, 0);
        expenseByCurrency.set(c, 0);
    }

    const walletList = Array.isArray(wallets) ? wallets : (currentWallets || []);
    const walletDeletedMap = new Map();
    for (const w of walletList) {
        if (w && typeof w.id === 'string') {
            walletDeletedMap.set(w.id, !!w.deleted);
        }
    }
    const isWalletActive = (id) => {
        const wId = id || DEFAULT_WALLET_ID;
        if (!walletDeletedMap.has(wId)) return wId === DEFAULT_WALLET_ID;
        return !walletDeletedMap.get(wId);
    };

    for (const tx of transactions || []) {
        if (tx.deleted || !(tx.amount > 0) || !tx.date) continue;
        const match = tx.date.match(/^(\d{4})-(\d{1,2})/);
        if (!match) continue;
        const txMonth = `${match[1]}-${String(match[2]).padStart(2, '0')}`;
        const curr = getTransactionCurrency(tx);

        if (txMonth <= targetMonthKey) {
            const fromId = tx.walletId || DEFAULT_WALLET_ID;
            const fromActive = isWalletActive(fromId);

            if (tx.type === 'income') {
                if (fromActive) {
                    totalBalanceByCurrency.set(curr, (totalBalanceByCurrency.get(curr) || 0) + tx.amount);
                }
            } else if (tx.type === 'expense') {
                if (fromActive) {
                    totalBalanceByCurrency.set(curr, (totalBalanceByCurrency.get(curr) || 0) - tx.amount);
                }
            } else if (tx.type === 'transfer') {
                const toId = tx.toWalletId;
                const toActive = isWalletActive(toId);
                if (fromActive && !toActive) {
                    totalBalanceByCurrency.set(curr, (totalBalanceByCurrency.get(curr) || 0) - tx.amount);
                } else if (!fromActive && toActive) {
                    totalBalanceByCurrency.set(curr, (totalBalanceByCurrency.get(curr) || 0) + tx.amount);
                }
            }
        }

        if (txMonth === targetMonthKey) {
            if (tx.type === 'income') {
                incomeByCurrency.set(curr, (incomeByCurrency.get(curr) || 0) + tx.amount);
            } else if (tx.type === 'expense') {
                expenseByCurrency.set(curr, (expenseByCurrency.get(curr) || 0) + tx.amount);
            }
        }
    }
    for (const [c, val] of totalBalanceByCurrency.entries()) {
        totalBalanceByCurrency.set(c, c === 'USD' ? Math.round(val * 100) / 100 : Math.round(val));
    }
    for (const [c, val] of incomeByCurrency.entries()) {
        incomeByCurrency.set(c, c === 'USD' ? Math.round(val * 100) / 100 : Math.round(val));
    }
    for (const [c, val] of expenseByCurrency.entries()) {
        expenseByCurrency.set(c, c === 'USD' ? Math.round(val * 100) / 100 : Math.round(val));
    }

    return {
        totalBalanceByCurrency,
        incomeByCurrency,
        expenseByCurrency
    };
}

function shiftBerandaMonth(delta) {
    if (!selectedBerandaMonth) {
        selectedBerandaMonth = getLocalYearMonthString();
    }
    const currentMonth = getLocalYearMonthString();
    if (delta > 0 && selectedBerandaMonth >= currentMonth) {
        return;
    }
    const [yStr, mStr] = selectedBerandaMonth.split('-');
    let y = parseInt(yStr, 10);
    let m = parseInt(mStr, 10) + delta;
    if (m < 1) {
        m = 12;
        y--;
    } else if (m > 12) {
        m = 1;
        y++;
    }
    const nextMonthStr = `${y}-${String(m).padStart(2, '0')}`;
    if (delta > 0 && nextMonthStr > currentMonth) {
        selectedBerandaMonth = currentMonth;
    } else {
        selectedBerandaMonth = nextMonthStr;
    }
    renderSummaryCard();
}

function renderSummaryCard() {
    if (!totalBalanceEl || !totalIncomeEl || !totalExpenseEl) return;

    if (!selectedBerandaMonth) {
        selectedBerandaMonth = getLocalYearMonthString();
    }
    const currentMonth = getLocalYearMonthString();
    if (selectedBerandaMonth > currentMonth) {
        selectedBerandaMonth = currentMonth;
    }

    if (berandaMonthLabel) {
        const [yStr, mStr] = selectedBerandaMonth.split('-');
        const mNum = parseInt(mStr, 10);
        const monthName = INDO_MONTHS_FULL[mNum - 1] || '';
        berandaMonthLabel.textContent = `${monthName} ${yStr}`;
    }

    if (btnBerandaNextMonth) {
        const isCurrentOrFuture = selectedBerandaMonth >= currentMonth;
        btnBerandaNextMonth.disabled = isCurrentOrFuture;
        if (isCurrentOrFuture) {
            btnBerandaNextMonth.setAttribute('aria-disabled', 'true');
        } else {
            btnBerandaNextMonth.removeAttribute('aria-disabled');
        }
    }

    const activeWallets = getActiveWallets();
    const activeCurrencies = [];
    if (activeWallets.some(w => (w.currency || 'IDR') === 'IDR')) activeCurrencies.push('IDR');
    if (activeWallets.some(w => w.currency === 'JPY')) activeCurrencies.push('JPY');
    if (activeWallets.some(w => w.currency === 'USD')) activeCurrencies.push('USD');
    if (activeCurrencies.length === 0) activeCurrencies.push('IDR');

    const { totalBalanceByCurrency, incomeByCurrency, expenseByCurrency } =
        getBerandaMonthSummary(currentTransactions, selectedBerandaMonth, activeCurrencies, currentWallets);

    if (activeCurrencies.length === 1) {
        const c = activeCurrencies[0];
        let bal = totalBalanceByCurrency.get(c) || 0;
        let inc = incomeByCurrency.get(c) || 0;
        let exp = expenseByCurrency.get(c) || 0;
        if (c === 'USD') {
            bal = Math.round(bal * 100) / 100;
            inc = Math.round(inc * 100) / 100;
            exp = Math.round(exp * 100) / 100;
        }
        totalBalanceEl.textContent = currencyFormatter.format(bal, c);
        totalIncomeEl.textContent = '+' + currencyFormatter.format(inc, c);
        totalExpenseEl.textContent = '−' + currencyFormatter.format(exp, c);
    } else {
        totalBalanceEl.innerHTML = activeCurrencies.map(c => {
            let bal = totalBalanceByCurrency.get(c) || 0;
            if (c === 'USD') bal = Math.round(bal * 100) / 100;
            return `<span class="summary-balance-line">${escapeHTML(currencyFormatter.format(bal, c))}</span>`;
        }).join('');

        totalIncomeEl.innerHTML = activeCurrencies.map(c => {
            let inc = incomeByCurrency.get(c) || 0;
            if (c === 'USD') inc = Math.round(inc * 100) / 100;
            return `<span class="summary-item-line">+${escapeHTML(currencyFormatter.format(inc, c))}</span>`;
        }).join('');

        totalExpenseEl.innerHTML = activeCurrencies.map(c => {
            let exp = expenseByCurrency.get(c) || 0;
            if (c === 'USD') exp = Math.round(exp * 100) / 100;
            return `<span class="summary-item-line">−${escapeHTML(currencyFormatter.format(exp, c))}</span>`;
        }).join('');
    }
}

function render() {
    renderSummaryCard();

    const activeWallets = getActiveWallets();
    const walletBalances = new Map();
    for (const w of activeWallets) {
        walletBalances.set(w.id, 0);
    }

    currentTransactions.forEach(tx => {
        if (tx.deleted) return;
        const curr = getTransactionCurrency(tx);
        const wId = tx.walletId || DEFAULT_WALLET_ID;
        if (tx.type === 'income') {
            walletBalances.set(wId, (walletBalances.get(wId) || 0) + tx.amount);
        } else if (tx.type === 'expense') {
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

    if (walletsSection) {
        if (activeWallets.length < 2) {
            walletsSection.classList.add('hidden');
        } else {
            walletsSection.classList.remove('hidden');
        }
    }

    if (walletBalancesContainer) {
        if (activeWallets.length < 2) {
            walletBalancesContainer.innerHTML = '';
        } else {
            let balancesHtml = '';
            for (const w of activeWallets) {
                const rawBal = walletBalances.get(w.id) || 0;
                const curr = getWalletCurrency(w.id);
                const bal = curr === 'USD' ? Math.round(rawBal * 100) / 100 : Math.round(rawBal);
                const isZero = bal === 0;
                const isSelected = currentWalletFilter === w.id;
                balancesHtml += `
                    <button type="button" class="wallet-balance-item${isZero ? ' is-zero' : ''}${isSelected ? ' is-selected' : ''}" data-wallet-id="${escapeHTML(w.id)}" aria-pressed="${isSelected ? 'true' : 'false'}" aria-label="Dompet ${escapeHTML(w.name)}, saldo ${escapeHTML(currencyFormatter.format(bal, curr))}">
                        <span class="wallet-balance-name">${escapeHTML(w.name)}</span>
                        <strong class="wallet-balance-amount tabular-nums">${currencyFormatter.format(bal, curr)}</strong>
                    </button>
                `;
            }
            walletBalancesContainer.innerHTML = balancesHtml;
        }
    }

    const filteredTx = currentTransactions.filter(tx => {
        if (currentWalletFilter === 'all') return true;
        if (tx.type === 'transfer') {
            return (tx.walletId || DEFAULT_WALLET_ID) === currentWalletFilter || tx.toWalletId === currentWalletFilter;
        }
        return (tx.walletId || DEFAULT_WALLET_ID) === currentWalletFilter;
    });

    if (txListContainer) {
        txListContainer.innerHTML = '';
        if (filteredTx.length === 0) {
            txListContainer.innerHTML = `
                <div class="empty-state">
                    <p class="empty-state-text">Belum ada transaksi.</p>
                    <button type="button" class="btn-action-outline" id="btn-empty-riwayat-add">Tambah Transaksi</button>
                </div>
            `;
            const btnEmptyRiwayat = document.getElementById('btn-empty-riwayat-add');
            if (btnEmptyRiwayat) {
                btnEmptyRiwayat.addEventListener('click', () => {
                    resetForm();
                    openFormScreen();
                });
            }
        } else {
            // Kelompokkan transaksi berdasarkan tanggal (YYYY-MM-DD)
            const dateGroups = new Map();
            for (const tx of filteredTx) {
                const dateKey = tx.date || '';
                if (!dateGroups.has(dateKey)) {
                    dateGroups.set(dateKey, []);
                }
                dateGroups.get(dateKey).push(tx);
            }

            for (const [dateKey, groupTxList] of dateGroups.entries()) {
                const groupEl = document.createElement('div');
                groupEl.className = 'tx-date-group';

                const netByCurrency = new Map();
                for (const tx of groupTxList) {
                    const c = getTransactionCurrency(tx);
                    const net = netByCurrency.get(c) || 0;
                    if (tx.type === 'income') {
                        netByCurrency.set(c, net + tx.amount);
                    } else if (tx.type === 'expense') {
                        netByCurrency.set(c, net - tx.amount);
                    } else if (tx.type === 'transfer') {
                        if (currentWalletFilter === 'all') {
                            // Transfer antar dompet semata uang tidak mempengaruhi total netto keseluruhan
                        } else if (currentWalletFilter === (tx.walletId || DEFAULT_WALLET_ID)) {
                            netByCurrency.set(c, net - tx.amount);
                        } else if (currentWalletFilter === tx.toWalletId) {
                            netByCurrency.set(c, net + tx.amount);
                        }
                    }
                }

                const netEntries = Array.from(netByCurrency.entries());
                let totalClass = 'neutral';
                let totalText = '';
                if (netEntries.length === 0) {
                    totalText = currencyFormatter.format(0, 'IDR');
                } else if (netEntries.length === 1) {
                    const [c, val] = netEntries[0];
                    if (val > 0) {
                        totalClass = 'income';
                        totalText = '+' + currencyFormatter.format(val, c);
                    } else if (val < 0) {
                        totalClass = 'expense';
                        totalText = '−' + currencyFormatter.format(Math.abs(val), c);
                    } else {
                        totalClass = 'neutral';
                        totalText = currencyFormatter.format(0, c);
                    }
                } else {
                    totalText = netEntries.map(([c, val]) => {
                        if (val > 0) return `+${currencyFormatter.format(val, c)}`;
                        if (val < 0) return `−${currencyFormatter.format(Math.abs(val), c)}`;
                        return currencyFormatter.format(0, c);
                    }).join(' &bull; ');
                    totalClass = 'neutral';
                }

                const groupTitle = formatGroupDate(dateKey);

                groupEl.innerHTML = `
                    <div class="tx-date-group-header">
                        <span class="tx-date-group-title">${escapeHTML(groupTitle)}</span>
                        <span class="tx-date-group-total tabular-nums ${totalClass}">${totalText}</span>
                    </div>
                    <div class="tx-date-group-items"></div>
                `;

                const itemsContainer = groupEl.querySelector('.tx-date-group-items');
                for (const tx of groupTxList) {
                    itemsContainer.appendChild(createTxItemElement(tx, currentWalletFilter));
                }

                txListContainer.appendChild(groupEl);
            }
        }
    }

    if (recentTxListContainer) {
        recentTxListContainer.innerHTML = '';
        const recentTx = currentTransactions.slice(0, 5);
        if (recentTx.length === 0) {
            recentTxListContainer.innerHTML = `
                <div class="empty-state">
                    <p class="empty-state-text">Belum ada transaksi.</p>
                    <button type="button" class="btn-action-outline" id="btn-empty-add-tx">Tambah Transaksi</button>
                </div>
            `;
            const btnEmptyAddTx = document.getElementById('btn-empty-add-tx');
            if (btnEmptyAddTx) {
                btnEmptyAddTx.addEventListener('click', () => {
                    resetForm();
                    openFormScreen();
                });
            }
        } else {
            recentTx.forEach(tx => {
                recentTxListContainer.appendChild(createTxItemElement(tx, 'all'));
            });
        }
    }

    renderMonthlyChart(currentTransactions);
    renderBudgetSection();
    renderDonutChart(currentTransactions);
    renderTimbangan();

    // Pasang event listener tombol edit dan hapus pada kedua daftar transaksi
    document.querySelectorAll('.btn-edit').forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.stopPropagation();
            handleEdit(btn.dataset.id);
            openFormScreen();
        });
    });
    document.querySelectorAll('.btn-delete').forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.stopPropagation();
            handleDelete(btn.dataset.id);
        });
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
        const wrapperLi = document.createElement('li');
        wrapperLi.className = 'category-item-wrapper';

        const isEditing = editingCategory && editingCategory.type === type && editingCategory.name === cat;

        const catRow = document.createElement('div');
        catRow.className = 'category-item' + (isEditing ? ' editing' : '');

        if (isEditing) {
            catRow.innerHTML = `
                <form class="category-inline-edit-form">
                    <input type="text" class="category-edit-input" value="${escapeHTML(cat)}" required autocomplete="off" aria-label="Ubah nama kategori ${escapeHTML(cat)}">
                    <div class="category-item-actions">
                        <button type="submit" class="btn-icon btn-save-cat">Simpan</button>
                        <button type="button" class="btn-icon btn-cancel-cat">Batal</button>
                    </div>
                </form>
            `;

            const editForm = catRow.querySelector('.category-inline-edit-form');
            const editInput = catRow.querySelector('.category-edit-input');
            const btnCancelCat = catRow.querySelector('.btn-cancel-cat');

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

            requestAnimationFrame(() => {
                editInput.focus();
                editInput.select();
            });
        } else {
            catRow.innerHTML = `
                <span class="category-name">${escapeHTML(cat)}</span>
                <div class="category-item-actions">
                    <button type="button" class="btn-icon btn-add-subcat" data-type="${type}" data-cat="${escapeHTML(cat)}" aria-label="Tambah sub kategori untuk ${escapeHTML(cat)}">+ Sub</button>
                    <button type="button" class="btn-icon btn-edit-cat" data-type="${type}" data-name="${escapeHTML(cat)}" aria-label="Ubah nama kategori ${escapeHTML(cat)}">Ubah</button>
                    <button type="button" class="btn-icon btn-delete-cat" data-type="${type}" data-name="${escapeHTML(cat)}" aria-label="Hapus kategori ${escapeHTML(cat)}">Hapus</button>
                </div>
            `;

            catRow.querySelector('.btn-add-subcat').addEventListener('click', () => {
                addingSubcategoryFor = { type, cat };
                editingCategory = null;
                editingSubcategory = null;
                renderCategorySettings();
            });

            catRow.querySelector('.btn-edit-cat').addEventListener('click', () => {
                editingCategory = { type, name: cat };
                addingSubcategoryFor = null;
                editingSubcategory = null;
                renderCategorySettings();
            });

            catRow.querySelector('.btn-delete-cat').addEventListener('click', () => {
                handleDeleteCategoryPrompt(type, cat);
            });
        }
        wrapperLi.appendChild(catRow);

        // Sub categories list
        const subList = getSubcategoriesFor(type, cat);
        if (subList.length > 0) {
            const subUl = document.createElement('ul');
            subUl.className = 'subcategory-items';
            subUl.setAttribute('aria-label', `Daftar sub kategori ${escapeHTML(cat)}`);

            subList.forEach(sub => {
                const subLi = document.createElement('li');
                const isSubEditing = editingSubcategory && editingSubcategory.type === type && editingSubcategory.cat === cat && editingSubcategory.name === sub;

                if (isSubEditing) {
                    subLi.className = 'subcategory-item editing';
                    subLi.innerHTML = `
                        <form class="category-inline-edit-form" style="width: 100%;">
                            <input type="text" class="category-edit-input" value="${escapeHTML(sub)}" required autocomplete="off" aria-label="Ubah nama sub kategori ${escapeHTML(sub)}">
                            <div class="category-item-actions">
                                <button type="submit" class="btn-icon btn-save-cat">Simpan</button>
                                <button type="button" class="btn-icon btn-cancel-cat">Batal</button>
                            </div>
                        </form>
                    `;

                    const subEditForm = subLi.querySelector('.category-inline-edit-form');
                    const subEditInput = subLi.querySelector('.category-edit-input');
                    const subBtnCancel = subLi.querySelector('.btn-cancel-cat');

                    subEditForm.addEventListener('submit', async (e) => {
                        e.preventDefault();
                        await handleSaveSubcategoryName(type, cat, sub, subEditInput.value.trim());
                    });

                    subBtnCancel.addEventListener('click', () => {
                        editingSubcategory = null;
                        renderCategorySettings();
                    });

                    subEditInput.addEventListener('keydown', (e) => {
                        if (e.key === 'Escape') {
                            editingSubcategory = null;
                            renderCategorySettings();
                        }
                    });

                    requestAnimationFrame(() => {
                        subEditInput.focus();
                        subEditInput.select();
                    });
                } else {
                    subLi.className = 'subcategory-item';
                    subLi.innerHTML = `
                        <span class="subcategory-name">${escapeHTML(sub)}</span>
                        <div class="subcategory-item-actions">
                            <button type="button" class="btn-icon btn-edit-subcat" data-type="${type}" data-cat="${escapeHTML(cat)}" data-name="${escapeHTML(sub)}" aria-label="Ubah nama sub kategori ${escapeHTML(sub)}">Ubah</button>
                            <button type="button" class="btn-icon btn-delete-subcat" data-type="${type}" data-cat="${escapeHTML(cat)}" data-name="${escapeHTML(sub)}" aria-label="Hapus sub kategori ${escapeHTML(sub)}">Hapus</button>
                        </div>
                    `;

                    subLi.querySelector('.btn-edit-subcat').addEventListener('click', () => {
                        editingSubcategory = { type, cat, name: sub };
                        editingCategory = null;
                        addingSubcategoryFor = null;
                        renderCategorySettings();
                    });

                    subLi.querySelector('.btn-delete-subcat').addEventListener('click', () => {
                        handleDeleteSubcategoryPrompt(type, cat, sub);
                    });
                }

                subUl.appendChild(subLi);
            });

            wrapperLi.appendChild(subUl);
        }

        // Form tambah sub kategori jika aktif untuk kategori ini
        if (addingSubcategoryFor && addingSubcategoryFor.type === type && addingSubcategoryFor.cat === cat) {
            const addForm = document.createElement('form');
            addForm.className = 'subcategory-inline-form';
            addForm.innerHTML = `
                <input type="text" class="subcategory-add-input" placeholder="Tambah sub kategori ${escapeHTML(cat)}..." required autocomplete="off" aria-label="Tambah sub kategori ${escapeHTML(cat)}">
                <button type="submit" class="btn-icon btn-save-cat">Simpan</button>
                <button type="button" class="btn-icon btn-cancel-cat">Batal</button>
            `;

            const addInput = addForm.querySelector('.subcategory-add-input');
            const btnCancelAdd = addForm.querySelector('.btn-cancel-cat');

            addForm.addEventListener('submit', async (e) => {
                e.preventDefault();
                await handleAddSubcategory(type, cat, addInput.value.trim());
            });

            btnCancelAdd.addEventListener('click', () => {
                addingSubcategoryFor = null;
                renderCategorySettings();
            });

            addInput.addEventListener('keydown', (e) => {
                if (e.key === 'Escape') {
                    addingSubcategoryFor = null;
                    renderCategorySettings();
                }
            });

            wrapperLi.appendChild(addForm);
            requestAnimationFrame(() => {
                addInput.focus();
            });
        }

        container.appendChild(wrapperLi);
    });
}

async function handleSaveCategoryName(type, oldName, newName) {
    if (!newName) {
        showNotification('Nama kategori tidak boleh kosong.', 'warn');
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
        showNotification(`Kategori "${newName}" sudah ada di daftar ${type === 'income' ? 'pemasukan' : 'pengeluaran'}.`, 'warn');
        return;
    }

    ensureToken();

    const idx = list.indexOf(oldName);
    if (idx !== -1) {
        list[idx] = newName;
        if (currentCategories.subcategories && currentCategories.subcategories[type] && currentCategories.subcategories[type][oldName]) {
            currentCategories.subcategories[type][newName] = currentCategories.subcategories[type][oldName];
            delete currentCategories.subcategories[type][oldName];
        }
        currentCategories[type] = list;
        currentCategories.updatedAt = Date.now();
        try {
            await saveCategories(currentCategories);
            editingCategory = null;
            renderCategorySettings();
            renderBudgetSettings();
            renderBudgetSection();
            if (txCategoryInput.value === oldName) {
                populateCategoryOptions(newName, null, txSubcategoryInput ? txSubcategoryInput.value : null);
            } else {
                populateCategoryOptions(txCategoryInput.value, null, txSubcategoryInput ? txSubcategoryInput.value : null);
            }
            markDirty().catch(err => console.error("Gagal menandai perubahan kategori untuk sinkron", err));
        } catch (error) {
            console.error("Gagal mengubah nama kategori", error);
            showNotification("Terjadi kesalahan saat menyimpan kategori.", "error");
        }
    }
}

async function handleAddCategory(type, inputEl) {
    const name = inputEl.value.trim();
    if (!name) {
        showNotification('Nama kategori tidak boleh kosong.', 'warn');
        return;
    }

    const list = currentCategories[type] || [];
    const exists = list.some(c => c.toLowerCase() === name.toLowerCase());
    if (exists) {
        showNotification(`Kategori "${name}" sudah ada di daftar ${type === 'income' ? 'pemasukan' : 'pengeluaran'}.`, 'warn');
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
        populateCategoryOptions(txCategoryInput.value, null, txSubcategoryInput ? txSubcategoryInput.value : null);
        markDirty().catch(err => console.error("Gagal menambah kategori untuk sinkron", err));
    } catch (error) {
        console.error("Gagal menambah kategori", error);
        showNotification("Terjadi kesalahan saat menyimpan kategori.", "error");
    }
}

function handleDeleteCategoryPrompt(type, name) {
    pendingDeleteId = null;
    pendingCategoryDelete = { type, name };
    pendingSubcategoryDelete = null;
    pendingBudgetDelete = null;
    pendingWalletDelete = null;
    pendingLogout = false;
    pendingFillDummy = false;
    pendingClearDummy = false;
    if (confirmDialogTitle) {
        confirmDialogTitle.textContent = `Hapus kategori "${name}"?`;
    }
    if (btnConfirmDelete) {
        btnConfirmDelete.textContent = 'Hapus';
    }
    if (confirmDialog && typeof confirmDialog.showModal === 'function') {
        confirmDialog.showModal();
    }
}

async function performDeleteCategory(type, name) {
    ensureToken();

    const list = currentCategories[type] || [];
    currentCategories[type] = list.filter(c => c !== name);
    if (currentCategories.subcategories && currentCategories.subcategories[type]) {
        delete currentCategories.subcategories[type][name];
    }
    currentCategories.updatedAt = Date.now();

    try {
        await saveCategories(currentCategories);
        renderCategorySettings();
        renderBudgetSettings();
        renderBudgetSection();
        if (txCategoryInput.value === name) {
            populateCategoryOptions(null);
        } else {
            populateCategoryOptions(txCategoryInput.value, null, txSubcategoryInput ? txSubcategoryInput.value : null);
        }
        markDirty().catch(err => console.error("Gagal menandai penghapusan kategori untuk sinkron", err));
    } catch (error) {
        console.error("Gagal menghapus kategori", error);
        showNotification("Terjadi kesalahan saat menghapus kategori.", "error");
    }
}

async function handleAddSubcategory(type, cat, subName) {
    const name = subName.trim();
    if (!name) {
        showNotification('Nama sub kategori tidak boleh kosong.', 'warn');
        return;
    }

    if (!currentCategories.subcategories) {
        currentCategories.subcategories = { income: {}, expense: {} };
    }
    if (!currentCategories.subcategories[type]) {
        currentCategories.subcategories[type] = {};
    }
    if (!Array.isArray(currentCategories.subcategories[type][cat])) {
        currentCategories.subcategories[type][cat] = [];
    }

    const list = currentCategories.subcategories[type][cat];
    const exists = list.some(s => s.toLowerCase() === name.toLowerCase());
    if (exists) {
        showNotification(`Sub kategori "${name}" sudah ada di kategori "${cat}".`, 'warn');
        return;
    }

    ensureToken();

    list.push(name);
    currentCategories.updatedAt = Date.now();

    try {
        await saveCategories(currentCategories);
        addingSubcategoryFor = null;
        renderCategorySettings();
        if (txCategoryInput.value === cat) {
            populateSubcategoryOptions(cat, txSubcategoryInput ? txSubcategoryInput.value : null);
        }
        markDirty().catch(err => console.error("Gagal menambah sub kategori untuk sinkron", err));
        showNotification(`Sub kategori "${name}" berhasil ditambahkan.`, 'success');
    } catch (error) {
        console.error("Gagal menambah sub kategori", error);
        showNotification("Terjadi kesalahan saat menyimpan sub kategori.", "error");
    }
}

async function handleSaveSubcategoryName(type, cat, oldName, newName) {
    const name = newName.trim();
    if (!name) {
        showNotification('Nama sub kategori tidak boleh kosong.', 'warn');
        return;
    }
    if (name === oldName) {
        editingSubcategory = null;
        renderCategorySettings();
        return;
    }

    const list = getSubcategoriesFor(type, cat);
    const exists = list.some(s => s !== oldName && s.toLowerCase() === name.toLowerCase());
    if (exists) {
        showNotification(`Sub kategori "${name}" sudah ada di kategori "${cat}".`, 'warn');
        return;
    }

    ensureToken();

    const idx = list.indexOf(oldName);
    if (idx !== -1) {
        list[idx] = name;
        currentCategories.subcategories[type][cat] = list;
        currentCategories.updatedAt = Date.now();

        try {
            await saveCategories(currentCategories);
            let txChanged = false;
            const now = Date.now();
            for (const tx of currentTransactions) {
                if (tx.type === type && tx.category === cat && tx.subcategory === oldName) {
                    tx.subcategory = name;
                    tx.updatedAt = now;
                    await saveTransaction(tx);
                    txChanged = true;
                }
            }

            editingSubcategory = null;
            renderCategorySettings();
            if (txChanged) {
                render();
            }
            if (txCategoryInput.value === cat) {
                populateSubcategoryOptions(cat, txSubcategoryInput.value === oldName ? name : txSubcategoryInput.value);
            }
            markDirty().catch(err => console.error("Gagal mengubah nama sub kategori untuk sinkron", err));
            showNotification(`Sub kategori berhasil diubah menjadi "${name}".`, 'success');
        } catch (error) {
            console.error("Gagal mengubah nama sub kategori", error);
            showNotification("Terjadi kesalahan saat menyimpan sub kategori.", "error");
        }
    }
}

function handleDeleteSubcategoryPrompt(type, cat, subName) {
    pendingDeleteId = null;
    pendingCategoryDelete = null;
    pendingSubcategoryDelete = { type, cat, name: subName };
    pendingBudgetDelete = null;
    pendingWalletDelete = null;
    pendingLogout = false;
    pendingFillDummy = false;
    pendingClearDummy = false;
    if (confirmDialogTitle) {
        confirmDialogTitle.textContent = `Hapus sub kategori "${subName}"?`;
    }
    if (btnConfirmDelete) {
        btnConfirmDelete.textContent = 'Hapus';
    }
    if (confirmDialog && typeof confirmDialog.showModal === 'function') {
        confirmDialog.showModal();
    }
}

async function performDeleteSubcategory(type, cat, subName) {
    ensureToken();
    if (currentCategories && currentCategories.subcategories && currentCategories.subcategories[type]) {
        const list = currentCategories.subcategories[type][cat] || [];
        currentCategories.subcategories[type][cat] = list.filter(s => s !== subName);
        currentCategories.updatedAt = Date.now();
    }

    try {
        await saveCategories(currentCategories);

        let txChanged = false;
        const now = Date.now();
        for (const tx of currentTransactions) {
            if (tx.type === type && tx.category === cat && tx.subcategory === subName) {
                delete tx.subcategory;
                tx.updatedAt = now;
                await saveTransaction(tx);
                txChanged = true;
            }
        }

        renderCategorySettings();
        if (txChanged) {
            render();
        }
        if (txCategoryInput.value === cat) {
            populateSubcategoryOptions(cat, txSubcategoryInput ? txSubcategoryInput.value : null);
        }
        markDirty().catch(err => console.error("Gagal menandai penghapusan sub kategori untuk sinkron", err));
        showNotification(`Sub kategori "${subName}" berhasil dihapus.`, 'success');
    } catch (error) {
        console.error("Gagal menghapus sub kategori", error);
        showNotification("Terjadi kesalahan saat menghapus sub kategori.", "error");
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
            const curr = getWalletCurrency(w.id);
            li.innerHTML = `
                <div class="category-name-group">
                    <span class="category-name">${escapeHTML(w.name)}</span>
                    <span class="wallet-currency-badge">${curr}</span>
                </div>
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
        showNotification('Nama dompet tidak boleh kosong.', 'warn');
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
        showNotification(`Dompet "${newName}" sudah ada.`, 'warn');
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
        showNotification("Terjadi kesalahan saat menyimpan dompet.", "error");
    }
}

async function handleAddWallet(inputEl) {
    const name = inputEl.value.trim();
    if (!name) {
        showNotification('Nama dompet tidak boleh kosong.', 'warn');
        return;
    }

    const exists = getActiveWallets().some(w => w.name.toLowerCase() === name.toLowerCase());
    if (exists) {
        showNotification(`Dompet "${name}" sudah ada.`, 'warn');
        return;
    }

    ensureToken();

    const selectedCurrency = selectAddWalletCurrency && ['JPY', 'USD'].includes(selectAddWalletCurrency.value) ? selectAddWalletCurrency.value : 'IDR';
    const now = Date.now();
    const newWallet = {
        id: crypto.randomUUID(),
        name,
        currency: selectedCurrency,
        createdAt: now,
        updatedAt: now,
        deleted: false
    };

    currentWallets.push(newWallet);

    try {
        await saveWallets(currentWallets);
        inputEl.value = '';
        if (selectAddWalletCurrency) {
            selectAddWalletCurrency.value = 'IDR';
        }
        render();
        renderWalletSettings();
        populateWalletOptions(txWalletInput.value);
        populateWalletFilterOptions();
        markDirty().catch(err => console.error("Gagal menambah dompet untuk sinkron", err));
    } catch (error) {
        console.error("Gagal menambah dompet", error);
        showNotification("Terjadi kesalahan saat menyimpan dompet.", "error");
    }
}

function handleDeleteWalletPrompt(walletId) {
    const wallet = currentWallets.find(w => w.id === walletId);
    if (!wallet) return;

    const active = getActiveWallets();
    if (active.length <= 1) {
        showNotification('Dompet tidak bisa dihapus karena minimal harus ada satu dompet.', 'warn');
        return;
    }

    pendingDeleteId = null;
    pendingCategoryDelete = null;
    pendingBudgetDelete = null;
    pendingWalletDelete = walletId;
    pendingLogout = false;

    const bal = getWalletCurrentBalance(wallet.id);
    const curr = getWalletCurrency(wallet.id);
    const formattedBal = currencyFormatter.format(bal, curr);

    if (confirmDialogTitle) {
        confirmDialogTitle.textContent = `Hapus dompet "${wallet.name}" dengan sisa saldo ${formattedBal}?`;
    }
    if (btnConfirmDelete) {
        btnConfirmDelete.textContent = 'Hapus';
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
            populateWalletOptions();
            populateWalletFilterOptions();
            markDirty().catch(err => console.error("Gagal menandai penghapusan dompet untuk sinkron", err));
            showNotification(`Dompet "${wallet.name}" berhasil dihapus.`, 'success');
        } catch (error) {
            console.error("Gagal menghapus dompet", error);
            showNotification("Terjadi kesalahan saat menghapus dompet.", "error");
        }
    }
}

function getLocalYearMonthString(d = new Date()) {
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, '0');
    return `${y}-${m}`;
}

export function getActiveMonthlyExpenses(transactions, targetMonthKey, currency = 'IDR') {
    const categoryTotals = new Map();
    let totalExpense = 0;
    for (const tx of transactions || []) {
        if (tx.type !== 'expense' || tx.deleted || !(tx.amount > 0) || !tx.date) continue;
        if (getTransactionCurrency(tx) !== currency) continue;
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
    for (const [cat, val] of categoryTotals.entries()) {
        categoryTotals.set(cat, currency === 'USD' ? Math.round(val * 100) / 100 : Math.round(val));
    }
    totalExpense = currency === 'USD' ? Math.round(totalExpense * 100) / 100 : Math.round(totalExpense);
    return { categoryTotals, totalExpense };
}

function renderBudgetSection() {
    if (!budgetListContainer) return;

    const budgetMap = (currentBudgets && currentBudgets.items) ? currentBudgets.items : {};
    const budgetedCats = Object.keys(budgetMap).filter(cat => typeof budgetMap[cat] === 'number' && budgetMap[cat] > 0);

    if (budgetSection) {
        if (budgetedCats.length === 0) {
            budgetSection.classList.add('hidden');
            budgetListContainer.innerHTML = '';
            if (budgetSummaryBanner) {
                budgetSummaryBanner.innerHTML = '';
                budgetSummaryBanner.classList.add('hidden');
            }
            return;
        } else {
            budgetSection.classList.remove('hidden');
        }
    } else if (budgetedCats.length === 0) {
        budgetListContainer.innerHTML = '';
        if (budgetSummaryBanner) {
            budgetSummaryBanner.innerHTML = '';
            budgetSummaryBanner.classList.add('hidden');
        }
        return;
    }

    updateBudgetCurrencyUI();

    if (!selectedBudgetMonth) {
        selectedBudgetMonth = getLocalYearMonthString();
    }
    if (budgetMonthInput && budgetMonthInput.value !== selectedBudgetMonth) {
        budgetMonthInput.value = selectedBudgetMonth;
    }
    if (budgetMonthLabel) {
        const [yStr, mStr] = selectedBudgetMonth.split('-');
        const mNum = parseInt(mStr, 10);
        const monthName = INDO_MONTHS_SHORT[mNum - 1] || '';
        budgetMonthLabel.textContent = `${monthName} ${yStr}`;
    }

    if (currentBudgetCurrency !== 'IDR') {
        budgetListContainer.innerHTML = `
            <div class="empty-state">
                <p class="empty-state-text">Budget hanya tersedia untuk mata uang Rupiah.</p>
            </div>
        `;
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

    const { categoryTotals } = getActiveMonthlyExpenses(currentTransactions, selectedBudgetMonth, 'IDR');

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
            ? `Lewat ${currencyFormatter.format(lebih)}`
            : `Sisa ${currencyFormatter.format(sisa)}`;

        listHtml += `
            <div class="budget-item">
                <div class="budget-item-header">
                    <span class="budget-category-name">${escapeHTML(cat)}</span>
                    <span class="budget-spending tabular-nums">${formattedTerpakai} <span class="budget-spending-divider">dari</span> ${formattedBudget}</span>
                </div>
                <div class="budget-progress-track" role="progressbar" aria-valuenow="${Math.round(percent)}" aria-valuemin="0" aria-valuemax="100" aria-label="Progres budget ${escapeHTML(cat)}: ${Math.round(percent)}%">
                    <div class="budget-progress-fill ${toneClass}" style="width: ${percentClamped}%;"></div>
                </div>
                <div class="budget-item-footer">
                    <span class="budget-status-text ${toneClass}">${statusText}</span>
                    <span class="budget-percent tabular-nums ${toneClass}">${Math.round(percent)}%</span>
                </div>
            </div>
        `;
    }

    budgetListContainer.innerHTML = listHtml;

    if (budgetSummaryBanner) {
        const totalSisa = totalBudget - totalTerpakai;
        const totalOver = totalTerpakai > totalBudget;
        const summaryStatus = totalOver
            ? `Lewat ${currencyFormatter.format(totalTerpakai - totalBudget)}`
            : `Sisa ${currencyFormatter.format(Math.max(0, totalSisa))}`;
        const summaryTone = totalOver ? 'tone-danger' : (totalBudget > 0 && totalTerpakai / totalBudget >= 0.8 ? 'tone-warn' : 'tone-normal');

        budgetSummaryBanner.innerHTML = `
            <div class="budget-summary-banner-item">
                <span>Total Budget:</span>
                <strong class="tabular-nums">${currencyFormatter.format(totalBudget)}</strong>
            </div>
            <div class="budget-summary-banner-item">
                <span>Terpakai:</span>
                <strong class="tabular-nums">${currencyFormatter.format(totalTerpakai)}</strong>
            </div>
            <div class="budget-summary-banner-item">
                <span>${totalOver ? 'Kelebihan:' : 'Sisa:'}</span>
                <strong class="budget-detail-value tabular-nums ${summaryTone}">${summaryStatus}</strong>
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
        showNotification('Jumlah budget harus berupa angka lebih dari 0.', 'warn');
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
        showNotification("Terjadi kesalahan saat menyimpan budget.", "error");
    }
}

async function handleSetBudgetSubmit(e) {
    e.preventDefault();
    const cat = selectBudgetCat ? selectBudgetCat.value.trim() : '';
    const amount = inputBudgetAmount ? parseAmount(inputBudgetAmount.value) : 0;

    if (!cat) {
        showNotification('Pilih kategori pengeluaran.', 'warn');
        return;
    }
    if (isNaN(amount) || amount <= 0) {
        showNotification('Jumlah budget harus berupa angka lebih dari 0.', 'warn');
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
        showNotification("Terjadi kesalahan saat menyimpan budget.", "error");
    }
}

function handleDeleteBudgetPrompt(cat) {
    pendingDeleteId = null;
    pendingCategoryDelete = null;
    pendingBudgetDelete = cat;
    pendingWalletDelete = null;
    pendingLogout = false;
    if (confirmDialogTitle) {
        confirmDialogTitle.textContent = `Hapus budget untuk kategori "${cat}"?`;
    }
    if (btnConfirmDelete) {
        btnConfirmDelete.textContent = 'Hapus';
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
            showNotification("Terjadi kesalahan saat menghapus budget.", "error");
        }
    }
}


function resetForm() {
    txForm.reset();
    txIdInput.value = '';
    if (txSubcategoryInput) {
        txSubcategoryInput.value = '';
    }
    if (formGroupSubcategory) {
        formGroupSubcategory.classList.add('hidden');
    }
    if (subcategoryChipsContainer) {
        subcategoryChipsContainer.innerHTML = '';
    }
    // Default date to today
    const today = new Date();
    // Offset for local timezone
    const localToday = new Date(today.getTime() - (today.getTimezoneOffset() * 60000)).toISOString().split('T')[0];
    txDateInput.value = localToday;
    syncDateInputToSelects(localToday);
    setTimeValueToForm(getCurrentTimeString());
    hideTimeError();
    
    formTitle.textContent = 'Tambah Transaksi';
    btnCancel.classList.remove('hidden');
    if (btnDeleteFromForm) {
        btnDeleteFromForm.classList.add('hidden');
    }
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

    const selectedWalletId = (txWalletInput && txWalletInput.value) ? txWalletInput.value : DEFAULT_WALLET_ID;
    const curr = getWalletCurrency(selectedWalletId);
    const amount = parseAmount(txAmountInput.value, curr);
    if (isNaN(amount) || amount <= 0) {
        showNotification("Jumlah harus berupa angka lebih dari 0.", "warn");
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

    localStorage.setItem(LAST_WALLET_KEY, selectedWalletId);

    let selectedToWalletId = null;
    let selectedCategory = '';

    if (selectedType === 'transfer') {
        selectedToWalletId = txToWalletInput ? txToWalletInput.value : '';
        if (!selectedWalletId) {
            showNotification('Pilih dompet asal.', 'warn');
            return;
        }
        if (!selectedToWalletId) {
            showNotification('Pilih dompet tujuan.', 'warn');
            return;
        }
        if (selectedWalletId === selectedToWalletId) {
            showNotification('Dompet tujuan harus berbeda dari dompet asal.', 'warn');
            return;
        }
        const fromCurr = getWalletCurrency(selectedWalletId);
        const toCurr = getWalletCurrency(selectedToWalletId);
        if (fromCurr !== toCurr) {
            showNotification('Transfer hanya bisa dilakukan antar dompet dengan mata uang yang sama.', 'warn');
            return;
        }
        selectedCategory = 'Transfer';
    } else {
        selectedCategory = txCategoryInput ? txCategoryInput.value.trim() : '';
        if (!selectedCategory) {
            showNotification('Pilih kategori.', 'warn');
            return;
        }
    }

    const subcategoryVal = (txSubcategoryInput && txSubcategoryInput.value.trim()) ? txSubcategoryInput.value.trim() : null;

    const now = Date.now();
    const id = txIdInput.value || crypto.randomUUID();
    
    // Check if it's an update to preserve createdAt and dummy flag
    let createdAt = now;
    let isDummy = false;
    if (txIdInput.value) {
        const existingTx = currentTransactions.find(t => t.id === txIdInput.value);
        if (existingTx) {
            createdAt = existingTx.createdAt;
            isDummy = !!existingTx.dummy;
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

    if (isDummy) {
        tx.dummy = true;
    }

    if (selectedType === 'transfer') {
        tx.toWalletId = selectedToWalletId;
    } else if (subcategoryVal) {
        tx.subcategory = subcategoryVal;
    }

    try {
        await saveTransaction(tx);
        resetForm();
        closeFormScreen();
        await loadData();
        if (!isDummy) {
            markDirty().catch(err => console.error("Gagal menandai perubahan untuk sinkron", err));
        }
    } catch (error) {
        console.error("Gagal menyimpan transaksi", error);
        showNotification("Terjadi kesalahan saat menyimpan data.", "error");
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
    syncDateInputToSelects(tx.date);
    setTimeValueToForm(tx.time || '');
    hideTimeError();
    const txCurr = getWalletCurrency(tx.walletId || DEFAULT_WALLET_ID);
    txAmountInput.value = formatAmountForInput(tx.amount, txCurr);
    txNoteInput.value = tx.note || '';

    updateTypeUI(tx.type);

    if (tx.type === 'transfer') {
        populateWalletOptions(tx.walletId || DEFAULT_WALLET_ID, tx.toWalletId || null);
        formTitle.textContent = 'Ubah Transfer';
    } else {
        // Pilihan dompet: gunakan walletId transaksi atau fallback DEFAULT_WALLET_ID
        populateWalletOptions(tx.walletId || DEFAULT_WALLET_ID);

        // Pilihan kategori dan subkategori mengikuti tipe yang dipilih
        const list = (currentCategories && currentCategories[tx.type]) ? currentCategories[tx.type] : [];
        if (list.includes(tx.category)) {
            populateCategoryOptions(tx.category, null, tx.subcategory || null);
        } else {
            // Transaksi lama dengan kategori yang tidak ada di daftar tipenya
            // Minta pengguna memilih kategori dari daftar yang sesuai
            populateCategoryOptions(null, tx.category, tx.subcategory || null);
        }
        formTitle.textContent = 'Ubah Transaksi';
    }

    btnCancel.classList.remove('hidden');
    if (btnDeleteFromForm) {
        btnDeleteFromForm.classList.remove('hidden');
    }
    openFormScreen();
}

function handleDelete(id) {
    if (!id) return;
    const tx = currentTransactions.find(t => t.id === id);
    pendingDeleteId = id;
    pendingCategoryDelete = null;
    pendingBudgetDelete = null;
    pendingWalletDelete = null;
    pendingLogout = false;
    if (confirmDialogTitle) {
        confirmDialogTitle.textContent = (tx && tx.type === 'transfer') ? 'Hapus transfer ini?' : 'Hapus transaksi ini?';
    }
    if (btnConfirmDelete) {
        btnConfirmDelete.textContent = 'Hapus';
    }
    if (confirmDialog && typeof confirmDialog.showModal === 'function') {
        confirmDialog.showModal();
    }
}

async function performDelete(id) {
    try {
        const allTx = await getAllTransactions();
        const tx = allTx.find(t => t.id === id);
        
        if (tx) {
            if (tx.dummy) {
                const remaining = allTx.filter(t => t.id !== id);
                await replaceAllTransactions(remaining);
                await loadData();
                showNotification("Data contoh berhasil dihapus.", "success");
                return;
            }
            // Minta token dalam handler interaksi pengguna bila token sudah habis
            ensureToken();
            tx.deleted = true;
            tx.updatedAt = Date.now();
            await saveTransaction(tx);
            await loadData();
            markDirty().catch(err => console.error("Gagal menandai perubahan untuk sinkron", err));
        }
    } catch (error) {
        console.error("Gagal menghapus transaksi", error);
        showNotification("Terjadi kesalahan saat menghapus data.", "error");
    }
}

// Event Listeners
txForm.addEventListener('submit', handleSubmit);
btnCancel.addEventListener('click', () => {
    resetForm();
    closeFormScreen();
});
attachAmountInputFormatting(txAmountInput, () => getWalletCurrency(txWalletInput ? txWalletInput.value : DEFAULT_WALLET_ID));
attachAmountInputFormatting(inputBudgetAmount, () => 'IDR');

txTypeInputs.forEach(radio => {
    radio.addEventListener('change', () => {
        if (radio.value === 'transfer') {
            if (getActiveWallets().length < 2) {
                showNotification('Transfer membutuhkan minimal 2 dompet. Silakan tambah dompet baru terlebih dahulu di Pengaturan Dompet.', 'warn');
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
        const subcatDelete = pendingSubcategoryDelete;
        const bgtDelete = pendingBudgetDelete;
        const wltDelete = pendingWalletDelete;
        const isLogout = pendingLogout;
        const isFillDummy = pendingFillDummy;
        const isClearDummy = pendingClearDummy;

        if (!txId && !catDelete && !subcatDelete && !bgtDelete && !wltDelete && !isLogout && !isFillDummy && !isClearDummy) {
            confirmDialog.close();
            return;
        }

        btnConfirmDelete.disabled = true;
        try {
            if (isLogout) {
                await signOut();
            } else if (isFillDummy) {
                await performFillDummy();
            } else if (isClearDummy) {
                await performClearDummy();
            } else if (txId) {
                await performDelete(txId);
            } else if (catDelete) {
                await performDeleteCategory(catDelete.type, catDelete.name);
            } else if (subcatDelete) {
                await performDeleteSubcategory(subcatDelete.type, subcatDelete.cat, subcatDelete.name);
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
        pendingSubcategoryDelete = null;
        pendingBudgetDelete = null;
        pendingWalletDelete = null;
        pendingLogout = false;
        pendingFillDummy = false;
        pendingClearDummy = false;
        if (btnConfirmDelete) {
            btnConfirmDelete.textContent = 'Hapus';
            btnConfirmDelete.className = 'btn-danger';
        }
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
const btnImportJson = document.getElementById('btn-import-json');
const inputImportJson = document.getElementById('input-import-json');
const btnExportCsv = document.getElementById('btn-export-csv');
const btnFillDummy = document.getElementById('btn-fill-dummy');
const btnExitDummy = document.getElementById('btn-exit-dummy') || document.getElementById('btn-clear-dummy');
const btnBannerExitDummy = document.getElementById('btn-banner-exit-dummy');
const btnClearDummy = btnExitDummy;

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
    const finalSyncText = s.status === 'signed-out' && s.message ? `${s.message} ${text}` : text;
    const finalSyncTone = s.status === 'signed-out' && s.message ? 'error' : tone;

    if (syncStatusEl) {
        syncStatusEl.textContent = finalSyncText;
        syncStatusEl.dataset.tone = finalSyncTone;
    }

    if (settingsSyncStatusEl) {
        settingsSyncStatusEl.textContent = finalSyncText;
        settingsSyncStatusEl.dataset.tone = finalSyncTone;
    }

    updateHeaderSyncUI(s.status, finalSyncTone, finalSyncText);

    btnLogin.classList.toggle('hidden', linked);
    btnLogin.disabled = busy;
    btnLogout.classList.toggle('hidden', !linked);
    btnSyncNow.classList.toggle('hidden', !linked);
    btnSyncNow.disabled = busy;
}

function updateHeaderSyncUI(status, tone, text) {
    const headerSyncBtn = document.getElementById('header-sync-btn');
    const headerSyncIcon = document.getElementById('header-sync-icon');
    if (!headerSyncBtn || !headerSyncIcon) return;

    let state = 'unlinked';
    let label = 'Status sinkron: Belum terhubung';
    let iconSvg = '';

    if (tone === 'ok' || status === 'synced') {
        state = 'synced';
        label = `Status sinkron: Tersinkron (${text})`;
        // Centang accent outline
        iconSvg = `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><polyline points="20 6 9 17 4 12"></polyline></svg>`;
    } else if (tone === 'error' || tone === 'warn' || status === 'error' || status === 'needs-login' || status === 'offline') {
        state = 'warning';
        label = `Status sinkron: Perhatian (${text})`;
        // Peringatan amber outline
        iconSvg = `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>`;
    } else if (status === 'syncing' || status === 'connecting') {
        state = 'syncing';
        label = `Status sinkron: Menyinkronkan (${text})`;
        // Sync arrows outline
        iconSvg = `<svg class="icon-spin" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><polyline points="23 4 23 10 17 10"></polyline><polyline points="1 20 1 14 7 14"></polyline><path d="M3.51 9a9 9 0 0 1 14.85-3.36L23 10M1 14l4.64 4.36A9 9 0 0 0 20.49 15"></path></svg>`;
    } else {
        state = 'unlinked';
        label = `Status sinkron: Belum terhubung (${text})`;
        // Abu-abu cloud outline
        iconSvg = `<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M18 10h-1.26A8 8 0 1 0 9 20h9a5 5 0 0 0 0-10z"></path></svg>`;
    }

    headerSyncBtn.dataset.state = state;
    headerSyncBtn.setAttribute('aria-label', label);
    headerSyncBtn.setAttribute('title', label);
    headerSyncIcon.innerHTML = iconSvg;
}

btnLogin.addEventListener('click', () => {
    if (!navigator.onLine) {
        renderSyncState({ status: 'signed-out', message: 'Perlu koneksi internet untuk login.' });
        return;
    }
    signIn();
});

btnLogout.addEventListener('click', () => {
    pendingDeleteId = null;
    pendingCategoryDelete = null;
    pendingBudgetDelete = null;
    pendingWalletDelete = null;
    pendingLogout = true;
    if (confirmDialogTitle) {
        confirmDialogTitle.textContent = 'Logout dari Google? Data di perangkat ini tetap ada, tapi tidak disinkronkan lagi sampai login ulang.';
    }
    if (btnConfirmDelete) {
        btnConfirmDelete.textContent = 'Logout';
    }
    if (confirmDialog && typeof confirmDialog.showModal === 'function') {
        confirmDialog.showModal();
    }
});

btnSyncNow.addEventListener('click', () => syncNow({ interactive: true }));

let toastNotificationTimeout = null;

export function showNotification(message, type = 'info') {
    const toastEl = document.getElementById('toast-notification');
    if (!toastEl) {
        if (type === 'error') {
            console.error(`[Notification error]:`, message);
        } else {
            console.log(`[Notification ${type}]:`, message);
        }
        return;
    }

    toastEl.textContent = message;
    toastEl.className = 'toast-notification';
    if (type === 'success') {
        toastEl.classList.add('toast-success');
    } else if (type === 'error') {
        toastEl.classList.add('toast-error');
    } else if (type === 'warn') {
        toastEl.classList.add('toast-warn');
    }

    if (toastNotificationTimeout) {
        clearTimeout(toastNotificationTimeout);
    }

    requestAnimationFrame(() => {
        toastEl.classList.add('show');
    });

    toastNotificationTimeout = setTimeout(() => {
        toastEl.classList.remove('show');
    }, 3500);
}

export function isCapacitorApp() {
    return typeof window !== 'undefined' &&
           typeof window.Capacitor !== 'undefined' &&
           typeof window.Capacitor.isNativePlatform === 'function' &&
           window.Capacitor.isNativePlatform();
}

export function getCapacitorPlugin(name) {
    if (typeof window === 'undefined' || !window.Capacitor) return null;
    if (window.Capacitor.Plugins && window.Capacitor.Plugins[name]) {
        return window.Capacitor.Plugins[name];
    }
    if (typeof window.Capacitor.registerPlugin === 'function') {
        try {
            return window.Capacitor.registerPlugin(name);
        } catch (e) {
            console.warn(`Gagal meregistrasi plugin ${name}:`, e);
        }
    }
    return null;
}

export async function exportViaCapacitor(fileName, dataContent, dialogTitle) {
    const filesystem = getCapacitorPlugin('Filesystem');
    const share = getCapacitorPlugin('Share');

    if (!filesystem) {
        throw new Error('Plugin Capacitor Filesystem tidak tersedia di aplikasi ini.');
    }

    // Tulis ke direktori CACHE agar FileProvider dapat membagikannya tanpa izin penyimpanan sensitif
    const writeResult = await filesystem.writeFile({
        path: fileName,
        data: dataContent,
        directory: 'CACHE',
        encoding: 'utf8'
    });

    let targetUri = writeResult && writeResult.uri;
    if (!targetUri && typeof filesystem.getUri === 'function') {
        const uriResult = await filesystem.getUri({
            path: fileName,
            directory: 'CACHE'
        });
        targetUri = uriResult && uriResult.uri;
    }

    if (share && targetUri) {
        try {
            await share.share({
                title: fileName,
                url: targetUri,
                dialogTitle: dialogTitle || 'Simpan atau Bagikan File'
            });
            showNotification(`Ekspor ${fileName} berhasil.`, 'success');
        } catch (shareErr) {
            const errStr = String((shareErr && shareErr.message) || shareErr || '').toLowerCase();
            if (errStr.includes('cancel')) {
                // Pengguna menutup share sheet, namun file tetap sudah tersimpan
                showNotification(`File ${fileName} berhasil disimpan.`, 'success');
            } else {
                console.warn('Gagal membagikan lewat Share plugin:', shareErr);
                showNotification(`File ${fileName} berhasil disimpan.`, 'success');
            }
        }
    } else {
        showNotification(`File ${fileName} berhasil disimpan.`, 'success');
    }
}

export async function exportBackupJson() {
    try {
        const allTx = await getRealTransactions();
        const realTx = allTx.filter(tx => !tx.dummy);
        const cats = await getRealCategories();
        const bgs = await getRealBudgets();
        const wts = await getRealWallets();
        const stgs = await getAppSettings();
        const data = {
            app: 'mizan-okane',
            version: 2,
            schemaVersion: 2,
            exportedAt: new Date().toISOString(),
            transactions: realTx,
            wallets: wts,
            categories: cats,
            budgets: bgs,
            settings: stgs
        };
        const fileName = `mizan-okane-backup-${new Date().toISOString().split('T')[0]}.json`;
        const jsonContent = JSON.stringify(data, null, 2);

        if (isCapacitorApp()) {
            await exportViaCapacitor(fileName, jsonContent, 'Simpan atau Bagikan Cadangan JSON');
        } else {
            const blob = new Blob([jsonContent], { type: 'application/json' });
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = fileName;
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(url);
            showNotification(`Ekspor cadangan JSON berhasil diunduh.`, 'success');
        }
        return true;
    } catch (error) {
        console.error("Gagal mengekspor data", error);
        showNotification("Terjadi kesalahan saat mengekspor data.", "error");
        return false;
    }
}

if (btnExport) {
    btnExport.addEventListener('click', exportBackupJson);
}

function isValidTransaction(item) {
    if (!item || typeof item !== 'object') return false;
    if (typeof item.id !== 'string' || item.id.trim() === '') return false;
    if (item.type !== 'income' && item.type !== 'expense' && item.type !== 'transfer') return false;
    const amt = typeof item.amount === 'number' ? item.amount : Number(item.amount);
    if (!Number.isFinite(amt) || amt <= 0) return false;
    if (typeof item.date !== 'string' || item.date.trim() === '') return false;
    return true;
}

function sanitizeImportedTx(item) {
    const amt = typeof item.amount === 'number' ? item.amount : Number(item.amount);
    const createdAt = typeof item.createdAt === 'number' ? item.createdAt : Date.now();
    const updatedAt = typeof item.updatedAt === 'number' ? item.updatedAt : createdAt;
    const tx = {
        id: String(item.id).trim(),
        type: item.type,
        amount: Math.round(amt * 100) / 100,
        date: String(item.date).trim(),
        createdAt,
        updatedAt,
        deleted: !!item.deleted
    };
    if (typeof item.category === 'string') {
        tx.category = item.category;
    }
    if (typeof item.subcategory === 'string' && item.subcategory.trim()) {
        tx.subcategory = item.subcategory.trim();
    }
    if (typeof item.note === 'string') {
        tx.note = item.note;
    }
    if (typeof item.time === 'string' && item.time) {
        tx.time = item.time;
    }
    // Field walletId opsional; transaksi lama tanpa walletId tetap aman tanpa field ini
    if (typeof item.walletId === 'string' && item.walletId) {
        tx.walletId = item.walletId;
    }
    if (typeof item.toWalletId === 'string' && item.toWalletId) {
        tx.toWalletId = item.toWalletId;
    }
    return tx;
}

function mergeWalletsList(localWallets, importedWallets) {
    const map = new Map();
    if (Array.isArray(importedWallets)) {
        for (const w of importedWallets) {
            if (w && typeof w.id === 'string') {
                map.set(w.id, {
                    id: String(w.id),
                    name: String(w.name || '').trim(),
                    currency: (w.currency === 'JPY' || w.currency === 'USD') ? w.currency : 'IDR',
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
                const existing = map.get(w.id);
                if (!existing || (w.updatedAt || 0) >= (existing.updatedAt || 0)) {
                    map.set(w.id, {
                        id: String(w.id),
                        name: String(w.name || '').trim(),
                        currency: (w.currency === 'JPY' || w.currency === 'USD') ? w.currency : 'IDR',
                        createdAt: typeof w.createdAt === 'number' ? w.createdAt : 0,
                        updatedAt: typeof w.updatedAt === 'number' ? w.updatedAt : 0,
                        deleted: !!w.deleted
                    });
                }
            }
        }
    }
    if (!map.has(DEFAULT_WALLET_ID)) {
        map.set(DEFAULT_WALLET_ID, {
            id: DEFAULT_WALLET_ID,
            name: DEFAULT_WALLET_NAME,
            currency: 'IDR',
            createdAt: 0,
            updatedAt: 0,
            deleted: false
        });
    }
    return Array.from(map.values());
}

function walletsDiffer(w1, w2) {
    if (w1.length !== w2.length) return true;
    const m1 = new Map(w1.map(w => [w.id, w]));
    for (const item of w2) {
        const exist = m1.get(item.id);
        if (!exist) return true;
        if (exist.name !== item.name || exist.currency !== item.currency || exist.deleted !== item.deleted || exist.updatedAt !== item.updatedAt) {
            return true;
        }
    }
    return false;
}

async function handleImportJson(file) {
    if (isDemoMode()) {
        showNotification("Tidak dapat mengimpor saat dalam mode data contoh. Silakan keluar dari mode data contoh terlebih dahulu.", "warn");
        return;
    }
    let text;
    try {
        text = await file.text();
    } catch (readErr) {
        console.error("Gagal membaca file", readErr);
        showNotification("Gagal membaca file yang dipilih.", "error");
        return;
    }

    let parsed;
    try {
        parsed = JSON.parse(text);
    } catch (jsonErr) {
        console.error("JSON tidak valid", jsonErr);
        showNotification("File tidak valid atau bukan format JSON.", "error");
        return;
    }

    let rawTransactions = null;
    let rawCategories = null;
    let rawBudgets = null;
    let rawWallets = null;
    let rawSettings = null;

    if (Array.isArray(parsed)) {
        rawTransactions = parsed;
    } else if (parsed && typeof parsed === 'object') {
        if (Array.isArray(parsed.transactions)) {
            rawTransactions = parsed.transactions;
        }
        if (parsed.categories && typeof parsed.categories === 'object') {
            rawCategories = parsed.categories;
        }
        if (parsed.budgets && typeof parsed.budgets === 'object') {
            rawBudgets = parsed.budgets;
        }
        if (Array.isArray(parsed.wallets)) {
            rawWallets = parsed.wallets;
        }
        if (parsed.settings && typeof parsed.settings === 'object') {
            rawSettings = parsed.settings;
        }
    }

    if (!rawTransactions && !rawCategories && !rawBudgets && !rawWallets && !rawSettings) {
        showNotification("Format file cadangan tidak dikenali. Pastikan file berisi data cadangan mizan-okane.", "error");
        return;
    }

    const validImportedTx = [];
    if (Array.isArray(rawTransactions)) {
        for (const item of rawTransactions) {
            if (isValidTransaction(item) && !item.dummy) {
                validImportedTx.push(sanitizeImportedTx(item));
            }
        }
    }

    if (rawTransactions && rawTransactions.length > 0 && validImportedTx.length === 0) {
        showNotification("Tidak ada transaksi valid yang ditemukan di dalam file cadangan.", "warn");
        return;
    }

    try {
        const localTx = await getAllTransactions();
        const txMap = new Map();
        for (const tx of localTx) {
            if (tx && typeof tx.id === 'string') {
                txMap.set(tx.id, tx);
            }
        }

        let txAdded = 0;
        let txUpdated = 0;

        for (const impTx of validImportedTx) {
            const existing = txMap.get(impTx.id);
            if (!existing) {
                txMap.set(impTx.id, impTx);
                txAdded++;
            } else {
                const impTime = typeof impTx.updatedAt === 'number' ? impTx.updatedAt : (impTx.createdAt || 0);
                const existTime = typeof existing.updatedAt === 'number' ? existing.updatedAt : (existing.createdAt || 0);
                if (impTime > existTime) {
                    txMap.set(impTx.id, impTx);
                    txUpdated++;
                }
            }
        }

        if (txAdded > 0 || txUpdated > 0) {
            await replaceAllTransactions(Array.from(txMap.values()));
        }

        let categoriesChanged = false;
        if (rawCategories && Array.isArray(rawCategories.income) && Array.isArray(rawCategories.expense)) {
            const localCategories = await getCategories();
            const remoteCatTime = typeof rawCategories.updatedAt === 'number' ? rawCategories.updatedAt : 0;
            const localCatTime = localCategories.updatedAt || 0;
            if (remoteCatTime > localCatTime || (localCatTime === 0 && (rawCategories.income.length > 0 || rawCategories.expense.length > 0))) {
                await saveCategories(rawCategories);
                categoriesChanged = true;
            }
        }

        let budgetsChanged = false;
        if (rawBudgets && typeof rawBudgets.items === 'object' && !Array.isArray(rawBudgets.items)) {
            const localBudgets = await getBudgets();
            const remoteBudgetTime = typeof rawBudgets.updatedAt === 'number' ? rawBudgets.updatedAt : 0;
            const localBudgetTime = localBudgets.updatedAt || 0;
            if (remoteBudgetTime > localBudgetTime || (localBudgetTime === 0 && Object.keys(rawBudgets.items).length > 0)) {
                await saveBudgets(rawBudgets);
                budgetsChanged = true;
            }
        }

        let walletsChanged = false;
        if (Array.isArray(rawWallets) && rawWallets.length > 0) {
            const localWallets = await getWallets();
            const finalWallets = mergeWalletsList(localWallets, rawWallets);
            if (walletsDiffer(localWallets, finalWallets)) {
                await saveWallets(finalWallets);
                walletsChanged = true;
            }
        }

        let settingsChanged = false;
        if (rawSettings && typeof rawSettings === 'object') {
            const localSettings = await getAppSettings();
            const remoteSettingsTime = typeof rawSettings.updatedAt === 'number' ? rawSettings.updatedAt : 0;
            const localSettingsTime = localSettings.updatedAt || 0;
            if (remoteSettingsTime > localSettingsTime || (localSettingsTime === 0 && (rawSettings.theme || rawSettings.timeFormat))) {
                let appliedTheme = localSettings.theme;
                let appliedFormat = localSettings.timeFormat;
                if (rawSettings.theme && VALID_THEMES.includes(rawSettings.theme)) {
                    appliedTheme = rawSettings.theme;
                    try {
                        localStorage.setItem(THEME_STORAGE_KEY, appliedTheme);
                    } catch (e) {
                        console.warn("Gagal menyimpan tema dari impor", e);
                    }
                    applyThemeUI(appliedTheme);
                }
                if (rawSettings.timeFormat && (rawSettings.timeFormat === '12' || rawSettings.timeFormat === '24')) {
                    appliedFormat = rawSettings.timeFormat;
                    currentTimeFormat = appliedFormat;
                    await saveTimeFormat(appliedFormat);
                    applyTimeFormatUI(appliedFormat);
                }
                await saveSettings({
                    theme: appliedTheme,
                    timeFormat: appliedFormat,
                    updatedAt: remoteSettingsTime > 0 ? remoteSettingsTime : Date.now()
                });
                settingsChanged = true;
            }
        }

        const hasChanges = (txAdded > 0 || txUpdated > 0 || categoriesChanged || budgetsChanged || walletsChanged || settingsChanged);

        if (hasChanges) {
            resetForm();
            await loadData();
            await markDirty();
            render();
            if (currentTransactions && currentTransactions.length > 0) {
                renderMonthlyChart(currentTransactions);
                renderDonutChart(currentTransactions);
                renderTimbangan();
            }

            const details = [];
            if (txAdded > 0) details.push(`${txAdded} transaksi baru ditambahkan`);
            if (txUpdated > 0) details.push(`${txUpdated} transaksi diperbarui`);
            if (walletsChanged) details.push('daftar dompet diperbarui');
            if (categoriesChanged) details.push('kategori diperbarui');
            if (budgetsChanged) details.push('budget diperbarui');
            if (settingsChanged) details.push('pengaturan diperbarui');

            const detailStr = details.length > 0 ? details.join(', ') : 'data diperbarui';
            showNotification(`Impor data berhasil (${detailStr}).`, 'success');
        } else {
            showNotification("Semua data pada file cadangan sudah sesuai dengan data saat ini (tidak ada perubahan).", 'info');
        }
    } catch (err) {
        console.error("Gagal mengimpor data", err);
        showNotification("Terjadi kesalahan saat memproses data cadangan.", "error");
    }
}

if (btnImportJson && inputImportJson) {
    btnImportJson.addEventListener('click', () => {
        ensureToken();
        inputImportJson.click();
    });

    inputImportJson.addEventListener('change', async (e) => {
        const file = e.target.files && e.target.files[0];
        if (!file) return;
        try {
            await handleImportJson(file);
        } finally {
            inputImportJson.value = '';
        }
    });
}

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
        const allTx = await getRealTransactions();
        const activeTx = allTx.filter(tx => !tx.deleted && !tx.dummy);

        if (activeTx.length === 0) {
            showNotification('Belum ada transaksi untuk diekspor.', 'warn');
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

        const headers = ['tanggal', 'jam', 'dompet', 'tipe', 'kategori', 'jumlah', 'catatan', 'subkategori'];
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
                escapeCSVField(tx.note || ''),
                escapeCSVField(getTxSubcategory(tx) || '')
            ];
            rows.push(row.join(','));
        }

        const csvContent = rows.join('\r\n');
        const BOM = '\uFEFF';
        const fullCsvContent = BOM + csvContent;
        const fileName = `mizan-okane-transaksi-${getLocalDateString()}.csv`;

        if (isCapacitorApp()) {
            await exportViaCapacitor(fileName, fullCsvContent, 'Simpan atau Bagikan Transaksi CSV');
        } else {
            const blob = new Blob([fullCsvContent], { type: 'text/csv;charset=utf-8;' });
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = fileName;
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(url);
            showNotification(`Ekspor CSV berhasil diunduh.`, 'success');
        }
    } catch (error) {
        console.error("Gagal mengekspor CSV", error);
        showNotification("Terjadi kesalahan saat mengekspor CSV.", "error");
    }
});

// --- Data Contoh Lengkap ---

export function updateDemoModeUI() {
    const demoBanner = document.getElementById('demo-mode-indicator');
    const btnExit = document.getElementById('btn-exit-dummy');
    const isDemo = isDemoMode();

    if (demoBanner) {
        demoBanner.classList.toggle('hidden', !isDemo);
        demoBanner.setAttribute('aria-hidden', String(!isDemo));
    }

    if (btnExit) {
        btnExit.disabled = !isDemo;
        btnExit.setAttribute('aria-disabled', String(!isDemo));
    }

    if (isDemo) {
        const demoSyncNotice = 'Mode data contoh aktif. Sinkronisasi dengan Google Drive dinonaktifkan.';
        if (syncStatusEl) {
            syncStatusEl.textContent = demoSyncNotice;
            syncStatusEl.dataset.tone = 'warn';
        }
        if (settingsSyncStatusEl) {
            settingsSyncStatusEl.textContent = demoSyncNotice;
            settingsSyncStatusEl.dataset.tone = 'warn';
        }
        updateHeaderSyncUI('offline', 'warn', demoSyncNotice);
    }
}

export async function performFillDummy() {
    try {
        await activateSampleData();
        try { localStorage.removeItem(LAST_WALLET_KEY); } catch (e) {}
        currentWalletFilter = 'all';
        selectedBerandaMonth = '';
        selectedDonutMonth = '';
        activeDonutCategory = null;
        selectedTimbanganMonth = '';
        resetForm();
        await loadData();
        updateDemoModeUI();
        showNotification('Mode data contoh aktif. Data asli aman dan terpisah.', 'success');
    } catch (err) {
        console.error('Gagal mengisi data contoh', err);
        showNotification('Gagal mengisi data contoh.', 'error');
    }
}

export async function performExitDummy() {
    try {
        await clearSampleData();
        try { localStorage.removeItem(LAST_WALLET_KEY); } catch (e) {}
        currentWalletFilter = 'all';
        selectedBerandaMonth = '';
        selectedDonutMonth = '';
        activeDonutCategory = null;
        selectedTimbanganMonth = '';
        resetForm();
        await loadData();
        updateDemoModeUI();
        showNotification('Kembali ke data asli.', 'success');
    } catch (err) {
        console.error('Gagal keluar dari mode data contoh', err);
        showNotification('Gagal keluar dari mode data contoh.', 'error');
    }
}

export const performClearDummy = performExitDummy;

if (btnFillDummy) {
    btnFillDummy.addEventListener('click', () => {
        pendingDeleteId = null;
        pendingCategoryDelete = null;
        pendingSubcategoryDelete = null;
        pendingBudgetDelete = null;
        pendingWalletDelete = null;
        pendingLogout = false;
        pendingClearDummy = false;
        pendingFillDummy = true;
        if (confirmDialogTitle) {
            if (isDemoMode()) {
                confirmDialogTitle.textContent = 'Muat ulang data contoh? Seluruh data contoh akan dikembalikan ke setelan awal. Data asli Anda tetap aman.';
            } else {
                confirmDialogTitle.textContent = 'Aktifkan mode data contoh? Satu set data lengkap (dompet Rupiah, Yen, USD, kategori, budget, dan transaksi) akan dimuat untuk mencoba fitur. Data asli Anda aman dan tidak tertimpa.';
            }
        }
        if (btnConfirmDelete) {
            btnConfirmDelete.textContent = isDemoMode() ? 'Muat Ulang' : 'Aktifkan';
            btnConfirmDelete.className = 'btn-primary';
        }
        if (confirmDialog && typeof confirmDialog.showModal === 'function') {
            confirmDialog.showModal();
        }
    });
}

function handleExitDemoPrompt() {
    if (!isDemoMode()) {
        showNotification('Aplikasi sedang tidak dalam mode data contoh.', 'info');
        return;
    }
    pendingDeleteId = null;
    pendingCategoryDelete = null;
    pendingSubcategoryDelete = null;
    pendingBudgetDelete = null;
    pendingWalletDelete = null;
    pendingLogout = false;
    pendingFillDummy = false;
    pendingClearDummy = true;
    if (confirmDialogTitle) {
        confirmDialogTitle.textContent = 'Keluar dari mode data contoh? Aplikasi akan kembali menampilkan data asli Anda.';
    }
    if (btnConfirmDelete) {
        btnConfirmDelete.textContent = 'Keluar';
        btnConfirmDelete.className = 'btn-primary';
    }
    if (confirmDialog && typeof confirmDialog.showModal === 'function') {
        confirmDialog.showModal();
    }
}

if (btnExitDummy) {
    btnExitDummy.addEventListener('click', handleExitDemoPrompt);
}

if (btnBannerExitDummy) {
    btnBannerExitDummy.addEventListener('click', handleExitDemoPrompt);
}

// --- Hapus Semua Data / Reset Setelan Pabrik (Zona Bahaya) ---

export async function performDeleteAllData() {
    try {
        if (isLinked()) {
            if (!navigator.onLine) {
                showNotification('Tidak dapat mereset: perangkat sedang offline. Hubungkan ke internet untuk mengosongkan data di Google Drive.', 'error');
                return false;
            }
            await clearRemoteData();
        }

        // Reset database lokal (transaksi, dompet, kategori, sub kategori, budget, pengaturan)
        await resetDatabase();

        // Reset penyimpanan lokal (localStorage)
        try {
            localStorage.removeItem(LAST_WALLET_KEY);
            localStorage.setItem(THEME_STORAGE_KEY, 'system');
        } catch (e) {
            console.warn('Gagal mereset localStorage', e);
        }

        if (!isLinked()) {
            await setMeta('sync', { account: null, syncedRev: null, remoteVersion: null, lastSyncAt: null });
        }

        // Kembalikan state runtime UI ke nilai bawaan
        currentTimeFormat = DEFAULT_TIME_FORMAT;
        applyTimeFormatUI(currentTimeFormat);
        applyThemeUI('system');
        currentWalletFilter = 'all';
        currentChartType = 'expense';
        currentDonutType = 'expense';
        selectedBerandaMonth = '';
        selectedDonutMonth = '';
        activeDonutCategory = null;
        currentStatsCurrency = 'IDR';
        currentBudgetCurrency = 'IDR';
        currentTimbanganCurrency = 'IDR';
        selectedTimbanganMonth = '';
        resetForm();

        await loadData();
        updateDemoModeUI();

        showNotification('Aplikasi berhasil dikembalikan ke setelan awal.', 'success');
        return true;
    } catch (error) {
        console.error('Gagal mengembalikan aplikasi ke setelan awal', error);
        showNotification('Terjadi kesalahan saat mereset data: ' + (error?.message || error), 'error');
        return false;
    }
}

if (btnDeleteAllData) {
    btnDeleteAllData.addEventListener('click', () => {
        if (inputConfirmDeleteAll) {
            inputConfirmDeleteAll.value = '';
        }
        if (btnDeleteAllConfirm) {
            btnDeleteAllConfirm.disabled = true;
        }
        if (deleteAllDialog && typeof deleteAllDialog.showModal === 'function') {
            deleteAllDialog.showModal();
        }
    });
}

if (btnDialogExportJson) {
    btnDialogExportJson.addEventListener('click', async () => {
        await exportBackupJson();
    });
}

if (inputConfirmDeleteAll) {
    inputConfirmDeleteAll.addEventListener('input', () => {
        const isMatch = inputConfirmDeleteAll.value.trim() === 'HAPUS';
        if (btnDeleteAllConfirm) {
            btnDeleteAllConfirm.disabled = !isMatch;
        }
    });

    inputConfirmDeleteAll.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') {
            e.preventDefault();
            if (inputConfirmDeleteAll.value.trim() === 'HAPUS' && btnDeleteAllConfirm && !btnDeleteAllConfirm.disabled) {
                btnDeleteAllConfirm.click();
            }
        }
    });
}

if (btnDeleteAllCancel && deleteAllDialog) {
    btnDeleteAllCancel.addEventListener('click', () => {
        deleteAllDialog.close();
    });
}

if (btnDeleteAllConfirm) {
    btnDeleteAllConfirm.addEventListener('click', async () => {
        if (inputConfirmDeleteAll && inputConfirmDeleteAll.value.trim() !== 'HAPUS') {
            return;
        }
        btnDeleteAllConfirm.disabled = true;
        const originalText = btnDeleteAllConfirm.textContent;
        btnDeleteAllConfirm.textContent = 'Mereset...';
        try {
            const success = await performDeleteAllData();
            if (success && deleteAllDialog && deleteAllDialog.open) {
                deleteAllDialog.close();
            }
        } finally {
            if (btnDeleteAllConfirm) {
                btnDeleteAllConfirm.textContent = originalText;
                if (!deleteAllDialog || !deleteAllDialog.open) {
                    btnDeleteAllConfirm.disabled = true;
                }
            }
        }
    });
}

if (deleteAllDialog) {
    deleteAllDialog.addEventListener('close', () => {
        if (inputConfirmDeleteAll) {
            inputConfirmDeleteAll.value = '';
        }
        if (btnDeleteAllConfirm) {
            btnDeleteAllConfirm.disabled = true;
        }
    });

    if (!('closedBy' in HTMLDialogElement.prototype)) {
        deleteAllDialog.addEventListener('click', (event) => {
            if (event.target !== deleteAllDialog) return;
            const rect = deleteAllDialog.getBoundingClientRect();
            const isInside = (
                rect.top <= event.clientY &&
                event.clientY <= rect.top + rect.height &&
                rect.left <= event.clientX &&
                event.clientX <= rect.left + rect.width
            );
            if (!isInside) {
                deleteAllDialog.close();
            }
        });
    }
}

// Init
async function init() {
    try {
        await initDB();
        currentCategories = await getCategories();
        currentBudgets = await getBudgets();
        currentWallets = await getWallets();
        selectedBerandaMonth = getLocalYearMonthString();
        selectedBudgetMonth = getLocalYearMonthString();
        selectedDonutMonth = getLocalYearMonthString();
        selectedTimbanganMonth = getLocalYearMonthString();
        initDateSelectOptions();
        init24TimeSelectOptions();
        resetForm(); // Set default date, kategori, dan dompet
        await loadData();
        updateDemoModeUI();
        const initialTab = getTabFromHash();
        recordTabNavigation(initialTab);
        switchTab(initialTab);
    } catch (error) {
        console.error("Gagal inisialisasi database", error);
        showNotification("Browser Anda tidak mendukung penyimpanan lokal atau terjadi kesalahan.", "error");
        return;
    }

    if (btnFabAdd) {
        btnFabAdd.addEventListener('click', () => {
            resetForm();
            openFormScreen();
        });
    }

    if (btnFormBack) {
        btnFormBack.addEventListener('click', () => {
            resetForm();
            closeFormScreen();
        });
    }

    if (formScreenBackdrop) {
        formScreenBackdrop.addEventListener('click', () => {
            resetForm();
            closeFormScreen();
        });
    }

    if (btnDeleteFromForm) {
        btnDeleteFromForm.addEventListener('click', () => {
            const id = txIdInput.value;
            if (id) {
                closeFormScreen();
                handleDelete(id);
            }
        });
    }

    if (btnSeeAllTx) {
        btnSeeAllTx.addEventListener('click', () => {
            recordTabNavigation('riwayat');
            window.location.hash = 'riwayat';
            switchTab('riwayat');
        });
    }

    document.querySelectorAll('.nav-tab-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const tab = btn.dataset.tab;
            if (tab) {
                recordTabNavigation(tab);
                window.location.hash = tab;
                switchTab(tab);
            }
        });
    });

    window.addEventListener('hashchange', () => {
        if (isNavigatingBack) return;
        const tab = getTabFromHash();
        if (tab !== currentActiveTab) {
            recordTabNavigation(tab);
            switchTab(tab);
        }
    });

    window.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && formScreen && !formScreen.classList.contains('hidden')) {
            if ((!confirmDialog || !confirmDialog.open) && (!deleteAllDialog || !deleteAllDialog.open)) {
                resetForm();
                closeFormScreen();
            }
        }
    });

    const headerSyncBtn = document.getElementById('header-sync-btn');
    const headerSyncPopover = document.getElementById('header-sync-popover');
    if (headerSyncBtn && headerSyncPopover) {
        headerSyncBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            const isHidden = headerSyncPopover.classList.contains('hidden');
            headerSyncPopover.classList.toggle('hidden', !isHidden);
            headerSyncBtn.setAttribute('aria-expanded', String(isHidden));
        });

        document.addEventListener('click', (e) => {
            if (!headerSyncPopover.classList.contains('hidden')) {
                if (!headerSyncPopover.contains(e.target) && !headerSyncBtn.contains(e.target)) {
                    headerSyncPopover.classList.add('hidden');
                    headerSyncBtn.setAttribute('aria-expanded', 'false');
                }
            }
        });

        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape' && !headerSyncPopover.classList.contains('hidden')) {
                headerSyncPopover.classList.add('hidden');
                headerSyncBtn.setAttribute('aria-expanded', 'false');
                headerSyncBtn.focus();
            }
        });
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

    if (walletBalancesContainer) {
        walletBalancesContainer.addEventListener('click', (e) => {
            const btn = e.target.closest('.wallet-balance-item');
            if (!btn) return;
            const walletId = btn.dataset.walletId;
            if (!walletId) return;
            handleWalletCardClick(walletId);
        });
    }

    if (txWalletInput) {
        txWalletInput.addEventListener('change', () => {
            updateFormCurrencyUI();
            if (getSelectedType() === 'transfer' && txToWalletInput) {
                populateWalletOptions(txWalletInput.value, txToWalletInput.value);
            }
        });
    }

    if (txToWalletInput) {
        txToWalletInput.addEventListener('change', () => {
            if (getSelectedType() === 'transfer' && txWalletInput) {
                if (txWalletInput.value === txToWalletInput.value) {
                    const active = getActiveWallets();
                    const fromCurr = getWalletCurrency(txToWalletInput.value);
                    const other = active.find(w => w.id !== txToWalletInput.value && getWalletCurrency(w.id) === fromCurr);
                    if (other) {
                        txWalletInput.value = other.id;
                        updateFormCurrencyUI();
                    }
                }
            }
        });
    }

    if (btnBudgetCurrencyIdr) {
        btnBudgetCurrencyIdr.addEventListener('click', () => {
            if (currentBudgetCurrency !== 'IDR') {
                currentBudgetCurrency = 'IDR';
                renderBudgetSection();
            }
        });
    }

    if (btnBudgetCurrencyJpy) {
        btnBudgetCurrencyJpy.addEventListener('click', () => {
            if (!getAvailableCurrencies().includes('JPY')) return;
            if (currentBudgetCurrency !== 'JPY') {
                currentBudgetCurrency = 'JPY';
                renderBudgetSection();
            }
        });
    }

    if (btnBudgetCurrencyUsd) {
        btnBudgetCurrencyUsd.addEventListener('click', () => {
            if (!getAvailableCurrencies().includes('USD')) return;
            if (currentBudgetCurrency !== 'USD') {
                currentBudgetCurrency = 'USD';
                renderBudgetSection();
            }
        });
    }

    if (btnStatsCurrencyIdr) {
        btnStatsCurrencyIdr.addEventListener('click', () => {
            if (currentStatsCurrency !== 'IDR') {
                currentStatsCurrency = 'IDR';
                updateStatsCurrencyUI();
                renderMonthlyChart(currentTransactions);
                renderDonutChart(currentTransactions);
            }
        });
    }

    if (btnStatsCurrencyJpy) {
        btnStatsCurrencyJpy.addEventListener('click', () => {
            if (!getAvailableCurrencies().includes('JPY')) return;
            if (currentStatsCurrency !== 'JPY') {
                currentStatsCurrency = 'JPY';
                updateStatsCurrencyUI();
                renderMonthlyChart(currentTransactions);
                renderDonutChart(currentTransactions);
            }
        });
    }

    if (btnStatsCurrencyUsd) {
        btnStatsCurrencyUsd.addEventListener('click', () => {
            if (!getAvailableCurrencies().includes('USD')) return;
            if (currentStatsCurrency !== 'USD') {
                currentStatsCurrency = 'USD';
                updateStatsCurrencyUI();
                renderMonthlyChart(currentTransactions);
                renderDonutChart(currentTransactions);
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

    if (btnBerandaPrevMonth) {
        btnBerandaPrevMonth.addEventListener('click', () => shiftBerandaMonth(-1));
    }

    if (btnBerandaNextMonth) {
        btnBerandaNextMonth.addEventListener('click', () => shiftBerandaMonth(1));
    }

    if (btnBerandaTimbangan) {
        btnBerandaTimbangan.addEventListener('click', () => {
            recordTabNavigation('timbangan');
            window.location.hash = 'timbangan';
            switchTab('timbangan');
        });
    }

    if (btnTimbanganBack) {
        btnTimbanganBack.addEventListener('click', () => {
            if (window.handleAndroidBack) {
                window.handleAndroidBack();
            } else {
                recordTabNavigation('beranda');
                window.location.hash = 'beranda';
                switchTab('beranda');
            }
        });
    }

    if (btnTimbanganCurrencyIdr) {
        btnTimbanganCurrencyIdr.addEventListener('click', () => {
            if (currentTimbanganCurrency !== 'IDR') {
                currentTimbanganCurrency = 'IDR';
                renderTimbangan();
            }
        });
    }

    if (btnTimbanganCurrencyJpy) {
        btnTimbanganCurrencyJpy.addEventListener('click', () => {
            if (!getAvailableCurrencies().includes('JPY')) return;
            if (currentTimbanganCurrency !== 'JPY') {
                currentTimbanganCurrency = 'JPY';
                renderTimbangan();
            }
        });
    }

    if (btnTimbanganCurrencyUsd) {
        btnTimbanganCurrencyUsd.addEventListener('click', () => {
            if (!getAvailableCurrencies().includes('USD')) return;
            if (currentTimbanganCurrency !== 'USD') {
                currentTimbanganCurrency = 'USD';
                renderTimbangan();
            }
        });
    }

    if (btnTimbanganPrevMonth) {
        btnTimbanganPrevMonth.addEventListener('click', () => shiftTimbanganMonth(-1));
    }

    if (btnTimbanganNextMonth) {
        btnTimbanganNextMonth.addEventListener('click', () => shiftTimbanganMonth(1));
    }

    if (calcPriceInput) {
        attachAmountInputFormatting(calcPriceInput, () => currentTimbanganCurrency);
        calcPriceInput.addEventListener('input', () => renderTimbanganCalculator());
    }

    if (calcCategorySelect) {
        calcCategorySelect.addEventListener('change', () => renderTimbanganCalculator());
    }

    if (calcWalletSelect) {
        calcWalletSelect.addEventListener('change', () => renderTimbanganCalculator());
    }

    const syncTimeInputs = () => {
        const timeVal = getTimeValueFromForm();
        if (timeVal) {
            setTimeValueToForm(timeVal);
            hideTimeError();
        } else {
            if (txTimeInput) txTimeInput.value = '';
        }
    };
    if (txTimeHour24Select) txTimeHour24Select.addEventListener('change', syncTimeInputs);
    if (txTimeMinute24Select) txTimeMinute24Select.addEventListener('change', syncTimeInputs);
    if (txTimeHourSelect) txTimeHourSelect.addEventListener('change', syncTimeInputs);
    if (txTimeMinuteSelect) txTimeMinuteSelect.addEventListener('change', syncTimeInputs);
    if (txTimeAmpmSelect) txTimeAmpmSelect.addEventListener('change', syncTimeInputs);

    if (timeFormatRadios) {
        for (const radio of timeFormatRadios) {
            radio.addEventListener('change', async (e) => {
                if (e.target.checked) {
                    const newFormat = e.target.value === '12' ? '12' : '24';
                    currentTimeFormat = newFormat;
                    await saveTimeFormat(newFormat);
                    applyTimeFormatUI(newFormat);
                    render();
                    markDirty().catch(err => console.error("Gagal menandai perubahan format waktu untuk sinkron", err));
                }
            });
        }
    }

    // Inisialisasi tema tampilan
    const initialTheme = getStoredTheme();
    applyThemeUI(initialTheme);

    if (themeRadios) {
        for (const radio of themeRadios) {
            radio.addEventListener('change', async (e) => {
                if (e.target.checked) {
                    const newTheme = e.target.value;
                    try {
                        localStorage.setItem(THEME_STORAGE_KEY, newTheme);
                    } catch (err) {
                        console.warn('Gagal menyimpan tema:', err);
                    }
                    applyThemeUI(newTheme);
                    if (currentTransactions && currentTransactions.length > 0) {
                        renderMonthlyChart(currentTransactions);
                        renderDonutChart(currentTransactions);
                        renderTimbangan();
                    }
                    try {
                        await saveSettings({ theme: newTheme, timeFormat: currentTimeFormat, updatedAt: Date.now() });
                    } catch (err) {
                        console.warn('Gagal menyimpan pengaturan tema ke IndexedDB:', err);
                    }
                    markDirty().catch(err => console.error("Gagal menandai perubahan tema untuk sinkron", err));
                }
            });
        }
    }

    if (window.matchMedia) {
        const colorSchemeMedia = window.matchMedia('(prefers-color-scheme: dark)');
        const onSystemThemeChange = () => {
            if (getStoredTheme() === 'system') {
                applyTheme('system');
                if (currentTransactions && currentTransactions.length > 0) {
                    renderMonthlyChart(currentTransactions);
                    renderDonutChart(currentTransactions);
                    renderTimbangan();
                }
            }
        };
        if (colorSchemeMedia.addEventListener) {
            colorSchemeMedia.addEventListener('change', onSystemThemeChange);
        } else if (colorSchemeMedia.addListener) {
            colorSchemeMedia.addListener(onSystemThemeChange);
        }
    }

    // Inisialisasi navigasi kelompok dan submenu Pengaturan
    document.querySelectorAll('.settings-menu-row').forEach(row => {
        row.addEventListener('click', () => {
            const submenu = row.dataset.submenu;
            if (submenu) {
                openSettingsSubmenu(submenu);
            }
        });
    });

    document.querySelectorAll('.btn-submenu-back').forEach(btn => {
        btn.addEventListener('click', () => {
            closeSettingsSubmenu();
        });
    });

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
