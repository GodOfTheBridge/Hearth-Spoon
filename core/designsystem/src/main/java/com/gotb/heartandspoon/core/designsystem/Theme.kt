package com.gotb.heartandspoon.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.gotb.heartandspoon.core.model.ThemeMode

@Composable
fun HearthSpoonTheme(
    themeMode: ThemeMode,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = resolveColorScheme(themeMode = themeMode),
        content = content,
    )
}

private fun resolveColorScheme(themeMode: ThemeMode) =
    when (themeMode) {
        ThemeMode.Light -> hearthSpoonLightColorScheme
        ThemeMode.Dark -> hearthSpoonDarkColorScheme
    }
