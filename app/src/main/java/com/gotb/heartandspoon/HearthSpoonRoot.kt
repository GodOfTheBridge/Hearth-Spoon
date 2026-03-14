package com.gotb.heartandspoon

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gotb.heartandspoon.core.designsystem.HearthSpoonTheme
import com.gotb.heartandspoon.core.model.isEffectivelyDark
import com.gotb.heartandspoon.navigation.HearthSpoonAppNavigation

@Composable
fun HearthSpoonRoot(viewModel: HearthSpoonRootViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val systemIsDarkTheme = isSystemInDarkTheme()

    if (!state.isReady) {
        return
    }

    ClearCommittedPreviewEffect(
        savedValue = state.savedThemeMode,
        previewValue = state.previewThemeMode,
        clearPreview = viewModel::previewThemeMode,
    )
    ClearCommittedPreviewEffect(
        savedValue = state.savedThemeFamily,
        previewValue = state.previewThemeFamily,
        clearPreview = viewModel::previewThemeFamily,
    )

    val effectiveIsDarkTheme = state.activeThemeMode.isEffectivelyDark(systemIsDarkTheme = systemIsDarkTheme)

    HearthSpoonTheme(
        themeFamily = state.activeThemeFamily,
        isDarkTheme = effectiveIsDarkTheme,
    ) {
        HearthSpoonAppNavigation(
            previewThemeMode = state.previewThemeMode,
            onThemeModePreviewed = viewModel::previewThemeMode,
            onThemeFamilyPreviewed = viewModel::previewThemeFamily,
        )
    }
}

@Composable
private fun <T> ClearCommittedPreviewEffect(
    savedValue: T?,
    previewValue: T?,
    clearPreview: (T?) -> Unit,
) {
    LaunchedEffect(savedValue, previewValue) {
        if (previewValue != null && previewValue == savedValue) {
            clearPreview(null)
        }
    }
}
