package com.cabbagebeyond.ui

import android.content.Context
import android.view.View
import android.widget.TextView

import android.view.ViewGroup

import android.widget.ArrayAdapter
import com.cabbagebeyond.model.World


class WorldSpinnerAdapter(context: Context, textViewResourceId: Int, private val values: Array<World>) : ArrayAdapter<World>(context, textViewResourceId, values) {

    override fun getCount(): Int {
        return values.size
    }

    override fun getItem(position: Int): World {
        return values[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        label.text = values[position].name
        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.text = values[position].name
        return label
    }

}