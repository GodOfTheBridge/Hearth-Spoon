package com.gotb.heartandspoon.domain.api

import com.gotb.heartandspoon.core.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface ThemeSettingsRepository {
    val themeMode: Flow<ThemeMode>

    suspend fun setThemeMode(themeMode: ThemeMode)
}
