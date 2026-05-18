package com.example.wishlot.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "wishlot_settings")

class SettingsRepository(private val context: Context) {

    private val lastBudgetKey = longPreferencesKey("last_treat_budget_minor")
    private val wheelAnimationKey = booleanPreferencesKey("wheel_animation_enabled")

    val lastTreatBudgetMinor: Flow<Long?> = context.settingsDataStore.data.map { prefs ->
        prefs[lastBudgetKey]
    }

    val wheelAnimationEnabled: Flow<Boolean> = context.settingsDataStore.data.map { prefs ->
        prefs[wheelAnimationKey] ?: true
    }

    suspend fun setLastTreatBudgetMinor(minor: Long) {
        context.settingsDataStore.edit { prefs ->
            prefs[lastBudgetKey] = minor
        }
    }

    suspend fun setWheelAnimationEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[wheelAnimationKey] = enabled
        }
    }

    companion object {
        @Volatile
        private var instance: SettingsRepository? = null

        fun getInstance(context: Context): SettingsRepository =
            instance ?: synchronized(this) {
                instance ?: SettingsRepository(context.applicationContext).also { instance = it }
            }
    }
}
