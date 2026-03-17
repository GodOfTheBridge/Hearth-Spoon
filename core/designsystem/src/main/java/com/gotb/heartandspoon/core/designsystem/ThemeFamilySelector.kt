package com.gotb.heartandspoon.core.designsystem

import androidx.annotation.StringRes
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
        options =
            listOf(
                HSSegmentedSelectorOption(
                    value = ThemeFamily.Khokhloma,
                    title = localizedStringResource(themeFamilyTitleRes(ThemeFamily.Khokhloma)),
                ),
                HSSegmentedSelectorOption(
                    value = ThemeFamily.Gzhel,
                    title = localizedStringResource(themeFamilyTitleRes(ThemeFamily.Gzhel)),
                ),
            ),
        selectedOption = selectedThemeFamily,
        onOptionPreviewed = onThemeFamilyPreviewed,
        onOptionSelected = onThemeFamilySelected,
        segmentTextMotion = HSAnimatedTextMotion.None,
        supportingTextMotion = HSAnimatedTextMotion.None,
        supportingText = { themeFamily ->
            localizedStringResource(
                R.string.theme_family_summary_selected,
                localizedStringResource(themeFamilyTitleRes(themeFamily)),
            )
        },
    )
}

@StringRes
private fun themeFamilyTitleRes(themeFamily: ThemeFamily): Int =
    when (themeFamily) {
        ThemeFamily.Khokhloma -> R.string.theme_family_khokhloma
        ThemeFamily.Gzhel -> R.string.theme_family_gzhel
    }
