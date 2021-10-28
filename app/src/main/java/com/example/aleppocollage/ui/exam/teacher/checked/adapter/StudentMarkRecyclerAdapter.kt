package com.example.aleppocollage.ui.exam.teacher.checked.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.ItemRecyclerStudentMarkBinding
import com.example.aleppocollage.model.groupstudent.domain.GroupStudent

class StudentMarkRecyclerAdapter(
    private var groupStudents: List<GroupStudent>,
    private val maxMark: Int,
) : RecyclerView.Adapter<StudentMarkRecyclerAdapter.StudentMarkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentMarkViewHolder {
        val binding =
            ItemRecyclerStudentMarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentMarkViewHolder(binding, maxMark)
    }

    override fun getItemCount(): Int {
        return groupStudents.size
    }

    override fun onBindViewHolder(holder: StudentMarkViewHolder, position: Int) {

        holder.bind(groupStudents[holder.layoutPosition])
    }

    class StudentMarkViewHolder(
        val binding: ItemRecyclerStudentMarkBinding,
        private val maxMark: Int,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(itemEntity: GroupStudent) {

                binding.apply {
                    txtRecyclerStudentMarkName.text = itemEntity.studentName
                    txtRecyclerStudentMark.text = itemEntity.grade.toString()

                    if (itemEntity.grade < maxMark/2) {
                        viewRecyclerStudentMarkState.setBackgroundResource(R.drawable.bg_rounded_red)
                    } else {
                        viewRecyclerStudentMarkState.setBackgroundResource(R.drawable.bg_rounded_green)
                    }
                }
            }
        }

    fun setData(groupStudents: List<GroupStudent>) {
        this.groupStudents = groupStudents
        notifyDataSetChanged()
    }
}