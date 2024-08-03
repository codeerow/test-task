package com.example.testtaskaura.core

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.Locale

class BootNotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val bootRepository: BootRepository by inject()

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val bootInfoList = bootRepository.getAllBootInfoSync()
                val notificationText = generateNotificationText(bootInfoList)
                showNotification(notificationText)
                Log.d("MyWorker", "Notification shown")
                Result.success()
            } catch (e: Exception) {
                Result.retry()
            }
        }
    }

    private fun generateNotificationText(bootInfoList: List<BootInfo>): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

        return when {
            bootInfoList.isEmpty() -> {
                "No boots detected"
            }

            bootInfoList.size == 1 -> {
                "The boot was detected = ${dateFormat.format(bootInfoList.first().date)}"
            }

            else -> {
                val lastBoot = bootInfoList.first()
                val preLastBoot = bootInfoList[1]
                val timeDelta = lastBoot.date.time - preLastBoot.date.time
                "Last boots time delta = ${formatTimeDelta(timeDelta)}"
            }
        }
    }

    private fun formatTimeDelta(deltaMillis: Long): String {
        val deltaSeconds = deltaMillis / 1000 % 60
        val deltaMinutes = deltaMillis / (1000 * 60) % 60
        val deltaHours = deltaMillis / (1000 * 60 * 60) % 24
        val deltaDays = deltaMillis / (1000 * 60 * 60 * 24)
        return "$deltaDays days, $deltaHours hours, $deltaMinutes minutes, $deltaSeconds seconds"
    }

    private fun showNotification(text: String) {
        val channelId = "boot_notification_channel"
        val notificationId = 1

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Boot Notification"
            val descriptionText = "Notification for boot events"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val dismissIntent = Intent(applicationContext, NotificationDismissReceiver::class.java).apply {
            action = "DISMISS_NOTIFICATION"
            putExtra("notification_id", notificationId) // TODO: magic string
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            applicationContext, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.star_on)
            .setContentTitle("Boot Event Notification")
            .setContentText(text)
            .addAction(android.R.drawable.ic_notification_clear_all, "Dismiss", dismissPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true) // TODO: will not work for Android 14, make it non-cancellable somehow or find a way how to listen for cancellation

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, builder.build()) // TODO: ask for permissions on app start
        }
    }
}
