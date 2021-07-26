package com.example.aleppocollage.ui.testteacher.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.databinding.ItemRecyclerAbsenceBinding

class TestRecyclerAdapter() :
    RecyclerView.Adapter<TestRecyclerAdapter.TestTeacherViewHolder>() {

    inner class TestTeacherViewHolder(val binding: ItemRecyclerAbsenceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestTeacherViewHolder {
        val view = LayoutInflater.from(parent.context)
        return TestTeacherViewHolder(ItemRecyclerAbsenceBinding.inflate(view, parent, false))
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun onBindViewHolder(holder: TestTeacherViewHolder, position: Int) {

    }

//    fun setData(absences: List<Absence>) {
//        this.absences = absences
//        notifyDataSetChanged()
//    }
}