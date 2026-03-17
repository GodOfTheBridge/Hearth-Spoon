package com.gotb.heartandspoon.core.designsystem

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

enum class HSAnimatedTextMotion {
    None,
    Fade,
    Rotor,
}

@Composable
fun HSAnimatedText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    motion: HSAnimatedTextMotion = HSAnimatedTextMotion.Fade,
) {
    if (motion == HSAnimatedTextMotion.None) {
        Text(
            text = text,
            modifier = modifier,
            style = style,
            color = color,
            fontWeight = fontWeight,
            textAlign = textAlign,
            maxLines = maxLines,
            overflow = overflow,
            softWrap = softWrap,
        )
        return
    }

    if (motion == HSAnimatedTextMotion.Rotor && text.shouldUseLetterRotor()) {
        HSLetterRotorText(
            text = text,
            modifier = modifier,
            style = style,
            color = color,
            fontWeight = fontWeight,
            textAlign = textAlign,
        )
        return
    }

    AnimatedContent(
        targetState = text,
        modifier = modifier,
        transitionSpec = {
            fadeIn(animationSpec = hsStandardMotionSpec()) togetherWith
                fadeOut(animationSpec = hsQuickFadeMotionSpec()) using SizeTransform(clip = false)
        },
        label = "hsAnimatedText",
    ) { animatedText ->
        Text(
            text = animatedText,
            style = style,
            color = color,
            fontWeight = fontWeight,
            textAlign = textAlign,
            maxLines = maxLines,
            overflow = overflow,
            softWrap = softWrap,
        )
    }
}

@Composable
private fun HSLetterRotorText(
    text: String,
    modifier: Modifier,
    style: TextStyle,
    color: Color,
    fontWeight: FontWeight?,
    textAlign: TextAlign?,
) {
    var slotCount by remember { mutableIntStateOf(text.length) }

    LaunchedEffect(text) {
        if (text.length > slotCount) {
            slotCount = text.length
        }

        val settleDelayMillis =
            (hsLetterRotorStepDurationMillis * hsLetterRotorCycleStepCount) +
                ((slotCount - 1).coerceAtLeast(0) * hsLetterRotorStaggerMillis)
        delay(settleDelayMillis.toLong())
        slotCount = text.length
    }

    val resolvedModifier =
        when (textAlign) {
            TextAlign.Center,
            TextAlign.End,
            TextAlign.Right,
            -> modifier.fillMaxWidth()

            else -> modifier
        }
    val contentAlignment =
        when (textAlign) {
            TextAlign.Center -> Alignment.Center
            TextAlign.End,
            TextAlign.Right,
            -> Alignment.CenterEnd

            else -> Alignment.CenterStart
        }

    Box(
        modifier = resolvedModifier,
        contentAlignment = contentAlignment,
    ) {
        Row {
            repeat(slotCount) { index ->
                HSLetterRotorSlot(
                    targetChar = text.getOrNull(index) ?: ' ',
                    index = index,
                    style = style,
                    color = color,
                    fontWeight = fontWeight,
                )
            }
        }
    }
}

@Composable
private fun HSLetterRotorSlot(
    targetChar: Char,
    index: Int,
    style: TextStyle,
    color: Color,
    fontWeight: FontWeight?,
) {
    val density = LocalDensity.current
    val cameraDistancePx = with(density) { 28.dp.toPx() }
    var displayedChar by remember { mutableStateOf(targetChar) }
    val motionSpec =
        remember {
            tween<Float>(
                durationMillis = hsLetterRotorStepDurationMillis,
                easing = LinearOutSlowInEasing,
            )
        }

    LaunchedEffect(targetChar) {
        if (displayedChar == targetChar) {
            return@LaunchedEffect
        }

        delay((index * hsLetterRotorStaggerMillis).toLong())
        rotorSequence(
            from = displayedChar,
            to = targetChar,
            seed = index,
        ).forEachIndexed { sequenceIndex, sequenceChar ->
            displayedChar = sequenceChar
            if (sequenceIndex != hsLetterRotorCycleStepCount - 1) {
                delay(hsLetterRotorStepDurationMillis.toLong())
            }
        }
    }

    AnimatedContent(
        targetState = displayedChar,
        transitionSpec = {
            fadeIn(animationSpec = motionSpec) togetherWith
                fadeOut(animationSpec = hsQuickFadeMotionSpec()) using SizeTransform(clip = false)
        },
        label = "hsLetterRotorSlot",
    ) { animatedChar ->
        val rotationY by transition.animateFloat(
            transitionSpec = { motionSpec },
            label = "hsLetterRotorRotationY",
        ) { character ->
            if (character == transition.targetState) {
                0f
            } else {
                82f
            }
        }
        val alpha by transition.animateFloat(
            transitionSpec = { motionSpec },
            label = "hsLetterRotorAlpha",
        ) { character ->
            if (character == transition.targetState) {
                1f
            } else {
                0f
            }
        }
        val translationY by transition.animateFloat(
            transitionSpec = { motionSpec },
            label = "hsLetterRotorTranslationY",
        ) { character ->
            if (character == transition.targetState) {
                0f
            } else {
                -3f
            }
        }

        Text(
            text = animatedChar.toString(),
            modifier =
                Modifier.graphicsLayer {
                    this.rotationY = rotationY
                    this.alpha = alpha
                    this.translationY = translationY
                    cameraDistance = cameraDistancePx
                },
            style = style,
            color = color,
            fontWeight = fontWeight,
            maxLines = 1,
            softWrap = false,
        )
    }
}

private fun String.shouldUseLetterRotor(): Boolean =
    isNotEmpty() &&
        !contains('\n') &&
        !contains(' ') &&
        length <= 12

private fun rotorSequence(
    from: Char,
    to: Char,
    seed: Int,
): List<Char> {
    if (from == to || from.isWhitespace() || to.isWhitespace()) {
        return List(hsLetterRotorCycleStepCount) { to }
    }

    val rotorAlphabet =
        when {
            from.isCyrillic() || to.isCyrillic() -> hsCombinedRotorAlphabet
            from.isLetter() || to.isLetter() -> hsLatinRotorAlphabet
            else -> return List(hsLetterRotorCycleStepCount) { to }
        }
    val intermediate = rotorAlphabet[(from.code + to.code + seed) % rotorAlphabet.size]
    return listOf(intermediate, to)
}

private fun Char.isCyrillic(): Boolean = this in '\u0400'..'\u04FF'

private const val hsLetterRotorStaggerMillis = 26
private const val hsLetterRotorStepDurationMillis = 90
private const val hsLetterRotorCycleStepCount = 2
private val hsLatinRotorAlphabet = ('A'..'Z') + ('a'..'z')
private val hsCyrillicRotorAlphabet = ('\u0410'..'\u042F') + ('\u0430'..'\u044F')
private val hsCombinedRotorAlphabet = hsLatinRotorAlphabet + hsCyrillicRotorAlphabet
