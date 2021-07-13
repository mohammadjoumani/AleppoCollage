package com.example.aleppocollage.ui.markStudent.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.ItemRecyclerAbsenceBinding
import com.example.aleppocollage.databinding.ItemRecyclerMarkBinding
import com.example.aleppocollage.model.mark.domain.Mark

class MarkRecyclerAdapter(
    private var marks: List<Mark>,
    private var nameTest: List<String>,
    private val context: Context?
) : RecyclerView.Adapter<MarkRecyclerAdapter.MarkViewHolder>() {


    inner class MarkViewHolder(val binding: ItemRecyclerMarkBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkViewHolder {
        val view =
            LayoutInflater.from(parent.context)
        return MarkViewHolder(ItemRecyclerMarkBinding.inflate(view, parent, false))
    }

    override fun getItemCount(): Int {
        return marks.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MarkViewHolder, position: Int) {
        for (i in 0..10) {
            if (i + 1 == TextUtils.substring(marks[holder.adapterPosition].examName, 9, 10).toInt())
                holder.binding.txtRecyclerMarkExamNumber.text = nameTest[i]
        }
        holder.binding.txtRecyclerMarkExamDate.text = marks[holder.adapterPosition].date
        if (marks[holder.adapterPosition].description.length>12)
            holder.binding.txtRecyclerMarkExamInfo.text = TextUtils.substring(marks[holder.adapterPosition].description ,0,6)+"..."
        else
            holder.binding.txtRecyclerMarkExamInfo.text = marks[holder.adapterPosition].description
        holder.binding.txtRecyclerMarkExamGrade.text = marks[holder.adapterPosition].grade.toString()
        holder.binding.txtRecyclerMarkExamMaxMark.text = marks[holder.adapterPosition].maxMark.toString()
    }

    fun setData(marks: List<Mark>) {
        this.marks = marks
        notifyDataSetChanged()
    }
}