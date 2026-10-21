package com.mizoar.mizanokane.data.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "mizan_auth_prefs")

/**
 * Penyimpanan status akun terpisah menggunakan DataStore Preferences.
 * Sesuai spek Kotlin 8A:
 * Status akun (email) disimpan terpisah dari Room, dan tidak ikut ekspor, impor, atau sinkron.
 */
class AuthPreferences(private val context: Context) {
    companion object {
        private val KEY_ACCOUNT_EMAIL = stringPreferencesKey("google_account_email")
    }

    val accountEmailFlow: Flow<String?> = context.authDataStore.data.map { preferences ->
        preferences[KEY_ACCOUNT_EMAIL]
    }

    suspend fun setAccountEmail(email: String) {
        context.authDataStore.edit { preferences ->
            preferences[KEY_ACCOUNT_EMAIL] = email
        }
    }

    suspend fun clearAccount() {
        context.authDataStore.edit { preferences ->
            preferences.remove(KEY_ACCOUNT_EMAIL)
        }
    }
}
