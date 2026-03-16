package com.gotb.heartandspoon.feature.profile

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gotb.heartandspoon.core.model.AppLanguage
import com.gotb.heartandspoon.core.model.ThemeFamily
import com.gotb.heartandspoon.core.model.ThemeMode
import com.gotb.heartandspoon.domain.api.LanguageSettingsRepository
import com.gotb.heartandspoon.domain.api.ThemeSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val themeSettingsRepository: ThemeSettingsRepository,
    private val languageSettingsRepository: LanguageSettingsRepository,
) : ViewModel() {
    private val errorMessageResState = MutableStateFlow<Int?>(null)

    val state: StateFlow<ProfileUiState> =
        combine(
            themeSettingsRepository.themeMode,
            themeSettingsRepository.themeFamily,
            languageSettingsRepository.appLanguage,
            errorMessageResState,
        ) { themeMode, themeFamily, appLanguage, errorMessageRes ->
                ProfileUiState(
                    themeMode = themeMode,
                    themeFamily = themeFamily,
                    appLanguage = appLanguage,
                    errorMessageRes = errorMessageRes,
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = ProfileUiState(),
            )

    fun setThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            errorMessageResState.value = null
            runCatching {
                themeSettingsRepository.setThemeMode(themeMode)
            }.onFailure {
                errorMessageResState.value = R.string.profile_error_save_theme
            }
        }
    }

    fun setThemeFamily(themeFamily: ThemeFamily) {
        viewModelScope.launch {
            errorMessageResState.value = null
            runCatching {
                themeSettingsRepository.setThemeFamily(themeFamily)
            }.onFailure {
                errorMessageResState.value = R.string.profile_error_save_theme_family
            }
        }
    }

    fun setAppLanguage(appLanguage: AppLanguage) {
        viewModelScope.launch {
            errorMessageResState.value = null
            runCatching {
                languageSettingsRepository.setAppLanguage(appLanguage)
            }.onFailure {
                errorMessageResState.value = R.string.profile_error_save_language
            }
        }
    }

    fun clearErrorMessage() {
        errorMessageResState.value = null
    }
}

data class ProfileUiState(
    val themeMode: ThemeMode = ThemeMode.System,
    val themeFamily: ThemeFamily = ThemeFamily.Khokhloma,
    val appLanguage: AppLanguage = AppLanguage.System,
    @get:StringRes val errorMessageRes: Int? = null,
)
