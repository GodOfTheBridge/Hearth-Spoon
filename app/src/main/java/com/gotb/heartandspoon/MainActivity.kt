package com.gotb.heartandspoon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.gotb.heartandspoon.core.model.ThemeMode
import com.gotb.heartandspoon.core.designsystem.HearthSpoonTheme
import com.gotb.heartandspoon.domain.api.ThemeSettingsRepository
import com.gotb.heartandspoon.navigation.HearthSpoonAppNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var themeSettingsRepository: ThemeSettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeMode by themeSettingsRepository.themeMode.collectAsState(initial = ThemeMode.Light)

            HearthSpoonTheme(themeMode = themeMode) {
                HearthSpoonAppNavigation()
            }
        }
    }
}
