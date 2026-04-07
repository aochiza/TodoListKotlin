package com.example.todolist.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {

    companion object {
        val COMPLETED_COLOR_ENABLED = booleanPreferencesKey("completed_color_enabled")
    }

    val isCompletedColorEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[COMPLETED_COLOR_ENABLED] ?: false
        }

    suspend fun setCompletedColorEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[COMPLETED_COLOR_ENABLED] = enabled
        }
    }
}