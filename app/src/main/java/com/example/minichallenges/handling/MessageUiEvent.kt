package com.example.minichallenges.handling

sealed interface MessageUiEvent {
    data class SmsReceived(val number: String, val text: String) : MessageUiEvent
}