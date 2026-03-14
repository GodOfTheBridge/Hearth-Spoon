package com.gotb.heartandspoon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gotb.heartandspoon.core.model.ThemeFamily
import com.gotb.heartandspoon.core.model.ThemeMode
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
) : ViewModel() {
    private val previewThemeModeState = MutableStateFlow<ThemeMode?>(null)
    private val previewThemeFamilyState = MutableStateFlow<ThemeFamily?>(null)

    val uiState: StateFlow<HearthSpoonRootUiState> =
        combine(
            themeSettingsRepository.themeMode,
            themeSettingsRepository.themeFamily,
            previewThemeModeState,
            previewThemeFamilyState,
        ) { savedThemeMode, savedThemeFamily, previewThemeMode, previewThemeFamily ->
            HearthSpoonRootUiState(
                savedThemeMode = savedThemeMode,
                savedThemeFamily = savedThemeFamily,
                previewThemeMode = previewThemeMode,
                previewThemeFamily = previewThemeFamily,
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
}

data class HearthSpoonRootUiState(
    val savedThemeMode: ThemeMode? = null,
    val savedThemeFamily: ThemeFamily? = null,
    val previewThemeMode: ThemeMode? = null,
    val previewThemeFamily: ThemeFamily? = null,
    val isReady: Boolean = false,
) {
    val activeThemeMode: ThemeMode
        get() = previewThemeMode ?: savedThemeMode ?: ThemeMode.System

    val activeThemeFamily: ThemeFamily
        get() = previewThemeFamily ?: savedThemeFamily ?: ThemeFamily.Khokhloma
}
