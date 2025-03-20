package com.example.minichallenges

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.minichallenges.ui.theme.DarkBackground
import com.example.minichallenges.ui.theme.LightBackground
import com.example.minichallenges.ui.theme.MiniChallengesTheme
import com.example.minichallenges.ui.theme.StarActive

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiniChallengesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DawnDuskScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
private fun DawnDuskScreen(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        DawnDuskBackground()

        SunMoonRow(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .padding(top = 165.dp)
                .padding(horizontal = 32.dp)
        )

        Column(
            modifier = Modifier.padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "How was your day?",
                style = MaterialTheme.typography.titleMedium,
                color =  MaterialTheme.colorScheme.onSurface
            )
            var rating by remember { mutableFloatStateOf(1f) }

            StarRatingBar(
                maxStars = 5,
                rating = rating,
                onRatingChanged = {
                    rating = it
                }
            )
        }

    }
}

@Composable
private fun SunMoonRow(
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()
    Box(
        modifier = modifier
    ) {
        if (isDarkTheme) {
            Icon(
                painter = painterResource(R.drawable.ic_moon),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterEnd),
                tint = Color.Unspecified
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_sun),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterStart),
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
private fun StarRatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    onRatingChanged: (Float) -> Unit
) {
    val density = LocalDensity.current.density
    val starSize = (20f * density).dp
    val starSpacing = (3f * density).dp

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = CircleShape
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.selectableGroup(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 1..maxStars) {
                    val isSelected = i <= rating
                    val icon = if (isSelected) Icons.Filled.Star else Icons.Default.Star
                    val iconTintColor = if (isSelected) StarActive else MaterialTheme.colorScheme.tertiary
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTintColor,
                        modifier = Modifier
                            .selectable(
                                selected = isSelected,
                                onClick = {
                                    onRatingChanged(i.toFloat())
                                },
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            )
                            .width(starSize).height(starSize)
                    )

                    if (i < maxStars) {
                        Spacer(modifier = Modifier.width(starSpacing))
                    }
                }
            }
        }
    }
}

@Composable
private fun DawnDuskBackground(
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = if (isDarkTheme) DarkBackground else LightBackground)
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MiniChallengesTheme {
        DawnDuskScreen()
    }
}