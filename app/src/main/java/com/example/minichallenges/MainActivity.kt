package com.example.minichallenges

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.minichallenges.ui.theme.CloverIcon
import com.example.minichallenges.ui.theme.DarkGray
import com.example.minichallenges.ui.theme.Green
import com.example.minichallenges.ui.theme.HeartIcon
import com.example.minichallenges.ui.theme.MiniChallengesTheme
import com.example.minichallenges.ui.theme.Red
import com.example.minichallenges.ui.theme.RedAlt
import com.example.minichallenges.ui.theme.Yellow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiniChallengesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Scaffold(
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.safeDrawing),
                        containerColor = MaterialTheme.colorScheme.surface
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            FestiveBatteryIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FestiveBatteryIndicator(
    modifier: Modifier = Modifier
) {
    var batteryLevel by remember { mutableFloatStateOf(0f) }
    val batteryLevelReceiver = remember {
        BatteryLevelReceiver { level ->
            batteryLevel = level
        }
    }
    val context = LocalContext.current

    DisposableEffect(context) {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(batteryLevelReceiver, filter)

        onDispose {
            context.unregisterReceiver(batteryLevelReceiver)
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HeartIndicator(batteryLevel)
        BatteryIndicator(batteryLevel)
        CloverIndicator(batteryLevel)
    }
}

@Composable
private fun BatteryIndicator(
    batteryLevel: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.width(223.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .height(25.dp)
                .width(10.dp)
                .align(Alignment.CenterEnd)
        )

        BoxWithConstraints(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .height(68.dp)
                .width(218.dp)
                .padding(4.dp)
        ) {
            val batteryLevelWidth by animateDpAsState(targetValue = (maxWidth.value * batteryLevel).dp)
            val batteryColor by animateColorAsState(
                targetValue = when {
                    batteryLevel <= 0.2f -> Red
                    batteryLevel >= 0.8f -> Green
                    else -> Yellow
                }
            )

            Box(
                Modifier
                    .fillMaxHeight()
                    .width(batteryLevelWidth)
                    .background(
                        color = batteryColor,
                        shape = RoundedCornerShape(12.dp)
                    )
            )

            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                repeat(5) { index ->
                    Spacer(modifier = Modifier.weight(1f))
                    if (index != 4) {
                        VerticalDivider(
                            modifier = Modifier.fillMaxHeight(),
                            thickness = 2.dp,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeartIndicator(
    batteryLevel: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        val isBatteryLow = batteryLevel <= 0.2f
        val infiniteTransition = rememberInfiniteTransition()

        val iconSize by infiniteTransition.animateFloat(
            initialValue = 30f,
            targetValue = 42f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000),
                repeatMode = RepeatMode.Reverse
            )
        )
        val iconTint by infiniteTransition.animateColor(
            initialValue = Red,
            targetValue = RedAlt,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000),
                repeatMode = RepeatMode.Reverse
            )
        )
        Icon(
            imageVector = HeartIcon,
            contentDescription = null,
            modifier = Modifier
                .size(if (isBatteryLow) iconSize.dp else 30.dp),
            tint = if (isBatteryLow) iconTint else DarkGray
        )
    }
}

@Composable
private fun CloverIndicator(
    batteryLevel: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        val isBatteryCharge = batteryLevel >= 0.8f
        val iconSize by animateDpAsState(targetValue = if (isBatteryCharge) 42.dp else 30.dp)

        Icon(
            imageVector = CloverIcon,
            contentDescription = null,
            modifier = Modifier.size(iconSize),
            tint = if (isBatteryCharge) Green else DarkGray
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFE7E9EF
)
@Composable
private fun ButteryPreview() {
    MiniChallengesTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            BatteryIndicator(0.85f)
        }
    }
}