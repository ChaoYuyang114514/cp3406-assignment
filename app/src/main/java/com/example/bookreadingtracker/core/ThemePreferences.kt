package com.example.bookreadingtracker.core

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "settings")

object ThemePreferences {
    private val KEY_DARK = booleanPreferencesKey("is_dark")

    fun isDarkFlow(context: Context): Flow<Boolean> =
        context.settingsDataStore.data.map { it[KEY_DARK] ?: false }

    suspend fun setDark(context: Context, value: Boolean) {
        context.settingsDataStore.edit { it[KEY_DARK] = value }
    }
}