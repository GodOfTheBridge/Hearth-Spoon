package com.gotb.heartandspoon.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gotb.heartandspoon.core.designsystem.HSAnimatedText
import com.gotb.heartandspoon.core.designsystem.HSAnimatedTextMotion
import com.gotb.heartandspoon.core.designsystem.currentHSStringResolver
import com.gotb.heartandspoon.core.designsystem.localizedStringResource
import com.gotb.heartandspoon.core.model.HomeItem

@Composable
fun HomeRoute(
    onOpenDetails: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val stringResolver = currentHSStringResolver()
    val currentStringResolver = rememberUpdatedState(stringResolver)

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is HomeEffect.Error -> snackbarHostState.showSnackbar(currentStringResolver.value.getString(effect.messageRes))
            }
        }
    }

    HomeScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onOpenDetails = onOpenDetails,
    )
}

@Composable
private fun HomeScreen(
    state: HomeState,
    snackbarHostState: SnackbarHostState,
    onOpenDetails: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        HSAnimatedText(
            text = localizedStringResource(R.string.home_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Button(onClick = onOpenDetails) {
            HSAnimatedText(text = localizedStringResource(R.string.home_open_details))
        }
        if (state.isLoading) {
            CircularProgressIndicator()
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(
                items = state.items,
                key = HomeItem::id,
            ) { item ->
                HSAnimatedText(
                    text = item.localizedTitle(),
                    style = MaterialTheme.typography.bodyLarge,
                    motion = HSAnimatedTextMotion.None,
                )
            }
        }
        SnackbarHost(hostState = snackbarHostState)
    }
}

@Composable
private fun HomeItem.localizedTitle(): String {
    return when (id) {
        1L -> localizedStringResource(R.string.home_seed_welcome)
        2L -> localizedStringResource(R.string.home_seed_stack)
        else -> title
    }
}
