import { initDB, saveTransaction, getAllTransactions } from './db.js';

// DOM Elements
const formTitle = document.getElementById('form-title');
const txForm = document.getElementById('tx-form');
const txIdInput = document.getElementById('tx-id');
const txTypeInputs = document.getElementsByName('type');
const txDateInput = document.getElementById('tx-date');
const txAmountInput = document.getElementById('tx-amount');
const txCategoryInput = document.getElementById('tx-category');
const txNoteInput = document.getElementById('tx-note');
const btnCancel = document.getElementById('btn-cancel');

const txListContainer = document.getElementById('tx-list');
const totalBalanceEl = document.getElementById('total-balance');
const totalIncomeEl = document.getElementById('total-income');
const totalExpenseEl = document.getElementById('total-expense');

let currentTransactions = [];

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

async function loadData() {
    try {
        const allTx = await getAllTransactions();
        currentTransactions = allTx.filter(tx => !tx.deleted);
        // Sort by date descending, then by createdAt descending
        currentTransactions.sort((a, b) => {
            if (a.date !== b.date) {
                return a.date > b.date ? -1 : 1;
            }
            return b.createdAt - a.createdAt;
        });
        render();
    } catch (error) {
        console.error("Gagal memuat data", error);
        alert("Terjadi kesalahan saat memuat data.");
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
                    <span class="tx-date">${formatDate(tx.date)}</span>
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

    try {
        await saveTransaction(tx);
        resetForm();
        await loadData();
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
    txAmountInput.value = tx.amount;
    txCategoryInput.value = tx.category;
    txNoteInput.value = tx.note || '';

    formTitle.textContent = 'Edit Transaksi';
    btnCancel.classList.remove('hidden');
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

async function handleDelete(id) {
    if (!confirm("Apakah Anda yakin ingin menghapus transaksi ini?")) {
        return;
    }

    try {
        const allTx = await getAllTransactions();
        const tx = allTx.find(t => t.id === id);
        
        if (tx) {
            tx.deleted = true;
            tx.updatedAt = Date.now();
            await saveTransaction(tx);
            await loadData();
        }
    } catch (error) {
        console.error("Gagal menghapus transaksi", error);
        alert("Terjadi kesalahan saat menghapus data.");
    }
}

// Event Listeners
txForm.addEventListener('submit', handleSubmit);
btnCancel.addEventListener('click', resetForm);

// Init
async function init() {
    try {
        await initDB();
        resetForm(); // Set default date
        await loadData();
    } catch (error) {
        console.error("Gagal inisialisasi database", error);
        alert("Browser Anda tidak mendukung penyimpanan lokal atau terjadi kesalahan.");
    }
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
