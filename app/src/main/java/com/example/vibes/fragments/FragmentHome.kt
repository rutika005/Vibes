package com.example.vibes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vibes.ApiInterface
import com.example.vibes.Data.Data
import com.example.vibes.Data.MyData
import com.example.vibes.R
import com.example.vibes.adapter.RecyclerViewAdapter
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentHome : Fragment() {

    private lateinit var myRecyclerView: RecyclerView
    private lateinit var myAdapter: RecyclerViewAdapter
    private lateinit var albumArt: ImageView
    private lateinit var songTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var playPauseButton: ImageButton
    private val combinedDataList = mutableListOf<Data>() // Combined list for all genres

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myRecyclerView = view.findViewById(R.id.mostPlayedSongs)
        albumArt = view.findViewById(R.id.songImg)
        songTitle = view.findViewById(R.id.currentSongTitle)
        artistName = view.findViewById(R.id.currentArtistName)
        playPauseButton = view.findViewById(R.id.btnPlayPause)

        // Retrofit setup
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://deezerdevs-deezer.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        // List of genres to fetch
        val genres = listOf("Rock", "Top", "Hits", "Hip Hop", "K-Pop", "Blues","Romantic")

        // Fetch data for each genre
        for (genre in genres) {
            fetchGenreData(retrofitBuilder, genre)
        }

        // Handle play/pause button click in Now Playing section
        playPauseButton.setOnClickListener {
            myAdapter.playPauseSong()
        }
    }

    private fun fetchGenreData(apiInterface: ApiInterface, genre: String) {
        val call = apiInterface.getData(genre)
        call.enqueue(object : Callback<MyData?> {
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                response.body()?.data?.let { dataList ->
                    combinedDataList.addAll(dataList) // Add fetched data to combined list

                    // Initialize or update adapter with combined data
                    if (!::myAdapter.isInitialized) {
                        myAdapter = RecyclerViewAdapter(requireContext(), combinedDataList,
                            onSongSelected = { selectedSong -> updateNowPlayingUI(selectedSong) },
                            onPlayPauseStateChanged = { isPlaying -> updatePlayPauseButton(isPlaying) }
                        )
                        myRecyclerView.adapter = myAdapter
                        myRecyclerView.layoutManager = LinearLayoutManager(
                            requireContext(), LinearLayoutManager.HORIZONTAL, false
                        )
                    } else {
                        myAdapter.notifyDataSetChanged() // Refresh adapter with new data
                    }
                }
            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                // Handle error
            }
        })
    }

    private fun updateNowPlayingUI(song: Data) {
        Picasso.get().load(song.album.cover).into(albumArt)
        songTitle.text = song.title
        artistName.text = song.artist.name
    }

    private fun updatePlayPauseButton(isPlaying: Boolean) {
        playPauseButton.setImageResource(
            if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
        )
    }
}
