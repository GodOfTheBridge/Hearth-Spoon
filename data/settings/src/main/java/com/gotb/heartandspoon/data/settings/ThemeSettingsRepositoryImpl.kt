package com.gotb.heartandspoon.data.settings

import com.gotb.heartandspoon.core.model.ThemeFamily
import com.gotb.heartandspoon.core.model.ThemeMode
import com.gotb.heartandspoon.domain.api.ThemeSettingsRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class ThemeSettingsRepositoryImpl @Inject constructor(
    private val themePreferencesDataSource: ThemePreferencesDataSource,
) : ThemeSettingsRepository {
    override val themeMode: Flow<ThemeMode> = themePreferencesDataSource.themeMode
    override val themeFamily: Flow<ThemeFamily> = themePreferencesDataSource.themeFamily

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        themePreferencesDataSource.setThemeMode(themeMode)
    }

    override suspend fun setThemeFamily(themeFamily: ThemeFamily) {
        themePreferencesDataSource.setThemeFamily(themeFamily)
    }
}
