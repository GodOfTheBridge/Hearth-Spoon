package com.gotb.heartandspoon.core.designsystem

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.LocaleList
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.gotb.heartandspoon.core.model.AppLanguage
import java.util.Locale

@Immutable
class HSStringResolver internal constructor(
    private val resources: Resources,
) {
    fun getString(
        @StringRes resId: Int,
        vararg formatArgs: Any,
    ): String = resources.getString(resId, *formatArgs)
}

private val LocalHSStringResolver = compositionLocalOf<HSStringResolver?> { null }

@Composable
fun ProvideHSLocalizedResources(
    appLanguage: AppLanguage,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val stringResolver =
        remember(context, configuration, appLanguage) {
            HSStringResolver(resources = context.localizedResourcesFor(appLanguage))
        }

    CompositionLocalProvider(LocalHSStringResolver provides stringResolver) {
        content()
    }
}

@Composable
fun currentHSStringResolver(): HSStringResolver {
    val context = LocalContext.current
    return LocalHSStringResolver.current ?: HSStringResolver(resources = context.resources)
}

@Composable
fun localizedStringResource(
    @StringRes resId: Int,
    vararg formatArgs: Any,
): String = currentHSStringResolver().getString(resId, *formatArgs)

private fun Context.localizedResourcesFor(appLanguage: AppLanguage): Resources {
    val languageTag = appLanguage.languageTag ?: return resources
    val configuration = Configuration(resources.configuration)
    configuration.setLocales(LocaleList(Locale.forLanguageTag(languageTag)))
    return createConfigurationContext(configuration).resources
}
