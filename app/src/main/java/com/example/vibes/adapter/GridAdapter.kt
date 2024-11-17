package com.example.vibes.adapter

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.vibes.R

class GridAdapter(
    private val context: Context,
    private val numbersInWords: Array<String>,
    private val numberImage: IntArray
) : BaseAdapter() {

    private var layoutInflater: LayoutInflater? = null
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private var selectedPosition: Int = -1 // Track the selected item position

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("SelectedItems", Context.MODE_PRIVATE)

    override fun getCount(): Int {
        return numbersInWords.size
    }

    override fun getItem(position: Int): Any {
        return numbersInWords[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.grid_item, null)
        }

        imageView = convertView!!.findViewById(R.id.imageView)
        textView = convertView.findViewById(R.id.textView)
        imageView.setImageResource(numberImage[position])
        textView.text = numbersInWords[position]

        // Change background based on selection
        if (selectedPosition == position) {
            convertView.setBackgroundColor(Color.CYAN) // Highlight color
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT) // Default color
        }

        // Set click listener
        convertView.setOnClickListener {
            if (selectedPosition != position) {
                // Update selection
                selectedPosition = position
                saveToSharedPreferences(numbersInWords[position])
            } else {
                // Deselect the current item
                selectedPosition = -1
                clearSharedPreferences()
            }
            notifyDataSetChanged() // Refresh the grid to reflect changes
        }

        imageView.setOnClickListener {
            if (selectedPosition != position) {
                // Update selection
                selectedPosition = position
                saveToSharedPreferences(numbersInWords[position])
            } else {
                // Deselect the current item
                selectedPosition = -1
                clearSharedPreferences()
            }
            notifyDataSetChanged() // Refresh the grid to reflect changes
        }

        return convertView
    }

    private fun saveToSharedPreferences(value: String) {
        val editor = sharedPreferences.edit()
        editor.putString("artistName", value) // Save the currently selected item
        editor.apply()
    }

    private fun clearSharedPreferences() {
        val editor = sharedPreferences.edit()
        editor.remove("artistName") // Clear the stored selection
        editor.apply()
    }
}
