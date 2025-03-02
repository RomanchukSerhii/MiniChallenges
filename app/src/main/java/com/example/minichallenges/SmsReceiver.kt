package com.example.minichallenges

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony

class SmsReceiver(private val onSmsReceived: (String, String) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (message in messages) {
                val sender = message.displayOriginatingAddress
                val body = message.messageBody

                onSmsReceived(sender, body)
            }
        }
    }
}