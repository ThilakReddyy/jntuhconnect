package com.dhethi.jntuhconnect.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dhethi.jntuhconnect.R
import com.dhethi.jntuhconnect.presentation.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title
            ?: remoteMessage.data["title"]
            ?: "JNTUH Connect"
        val body = remoteMessage.notification?.body
            ?: remoteMessage.data["body"]
            ?: "A new result update is available"
        showNotification(title, body, remoteMessage.data["link"])
    }

    private fun showNotification(title: String, message: String, link: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            link?.let { putExtra(EXTRA_NOTIFICATION_LINK, it) }
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "jntuh_connect_channel"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "JNTUH Connect",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "JNTUH result releases and important updates"
            }
            notificationManager.createNotificationChannel(channel)
        }
        val largeIconBitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setLargeIcon(largeIconBitmap)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notification)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        }
    }

    companion object {
        const val EXTRA_NOTIFICATION_LINK = "notification_link"
    }
}
