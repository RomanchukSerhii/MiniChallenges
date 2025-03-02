package com.example.minichallenges

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minichallenges.components.ConfettiBox
import com.example.minichallenges.components.SmsMessage
import com.example.minichallenges.handling.MessageUiEvent
import com.example.minichallenges.handling.MessageUiState
import com.example.minichallenges.ui.theme.MiniChallengesTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiniChallengesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Scaffold(
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.safeDrawing),
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(text = "Messages")
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.background
                                )
                            )
                        }
                    ) { innerPadding ->
                        val viewModel: MessageViewModel = viewModel()
                        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                        Box {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                                    .padding(horizontal = 12.dp)
                            ) {
                                MessageScreen(
                                    uiState = uiState,
                                    onEvent = viewModel::onEvent,
                                )
                            }

                            if (uiState.isConfettiStarted) {
                                ConfettiBox()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageScreen(
    uiState: MessageUiState,
    onEvent: (MessageUiEvent) -> Unit
) {
    val context = LocalContext.current
    val isPermissionGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.RECEIVE_SMS
    ) == PackageManager.PERMISSION_GRANTED

    var hasPermission by remember { mutableStateOf(isPermissionGranted) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            permissionLauncher.launch(Manifest.permission.RECEIVE_SMS)
        }
    }

    if (hasPermission) {
        val smsReceiver = remember {
            SmsReceiver { number, text ->
                onEvent(MessageUiEvent.SmsReceived(number, text))
            }
        }

        DisposableEffect(Unit) {
            val filter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
            context.registerReceiver(smsReceiver, filter)

            onDispose {
                context.unregisterReceiver(smsReceiver)
            }
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(uiState.messages) { message ->
            SmsMessage(
                number = message.number,
                message = message.text
            )
        }
    }
}