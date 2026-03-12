package com.gotb.heartandspoon.data.settings

import com.gotb.heartandspoon.core.model.ThemeMode
import com.gotb.heartandspoon.domain.api.ThemeSettingsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ThemeSettingsRepositoryImpl @Inject constructor(
    private val themePreferencesDataSource: ThemePreferencesDataSource,
) : ThemeSettingsRepository {
    override val themeMode: Flow<ThemeMode> = themePreferencesDataSource.themeMode

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        themePreferencesDataSource.setThemeMode(themeMode)
    }
}
