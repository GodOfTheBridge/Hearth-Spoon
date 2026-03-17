package com.gotb.heartandspoon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gotb.heartandspoon.core.model.AppLanguage
import com.gotb.heartandspoon.core.model.ThemeFamily
import com.gotb.heartandspoon.core.model.ThemeMode
import com.gotb.heartandspoon.navigation.HearthSpoonNavKey
import com.gotb.heartandspoon.navigation.Home
import com.gotb.heartandspoon.domain.api.LanguageSettingsRepository
import com.gotb.heartandspoon.domain.api.ThemeSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HearthSpoonRootViewModel @Inject constructor(
    themeSettingsRepository: ThemeSettingsRepository,
    languageSettingsRepository: LanguageSettingsRepository,
) : ViewModel() {
    private val previewThemeModeState = MutableStateFlow<ThemeMode?>(null)
    private val previewThemeFamilyState = MutableStateFlow<ThemeFamily?>(null)
    private val previewAppLanguageState = MutableStateFlow<AppLanguage?>(null)
    private val navBackStackState = MutableStateFlow<List<HearthSpoonNavKey>>(listOf(Home))
    private val previewSettingsState =
        combine(previewThemeModeState, previewThemeFamilyState, previewAppLanguageState) {
                previewThemeMode,
                previewThemeFamily,
                previewAppLanguage,
            ->
            PreviewSettingsState(
                previewThemeMode = previewThemeMode,
                previewThemeFamily = previewThemeFamily,
                previewAppLanguage = previewAppLanguage,
            )
        }

    val uiState: StateFlow<HearthSpoonRootUiState> =
        combine(
            themeSettingsRepository.themeMode,
            themeSettingsRepository.themeFamily,
            languageSettingsRepository.appLanguage,
            navBackStackState,
            previewSettingsState,
        ) { savedThemeMode, savedThemeFamily, savedAppLanguage, navBackStack, previewSettings ->
            HearthSpoonRootUiState(
                savedThemeMode = savedThemeMode,
                savedThemeFamily = savedThemeFamily,
                savedAppLanguage = savedAppLanguage,
                navBackStack = navBackStack,
                previewThemeMode = previewSettings.previewThemeMode,
                previewThemeFamily = previewSettings.previewThemeFamily,
                previewAppLanguage = previewSettings.previewAppLanguage,
                isReady = true,
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = HearthSpoonRootUiState(),
            )

    fun previewThemeMode(themeMode: ThemeMode?) {
        previewThemeModeState.value = themeMode
    }

    fun previewThemeFamily(themeFamily: ThemeFamily?) {
        previewThemeFamilyState.value = themeFamily
    }

    fun previewAppLanguage(appLanguage: AppLanguage?) {
        previewAppLanguageState.value = appLanguage
    }

    fun setNavBackStack(navBackStack: List<HearthSpoonNavKey>) {
        val normalizedBackStack = navBackStack.ifEmpty { listOf(Home) }
        if (navBackStackState.value != normalizedBackStack) {
            navBackStackState.value = normalizedBackStack
        }
    }
}

private data class PreviewSettingsState(
    val previewThemeMode: ThemeMode? = null,
    val previewThemeFamily: ThemeFamily? = null,
    val previewAppLanguage: AppLanguage? = null,
)

data class HearthSpoonRootUiState(
    val savedThemeMode: ThemeMode? = null,
    val savedThemeFamily: ThemeFamily? = null,
    val savedAppLanguage: AppLanguage? = null,
    val navBackStack: List<HearthSpoonNavKey> = listOf(Home),
    val previewThemeMode: ThemeMode? = null,
    val previewThemeFamily: ThemeFamily? = null,
    val previewAppLanguage: AppLanguage? = null,
    val isReady: Boolean = false,
) {
    val activeThemeMode: ThemeMode
        get() = previewThemeMode ?: savedThemeMode ?: ThemeMode.System

    val activeThemeFamily: ThemeFamily
        get() = previewThemeFamily ?: savedThemeFamily ?: ThemeFamily.Khokhloma

    val activeAppLanguage: AppLanguage
        get() = previewAppLanguage ?: savedAppLanguage ?: AppLanguage.System
}
