package com.gotb.heartandspoon.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.gotb.heartandspoon.domain.api.ThemeSettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ThemeSettingsDataStoreModule {

    @Provides
    @Singleton
    fun provideThemeSettingsPreferencesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(name = "theme_settings.preferences_pb")
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ThemeSettingsDataModule {

    @Binds
    @Singleton
    abstract fun bindThemeSettingsRepository(
        themeSettingsRepositoryImpl: ThemeSettingsRepositoryImpl,
    ): ThemeSettingsRepository
}
