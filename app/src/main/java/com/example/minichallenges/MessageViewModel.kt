package com.example.minichallenges

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minichallenges.handling.MessageUiEvent
import com.example.minichallenges.handling.MessageUiState
import com.example.minichallenges.handling.SmsMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MessageUiState())
    val uiState: StateFlow<MessageUiState> = _uiState.asStateFlow()

    fun onEvent(event: MessageUiEvent) {
        when (event) {
            is MessageUiEvent.SmsReceived -> receivedMessage(event.number, event.text)
        }
    }

    private fun receivedMessage(number: String, text: String) {
        _uiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages + SmsMessage(number, text)
            )
        }

        if (validateMessage(number, text)) {
            viewModelScope.launch {
                toggleConfettiAnimation()
                delay(6000)
                toggleConfettiAnimation()
            }
        }
    }

    private fun toggleConfettiAnimation() {
        _uiState.update {
            it.copy(isConfettiStarted = !it.isConfettiStarted)
        }
    }

    private fun validateMessage(number: String, text: String): Boolean {
        return number == NUMBER_TEMPLATE && text == MESSAGE_TEMPLATE
    }

    private companion object {
        const val NUMBER_TEMPLATE = "+4444555551"
        const val MESSAGE_TEMPLATE = "Congratulations! You've earned 500 points."
    }
}