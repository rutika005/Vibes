package com.example.vibes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.vibes.R

class GridAdapter(
    private val context: Context,
    private val numbersInWords: Array<String>,
    private val numberImage: IntArray
) : BaseAdapter() {

    private var layoutInflater: LayoutInflater? = null
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    // Set to store selected positions
    private val selectedPositions = mutableSetOf<Int>()

    override fun getCount(): Int {
        return numbersInWords.size
    }

    override fun getItem(position: Int): Any? {
        return numbersInWords[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        // Properly handle convertView null scenario
        if (view == null) {
            view = layoutInflater!!.inflate(R.layout.grid_item, parent, false)
        }

        // Binding views
        imageView = view!!.findViewById(R.id.imageView)
        textView = view.findViewById(R.id.textView)

        imageView.setImageResource(numberImage[position])
        textView.text = numbersInWords[position]

        // Set up click listener to handle selection
        view.setOnClickListener {
            if (selectedPositions.contains(position)) {
                selectedPositions.remove(position) // Deselect
            } else {
                selectedPositions.add(position) // Select
            }
            notifyDataSetChanged() // Refresh the grid to update selection visuals
        }

        return view
    }

    // Method to get selected positions
    fun getSelectedItems(): Set<Int> {
        return selectedPositions
    }
}