package com.gotb.heartandspoon.feature.home

import com.gotb.heartandspoon.core.model.HomeItem

data class HomeState(
    val isLoading: Boolean = false,
    val items: List<HomeItem> = emptyList(),
)

sealed interface HomeIntent {
    data object Load : HomeIntent
}

sealed interface HomeEffect {
    data class Error(val message: String) : HomeEffect
}
