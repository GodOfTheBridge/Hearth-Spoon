package com.gotb.heartandspoon.data.settings

import com.gotb.heartandspoon.core.model.ThemeMode
import com.gotb.heartandspoon.domain.api.ThemeSettingsRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

@Singleton
class ThemeSettingsRepositoryImpl @Inject constructor(
    private val themePreferencesDataSource: ThemePreferencesDataSource,
) : ThemeSettingsRepository {
    private val previewThemeModeState = MutableStateFlow<ThemeMode?>(null)

    override val savedThemeMode: Flow<ThemeMode> = themePreferencesDataSource.themeMode

    override val themeMode: Flow<ThemeMode> =
        combine(savedThemeMode, previewThemeModeState) { savedThemeMode, previewThemeMode ->
            previewThemeMode ?: savedThemeMode
        }.distinctUntilChanged()

    override suspend fun previewThemeMode(themeMode: ThemeMode?) {
        previewThemeModeState.value = themeMode
    }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        previewThemeModeState.value = themeMode
        try {
            themePreferencesDataSource.setThemeMode(themeMode)
        } finally {
            previewThemeModeState.value = null
        }
    }
}
