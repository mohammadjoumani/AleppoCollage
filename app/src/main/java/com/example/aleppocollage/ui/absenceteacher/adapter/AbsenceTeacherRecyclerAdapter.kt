package com.example.aleppocollage.ui.absenceteacher.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.databinding.ItemRecyclerAbsenceBinding
import com.example.aleppocollage.listener.AbsenceListener
import com.example.aleppocollage.model.absence.domain.Absence

class AbsenceTeacherRecyclerAdapter(
    private var absences: List<Absence>,
    private val context: Context?,
    private val abseceListner: AbsenceListener) :
    RecyclerView.Adapter<AbsenceTeacherRecyclerAdapter.AbsenceTeacherViewHolder>() {

    private var sessions = ArrayList<String>()

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
        holder.binding.txtRecyclerAbsenceName.text = absences[holder.adapterPosition].student
        holder.binding.edtRecyclerAbsenceNote.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (charSequence.toString() == "" || charSequence == null) {
                    absences[holder.adapterPosition].note = "لا توجد ملاحظات"
                    abseceListner.onAbsenceChange(absences)
                } else {
                    absences[holder.adapterPosition].note = charSequence.toString()
                    abseceListner.onAbsenceChange(absences)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        holder.binding.tbgRecyclerAbsenceSessions.setOnSelectListener {
            if (holder.binding.btnRecyclerAbsenceSession1.isSelected) {
                absences[holder.adapterPosition].session0 = "1"
                abseceListner.onAbsenceChange(absences)
            } else {
                absences[holder.adapterPosition].session0 = "0"
                abseceListner.onAbsenceChange(absences)

            }
            if (holder.binding.btnRecyclerAbsenceSession2.isSelected) {
                absences[holder.adapterPosition].session1 = "1"
                abseceListner.onAbsenceChange(absences)
            } else {
                absences[holder.adapterPosition].session1 = "0"
                abseceListner.onAbsenceChange(absences)
            }

            if (holder.binding.btnRecyclerAbsenceSession3.isSelected) {
                absences[holder.adapterPosition].session2 = "1"
                abseceListner.onAbsenceChange(absences)
            } else {
                absences[holder.adapterPosition].session2 = "0"
                abseceListner.onAbsenceChange(absences)
            }

            if (holder.binding.btnRecyclerAbsenceSession4.isSelected) {
                absences[holder.adapterPosition].session3 = "1"
                abseceListner.onAbsenceChange(absences)
            } else {
                absences[holder.adapterPosition].session3 = "0"
                abseceListner.onAbsenceChange(absences)
            }

            if (holder.binding.btnRecyclerAbsenceSession5.isSelected) {
                absences[holder.adapterPosition].session4 = "1"
                abseceListner.onAbsenceChange(absences)
            } else {
                absences[holder.adapterPosition].session4 = "0"
                abseceListner.onAbsenceChange(absences)
            }

            if (holder.binding.btnRecyclerAbsenceSession6.isSelected) {
                absences[holder.adapterPosition].session5 = "1"
                abseceListner.onAbsenceChange(absences)
            } else {
                absences[holder.adapterPosition].session5 = "0"
                abseceListner.onAbsenceChange(absences)
            }

            if (holder.binding.btnRecyclerAbsenceSession7.isSelected) {
                absences[holder.adapterPosition].session6 = "1"
                abseceListner.onAbsenceChange(absences)
            } else {
                absences[holder.adapterPosition].session6 = "0"
                abseceListner.onAbsenceChange(absences)
            }

            if (holder.binding.btnRecyclerAbsenceSession8.isSelected) {
                absences[holder.adapterPosition].session7 = "1"
                abseceListner.onAbsenceChange(absences)
            } else {
                absences[holder.adapterPosition].session7 = "0"
                abseceListner.onAbsenceChange(absences)
            }

            if (holder.binding.btnRecyclerAbsenceSession9.isSelected) {
                absences[holder.adapterPosition].session8 = "1"
                abseceListner.onAbsenceChange(absences)
            } else {
                absences[holder.adapterPosition].session8 = "0"
                abseceListner.onAbsenceChange(absences)
            }
        }
    }

    fun setData(absences: List<Absence>) {
        this.absences = absences
        notifyDataSetChanged()
    }
}