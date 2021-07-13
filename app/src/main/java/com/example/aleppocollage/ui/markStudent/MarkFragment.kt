package com.example.aleppocollage.ui.markStudent

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.lifecycle.Observer
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.FragmentMarkBinding
import com.example.aleppocollage.model.mark.domain.Mark
import com.example.aleppocollage.model.user.domain.Student
import com.example.aleppocollage.ui.markStudent.adapter.MarkRecyclerAdapter
import com.example.aleppocollage.ui.markStudent.adapter.YearSpinnerAdapter
import com.example.aleppocollage.util.Status
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.getInstance
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MarkFragment : Fragment(R.layout.fragment_mark) {

    private lateinit var binding: FragmentMarkBinding
    private val markViewModel by viewModels<MarkViewModel>()
    //private val markViewModel by viewModels<MarkViewModel>()
    //private val markViewModel: MarkViewModel by viewModels()
    private var years: ArrayList<String>? = null
    private var cardMonth: ArrayList<CardView>? = null
    private var linearMonth: ArrayList<LinearLayout>? = null
    private var textNameMonth: ArrayList<TextView>? = null
    private var textNumberMonth: ArrayList<TextView>? = null
    private var relativeMonth: ArrayList<RelativeLayout>? = null

    companion object {
        lateinit var markRecyclerAdapter: MarkRecyclerAdapter
        lateinit var marks: ArrayList<Mark>
        lateinit var nameTest: ArrayList<String>
        private lateinit var year: String
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMarkBinding.bind(view)

        ///region recycler marks
        marks = ArrayList()
        nameTest= ArrayList()
        setNameTest()
        binding.recyclerMarkFragment.setHasFixedSize(true)
        val layoutManagerMyEstate = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.recyclerMarkFragment.layoutManager = layoutManagerMyEstate
        markRecyclerAdapter = MarkRecyclerAdapter(marks, nameTest,context)
        binding.recyclerMarkFragment.adapter = markRecyclerAdapter
        //endregion

        ///region spinner Years
        val date: Date = getInstance().time
        val df = SimpleDateFormat("yyyy", Locale.getDefault())
        val formattedDate: String = df.format(date)
        years = ArrayList()
        setYear(formattedDate.toInt())

        val adapter = activity?.applicationContext?.let {
            YearSpinnerAdapter(it, years!!)
        }
        binding.spinnerMarkFragmentYear.adapter = adapter

        binding.spinnerMarkFragmentYear.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(adapterView: AdapterView<*>?) {

                }
                override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    year = adapterView?.getItemAtPosition(position).toString()
                }
            }
        //endregion

        ///region SwipeRefresh
        binding.swipeRefreshMarkFragmentStudent.setOnRefreshListener {
            for (i in 0..11) {
                linearMonth!![i].setBackgroundColor(resources.getColor(R.color.colorWhite))
                textNameMonth!![i].setTextColor(resources.getColor(R.color.colorPurple))
                textNumberMonth!![i].setTextColor(resources.getColor(R.color.colorWhite))
                relativeMonth!![i].setBackgroundResource(R.drawable.ic_ellipse_purple)
                binding.recyclerMarkFragment.visibility = View.GONE
                binding.imgMarkFragmentChoseDate.visibility = View.VISIBLE
                binding.imgMarkFragmentChoseDate.setImageResource(R.drawable.ic_aplus)
                binding.swipeRefreshMarkFragmentStudent.isRefreshing = false
            }
        }
        //endregion

        ///region Chose Month
        bindMonth()
        for (i in 0..11) {
            cardMonth!![i].setOnClickListener {
                val student = Paper.book().read<Student>("Student")
                getMarkStudent(student.id, student.groupID, i + 1, "$year-${year.toInt() + 1}")
                cardMonth!![i].animation =
                    AnimationUtils.loadAnimation(context, R.anim.bubble_animation)
                when (i) {
                    0 -> {
                        binding.txtMarkFragmentNamePlus1.setTextColor(resources.getColor(R.color.colorWhite))
                    }
                    9 -> {
                        binding.txtMarkFragmentNamePlus10.setTextColor(resources.getColor(R.color.colorWhite))
                    }
                    10 -> {
                        binding.txtMarkFragmentNamePlus11.setTextColor(resources.getColor(R.color.colorWhite))
                    }
                    11 -> {
                        binding.txtMarkFragmentNamePlus12.setTextColor(resources.getColor(R.color.colorWhite))
                    }
                }
                linearMonth!![i].setBackgroundColor(resources.getColor(R.color.colorPurple))
                textNameMonth!![i].setTextColor(resources.getColor(R.color.colorWhite))
                textNumberMonth!![i].setTextColor(resources.getColor(R.color.colorPurple))
                relativeMonth!![i].setBackgroundResource(R.drawable.ic_ellipse_white)

                for (j in 0..11) {
                    if (i == j)
                        continue
                    if (j == 0 && i != j) {
                        binding.txtMarkFragmentNamePlus1.setTextColor(resources.getColor(R.color.colorPurple))
                    } else if (j == 9 && i != j) {
                        binding.txtMarkFragmentNamePlus10.setTextColor(resources.getColor(R.color.colorPurple))
                    } else if (j == 10 && i != j) {
                        binding.txtMarkFragmentNamePlus11.setTextColor(resources.getColor(R.color.colorPurple))
                    } else if (j == 11 && i != j) {
                        binding.txtMarkFragmentNamePlus12.setTextColor(resources.getColor(R.color.colorPurple))
                    }
                    linearMonth!![j].setBackgroundColor(resources.getColor(R.color.colorWhite))
                    textNameMonth!![j].setTextColor(resources.getColor(R.color.colorPurple))
                    textNumberMonth!![j].setTextColor(resources.getColor(R.color.colorWhite))
                    relativeMonth!![j].setBackgroundResource(R.drawable.ic_ellipse_purple)

                }
            }
        }
        //endregion
    }

    private fun getMarkStudent(studentID: Int, groupID: Int, month: Int, studyYear: String) {
//        val markViewModel = MarkViewModel()
        markViewModel.getMarkStudent(studentID, groupID, month, studyYear)
        markViewModel.markStudents.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.swipeRefreshMarkFragmentStudent.isRefreshing = true
                    binding.shimmerAnimationMarkFragment.visibility = View.VISIBLE
                    binding.recyclerMarkFragment.visibility = View.GONE
                    binding.txtMarkFragmentChoseDate.visibility = View.GONE
                    binding.imgMarkFragmentChoseDate.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    marks = it._data!!
                    if (marks.size != 0) {
                        markRecyclerAdapter.setData(marks)
                        binding.recyclerMarkFragment.visibility = View.VISIBLE
                        binding.txtMarkFragmentChoseDate.visibility = View.GONE
                        binding.imgMarkFragmentChoseDate.visibility = View.GONE
                    } else {
                        binding.recyclerMarkFragment.visibility = View.GONE
                        binding.imgMarkFragmentChoseDate.visibility = View.VISIBLE
                        binding.txtMarkFragmentChoseDate.visibility = View.VISIBLE
                        binding.imgMarkFragmentChoseDate.setImageResource(R.drawable.ic_no_data)
                    }
                    binding.swipeRefreshMarkFragmentStudent.isRefreshing = false
                    binding.shimmerAnimationMarkFragment.visibility = View.GONE
                }

                Status.FAILURE -> {
                    Log.d("stateMark", "Failure")
                    binding.shimmerAnimationMarkFragment.visibility = View.GONE
                    binding.swipeRefreshMarkFragmentStudent.isRefreshing = false
                }

                Status.ERROR -> {
                    Log.d("stateMark", "ERROR")
                }
            }
        })
    }

    private fun bindMonth() {
        setRelativeMonth()
        setTextNumberMonth()
        setTextNameMonth()
        setLinearMonth()
        setCardMonth()
    }

    private fun setRelativeMonth() {
        relativeMonth = ArrayList()
        relativeMonth?.add(binding.relativeMarkFragmentShap1)
        relativeMonth?.add(binding.relativeMarkFragmentShap2)
        relativeMonth?.add(binding.relativeMarkFragmentShap3)
        relativeMonth?.add(binding.relativeMarkFragmentShap4)
        relativeMonth?.add(binding.relativeMarkFragmentShap5)
        relativeMonth?.add(binding.relativeMarkFragmentShap6)
        relativeMonth?.add(binding.relativeMarkFragmentShap7)
        relativeMonth?.add(binding.relativeMarkFragmentShap8)
        relativeMonth?.add(binding.relativeMarkFragmentShap9)
        relativeMonth?.add(binding.relativeMarkFragmentShap10)
        relativeMonth?.add(binding.relativeMarkFragmentShap11)
        relativeMonth?.add(binding.relativeMarkFragmentShap12)
    }

    private fun setTextNumberMonth() {
        textNumberMonth = ArrayList()
        textNumberMonth?.add(binding.txtMarkFragmentNumber1)
        textNumberMonth?.add(binding.txtMarkFragmentNumber2)
        textNumberMonth?.add(binding.txtMarkFragmentNumber3)
        textNumberMonth?.add(binding.txtMarkFragmentNumber4)
        textNumberMonth?.add(binding.txtMarkFragmentNumber5)
        textNumberMonth?.add(binding.txtMarkFragmentNumber6)
        textNumberMonth?.add(binding.txtMarkFragmentNumber7)
        textNumberMonth?.add(binding.txtMarkFragmentNumber8)
        textNumberMonth?.add(binding.txtMarkFragmentNumber9)
        textNumberMonth?.add(binding.txtMarkFragmentNumber10)
        textNumberMonth?.add(binding.txtMarkFragmentNumber11)
        textNumberMonth?.add(binding.txtMarkFragmentNumber12)
    }

    private fun setTextNameMonth() {
        textNameMonth = ArrayList()
        textNameMonth?.add(binding.txtMarkFragmentName1)
        textNameMonth?.add(binding.txtMarkFragmentName2)
        textNameMonth?.add(binding.txtMarkFragmentName3)
        textNameMonth?.add(binding.txtMarkFragmentName4)
        textNameMonth?.add(binding.txtMarkFragmentName5)
        textNameMonth?.add(binding.txtMarkFragmentName6)
        textNameMonth?.add(binding.txtMarkFragmentName7)
        textNameMonth?.add(binding.txtMarkFragmentName8)
        textNameMonth?.add(binding.txtMarkFragmentName9)
        textNameMonth?.add(binding.txtMarkFragmentName10)
        textNameMonth?.add(binding.txtMarkFragmentName11)
        textNameMonth?.add(binding.txtMarkFragmentName12)
    }

    private fun setLinearMonth() {
        linearMonth = ArrayList()
        linearMonth?.add(binding.linearMarkFragment1)
        linearMonth?.add(binding.linearMarkFragment2)
        linearMonth?.add(binding.linearMarkFragment3)
        linearMonth?.add(binding.linearMarkFragment4)
        linearMonth?.add(binding.linearMarkFragment5)
        linearMonth?.add(binding.linearMarkFragment6)
        linearMonth?.add(binding.linearMarkFragment7)
        linearMonth?.add(binding.linearMarkFragment8)
        linearMonth?.add(binding.linearMarkFragment9)
        linearMonth?.add(binding.linearMarkFragment10)
        linearMonth?.add(binding.linearMarkFragment11)
        linearMonth?.add(binding.linearMarkFragment12)
    }

    private fun setCardMonth() {
        cardMonth = ArrayList()
        cardMonth?.add(binding.cardMarkFragment1)
        cardMonth?.add(binding.cardMarkFragment2)
        cardMonth?.add(binding.cardMarkFragment3)
        cardMonth?.add(binding.cardMarkFragment4)
        cardMonth?.add(binding.cardMarkFragment5)
        cardMonth?.add(binding.cardMarkFragment6)
        cardMonth?.add(binding.cardMarkFragment7)
        cardMonth?.add(binding.cardMarkFragment8)
        cardMonth?.add(binding.cardMarkFragment9)
        cardMonth?.add(binding.cardMarkFragment10)
        cardMonth?.add(binding.cardMarkFragment11)
        cardMonth?.add(binding.cardMarkFragment12)
    }

    private fun setYear(nowYear: Int) {
        for (i in 2019..nowYear) {
            Log.d("year1", "$i")
            years?.add("$i")
        }
    }

    private fun setNameTest(){
        nameTest.add("الأولى")
        nameTest.add("الثانية")
        nameTest.add("الثالثة")
        nameTest.add("الرابعة")
        nameTest.add("الخامسة")
        nameTest.add("السادسة")
        nameTest.add("السابعة")
        nameTest.add("الثامنة")
        nameTest.add("التاسعة")
        nameTest.add("العاشرة")


    }

}