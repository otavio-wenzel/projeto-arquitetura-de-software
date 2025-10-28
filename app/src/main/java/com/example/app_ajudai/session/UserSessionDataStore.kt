package com.example.app_ajudai.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("ajudai_session")

class UserSession(private val context: Context) {
    private val KEY_USER_ID = longPreferencesKey("user_id")

    val userIdFlow: Flow<Long?> = context.dataStore.data.map { prefs ->
        prefs[KEY_USER_ID]
    }

    suspend fun setUserId(id: Long?) {
        context.dataStore.edit { prefs ->
            if (id == null) prefs.remove(KEY_USER_ID) else prefs[KEY_USER_ID] = id
        }
    }
}