package com.mizoar.mizanokane.data.model

import kotlinx.serialization.Serializable

/**
 * Representasi satu butir kategori atau sub kategori.
 */
@Serializable
data class CategoryItem(
    val name: String,
    val order: Int,
    val icon: String? = null
)

/**
 * Pemetaan sub kategori per kategori induk untuk pemasukan dan pengeluaran.
 */
@Serializable
data class SubcategoriesGroup(
    val income: Map<String, List<CategoryItem>> = emptyMap(),
    val expense: Map<String, List<CategoryItem>> = emptyMap()
)

/**
 * Struktur utuh objek kategori sesuai dev/SCHEMA.md bagian 3 rincian field categories.
 */
@Serializable
data class CategoriesData(
    val income: List<CategoryItem> = emptyList(),
    val expense: List<CategoryItem> = emptyList(),
    val subcategories: SubcategoriesGroup = SubcategoriesGroup(),
    val updatedAt: Long = 0L
)
