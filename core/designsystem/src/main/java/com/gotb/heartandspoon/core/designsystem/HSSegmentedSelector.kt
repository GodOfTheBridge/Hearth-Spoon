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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

internal data class HSSegmentedSelectorOption<T : Any>(
    val value: T,
    val title: String,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun <T : Any> HSSegmentedSelector(
    options: List<HSSegmentedSelectorOption<T>>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    onOptionPreviewed: (T?) -> Unit = {},
    supportingText: ((T) -> String)? = null,
) {
    require(options.isNotEmpty()) { "HSSegmentedSelector requires at least one option." }
    require(options.any { option -> option.value == selectedOption }) {
        "HSSegmentedSelector selectedOption must be present in options."
    }

    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val currentSelectedOption by rememberUpdatedState(selectedOption)
    val currentOnOptionPreviewed by rememberUpdatedState(onOptionPreviewed)
    val currentOnOptionSelected by rememberUpdatedState(onOptionSelected)
    val selectorState = remember { AnchoredDraggableState(initialValue = selectedOption) }
    var lastPreviewedOption by remember { mutableStateOf<T?>(null) }
    val flingBehavior =
        AnchoredDraggableDefaults.flingBehavior(
            state = selectorState,
            positionalThreshold = { distance ->
                distance * hsSegmentedSelectorPositionalThresholdFraction
            },
            animationSpec = hsStandardMotionSpec(),
        )

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val contentPaddingPx = with(density) { hsSegmentedSelectorContentPadding.toPx() }
        val selectorWidthPx = with(density) { maxWidth.toPx() }
        val trackWidthPx = (selectorWidthPx - (contentPaddingPx * 2f)).coerceAtLeast(0f)
        val segmentWidthPx = trackWidthPx / options.size
        val selectedIndex = options.indexOfFirst { option -> option.value == selectedOption }
        val anchors =
            remember(options, segmentWidthPx) {
                DraggableAnchors<T> {
                    options.forEachIndexed { index, option ->
                        option.value at (segmentWidthPx * index)
                    }
                }
            }

        LaunchedEffect(anchors, segmentWidthPx, selectedOption) {
            selectorState.updateAnchors(anchors, selectedOption)
            if (
                segmentWidthPx > 0f &&
                selectorState.settledValue != selectedOption &&
                selectorState.targetValue != selectedOption
            ) {
                selectorState.snapTo(selectedOption)
            }
        }

        LaunchedEffect(selectorState) {
            snapshotFlow { selectorState.targetValue }
                .drop(1)
                .collect { targetOption ->
                    if (targetOption == currentSelectedOption) {
                        if (lastPreviewedOption != null) {
                            lastPreviewedOption = null
                            currentOnOptionPreviewed(null)
                        }
                        return@collect
                    }

                    if (lastPreviewedOption == targetOption) return@collect

                    lastPreviewedOption = targetOption
                    currentOnOptionPreviewed(targetOption)
                }
        }

        LaunchedEffect(selectorState) {
            snapshotFlow { selectorState.settledValue }
                .drop(1)
                .collect { settledOption ->
                    if (settledOption == currentSelectedOption) {
                        if (lastPreviewedOption != null) {
                            lastPreviewedOption = null
                            currentOnOptionPreviewed(null)
                        }
                        return@collect
                    }

                    lastPreviewedOption = null
                    currentOnOptionSelected(settledOption)
                }
        }

        val indicatorOffsetPx =
            selectorState.offset
                .takeIf { !it.isNaN() }
                ?: (selectedIndex * segmentWidthPx)
        val indicatorWidth = with(density) { segmentWidthPx.toDp() }
        val visualSelectedOption =
            selectorState.offset
                .takeIf { !it.isNaN() }
                ?.let { selectorState.targetValue }
                ?: selectedOption

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(hsSegmentedSelectorHeight)
                        .shadow(
                            elevation = 6.dp,
                            shape = hsSegmentedSelectorShape,
                            clip = false,
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = hsSegmentedSelectorShape,
                        )
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerLow,
                            shape = hsSegmentedSelectorShape,
                        )
                        .anchoredDraggable(
                            state = selectorState,
                            orientation = Orientation.Horizontal,
                            enabled = segmentWidthPx > 0f,
                            flingBehavior = flingBehavior,
                        )
                        .padding(hsSegmentedSelectorContentPadding),
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
                                shape = hsSegmentedSelectorIndicatorShape,
                                clip = false,
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = hsSegmentedSelectorIndicatorBorderAlpha),
                                shape = hsSegmentedSelectorIndicatorShape,
                            )
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = hsSegmentedSelectorIndicatorShape,
                            ),
                )

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .selectableGroup(),
                ) {
                    options.forEach { option ->
                        HSSegmentedSelectorSegment(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                            title = option.title,
                            selected = visualSelectedOption == option.value,
                            onClick = {
                                if (option.value == selectorState.targetValue || segmentWidthPx <= 0f) {
                                    return@HSSegmentedSelectorSegment
                                }

                                if (option.value != currentSelectedOption) {
                                    lastPreviewedOption = option.value
                                    currentOnOptionPreviewed(option.value)
                                }

                                scope.launch {
                                    selectorState.animateTo(option.value, hsStandardMotionSpec())
                                }
                            },
                        )
                    }
                }
            }

            supportingText?.let { textProvider ->
                Crossfade(
                    targetState = textProvider(visualSelectedOption),
                    animationSpec = hsStandardMotionSpec(),
                    label = "hsSegmentedSelectorSupportingText",
                ) { text ->
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun HSSegmentedSelectorSegment(
    modifier: Modifier,
    title: String,
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
            label = "hsSegmentedSelectorTextColor",
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
            text = title,
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

private const val hsSegmentedSelectorPositionalThresholdFraction = 0.35f
private const val hsSegmentedSelectorIndicatorBorderAlpha = 0.32f

private val hsSegmentedSelectorHeight = 64.dp
private val hsSegmentedSelectorContentPadding = 6.dp
private val hsSegmentedSelectorShape = RoundedCornerShape(22.dp)
private val hsSegmentedSelectorIndicatorShape = RoundedCornerShape(18.dp)
