package com.gotb.heartandspoon.core.designsystem

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun HearthSpoonTheme(
    isDarkTheme: Boolean,
    content: @Composable () -> Unit,
) {
    val animatedColorScheme = rememberAnimatedColorScheme(isDarkTheme = isDarkTheme)
    val contentAlpha = rememberThemeContentAlpha(isDarkTheme = isDarkTheme)

    MaterialTheme(
        colorScheme = animatedColorScheme,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Crossfade(
                targetState = isDarkTheme,
                modifier = Modifier.fillMaxSize(),
                animationSpec = hsStandardMotionSpec(),
                label = "themeBackdrop",
            ) { currentIsDarkTheme ->
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(resolveColorScheme(isDarkTheme = currentIsDarkTheme).background),
                )
            }

            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .graphicsLayer { alpha = contentAlpha },
            ) {
                content()
            }
        }
    }
}

private fun resolveColorScheme(isDarkTheme: Boolean) =
    when (isDarkTheme) {
        true -> hearthSpoonDarkColorScheme
        false -> hearthSpoonLightColorScheme
    }

@Composable
private fun rememberAnimatedColorScheme(isDarkTheme: Boolean): ColorScheme {
    val targetColorScheme = resolveColorScheme(isDarkTheme = isDarkTheme)
    val transition =
        updateTransition(
            targetState = isDarkTheme,
            label = "themeColorSchemeTransition",
        )

    val primary by transition.animateThemeColor(label = "primary") { colorScheme -> colorScheme.primary }
    val onPrimary by transition.animateThemeColor(label = "onPrimary") { colorScheme -> colorScheme.onPrimary }
    val primaryContainer by transition.animateThemeColor(label = "primaryContainer") { colorScheme -> colorScheme.primaryContainer }
    val onPrimaryContainer by transition.animateThemeColor(label = "onPrimaryContainer") { colorScheme -> colorScheme.onPrimaryContainer }
    val inversePrimary by transition.animateThemeColor(label = "inversePrimary") { colorScheme -> colorScheme.inversePrimary }
    val secondary by transition.animateThemeColor(label = "secondary") { colorScheme -> colorScheme.secondary }
    val onSecondary by transition.animateThemeColor(label = "onSecondary") { colorScheme -> colorScheme.onSecondary }
    val secondaryContainer by transition.animateThemeColor(label = "secondaryContainer") { colorScheme -> colorScheme.secondaryContainer }
    val onSecondaryContainer by transition.animateThemeColor(label = "onSecondaryContainer") { colorScheme -> colorScheme.onSecondaryContainer }
    val tertiary by transition.animateThemeColor(label = "tertiary") { colorScheme -> colorScheme.tertiary }
    val onTertiary by transition.animateThemeColor(label = "onTertiary") { colorScheme -> colorScheme.onTertiary }
    val tertiaryContainer by transition.animateThemeColor(label = "tertiaryContainer") { colorScheme -> colorScheme.tertiaryContainer }
    val onTertiaryContainer by transition.animateThemeColor(label = "onTertiaryContainer") { colorScheme -> colorScheme.onTertiaryContainer }
    val background by transition.animateThemeColor(label = "background") { colorScheme -> colorScheme.background }
    val onBackground by transition.animateThemeColor(label = "onBackground") { colorScheme -> colorScheme.onBackground }
    val surface by transition.animateThemeColor(label = "surface") { colorScheme -> colorScheme.surface }
    val onSurface by transition.animateThemeColor(label = "onSurface") { colorScheme -> colorScheme.onSurface }
    val surfaceVariant by transition.animateThemeColor(label = "surfaceVariant") { colorScheme -> colorScheme.surfaceVariant }
    val onSurfaceVariant by transition.animateThemeColor(label = "onSurfaceVariant") { colorScheme -> colorScheme.onSurfaceVariant }
    val surfaceTint by transition.animateThemeColor(label = "surfaceTint") { colorScheme -> colorScheme.surfaceTint }
    val inverseSurface by transition.animateThemeColor(label = "inverseSurface") { colorScheme -> colorScheme.inverseSurface }
    val inverseOnSurface by transition.animateThemeColor(label = "inverseOnSurface") { colorScheme -> colorScheme.inverseOnSurface }
    val error by transition.animateThemeColor(label = "error") { colorScheme -> colorScheme.error }
    val onError by transition.animateThemeColor(label = "onError") { colorScheme -> colorScheme.onError }
    val errorContainer by transition.animateThemeColor(label = "errorContainer") { colorScheme -> colorScheme.errorContainer }
    val onErrorContainer by transition.animateThemeColor(label = "onErrorContainer") { colorScheme -> colorScheme.onErrorContainer }
    val outline by transition.animateThemeColor(label = "outline") { colorScheme -> colorScheme.outline }
    val outlineVariant by transition.animateThemeColor(label = "outlineVariant") { colorScheme -> colorScheme.outlineVariant }
    val scrim by transition.animateThemeColor(label = "scrim") { colorScheme -> colorScheme.scrim }
    val surfaceBright by transition.animateThemeColor(label = "surfaceBright") { colorScheme -> colorScheme.surfaceBright }
    val surfaceDim by transition.animateThemeColor(label = "surfaceDim") { colorScheme -> colorScheme.surfaceDim }
    val surfaceContainer by transition.animateThemeColor(label = "surfaceContainer") { colorScheme -> colorScheme.surfaceContainer }
    val surfaceContainerHigh by transition.animateThemeColor(label = "surfaceContainerHigh") { colorScheme -> colorScheme.surfaceContainerHigh }
    val surfaceContainerHighest by transition.animateThemeColor(label = "surfaceContainerHighest") { colorScheme -> colorScheme.surfaceContainerHighest }
    val surfaceContainerLow by transition.animateThemeColor(label = "surfaceContainerLow") { colorScheme -> colorScheme.surfaceContainerLow }
    val surfaceContainerLowest by transition.animateThemeColor(label = "surfaceContainerLowest") { colorScheme -> colorScheme.surfaceContainerLowest }
    val primaryFixed by transition.animateThemeColor(label = "primaryFixed") { colorScheme -> colorScheme.primaryFixed }
    val primaryFixedDim by transition.animateThemeColor(label = "primaryFixedDim") { colorScheme -> colorScheme.primaryFixedDim }
    val onPrimaryFixed by transition.animateThemeColor(label = "onPrimaryFixed") { colorScheme -> colorScheme.onPrimaryFixed }
    val onPrimaryFixedVariant by transition.animateThemeColor(label = "onPrimaryFixedVariant") { colorScheme -> colorScheme.onPrimaryFixedVariant }
    val secondaryFixed by transition.animateThemeColor(label = "secondaryFixed") { colorScheme -> colorScheme.secondaryFixed }
    val secondaryFixedDim by transition.animateThemeColor(label = "secondaryFixedDim") { colorScheme -> colorScheme.secondaryFixedDim }
    val onSecondaryFixed by transition.animateThemeColor(label = "onSecondaryFixed") { colorScheme -> colorScheme.onSecondaryFixed }
    val onSecondaryFixedVariant by transition.animateThemeColor(label = "onSecondaryFixedVariant") { colorScheme -> colorScheme.onSecondaryFixedVariant }
    val tertiaryFixed by transition.animateThemeColor(label = "tertiaryFixed") { colorScheme -> colorScheme.tertiaryFixed }
    val tertiaryFixedDim by transition.animateThemeColor(label = "tertiaryFixedDim") { colorScheme -> colorScheme.tertiaryFixedDim }
    val onTertiaryFixed by transition.animateThemeColor(label = "onTertiaryFixed") { colorScheme -> colorScheme.onTertiaryFixed }
    val onTertiaryFixedVariant by transition.animateThemeColor(label = "onTertiaryFixedVariant") { colorScheme -> colorScheme.onTertiaryFixedVariant }

    return targetColorScheme.copy(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        inversePrimary = inversePrimary,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onTertiary,
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        surfaceTint = surfaceTint,
        inverseSurface = inverseSurface,
        inverseOnSurface = inverseOnSurface,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        outline = outline,
        outlineVariant = outlineVariant,
        scrim = scrim,
        surfaceBright = surfaceBright,
        surfaceDim = surfaceDim,
        surfaceContainer = surfaceContainer,
        surfaceContainerHigh = surfaceContainerHigh,
        surfaceContainerHighest = surfaceContainerHighest,
        surfaceContainerLow = surfaceContainerLow,
        surfaceContainerLowest = surfaceContainerLowest,
        primaryFixed = primaryFixed,
        primaryFixedDim = primaryFixedDim,
        onPrimaryFixed = onPrimaryFixed,
        onPrimaryFixedVariant = onPrimaryFixedVariant,
        secondaryFixed = secondaryFixed,
        secondaryFixedDim = secondaryFixedDim,
        onSecondaryFixed = onSecondaryFixed,
        onSecondaryFixedVariant = onSecondaryFixedVariant,
        tertiaryFixed = tertiaryFixed,
        tertiaryFixedDim = tertiaryFixedDim,
        onTertiaryFixed = onTertiaryFixed,
        onTertiaryFixedVariant = onTertiaryFixedVariant,
    )
}

@Composable
private fun rememberThemeContentAlpha(isDarkTheme: Boolean): Float {
    val contentAlpha = remember { Animatable(1f) }
    var previousThemeMode by remember { mutableStateOf(isDarkTheme) }

    LaunchedEffect(isDarkTheme) {
        if (previousThemeMode == isDarkTheme) return@LaunchedEffect

        previousThemeMode = isDarkTheme
        contentAlpha.snapTo(themeContentMinAlpha)
        contentAlpha.animateTo(
            targetValue = 1f,
            animationSpec = hsStandardMotionSpec(),
        )
    }

    return contentAlpha.value
}

@Composable
private fun Transition<Boolean>.animateThemeColor(
    label: String,
    targetValueByState: @Composable (ColorScheme) -> Color,
): State<Color> =
    animateColor(
        transitionSpec = { hsStandardMotionSpec() },
        label = label,
        targetValueByState = { isDarkTheme ->
            targetValueByState(resolveColorScheme(isDarkTheme = isDarkTheme))
        },
    )

private const val themeContentMinAlpha = 0.97f
