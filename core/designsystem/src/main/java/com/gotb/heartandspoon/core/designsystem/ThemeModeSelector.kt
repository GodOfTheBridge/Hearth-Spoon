package com.gotb.heartandspoon.core.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.annotation.StringRes
import com.gotb.heartandspoon.core.model.ThemeMode

@Composable
fun ThemeModeSelector(
    selectedThemeMode: ThemeMode,
    effectiveIsDarkTheme: Boolean,
    onThemeModePreviewed: (ThemeMode?) -> Unit,
    onThemeModeSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    HSSegmentedSelector(
        modifier = modifier,
        options =
            listOf(
                HSSegmentedSelectorOption(
                    value = ThemeMode.Light,
                    title = localizedStringResource(R.string.theme_mode_light),
                ),
                HSSegmentedSelectorOption(
                    value = ThemeMode.System,
                    title = localizedStringResource(R.string.theme_mode_system),
                ),
                HSSegmentedSelectorOption(
                    value = ThemeMode.Dark,
                    title = localizedStringResource(R.string.theme_mode_dark),
                ),
            ),
        selectedOption = selectedThemeMode,
        onOptionPreviewed = onThemeModePreviewed,
        onOptionSelected = onThemeModeSelected,
        segmentTextMotion = HSAnimatedTextMotion.None,
        supportingTextMotion = HSAnimatedTextMotion.None,
        supportingText = { themeMode ->
            localizedStringResource(
                themeModeSummaryTextRes(
                    selectedThemeMode = themeMode,
                    effectiveIsDarkTheme = effectiveIsDarkTheme,
                ),
            )
        },
    )
}

@StringRes
private fun themeModeSummaryTextRes(
    selectedThemeMode: ThemeMode,
    effectiveIsDarkTheme: Boolean,
): Int =
    when (selectedThemeMode) {
        ThemeMode.Light -> R.string.theme_mode_summary_light
        ThemeMode.System ->
            if (effectiveIsDarkTheme) {
                R.string.theme_mode_summary_system_dark
            } else {
                R.string.theme_mode_summary_system_light
            }

        ThemeMode.Dark -> R.string.theme_mode_summary_dark
    }
