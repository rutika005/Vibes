package com.example.vibes.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SearchView
import android.widget.SeekBar
import android.widget.TextView
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

class FragmentSearch : Fragment() {

    private lateinit var searchAlbums: RecyclerView
    private lateinit var myAdapter: RecyclerViewAdapter
    private lateinit var albumArt: ImageView
    private lateinit var songTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var playPauseButton: ImageButton
    private lateinit var songSeekBar: SeekBar
    private lateinit var searchEdit: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewElements(view)
        setupRetrofitAndSearch()
        setupPlayPauseButton()
    }

    private fun initViewElements(view: View) {
        searchAlbums = view.findViewById(R.id.searchAlbums)
        albumArt = view.findViewById(R.id.songImg)
        songTitle = view.findViewById(R.id.currentSongTitle)
        artistName = view.findViewById(R.id.currentArtistName)
        playPauseButton = view.findViewById(R.id.btnPlayPause)
        songSeekBar = view.findViewById(R.id.songSeekBar)
        searchEdit = view.findViewById(R.id.searchEdit)
    }

    private fun setupRetrofitAndSearch() {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://deezerdevs-deezer.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        searchEdit.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchForMusic(it, retrofitBuilder)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        // Initial data load with a default query
        searchForMusic("Selena Gomez", retrofitBuilder)
    }

    private fun setupPlayPauseButton() {
        playPauseButton.setOnClickListener {
            myAdapter.playPauseSong()
        }
    }

    private fun updateNowPlayingUI(song: Data) {
        Picasso.get().load(song.album.cover).into(albumArt)
        songTitle.text = song.title
        artistName.text = song.artist.name
        songSeekBar.max = song.duration * 1000
    }

    private fun searchForMusic(query: String, apiInterface: ApiInterface) {
        apiInterface.getData(query).enqueue(object : Callback<MyData?> {
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                val dataList = response.body()?.data
                if (dataList != null) {
                    setupRecyclerView(dataList)
                }
            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                Log.d("onFailure", "Error: ${t.message}")
            }
        })
    }

    private fun setupRecyclerView(dataList: List<Data>) {
        myAdapter = RecyclerViewAdapter(
            requireContext(),
            dataList,
            onSongSelected = { selectedSong -> updateNowPlayingUI(selectedSong) },
            onPlayPauseStateChanged = { isPlaying -> updatePlayPauseButton(isPlaying) },
            onSongProgressUpdate = { progress -> songSeekBar.progress = progress }
        )
        searchAlbums.adapter = myAdapter
        searchAlbums.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    private fun updatePlayPauseButton(isPlaying: Boolean) {
        playPauseButton.setImageResource(
            if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
        )
    }
}