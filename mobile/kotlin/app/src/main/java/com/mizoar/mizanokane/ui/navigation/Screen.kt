package com.mizoar.mizanokane.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mizoar.mizanokane.R

sealed class Screen(
    val route: String,
    @StringRes val titleRes: Int,
    @DrawableRes val iconRes: Int
) {
    data object Beranda : Screen(
        route = "beranda",
        titleRes = R.string.nav_beranda,
        iconRes = R.drawable.ic_nav_beranda
    )

    data object Riwayat : Screen(
        route = "riwayat",
        titleRes = R.string.nav_riwayat,
        iconRes = R.drawable.ic_nav_riwayat
    )

    data object Timbangan : Screen(
        route = "timbangan",
        titleRes = R.string.nav_timbangan,
        iconRes = R.drawable.ic_neraca
    )

    data object Statistik : Screen(
        route = "statistik",
        titleRes = R.string.nav_statistik,
        iconRes = R.drawable.ic_nav_statistik
    )

    data object Pengaturan : Screen(
        route = "pengaturan",
        titleRes = R.string.nav_pengaturan,
        iconRes = R.drawable.ic_nav_pengaturan
    )

    data object TransactionForm : Screen(
        route = "transaction_form",
        titleRes = R.string.title_tambah_transaksi,
        iconRes = R.drawable.ic_plus
    )

    data object KelolaDompet : Screen(
        route = "kelola_dompet",
        titleRes = R.string.title_dompet,
        iconRes = R.drawable.ic_wallet
    )

    data object KelolaKategori : Screen(
        route = "kelola_kategori",
        titleRes = R.string.title_kategori,
        iconRes = R.drawable.ic_category_default
    )

    data object KelolaBudget : Screen(
        route = "kelola_budget",
        titleRes = R.string.title_kelola_budget,
        iconRes = R.drawable.ic_budget
    )

    companion object {
        val items = listOf(Beranda, Riwayat, Timbangan, Statistik, Pengaturan)
    }
}
