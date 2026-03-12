package com.gotb.heartandspoon.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
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
    val themeMode: Flow<ThemeMode> =
        themeSettingsPreferencesDataStore.data
            .catch { throwable ->
                if (throwable is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw throwable
                }
            }
            .map { preferences ->
                ThemeMode.fromStorageValue(preferences[themeModePreferenceKey])
            }

    suspend fun setThemeMode(themeMode: ThemeMode) {
        themeSettingsPreferencesDataStore.edit { preferences ->
            preferences[themeModePreferenceKey] = themeMode.storageValue
        }
    }

    private companion object {
        val themeModePreferenceKey = stringPreferencesKey(name = "theme_mode")
    }
}
