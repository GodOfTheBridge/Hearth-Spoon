package com.gotb.heartandspoon.data.settings

import com.gotb.heartandspoon.core.model.AppLanguage
import com.gotb.heartandspoon.domain.api.LanguageSettingsRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class LanguageSettingsRepositoryImpl @Inject constructor(
    private val themePreferencesDataSource: ThemePreferencesDataSource,
) : LanguageSettingsRepository {
    override val appLanguage: Flow<AppLanguage> = themePreferencesDataSource.appLanguage

    override suspend fun setAppLanguage(appLanguage: AppLanguage) {
        themePreferencesDataSource.setAppLanguage(appLanguage)
    }
}
