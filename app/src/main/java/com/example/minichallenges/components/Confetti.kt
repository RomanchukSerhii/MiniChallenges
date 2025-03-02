package com.example.minichallenges.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ConfettiBox(
    modifier: Modifier = Modifier
) {
    BoxWithConstraints (
        modifier = modifier.fillMaxSize()
    ) {
        val density = LocalDensity.current
        repeat(200) {
            Confetti(
                confettiParams = ConfettiParams(
                    containerWidth = with(density) {
                        maxWidth.toPx().toInt()
                    },
                    containerHeight = with(density) {
                        maxHeight.toPx().toInt()
                    }
                )
            )
        }
    }
}

@Composable
private fun Confetti(
    confettiParams: ConfettiParams,
    modifier: Modifier = Modifier
) {
    val destiny = LocalDensity.current
    var isAnimationStart by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(confettiParams.appearanceDelay)
        isAnimationStart = true
    }

    val shape = when (confettiParams.confettiShape) {
        ConfettiShape.TRIANGLE -> TriangleShape
        ConfettiShape.RECTANGLE -> RectangleShape
        else -> CircleShape
    }

    val size = with(destiny) {
        val standardSize = 16.dp
        (standardSize.toPx() * confettiParams.sizeCoefficient).toDp()
    }

    val targetIntOffset by animateIntOffsetAsState(
        targetValue = if (isAnimationStart) {
            confettiParams.endIntOffset
        } else {
            confettiParams.startIntOffset
        },
        animationSpec = tween(
            durationMillis = 3000,
            easing = LinearEasing
        )
    )

    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = confettiParams.rotationDegrees,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000,
                easing = LinearEasing
            )
        )
    )

    if (isAnimationStart) {
        Box(
            modifier = modifier
                .offset { targetIntOffset }
                .rotate(rotation)
        ) {
            Box(
                modifier = modifier
                    .size(size)
                    .clip(shape = shape)
                    .background(
                        color = confettiParams.color,
                        shape = shape
                    )
            )
        }
    }
}

private val TriangleShape = GenericShape { size, _ ->
    moveTo(size.width / 2f, 0f)
    lineTo(0f, size.height)
    lineTo(size.width, size.height)
    close()
}

private val RectangleShape = GenericShape { size, _ ->
    moveTo(0f, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width, size.height / 2f)
    lineTo(0f, size.height / 2f)
    close()
}

@Preview
@Composable
private fun ConfettiPreview() {
    Confetti(
        confettiParams = ConfettiParams(
            confettiShape = ConfettiShape.TRIANGLE,
            containerWidth = 300,
            containerHeight = 600
        ),
        modifier = Modifier.size(50.dp)
    )
}