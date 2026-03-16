package com.gotb.heartandspoon.data.home

import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import com.gotb.heartandspoon.core.database.HomeDao
import com.gotb.heartandspoon.core.database.HomeEntity
import com.gotb.heartandspoon.core.model.AppLanguage
import com.gotb.heartandspoon.core.model.HomeItem
import com.gotb.heartandspoon.domain.api.HomeRepository
import com.gotb.heartandspoon.domain.api.LanguageSettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class HomeRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val homeDao: HomeDao,
    private val languageSettingsRepository: LanguageSettingsRepository,
    @param:ApplicationContext private val context: Context,
) : HomeRepository {
    override suspend fun getHomeItems(): List<HomeItem> {
        httpClient.toString() // placeholder network touchpoint

        val appLanguage = languageSettingsRepository.appLanguage.first()
        val seeded = seededItems(appLanguage = appLanguage)
        val local = homeDao.getAll()

        if (local.isEmpty()) {
            homeDao.insertAll(seeded)
            return seeded.map { it.toModel() }
        }

        if (local.isSeededContent() && local != seeded) {
            homeDao.insertAll(seeded)
            return seeded.map { it.toModel() }
        }

        return local.map { it.toModel() }
    }

    private fun seededItems(appLanguage: AppLanguage): List<HomeEntity> {
        val localizedContext = context.localizedFor(appLanguage = appLanguage)
        return listOf(
            HomeEntity(1, localizedContext.getString(R.string.home_seed_welcome)),
            HomeEntity(2, localizedContext.getString(R.string.home_seed_stack)),
        )
    }
}

private fun List<HomeEntity>.isSeededContent(): Boolean {
    return map(HomeEntity::id).toSet() == setOf(1L, 2L)
}

private fun Context.localizedFor(appLanguage: AppLanguage): Context {
    val languageTag = appLanguage.languageTag ?: return this
    val configuration = Configuration(resources.configuration)
    val locale = Locale.forLanguageTag(languageTag)
    configuration.setLocales(LocaleList(locale))
    return createConfigurationContext(configuration)
}

private fun HomeEntity.toModel() = HomeItem(id = id, title = title)
