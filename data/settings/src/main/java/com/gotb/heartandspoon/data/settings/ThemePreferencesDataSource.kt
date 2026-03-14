package com.gotb.heartandspoon.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.gotb.heartandspoon.core.model.ThemeFamily
import com.gotb.heartandspoon.core.model.ThemeMode
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

@Singleton
class ThemePreferencesDataSource @Inject constructor(
    private val themeSettingsPreferencesDataStore: DataStore<Preferences>,
) {
    private val preferencesFlow: Flow<Preferences> =
        themeSettingsPreferencesDataStore.data
            .catch { throwable ->
                if (throwable is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw throwable
                }
            }

    val themeMode: Flow<ThemeMode> =
        preferencesFlow
            .map { preferences ->
                ThemeMode.fromStorageValue(preferences[themeModePreferenceKey])
            }

    val themeFamily: Flow<ThemeFamily> =
        preferencesFlow
            .map { preferences ->
                ThemeFamily.fromStorageValue(preferences[themeFamilyPreferenceKey])
            }

    suspend fun setThemeMode(themeMode: ThemeMode) {
        themeSettingsPreferencesDataStore.edit { preferences ->
            preferences[themeModePreferenceKey] = themeMode.storageValue
        }
    }

    suspend fun setThemeFamily(themeFamily: ThemeFamily) {
        themeSettingsPreferencesDataStore.edit { preferences ->
            preferences[themeFamilyPreferenceKey] = themeFamily.storageValue
        }
    }

    private companion object {
        val themeModePreferenceKey = stringPreferencesKey(name = "theme_mode")
        val themeFamilyPreferenceKey = stringPreferencesKey(name = "theme_family")
    }
}
