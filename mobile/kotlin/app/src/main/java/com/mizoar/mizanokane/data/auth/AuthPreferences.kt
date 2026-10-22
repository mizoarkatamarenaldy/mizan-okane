package com.mizoar.mizanokane.data.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "mizan_auth_prefs")

/**
 * Penyimpanan status akun dan waktu sinkron terpisah menggunakan DataStore Preferences.
 * Sesuai spek Kotlin 8A dan 8B:
 * Status akun (email) dan waktu sinkron terakhir disimpan terpisah dari Room,
 * dan tidak ikut ekspor, impor, atau sinkron.
 */
class AuthPreferences(private val context: Context) {
    companion object {
        private val KEY_ACCOUNT_EMAIL = stringPreferencesKey("google_account_email")
        private val KEY_LAST_SYNC_AT = longPreferencesKey("last_sync_at")
    }

    val accountEmailFlow: Flow<String?> = context.authDataStore.data.map { preferences ->
        preferences[KEY_ACCOUNT_EMAIL]
    }

    val lastSyncAtFlow: Flow<Long?> = context.authDataStore.data.map { preferences ->
        preferences[KEY_LAST_SYNC_AT]
    }

    suspend fun getAccountEmail(): String? = accountEmailFlow.firstOrNull()

    suspend fun getLastSyncAt(): Long? = lastSyncAtFlow.firstOrNull()

    suspend fun setAccountEmail(email: String) {
        context.authDataStore.edit { preferences ->
            preferences[KEY_ACCOUNT_EMAIL] = email
        }
    }

    suspend fun setLastSyncAt(timestamp: Long) {
        context.authDataStore.edit { preferences ->
            preferences[KEY_LAST_SYNC_AT] = timestamp
        }
    }

    suspend fun clearAccount() {
        context.authDataStore.edit { preferences ->
            preferences.remove(KEY_ACCOUNT_EMAIL)
            preferences.remove(KEY_LAST_SYNC_AT)
        }
    }
}
