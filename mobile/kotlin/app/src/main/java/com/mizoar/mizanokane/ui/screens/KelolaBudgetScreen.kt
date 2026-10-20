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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.data.DefaultData
import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.data.model.BudgetsData
import com.mizoar.mizanokane.data.model.CategoryItem
import com.mizoar.mizanokane.data.repository.MizanRepository
import com.mizoar.mizanokane.ui.CategoryIcons
import com.mizoar.mizanokane.ui.components.CustomDropdown
import com.mizoar.mizanokane.ui.components.DropdownOption
import com.mizoar.mizanokane.ui.components.ReorderDragHandle
import com.mizoar.mizanokane.ui.theme.MizanTheme
import kotlinx.coroutines.launch

/**
 * Layar Kelola Budget aplikasi Mizan Okane.
 * Sesuai dev/PLANSPEC.md item "Kotlin 5A: Kelola budget":
 * - Daftar budget per kategori pengeluaran dan sub kategori di bawah kategorinya
 * - Ubah nominal budget secara inline
 * - Hapus budget kategori dan sub kategori dengan aturan cascading & validasi persis web
 * - Geser urutan budget kategori menggunakan pegangan ≡ via ReorderDragHandle (memperbarui urutan kategori)
 * - Form atur budget di bagian bawah kartu: dropdown pemilih kategori/sub kategori, nominal bulanan, tombol Simpan Budget
 * - Mendukung badge orphan untuk kategori budget yang tidak ada di daftar kategori
 */
@Composable
fun KelolaBudgetScreen(
    repository: MizanRepository,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    val categories by repository.getCategoriesFlow().collectAsState(initial = DefaultData.DEFAULT_CATEGORIES)
    val budgets by repository.getBudgetsFlow().collectAsState(initial = BudgetsData())

    // Kategori pengeluaran aktif (tanpa Equilibrium) yang terurut berdasarkan order
    val activeExpenseList = remember(categories) {
        categories.expense
            .filter { it.name != "Equilibrium" }
            .sortedBy { it.order }
            .map { it.name }
    }

    // Pemetaan budget aktif (> 0)
    val budgetMap = remember(budgets) {
        budgets.items.filter { it.value > 0L }
    }

    // Kategori yang memiliki budget, terurut mengikuti activeExpenseList lalu orphan cats
    val budgetedCats = remember(budgetMap, activeExpenseList) {
        val list = mutableListOf<String>()
        for (cat in activeExpenseList) {
            if (budgetMap.containsKey(cat)) {
                list.add(cat)
            }
        }
        val orphanCats = budgetMap.keys.filter { !activeExpenseList.contains(it) }.sorted()
        list.addAll(orphanCats)
        list
    }

    // State daftar kategori budget lokal untuk animasi geser interaktif
    var activeBudgetedCatsList by remember(budgetedCats) { mutableStateOf(budgetedCats) }

    // State geser urutan
    var draggingCategoryName by remember { mutableStateOf<String?>(null) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }

    // Sinkronkan daftar lokal bila budgetedCats berubah saat tidak sedang menggeser
    LaunchedEffect(budgetedCats) {
        if (draggingCategoryName == null) {
            activeBudgetedCatsList = budgetedCats
        }
    }

    // State inline edit
    var editingCategoryName by remember { mutableStateOf<String?>(null) }
    var editingCategoryAmountText by remember { mutableStateOf("") }

    var editingSubcategory by remember { mutableStateOf<Pair<String, String>?>(null) }
    var editingSubcategoryAmountText by remember { mutableStateOf("") }

    // State form tambah/atur budget
    var selectedDropdownId by remember { mutableStateOf<String?>(null) }
    var formAmountText by remember { mutableStateOf("") }

    // Dialog konfirmasi hapus budget
    var pendingDeleteCategory by remember { mutableStateOf<String?>(null) }
    var pendingDeleteSubcategory by remember { mutableStateOf<Pair<String, String>?>(null) }

    // Opsi dropdown kategori & sub kategori untuk form atur budget
    val dropdownOptions = remember(categories, budgets) {
        val options = mutableListOf<DropdownOption>()
        val expenseCats = categories.expense
            .filter { it.name != "Equilibrium" }
            .sortedBy { it.order }

        for (cat in expenseCats) {
            val catBudget = budgets.items[cat.name]
            val catLabel = if (catBudget != null && catBudget > 0L) {
                "${cat.name} (Budget: ${Uang.formatCurrency(catBudget, "IDR")})"
            } else {
                cat.name
            }
            options.add(
                DropdownOption(
                    id = "cat:${cat.name}",
                    label = catLabel,
                    iconRes = CategoryIcons.getCategoryIconRes(cat.name, "expense")
                )
            )

            val definedSubs = categories.subcategories.expense[cat.name]
                ?.sortedBy { it.order }
                ?: emptyList()

            for (sub in definedSubs) {
                val subBudget = budgets.subcategories[cat.name]?.get(sub.name)
                val subLabel = if (subBudget != null && subBudget > 0L) {
                    "    ↳ ${sub.name} (Budget: ${Uang.formatCurrency(subBudget, "IDR")})"
                } else {
                    "    ↳ ${sub.name}"
                }
                options.add(
                    DropdownOption(
                        id = "sub:${cat.name}:${sub.name}",
                        label = subLabel,
                        iconRes = CategoryIcons.getSubcategoryIconRes(cat.name, sub.name, "expense")
                    )
                )
            }
        }
        options
    }

    // Dialog Konfirmasi Hapus Budget Kategori
    if (pendingDeleteCategory != null) {
        val cat = pendingDeleteCategory!!
        AlertDialog(
            onDismissRequest = { pendingDeleteCategory = null },
            containerColor = MizanTheme.colors.surface,
            title = {
                Text(
                    text = stringResource(id = R.string.confirm_delete_budget_title, cat),
                    style = MaterialTheme.typography.titleMedium.copy(color = MizanTheme.colors.text)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val totalSubs = budgets.subcategories[cat]?.values?.filter { it > 0 }?.sum() ?: 0L
                        if (totalSubs > 0L) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.warn_budget_sub_exceeds_parent),
                                Toast.LENGTH_SHORT
                            ).show()
                            pendingDeleteCategory = null
                            return@TextButton
                        }

                        coroutineScope.launch {
                            val now = System.currentTimeMillis()
                            val newItems = budgets.items.toMutableMap()
                            newItems.remove(cat)
                            repository.saveBudgets(budgets.copy(items = newItems, updatedAt = now))
                            pendingDeleteCategory = null
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
                TextButton(onClick = { pendingDeleteCategory = null }) {
                    Text(
                        text = stringResource(id = R.string.action_batal),
                        style = MaterialTheme.typography.bodyMedium.copy(color = MizanTheme.colors.textSecondary)
                    )
                }
            }
        )
    }

    // Dialog Konfirmasi Hapus Budget Sub Kategori
    if (pendingDeleteSubcategory != null) {
        val (cat, sub) = pendingDeleteSubcategory!!
        AlertDialog(
            onDismissRequest = { pendingDeleteSubcategory = null },
            containerColor = MizanTheme.colors.surface,
            title = {
                Text(
                    text = stringResource(id = R.string.confirm_delete_sub_budget_title, sub),
                    style = MaterialTheme.typography.titleMedium.copy(color = MizanTheme.colors.text)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            val now = System.currentTimeMillis()
                            val parentSubBudgets = budgets.subcategories[cat]?.toMutableMap() ?: mutableMapOf()
                            parentSubBudgets.remove(sub)
                            val newSubBudgets = budgets.subcategories.toMutableMap()
                            if (parentSubBudgets.isEmpty()) {
                                newSubBudgets.remove(cat)
                            } else {
                                newSubBudgets[cat] = parentSubBudgets
                            }
                            repository.saveBudgets(budgets.copy(subcategories = newSubBudgets, updatedAt = now))
                            pendingDeleteSubcategory = null
                            Toast.makeText(
                                context,
                                context.getString(R.string.toast_sub_budget_deleted, sub),
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
                TextButton(onClick = { pendingDeleteSubcategory = null }) {
                    Text(
                        text = stringResource(id = R.string.action_batal),
                        style = MaterialTheme.typography.bodyMedium.copy(color = MizanTheme.colors.textSecondary)
                    )
                }
            }
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
        // === HEADER BAR ATAS ===
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(MizanTheme.colors.surface)
                .border(width = 0.5.dp, color = MizanTheme.colors.border)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = stringResource(id = R.string.action_batal),
                    tint = MizanTheme.colors.text
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = stringResource(id = R.string.title_kelola_budget),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MizanTheme.colors.text
                )
            )
        }

        // === KONTEN KELOLA BUDGET (SCROLLABLE) ===
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Kartu Pengaturan Budget Bulanan
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MizanTheme.colors.surface)
                    .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(id = R.string.title_budget_card),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MizanTheme.colors.text
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // === DAFTAR BUDGET PER KATEGORI ===
                    if (activeBudgetedCatsList.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.empty_budget),
                            style = MaterialTheme.typography.bodyMedium.copy(color = MizanTheme.colors.textSecondary),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        val itemHeightThreshold = with(density) { 56.dp.toPx() }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (cat in activeBudgetedCatsList) {
                                val amount = budgets.items[cat] ?: 0L
                                val isOrphan = !activeExpenseList.contains(cat)
                                val isDraggingThis = draggingCategoryName == cat
                                val isEditingCat = editingCategoryName == cat

                                val rowModifier = Modifier
                                    .fillMaxWidth()
                                    .zIndex(if (isDraggingThis) 2f else 1f)
                                    .graphicsLayer {
                                        if (isDraggingThis) {
                                            translationY = dragOffsetY
                                            shadowElevation = 8f
                                        }
                                    }
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isDraggingThis) MizanTheme.colors.surfaceRaised else MizanTheme.colors.surface)
                                    .border(
                                        width = if (isDraggingThis) 1.5.dp else 1.dp,
                                        color = if (isDraggingThis) MizanTheme.colors.accent else MizanTheme.colors.border,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 8.dp)

                                Column(
                                    modifier = rowModifier
                                ) {
                                    if (isEditingCat) {
                                        // Mode inline edit nominal budget kategori
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .defaultMinSize(minHeight = 44.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(MizanTheme.colors.surfaceRaised)
                                                    .border(1.dp, MizanTheme.colors.accent, RoundedCornerShape(6.dp))
                                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                                contentAlignment = Alignment.CenterStart
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(
                                                        text = "Rp ",
                                                        style = MaterialTheme.typography.bodyMedium.copy(
                                                            color = MizanTheme.colors.textSecondary,
                                                            fontWeight = FontWeight.SemiBold
                                                        )
                                                    )
                                                    BasicTextField(
                                                        value = editingCategoryAmountText,
                                                        onValueChange = { input ->
                                                            val digits = input.filter { it.isDigit() }
                                                            editingCategoryAmountText = if (digits.isNotEmpty()) {
                                                                Uang.formatNumber(digits.toLongOrNull() ?: 0L)
                                                            } else {
                                                                ""
                                                            }
                                                        },
                                                        modifier = Modifier.fillMaxWidth(),
                                                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                                                            color = MizanTheme.colors.text,
                                                            fontWeight = FontWeight.SemiBold
                                                        ),
                                                        cursorBrush = SolidColor(MizanTheme.colors.accent),
                                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                        singleLine = true
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.width(6.dp))

                                            TextButton(
                                                onClick = {
                                                    val parsed = Uang.parseAmountInput(editingCategoryAmountText, "IDR")
                                                    if (parsed <= 0L) {
                                                        Toast.makeText(
                                                            context,
                                                            context.getString(R.string.warn_budget_amount_invalid),
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        return@TextButton
                                                    }
                                                    val totalSubs = budgets.subcategories[cat]?.values?.filter { it > 0 }?.sum() ?: 0L
                                                    if (parsed < totalSubs) {
                                                        Toast.makeText(
                                                            context,
                                                            context.getString(R.string.warn_budget_sub_exceeds_parent),
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        return@TextButton
                                                    }

                                                    coroutineScope.launch {
                                                        val now = System.currentTimeMillis()
                                                        val newItems = budgets.items.toMutableMap()
                                                        newItems[cat] = parsed
                                                        repository.saveBudgets(budgets.copy(items = newItems, updatedAt = now))
                                                        editingCategoryName = null
                                                    }
                                                }
                                            ) {
                                                Text(
                                                    text = stringResource(id = R.string.action_simpan),
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        color = MizanTheme.colors.accent,
                                                        fontWeight = FontWeight.SemiBold
                                                    )
                                                )
                                            }

                                            TextButton(
                                                onClick = { editingCategoryName = null }
                                            ) {
                                                Text(
                                                    text = stringResource(id = R.string.action_batal),
                                                    style = MaterialTheme.typography.bodyMedium.copy(color = MizanTheme.colors.textSecondary)
                                                )
                                            }
                                        }
                                    } else {
                                        // Mode normal baris kategori: Ikon, Nama, Nominal, Tombol Ubah, Hapus, Pegangan ≡
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                painter = painterResource(id = CategoryIcons.getCategoryIconRes(cat, "expense")),
                                                contentDescription = null,
                                                tint = MizanTheme.colors.text,
                                                modifier = Modifier.size(22.dp)
                                            )

                                            Spacer(modifier = Modifier.width(10.dp))

                                            Column(modifier = Modifier.weight(1f)) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(
                                                        text = cat,
                                                        style = MaterialTheme.typography.bodyLarge.copy(
                                                            fontWeight = FontWeight.Medium,
                                                            color = MizanTheme.colors.text
                                                        ),
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                    if (isOrphan) {
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Text(
                                                            text = stringResource(id = R.string.budget_orphan_badge),
                                                            style = MaterialTheme.typography.bodySmall.copy(
                                                                color = MizanTheme.colors.expense,
                                                                fontSize = 11.sp
                                                            )
                                                        )
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = stringResource(
                                                        id = R.string.budget_per_bulan,
                                                        Uang.formatCurrency(amount, "IDR")
                                                    ),
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        color = MizanTheme.colors.textSecondary,
                                                        fontSize = 12.sp
                                                    )
                                                )
                                            }

                                            // Tombol Ubah
                                            TextButton(
                                                onClick = {
                                                    editingCategoryName = cat
                                                    editingCategoryAmountText = Uang.formatAmountForInput(amount, "IDR")
                                                    editingSubcategory = null
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
                                                onClick = {
                                                    val totalSubs = budgets.subcategories[cat]?.values?.filter { it > 0 }?.sum() ?: 0L
                                                    if (totalSubs > 0L) {
                                                        Toast.makeText(
                                                            context,
                                                            context.getString(R.string.warn_budget_sub_exceeds_parent),
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    } else {
                                                        pendingDeleteCategory = cat
                                                    }
                                                }
                                            ) {
                                                Text(
                                                    text = stringResource(id = R.string.action_hapus),
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        color = MizanTheme.colors.expense,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                )
                                            }

                                            // Pegangan geser urutan (≡) dengan ReorderDragHandle
                                            ReorderDragHandle(
                                                isDragging = isDraggingThis,
                                                key = cat,
                                                onDragStart = {
                                                    draggingCategoryName = cat
                                                    dragOffsetY = 0f
                                                },
                                                onDrag = { dragAmountY ->
                                                    dragOffsetY += dragAmountY
                                                    val currentIndex = activeBudgetedCatsList.indexOf(draggingCategoryName)
                                                    if (currentIndex != -1) {
                                                        if (dragOffsetY > itemHeightThreshold && currentIndex < activeBudgetedCatsList.size - 1) {
                                                            val mutable = activeBudgetedCatsList.toMutableList()
                                                            val item = mutable.removeAt(currentIndex)
                                                            mutable.add(currentIndex + 1, item)
                                                            activeBudgetedCatsList = mutable
                                                            dragOffsetY -= itemHeightThreshold
                                                        } else if (dragOffsetY < -itemHeightThreshold && currentIndex > 0) {
                                                            val mutable = activeBudgetedCatsList.toMutableList()
                                                            val item = mutable.removeAt(currentIndex)
                                                            mutable.add(currentIndex - 1, item)
                                                            activeBudgetedCatsList = mutable
                                                            dragOffsetY += itemHeightThreshold
                                                        }
                                                    }
                                                },
                                                onDragEnd = {
                                                    val draggedCat = draggingCategoryName
                                                    draggingCategoryName = null
                                                    dragOffsetY = 0f
                                                    if (draggedCat != null) {
                                                        coroutineScope.launch {
                                                            val rawExpenseList = categories.expense
                                                                .filter { it.name != "Equilibrium" }
                                                                .sortedBy { it.order }

                                                            val budgetSet = activeBudgetedCatsList.toSet()
                                                            val budgetQueue = ArrayDeque(activeBudgetedCatsList)
                                                            val newExpenseList = mutableListOf<CategoryItem>()

                                                            for (catItem in rawExpenseList) {
                                                                val name = catItem.name
                                                                if (budgetSet.contains(name)) {
                                                                    val nextBudgetName = budgetQueue.removeFirst()
                                                                    val existingItem = rawExpenseList.find { it.name == nextBudgetName }
                                                                    val itemObj = existingItem ?: CategoryItem(name = nextBudgetName, order = 0)
                                                                    newExpenseList.add(itemObj)
                                                                } else {
                                                                    newExpenseList.add(catItem)
                                                                }
                                                            }

                                                            while (budgetQueue.isNotEmpty()) {
                                                                val orphanName = budgetQueue.removeFirst()
                                                                newExpenseList.add(CategoryItem(name = orphanName, order = 0))
                                                            }

                                                            val orderChanged = newExpenseList.mapIndexed { idx, itm ->
                                                                itm.name != rawExpenseList.getOrNull(idx)?.name
                                                            }.any { it }

                                                            if (orderChanged) {
                                                                val reindexed = newExpenseList.mapIndexed { idx, itm -> itm.copy(order = idx) }
                                                                val now = System.currentTimeMillis()
                                                                val updatedCategories = categories.copy(expense = reindexed, updatedAt = now)
                                                                repository.saveCategories(updatedCategories)
                                                            }
                                                        }
                                                    }
                                                },
                                                onDragCancel = {
                                                    draggingCategoryName = null
                                                    dragOffsetY = 0f
                                                    activeBudgetedCatsList = budgetedCats
                                                }
                                            )
                                        }
                                    }

                                    // Sub kategori di bawah kategori induk
                                    val definedSubs = categories.subcategories.expense[cat]
                                        ?.sortedBy { it.order }
                                        ?.map { it.name }
                                        ?: emptyList()
                                    val subBudgetMap = budgets.subcategories[cat]?.filter { it.value > 0L } ?: emptyMap()
                                    val orderedSubs = mutableListOf<String>()
                                    for (s in definedSubs) {
                                        if (subBudgetMap.containsKey(s)) orderedSubs.add(s)
                                    }
                                    for (s in subBudgetMap.keys) {
                                        if (!orderedSubs.contains(s)) orderedSubs.add(s)
                                    }

                                    if (orderedSubs.isNotEmpty()) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 28.dp, top = 8.dp),
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            for (sub in orderedSubs) {
                                                val subAmount = subBudgetMap[sub] ?: 0L
                                                val isEditingSub = editingSubcategory?.first == cat && editingSubcategory?.second == sub

                                                if (isEditingSub) {
                                                    // Mode inline edit nominal budget sub kategori
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Box(
                                                            modifier = Modifier
                                                                .weight(1f)
                                                                .defaultMinSize(minHeight = 40.dp)
                                                                .clip(RoundedCornerShape(6.dp))
                                                                .background(MizanTheme.colors.surfaceRaised)
                                                                .border(1.dp, MizanTheme.colors.accent, RoundedCornerShape(6.dp))
                                                                .padding(horizontal = 10.dp, vertical = 6.dp),
                                                            contentAlignment = Alignment.CenterStart
                                                        ) {
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                Text(
                                                                    text = "Rp ",
                                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                                        color = MizanTheme.colors.textSecondary,
                                                                        fontWeight = FontWeight.SemiBold
                                                                    )
                                                                )
                                                                BasicTextField(
                                                                    value = editingSubcategoryAmountText,
                                                                    onValueChange = { input ->
                                                                        val digits = input.filter { it.isDigit() }
                                                                        editingSubcategoryAmountText = if (digits.isNotEmpty()) {
                                                                            Uang.formatNumber(digits.toLongOrNull() ?: 0L)
                                                                        } else {
                                                                            ""
                                                                        }
                                                                    },
                                                                    modifier = Modifier.fillMaxWidth(),
                                                                    textStyle = MaterialTheme.typography.bodySmall.copy(
                                                                        color = MizanTheme.colors.text,
                                                                        fontWeight = FontWeight.SemiBold
                                                                    ),
                                                                    cursorBrush = SolidColor(MizanTheme.colors.accent),
                                                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                                    singleLine = true
                                                                )
                                                            }
                                                        }

                                                        Spacer(modifier = Modifier.width(6.dp))

                                                        TextButton(
                                                            onClick = {
                                                                val parsed = Uang.parseAmountInput(editingSubcategoryAmountText, "IDR")
                                                                if (parsed <= 0L) {
                                                                    Toast.makeText(
                                                                        context,
                                                                        context.getString(R.string.warn_budget_amount_invalid),
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                    return@TextButton
                                                                }
                                                                val parentBudget = budgets.items[cat] ?: 0L
                                                                if (parentBudget <= 0L) {
                                                                    Toast.makeText(
                                                                        context,
                                                                        context.getString(R.string.warn_budget_parent_required),
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                    return@TextButton
                                                                }
                                                                val otherSubs = budgets.subcategories[cat]
                                                                    ?.filter { it.key != sub && it.value > 0L }
                                                                    ?.values
                                                                    ?.sum() ?: 0L
                                                                if (otherSubs + parsed > parentBudget) {
                                                                    Toast.makeText(
                                                                        context,
                                                                        context.getString(R.string.warn_budget_sub_exceeds_parent),
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                    return@TextButton
                                                                }

                                                                coroutineScope.launch {
                                                                    val now = System.currentTimeMillis()
                                                                    val parentSubBudgets = budgets.subcategories[cat]?.toMutableMap() ?: mutableMapOf()
                                                                    parentSubBudgets[sub] = parsed
                                                                    val newSubBudgets = budgets.subcategories.toMutableMap()
                                                                    newSubBudgets[cat] = parentSubBudgets
                                                                    repository.saveBudgets(budgets.copy(subcategories = newSubBudgets, updatedAt = now))
                                                                    editingSubcategory = null
                                                                }
                                                            }
                                                        ) {
                                                            Text(
                                                                text = stringResource(id = R.string.action_simpan),
                                                                style = MaterialTheme.typography.bodySmall.copy(
                                                                    color = MizanTheme.colors.accent,
                                                                    fontWeight = FontWeight.SemiBold
                                                                )
                                                            )
                                                        }

                                                        TextButton(
                                                            onClick = { editingSubcategory = null }
                                                        ) {
                                                            Text(
                                                                text = stringResource(id = R.string.action_batal),
                                                                style = MaterialTheme.typography.bodySmall.copy(color = MizanTheme.colors.textSecondary)
                                                            )
                                                        }
                                                    }
                                                } else {
                                                    // Mode normal baris sub kategori: Ikon, Nama, Nominal, Ubah, Hapus (tanpa pegangan geser)
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Icon(
                                                            painter = painterResource(id = CategoryIcons.getSubcategoryIconRes(cat, sub, "expense")),
                                                            contentDescription = null,
                                                            tint = MizanTheme.colors.textSecondary,
                                                            modifier = Modifier.size(18.dp)
                                                        )

                                                        Spacer(modifier = Modifier.width(8.dp))

                                                        Column(modifier = Modifier.weight(1f)) {
                                                            Text(
                                                                text = sub,
                                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                                    color = MizanTheme.colors.text
                                                                ),
                                                                maxLines = 1,
                                                                overflow = TextOverflow.Ellipsis
                                                            )
                                                            Text(
                                                                text = stringResource(
                                                                    id = R.string.budget_per_bulan,
                                                                    Uang.formatCurrency(subAmount, "IDR")
                                                                ),
                                                                style = MaterialTheme.typography.bodySmall.copy(
                                                                    color = MizanTheme.colors.textSecondary,
                                                                    fontSize = 11.sp
                                                                )
                                                            )
                                                        }

                                                        // Tombol Ubah Sub Kategori
                                                        TextButton(
                                                            onClick = {
                                                                editingSubcategory = Pair(cat, sub)
                                                                editingSubcategoryAmountText = Uang.formatAmountForInput(subAmount, "IDR")
                                                                editingCategoryName = null
                                                            }
                                                        ) {
                                                            Text(
                                                                text = stringResource(id = R.string.action_ubah),
                                                                style = MaterialTheme.typography.bodySmall.copy(
                                                                    color = MizanTheme.colors.textSecondary,
                                                                    fontSize = 12.sp
                                                                )
                                                            )
                                                        }

                                                        // Tombol Hapus Sub Kategori
                                                        TextButton(
                                                            onClick = {
                                                                pendingDeleteSubcategory = Pair(cat, sub)
                                                            }
                                                        ) {
                                                            Text(
                                                                text = stringResource(id = R.string.action_hapus),
                                                                style = MaterialTheme.typography.bodySmall.copy(
                                                                    color = MizanTheme.colors.expense,
                                                                    fontSize = 12.sp
                                                                )
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Garis pemisah antara daftar dan form
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MizanTheme.colors.border)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // === FORM ATUR NOMINAL BUDGET ===
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Dropdown pemilih kategori atau sub kategori
                        CustomDropdown(
                            selectedId = selectedDropdownId,
                            options = dropdownOptions,
                            placeholder = stringResource(id = R.string.placeholder_pilih_budget_item),
                            onOptionSelected = { id ->
                                selectedDropdownId = id
                                if (id.startsWith("cat:")) {
                                    val cat = id.removePrefix("cat:")
                                    val b = budgets.items[cat]
                                    formAmountText = if (b != null && b > 0L) Uang.formatAmountForInput(b, "IDR") else ""
                                } else if (id.startsWith("sub:")) {
                                    val parts = id.removePrefix("sub:").split(":")
                                    val cat = parts[0]
                                    val sub = parts.getOrNull(1) ?: ""
                                    val b = budgets.subcategories[cat]?.get(sub)
                                    formAmountText = if (b != null && b > 0L) Uang.formatAmountForInput(b, "IDR") else ""
                                }
                            }
                        )

                        // Input nominal budget bulanan
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 44.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(MizanTheme.colors.surface)
                                .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(6.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Rp ",
                                    style = TextStyle(
                                        fontFamily = FontFamily.Default,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = MizanTheme.colors.textSecondary
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                BasicTextField(
                                    value = formAmountText,
                                    onValueChange = { input ->
                                        val digits = input.filter { it.isDigit() }
                                        formAmountText = if (digits.isNotEmpty()) {
                                            Uang.formatNumber(digits.toLongOrNull() ?: 0L)
                                        } else {
                                            ""
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    textStyle = TextStyle(
                                        fontFamily = FontFamily.Default,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = MizanTheme.colors.text
                                    ),
                                    cursorBrush = SolidColor(MizanTheme.colors.accent),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    decorationBox = { innerTextField ->
                                        if (formAmountText.isEmpty()) {
                                            Text(
                                                text = stringResource(id = R.string.placeholder_nominal_budget),
                                                style = TextStyle(
                                                    fontFamily = FontFamily.Default,
                                                    fontSize = 14.sp,
                                                    color = MizanTheme.colors.textSecondary
                                                )
                                            )
                                        }
                                        innerTextField()
                                    }
                                )
                            }
                        }

                        // Tombol Simpan Budget
                        Button(
                            onClick = {
                                if (selectedDropdownId == null) {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.warn_select_budget_category),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }
                                val amount = Uang.parseAmountInput(formAmountText, "IDR")
                                if (amount <= 0L) {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.warn_budget_amount_invalid),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }

                                val optId = selectedDropdownId!!
                                if (optId.startsWith("cat:")) {
                                    val cat = optId.removePrefix("cat:")
                                    val totalSubs = budgets.subcategories[cat]?.values?.filter { it > 0 }?.sum() ?: 0L
                                    if (amount < totalSubs) {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.warn_budget_sub_exceeds_parent),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@Button
                                    }

                                    coroutineScope.launch {
                                        val now = System.currentTimeMillis()
                                        val newItems = budgets.items.toMutableMap()
                                        newItems[cat] = amount
                                        repository.saveBudgets(budgets.copy(items = newItems, updatedAt = now))
                                        formAmountText = ""
                                        selectedDropdownId = null
                                    }
                                } else if (optId.startsWith("sub:")) {
                                    val parts = optId.removePrefix("sub:").split(":")
                                    val cat = parts[0]
                                    val sub = parts.getOrNull(1) ?: ""

                                    val parentBudget = budgets.items[cat] ?: 0L
                                    if (parentBudget <= 0L) {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.warn_budget_parent_required),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@Button
                                    }

                                    val otherSubs = budgets.subcategories[cat]
                                        ?.filter { it.key != sub && it.value > 0L }
                                        ?.values
                                        ?.sum() ?: 0L

                                    if (otherSubs + amount > parentBudget) {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.warn_budget_sub_exceeds_parent),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@Button
                                    }

                                    coroutineScope.launch {
                                        val now = System.currentTimeMillis()
                                        val parentSubBudgets = budgets.subcategories[cat]?.toMutableMap() ?: mutableMapOf()
                                        parentSubBudgets[sub] = amount
                                        val newSubBudgets = budgets.subcategories.toMutableMap()
                                        newSubBudgets[cat] = parentSubBudgets
                                        repository.saveBudgets(budgets.copy(subcategories = newSubBudgets, updatedAt = now))
                                        formAmountText = ""
                                        selectedDropdownId = null
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 44.dp),
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MizanTheme.colors.accent,
                                contentColor = MizanTheme.colors.surface
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.action_simpan_budget),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
