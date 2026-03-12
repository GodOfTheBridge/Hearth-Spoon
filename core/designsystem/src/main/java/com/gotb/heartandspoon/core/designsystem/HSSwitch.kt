package com.gotb.heartandspoon.core.designsystem

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun HSSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val transition =
        updateTransition(
            targetState = checked,
            label = "hsSwitchTransition",
        )
    val thumbOffset by
        transition.animateDp(
            transitionSpec = { hsStandardMotionSpec() },
            label = "hsSwitchThumbOffset",
        ) { isChecked ->
            if (isChecked) {
                hsSwitchCheckedThumbOffset
            } else {
                0.dp
            }
        }
    val trackColor by
        transition.animateColor(
            transitionSpec = { hsStandardMotionSpec() },
            label = "hsSwitchTrackColor",
        ) { isChecked ->
            if (isChecked) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        }
    val borderColor by
        transition.animateColor(
            transitionSpec = { hsStandardMotionSpec() },
            label = "hsSwitchBorderColor",
        ) { isChecked ->
            if (isChecked) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
        }
    val thumbColor by
        transition.animateColor(
            transitionSpec = { hsStandardMotionSpec() },
            label = "hsSwitchThumbColor",
        ) { isChecked ->
            if (isChecked) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.surface
            }
        }

    Box(
        modifier =
            modifier
                .alpha(
                    alpha =
                        if (enabled) {
                            1f
                        } else {
                            hsSwitchDisabledAlpha
                        },
                )
                .size(width = hsSwitchWidth, height = hsSwitchHeight)
                .shadow(
                    elevation = 1.dp,
                    shape = CircleShape,
                    clip = false,
                )
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = CircleShape,
                )
                .background(
                    color = trackColor,
                    shape = CircleShape,
                )
                .toggleable(
                    value = checked,
                    enabled = enabled,
                    role = Role.Switch,
                    onValueChange = onCheckedChange,
                )
                .padding(horizontal = hsSwitchPadding, vertical = hsSwitchPadding),
    ) {
        Box(
            modifier =
                Modifier
                    .offset(x = thumbOffset)
                    .size(hsSwitchThumbSize)
                    .shadow(
                        elevation = 2.dp,
                        shape = CircleShape,
                        clip = false,
                    )
                    .background(
                        color = thumbColor,
                        shape = CircleShape,
                    ),
        )
    }
}

private const val hsSwitchDisabledAlpha = 0.38f

private val hsSwitchWidth = 52.dp
private val hsSwitchHeight = 32.dp
private val hsSwitchThumbSize = 24.dp
private val hsSwitchPadding = 4.dp
private val hsSwitchCheckedThumbOffset = hsSwitchWidth - (hsSwitchPadding * 2) - hsSwitchThumbSize
