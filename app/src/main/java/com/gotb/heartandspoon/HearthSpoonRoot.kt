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

    LaunchedEffect(state.savedThemeMode, state.previewThemeMode) {
        if (state.previewThemeMode != null && state.previewThemeMode == state.savedThemeMode) {
            viewModel.previewThemeMode(themeMode = null)
        }
    }

    val effectiveIsDarkTheme = state.activeThemeMode.isEffectivelyDark(systemIsDarkTheme = systemIsDarkTheme)

    HearthSpoonTheme(isDarkTheme = effectiveIsDarkTheme) {
        HearthSpoonAppNavigation(
            previewThemeMode = state.previewThemeMode,
            onThemeModePreviewed = viewModel::previewThemeMode,
        )
    }
}
