package com.example.vibes.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vibes.ApiInterface
import com.example.vibes.Data.Data
import com.example.vibes.Data.MyData
import com.example.vibes.R
import com.example.vibes.activity.Edituserprofile
import com.example.vibes.adapter.RecyclerViewAdapter
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentHome : Fragment() {

    private lateinit var mostPlayedSongs: RecyclerView
    private lateinit var trendingSongsPlaylist: RecyclerView
    private lateinit var popularAlbumsSong: RecyclerView
    private lateinit var mostPlayedAdapter: RecyclerViewAdapter
    private lateinit var trendingAdapter: RecyclerViewAdapter
    private lateinit var popularAdapter: RecyclerViewAdapter
    private lateinit var albumArt: ImageView
    private lateinit var songTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var playPauseButton: ImageButton
    private lateinit var songSeekBar: SeekBar
    private lateinit var profileImage:ImageView

    private var currentPlayingAdapter: RecyclerViewAdapter? = null

    private val combinedDataList = mutableListOf<Data>()
    private val TrendingDataList = mutableListOf<Data>()
    private val PopularDataList = mutableListOf<Data>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playPauseButton = view.findViewById(R.id.btnPlayPause)
        albumArt = view.findViewById(R.id.songImg)
        songTitle = view.findViewById(R.id.currentSongTitle)
        artistName = view.findViewById(R.id.currentArtistName)
        profileImage = view.findViewById(R.id.profileImage)
        songSeekBar = view.findViewById(R.id.songSeekBar)

        profileImage.setOnClickListener{
            val i=Intent(activity,Edituserprofile::class.java)
            startActivity(i)
        }

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://deezerdevs-deezer.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        mostPlayedSongs = view.findViewById(R.id.mostPlayedSongs)
        trendingSongsPlaylist = view.findViewById(R.id.trendingSongsPlaylist)
        popularAlbumsSong = view.findViewById(R.id.popularAlbumsSong)

        // Fetch data for different genres
        fetchPopularData(retrofitBuilder, "Popular")
        fetchTrendingData(retrofitBuilder, "trending")
        fetchGenreData(retrofitBuilder, "Rock") // Example genre, you can loop through other genres

        playPauseButton.setOnClickListener {
            currentPlayingAdapter?.playPauseSong() // Play/Pause the current playing song
        }

        songSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser ) {
                    currentPlayingAdapter?.mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun updateNowPlayingUI(song: Data, adapter: RecyclerViewAdapter) {
        // Stop the currently playing song if it's from a different adapter
        if (currentPlayingAdapter != null && currentPlayingAdapter != adapter) {
            currentPlayingAdapter?.stopSong() // Stop the song from the previous adapter
        }

        // Update the current playing adapter
        currentPlayingAdapter = adapter

        // // Update UI
        Picasso.get().load(song.album.cover).into(albumArt)
        songTitle.text = song.title
        artistName.text = song.artist.name
        songSeekBar.max = song.duration * 1000 // Set SeekBar max to song duration
    }

    private fun fetchGenreData(apiInterface: ApiInterface, genre: String) {
        val call = apiInterface.getData(genre)
        call.enqueue(object : Callback<MyData?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                response.body()?.data?.let { dataList ->
                    combinedDataList.addAll(dataList)
                    if (!::mostPlayedAdapter.isInitialized) {
                        mostPlayedAdapter = RecyclerViewAdapter(
                            requireContext(), combinedDataList,
                            onSongSelected = { selectedSong -> updateNowPlayingUI(selectedSong, mostPlayedAdapter) },
                            onPlayPauseStateChanged = { isPlaying -> updatePlayPauseButton(isPlaying) },
                            onSongProgressUpdate = { progress -> songSeekBar.progress = progress }
                        )
                        mostPlayedSongs.adapter = mostPlayedAdapter
                        mostPlayedSongs.layoutManager = LinearLayoutManager(
                            requireContext(), LinearLayoutManager.HORIZONTAL, false
                        )
                    } else {
                        mostPlayedAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {}
        })
    }

    private fun fetchTrendingData(apiInterface: ApiInterface, i: String) {
        val call = apiInterface.getData(i)
        call.enqueue(object : Callback<MyData?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                response.body()?.data?.let { dataList ->
                    TrendingDataList.addAll(dataList)
                    if (!::trendingAdapter.isInitialized) {
                        trendingAdapter = RecyclerViewAdapter(
                            requireContext(), TrendingDataList,
                            onSongSelected = { selectedSong -> updateNowPlayingUI(selectedSong, trendingAdapter) },
                            onPlayPauseStateChanged = { isPlaying -> updatePlayPauseButton(isPlaying) },
                            onSongProgressUpdate = { progress -> songSeekBar.progress = progress }
                        )
                        trendingSongsPlaylist.adapter = trendingAdapter
                        trendingSongsPlaylist.layoutManager = LinearLayoutManager(
                            requireContext(), LinearLayoutManager.HORIZONTAL, false
                        )
                    } else {
                        trendingAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {}
        })
    }

    private fun fetchPopularData(apiInterface: ApiInterface, j: String) {
        val call = apiInterface.getData(j)
        call.enqueue(object : Callback<MyData?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                response.body()?.data?.let { dataList ->
                    PopularDataList.addAll(dataList)
                    if (!::popularAdapter.isInitialized) {
                        popularAdapter = RecyclerViewAdapter(
                            requireContext(), PopularDataList,
                            onSongSelected = { selectedSong -> updateNowPlayingUI(selectedSong, popularAdapter) },
                            onPlayPauseStateChanged = { isPlaying -> updatePlayPauseButton(isPlaying) },
                            onSongProgressUpdate = { progress -> songSeekBar.progress = progress }
                        )
                        popularAlbumsSong.adapter = popularAdapter
                        popularAlbumsSong.layoutManager = LinearLayoutManager(
                            requireContext(), LinearLayoutManager.HORIZONTAL, false
                        )
                    } else {
                        popularAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {}
        })
    }

    private fun updatePlayPauseButton(isPlaying: Boolean) {
        playPauseButton.setImageResource(
            if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
        )
    }
}