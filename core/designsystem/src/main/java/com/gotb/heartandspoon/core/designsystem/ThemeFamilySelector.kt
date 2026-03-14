package com.gotb.heartandspoon.core.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gotb.heartandspoon.core.model.ThemeFamily

@Composable
fun ThemeFamilySelector(
    selectedThemeFamily: ThemeFamily,
    onThemeFamilySelected: (ThemeFamily) -> Unit,
    modifier: Modifier = Modifier,
    onThemeFamilyPreviewed: (ThemeFamily?) -> Unit = {},
) {
    HSSegmentedSelector(
        modifier = modifier,
        options = themeFamilySelectorOptions,
        selectedOption = selectedThemeFamily,
        onOptionPreviewed = onThemeFamilyPreviewed,
        onOptionSelected = onThemeFamilySelected,
        supportingText = { themeFamily ->
            "\u0412\u044b\u0431\u0440\u0430\u043d \u0441\u0442\u0438\u043b\u044c: ${themeFamily.selectorTitle()}"
        },
    )
}

private fun ThemeFamily.selectorTitle(): String =
    when (this) {
        ThemeFamily.Khokhloma -> "\u0425\u043e\u0445\u043b\u043e\u043c\u0430"
        ThemeFamily.Gzhel -> "\u0413\u0436\u0435\u043b\u044c"
    }

private val themeFamilySelectorOptions =
    listOf(
        HSSegmentedSelectorOption(
            value = ThemeFamily.Khokhloma,
            title = "\u0425\u043e\u0445\u043b\u043e\u043c\u0430",
        ),
        HSSegmentedSelectorOption(
            value = ThemeFamily.Gzhel,
            title = "\u0413\u0436\u0435\u043b\u044c",
        ),
    )
