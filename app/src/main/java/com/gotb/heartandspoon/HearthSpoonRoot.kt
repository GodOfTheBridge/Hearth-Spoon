package com.gotb.heartandspoon

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gotb.heartandspoon.core.designsystem.ProvideHSLocalizedResources
import com.gotb.heartandspoon.core.designsystem.HearthSpoonTheme
import com.gotb.heartandspoon.core.model.isEffectivelyDark
import com.gotb.heartandspoon.navigation.HearthSpoonAppNavigation

@Composable
fun HearthSpoonRoot(
    viewModel: HearthSpoonRootViewModel,
    onExitRequested: () -> Unit,
) {
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
    ClearCommittedPreviewEffect(
        savedValue = state.savedAppLanguage,
        previewValue = state.previewAppLanguage,
        clearPreview = viewModel::previewAppLanguage,
    )

    val effectiveIsDarkTheme = state.activeThemeMode.isEffectivelyDark(systemIsDarkTheme = systemIsDarkTheme)

    ProvideHSLocalizedResources(appLanguage = state.activeAppLanguage) {
        HearthSpoonTheme(themeFamily = state.activeThemeFamily, isDarkTheme = effectiveIsDarkTheme) {
            HearthSpoonAppNavigation(
                initialBackStack = state.navBackStack,
                initialThemeMode = state.savedThemeMode ?: state.activeThemeMode,
                initialThemeFamily = state.savedThemeFamily ?: state.activeThemeFamily,
                initialAppLanguage = state.savedAppLanguage ?: state.activeAppLanguage,
                previewThemeMode = state.previewThemeMode,
                onThemeModePreviewed = viewModel::previewThemeMode,
                onThemeFamilyPreviewed = viewModel::previewThemeFamily,
                onAppLanguagePreviewed = viewModel::previewAppLanguage,
                onBackStackChanged = viewModel::setNavBackStack,
                onExitRequested = onExitRequested,
            )
        }
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
