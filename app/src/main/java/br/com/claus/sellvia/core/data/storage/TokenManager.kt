package br.com.claus.sellvia.core.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class TokenManager(private val context: Context) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val COMPANY_ID_KEY = stringPreferencesKey("company_id")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
    }

    suspend fun saveTokens(token: String, refreshToken: String, companyId: Long, userId: Long) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[REFRESH_TOKEN_KEY] = refreshToken
            preferences[COMPANY_ID_KEY] = companyId.toString()
            preferences[USER_ID_KEY] = userId.toString()
        }
    }

    val accessToken: Flow<String?> = context.dataStore.data.map {
        it[TOKEN_KEY]
    }

    val refreshToken: Flow<String?> = context.dataStore.data.map {
        it[REFRESH_TOKEN_KEY]
    }

    fun companyId(): Flow<Long?> = context.dataStore.data.map {
        it[COMPANY_ID_KEY]?.toLongOrNull()
    }

    fun userId(): Flow<Long?> = context.dataStore.data.map {
        it[USER_ID_KEY]?.toLongOrNull()
    }

    suspend fun clearTokens() {
        context.dataStore.edit { it.clear() }
    }
}