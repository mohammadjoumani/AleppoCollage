package com.example.aleppocollage.ui.mark.student.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.databinding.ItemRecyclerMarkBinding
import com.example.aleppocollage.model.mark.domain.Mark

class MarkRecyclerAdapter(
    private var marks: List<Mark>,
    private var nameTest: List<String>,
) : RecyclerView.Adapter<MarkRecyclerAdapter.MarkViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkViewHolder {
        val binding =
            ItemRecyclerMarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarkViewHolder(binding, nameTest)
    }

    override fun getItemCount(): Int {
        return marks.size
    }

    override fun onBindViewHolder(holder: MarkViewHolder, position: Int) {

        val mark = marks[holder.layoutPosition]
        holder.bind(mark)
    }

    class MarkViewHolder(
        val binding: ItemRecyclerMarkBinding,
        private var nameTest: List<String>,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(itemEntity: Mark) {

            binding.apply {

                for (i in 0..10) {
                    if (i + 1 == TextUtils.substring(itemEntity.examName, 9, 10)
                            .toInt()
                    ) txtRecyclerMarkExamNumber.text = nameTest[i]
                }

                txtRecyclerMarkExamDate.text = itemEntity.date

                if (itemEntity.description.length > 12) txtRecyclerMarkExamInfo.text =
                    TextUtils.substring(itemEntity.description, 0, 6) + "..."
                else txtRecyclerMarkExamInfo.text = itemEntity.description

                txtRecyclerMarkExamGrade.text = itemEntity.grade.toString()

            }

        }
    }

    fun setData(marks: List<Mark>) {
        this.marks = marks
        notifyDataSetChanged()
    }
}