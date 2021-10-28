package com.example.aleppocollage.ui.exam.teacher.get.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.ItemRecyclerRecordExamBinding
import com.example.aleppocollage.model.exam.domain.Exam

class ExamRecyclerAdapter(
    private var exams: List<Exam>,
    private val context: Context,
    private val clickListener: (Exam) -> Unit
) : RecyclerView.Adapter<ExamRecyclerAdapter.TestTestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestTestViewHolder {
        val binding =
            ItemRecyclerRecordExamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TestTestViewHolder(binding, context, clickListener)
    }

    override fun getItemCount(): Int {
        return exams.size
    }

    override fun onBindViewHolder(holder: TestTestViewHolder, position: Int) {

        holder.bind(exams[holder.layoutPosition])
    }

    class TestTestViewHolder(
        private val binding: ItemRecyclerRecordExamBinding,
        private val context: Context,
        private val clickListener: (Exam) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(itemEntity: Exam) {
                binding.apply {

                    txtRecyclerRecordExamTitle.text = itemEntity.name

                    txtRecyclerRecordExamDate.text = if (itemEntity.date == "") "لايوجد تاريخ بعد" else itemEntity.date

                    if (itemEntity.status == "Delivered") {

                        relativeRecyclerRecordExamBackGround.setBackgroundColor(getColor(context, R.color.colorGreenLight))

                    } else if (itemEntity.status == "Checked") {

                        relativeRecyclerRecordExamBackGround.setBackgroundColor(getColor(context, R.color.purple_500))

                    } else if (itemEntity.status == "New") {

                        relativeRecyclerRecordExamBackGround.setBackgroundColor(getColor(context, R.color.colorPrimary))

                    }

                    root.setOnClickListener {
                        clickListener(itemEntity)
                    }
                }

            }
        }

    fun setData(exams: List<Exam>) {
        this.exams = exams
        notifyDataSetChanged()
    }
}