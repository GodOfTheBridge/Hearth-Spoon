package com.gotb.heartandspoon.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gotb.heartandspoon.core.designsystem.ThemeModeSelector
import com.gotb.heartandspoon.core.model.ThemeMode
import com.gotb.heartandspoon.core.model.isEffectivelyDark

@Composable
fun ProfileRoute(
    previewThemeMode: ThemeMode?,
    onThemeModePreviewed: (ThemeMode?) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    DisposableEffect(Unit) {
        onDispose {
            onThemeModePreviewed(null)
        }
    }

    LaunchedEffect(state.errorMessage) {
        val errorMessage = state.errorMessage ?: return@LaunchedEffect
        onThemeModePreviewed(null)
        snackbarHostState.showSnackbar(errorMessage)
        viewModel.clearErrorMessage()
    }

    ProfileScreen(
        state = state,
        previewThemeMode = previewThemeMode,
        snackbarHostState = snackbarHostState,
        onThemeModePreviewed = onThemeModePreviewed,
        onThemeModeChanged = viewModel::setThemeMode,
    )
}

@Composable
private fun ProfileScreen(
    state: ProfileUiState,
    previewThemeMode: ThemeMode?,
    snackbarHostState: SnackbarHostState,
    onThemeModePreviewed: (ThemeMode?) -> Unit,
    onThemeModeChanged: (ThemeMode) -> Unit,
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
            onThemeModeChanged = onThemeModeChanged,
        )
    }
}

@Composable
private fun ProfileContent(
    state: ProfileUiState,
    contentPadding: PaddingValues,
    effectiveIsDarkTheme: Boolean,
    onThemeModePreviewed: (ThemeMode?) -> Unit,
    onThemeModeChanged: (ThemeMode) -> Unit,
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
                    text = "\u0422\u0435\u043c\u0430 \u043e\u0444\u043e\u0440\u043c\u043b\u0435\u043d\u0438\u044f",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    text = "\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435, \u043a\u0430\u043a \u043f\u0440\u0438\u043b\u043e\u0436\u0435\u043d\u0438\u0435 \u0434\u043e\u043b\u0436\u043d\u043e \u0432\u0435\u0441\u0442\u0438 \u0441\u0435\u0431\u044f \u0432 \u0441\u0432\u0435\u0442\u043b\u043e\u043c \u0438 \u0442\u0451\u043c\u043d\u043e\u043c \u043e\u043a\u0440\u0443\u0436\u0435\u043d\u0438\u0438.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                key(state.themeMode, state.errorMessage != null) {
                    ThemeModeSelector(
                        modifier = Modifier.fillMaxWidth(),
                        selectedThemeMode = state.themeMode,
                        effectiveIsDarkTheme = effectiveIsDarkTheme,
                        onThemeModePreviewed = onThemeModePreviewed,
                        onThemeModeSelected = onThemeModeChanged,
                    )
                }
            }
        }
    }
}
