package com.gotb.heartandspoon.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface HearthSpoonNavKey : NavKey

@Serializable
data object Home : HearthSpoonNavKey

@Serializable
data object HomeDetails : HearthSpoonNavKey

@Serializable
data object Profile : HearthSpoonNavKey