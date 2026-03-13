package com.gotb.heartandspoon.core.designsystem

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.gotb.heartandspoon.core.model.ThemeMode
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThemeModeSelector(
    selectedThemeMode: ThemeMode,
    effectiveIsDarkTheme: Boolean,
    onThemeModePreviewed: (ThemeMode?) -> Unit,
    onThemeModeSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val currentSelectedThemeMode by rememberUpdatedState(selectedThemeMode)
    val currentOnThemeModePreviewed by rememberUpdatedState(onThemeModePreviewed)
    val currentOnThemeModeSelected by rememberUpdatedState(onThemeModeSelected)
    val selectorState = remember { AnchoredDraggableState(initialValue = selectedThemeMode) }
    var lastPreviewedThemeMode by remember { mutableStateOf<ThemeMode?>(null) }
    val flingBehavior =
        AnchoredDraggableDefaults.flingBehavior(
            state = selectorState,
            positionalThreshold = { distance ->
                distance * themeModeSelectorPositionalThresholdFraction
            },
            animationSpec = hsStandardMotionSpec(),
        )

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val contentPaddingPx = with(density) { themeModeSelectorContentPadding.toPx() }
        val selectorWidthPx = with(density) { maxWidth.toPx() }
        val trackWidthPx = (selectorWidthPx - (contentPaddingPx * 2f)).coerceAtLeast(0f)
        val segmentWidthPx = trackWidthPx / themeModeSelectorModes.size
        val anchors =
            remember(segmentWidthPx) {
                DraggableAnchors<ThemeMode> {
                    ThemeMode.Light at 0f
                    ThemeMode.System at segmentWidthPx
                    ThemeMode.Dark at segmentWidthPx * 2f
                }
            }

        LaunchedEffect(anchors, segmentWidthPx, selectedThemeMode) {
            selectorState.updateAnchors(anchors, selectedThemeMode)
            if (
                segmentWidthPx > 0f &&
                selectorState.settledValue != selectedThemeMode &&
                selectorState.targetValue != selectedThemeMode
            ) {
                selectorState.snapTo(selectedThemeMode)
            }
        }

        LaunchedEffect(selectorState) {
            snapshotFlow { selectorState.targetValue }
                .drop(1)
                .collect { targetThemeMode ->
                    if (targetThemeMode == currentSelectedThemeMode) {
                        if (lastPreviewedThemeMode != null) {
                            lastPreviewedThemeMode = null
                            currentOnThemeModePreviewed(null)
                        }
                        return@collect
                    }

                    if (lastPreviewedThemeMode == targetThemeMode) {
                        return@collect
                    }

                    lastPreviewedThemeMode = targetThemeMode
                    currentOnThemeModePreviewed(targetThemeMode)
                }
        }

        LaunchedEffect(selectorState) {
            snapshotFlow { selectorState.settledValue }
                .drop(1)
                .collect { settledThemeMode ->
                    if (settledThemeMode == currentSelectedThemeMode) {
                        if (lastPreviewedThemeMode != null) {
                            lastPreviewedThemeMode = null
                            currentOnThemeModePreviewed(null)
                        }
                        return@collect
                    }

                    lastPreviewedThemeMode = null
                    currentOnThemeModeSelected(settledThemeMode)
                }
        }

        val indicatorOffsetPx =
            selectorState.offset
                .takeIf { !it.isNaN() }
                ?: (selectedThemeMode.selectorIndex() * segmentWidthPx)
        val indicatorWidth = with(density) { segmentWidthPx.toDp() }
        val visualSelectedThemeMode =
            selectorState.offset
                .takeIf { !it.isNaN() }
                ?.let { selectorState.targetValue }
                ?: selectedThemeMode

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(themeModeSelectorHeight)
                        .shadow(
                            elevation = 6.dp,
                            shape = themeModeSelectorShape,
                            clip = false,
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = themeModeSelectorShape,
                        )
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerLow,
                            shape = themeModeSelectorShape,
                        )
                        .anchoredDraggable(
                            state = selectorState,
                            orientation = Orientation.Horizontal,
                            enabled = segmentWidthPx > 0f,
                            flingBehavior = flingBehavior,
                        )
                        .padding(themeModeSelectorContentPadding),
            ) {
                Box(
                    modifier =
                        Modifier
                            .offset {
                                IntOffset(
                                    x = indicatorOffsetPx.roundToInt(),
                                    y = 0,
                                )
                            }
                            .width(indicatorWidth)
                            .fillMaxHeight()
                            .shadow(
                                elevation = 4.dp,
                                shape = themeModeSelectorIndicatorShape,
                                clip = false,
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = themeModeSelectorIndicatorBorderAlpha),
                                shape = themeModeSelectorIndicatorShape,
                            )
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = themeModeSelectorIndicatorShape,
                            ),
                )

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .selectableGroup(),
                ) {
                    themeModeSelectorModes.forEach { themeMode ->
                        ThemeModeSelectorSegment(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                            themeMode = themeMode,
                            selected = visualSelectedThemeMode == themeMode,
                            onClick = {
                                if (themeMode == selectorState.targetValue || segmentWidthPx <= 0f) {
                                    return@ThemeModeSelectorSegment
                                }

                                if (themeMode != currentSelectedThemeMode) {
                                    lastPreviewedThemeMode = themeMode
                                    currentOnThemeModePreviewed(themeMode)
                                }
                                scope.launch {
                                    selectorState.animateTo(themeMode, hsStandardMotionSpec())
                                }
                            },
                        )
                    }
                }
            }

            Crossfade(
                targetState =
                    themeModeSummaryText(
                        selectedThemeMode = visualSelectedThemeMode,
                        effectiveIsDarkTheme = effectiveIsDarkTheme,
                    ),
                animationSpec = hsStandardMotionSpec(),
                label = "themeModeSummary",
            ) { summaryText ->
                Text(
                    text = summaryText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun ThemeModeSelectorSegment(
    modifier: Modifier,
    themeMode: ThemeMode,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val textColor by
        animateColorAsState(
            targetValue =
                if (selected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            animationSpec = hsStandardMotionSpec(),
            label = "themeModeSelectorTextColor",
        )

    Box(
        modifier =
            modifier
                .selectable(
                    selected = selected,
                    role = Role.RadioButton,
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                )
                .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = themeMode.selectorTitle(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight =
                if (selected) {
                    FontWeight.SemiBold
                } else {
                    FontWeight.Medium
                },
            color = textColor,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

private fun ThemeMode.selectorIndex(): Float =
    when (this) {
        ThemeMode.Light -> 0f
        ThemeMode.System -> 1f
        ThemeMode.Dark -> 2f
    }

private fun ThemeMode.selectorTitle(): String =
    when (this) {
        ThemeMode.Light -> "\u0421\u0432\u0435\u0442\u043b\u0430\u044f"
        ThemeMode.System -> "\u0421\u0438\u0441\u0442\u0435\u043c\u043d\u0430\u044f"
        ThemeMode.Dark -> "\u0422\u0451\u043c\u043d\u0430\u044f"
    }

private fun themeModeSummaryText(
    selectedThemeMode: ThemeMode,
    effectiveIsDarkTheme: Boolean,
): String =
    when (selectedThemeMode) {
        ThemeMode.Light -> "\u0412\u044b\u0431\u0440\u0430\u043d\u0430 \u0441\u0432\u0435\u0442\u043b\u0430\u044f \u0442\u0435\u043c\u0430"
        ThemeMode.System ->
            if (effectiveIsDarkTheme) {
                "\u0421\u0435\u0439\u0447\u0430\u0441: \u0442\u0451\u043c\u043d\u0430\u044f"
            } else {
                "\u0421\u0435\u0439\u0447\u0430\u0441: \u0441\u0432\u0435\u0442\u043b\u0430\u044f"
            }

        ThemeMode.Dark -> "\u0412\u044b\u0431\u0440\u0430\u043d\u0430 \u0442\u0451\u043c\u043d\u0430\u044f \u0442\u0435\u043c\u0430"
    }

private val themeModeSelectorModes =
    listOf(
        ThemeMode.Light,
        ThemeMode.System,
        ThemeMode.Dark,
    )

private const val themeModeSelectorPositionalThresholdFraction = 0.35f
private const val themeModeSelectorIndicatorBorderAlpha = 0.32f

private val themeModeSelectorHeight = 64.dp
private val themeModeSelectorContentPadding = 6.dp
private val themeModeSelectorShape = RoundedCornerShape(22.dp)
private val themeModeSelectorIndicatorShape = RoundedCornerShape(18.dp)
