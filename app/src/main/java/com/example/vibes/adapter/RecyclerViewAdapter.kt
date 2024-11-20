
package com.example.vibes.adapter

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vibes.Data.Data
import com.example.vibes.R
import com.example.vibes.service.MusicService
import com.squareup.picasso.Picasso

class RecyclerViewAdapter(
    private val context: Context,
    private val dataList: List<Data>,
    private val onSongSelected: (Data) -> Unit,
    private val onPlayPauseStateChanged: (Boolean) -> Unit,
    private val onSongProgressUpdate: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private var currentPosition: Int = -1 // Use var for reassignable variables
    private val handler = Handler()
    private lateinit var musicService: MusicService
    private val progressUpdater = object : Runnable {
        override fun run() {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    onSongProgressUpdate(it.currentPosition)
                    handler.postDelayed(this, 500)
                }
            }
        }
    }
    companion object {
        var currentlyPlayingAdapter: RecyclerViewAdapter? = null
    }

    private fun startSong(songData: Data, position: Int) {
        // Stop the currently playing song from another adapter
        currentlyPlayingAdapter?.stopSong()

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(songData.preview)
            prepare()
            start()
            this@RecyclerViewAdapter.isPlaying = true
            currentlyPlayingAdapter = this@RecyclerViewAdapter // Set this adapter as currently playing
            this@RecyclerViewAdapter.currentPosition = position // Update the current position
            onPlayPauseStateChanged(isPlaying)
            handler.post(progressUpdater)

            val musicServiceIntent = Intent(context, MusicService::class.java).apply {
                putExtra("song_title", songData.title)
                putExtra("song_uri", songData.preview)
                putExtra("action", "play")
            }
            context.startService(musicServiceIntent)

// Pass MediaPlayer instance to the service
            mediaPlayer?.let { MusicService().setMediaPlayer(it) }


        }
    }


    fun stopSong() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
        currentPosition = -1 // Reset the current position
        currentlyPlayingAdapter = null // Reset currently playing adapter
        handler.removeCallbacks(progressUpdater)
        onPlayPauseStateChanged(isPlaying)

        // Create an explicit intent to stop the MusicService
        val intent = Intent(context, MusicService::class.java)
        context.stopService(intent) // Stop the service correctly
    }

    fun playPauseSong() {
        mediaPlayer?.let {
            if (currentPosition != -1) {
                val songData = dataList[currentPosition]
                if (it.isPlaying) {
                    it.pause()
                    isPlaying = false
                    onPlayPauseStateChanged(isPlaying)

                    val intent = Intent(context, MusicService::class.java).apply {
                        putExtra("song_title", songData.title)
                        putExtra("action", "pause") // Send pause action
                    }
                    context.startService(intent)
                } else {
                    it.start()
                    isPlaying = true
                    onPlayPauseStateChanged(isPlaying)

                    val intent = Intent(context, MusicService::class.java).apply {
                        putExtra("song_title", songData.title)
                        putExtra("action", "play") // Send play action
                    }
                    context.startService(intent)
                }
            }
        }
    }



    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        handler.removeCallbacks(progressUpdater)
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.each_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val songData = dataList[position]
        holder.title.text = songData.title
        holder.artist.text = songData.artist.name
        Picasso.get().load(songData.album.cover).into(holder.image)

        holder.itemView.setOnClickListener {
            onSongSelected(songData)
            startSong(songData, position) // Pass the position to startSong
        }
    }

    override fun getItemCount() = dataList.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.musicImage)
        val title: TextView = itemView.findViewById(R.id.musicTitle)
        val artist: TextView = itemView.findViewById(R.id.musicArtist)
    }
}
