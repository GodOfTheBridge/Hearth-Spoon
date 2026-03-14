package com.gotb.heartandspoon.core.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        options = themeModeSelectorOptions,
        selectedOption = selectedThemeMode,
        onOptionPreviewed = onThemeModePreviewed,
        onOptionSelected = onThemeModeSelected,
        supportingText = { themeMode ->
            themeModeSummaryText(
                selectedThemeMode = themeMode,
                effectiveIsDarkTheme = effectiveIsDarkTheme,
            )
        },
    )
}

private fun themeModeSummaryText(
    selectedThemeMode: ThemeMode,
    effectiveIsDarkTheme: Boolean,
): String =
    when (selectedThemeMode) {
        ThemeMode.Light -> "\u0412\u044b\u0431\u0440\u0430\u043d\u0430 \u0441\u0432\u0435\u0442\u043b\u0430\u044f \u0442\u0435\u043c\u0430"
        ThemeMode.System ->
            if (effectiveIsDarkTheme) {
                "\u0421\u0435\u0439\u0447\u0430\u0441: \u0442\u0451\u043c\u043d\u0430\u044f"
            } else {
                "\u0421\u0435\u0439\u0447\u0430\u0441: \u0441\u0432\u0435\u0442\u043b\u0430\u044f"
            }

        ThemeMode.Dark -> "\u0412\u044b\u0431\u0440\u0430\u043d\u0430 \u0442\u0451\u043c\u043d\u0430\u044f \u0442\u0435\u043c\u0430"
    }

private val themeModeSelectorOptions =
    listOf(
        HSSegmentedSelectorOption(
            value = ThemeMode.Light,
            title = "\u0421\u0432\u0435\u0442\u043b\u0430\u044f",
        ),
        HSSegmentedSelectorOption(
            value = ThemeMode.System,
            title = "\u0421\u0438\u0441\u0442\u0435\u043c\u043d\u0430\u044f",
        ),
        HSSegmentedSelectorOption(
            value = ThemeMode.Dark,
            title = "\u0422\u0451\u043c\u043d\u0430\u044f",
        ),
    )
