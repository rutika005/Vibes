package com.example.vibes.adapter

import android.content.Context
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
    private val handler = Handler()
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

    private fun startSong(songData: Data) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(songData.preview)
            prepare()
            start()
            this@RecyclerViewAdapter.isPlaying = true
            onPlayPauseStateChanged(isPlaying)
            handler.post(progressUpdater)
        }
    }

    fun playPauseSong() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                isPlaying = false
                onPlayPauseStateChanged(isPlaying)
                handler.removeCallbacks(progressUpdater)
            } else {
                it.start()
                isPlaying = true
                onPlayPauseStateChanged(isPlaying)
                handler.post(progressUpdater)
            }
        }
    }

    fun stopSong() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
        handler.removeCallbacks(progressUpdater)
        onPlayPauseStateChanged(isPlaying)
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
            startSong(songData)
        }
    }

    override fun getItemCount() = dataList.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.musicImage)
        val title: TextView = itemView.findViewById(R.id.musicTitle)
        val artist: TextView = itemView.findViewById(R.id.musicArtist)
    }
}