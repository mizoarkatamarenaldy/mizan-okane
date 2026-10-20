package com.mizoar.mizanokane.ui

import androidx.annotation.DrawableRes
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.data.model.CategoriesData
import com.mizoar.mizanokane.data.model.TransactionEntity

data class IconItem(
    val id: String,
    val label: String,
    @DrawableRes val resId: Int
)

data class IconTheme(
    val id: String,
    val label: String,
    val icons: List<IconItem>
)

/**
 * Pemetaan ID ikon kategori ke vector drawable dan aturan ikon bawaan
 * persis seperti versi web (desktop/src/js/app.js).
 */
object CategoryIcons {
    const val NEUTRAL_ICON_ID = "netral"

    val THEMES = listOf(
        IconTheme(
            id = "makanan",
            label = "Makanan",
            icons = listOf(
                IconItem("makan", "Makan", R.drawable.ic_cat_makan),
                IconItem("kopi", "Kopi & Minuman", R.drawable.ic_cat_kopi),
                IconItem("burger", "Cepat Saji", R.drawable.ic_cat_burger),
                IconItem("buah", "Buah & Segar", R.drawable.ic_cat_buah)
            )
        ),
        IconTheme(
            id = "transportasi",
            label = "Transportasi",
            icons = listOf(
                IconItem("mobil", "Mobil", R.drawable.ic_cat_mobil),
                IconItem("motor", "Sepeda Motor", R.drawable.ic_cat_motor),
                IconItem("bus", "Bus", R.drawable.ic_cat_bus),
                IconItem("bensin", "Bahan Bakar", R.drawable.ic_cat_bensin),
                IconItem("kereta", "Kereta", R.drawable.ic_cat_kereta),
                IconItem("pesawat", "Pesawat", R.drawable.ic_cat_pesawat)
            )
        ),
        IconTheme(
            id = "belanja",
            label = "Belanja",
            icons = listOf(
                IconItem("belanja", "Tas Belanja", R.drawable.ic_cat_belanja),
                IconItem("keranjang", "Keranjang", R.drawable.ic_cat_keranjang),
                IconItem("pakaian", "Pakaian", R.drawable.ic_cat_pakaian),
                IconItem("elektronik", "Elektronik & Gadget", R.drawable.ic_cat_elektronik)
            )
        ),
        IconTheme(
            id = "rumah",
            label = "Rumah",
            icons = listOf(
                IconItem("rumah", "Rumah", R.drawable.ic_cat_rumah),
                IconItem("tagihan", "Tagihan", R.drawable.ic_cat_tagihan),
                IconItem("listrik", "Listrik", R.drawable.ic_cat_listrik),
                IconItem("air", "Air", R.drawable.ic_cat_air),
                IconItem("wifi", "Internet & WiFi", R.drawable.ic_cat_wifi),
                IconItem("perabot", "Perkakas & Perabot", R.drawable.ic_cat_perabot)
            )
        ),
        IconTheme(
            id = "kesehatan",
            label = "Kesehatan",
            icons = listOf(
                IconItem("kesehatan", "Kesehatan", R.drawable.ic_cat_kesehatan),
                IconItem("obat", "Obat & Farmasi", R.drawable.ic_cat_obat),
                IconItem("dokter", "Dokter & Medis", R.drawable.ic_cat_dokter),
                IconItem("olahraga", "Olahraga & Kebugaran", R.drawable.ic_cat_olahraga)
            )
        ),
        IconTheme(
            id = "pendidikan",
            label = "Pendidikan",
            icons = listOf(
                IconItem("buku", "Buku", R.drawable.ic_cat_buku),
                IconItem("sekolah", "Pendidikan & Sekolah", R.drawable.ic_cat_sekolah),
                IconItem("kursus", "Kursus & Sertifikat", R.drawable.ic_cat_kursus)
            )
        ),
        IconTheme(
            id = "hiburan",
            label = "Hiburan",
            icons = listOf(
                IconItem("game", "Game & Permainan", R.drawable.ic_cat_game),
                IconItem("film", "Bioskop & Film", R.drawable.ic_cat_film),
                IconItem("musik", "Musik", R.drawable.ic_cat_musik),
                IconItem("liburan", "Liburan & Wisata", R.drawable.ic_cat_liburan)
            )
        ),
        IconTheme(
            id = "keuangan",
            label = "Keuangan",
            icons = listOf(
                IconItem("gaji", "Gaji & Pendapatan", R.drawable.ic_cat_gaji),
                IconItem("bonus", "Bonus & Hadiah", R.drawable.ic_cat_bonus),
                IconItem("investasi", "Investasi", R.drawable.ic_cat_investasi),
                IconItem("tabungan", "Tabungan", R.drawable.ic_cat_tabungan),
                IconItem("kartu", "Kartu Bank", R.drawable.ic_cat_kartu)
            )
        ),
        IconTheme(
            id = "lainnya",
            label = "Lainnya",
            icons = listOf(
                IconItem("netral", "Netral (Bawaan)", R.drawable.ic_cat_netral),
                IconItem("bintang", "Bintang", R.drawable.ic_cat_bintang),
                IconItem("titik_tiga", "Titik Tiga", R.drawable.ic_cat_titik_tiga),
                IconItem("donasi", "Donasi & Amal", R.drawable.ic_cat_donasi),
                IconItem("hewan", "Hewan Peliharaan", R.drawable.ic_cat_hewan)
            )
        )
    )

    private val ICON_MAP: Map<String, Int> = THEMES.flatMap { it.icons }.associate { it.id to it.resId }

    @DrawableRes
    fun getIconRes(iconId: String?): Int {
        val key = iconId?.trim()
        if (!key.isNullOrEmpty()) {
            val found = ICON_MAP[key]
            if (found != null) return found
        }
        return R.drawable.ic_category_default
    }

    @DrawableRes
    fun getCategoryIconRes(
        categoryName: String?,
        type: String = "expense",
        categories: CategoriesData? = null
    ): Int {
        if (categoryName.isNullOrBlank()) return R.drawable.ic_category_default
        val norm = categoryName.trim()
        if (norm.equals("Equilibrium", ignoreCase = true)) {
            return R.drawable.ic_neraca
        }

        if (categories != null) {
            val list = if (type == "income") categories.income else categories.expense
            val item = list.find { it.name.trim().equals(norm, ignoreCase = true) }
                ?: if (type == "income") categories.expense.find { it.name.trim().equals(norm, ignoreCase = true) }
                else categories.income.find { it.name.trim().equals(norm, ignoreCase = true) }

            if (item != null && !item.icon.isNullOrBlank()) {
                return getIconRes(item.icon)
            }
        }

        if (ICON_MAP.containsKey(norm)) {
            return getIconRes(norm)
        }

        return R.drawable.ic_category_default
    }

    @DrawableRes
    fun getSubcategoryIconRes(
        parentCategory: String?,
        subcategoryName: String?,
        type: String = "expense",
        categories: CategoriesData? = null
    ): Int {
        if (parentCategory?.trim()?.equals("Equilibrium", ignoreCase = true) == true) {
            return R.drawable.ic_neraca
        }
        val subNorm = subcategoryName?.trim().orEmpty()
        val catNorm = parentCategory?.trim().orEmpty()

        if (categories != null && subNorm.isNotEmpty() && catNorm.isNotEmpty()) {
            val groups = if (type == "income") listOf(categories.subcategories.income, categories.subcategories.expense)
            else listOf(categories.subcategories.expense, categories.subcategories.income)

            for (group in groups) {
                val subs = group.entries.find { it.key.trim().equals(catNorm, ignoreCase = true) }?.value
                val subItem = subs?.find { it.name.trim().equals(subNorm, ignoreCase = true) }
                if (subItem != null && !subItem.icon.isNullOrBlank()) {
                    return getIconRes(subItem.icon)
                }
            }
        }

        return getCategoryIconRes(parentCategory, type, categories)
    }

    @DrawableRes
    fun getTransactionIconRes(
        tx: TransactionEntity,
        categories: CategoriesData? = null
    ): Int {
        if (tx.type == "transfer") return R.drawable.ic_transfer
        if (tx.type == "penyesuaian" || tx.category?.trim()?.equals("Equilibrium", ignoreCase = true) == true) {
            return R.drawable.ic_neraca
        }
        if (!tx.subcategory.isNullOrBlank()) {
            return getSubcategoryIconRes(tx.category, tx.subcategory, tx.type, categories)
        }
        return getCategoryIconRes(tx.category, tx.type, categories)
    }
}
