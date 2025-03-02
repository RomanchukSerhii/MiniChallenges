package com.example.minichallenges.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import kotlin.random.Random

data class ConfettiParams(
    val containerWidth: Int,
    val containerHeight: Int,
    val confettiShape: ConfettiShape = when (Random.nextInt(3)) {
        0 -> ConfettiShape.TRIANGLE
        1 -> ConfettiShape.RECTANGLE
        else -> ConfettiShape.CIRCLE
    },
    val sizeCoefficient: Float = Random.nextFloat() * 0.5f + 0.5f,
    val color: Color = Color(
        red = Random.nextInt(256) / 255f,
        green = Random.nextInt(256) / 255f,
        blue = Random.nextInt(256) / 255f
    ),
    val rotationDegrees: Float = if (Random.nextBoolean()) 180f else -180f,
    val appearanceDelay: Long = Random.nextLong(0, 3000)
) {
    val startIntOffset = IntOffset(getRandomXPosition(containerWidth), 0)
    val endIntOffset = IntOffset(getRandomXPosition(containerWidth), containerHeight)

    private fun getRandomXPosition(containerWidth: Int): Int {
        return Random.nextInt(0, containerWidth)
    }
}

enum class ConfettiShape {
    TRIANGLE,
    RECTANGLE,
    CIRCLE
}
