
package com.example.vibes.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.vibes.R

class MusicService : Service() {

    private lateinit var mediaPlayer : MediaPlayer

    companion object {
        const val CHANNEL_ID = "Vibes_Channel"
        const val NOTIFICATION_ID = 1
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val songUri = intent.getStringExtra("song_uri")
        val songTitle = intent.getStringExtra("song_title") ?: "Unknown Song"
        val action = intent.getStringExtra("action") ?: "play"

        if (!this::mediaPlayer.isInitialized && songUri != null) {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(songUri)
                prepare() // Prepare synchronously
            }
        }

        when (action) {
            "play" -> {
                mediaPlayer.start()
                updateNotification(songTitle, isPlaying = true)
            }
            "pause" -> {
                mediaPlayer.pause()
                updateNotification(songTitle, isPlaying = false)
            }
        }

        startForeground(NOTIFICATION_ID, createNotification(songTitle, action == "play"))
        return START_STICKY
    }




    fun setMediaPlayer(mediaPlayer: MediaPlayer) {
        this.mediaPlayer = mediaPlayer
    }



    private fun updateNotification(songTitle: String, isPlaying: Boolean) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Update the notification
        val updatedNotification = createNotification(songTitle, isPlaying)
        notificationManager.notify(NOTIFICATION_ID, updatedNotification)
    }

    private fun createNotification(songTitle: String, isPlaying: Boolean): Notification {
        val playPauseIntent = Intent(this, MusicService::class.java).apply {
            putExtra("song_title", songTitle)
            putExtra("action", if (isPlaying) "pause" else "play")
        }
        val playPausePendingIntent = PendingIntent.getService(
            this, 0, playPauseIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Now Playing")
            .setContentText(songTitle)
            .setSmallIcon(R.drawable.applogo)
            .setOngoing(isPlaying)
            .addAction(
                if (isPlaying) R.drawable.pause_music_icon else R.drawable.start_music_icon,
                if (isPlaying) "Pause" else "Play",
                playPausePendingIntent
            )
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
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
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        Log.d("MusicService", "Service destroyed")
    }
}