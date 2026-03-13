package com.gotb.heartandspoon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    val uiState: StateFlow<HearthSpoonRootUiState> =
        combine(
            themeSettingsRepository.themeMode,
            previewThemeModeState,
        ) { savedThemeMode, previewThemeMode ->
                HearthSpoonRootUiState(
                    savedThemeMode = savedThemeMode,
                    previewThemeMode = previewThemeMode,
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
}

data class HearthSpoonRootUiState(
    val savedThemeMode: ThemeMode = ThemeMode.System,
    val previewThemeMode: ThemeMode? = null,
) {
    val activeThemeMode: ThemeMode
        get() = previewThemeMode ?: savedThemeMode
}
