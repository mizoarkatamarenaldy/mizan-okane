package com.mizoar.mizanokane.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.data.DefaultData
import com.mizoar.mizanokane.data.model.CategoriesData
import com.mizoar.mizanokane.data.model.CategoryItem
import com.mizoar.mizanokane.data.repository.MizanRepository
import com.mizoar.mizanokane.ui.CategoryIcons
import com.mizoar.mizanokane.ui.components.IconPickerDialog
import com.mizoar.mizanokane.ui.components.ReorderDragHandle
import com.mizoar.mizanokane.ui.theme.MizanTheme
import kotlinx.coroutines.launch

private data class PendingCategoryDelete(
    val type: String,
    val name: String
)

private data class PendingSubcategoryDelete(
    val type: String,
    val parentCategory: String,
    val name: String
)

private data class ActiveIconPickerState(
    val title: String,
    val currentSelectedId: String,
    val onSelect: (String) -> Unit
)

/**
 * Layar Kelola Kategori aplikasi Mizan Okane.
 * Sesuai dev/PLANSPEC.md item "Kotlin 4B: Kategori, sub kategori, ikon":
 * - Dua daftar terpisah: Pengeluaran dan Pemasukan
 * - Tambah, ubah inline, hapus kategori dan sub kategori
 * - Geser urutan kategori dan sub kategori menggunakan ≡ (ReorderDragHandle)
 * - Pemilih ikon kategori dan sub kategori
 * - Kategori tetap Equilibrium dan sub kategorinya tampil tanpa tombol ubah, hapus, geser
 */
@Composable
fun KelolaKategoriScreen(
    repository: MizanRepository,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    val categories by repository.getCategoriesFlow().collectAsState(initial = DefaultData.DEFAULT_CATEGORIES)
    val budgets by repository.getBudgetsFlow().collectAsState(initial = com.mizoar.mizanokane.data.model.BudgetsData())
    val transactions by repository.getAllTransactionsFlow().collectAsState(initial = emptyList())

    // Dialog konfirmasi hapus
    var pendingCategoryDelete by remember { mutableStateOf<PendingCategoryDelete?>(null) }
    var pendingSubcategoryDelete by remember { mutableStateOf<PendingSubcategoryDelete?>(null) }

    // Dialog pemilih ikon
    var activeIconPicker by remember { mutableStateOf<ActiveIconPickerState?>(null) }

    // Dialog Konfirmasi Hapus Kategori
    if (pendingCategoryDelete != null) {
        val target = pendingCategoryDelete!!
        AlertDialog(
            onDismissRequest = { pendingCategoryDelete = null },
            containerColor = MizanTheme.colors.surface,
            title = {
                Text(
                    text = stringResource(id = R.string.confirm_delete_category_title, target.name),
                    style = MaterialTheme.typography.titleMedium.copy(color = MizanTheme.colors.text)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            val now = System.currentTimeMillis()
                            val isExpense = target.type == "expense"
                            val currentList = if (isExpense) categories.expense else categories.income
                            val updatedList = currentList.filter { it.name != target.name }

                            val updatedSubsGroup = if (isExpense) {
                                val updatedMap = categories.subcategories.expense.toMutableMap()
                                updatedMap.remove(target.name)
                                categories.subcategories.copy(expense = updatedMap)
                            } else {
                                val updatedMap = categories.subcategories.income.toMutableMap()
                                updatedMap.remove(target.name)
                                categories.subcategories.copy(income = updatedMap)
                            }

                            val updatedCategories = if (isExpense) {
                                categories.copy(
                                    expense = updatedList,
                                    subcategories = updatedSubsGroup,
                                    updatedAt = now
                                )
                            } else {
                                categories.copy(
                                    income = updatedList,
                                    subcategories = updatedSubsGroup,
                                    updatedAt = now
                                )
                            }

                            // Hapus budget jika pengeluaran (kategori dan subkategori di bawahnya)
                            if (isExpense) {
                                var budgetChanged = false
                                val newItems = budgets.items.toMutableMap()
                                if (newItems.remove(target.name) != null) {
                                    budgetChanged = true
                                }
                                val newSubBudgets = budgets.subcategories.toMutableMap()
                                if (newSubBudgets.remove(target.name) != null) {
                                    budgetChanged = true
                                }
                                if (budgetChanged) {
                                    repository.saveBudgets(budgets.copy(items = newItems, subcategories = newSubBudgets, updatedAt = now))
                                }
                            }

                            repository.saveCategories(updatedCategories)
                            pendingCategoryDelete = null
                            Toast.makeText(
                                context,
                                context.getString(R.string.toast_category_deleted, target.name),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.action_hapus),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MizanTheme.colors.expense,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingCategoryDelete = null }) {
                    Text(
                        text = stringResource(id = R.string.action_batal),
                        style = MaterialTheme.typography.bodyMedium.copy(color = MizanTheme.colors.textSecondary)
                    )
                }
            }
        )
    }

    // Dialog Konfirmasi Hapus Sub Kategori
    if (pendingSubcategoryDelete != null) {
        val target = pendingSubcategoryDelete!!
        AlertDialog(
            onDismissRequest = { pendingSubcategoryDelete = null },
            containerColor = MizanTheme.colors.surface,
            title = {
                Text(
                    text = stringResource(id = R.string.confirm_delete_subcategory_title, target.name),
                    style = MaterialTheme.typography.titleMedium.copy(color = MizanTheme.colors.text)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            val now = System.currentTimeMillis()
                            val isExpense = target.type == "expense"
                            val groupMap = if (isExpense) categories.subcategories.expense.toMutableMap()
                            else categories.subcategories.income.toMutableMap()

                            val currentSubs = groupMap[target.parentCategory] ?: emptyList()
                            groupMap[target.parentCategory] = currentSubs.filter { it.name != target.name }

                            val updatedSubsGroup = if (isExpense) {
                                categories.subcategories.copy(expense = groupMap)
                            } else {
                                categories.subcategories.copy(income = groupMap)
                            }

                            val updatedCategories = categories.copy(
                                subcategories = updatedSubsGroup,
                                updatedAt = now
                            )

                            // Hapus subcategory budget jika ada
                            if (isExpense) {
                                val parentSubBudgets = budgets.subcategories[target.parentCategory]
                                if (parentSubBudgets != null && parentSubBudgets.containsKey(target.name)) {
                                    val newSubBudgetsMap = budgets.subcategories.toMutableMap()
                                    val newParentMap = parentSubBudgets.toMutableMap()
                                    newParentMap.remove(target.name)
                                    if (newParentMap.isEmpty()) {
                                        newSubBudgetsMap.remove(target.parentCategory)
                                    } else {
                                        newSubBudgetsMap[target.parentCategory] = newParentMap
                                    }
                                    repository.saveBudgets(budgets.copy(subcategories = newSubBudgetsMap, updatedAt = now))
                                }
                            }

                            // Bersihkan subcategory di transaksi terkait
                            for (tx in transactions) {
                                if (tx.type == target.type && tx.category == target.parentCategory && tx.subcategory == target.name) {
                                    repository.saveTransaction(tx.copy(subcategory = null, updatedAt = now))
                                }
                            }

                            repository.saveCategories(updatedCategories)
                            pendingSubcategoryDelete = null
                            Toast.makeText(
                                context,
                                context.getString(R.string.toast_subcategory_deleted, target.name),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.action_hapus),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MizanTheme.colors.expense,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingSubcategoryDelete = null }) {
                    Text(
                        text = stringResource(id = R.string.action_batal),
                        style = MaterialTheme.typography.bodyMedium.copy(color = MizanTheme.colors.textSecondary)
                    )
                }
            }
        )
    }

    // Dialog Pemilih Ikon
    if (activeIconPicker != null) {
        val picker = activeIconPicker!!
        IconPickerDialog(
            title = picker.title,
            currentSelectedId = picker.currentSelectedId,
            onIconSelected = { chosen ->
                picker.onSelect(chosen)
                activeIconPicker = null
            },
            onDismissRequest = { activeIconPicker = null }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MizanTheme.colors.bg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = stringResource(id = R.string.action_batal),
                    tint = MizanTheme.colors.text,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.title_kategori),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MizanTheme.colors.text
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // ==========================================
            // SEKSI PENGELUARAN
            // ==========================================
            CategoryTypeSection(
                type = "expense",
                title = stringResource(id = R.string.label_pengeluaran),
                placeholderAdd = stringResource(id = R.string.placeholder_kategori_pengeluaran),
                equilibriumSubcategories = listOf("Biaya admin", "Lupa catat", "Koreksi"),
                categories = categories,
                categoriesList = categories.expense,
                subcategoriesMap = categories.subcategories.expense,
                density = density,
                onAddCategory = { name, icon ->
                    val maxOrder = if (categories.expense.isNotEmpty()) categories.expense.maxOf { it.order } else -1
                    val newItem = CategoryItem(
                        name = name,
                        order = maxOrder + 1,
                        icon = icon.trim().takeIf { it.isNotEmpty() }
                    )
                    val updated = categories.copy(
                        expense = categories.expense + newItem,
                        updatedAt = System.currentTimeMillis()
                    )
                    coroutineScope.launch {
                        repository.saveCategories(updated)
                    }
                },
                onSaveCategory = { cat, newName, newIcon ->
                    coroutineScope.launch {
                        val now = System.currentTimeMillis()
                        val cleanIcon = newIcon.trim().takeIf { it.isNotEmpty() }
                        val updatedList = categories.expense.map {
                            if (it.name == cat.name) it.copy(name = newName, icon = cleanIcon) else it
                        }

                        val updatedSubsMap = categories.subcategories.expense.toMutableMap()
                        if (newName != cat.name && updatedSubsMap.containsKey(cat.name)) {
                            val subs = updatedSubsMap.remove(cat.name) ?: emptyList()
                            updatedSubsMap[newName] = subs
                        }

                        // Update budgets jika nama kategori berubah
                        if (newName != cat.name) {
                            var budgetChanged = false
                            val newItems = budgets.items.toMutableMap()
                            if (newItems.containsKey(cat.name)) {
                                val b = newItems.remove(cat.name)!!
                                newItems[newName] = b
                                budgetChanged = true
                            }
                            val newSubBudgets = budgets.subcategories.toMutableMap()
                            if (newSubBudgets.containsKey(cat.name)) {
                                val sb = newSubBudgets.remove(cat.name)!!
                                newSubBudgets[newName] = sb
                                budgetChanged = true
                            }
                            if (budgetChanged) {
                                repository.saveBudgets(budgets.copy(items = newItems, subcategories = newSubBudgets, updatedAt = now))
                            }
                        }

                        val updatedCategories = categories.copy(
                            expense = updatedList,
                            subcategories = categories.subcategories.copy(expense = updatedSubsMap),
                            updatedAt = now
                        )
                        repository.saveCategories(updatedCategories)
                    }
                },
                onDeleteCategory = { cat ->
                    pendingCategoryDelete = PendingCategoryDelete("expense", cat.name)
                },
                onReorderCategories = { reordered ->
                    val now = System.currentTimeMillis()
                    val reindexed = reordered.mapIndexed { idx, itm -> itm.copy(order = idx) }
                    val updated = categories.copy(expense = reindexed, updatedAt = now)
                    coroutineScope.launch {
                        repository.saveCategories(updated)
                    }
                },
                onAddSubcategory = { parent, name, icon ->
                    val currentSubs = categories.subcategories.expense[parent] ?: emptyList()
                    val maxOrder = if (currentSubs.isNotEmpty()) currentSubs.maxOf { it.order } else -1
                    val newSubItem = CategoryItem(
                        name = name,
                        order = maxOrder + 1,
                        icon = icon.trim().takeIf { it.isNotEmpty() }
                    )
                    val newMap = categories.subcategories.expense.toMutableMap()
                    newMap[parent] = currentSubs + newSubItem
                    val updated = categories.copy(
                        subcategories = categories.subcategories.copy(expense = newMap),
                        updatedAt = System.currentTimeMillis()
                    )
                    coroutineScope.launch {
                        repository.saveCategories(updated)
                    }
                },
                onSaveSubcategory = { parent, sub, newName, newIcon ->
                    coroutineScope.launch {
                        val now = System.currentTimeMillis()
                        val currentSubs = categories.subcategories.expense[parent] ?: emptyList()
                        val cleanIcon = newIcon.trim().takeIf { it.isNotEmpty() }
                        val updatedSubList = currentSubs.map {
                            if (it.name == sub.name) it.copy(name = newName, icon = cleanIcon) else it
                        }
                        val updatedGroup = categories.subcategories.expense.toMutableMap()
                        updatedGroup[parent] = updatedSubList

                        // Update budgets jika ada subcategory budget
                        if (newName != sub.name && budgets.subcategories[parent]?.containsKey(sub.name) == true) {
                            val parentSubBudgets = budgets.subcategories[parent]!!.toMutableMap()
                            val amount = parentSubBudgets.remove(sub.name)!!
                            parentSubBudgets[newName] = amount
                            val newSubBudgets = budgets.subcategories.toMutableMap()
                            newSubBudgets[parent] = parentSubBudgets
                            repository.saveBudgets(budgets.copy(subcategories = newSubBudgets, updatedAt = now))
                        }

                        // Update transaksi yang memakai subkategori lama
                        if (newName != sub.name) {
                            for (tx in transactions) {
                                if (tx.type == "expense" && tx.category == parent && tx.subcategory == sub.name) {
                                    repository.saveTransaction(tx.copy(subcategory = newName, updatedAt = now))
                                }
                            }
                        }

                        val updatedCategories = categories.copy(
                            subcategories = categories.subcategories.copy(expense = updatedGroup),
                            updatedAt = now
                        )
                        repository.saveCategories(updatedCategories)
                    }
                },
                onDeleteSubcategory = { parent, sub ->
                    pendingSubcategoryDelete = PendingSubcategoryDelete("expense", parent, sub.name)
                },
                onReorderSubcategories = { parent, reordered ->
                    val now = System.currentTimeMillis()
                    val reindexed = reordered.mapIndexed { idx, itm -> itm.copy(order = idx) }
                    val newMap = categories.subcategories.expense.toMutableMap()
                    newMap[parent] = reindexed
                    val updated = categories.copy(
                        subcategories = categories.subcategories.copy(expense = newMap),
                        updatedAt = now
                    )
                    coroutineScope.launch {
                        repository.saveCategories(updated)
                    }
                },
                onRequestIconPicker = { title, currIcon, onSelect ->
                    activeIconPicker = ActiveIconPickerState(title, currIcon, onSelect)
                }
            )

            // ==========================================
            // SEKSI PEMASUKAN
            // ==========================================
            CategoryTypeSection(
                type = "income",
                title = stringResource(id = R.string.label_pemasukan),
                placeholderAdd = stringResource(id = R.string.placeholder_kategori_pemasukan),
                equilibriumSubcategories = listOf("Bunga", "Cashback", "Koreksi"),
                categories = categories,
                categoriesList = categories.income,
                subcategoriesMap = categories.subcategories.income,
                density = density,
                onAddCategory = { name, icon ->
                    val maxOrder = if (categories.income.isNotEmpty()) categories.income.maxOf { it.order } else -1
                    val newItem = CategoryItem(
                        name = name,
                        order = maxOrder + 1,
                        icon = icon.trim().takeIf { it.isNotEmpty() }
                    )
                    val updated = categories.copy(
                        income = categories.income + newItem,
                        updatedAt = System.currentTimeMillis()
                    )
                    coroutineScope.launch {
                        repository.saveCategories(updated)
                    }
                },
                onSaveCategory = { cat, newName, newIcon ->
                    coroutineScope.launch {
                        val now = System.currentTimeMillis()
                        val cleanIcon = newIcon.trim().takeIf { it.isNotEmpty() }
                        val updatedList = categories.income.map {
                            if (it.name == cat.name) it.copy(name = newName, icon = cleanIcon) else it
                        }

                        val updatedSubsMap = categories.subcategories.income.toMutableMap()
                        if (newName != cat.name && updatedSubsMap.containsKey(cat.name)) {
                            val subs = updatedSubsMap.remove(cat.name) ?: emptyList()
                            updatedSubsMap[newName] = subs
                        }

                        val updatedCategories = categories.copy(
                            income = updatedList,
                            subcategories = categories.subcategories.copy(income = updatedSubsMap),
                            updatedAt = now
                        )
                        repository.saveCategories(updatedCategories)
                    }
                },
                onDeleteCategory = { cat ->
                    pendingCategoryDelete = PendingCategoryDelete("income", cat.name)
                },
                onReorderCategories = { reordered ->
                    val now = System.currentTimeMillis()
                    val reindexed = reordered.mapIndexed { idx, itm -> itm.copy(order = idx) }
                    val updated = categories.copy(income = reindexed, updatedAt = now)
                    coroutineScope.launch {
                        repository.saveCategories(updated)
                    }
                },
                onAddSubcategory = { parent, name, icon ->
                    val currentSubs = categories.subcategories.income[parent] ?: emptyList()
                    val maxOrder = if (currentSubs.isNotEmpty()) currentSubs.maxOf { it.order } else -1
                    val newSubItem = CategoryItem(
                        name = name,
                        order = maxOrder + 1,
                        icon = icon.trim().takeIf { it.isNotEmpty() }
                    )
                    val newMap = categories.subcategories.income.toMutableMap()
                    newMap[parent] = currentSubs + newSubItem
                    val updated = categories.copy(
                        subcategories = categories.subcategories.copy(income = newMap),
                        updatedAt = System.currentTimeMillis()
                    )
                    coroutineScope.launch {
                        repository.saveCategories(updated)
                    }
                },
                onSaveSubcategory = { parent, sub, newName, newIcon ->
                    coroutineScope.launch {
                        val now = System.currentTimeMillis()
                        val currentSubs = categories.subcategories.income[parent] ?: emptyList()
                        val cleanIcon = newIcon.trim().takeIf { it.isNotEmpty() }
                        val updatedSubList = currentSubs.map {
                            if (it.name == sub.name) it.copy(name = newName, icon = cleanIcon) else it
                        }
                        val updatedGroup = categories.subcategories.income.toMutableMap()
                        updatedGroup[parent] = updatedSubList

                        // Update transaksi yang memakai subkategori lama
                        if (newName != sub.name) {
                            for (tx in transactions) {
                                if (tx.type == "income" && tx.category == parent && tx.subcategory == sub.name) {
                                    repository.saveTransaction(tx.copy(subcategory = newName, updatedAt = now))
                                }
                            }
                        }

                        val updatedCategories = categories.copy(
                            subcategories = categories.subcategories.copy(income = updatedGroup),
                            updatedAt = now
                        )
                        repository.saveCategories(updatedCategories)
                    }
                },
                onDeleteSubcategory = { parent, sub ->
                    pendingSubcategoryDelete = PendingSubcategoryDelete("income", parent, sub.name)
                },
                onReorderSubcategories = { parent, reordered ->
                    val now = System.currentTimeMillis()
                    val reindexed = reordered.mapIndexed { idx, itm -> itm.copy(order = idx) }
                    val newMap = categories.subcategories.income.toMutableMap()
                    newMap[parent] = reindexed
                    val updated = categories.copy(
                        subcategories = categories.subcategories.copy(income = newMap),
                        updatedAt = now
                    )
                    coroutineScope.launch {
                        repository.saveCategories(updated)
                    }
                },
                onRequestIconPicker = { title, currIcon, onSelect ->
                    activeIconPicker = ActiveIconPickerState(title, currIcon, onSelect)
                }
            )
        }
    }
}

@Composable
private fun CategoryTypeSection(
    type: String,
    title: String,
    placeholderAdd: String,
    equilibriumSubcategories: List<String>,
    categories: CategoriesData,
    categoriesList: List<CategoryItem>,
    subcategoriesMap: Map<String, List<CategoryItem>>,
    density: Density,
    onAddCategory: (name: String, icon: String) -> Unit,
    onSaveCategory: (cat: CategoryItem, newName: String, newIcon: String) -> Unit,
    onDeleteCategory: (cat: CategoryItem) -> Unit,
    onReorderCategories: (List<CategoryItem>) -> Unit,
    onAddSubcategory: (parentCategory: String, name: String, icon: String) -> Unit,
    onSaveSubcategory: (parentCategory: String, sub: CategoryItem, newName: String, newIcon: String) -> Unit,
    onDeleteSubcategory: (parentCategory: String, sub: CategoryItem) -> Unit,
    onReorderSubcategories: (parentCategory: String, List<CategoryItem>) -> Unit,
    onRequestIconPicker: (title: String, currentIconId: String, onSelect: (String) -> Unit) -> Unit
) {
    val context = LocalContext.current

    // State daftar lokal untuk interaktivitas geser (drag swap)
    var localCatsList by remember(categoriesList) { mutableStateOf(categoriesList) }
    var localSubsMap by remember(subcategoriesMap) { mutableStateOf(subcategoriesMap) }

    // State drag reorder kategori
    var draggingCatName by remember { mutableStateOf<String?>(null) }
    var dragCatOffsetY by remember { mutableFloatStateOf(0f) }

    // State drag reorder sub kategori
    var draggingSubParent by remember { mutableStateOf<String?>(null) }
    var draggingSubName by remember { mutableStateOf<String?>(null) }
    var dragSubOffsetY by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(categoriesList) {
        if (draggingCatName == null) {
            localCatsList = categoriesList
        }
    }
    LaunchedEffect(subcategoriesMap) {
        if (draggingSubName == null) {
            localSubsMap = subcategoriesMap
        }
    }

    // State tambah kategori baru
    var newCatName by remember { mutableStateOf("") }
    var newCatIcon by remember { mutableStateOf("") }

    // State inline edit kategori
    var editingCatName by remember { mutableStateOf<String?>(null) }
    var editingCatText by remember { mutableStateOf("") }
    var editingCatIcon by remember { mutableStateOf("") }

    // State tambah sub kategori
    var addingSubForParent by remember { mutableStateOf<String?>(null) }
    var newSubText by remember { mutableStateOf("") }
    var newSubIcon by remember { mutableStateOf("") }

    // State inline edit sub kategori
    var editingSubKey by remember { mutableStateOf<String?>(null) } // "parent_sub"
    var editingSubText by remember { mutableStateOf("") }
    var editingSubIcon by remember { mutableStateOf("") }

    val itemHeightThreshold = with(density) { 56.dp.toPx() }
    val subItemHeightThreshold = with(density) { 48.dp.toPx() }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MizanTheme.colors.text
            )
        )

        // Form Tambah Kategori Baru
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MizanTheme.colors.surface)
                .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MizanTheme.colors.surfaceRaised)
                    .clickable {
                        onRequestIconPicker(
                            context.getString(R.string.title_pilih_ikon_kategori),
                            newCatIcon
                        ) { newCatIcon = it }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = CategoryIcons.getIconRes(newCatIcon)),
                    contentDescription = stringResource(id = R.string.title_pilih_ikon_kategori),
                    tint = MizanTheme.colors.text,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(modifier = Modifier.weight(1f)) {
                if (newCatName.isEmpty()) {
                    Text(
                        text = placeholderAdd,
                        style = MaterialTheme.typography.bodyMedium.copy(color = MizanTheme.colors.textSecondary)
                    )
                }
                BasicTextField(
                    value = newCatName,
                    onValueChange = { newCatName = it },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = MizanTheme.colors.text),
                    cursorBrush = SolidColor(MizanTheme.colors.accent),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    val name = newCatName.trim()
                    if (name.isEmpty()) {
                        Toast.makeText(context, context.getString(R.string.warn_category_empty), Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (name.equals("Equilibrium", ignoreCase = true)) {
                        Toast.makeText(context, context.getString(R.string.warn_category_equilibrium), Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val exists = categoriesList.any { it.name.trim().equals(name, ignoreCase = true) }
                    if (exists) {
                        val msg = if (type == "expense") context.getString(R.string.warn_category_exists_expense, name)
                        else context.getString(R.string.warn_category_exists_income, name)
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    onAddCategory(name, newCatIcon)
                    newCatName = ""
                    newCatIcon = ""
                    Toast.makeText(context, context.getString(R.string.toast_category_added, name), Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MizanTheme.colors.accent,
                    contentColor = MizanTheme.colors.bg
                ),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.action_tambah_transaksi).replace("Transaksi", "").trim(),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        // Daftar Kategori
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (localCatsList.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.empty_kategori),
                    style = MaterialTheme.typography.bodyMedium.copy(color = MizanTheme.colors.textSecondary),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            for (cat in localCatsList) {
                val isEditingThis = editingCatName == cat.name
                val isDraggingThis = draggingCatName == cat.name

                val catItemModifier = Modifier
                    .fillMaxWidth()
                    .zIndex(if (isDraggingThis) 2f else 1f)
                    .graphicsLayer {
                        if (isDraggingThis) {
                            translationY = dragCatOffsetY
                            shadowElevation = 8f
                        }
                    }

                Column(
                    modifier = catItemModifier,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isDraggingThis) MizanTheme.colors.surfaceRaised else MizanTheme.colors.surface)
                            .border(
                                width = if (isDraggingThis) 1.5.dp else 1.dp,
                                color = if (isDraggingThis) MizanTheme.colors.accent else MizanTheme.colors.border,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        if (isEditingThis) {
                            // Mode Inline Edit Kategori
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(MizanTheme.colors.surfaceRaised)
                                        .clickable {
                                            onRequestIconPicker(
                                                context.getString(R.string.title_pilih_ikon_kategori),
                                                editingCatIcon
                                            ) { editingCatIcon = it }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = CategoryIcons.getIconRes(editingCatIcon)),
                                        contentDescription = null,
                                        tint = MizanTheme.colors.accent,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                BasicTextField(
                                    value = editingCatText,
                                    onValueChange = { editingCatText = it },
                                    modifier = Modifier
                                        .weight(1f)
                                        .defaultMinSize(minHeight = 40.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(MizanTheme.colors.surfaceRaised)
                                        .border(1.dp, MizanTheme.colors.accent, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                                        color = MizanTheme.colors.text,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    cursorBrush = SolidColor(MizanTheme.colors.accent),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                TextButton(
                                    onClick = {
                                        val newName = editingCatText.trim()
                                        if (newName.isEmpty()) {
                                            Toast.makeText(context, context.getString(R.string.warn_category_empty), Toast.LENGTH_SHORT).show()
                                            return@TextButton
                                        }
                                        if (newName.equals("Equilibrium", ignoreCase = true)) {
                                            Toast.makeText(context, context.getString(R.string.warn_category_equilibrium), Toast.LENGTH_SHORT).show()
                                            return@TextButton
                                        }
                                        if (newName != cat.name && categoriesList.any { it.name.trim().equals(newName, ignoreCase = true) }) {
                                            val msg = if (type == "expense") context.getString(R.string.warn_category_exists_expense, newName)
                                            else context.getString(R.string.warn_category_exists_income, newName)
                                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                            return@TextButton
                                        }

                                        onSaveCategory(cat, newName, editingCatIcon)
                                        editingCatName = null
                                        Toast.makeText(context, context.getString(R.string.toast_category_updated, newName), Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.action_simpan),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MizanTheme.colors.accent
                                        )
                                    )
                                }

                                TextButton(onClick = { editingCatName = null }) {
                                    Text(
                                        text = stringResource(id = R.string.action_batal),
                                        style = MaterialTheme.typography.bodyMedium.copy(color = MizanTheme.colors.textSecondary)
                                    )
                                }
                            }
                        } else {
                            // Mode Normal Kategori
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(MizanTheme.colors.surfaceRaised),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = CategoryIcons.getIconRes(cat.icon)),
                                        contentDescription = null,
                                        tint = MizanTheme.colors.text,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Text(
                                    text = cat.name,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = MizanTheme.colors.text
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )

                                // Tombol + Sub
                                TextButton(
                                    onClick = {
                                        addingSubForParent = cat.name
                                        newSubText = ""
                                        newSubIcon = ""
                                        editingCatName = null
                                        editingSubKey = null
                                    }
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.action_tambah_subkategori),
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MizanTheme.colors.accent,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }

                                // Tombol Ubah
                                TextButton(
                                    onClick = {
                                        editingCatName = cat.name
                                        editingCatText = cat.name
                                        editingCatIcon = cat.icon ?: ""
                                        addingSubForParent = null
                                        editingSubKey = null
                                    }
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.action_ubah),
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MizanTheme.colors.textSecondary,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }

                                // Tombol Hapus
                                TextButton(
                                    onClick = { onDeleteCategory(cat) }
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.action_hapus),
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MizanTheme.colors.expense,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }

                                // Pegangan Geser ≡
                                ReorderDragHandle(
                                    isDragging = isDraggingThis,
                                    key = "cat_${type}_${cat.name}",
                                    onDragStart = {
                                        draggingCatName = cat.name
                                        dragCatOffsetY = 0f
                                    },
                                    onDrag = { dragAmountY ->
                                        dragCatOffsetY += dragAmountY
                                        val currentIndex = localCatsList.indexOfFirst { it.name == draggingCatName }
                                        if (currentIndex != -1) {
                                            if (dragCatOffsetY > itemHeightThreshold && currentIndex < localCatsList.size - 1) {
                                                val mutable = localCatsList.toMutableList()
                                                val item = mutable.removeAt(currentIndex)
                                                mutable.add(currentIndex + 1, item)
                                                localCatsList = mutable
                                                dragCatOffsetY -= itemHeightThreshold
                                            } else if (dragCatOffsetY < -itemHeightThreshold && currentIndex > 0) {
                                                val mutable = localCatsList.toMutableList()
                                                val item = mutable.removeAt(currentIndex)
                                                mutable.add(currentIndex - 1, item)
                                                localCatsList = mutable
                                                dragCatOffsetY += itemHeightThreshold
                                            }
                                        }
                                    },
                                    onDragEnd = {
                                        val dragged = draggingCatName
                                        draggingCatName = null
                                        dragCatOffsetY = 0f
                                        if (dragged != null) {
                                            onReorderCategories(localCatsList)
                                        }
                                    },
                                    onDragCancel = {
                                        draggingCatName = null
                                        dragCatOffsetY = 0f
                                    }
                                )
                            }
                        }
                    }

                    // Sub Kategori List di Bawah Kategori Induk
                    val subList = localSubsMap[cat.name] ?: emptyList()
                    if (subList.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            for (sub in subList) {
                                val subKey = "${cat.name}_${sub.name}"
                                val isSubEditingThis = editingSubKey == subKey
                                val isSubDraggingThis = draggingSubParent == cat.name && draggingSubName == sub.name

                                val subCardModifier = Modifier
                                    .fillMaxWidth()
                                    .zIndex(if (isSubDraggingThis) 2f else 1f)
                                    .graphicsLayer {
                                        if (isSubDraggingThis) {
                                            translationY = dragSubOffsetY
                                            shadowElevation = 6f
                                        }
                                    }
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSubDraggingThis) MizanTheme.colors.surfaceRaised else MizanTheme.colors.surfaceRaised.copy(alpha = 0.5f))
                                    .border(
                                        width = if (isSubDraggingThis) 1.5.dp else 1.dp,
                                        color = if (isSubDraggingThis) MizanTheme.colors.accent else MizanTheme.colors.border,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 6.dp)

                                Box(modifier = subCardModifier) {
                                    if (isSubEditingThis) {
                                        // Mode Inline Edit Sub Kategori
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .clip(CircleShape)
                                                    .background(MizanTheme.colors.surface)
                                                    .clickable {
                                                        onRequestIconPicker(
                                                            context.getString(R.string.title_pilih_ikon_subkategori),
                                                            editingSubIcon
                                                        ) { editingSubIcon = it }
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                val effectiveSubIcon = editingSubIcon.ifBlank { cat.icon ?: "" }
                                                Icon(
                                                    painter = painterResource(id = CategoryIcons.getIconRes(effectiveSubIcon)),
                                                    contentDescription = null,
                                                    tint = MizanTheme.colors.accent,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }

                                            Spacer(modifier = Modifier.width(8.dp))

                                            BasicTextField(
                                                value = editingSubText,
                                                onValueChange = { editingSubText = it },
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .defaultMinSize(minHeight = 36.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(MizanTheme.colors.surface)
                                                    .border(1.dp, MizanTheme.colors.accent, RoundedCornerShape(6.dp))
                                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                                    color = MizanTheme.colors.text,
                                                    fontWeight = FontWeight.SemiBold
                                                ),
                                                cursorBrush = SolidColor(MizanTheme.colors.accent),
                                                singleLine = true
                                            )

                                            Spacer(modifier = Modifier.width(8.dp))

                                            TextButton(
                                                onClick = {
                                                    val newName = editingSubText.trim()
                                                    if (newName.isEmpty()) {
                                                        Toast.makeText(context, context.getString(R.string.warn_subcategory_empty), Toast.LENGTH_SHORT).show()
                                                        return@TextButton
                                                    }
                                                    if (newName != sub.name && subList.any { it.name.trim().equals(newName, ignoreCase = true) }) {
                                                        Toast.makeText(context, context.getString(R.string.warn_subcategory_exists, newName, cat.name), Toast.LENGTH_SHORT).show()
                                                        return@TextButton
                                                    }

                                                    onSaveSubcategory(cat.name, sub, newName, editingSubIcon)
                                                    editingSubKey = null
                                                    Toast.makeText(context, context.getString(R.string.toast_subcategory_updated, newName), Toast.LENGTH_SHORT).show()
                                                }
                                            ) {
                                                Text(
                                                    text = stringResource(id = R.string.action_simpan),
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = MizanTheme.colors.accent
                                                    )
                                                )
                                            }

                                            TextButton(onClick = { editingSubKey = null }) {
                                                Text(
                                                    text = stringResource(id = R.string.action_batal),
                                                    style = MaterialTheme.typography.bodyMedium.copy(color = MizanTheme.colors.textSecondary)
                                                )
                                            }
                                        }
                                    } else {
                                        // Mode Normal Sub Kategori
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            val subIconRes = CategoryIcons.getSubcategoryIconRes(cat.name, sub.name, type, categories)
                                            Icon(
                                                painter = painterResource(id = subIconRes),
                                                contentDescription = null,
                                                tint = MizanTheme.colors.textSecondary,
                                                modifier = Modifier.size(16.dp)
                                            )

                                            Spacer(modifier = Modifier.width(8.dp))

                                            Text(
                                                text = sub.name,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    color = MizanTheme.colors.text
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.weight(1f)
                                            )

                                            TextButton(
                                                onClick = {
                                                    editingSubKey = subKey
                                                    editingSubText = sub.name
                                                    editingSubIcon = sub.icon ?: ""
                                                    editingCatName = null
                                                    addingSubForParent = null
                                                }
                                            ) {
                                                Text(
                                                    text = stringResource(id = R.string.action_ubah),
                                                    style = MaterialTheme.typography.bodySmall.copy(color = MizanTheme.colors.textSecondary)
                                                )
                                            }

                                            TextButton(
                                                onClick = { onDeleteSubcategory(cat.name, sub) }
                                            ) {
                                                Text(
                                                    text = stringResource(id = R.string.action_hapus),
                                                    style = MaterialTheme.typography.bodySmall.copy(color = MizanTheme.colors.expense)
                                                )
                                            }

                                            ReorderDragHandle(
                                                isDragging = isSubDraggingThis,
                                                key = "sub_${type}_${cat.name}_${sub.name}",
                                                onDragStart = {
                                                    draggingSubParent = cat.name
                                                    draggingSubName = sub.name
                                                    dragSubOffsetY = 0f
                                                },
                                                onDrag = { dragAmountY ->
                                                    dragSubOffsetY += dragAmountY
                                                    val group = localSubsMap[cat.name]?.toMutableList() ?: return@ReorderDragHandle
                                                    val currentIndex = group.indexOfFirst { it.name == draggingSubName }
                                                    if (currentIndex != -1) {
                                                        if (dragSubOffsetY > subItemHeightThreshold && currentIndex < group.size - 1) {
                                                            val item = group.removeAt(currentIndex)
                                                            group.add(currentIndex + 1, item)
                                                            val newMap = localSubsMap.toMutableMap()
                                                            newMap[cat.name] = group
                                                            localSubsMap = newMap
                                                            dragSubOffsetY -= subItemHeightThreshold
                                                        } else if (dragSubOffsetY < -subItemHeightThreshold && currentIndex > 0) {
                                                            val item = group.removeAt(currentIndex)
                                                            group.add(currentIndex - 1, item)
                                                            val newMap = localSubsMap.toMutableMap()
                                                            newMap[cat.name] = group
                                                            localSubsMap = newMap
                                                            dragSubOffsetY += subItemHeightThreshold
                                                        }
                                                    }
                                                },
                                                onDragEnd = {
                                                    val dragged = draggingSubName
                                                    val parent = draggingSubParent
                                                    draggingSubParent = null
                                                    draggingSubName = null
                                                    dragSubOffsetY = 0f
                                                    if (dragged != null && parent != null) {
                                                        val currentGroup = localSubsMap[parent] ?: emptyList()
                                                        onReorderSubcategories(parent, currentGroup)
                                                    }
                                                },
                                                onDragCancel = {
                                                    draggingSubParent = null
                                                    draggingSubName = null
                                                    dragSubOffsetY = 0f
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Form Inline Tambah Sub Kategori
                    if (addingSubForParent == cat.name) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(MizanTheme.colors.surface)
                                .border(1.dp, MizanTheme.colors.accent, RoundedCornerShape(6.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(MizanTheme.colors.surfaceRaised)
                                    .clickable {
                                        onRequestIconPicker(
                                            context.getString(R.string.title_pilih_ikon_subkategori),
                                            newSubIcon
                                        ) { newSubIcon = it }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                val effectiveIcon = newSubIcon.ifBlank { cat.icon ?: "" }
                                Icon(
                                    painter = painterResource(id = CategoryIcons.getIconRes(effectiveIcon)),
                                    contentDescription = null,
                                    tint = MizanTheme.colors.text,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(modifier = Modifier.weight(1f)) {
                                if (newSubText.isEmpty()) {
                                    Text(
                                        text = stringResource(id = R.string.placeholder_subkategori, cat.name),
                                        style = MaterialTheme.typography.bodySmall.copy(color = MizanTheme.colors.textSecondary)
                                    )
                                }
                                BasicTextField(
                                    value = newSubText,
                                    onValueChange = { newSubText = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = MizanTheme.colors.text),
                                    cursorBrush = SolidColor(MizanTheme.colors.accent),
                                    singleLine = true
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            TextButton(
                                onClick = {
                                    val name = newSubText.trim()
                                    if (name.isEmpty()) {
                                        Toast.makeText(context, context.getString(R.string.warn_subcategory_empty), Toast.LENGTH_SHORT).show()
                                        return@TextButton
                                    }
                                    val currentSubs = subcategoriesMap[cat.name] ?: emptyList()
                                    if (currentSubs.any { it.name.trim().equals(name, ignoreCase = true) }) {
                                        Toast.makeText(context, context.getString(R.string.warn_subcategory_exists, name, cat.name), Toast.LENGTH_SHORT).show()
                                        return@TextButton
                                    }

                                    onAddSubcategory(cat.name, name, newSubIcon)
                                    addingSubForParent = null
                                    newSubText = ""
                                    newSubIcon = ""
                                    Toast.makeText(context, context.getString(R.string.toast_subcategory_added, name), Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text(
                                    text = stringResource(id = R.string.action_simpan),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MizanTheme.colors.accent
                                    )
                                )
                            }

                            TextButton(onClick = { addingSubForParent = null }) {
                                Text(
                                    text = stringResource(id = R.string.action_batal),
                                    style = MaterialTheme.typography.bodyMedium.copy(color = MizanTheme.colors.textSecondary)
                                )
                            }
                        }
                    }
                }
            }

            // Baris Tetap Equilibrium
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MizanTheme.colors.surface)
                    .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MizanTheme.colors.surfaceRaised),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_neraca),
                            contentDescription = null,
                            tint = if (type == "expense") MizanTheme.colors.expense else MizanTheme.colors.accent,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Equilibrium",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            color = MizanTheme.colors.text
                        )
                    )
                }

                // Sub Kategori Tetap Equilibrium (tanpa tombol aksi)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    for (sub in equilibriumSubcategories) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(MizanTheme.colors.surfaceRaised.copy(alpha = 0.5f))
                                .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(6.dp))
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_neraca),
                                contentDescription = null,
                                tint = MizanTheme.colors.textSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = sub,
                                style = MaterialTheme.typography.bodyMedium.copy(color = MizanTheme.colors.textSecondary)
                            )
                        }
                    }
                }
            }
        }
    }
}
