package com.example.aleppocollage.ui.absenceTeacher.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.ItemRecyclerAbsenceBinding
import com.example.aleppocollage.model.absence.domain.Absence
import org.w3c.dom.Text

class AbsenceTeacherRecyclerAdapter(
    private var absences: List<Absence>,
    private val context: Context?
) : RecyclerView.Adapter<AbsenceTeacherRecyclerAdapter.AbsenceTeacherViewHolder>() {

    inner class AbsenceTeacherViewHolder(val binding: ItemRecyclerAbsenceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsenceTeacherViewHolder {
        val view = LayoutInflater.from(parent.context)
        return AbsenceTeacherViewHolder(ItemRecyclerAbsenceBinding.inflate(view, parent, false))
    }

    override fun getItemCount(): Int {
        return absences.size
    }

    override fun onBindViewHolder(holder: AbsenceTeacherViewHolder, position: Int) {
        holder.binding.txtRecyclerAbsenceName.text=absences[holder.adapterPosition].student
    }

    fun setData(absences: List<Absence>) {
        this.absences = absences
        notifyDataSetChanged()
    }
}