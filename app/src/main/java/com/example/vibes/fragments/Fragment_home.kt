package com.example.vibes.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.example.vibes.Song
import com.example.vibes.R
import com.example.vibes.activity.Edituserprofile
import android.media.MediaPlayer

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_home : Fragment() {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var currentSongTitle: TextView
    private lateinit var currentArtistName: TextView
    private lateinit var songSeekBar: SeekBar
    private lateinit var btnPlayPause: ImageButton

    private val songList = listOf(
        Song("Nadaniya", "Akshath", 210, "https://www.example.com/nadaniya.mp3"),
        Song("Perfect", "ED Sheern", 263, "https://www.example.com/perfect.mp3"),
        Song("Sajni", "Arijit Singh", 245, "https://www.example.com/sajni.mp3"),
        Song("With You", "AP Dhillon", 200, "https://www.example.com/with_you.mp3")
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

        currentSongTitle = view?.findViewById(R.id.currentSongTitle) ?: currentSongTitle
        currentArtistName = view?.findViewById(R.id.currentArtistName) ?: currentArtistName
        songSeekBar = view?.findViewById(R.id.songSeekBar) ?: songSeekBar
        btnPlayPause = view?.findViewById(R.id.btnPlayPause) ?: btnPlayPause

        // Set up button click listener
        btnPlayPause.setOnClickListener {
            // Call your method to play the song and update the UI
            val selectedSong = getSelectedSong() // Method to get the selected song
            playSong(selectedSong)
            updateBottomPlayerUI(selectedSong)
        }

        return view
    }

    private fun updateBottomPlayerUI(song: Song) {
        currentSongTitle.text = song.title
        currentArtistName.text = song.artist
        // Update SeekBar max value and progress if necessary
        songSeekBar.max = song.duration // Set the max duration of the song
    }
    private fun playSong(song: Song) {
        // Stop the media player if it's already playing
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
                it.reset()
            }
        }

        // Initialize the MediaPlayer with the song URL
        mediaPlayer = MediaPlayer().apply {
            setDataSource(song.url) // Set the data source to the song URL
            prepare() // Prepare the MediaPlayer
            start() // Start playing the song
        }
        updateSeekBar()
    }

    private fun updateSeekBar() {
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                // Assuming you have a way to get the current position of the song
                songSeekBar.progress = getCurrentSongPosition() // Method to get current position
                handler.postDelayed(this, 1000) // Update every second
            }
        })
    }

    private fun getCurrentSongPosition(): Int {
        // Return the current song position here (e.g., from media player)
        return 0 // Replace with actual implementation
    }

    private fun getSelectedSong(): Song {
        // Logic to get the currently selected song
        return songList.random() // Replace with actual song object
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the button from the fragment's layout
        val editProfileButton = view.findViewById<ImageView>(R.id.profileimage)

        editProfileButton.setOnClickListener {
            val intent = Intent(activity, Edituserprofile::class.java)
            startActivity(intent)
        }

        //Songpage
        val songpage = view.findViewById<ImageView>(R.id.songImg)

        songpage.setOnClickListener{
            val i=Intent(this, MusicPlayingPage::class.java)
            startActivity(i)
        }
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment Fragment_home.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            Fragment_home().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}