package com.gotb.heartandspoon.core.designsystem

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.gotb.heartandspoon.core.model.AppLanguage

@Composable
fun AppLanguageSelector(
    selectedAppLanguage: AppLanguage,
    onAppLanguageSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    HSSegmentedSelector(
        modifier = modifier,
        options =
            listOf(
                HSSegmentedSelectorOption(
                    value = AppLanguage.System,
                    title = context.getString(R.string.app_language_system),
                ),
                HSSegmentedSelectorOption(
                    value = AppLanguage.English,
                    title = context.getString(R.string.app_language_english),
                ),
                HSSegmentedSelectorOption(
                    value = AppLanguage.Russian,
                    title = context.getString(R.string.app_language_russian),
                ),
            ),
        selectedOption = selectedAppLanguage,
        onOptionSelected = onAppLanguageSelected,
        supportingText = { appLanguage ->
            context.getString(appLanguageSummaryRes(appLanguage))
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
