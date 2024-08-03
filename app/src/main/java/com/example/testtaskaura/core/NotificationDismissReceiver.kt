package com.example.testtaskaura.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class NotificationDismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "DISMISS_NOTIFICATION") {
            context?.let {
                val notificationId = intent.getIntExtra("notification_id", 1)
                NotificationManagerCompat.from(it).cancel(notificationId)
            }

            Log.d("NotificationDismissReceiver", "it works")
            context?.let {
                val preferences = it.getSharedPreferences("dismiss_prefs", Context.MODE_PRIVATE)
                val dismissCount = preferences.getInt("dismiss_count", 0) + 1
                val editor = preferences.edit()
                editor.putInt("dismiss_count", dismissCount)
                editor.apply()

                val delayMinutes = if (dismissCount <= 5) dismissCount * 20L else 15L
                Log.d(
                    "NotificationDismissReceiver",
                    "I will show a notification in $delayMinutes minutes"
                )
                val workRequest = OneTimeWorkRequestBuilder<BootNotificationWorker>()
                    .setInitialDelay(delayMinutes, TimeUnit.SECONDS)
                    .build()
                WorkManager.getInstance(it).enqueue(workRequest)
            }
        }
    }
}
