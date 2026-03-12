package com.gotb.heartandspoon.core.designsystem

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween

internal const val hsStandardMotionDurationMillis = 500

internal fun <T> hsStandardMotionSpec(): TweenSpec<T> =
    tween(
        durationMillis = hsStandardMotionDurationMillis,
        easing = LinearOutSlowInEasing,
    )
