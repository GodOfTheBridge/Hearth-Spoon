package com.gotb.heartandspoon.feature.profile

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gotb.heartandspoon.core.designsystem.AppLanguageSelector
import com.gotb.heartandspoon.core.designsystem.ThemeFamilySelector
import com.gotb.heartandspoon.core.designsystem.ThemeModeSelector
import com.gotb.heartandspoon.core.model.AppLanguage
import com.gotb.heartandspoon.core.model.ThemeFamily
import com.gotb.heartandspoon.core.model.ThemeMode
import com.gotb.heartandspoon.core.model.isEffectivelyDark

@Composable
fun ProfileRoute(
    previewThemeMode: ThemeMode?,
    onThemeModePreviewed: (ThemeMode?) -> Unit,
    onThemeFamilyPreviewed: (ThemeFamily?) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    DisposableEffect(Unit) {
        onDispose {
            onThemeModePreviewed(null)
            onThemeFamilyPreviewed(null)
        }
    }

    LaunchedEffect(state.errorMessageRes) {
        val errorMessageRes = state.errorMessageRes ?: return@LaunchedEffect
        onThemeModePreviewed(null)
        onThemeFamilyPreviewed(null)
        snackbarHostState.showSnackbar(context.getString(errorMessageRes))
        viewModel.clearErrorMessage()
    }

    ProfileScreen(
        state = state,
        previewThemeMode = previewThemeMode,
        snackbarHostState = snackbarHostState,
        onThemeModePreviewed = onThemeModePreviewed,
        onThemeFamilyPreviewed = onThemeFamilyPreviewed,
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
    onThemeModeChanged: (ThemeMode) -> Unit,
    onThemeFamilyChanged: (ThemeFamily) -> Unit,
    onAppLanguageChanged: (AppLanguage) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Text(
            text = stringResource(R.string.profile_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )

        ProfileSettingSection(
            title = stringResource(R.string.profile_theme_title),
            description = stringResource(R.string.profile_theme_description),
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
            title = stringResource(R.string.profile_theme_family_title),
            description = stringResource(R.string.profile_theme_family_description),
        ) {
            ThemeFamilySelector(
                modifier = Modifier.fillMaxWidth(),
                selectedThemeFamily = state.themeFamily,
                onThemeFamilyPreviewed = onThemeFamilyPreviewed,
                onThemeFamilySelected = onThemeFamilyChanged,
            )
        }

        ProfileSettingSection(
            title = stringResource(R.string.profile_language_title),
            description = stringResource(R.string.profile_language_description),
        ) {
            AppLanguageSelector(
                modifier = Modifier.fillMaxWidth(),
                selectedAppLanguage = state.appLanguage,
                onAppLanguageSelected = onAppLanguageChanged,
            )
        }
    }
}

@Composable
private fun ProfileSettingSection(
    title: String,
    description: String,
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
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            content()
        }
    }
}
