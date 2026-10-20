package com.mizoar.mizanokane.data

import com.mizoar.mizanokane.data.model.CategoriesData
import com.mizoar.mizanokane.data.model.CategoryItem
import com.mizoar.mizanokane.data.model.SettingsEntity
import com.mizoar.mizanokane.data.model.SubcategoriesGroup
import com.mizoar.mizanokane.data.model.WalletEntity

/**
 * Data bawaan sistem sesuai desktop/src/js/db.js dan dev/SCHEMA.md.
 */
object DefaultData {

    const val DEFAULT_WALLET_ID = "default"
    const val DEFAULT_WALLET_NAME = "Dompet Utama"

    val DEFAULT_WALLETS = listOf(
        WalletEntity(
            id = DEFAULT_WALLET_ID,
            name = DEFAULT_WALLET_NAME,
            currency = "IDR",
            order = 0,
            createdAt = 0L,
            updatedAt = 0L,
            deleted = false
        )
    )

    val DEFAULT_CATEGORIES = CategoriesData(
        income = listOf(
            CategoryItem(name = "Gaji", order = 0),
            CategoryItem(name = "Bonus", order = 1),
            CategoryItem(name = "Hadiah", order = 2),
            CategoryItem(name = "Lainnya", order = 3)
        ),
        expense = listOf(
            CategoryItem(name = "Makan", order = 0),
            CategoryItem(name = "Transport", order = 1),
            CategoryItem(name = "Hiburan", order = 2),
            CategoryItem(name = "Belanja", order = 3),
            CategoryItem(name = "Tagihan", order = 4),
            CategoryItem(name = "Lainnya", order = 5)
        ),
        subcategories = SubcategoriesGroup(
            income = emptyMap(),
            expense = emptyMap()
        ),
        updatedAt = 0L
    )

    val DEFAULT_SETTINGS = SettingsEntity(
        id = 1,
        theme = "system",
        timeFormat = "24",
        updatedAt = 0L
    )
}
