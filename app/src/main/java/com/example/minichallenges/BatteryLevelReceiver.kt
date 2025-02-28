package com.example.minichallenges

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BatteryLevelReceiver(val onBatteryLevelChanged: (Float) -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BATTERY_CHANGED) {
            val level = intent.getIntExtra("level", -1)
            val scale = intent.getIntExtra("scale", -1)
            val batteryLevel = level / scale.toFloat()
            onBatteryLevelChanged(batteryLevel)
        }
    }
}