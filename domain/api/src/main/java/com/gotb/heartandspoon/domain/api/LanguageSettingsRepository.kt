package com.gotb.heartandspoon.domain.api

import com.gotb.heartandspoon.core.model.AppLanguage
import kotlinx.coroutines.flow.Flow

interface LanguageSettingsRepository {
    val appLanguage: Flow<AppLanguage>

    suspend fun setAppLanguage(appLanguage: AppLanguage)
}
