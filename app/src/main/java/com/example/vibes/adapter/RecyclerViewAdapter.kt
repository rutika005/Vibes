package com.example.vibes.adapter

import android.content.Context
import android.media.MediaPlayer
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
    val context: Context,
    val dataList: List<Data>,
    val onSongSelected: (Data) -> Unit,    // Callback to update Now Playing section
    val onPlayPauseStateChanged: (Boolean) -> Unit  // Callback to update play/pause button
) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    private var mediaPlayer: MediaPlayer? = null
    private var currentPlayingSong: Data? = null
    private var isPlaying = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.each_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentData = dataList[position]

        holder.title.text = currentData.title
        holder.artist.text = currentData.artist.name
        Picasso.get().load(currentData.album.cover).into(holder.image)

        holder.itemView.setOnClickListener {
            if (currentPlayingSong != currentData) {
                // Stop and release previous mediaPlayer instance
                stopSong()

                // Set the new song data
                currentPlayingSong = currentData
                onSongSelected(currentData)

                // Start playing the selected song
                startSong(currentData)
            } else {
                playPauseSong()  // Toggle play/pause if the same song is clicked
            }
        }
    }

    private fun startSong(songData: Data) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(songData.preview)
            prepare()
            start()
            this@RecyclerViewAdapter.isPlaying = true
            onPlayPauseStateChanged(isPlaying)
        }
    }

    fun playPauseSong() {
        mediaPlayer?.let {
            if (isPlaying) {
                it.pause()
            } else {
                it.start()
            }
            isPlaying = !isPlaying
            onPlayPauseStateChanged(isPlaying)
        }
    }

    fun stopSong() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
        onPlayPauseStateChanged(isPlaying)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.musicImage)
        val title: TextView = itemView.findViewById(R.id.musicTitle)
        val artist: TextView = itemView.findViewById(R.id.musicArtist)
    }
}