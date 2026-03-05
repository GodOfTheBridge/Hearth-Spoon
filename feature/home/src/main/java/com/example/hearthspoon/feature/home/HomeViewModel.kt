package com.example.hearthspoon.feature.home

import androidx.lifecycle.viewModelScope
import com.example.hearthspoon.core.ui.mvi.MviViewModel
import com.example.hearthspoon.domain.api.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
) : MviViewModel<HomeState, HomeIntent, HomeEffect>(HomeState()) {

    init {
        onIntent(HomeIntent.Load)
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Load -> load()
        }
    }

    private fun load() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            runCatching { repository.getHomeItems() }
                .onSuccess { data ->
                    setState { copy(isLoading = false, items = data) }
                }
                .onFailure { throwable ->
                    setState { copy(isLoading = false) }
                    postEffect(HomeEffect.Error(throwable.message ?: "Unknown error"))
                }
        }
    }
}
