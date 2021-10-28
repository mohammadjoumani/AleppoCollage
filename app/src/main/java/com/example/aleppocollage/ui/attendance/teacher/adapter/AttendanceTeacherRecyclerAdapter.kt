package com.example.aleppocollage.ui.attendance.teacher.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.ItemRecyclerAttendanceBinding
import com.example.aleppocollage.model.attendance.domain.Attendance

class AttendanceTeacherRecyclerAdapter(
    private val listener: (List<Attendance>) -> Unit,
    private var attendances: List<Attendance>,
    private val context: Context,
) : RecyclerView.Adapter<AttendanceTeacherRecyclerAdapter.AttendanceTeacherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceTeacherViewHolder {
        val binding = ItemRecyclerAttendanceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return AttendanceTeacherViewHolder(listener, binding, context, this)
    }

    override fun getItemCount(): Int {
        return attendances.size
    }

    override fun onBindViewHolder(holder: AttendanceTeacherViewHolder, position: Int) {

        holder.bind(attendances[position])

    }

    class AttendanceTeacherViewHolder(
        private val listener: (List<Attendance>) -> Unit,
        val binding: ItemRecyclerAttendanceBinding,
        private val context: Context,
        val adapter: AttendanceTeacherRecyclerAdapter) : RecyclerView.ViewHolder(binding.root) {

        fun bind(itemEntity: Attendance) {

            val values: ArrayList<String?> = ArrayList()
            val sessions: ArrayList<TextView> = ArrayList()

             values.add(itemEntity.session0)
             values.add(itemEntity.session1)
             values.add(itemEntity.session2)
             values.add(itemEntity.session3)
             values.add(itemEntity.session4)
             values.add(itemEntity.session5)
             values.add(itemEntity.session6)
             values.add(itemEntity.session7)
             values.add(itemEntity.session8)
             values.add(itemEntity.session9)


            binding.apply {

                sessions.add(txtRecyclerAttendanceTeacherSession1)
                sessions.add(txtRecyclerAttendanceTeacherSession2)
                sessions.add(txtRecyclerAttendanceTeacherSession3)
                sessions.add(txtRecyclerAttendanceTeacherSession4)
                sessions.add(txtRecyclerAttendanceTeacherSession5)
                sessions.add(txtRecyclerAttendanceTeacherSession6)
                sessions.add(txtRecyclerAttendanceTeacherSession7)
                sessions.add(txtRecyclerAttendanceTeacherSession8)

                txtRecyclerAttendanceName.text = itemEntity.student
                edtRecyclerAttendanceNote.setText(itemEntity.note)

                if (itemEntity.note == null) {

                    edtRecyclerAttendanceNote.setText("")

                } else if (itemEntity.toString().trim().equals("")) {

                    edtRecyclerAttendanceNote.setText("")
                }

                edtRecyclerAttendanceNote.addTextChangedListener(object : TextWatcher {

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(
                        charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        itemEntity.note = charSequence.toString()
//                        adapter.notifyItemChanged(layoutPosition)
//                        adapter.saveNote(charSequence.toString(),this@AttendanceTeacherViewHolder,itemEntity)
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }
                })

                for (i in 0 until sessions.size) {

                    sessions[i].setOnClickListener {

                        sessions[i].animation = AnimationUtils.loadAnimation(context, R.anim.anim_bubble_animation)

                        adapter.changeState(sessions[i],this@AttendanceTeacherViewHolder, itemEntity)

                    }

                    if (values[i] == null) {

                        sessions[i].setBackgroundResource(R.drawable.bg_gray)
                        sessions[i].setTextColor(getColor(context,R.color.colorBlack))

                    } else if (values[i] == "P") {

                        sessions[i].setBackgroundResource(R.drawable.bg_rounded_primary)
                        sessions[i].setTextColor(getColor(context,R.color.colorWhite))

                    } else if (values[i] == "A") {

                        sessions[i].setBackgroundResource(R.drawable.bg_rounded_red)
                        sessions[i].setTextColor(getColor(context,R.color.colorWhite))

                    } else if (values[i] == "N") {

                        sessions[i].setBackgroundResource(R.drawable.bg_rounded_green)
                        sessions[i].setTextColor(getColor(context,R.color.colorWhite))

                    }

                }
            }
        }
    }

    private fun changeState(view: View, attendanceTeacherViewHolder: AttendanceTeacherViewHolder, attendance: Attendance) {

        val txt = view as TextView
        val sessionNo = txt.text.toString().toInt() - 1
        val values: ArrayList<String?> = ArrayList()

        values.add(attendance.session0)
        values.add(attendance.session1)
        values.add(attendance.session2)
        values.add(attendance.session3)
        values.add(attendance.session4)
        values.add(attendance.session5)
        values.add(attendance.session6)
        values.add(attendance.session7)
        values.add(attendance.session8)
        values.add(attendance.session9)

        val state = values[sessionNo]
        if (state == null) {
            values[sessionNo] = "P"
        } else if (state == "N") {
            values[sessionNo] = "P"
        } else if (state == "P") {
            values[sessionNo] = "A"
        } else if (state == "A") {
            values[sessionNo] = "N"
        }
        attendance.session0 = values[0]
        attendance.session1 = values[1]
        attendance.session2 = values[2]
        attendance.session3 = values[3]
        attendance.session4 = values[4]
        attendance.session5 = values[5]
        attendance.session6 = values[6]
        attendance.session7 = values[7]

        listener(attendances)
        notifyItemChanged(attendanceTeacherViewHolder.layoutPosition)
    }

//    fun saveNote(note: String?, attendanceTeacherViewHolder: AttendanceTeacherViewHolder, attendance:Attendance) {
//        attendance.note = note
//        notifyItemChanged(attendanceTeacherViewHolder.layoutPosition)
//    }
    fun setData(absences: List<Attendance>) {
        this.attendances = absences
        notifyDataSetChanged()
    }
}