package com.example.minichallenges.handling

data class MessageUiState(
    val messages: List<SmsMessage> = fakeMessages,
    val isConfettiStarted: Boolean = false
)

data class SmsMessage(
    val number: String,
    val text: String
)

val fakeMessages = listOf(
    SmsMessage("+4444112233", "Your verification code is 872394. Please enter it to confirm your action."),
    SmsMessage("+4444334455", "Thank you for subscribing to our newsletter! Stay tuned for updates."),
    SmsMessage("+4444778899", "Your order has been shipped and will arrive by the end of the day."),
    SmsMessage("+4444990011", "Reminder: Your subscription will expire in 3 days. Renew now to continue enjoying the service.")
)
