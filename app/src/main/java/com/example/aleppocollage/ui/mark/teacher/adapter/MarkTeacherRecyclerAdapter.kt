package com.example.aleppocollage.ui.mark.teacher.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.ItemRecyclerMarkTeacherBinding
import com.example.aleppocollage.model.groupstudent.domain.GroupStudent

class MarkTeacherRecyclerAdapter(
    private val clickListener: (GroupStudent) -> Unit,
    private var groupStudents: List<GroupStudent>,
    private val maxMark: Int,
) : RecyclerView.Adapter<MarkTeacherRecyclerAdapter.StudentMarkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentMarkViewHolder {
        val binding =
            ItemRecyclerMarkTeacherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentMarkViewHolder(binding, clickListener, maxMark)
    }

    override fun getItemCount(): Int {
        return groupStudents.size
    }

    override fun onBindViewHolder(holder: StudentMarkViewHolder, position: Int) {

        holder.bind(groupStudents[holder.layoutPosition])
    }

    class StudentMarkViewHolder(
        val binding: ItemRecyclerMarkTeacherBinding,
        private val clickListener: (GroupStudent) -> Unit,
        private val maxMark: Int,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(itemEntity: GroupStudent) {

                binding.apply {
                    txtRecyclerMarkTeacherName.text = itemEntity.studentName
                    txtRecyclerMarkTeacherMark.text = itemEntity.grade.toString()

                    if (itemEntity.grade < maxMark/2) {
                        viewRecyclerMarkTeacherState.setBackgroundResource(R.drawable.bg_rounded_red)
                    } else {
                        viewRecyclerMarkTeacherState.setBackgroundResource(R.drawable.bg_rounded_green)
                    }

                    root.setOnClickListener {
                        clickListener(itemEntity)
                    }
                }
            }
        }

    fun setData(groupStudents: List<GroupStudent>) {
        this.groupStudents = groupStudents
        notifyDataSetChanged()
    }
}