package com.gotb.heartandspoon.feature.profile

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
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val themeSettingsRepository: ThemeSettingsRepository,
) : ViewModel() {
    private val errorMessageState = MutableStateFlow<String?>(null)

    val state: StateFlow<ProfileUiState> =
        combine(
            themeSettingsRepository.themeMode,
            themeSettingsRepository.themeFamily,
            errorMessageState,
        ) { themeMode, themeFamily, errorMessage ->
                ProfileUiState(
                    title = "\u041f\u0440\u043e\u0444\u0438\u043b\u044c",
                    themeMode = themeMode,
                    themeFamily = themeFamily,
                    errorMessage = errorMessage,
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = ProfileUiState(),
            )

    fun setThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            errorMessageState.value = null
            runCatching {
                themeSettingsRepository.setThemeMode(themeMode)
            }.onFailure { throwable ->
                errorMessageState.value = throwable.message ?: "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0441\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0442\u0435\u043c\u0443"
            }
        }
    }

    fun setThemeFamily(themeFamily: ThemeFamily) {
        viewModelScope.launch {
            errorMessageState.value = null
            runCatching {
                themeSettingsRepository.setThemeFamily(themeFamily)
            }.onFailure { throwable ->
                errorMessageState.value = throwable.message ?: "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0441\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0441\u0442\u0438\u043b\u044c \u043e\u0444\u043e\u0440\u043c\u043b\u0435\u043d\u0438\u044f"
            }
        }
    }

    fun clearErrorMessage() {
        errorMessageState.value = null
    }
}

data class ProfileUiState(
    val title: String = "\u041f\u0440\u043e\u0444\u0438\u043b\u044c",
    val themeMode: ThemeMode = ThemeMode.System,
    val themeFamily: ThemeFamily = ThemeFamily.Khokhloma,
    val errorMessage: String? = null,
)
