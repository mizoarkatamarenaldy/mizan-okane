package com.mizoar.mizanokane.data

import com.mizoar.mizanokane.data.json.JsonBackupManager
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.repository.InMemoryMizanRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit test JVM untuk ekspor dan impor JSON [JsonBackupManager].
 */
class JsonBackupManagerTest {

    private fun readSampleJson(): String {
        val stream = javaClass.classLoader?.getResourceAsStream("mizan-okane-backup-sample.json")
            ?: error("Resource mizan-okane-backup-sample.json tidak ditemukan")
        return stream.bufferedReader().use { it.readText() }
    }

    @Test
    fun testJsonImportExportRoundtrip() = runTest {
        val sampleJson = readSampleJson()
        val repo = InMemoryMizanRepository()

        // 1. Impor data dari file contoh
        val importResult = JsonBackupManager.importFromJson(sampleJson, repo)
        assertTrue("Impor data contoh harus sukses", importResult.success)
        assertEquals(9, importResult.transactionsAdded)
        assertTrue(importResult.walletsChanged)
        assertTrue(importResult.categoriesChanged)
        assertTrue(importResult.budgetsChanged)
        assertTrue(importResult.settingsChanged)

        // Verifikasi isi yang terimpor di repository
        val txList = repo.getAllTransactions()
        assertEquals(9, txList.size)

        // Verifikasi nominal transaksi uang pecahan USD
        val tx49 = txList.firstOrNull { it.id == "tx_sample_05" }
        assertNotNull(tx49)
        assertEquals(4990L, tx49?.amount) // 49.9 x 100

        val tx12 = txList.firstOrNull { it.id == "tx_sample_06" }
        assertNotNull(tx12)
        assertEquals(1299L, tx12?.amount) // 12.99 x 100

        val tx100 = txList.firstOrNull { it.id == "tx_sample_07" }
        assertNotNull(tx100)
        assertEquals(10000L, tx100?.amount) // 100 x 100

        val tx001 = txList.firstOrNull { it.id == "tx_sample_08" }
        assertNotNull(tx001)
        assertEquals(1L, tx001?.amount) // 0.01 x 100

        // Verifikasi transaksi soft-deleted ikut tersimpan
        val txDel = txList.firstOrNull { it.id == "tx_sample_09" }
        assertNotNull(txDel)
        assertTrue("Transaksi terhapus harus memiliki deleted=true", txDel?.deleted == true)

        // Verifikasi kategori & sub kategori
        val cats = repo.getCategories()
        assertEquals(2, cats.income.size)
        assertEquals(4, cats.expense.size)
        assertEquals(1, cats.subcategories.income["Gaji"]?.size)
        assertEquals(1, cats.subcategories.expense["Makan"]?.size)

        // Verifikasi dompet
        val wallets = repo.getAllWallets()
        assertEquals(4, wallets.size)
        assertTrue(wallets.any { it.deleted })

        // 2. Ekspor kembali ke teks JSON
        val exportedJson = JsonBackupManager.exportToJson(repo, exportedAt = "2026-10-20T10:00:00.000Z")
        assertNotNull(exportedJson)

        // Verifikasi format angka uang di teks JSON ekspor
        // Bilangan bulat tanpa .0, pecahan tanpa trailing zero
        assertTrue("Harus memuat '\"amount\": 49.9'", exportedJson.contains("\"amount\": 49.9"))
        assertTrue("Harus memuat '\"amount\": 12.99'", exportedJson.contains("\"amount\": 12.99"))
        assertTrue("Harus memuat '\"amount\": 100'", exportedJson.contains("\"amount\": 100"))
        assertTrue("Harus memuat '\"amount\": 0.01'", exportedJson.contains("\"amount\": 0.01"))

        // 3. Impor kembali hasil ekspor ke repository kedua (round-trip)
        val secondRepo = InMemoryMizanRepository()
        val secondImportResult = JsonBackupManager.importFromJson(exportedJson, secondRepo)
        assertTrue("Impor ulang hasil ekspor harus sukses", secondImportResult.success)

        val secondTxList = secondRepo.getAllTransactions()
        assertEquals(txList.size, secondTxList.size)
        for (i in txList.indices) {
            assertEquals(txList[i].id, secondTxList[i].id)
            assertEquals(txList[i].amount, secondTxList[i].amount)
            assertEquals(txList[i].deleted, secondTxList[i].deleted)
            assertEquals(txList[i].type, secondTxList[i].type)
            assertEquals(txList[i].date, secondTxList[i].date)
        }

        val secondWallets = secondRepo.getAllWallets()
        assertEquals(wallets.size, secondWallets.size)

        val secondBudgets = secondRepo.getBudgets()
        val firstBudgets = repo.getBudgets()
        assertEquals(firstBudgets.items, secondBudgets.items)
        assertEquals(firstBudgets.subcategories, secondBudgets.subcategories)
    }

    @Test
    fun testTolakAngkaTigaDesimalDiJson() = runTest {
        val invalidJson = """
        {
          "app": "mizan-okane",
          "version": 2,
          "schemaVersion": 2,
          "exportedAt": "2026-10-20T10:00:00.000Z",
          "transactions": [
            {
              "id": "tx_invalid_3desimal",
              "type": "expense",
              "amount": 12.345,
              "date": "2026-10-01"
            }
          ]
        }
        """.trimIndent()

        val repo = InMemoryMizanRepository()
        assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                JsonBackupManager.importFromJson(invalidJson, repo)
            }
        }
    }

    @Test
    fun testAturanPenggabunganLastWriteWins() = runTest {
        val repo = InMemoryMizanRepository()

        // Data awal lokal
        val initialTx = TransactionEntity(
            id = "tx_conflict_1",
            type = "expense",
            amount = 1000000L, // 10.000
            date = "2026-10-01",
            createdAt = 1000L,
            updatedAt = 2000L,
            note = "Lokal lebih baru"
        )
        repo.saveTransaction(initialTx)

        // Impor data dengan updatedAt lebih lama (harus diabaikan)
        val olderJson = """
        {
          "app": "mizan-okane",
          "version": 2,
          "schemaVersion": 2,
          "exportedAt": "2026-10-20T10:00:00.000Z",
          "transactions": [
            {
              "id": "tx_conflict_1",
              "type": "expense",
              "amount": 500000L,
              "date": "2026-10-01",
              "createdAt": 1000,
              "updatedAt": 1500,
              "note": "Data lama dari luar"
            }
          ]
        }
        """.trimIndent()

        val res1 = JsonBackupManager.importFromJson(olderJson, repo)
        assertTrue(res1.success)
        assertEquals(0, res1.transactionsUpdated)
        assertEquals("Lokal lebih baru", repo.getTransactionById("tx_conflict_1")?.note)

        // Impor data dengan updatedAt lebih baru (harus menimpa lokal)
        val newerJson = """
        {
          "app": "mizan-okane",
          "version": 2,
          "schemaVersion": 2,
          "exportedAt": "2026-10-20T10:00:00.000Z",
          "transactions": [
            {
              "id": "tx_conflict_1",
              "type": "expense",
              "amount": 750000L,
              "date": "2026-10-01",
              "createdAt": 1000,
              "updatedAt": 3000,
              "note": "Data baru dari luar"
            }
          ]
        }
        """.trimIndent()

        val res2 = JsonBackupManager.importFromJson(newerJson, repo)
        assertTrue(res2.success)
        assertEquals(1, res2.transactionsUpdated)
        assertEquals("Data baru dari luar", repo.getTransactionById("tx_conflict_1")?.note)
    }

    @Test
    fun testResetToDefaults() = runTest {
        val repo = InMemoryMizanRepository()
        val sampleJson = readSampleJson()
        JsonBackupManager.importFromJson(sampleJson, repo)

        assertTrue(repo.getAllTransactions().isNotEmpty())

        repo.activateSampleData()
        assertTrue(repo.isDemoMode())

        repo.resetToDefaults()

        assertEquals(false, repo.isDemoMode())
        assertEquals(null, repo.getMeta("demo_mode"))
        assertEquals(null, repo.getMeta("demo_transactions"))
        assertEquals(null, repo.getMeta("demo_wallets"))
        assertEquals(null, repo.getMeta("demo_categories"))
        assertEquals(null, repo.getMeta("demo_budgets"))
        assertEquals(0, repo.getAllTransactions().size)
        assertEquals(1, repo.getAllWallets().size)
        assertEquals("default", repo.getAllWallets().first().id)
        assertEquals(DefaultData.DEFAULT_CATEGORIES.income.size, repo.getCategories().income.size)
        assertEquals(DefaultData.DEFAULT_CATEGORIES.expense.size, repo.getCategories().expense.size)
        assertEquals(0, repo.getBudgets().items.size)
        assertEquals(0, repo.getBudgets().subcategories.size)
        assertEquals("system", repo.getSettings().theme)
        assertEquals("24", repo.getSettings().timeFormat)
    }
}
