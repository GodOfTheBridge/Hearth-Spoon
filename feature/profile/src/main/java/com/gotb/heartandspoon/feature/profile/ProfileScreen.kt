package com.gotb.heartandspoon.feature.profile

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gotb.heartandspoon.core.designsystem.AppLanguageSelector
import com.gotb.heartandspoon.core.designsystem.HSAnimatedText
import com.gotb.heartandspoon.core.designsystem.HSAnimatedTextMotion
import com.gotb.heartandspoon.core.designsystem.ThemeFamilySelector
import com.gotb.heartandspoon.core.designsystem.ThemeModeSelector
import com.gotb.heartandspoon.core.designsystem.currentHSStringResolver
import com.gotb.heartandspoon.core.designsystem.localizedStringResource
import com.gotb.heartandspoon.core.model.AppLanguage
import com.gotb.heartandspoon.core.model.ThemeFamily
import com.gotb.heartandspoon.core.model.ThemeMode
import com.gotb.heartandspoon.core.model.isEffectivelyDark

@Composable
fun ProfileRoute(
    initialThemeMode: ThemeMode,
    initialThemeFamily: ThemeFamily,
    initialAppLanguage: AppLanguage,
    previewThemeMode: ThemeMode?,
    onThemeModePreviewed: (ThemeMode?) -> Unit,
    onThemeFamilyPreviewed: (ThemeFamily?) -> Unit,
    onAppLanguagePreviewed: (AppLanguage?) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val resolvedState =
        if (state.isReady) {
            state
        } else {
            state.copy(
                themeMode = initialThemeMode,
                themeFamily = initialThemeFamily,
                appLanguage = initialAppLanguage,
            )
        }
    val snackbarHostState = remember { SnackbarHostState() }
    val stringResolver = currentHSStringResolver()
    val currentStringResolver = rememberUpdatedState(stringResolver)

    DisposableEffect(Unit) {
        onDispose {
            onThemeModePreviewed(null)
            onThemeFamilyPreviewed(null)
            onAppLanguagePreviewed(null)
        }
    }

    LaunchedEffect(state.errorMessageRes) {
        val errorMessageRes = state.errorMessageRes ?: return@LaunchedEffect
        onThemeModePreviewed(null)
        onThemeFamilyPreviewed(null)
        onAppLanguagePreviewed(null)
        snackbarHostState.showSnackbar(currentStringResolver.value.getString(errorMessageRes))
        viewModel.clearErrorMessage()
    }

    ProfileScreen(
        state = resolvedState,
        previewThemeMode = previewThemeMode,
        snackbarHostState = snackbarHostState,
        onThemeModePreviewed = onThemeModePreviewed,
        onThemeFamilyPreviewed = onThemeFamilyPreviewed,
        onAppLanguagePreviewed = onAppLanguagePreviewed,
        onThemeModeChanged = viewModel::setThemeMode,
        onThemeFamilyChanged = viewModel::setThemeFamily,
        onAppLanguageChanged = viewModel::setAppLanguage,
    )
}

@Composable
private fun ProfileScreen(
    state: ProfileUiState,
    previewThemeMode: ThemeMode?,
    snackbarHostState: SnackbarHostState,
    onThemeModePreviewed: (ThemeMode?) -> Unit,
    onThemeFamilyPreviewed: (ThemeFamily?) -> Unit,
    onAppLanguagePreviewed: (AppLanguage?) -> Unit,
    onThemeModeChanged: (ThemeMode) -> Unit,
    onThemeFamilyChanged: (ThemeFamily) -> Unit,
    onAppLanguageChanged: (AppLanguage) -> Unit,
) {
    val systemIsDarkTheme = isSystemInDarkTheme()
    val activeThemeMode = previewThemeMode ?: state.themeMode
    val effectiveIsDarkTheme = activeThemeMode.isEffectivelyDark(systemIsDarkTheme = systemIsDarkTheme)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { contentPadding ->
        ProfileContent(
            state = state,
            contentPadding = contentPadding,
            effectiveIsDarkTheme = effectiveIsDarkTheme,
            onThemeModePreviewed = onThemeModePreviewed,
            onThemeFamilyPreviewed = onThemeFamilyPreviewed,
            onAppLanguagePreviewed = onAppLanguagePreviewed,
            onThemeModeChanged = onThemeModeChanged,
            onThemeFamilyChanged = onThemeFamilyChanged,
            onAppLanguageChanged = onAppLanguageChanged,
        )
    }
}

@Composable
private fun ProfileContent(
    state: ProfileUiState,
    contentPadding: PaddingValues,
    effectiveIsDarkTheme: Boolean,
    onThemeModePreviewed: (ThemeMode?) -> Unit,
    onThemeFamilyPreviewed: (ThemeFamily?) -> Unit,
    onAppLanguagePreviewed: (AppLanguage?) -> Unit,
    onThemeModeChanged: (ThemeMode) -> Unit,
    onThemeFamilyChanged: (ThemeFamily) -> Unit,
    onAppLanguageChanged: (AppLanguage) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        HSAnimatedText(
            text = localizedStringResource(R.string.profile_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )

        ProfileSettingSection(
            title = localizedStringResource(R.string.profile_theme_title),
            description = localizedStringResource(R.string.profile_theme_description),
        ) {
            ThemeModeSelector(
                modifier = Modifier.fillMaxWidth(),
                selectedThemeMode = state.themeMode,
                effectiveIsDarkTheme = effectiveIsDarkTheme,
                onThemeModePreviewed = onThemeModePreviewed,
                onThemeModeSelected = onThemeModeChanged,
            )
        }

        ProfileSettingSection(
            title = localizedStringResource(R.string.profile_theme_family_title),
            description = localizedStringResource(R.string.profile_theme_family_description),
        ) {
            ThemeFamilySelector(
                modifier = Modifier.fillMaxWidth(),
                selectedThemeFamily = state.themeFamily,
                onThemeFamilyPreviewed = onThemeFamilyPreviewed,
                onThemeFamilySelected = onThemeFamilyChanged,
            )
        }

        ProfileSettingSection(
            title = localizedStringResource(R.string.profile_language_title),
            description = localizedStringResource(R.string.profile_language_description),
            titleMotion = HSAnimatedTextMotion.Rotor,
        ) {
            AppLanguageSelector(
                modifier = Modifier.fillMaxWidth(),
                selectedAppLanguage = state.appLanguage,
                onAppLanguagePreviewed = onAppLanguagePreviewed,
                onAppLanguageSelected = onAppLanguageChanged,
            )
        }
    }
}

@Composable
private fun ProfileSettingSection(
    title: String,
    description: String,
    titleMotion: HSAnimatedTextMotion = HSAnimatedTextMotion.Fade,
    content: @Composable () -> Unit,
) {
    ElevatedCard(
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            HSAnimatedText(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                motion = titleMotion,
            )

            HSAnimatedText(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                motion = HSAnimatedTextMotion.None,
            )

            content()
        }
    }
}
