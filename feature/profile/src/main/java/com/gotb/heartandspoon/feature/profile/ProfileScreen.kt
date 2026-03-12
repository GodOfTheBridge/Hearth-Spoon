package com.gotb.heartandspoon.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gotb.heartandspoon.core.model.ThemeMode

@Composable
fun ProfileRoute(viewModel: ProfileViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileScreen(
        state = state,
        onThemeModeChanged = viewModel::setThemeMode,
    )
}

@Composable
private fun ProfileScreen(
    state: ProfileUiState,
    onThemeModeChanged: (ThemeMode) -> Unit,
) {
    val isDarkThemeEnabled = state.themeMode == ThemeMode.Dark

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { contentPadding ->
        ProfileContent(
            state = state,
            contentPadding = contentPadding,
            isDarkThemeEnabled = isDarkThemeEnabled,
            onThemeModeChanged = onThemeModeChanged,
        )
    }
}

@Composable
private fun ProfileContent(
    state: ProfileUiState,
    contentPadding: PaddingValues,
    isDarkThemeEnabled: Boolean,
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
            ListItem(
                headlineContent = {
                    Text(text = "\u0422\u0435\u043c\u0430 \u043e\u0444\u043e\u0440\u043c\u043b\u0435\u043d\u0438\u044f")
                },
                supportingContent = {
                    Text(text = themeModeLabel(themeMode = state.themeMode))
                },
                trailingContent = {
                    Switch(
                        checked = isDarkThemeEnabled,
                        onCheckedChange = { isChecked ->
                            val selectedThemeMode =
                                if (isChecked) {
                                    ThemeMode.Dark
                                } else {
                                    ThemeMode.Light
                                }
                            onThemeModeChanged(selectedThemeMode)
                        },
                    )
                },
                colors =
                    ListItemDefaults.colors(
                        containerColor = Color.Transparent,
                        headlineColor = MaterialTheme.colorScheme.onSurface,
                        supportingColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
            )
        }
    }
}

private fun themeModeLabel(themeMode: ThemeMode): String =
    when (themeMode) {
        ThemeMode.Light -> "\u0421\u0432\u0435\u0442\u043b\u0430\u044f"
        ThemeMode.Dark -> "\u0422\u0451\u043c\u043d\u0430\u044f"
    }
