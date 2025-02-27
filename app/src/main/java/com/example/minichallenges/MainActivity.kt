package com.example.minichallenges

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.minichallenges.ui.theme.MiniChallengesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiniChallengesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        var selectedOption by remember { mutableStateOf(ThousandsSeparator.POINT) }

                        SettingItem(
                            title = stringResource(R.string.thousands_separator)
                        ) {
                            SegmentedButton(
                                segmentOptions = ThousandsSeparator.entries,
                                selectedOption = selectedOption,
                                onOptionClick = { selectedOption = it }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SegmentedButton(
    segmentOptions: List<ThousandsSeparator>,
    selectedOption: ThousandsSeparator,
    onOptionClick: (ThousandsSeparator) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var selectedOffset by remember { mutableStateOf(IntOffset.Zero) }
    var selectedBoxWidth by remember { mutableStateOf(0.dp) }
    val targetOffsetX by animateDpAsState(
        targetValue = with(density) { selectedOffset.x.toDp() },
        animationSpec = tween(durationMillis = 450)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(4.dp)
    ) {
        // Selected background
        Box(
            modifier = Modifier
                .offset (x = targetOffsetX)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .fillMaxHeight()
                .width(selectedBoxWidth)
        )

        Row {
            segmentOptions.forEach { option ->
                DisableRippleEffect {
                    Button(
                        onClick = { onOptionClick(option) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = if (option == selectedOption) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary
                        ),
                        modifier = modifier
                            .weight(1f)
                            .onGloballyPositioned { coordinates ->

                                if (option == selectedOption) {
                                    selectedOffset = IntOffset(
                                        coordinates.positionInParent().x.toInt(),
                                        coordinates.positionInParent().y.toInt()
                                    )
                                    selectedBoxWidth = with(density) {
                                        coordinates.size.width.toDp()
                                    }
                                }
                            }
                    ) {
                        option.label()
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingItem(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        content()
    }
}

enum class ThousandsSeparator(val label: @Composable () -> Unit, val separator: String) {
    POINT(label = { OptionText("1.000") }, "."),
    COMMA(label = { OptionText("1,000") }, ","),
    SPACE(label = { OptionText("1 000") }, " ")
}

@Composable
fun OptionText(
    text: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }

}

@Composable
private fun DisableRippleEffect(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalRippleTheme provides DisabledRippleThemeColorAndAlpha) {
        content()
    }
}

private object DisabledRippleThemeColorAndAlpha : RippleTheme {

    @Composable
    override fun defaultColor(): Color = Color.Transparent

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
}