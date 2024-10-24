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
import com.example.vibes.R
import com.example.vibes.activity.Edituserprofile
import android.media.MediaPlayer
import com.example.vibes.activity.MusicPlayingPage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_home.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("UNREACHABLE_CODE")
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun getCurrentSongPosition(): Int {
        // Return the current song position here (e.g., from media player)
        return 0 // Replace with actual implementation
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
            val i= Intent(activity, MusicPlayingPage::class.java)
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