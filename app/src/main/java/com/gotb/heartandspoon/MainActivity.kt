package com.gotb.heartandspoon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.gotb.heartandspoon.core.designsystem.HearthSpoonTheme
import com.gotb.heartandspoon.core.model.isEffectivelyDark
import com.gotb.heartandspoon.domain.api.ThemeSettingsRepository
import com.gotb.heartandspoon.navigation.HearthSpoonAppNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var themeSettingsRepository: ThemeSettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val initialThemeMode = themeSettingsRepository.savedThemeMode.first()

            setContent {
                val themeMode by themeSettingsRepository.themeMode.collectAsState(initial = initialThemeMode)
                val systemIsDarkTheme = isSystemInDarkTheme()
                val effectiveIsDarkTheme = themeMode.isEffectivelyDark(systemIsDarkTheme = systemIsDarkTheme)

                HearthSpoonTheme(isDarkTheme = effectiveIsDarkTheme) {
                    HearthSpoonAppNavigation()
                }
            }
        }
    }
}
