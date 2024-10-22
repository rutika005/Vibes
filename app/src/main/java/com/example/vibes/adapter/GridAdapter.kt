package com.example.vibes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.vibes.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class GridAdapter(
    private val context: Context,
    private val numbersInWords: Array<String>,
    private val numberImage: IntArray
) : BaseAdapter() {

    private var layoutInflater: LayoutInflater? = null
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var chipGroup: ChipGroup

    // Set to store selected items
    private val selectedItems = mutableSetOf<String>()

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
        chipGroup = convertView.findViewById(R.id.chipGroup)

        imageView.setImageResource(numberImage[position])
        textView.text = numbersInWords[position]

        // Add selectable chip
        val chip = Chip(context)
        chip.text = "Select ${numbersInWords[position]}"
        chip.isCheckable = true
        chip.isChecked = selectedItems.contains(numbersInWords[position])

        chip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedItems.add(numbersInWords[position])
            } else {
                selectedItems.remove(numbersInWords[position])
            }
        }

        chipGroup.removeAllViews()
        chipGroup.addView(chip)

        return convertView
    }

    // Method to get selected items
    fun getSelectedItems(): Set<String> {
        return selectedItems
    }
}