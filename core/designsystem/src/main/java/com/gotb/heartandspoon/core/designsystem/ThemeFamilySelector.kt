package com.gotb.heartandspoon.core.designsystem

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.gotb.heartandspoon.core.model.ThemeFamily

@Composable
fun ThemeFamilySelector(
    selectedThemeFamily: ThemeFamily,
    onThemeFamilySelected: (ThemeFamily) -> Unit,
    modifier: Modifier = Modifier,
    onThemeFamilyPreviewed: (ThemeFamily?) -> Unit = {},
) {
    val context = LocalContext.current

    HSSegmentedSelector(
        modifier = modifier,
        options =
            listOf(
                HSSegmentedSelectorOption(
                    value = ThemeFamily.Khokhloma,
                    title = context.getString(themeFamilyTitleRes(ThemeFamily.Khokhloma)),
                ),
                HSSegmentedSelectorOption(
                    value = ThemeFamily.Gzhel,
                    title = context.getString(themeFamilyTitleRes(ThemeFamily.Gzhel)),
                ),
            ),
        selectedOption = selectedThemeFamily,
        onOptionPreviewed = onThemeFamilyPreviewed,
        onOptionSelected = onThemeFamilySelected,
        supportingText = { themeFamily ->
            context.getString(
                R.string.theme_family_summary_selected,
                context.getString(themeFamilyTitleRes(themeFamily)),
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
