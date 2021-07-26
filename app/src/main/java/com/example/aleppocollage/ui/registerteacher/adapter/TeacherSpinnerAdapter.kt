package com.example.aleppocollage.ui.registerteacher.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.aleppocollage.R
import com.example.aleppocollage.model.user.domain.Teacher

class TeacherSpinnerAdapter(context: Context, teachers: List<Teacher>) :
    ArrayAdapter<Teacher>(context, 0, teachers) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {

        val teacher = getItem(position)

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_spinner_year, parent, false)
        val txtSpinnerMarkFragmentYear =
            view.findViewById<TextView>(R.id.txtSpinnerMarkFragmentYear)
        if (teacher != null) {
            txtSpinnerMarkFragmentYear.text = teacher.name
        }

        return view
    }
}