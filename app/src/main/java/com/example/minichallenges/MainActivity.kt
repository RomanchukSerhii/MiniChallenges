package com.example.minichallenges

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minichallenges.ui.theme.Background
import com.example.minichallenges.ui.theme.Card
import com.example.minichallenges.ui.theme.ChivoMono
import com.example.minichallenges.ui.theme.ConditionSurface
import com.example.minichallenges.ui.theme.MiniChallengesTheme
import com.example.minichallenges.ui.theme.PrimaryText
import com.example.minichallenges.ui.theme.SecondaryText
import com.example.minichallenges.ui.theme.TextOrange
import com.example.minichallenges.ui.theme.TextPurple

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.Transparent.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(Color.Transparent.toArgb())
        )
        setContent {
            MiniChallengesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MarsWeatherScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MarsWeatherScreen(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        MarsWeatherBackground()

        Box(
            modifier = modifier
        ) {
            WeatherCard()
        }
    }
}

@Composable
private fun MarsWeatherBackground(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = Background),
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            painter = painterResource(R.drawable.mars_surface),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun WeatherCard(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(380.dp)
            .padding(horizontal = 50.dp)
            .clip(WeatherCardShape)
            .background(Card, WeatherCardShape)
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            LocationInfo(location = stringResource(R.string.olympus_mons))

            Spacer(modifier = Modifier.weight(1f))

            WeatherCondition(condition = stringResource(R.string.dust_storm))

            TemperatureInfo(
                currentTemperature = "-63",
                highTemperature = "-52",
                lowTemperature = "-73",
                modifier = Modifier.padding(bottom = 14.dp)
            )
            
            ConditionGrid()
        }
    }
}

@Composable
private fun WeatherCondition(
    condition: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_wind),
            contentDescription = null,
            tint = Color.Unspecified
        )

        Text(
            text = condition,
            style = MaterialTheme.typography.bodyMedium,
            color = TextOrange
        )
    }
}

@Composable
private fun LocationInfo(
    location: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = TextPurple.copy(alpha = 0.3f)
        )
        Text(
            text = location,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPurple
        )
    }
}

@Composable
fun TemperatureInfo(
    currentTemperature: String,
    highTemperature: String,
    lowTemperature: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .offset(y = (-4).dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.offset(x = (-10).dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = currentTemperature,
                style = TextStyle(
                    fontFamily = ChivoMono,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Normal,
                    color = PrimaryText,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    ),
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.FirstLineTop
                    )
                )
            )
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = "°C",
                fontFamily = ChivoMono,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                color = PrimaryText
            )
        }

        Column {
            Text(
                text = "H: $highTemperature°C",
                style = MaterialTheme.typography.bodyMedium,
                color = SecondaryText
            )

            Text(
                text = "L: $lowTemperature°C",
                style = MaterialTheme.typography.bodyMedium,
                color = SecondaryText
            )
        }
    }
}

@Composable
fun ConditionGrid(
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = getWeatherConditions()) { weatherCondition ->
            ConditionItem(weatherCondition)
        }
    }
    
}

@Composable
private fun ConditionItem(
    weatherCondition: WeatherCondition,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .background(ConditionSurface)
            .padding(12.dp)

    ) {
        Text(
            text = weatherCondition.metricsName,
            style = MaterialTheme.typography.bodySmall,
            color = TextOrange
        )

        Text(
            text = weatherCondition.value,
            style = MaterialTheme.typography.bodyLarge,
            color = PrimaryText
        )
    }
}

data class WeatherCondition(
    val metricsName: String,
    val value: String
)

fun getWeatherConditions(): List<WeatherCondition> {
    return listOf(
        WeatherCondition(
            metricsName = "Wind speed",
            value = "27km/h NW"
        ),
        WeatherCondition(
            metricsName = "Pressure",
            value = "600 Pa"
        ),
        WeatherCondition(
            metricsName = "UV Radiation",
            value = "0.5 mSv/day"
        ),
        WeatherCondition(
            metricsName = "Martian date",
            value = "914 Sol"
        ),
    )
}

data object WeatherCardShape: Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val cornerOffset = with(density) { 28.dp.toPx() }

        return Outline.Generic(
            path = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width - cornerOffset, 0f)
                lineTo(size.width, cornerOffset)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
        )
    }

}

@Preview
@Composable
private fun MarsWeatherScreenPreview() {
    MiniChallengesTheme {
        MarsWeatherScreen()
    }
}