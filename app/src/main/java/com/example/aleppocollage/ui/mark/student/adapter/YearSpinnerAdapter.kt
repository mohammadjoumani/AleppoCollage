package com.example.aleppocollage.ui.mark.student.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.aleppocollage.R

class YearSpinnerAdapter(context: Context, years: List<String>) :
    ArrayAdapter<String>(context, 0, years) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {

        val year = getItem(position)

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_spinner_year, parent, false)
        val txtSpinnerMarkFragmentYear =
            view.findViewById<TextView>(R.id.txtSpinnerMarkFragmentYear)
        txtSpinnerMarkFragmentYear.text = year.toString()

        return view
    }
}