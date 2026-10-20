package com.mizoar.mizanokane.data

import com.mizoar.mizanokane.data.model.BudgetsData
import com.mizoar.mizanokane.data.model.CategoriesData
import com.mizoar.mizanokane.data.model.CategoryItem
import com.mizoar.mizanokane.data.model.SubcategoriesGroup
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.model.WalletEntity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale

/**
 * Data contoh lengkap (mode demo) sesuai desktop/src/js/db.js dan dev/SCHEMA.md.
 */
object SampleData {

    val SAMPLE_WALLETS = listOf(
        WalletEntity(
            id = "wallet_sample_kas",
            name = "Kas Tunai",
            currency = "IDR",
            order = 0,
            createdAt = 1700000000000L,
            updatedAt = 1700000000000L,
            deleted = false
        ),
        WalletEntity(
            id = "wallet_sample_bank",
            name = "Rekening Bank",
            currency = "IDR",
            order = 1,
            createdAt = 1700000000000L,
            updatedAt = 1700000000000L,
            deleted = false
        ),
        WalletEntity(
            id = "wallet_sample_jpy",
            name = "Dompet Yen",
            currency = "JPY",
            order = 2,
            createdAt = 1700000000000L,
            updatedAt = 1700000000000L,
            deleted = false
        ),
        WalletEntity(
            id = "wallet_sample_usd",
            name = "Akun USD",
            currency = "USD",
            order = 3,
            createdAt = 1700000000000L,
            updatedAt = 1700000000000L,
            deleted = false
        )
    )

    val SAMPLE_CATEGORIES = CategoriesData(
        income = listOf(
            CategoryItem(name = "Gaji", order = 0, icon = "gaji"),
            CategoryItem(name = "Bonus", order = 1, icon = "bonus"),
            CategoryItem(name = "Investasi", order = 2, icon = "investasi"),
            CategoryItem(name = "Lainnya", order = 3, icon = "titik_tiga")
        ),
        expense = listOf(
            CategoryItem(name = "Makan", order = 0, icon = "makan"),
            CategoryItem(name = "Transport", order = 1, icon = "mobil"),
            CategoryItem(name = "Tagihan", order = 2, icon = "tagihan"),
            CategoryItem(name = "Belanja", order = 3, icon = "belanja"),
            CategoryItem(name = "Hiburan", order = 4, icon = "game"),
            CategoryItem(name = "Lainnya", order = 5, icon = "titik_tiga")
        ),
        subcategories = SubcategoriesGroup(
            income = mapOf(
                "Gaji" to listOf(
                    CategoryItem(name = "Gaji Pokok", order = 0, icon = "gaji"),
                    CategoryItem(name = "Tunjangan", order = 1, icon = "kartu")
                ),
                "Bonus" to listOf(
                    CategoryItem(name = "Bonus Tahunan", order = 0, icon = "bonus"),
                    CategoryItem(name = "Komisi", order = 1, icon = "tabungan")
                ),
                "Investasi" to listOf(
                    CategoryItem(name = "Dividen", order = 0, icon = "investasi"),
                    CategoryItem(name = "Bunga", order = 1, icon = "tabungan")
                )
            ),
            expense = mapOf(
                "Makan" to listOf(
                    CategoryItem(name = "Restoran", order = 0, icon = "makan"),
                    CategoryItem(name = "Belanja Bahan", order = 1, icon = "keranjang"),
                    CategoryItem(name = "Kopi & Camilan", order = 2, icon = "kopi")
                ),
                "Transport" to listOf(
                    CategoryItem(name = "Bensin", order = 0, icon = "bensin"),
                    CategoryItem(name = "Parkir", order = 1, icon = "mobil"),
                    CategoryItem(name = "Transportasi Umum", order = 2, icon = "bus")
                ),
                "Tagihan" to listOf(
                    CategoryItem(name = "Listrik", order = 0, icon = "listrik"),
                    CategoryItem(name = "Air & IPL", order = 1, icon = "air"),
                    CategoryItem(name = "Internet", order = 2, icon = "wifi")
                ),
                "Belanja" to listOf(
                    CategoryItem(name = "Kebutuhan Rumah", order = 0, icon = "rumah"),
                    CategoryItem(name = "Pakaian", order = 1, icon = "pakaian"),
                    CategoryItem(name = "Elektronik", order = 2, icon = "elektronik")
                ),
                "Hiburan" to listOf(
                    CategoryItem(name = "Streaming", order = 0, icon = "film"),
                    CategoryItem(name = "Hobi", order = 1, icon = "game"),
                    CategoryItem(name = "Liburan", order = 2, icon = "liburan")
                )
            )
        ),
        updatedAt = 1700000000000L
    )

    val SAMPLE_BUDGETS = BudgetsData(
        items = mapOf(
            "Makan" to 250000000L,
            "Transport" to 80000000L,
            "Tagihan" to 120000000L,
            "Belanja" to 150000000L,
            "Hiburan" to 60000000L
        ),
        subcategories = mapOf(
            "Makan" to mapOf(
                "Restoran" to 120000000L,
                "Kopi & Camilan" to 30000000L
            )
        ),
        updatedAt = 1700000000000L
    )

    private data class RawSampleTx(
        val id: String,
        val isPrev: Boolean,
        val day: Int,
        val time: String,
        val type: String,
        val amount: Long,
        val category: String? = null,
        val subcategory: String? = null,
        val walletId: String,
        val toWalletId: String? = null,
        val note: String
    )

    fun getSampleTransactions(referenceDate: LocalDate = LocalDate.now()): List<TransactionEntity> {
        val curY = referenceDate.year
        val curM = referenceDate.monthValue

        val prevDate = referenceDate.minusMonths(1)
        val prevY = prevDate.year
        val prevM = prevDate.monthValue

        fun makeDate(isPrev: Boolean, day: Int): String {
            val y = if (isPrev) prevY else curY
            val m = if (isPrev) prevM else curM
            return String.format(Locale.US, "%04d-%02d-%02d", y, m, day)
        }

        fun makeTimestamp(dateStr: String, timeStr: String): Long {
            return try {
                val dt = LocalDateTime.parse("${dateStr}T${timeStr}:00")
                dt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            } catch (e: Exception) {
                System.currentTimeMillis()
            }
        }

        val rawList = listOf(
            RawSampleTx("sample_tx_01", true, 1, "08:30", "income", 1200000000L, "Gaji", "Gaji Pokok", "wallet_sample_bank", null, "Gaji bulan lalu"),
            RawSampleTx("sample_tx_02", true, 5, "12:30", "expense", 180000000L, "Makan", "Restoran", "wallet_sample_bank", null, "Kuliner & makan luar"),
            RawSampleTx("sample_tx_03", true, 10, "08:15", "expense", 60000000L, "Transport", "Bensin", "wallet_sample_bank", null, "Bensin & tol"),
            RawSampleTx("sample_tx_04", true, 15, "14:00", "expense", 105000000L, "Tagihan", "Listrik", "wallet_sample_bank", null, "Listrik PLN & internet"),
            RawSampleTx("sample_tx_05", true, 20, "16:45", "expense", 90000000L, "Belanja", "Pakaian", "wallet_sample_bank", null, "Pakaian dinas"),
            RawSampleTx("sample_tx_06", true, 25, "19:20", "expense", 40000000L, "Hiburan", "Liburan", "wallet_sample_bank", null, "Tiket wahana wisata"),

            RawSampleTx("sample_tx_07", false, 1, "08:00", "income", 10000000L, "Gaji", "Tunjangan", "wallet_sample_jpy", null, "Uang saku perjalanan dinas"),
            RawSampleTx("sample_tx_08", false, 1, "08:00", "income", 150000L, "Gaji", "Gaji Pokok", "wallet_sample_usd", null, "Saldo awal akun USD"),

            RawSampleTx("sample_tx_09", false, 1, "08:30", "income", 1200000000L, "Gaji", "Gaji Pokok", "wallet_sample_bank", null, "Gaji bulanan"),
            RawSampleTx("sample_tx_10", false, 8, "10:00", "income", 25000L, "Investasi", "Dividen", "wallet_sample_usd", null, "Dividen saham luar negeri"),
            RawSampleTx("sample_tx_11", false, 10, "11:15", "income", 250000000L, "Bonus", "Komisi", "wallet_sample_bank", null, "Bonus pencapaian proyek"),

            RawSampleTx("sample_tx_12", false, 2, "09:00", "transfer", 150000000L, null, null, "wallet_sample_bank", "wallet_sample_kas", "Tarik tunai ATM"),

            RawSampleTx("sample_tx_13", false, 3, "10:00", "expense", 45000000L, "Makan", "Belanja Bahan", "wallet_sample_kas", null, "Belanja mingguan pasar"),
            RawSampleTx("sample_tx_14", false, 4, "07:45", "expense", 25000000L, "Transport", "Bensin", "wallet_sample_kas", null, "Isi bensin mobil"),
            RawSampleTx("sample_tx_15", false, 5, "12:30", "expense", 12500000L, "Makan", "Restoran", "wallet_sample_bank", null, "Makan siang bersama tim"),
            RawSampleTx("sample_tx_16", false, 5, "14:20", "expense", 65000000L, "Tagihan", "Listrik", "wallet_sample_bank", null, "Token listrik PLN"),
            RawSampleTx("sample_tx_17", false, 6, "16:00", "expense", 5000000L, "Transport", "Parkir", "wallet_sample_kas", null, "Parkir harian kantor"),
            RawSampleTx("sample_tx_18", false, 7, "15:10", "expense", 4500000L, "Makan", "Kopi & Camilan", "wallet_sample_kas", null, "Kopi sore"),
            RawSampleTx("sample_tx_19", false, 8, "09:00", "expense", 40000000L, "Tagihan", "Internet", "wallet_sample_bank", null, "Langganan Wi-Fi rumah"),
            RawSampleTx("sample_tx_20", false, 9, "13:45", "expense", 35000000L, "Belanja", "Kebutuhan Rumah", "wallet_sample_bank", null, "Perlengkapan mandi dan dapur"),
            RawSampleTx("sample_tx_21", false, 11, "20:00", "expense", 18000000L, "Hiburan", "Streaming", "wallet_sample_bank", null, "Langganan streaming video"),

            RawSampleTx("sample_tx_22", false, 12, "18:30", "expense", 350000L, "Makan", "Restoran", "wallet_sample_jpy", null, "Makan malam ramen"),
            RawSampleTx("sample_tx_23", false, 13, "14:00", "expense", 4990L, "Belanja", "Elektronik", "wallet_sample_usd", null, "Aksesoris komputer"),
            RawSampleTx("sample_tx_24", false, 14, "16:00", "expense", 1200000L, "Belanja", "Pakaian", "wallet_sample_jpy", null, "Oleh-oleh kemeja"),
            RawSampleTx("sample_tx_25", false, 15, "17:30", "expense", 1299L, "Tagihan", "Internet", "wallet_sample_usd", null, "Langganan server cloud")
        )

        return rawList.map { item ->
            val dateStr = makeDate(item.isPrev, item.day)
            val ts = makeTimestamp(dateStr, item.time)
            TransactionEntity(
                id = item.id,
                type = item.type,
                amount = item.amount,
                date = dateStr,
                time = item.time,
                walletId = item.walletId,
                toWalletId = if (item.type == "transfer") item.toWalletId else null,
                category = if (item.type == "transfer") "Transfer" else item.category,
                subcategory = item.subcategory,
                note = item.note,
                createdAt = ts,
                updatedAt = ts,
                deleted = false,
                dummy = true
            )
        }
    }
}
