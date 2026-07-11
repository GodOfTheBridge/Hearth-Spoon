package com.gotb.heartandspoon.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.gotb.heartandspoon.core.designsystem.HSAnimatedText
import com.gotb.heartandspoon.core.designsystem.HSAnimatedTextMotion
import com.gotb.heartandspoon.core.designsystem.localizedStringResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.gotb.heartandspoon.R
import com.gotb.heartandspoon.core.model.AppLanguage
import com.gotb.heartandspoon.core.model.ThemeFamily
import com.gotb.heartandspoon.core.model.ThemeMode
import com.gotb.heartandspoon.feature.home.HomeRoute
import com.gotb.heartandspoon.feature.homedetails.HomeDetailsRoute
import com.gotb.heartandspoon.feature.profile.ProfileRoute
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map

@Composable
fun HearthSpoonAppNavigation(
    initialBackStack: List<HearthSpoonNavKey>,
    initialThemeMode: ThemeMode,
    initialThemeFamily: ThemeFamily,
    initialAppLanguage: AppLanguage,
    previewThemeMode: ThemeMode?,
    onThemeModePreviewed: (ThemeMode?) -> Unit,
    onThemeFamilyPreviewed: (ThemeFamily?) -> Unit,
    onAppLanguagePreviewed: (AppLanguage?) -> Unit,
    onBackStackChanged: (List<HearthSpoonNavKey>) -> Unit,
    onExitRequested: () -> Unit,
) {
    val backStack: NavBackStack<HearthSpoonNavKey> = remember { NavBackStack(*initialBackStack.toTypedArray()) }
    val currentInitialThemeMode = rememberUpdatedState(initialThemeMode)
    val currentInitialThemeFamily = rememberUpdatedState(initialThemeFamily)
    val currentInitialAppLanguage = rememberUpdatedState(initialAppLanguage)
    val currentPreviewThemeMode = rememberUpdatedState(previewThemeMode)
    val currentOnThemeModePreviewed = rememberUpdatedState(onThemeModePreviewed)
    val currentOnThemeFamilyPreviewed = rememberUpdatedState(onThemeFamilyPreviewed)
    val currentOnAppLanguagePreviewed = rememberUpdatedState(onAppLanguagePreviewed)
    val currentOnBackStackChanged = rememberUpdatedState(onBackStackChanged)
    val currentOnExitRequested = rememberUpdatedState(onExitRequested)
    val onBackRequested = {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        } else {
            currentOnExitRequested.value()
        }
    }

    LaunchedEffect(backStack) {
        snapshotFlow { backStack.toList().filterIsInstance<HearthSpoonNavKey>() }
            .map { navBackStack -> navBackStack.ifEmpty { listOf(Home) } }
            .drop(1)
            .collectLatest { navBackStack ->
                currentOnBackStackChanged.value(navBackStack)
            }
    }
    val entryProvider =
        remember(backStack) {
            entryProvider<HearthSpoonNavKey> {
                entry<Home> {
                    HomeRoute(onOpenDetails = { backStack.add(HomeDetails) })
                }
                entry<HomeDetails> {
                    HomeDetailsRoute(onBack = onBackRequested)
                }
                entry<Profile> {
                    ProfileRoute(
                        initialThemeMode = currentInitialThemeMode.value,
                        initialThemeFamily = currentInitialThemeFamily.value,
                        initialAppLanguage = currentInitialAppLanguage.value,
                        previewThemeMode = currentPreviewThemeMode.value,
                        onThemeModePreviewed = currentOnThemeModePreviewed.value,
                        onThemeFamilyPreviewed = currentOnThemeFamilyPreviewed.value,
                        onAppLanguagePreviewed = currentOnAppLanguagePreviewed.value,
                    )
                }
            }
        }
    val entryDecorators: List<NavEntryDecorator<HearthSpoonNavKey>> =
        listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        )

    Scaffold(
        bottomBar = {
            HearthSpoonBottomBar(
                currentDestination = backStack.lastOrNull().toTopLevelDestination(),
                onDestinationSelected = { destination ->
                    backStack.navigateToTopLevel(destination)
                },
            )
        },
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            onBack = onBackRequested,
            entryDecorators = entryDecorators,
            entryProvider = entryProvider,
        )
    }
}

@Composable
private fun HearthSpoonBottomBar(
    currentDestination: HearthSpoonTopLevelDestination,
    onDestinationSelected: (HearthSpoonTopLevelDestination) -> Unit,
) {
    NavigationBar {
        HearthSpoonTopLevelDestination.entries.forEach { destination ->
            NavigationBarItem(
                selected = destination == currentDestination,
                onClick = { onDestinationSelected(destination) },
                icon = {},
                label = {
                    HSAnimatedText(
                        text = localizedStringResource(destination.labelRes),
                        motion = HSAnimatedTextMotion.None,
                    )
                },
                alwaysShowLabel = true,
            )
        }
    }
}

private enum class HearthSpoonTopLevelDestination(
    @get:StringRes val labelRes: Int,
    val rootKey: HearthSpoonNavKey,
) {
    HOME(labelRes = R.string.nav_home, rootKey = Home),
    PROFILE(labelRes = R.string.nav_profile, rootKey = Profile),
}

private fun NavBackStack<HearthSpoonNavKey>.navigateToTopLevel(destination: HearthSpoonTopLevelDestination) {
    if (size == 1 && lastOrNull() == destination.rootKey) return
    clear()
    add(destination.rootKey)
}

private fun NavKey?.toTopLevelDestination(): HearthSpoonTopLevelDestination =
    when (this) {
        Profile -> HearthSpoonTopLevelDestination.PROFILE
        Home,
        HomeDetails,
        null,
        -> HearthSpoonTopLevelDestination.HOME

        else -> HearthSpoonTopLevelDestination.HOME
    }
