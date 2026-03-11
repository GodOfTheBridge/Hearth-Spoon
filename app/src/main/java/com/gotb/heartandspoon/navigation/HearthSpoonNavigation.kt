package com.gotb.heartandspoon.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.gotb.heartandspoon.feature.home.HomeDetailsRoute
import com.gotb.heartandspoon.feature.home.HomeRoute
import com.gotb.heartandspoon.feature.profile.ProfileRoute

@Composable
fun HearthSpoonAppNavigation() {
    val backStack = rememberNavBackStack(Home)
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
                    ProfileRoute()
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
                label = { Text(text = destination.label) },
                alwaysShowLabel = true,
            )
        }
    }
}

private enum class HearthSpoonTopLevelDestination(
    val label: String,
    val rootKey: HearthSpoonNavKey,
) {
    HOME(label = "Главная", rootKey = Home),
    PROFILE(label = "Профиль", rootKey = Profile),
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
