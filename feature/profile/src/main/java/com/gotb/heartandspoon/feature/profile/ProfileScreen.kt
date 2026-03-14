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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gotb.heartandspoon.core.designsystem.ThemeFamilySelector
import com.gotb.heartandspoon.core.designsystem.ThemeModeSelector
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

    DisposableEffect(Unit) {
        onDispose {
            onThemeModePreviewed(null)
            onThemeFamilyPreviewed(null)
        }
    }

    LaunchedEffect(state.errorMessage) {
        val errorMessage = state.errorMessage ?: return@LaunchedEffect
        onThemeModePreviewed(null)
        onThemeFamilyPreviewed(null)
        snackbarHostState.showSnackbar(errorMessage)
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
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Text(
            text = state.title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )

        ProfileSettingSection(
            title = "\u0422\u0435\u043c\u0430 \u043e\u0444\u043e\u0440\u043c\u043b\u0435\u043d\u0438\u044f",
            description = "\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435, \u043a\u0430\u043a \u043f\u0440\u0438\u043b\u043e\u0436\u0435\u043d\u0438\u0435 \u0434\u043e\u043b\u0436\u043d\u043e \u0432\u0435\u0441\u0442\u0438 \u0441\u0435\u0431\u044f \u0432 \u0441\u0432\u0435\u0442\u043b\u043e\u043c \u0438 \u0442\u0451\u043c\u043d\u043e\u043c \u043e\u043a\u0440\u0443\u0436\u0435\u043d\u0438\u0438.",
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
            title = "\u0421\u0442\u0438\u043b\u044c \u043e\u0444\u043e\u0440\u043c\u043b\u0435\u043d\u0438\u044f",
            description = "\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0445\u0443\u0434\u043e\u0436\u0435\u0441\u0442\u0432\u0435\u043d\u043d\u044b\u0439 \u0441\u0442\u0438\u043b\u044c \u0438\u043d\u0442\u0435\u0440\u0444\u0435\u0439\u0441\u0430 \u043f\u0440\u0438\u043b\u043e\u0436\u0435\u043d\u0438\u044f.",
        ) {
            ThemeFamilySelector(
                modifier = Modifier.fillMaxWidth(),
                selectedThemeFamily = state.themeFamily,
                onThemeFamilyPreviewed = onThemeFamilyPreviewed,
                onThemeFamilySelected = onThemeFamilyChanged,
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
