package com.gotb.heartandspoon.core.designsystem

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gotb.heartandspoon.core.model.AppLanguage

@Composable
fun AppLanguageSelector(
    selectedAppLanguage: AppLanguage,
    onAppLanguagePreviewed: (AppLanguage?) -> Unit = {},
    onAppLanguageSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier,
) {
    HSSegmentedSelector(
        modifier = modifier,
        options =
            listOf(
                HSSegmentedSelectorOption(
                    value = AppLanguage.Russian,
                    title = localizedStringResource(R.string.app_language_russian),
                ),
                HSSegmentedSelectorOption(
                    value = AppLanguage.System,
                    title = localizedStringResource(R.string.app_language_system),
                ),
                HSSegmentedSelectorOption(
                    value = AppLanguage.English,
                    title = localizedStringResource(R.string.app_language_english),
                ),
            ),
        selectedOption = selectedAppLanguage,
        onOptionPreviewed = onAppLanguagePreviewed,
        onOptionSelected = onAppLanguageSelected,
        segmentTextStyle = MaterialTheme.typography.labelMedium,
        segmentHorizontalPadding = 4.dp,
        segmentTextOverflow = TextOverflow.Ellipsis,
        segmentTextMotion = HSAnimatedTextMotion.Rotor,
        supportingTextMotion = HSAnimatedTextMotion.None,
        supportingText = { appLanguage ->
            localizedStringResource(appLanguageSummaryRes(appLanguage))
        },
    )
}

@StringRes
private fun appLanguageSummaryRes(appLanguage: AppLanguage): Int =
    when (appLanguage) {
        AppLanguage.System -> R.string.app_language_summary_system
        AppLanguage.English -> R.string.app_language_summary_english
        AppLanguage.Russian -> R.string.app_language_summary_russian
    }
