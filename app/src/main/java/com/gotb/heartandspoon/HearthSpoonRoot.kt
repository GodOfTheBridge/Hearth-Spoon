package com.gotb.heartandspoon

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gotb.heartandspoon.core.model.AppLanguage
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

    ApplyAppLanguageEffect(appLanguage = state.activeAppLanguage)

    if (!state.activeAppLanguage.isApplied(applicationLocales = AppCompatDelegate.getApplicationLocales())) {
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
private fun ApplyAppLanguageEffect(appLanguage: AppLanguage) {
    LaunchedEffect(appLanguage) {
        val targetLocales = appLanguage.toLocaleListCompat()
        if (AppCompatDelegate.getApplicationLocales().toLanguageTags() != targetLocales.toLanguageTags()) {
            AppCompatDelegate.setApplicationLocales(targetLocales)
        }
    }
}

private fun AppLanguage.isApplied(applicationLocales: LocaleListCompat): Boolean {
    return toLocaleListCompat().toLanguageTags() == applicationLocales.toLanguageTags()
}

private fun AppLanguage.toLocaleListCompat(): LocaleListCompat {
    return languageTag?.let(LocaleListCompat::forLanguageTags) ?: LocaleListCompat.getEmptyLocaleList()
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
