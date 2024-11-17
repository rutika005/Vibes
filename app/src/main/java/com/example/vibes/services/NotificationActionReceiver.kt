//package com.example.firebase.musicDemo.service
//
//import android.annotation.SuppressLint
//import android.app.Notification
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.app.Service
//import android.content.Context
//import android.content.Intent
//import android.content.SharedPreferences
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.media.MediaPlayer
//import android.media.MediaMetadataRetriever
//import android.os.Binder
//import android.os.Build
//import android.os.Handler
//import android.os.IBinder
//import android.util.Log
//import androidx.core.app.NotificationCompat
//import com.example.firebase.R
//import com.example.firebase.musicDemo.activity.MusicPlayerActivity
//import com.example.firebase.musicDemo.model.MusicDemoModel
//import java.io.IOException
//
//class MusicDemoService : Service() {
//    private val binder = MusicBinder()
//    private lateinit var mediaPlayer: MediaPlayer
//    private val handler = Handler()
//    private var isPlaying = false
//    private var musicFiles: List<MusicDemoModel> = emptyList()
//    private lateinit var sharedPreferences: SharedPreferences
//    private var currentSongIndex = 0
//    private var currentSongTime = 0
//    private var isRepeatEnabled = false
//    private var isShuffleEnabled = false
//    private var shuffledPlaylist: List<MusicDemoModel> = emptyList()
//
//
//    private val updateRunnable = object : Runnable {
//        override fun run() {
//            if (isPlaying) {
//                currentSongTime = getCurrentTime()
//                showNotification()
//                handler.postDelayed(this, 1000)
//            }
//        }
//    }
//
//    inner class MusicBinder : Binder() {
//        fun getService(): MusicDemoService = this@MusicDemoService
//    }
//
//    override fun onBind(intent: Intent?): IBinder = binder
//
//
//    override fun onCreate() {
//        super.onCreate()
//        init()
//    }
//
//    private fun init() {
//        mediaPlayer = MediaPlayer()
//        createNotificationChannel()
//        setMediaPlayerListeners()
//        sharedPreferences = getSharedPreferences("MusicDemoServicePrefs", Context.MODE_PRIVATE)
//        currentSongTime = sharedPreferences.getInt("last_playback_position", 0)
//    }
//
//
//
//    private fun savePlaybackPosition() {
//        sharedPreferences.edit().putInt("last_playback_position", currentSongTime).apply()
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        when (intent?.action) {
//            ACTION_PLAY_PAUSE -> if (isPlaying) pause() else play()
//            ACTION_NEXT -> playNext()
//            ACTION_PREVIOUS -> playPrevious()
//            ACTION_OPEN -> {
//                // Add checks to ensure musicFiles is initialized and currentSongIndex is valid
//                if (musicFiles.isNotEmpty()) {
//                    currentSongIndex = intent.getIntExtra(EXTRA_CURRENT_SONG_INDEX, 0).coerceIn(0, musicFiles.size - 1)
//                    setMusicFiles(musicFiles, currentSongIndex)
//
//                    if (isPlaying) {
//                        showNotification()
//                    } else {
//                        play()
//                    }
//                } else {
//                    Log.e("MusicDemoService", "Error: musicFiles is empty or not initialized")
//                }
//            }
//        }
//        return START_STICKY
//    }
//
//    fun setMusicFiles(files: List<MusicDemoModel>, index: Int) {
//        if (files.isNotEmpty()) {
//            this.musicFiles = files
//            this.currentSongIndex = index.coerceIn(0, files.size - 1) // Ensure index is valid
//            prepareMediaPlayer()
//        } else {
//            Log.e("MusicDemoService", "Error: Attempt to set empty music file list")
//        }
//    }
//
//
//    private fun prepareMediaPlayer() {
//        mediaPlayer.reset()
//        mediaPlayer.setDataSource(musicFiles[currentSongIndex].folderPath)
//        mediaPlayer.prepare()  // Make sure the player is prepared before starting
//        mediaPlayer.start()
//        currentSongTime = 0  // Start from the beginning
//    }
//
//
//    fun setRepeatEnabled(enabled: Boolean) {
//        isRepeatEnabled = enabled
//    }
//
//
//    private fun setMediaPlayerListeners() {
//        mediaPlayer.setOnCompletionListener {
//            playNext()
//            sendUpdateToActivity()
//        }
//    }
//
//    fun play() {
//        mediaPlayer.start()
//        isPlaying = true
//        handler.post(updateRunnable)
//        showNotification()
//        savePlaybackPosition()
//    }
//
//    fun pause() {
//        if (isPlaying) {
//            mediaPlayer.pause()
//            isPlaying = false
//            handler.removeCallbacks(updateRunnable)
//            showNotification()
//            savePlaybackPosition()
//        }
//    }
//
//    fun stop() {
//        if (isPlaying) {
//            mediaPlayer.stop()
//            mediaPlayer.reset()
//            isPlaying = false
//        }
//        handler.removeCallbacks(updateRunnable)
//        savePlaybackPosition()
//        stopSelf()
//    }
//
//
//    fun playNext() {
//        if (isRepeatEnabled) {
//            mediaPlayer.seekTo(0)
//            mediaPlayer.start()
//        } else {
//            if (isShuffleEnabled) {
//                // Pick a random song from the shuffled list
//                currentSongIndex = (0 until shuffledPlaylist.size).random()
//                Log.d("shufflecurrentSongIndex", "next : $currentSongIndex")
//            } else {
//                // Go to the next song in the original order
//                currentSongIndex = (currentSongIndex + 1) % musicFiles.size
//                Log.d("shufflecurrentSongIndex", "next : $currentSongIndex")
//            }
//            prepareMediaPlayer()
//            sendUpdateToActivity()
//        }
//    }
//
//    fun playPrevious() {
//        if (isShuffleEnabled) {
//            // Pick a random song from the shuffled list
//            currentSongIndex = (0 until shuffledPlaylist.size).random()
//            Log.d("shufflecurrentSongIndex", "next : $currentSongIndex")
//        } else {
//            // Go to the next song in the original order
//            currentSongIndex = (currentSongIndex - 1 + musicFiles.size) % musicFiles.size
//        }
//        prepareMediaPlayer()
//        sendUpdateToActivity()
//    }
//
//
//    fun getCurrentTime(): Int = mediaPlayer.currentPosition / 1000
//
//    fun getDuration(): Int = mediaPlayer.duration / 1000
//
//    fun seekTo(progress: Int) {
//        mediaPlayer.seekTo(progress * 1000)
//    }
//
//    fun isPlaying(): Boolean = isPlaying
//
//    fun setShuffleEnabled(enabled: Boolean) {
//        isShuffleEnabled = enabled
//        if (isShuffleEnabled){
//            shuffledPlaylist = musicFiles.shuffled()
//            Log.d("shufflecurrentSongIndex", "shuffle : $currentSongIndex")
//        } else {
//            shuffledPlaylist = musicFiles
//            currentSongIndex = musicFiles.indexOf(shuffledPlaylist[currentSongIndex])
//            Log.d("shufflecurrentSongIndex", "$currentSongIndex")
//        }
//    }
//
//    private fun showNotification() {
//        val title = musicFiles[currentSongIndex].musicList
//        val duration = getDuration()
//        val currentTime = getCurrentTime()
//
//        val albumArtBytes = getAlbumArt()
//        val albumArtBitmap = if (albumArtBytes != null) {
//            try {
//                val decodedBitmap = BitmapFactory.decodeByteArray(albumArtBytes, 0, albumArtBytes.size)
//                Bitmap.createScaledBitmap(decodedBitmap, 128, 128, false)
//            } catch (e: Exception) {
//                Log.e("MusicDemoService", "Error decoding album art: ${e.message}")
//                BitmapFactory.decodeResource(resources, R.drawable.song_drawable)
//            }
//        } else {
//            BitmapFactory.decodeResource(resources, R.drawable.song_drawable)
//        }
//
//
//        val playPauseIntent = Intent(this, MusicDemoService::class.java).apply {
//            action = ACTION_PLAY_PAUSE
//        }
//        val nextIntent = Intent(this, MusicDemoService::class.java).apply {
//            action = ACTION_NEXT
//        }
//        val previousIntent = Intent(this, MusicDemoService::class.java).apply {
//            action = ACTION_PREVIOUS
//        }
//
//        val openIntent = Intent(this, MusicPlayerActivity::class.java).apply {
//            putExtra(EXTRA_CURRENT_SONG_INDEX, currentSongIndex)
//            putExtra(EXTRA_MUSIC_FILE_PATH_LIST, ArrayList(musicFiles))
//            putExtra("current_position", currentSongTime)
//            putExtra("RepeatEnabled", isRepeatEnabled)
//            putExtra("isShuffleEnabled", isShuffleEnabled)
//            action = Intent.ACTION_MAIN
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//        }
//
//
//        val playPausePendingIntent = PendingIntent.getService(
//            this,
//            0,
//            playPauseIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//        val nextPendingIntent = PendingIntent.getService(
//            this,
//            1,
//            nextIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//        val previousPendingIntent = PendingIntent.getService(
//            this,
//            2,
//            previousIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//
//        val openPendingIntent = PendingIntent.getActivity(
//            this,
//            3,
//            openIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Ensure this is correct
//        )
//
//        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle(title)
//            .setContentText("Current Time: ${formatDuration(currentTime)} / Duration: ${formatDuration(duration)}")
//            .setSmallIcon(R.drawable.man_image)
//            .setLargeIcon(albumArtBitmap)
//            .setContentIntent(openPendingIntent)
//            .addAction(
//                if (isPlaying)
//                    androidx.media3.session.R.drawable.media3_icon_pause
//                else
//                    androidx.media3.session.R.drawable.media3_icon_play,
//                "Play/Pause",
//                playPausePendingIntent
//            )
//            .addAction(
//                androidx.media3.session.R.drawable.media3_icon_previous,
//                "Previous",
//                previousPendingIntent
//            )
//            .addAction(
//                androidx.media3.session.R.drawable.media3_icon_next,
//                "Next",
//                nextPendingIntent
//            )
//            .setPriority(NotificationCompat.PRIORITY_LOW)
//            .setOngoing(true)
//            .build()
//
//        if (isPlaying) {
//            startForeground(NOTIFICATION_ID, notification)
//        } else {
//            stopForeground(false)
//        }
//
//        sendUpdateToActivity()
//    }
//
//
//    private fun sendUpdateToActivity() {
//        currentSongTime = getCurrentTime()
//        val intent = Intent("com.example.firebase.UPDATE_UI").apply {
//            putExtra("title", musicFiles[currentSongIndex].musicList)
//            putExtra("duration", getDuration())
//            putExtra("currentTime", getCurrentTime())
//            putExtra("isPlaying", isPlaying)
//            putExtra("albumArt", getAlbumArt())
//            Log.d("currentTimeMedia"," duration : ${getDuration()}")
//        }
//        sendBroadcast(intent)
//    }
//
//
//
//    fun removeNotification() {
//        stopForeground(false)
//    }
//
//
//    private fun getAlbumArt(): ByteArray? {
//        val mediaMetadataRetriever = MediaMetadataRetriever()
//        mediaMetadataRetriever.setDataSource(musicFiles[currentSongIndex].folderPath)
//        return mediaMetadataRetriever.embeddedPicture
//    }
//
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                "Music Playback",
//                NotificationManager.IMPORTANCE_LOW
//            )
//            val manager = getSystemService(NotificationManager::class.java)
//            manager.createNotificationChannel(channel)
//        }
//    }
//
//    @SuppressLint("DefaultLocale")
//    private fun formatDuration(duration: Int): String {
//        val minutes = duration / 60
//        val seconds = duration % 60
//        return String.format("%02d:%02d", minutes, seconds)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        savePlaybackPosition()
//        if (isPlaying) {
//            pause()
//        }
//        mediaPlayer.release()
//        handler.removeCallbacks(updateRunnable)
//
//        stopForeground(true)
//    }
//
//    companion object {
//        const val CHANNEL_ID = "MusicPlaybackChannel"
//        const val NOTIFICATION_ID = 1
//        const val ACTION_PLAY_PAUSE = "com.example.firebase.ACTION_PLAY_PAUSE"
//        const val ACTION_NEXT = "com.example.firebase.ACTION_NEXT"
//        const val ACTION_PREVIOUS = "com.example.firebase.ACTION_PREVIOUS"
//        const val ACTION_OPEN = "com.example.firebase.ACTION_OPEN"
//        const val EXTRA_CURRENT_SONG_INDEX = "EXTRA_CURRENT_SONG_INDEX"
//        const val EXTRA_MUSIC_FILE_PATH_LIST = "EXTRA_MUSIC_FILE_PATH_LIST"
//    }
//}
//
