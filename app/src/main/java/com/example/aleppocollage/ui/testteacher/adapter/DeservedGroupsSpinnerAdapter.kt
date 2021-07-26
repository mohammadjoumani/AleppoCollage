package com.example.aleppocollage.ui.testteacher.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.aleppocollage.R
import com.example.aleppocollage.model.test.domain.DeservedGroup

class DeservedGroupsSpinnerAdapter(context: Context, groups: List<DeservedGroup>) :
    ArrayAdapter<DeservedGroup>(context, 0, groups) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {

        val group = getItem(position)

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_spinner_year, parent, false)
        val txtSpinnerMarkFragmentYear =
            view.findViewById<TextView>(R.id.txtSpinnerMarkFragmentYear)
        if (group != null) {
            txtSpinnerMarkFragmentYear.text = group.groupName
        }

        return view
    }
}