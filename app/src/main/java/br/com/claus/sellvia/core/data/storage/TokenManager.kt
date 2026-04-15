package br.com.claus.sellvia.core.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import br.com.claus.sellvia.core.model.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class TokenManager(private val context: Context) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val COMPANY_ID_KEY = stringPreferencesKey("company_id")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
        private val KEEP_LOGGED_IN_KEY = booleanPreferencesKey("keep_logged_in")
    }

    suspend fun saveTokens(token: String, refreshToken: String, companyId: Long, userId: Long, role: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[REFRESH_TOKEN_KEY] = refreshToken
            preferences[COMPANY_ID_KEY] = companyId.toString()
            preferences[USER_ID_KEY] = userId.toString()
            preferences[USER_ROLE_KEY] = role
        }
    }

    suspend fun saveUserRole(role: UserRole) {
        context.dataStore.edit { it[USER_ROLE_KEY] = role.name }
    }

    fun userRole(): Flow<UserRole?> = context.dataStore.data.map {
        it[USER_ROLE_KEY]?.let { name -> UserRole.entries.find { r -> r.name == name } }
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

    suspend fun saveKeepLoggedIn(value: Boolean) {
        context.dataStore.edit { it[KEEP_LOGGED_IN_KEY] = value }
    }

    fun keepLoggedIn(): Flow<Boolean> = context.dataStore.data.map {
        it[KEEP_LOGGED_IN_KEY] ?: true
    }

    suspend fun clearTokens() {
        context.dataStore.edit { it.clear() }
    }
}