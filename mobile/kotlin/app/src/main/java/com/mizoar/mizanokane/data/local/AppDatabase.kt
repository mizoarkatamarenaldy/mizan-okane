package com.mizoar.mizanokane.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mizoar.mizanokane.data.model.BudgetEntity
import com.mizoar.mizanokane.data.model.CategoryEntity
import com.mizoar.mizanokane.data.model.MetaEntity
import com.mizoar.mizanokane.data.model.SettingsEntity
import com.mizoar.mizanokane.data.model.SubcategoryBudgetEntity
import com.mizoar.mizanokane.data.model.SubcategoryEntity
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.model.WalletEntity

/**
 * Basis data Room utama aplikasi Mizan Okane.
 * Versi 1, exportSchema = true.
 */
@Database(
    entities = [
        TransactionEntity::class,
        WalletEntity::class,
        CategoryEntity::class,
        SubcategoryEntity::class,
        BudgetEntity::class,
        SubcategoryBudgetEntity::class,
        SettingsEntity::class,
        MetaEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun walletDao(): WalletDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao
    abstract fun settingsDao(): SettingsDao
    abstract fun metaDao(): MetaDao

    companion object {
        const val DATABASE_NAME = "mizan-okane-db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
