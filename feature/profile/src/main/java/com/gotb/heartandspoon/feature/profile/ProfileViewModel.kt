package com.gotb.heartandspoon.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gotb.heartandspoon.core.model.ThemeMode
import com.gotb.heartandspoon.domain.api.ThemeSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val themeSettingsRepository: ThemeSettingsRepository,
) : ViewModel() {
    val state: StateFlow<ProfileUiState> =
        themeSettingsRepository.themeMode
            .map { themeMode ->
                ProfileUiState(
                    title = "\u041f\u0440\u043e\u0444\u0438\u043b\u044c",
                    themeMode = themeMode,
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = ProfileUiState(),
            )

    fun setThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            themeSettingsRepository.setThemeMode(themeMode)
        }
    }
}

data class ProfileUiState(
    val title: String = "\u041f\u0440\u043e\u0444\u0438\u043b\u044c",
    val themeMode: ThemeMode = ThemeMode.System,
)
