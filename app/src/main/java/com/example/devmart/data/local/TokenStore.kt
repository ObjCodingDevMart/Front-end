package com.example.devmart.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.firstOrNull

private val Context.dataStore by preferencesDataStore("auth_prefs")

@Singleton
class TokenStore @Inject constructor(@ApplicationContext private val context: Context) {
    private val KEY = stringPreferencesKey("access_token")
    val tokenFlow = context.dataStore.data.map { it[KEY] }
    suspend fun save(token: String) { context.dataStore.edit { it[KEY] = token } }
    suspend fun clear() { context.dataStore.edit { it.remove(KEY) } }
    // 간단 호출용(Interceptor에서 1회 읽기)
    suspend fun snapshot(): String? = tokenFlow.firstOrNull()

}


