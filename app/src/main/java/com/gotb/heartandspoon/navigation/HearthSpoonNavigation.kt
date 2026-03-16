package com.gotb.heartandspoon.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.gotb.heartandspoon.R
import com.gotb.heartandspoon.core.model.ThemeFamily
import com.gotb.heartandspoon.core.model.ThemeMode
import com.gotb.heartandspoon.feature.home.HomeRoute
import com.gotb.heartandspoon.feature.homedetails.HomeDetailsRoute
import com.gotb.heartandspoon.feature.profile.ProfileRoute

@Composable
fun HearthSpoonAppNavigation(
    previewThemeMode: ThemeMode?,
    onThemeModePreviewed: (ThemeMode?) -> Unit,
    onThemeFamilyPreviewed: (ThemeFamily?) -> Unit,
) {
    val backStack = rememberNavBackStack(Home)
    val currentPreviewThemeMode = rememberUpdatedState(previewThemeMode)
    val currentOnThemeModePreviewed = rememberUpdatedState(onThemeModePreviewed)
    val currentOnThemeFamilyPreviewed = rememberUpdatedState(onThemeFamilyPreviewed)
    val entryProvider =
        remember(backStack) {
            entryProvider<NavKey> {
                entry<Home> {
                    HomeRoute(onOpenDetails = { backStack.add(HomeDetails) })
                }
                entry<HomeDetails> {
                    HomeDetailsRoute(onBack = { backStack.removeLastOrNull() })
                }
                entry<Profile> {
                    ProfileRoute(
                        previewThemeMode = currentPreviewThemeMode.value,
                        onThemeModePreviewed = currentOnThemeModePreviewed.value,
                        onThemeFamilyPreviewed = currentOnThemeFamilyPreviewed.value,
                    )
                }
            }
        }
    val entryDecorators: List<NavEntryDecorator<NavKey>> =
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
            onBack = { backStack.removeLastOrNull() },
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
                label = { Text(text = stringResource(destination.labelRes)) },
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

private fun NavBackStack<NavKey>.navigateToTopLevel(destination: HearthSpoonTopLevelDestination) {
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
