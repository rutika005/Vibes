package com.example.vibes.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.vibes.R

class MusicService : Service() {

    companion object {
        const val CHANNEL_ID = "Vibes_Channel"
        const val NOTIFICATION_ID = 1
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val songTitle = intent.getStringExtra("song_title") ?: "Unknown Song"
        val action = intent.getStringExtra("action") ?: "play"

        // Log for debugging
        Log.d("MusicService", "Action: $action, Song: $songTitle")

        when (action) {
            "play" -> {
                updateNotification(songTitle, isPlaying = true)
                Log.d("MusicService", "Playing: $songTitle")
            }
            "pause" -> {
                updateNotification(songTitle, isPlaying = false)
                Log.d("MusicService", "Paused: $songTitle")
            }
        }

        // Start the service in the foreground
        startForeground(NOTIFICATION_ID, createNotification(songTitle, action == "play"))

        return START_STICKY
    }

    private fun updateNotification(songTitle: String, isPlaying: Boolean) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Update the notification
        val updatedNotification = createNotification(songTitle, isPlaying)
        notificationManager.notify(NOTIFICATION_ID, updatedNotification)
    }

    private fun createNotification(songTitle: String, isPlaying: Boolean): Notification {
        // Create the play/pause intent
        val playPauseIntent = Intent(this, MusicService::class.java).apply {
            putExtra("song_title", songTitle)
            putExtra("action", if (isPlaying) "pause" else "play")
        }
        val playPausePendingIntent = PendingIntent.getService(
            this, 0, playPauseIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        // Build the notification
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Now Playing")
            .setContentText(songTitle)
            .setSmallIcon(R.drawable.applogo) // Replace with your app icon
            .setOngoing(isPlaying) // Keep notification active if playing
            .addAction(
                if (isPlaying) R.drawable.pause_music_icon else R.drawable.start_music_icon,
                if (isPlaying) "Pause" else "Play",
                playPausePendingIntent
            )
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Ensure visibility on the lock screen
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Vibes Music Channel"
            val channelDescription = "Notifications for Vibes Music Player"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        // Service is not bound
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MusicService", "Service destroyed")
    }
}
