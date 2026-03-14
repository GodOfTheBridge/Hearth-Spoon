package com.gotb.heartandspoon.domain.api

import com.gotb.heartandspoon.core.model.ThemeFamily
import com.gotb.heartandspoon.core.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface ThemeSettingsRepository {
    val themeMode: Flow<ThemeMode>
    val themeFamily: Flow<ThemeFamily>

    suspend fun setThemeMode(themeMode: ThemeMode)

    suspend fun setThemeFamily(themeFamily: ThemeFamily)
}
