package com.example.aleppocollage.ui.mark.student

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.FragmentMarkBinding
import com.example.aleppocollage.model.mark.domain.Mark
import com.example.aleppocollage.ui.main.model.ProfileInfo
import com.example.aleppocollage.ui.mark.student.adapter.MarkRecyclerAdapter
import com.example.aleppocollage.ui.mark.student.adapter.YearSpinnerAdapter
import com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModel
import com.example.aleppocollage.util.Common
import com.example.aleppocollage.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MarkFragment : Fragment(R.layout.fragment_mark) {

    ///region ViewModel & Binding

    private val viewModel: MarkViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentMarkBinding? = null
    private val binding get() = _binding!!

    ///endregion

    ///region Init List & Variables

    private var years: ArrayList<String> = ArrayList()
    private var cardMonth: ArrayList<CardView> = ArrayList()
    private var linearMonth: ArrayList<LinearLayout> = ArrayList()
    private var textNameMonth: ArrayList<TextView> = ArrayList()
    private var textNumberMonth: ArrayList<TextView> = ArrayList()
    private var relativeMonth: ArrayList<LinearLayout> = ArrayList()

    private lateinit var markRecyclerAdapter: MarkRecyclerAdapter
    private var marks: ArrayList<Mark> = ArrayList()
    private var nameTest: ArrayList<String> = ArrayList()
    private var year = ""

    //endregion

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentMarkBinding.bind(view)

        init()

        binding.apply {

            cardMarkFragmentBack.setOnClickListener {
                findNavController().navigateUp()
            }

            cardMarkFragmentProfile.setOnClickListener {

                val state = !(sharedViewModel.showProfileInfo.value!!.state)
                sharedViewModel.showProfileInfo.value = ProfileInfo(userType = Common.getCurrentTypeUser(),Common.getCurrentStudent()!!, state = state)
            }

            swipeRefreshMarkFragmentStudent.setOnRefreshListener {
                for (i in 0..11) {
                    linearMonth[i].setBackgroundColor(
                        getColor(
                            requireContext(),
                            R.color.colorWhite))
                    textNameMonth[i].setTextColor(getColor(requireContext(), R.color.colorPrimary))
                    textNumberMonth[i].setTextColor(getColor(requireContext(), R.color.colorWhite))
                    relativeMonth[i].setBackgroundResource(R.drawable.ic_ellipse_purple)
                    recyclerMarkFragment.visibility = View.GONE
                    imgMarkFragmentChoseDate.visibility = View.VISIBLE
                    imgMarkFragmentChoseDate.setImageResource(R.drawable.ic_aplus)
                    swipeRefreshMarkFragmentStudent.isRefreshing = false
                }
            }

            spinnerMarkFragmentYear.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }

                    override fun onItemSelected(
                        adapterView: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long) {
                        year = years[position]
                    }
                }

        }

        ///region Choose Month
        for (i in 0..11) {
            cardMonth[i].setOnClickListener {
                val student = Common.getCurrentStudent()
                observeMarkDataState()
                viewModel.setStateEvent(
                    MarkStateEvent.Mark,
                    student!!.id,
                    student.groupID,
                    i + 1,
                    "$year-${year.toInt() + 1}")

                cardMonth[i].animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.anim_bubble_animation)

                when (i) {
                    0 -> {
                        binding.txtMarkFragmentNamePlus1.setTextColor(
                            getColor(
                                requireContext(),
                                R.color.colorWhite))
                    }

                    9 -> {
                        binding.txtMarkFragmentNamePlus10.setTextColor(
                            getColor(
                                requireContext(),
                                R.color.colorWhite))
                    }

                    10 -> {
                        binding.txtMarkFragmentNamePlus11.setTextColor(
                            getColor(
                                requireContext(),
                                R.color.colorWhite))
                    }

                    11 -> {
                        binding.txtMarkFragmentNamePlus12.setTextColor(
                            getColor(
                                requireContext(),
                                R.color.colorWhite))
                    }
                }
                linearMonth[i].setBackgroundColor(getColor(requireContext(), R.color.colorPrimary))
                textNameMonth[i].setTextColor(getColor(requireContext(), R.color.colorWhite))
                textNumberMonth[i].setTextColor(getColor(requireContext(), R.color.colorPrimary))
                relativeMonth[i].setBackgroundResource(R.drawable.ic_ellipse_white)

                for (j in 0..11) {
                    if (i == j) continue
                    if (j == 0 && i != j) {
                        binding.txtMarkFragmentNamePlus1.setTextColor(
                            getColor(
                                requireContext(),
                                R.color.colorPrimary))
                    } else if (j == 9 && i != j) {
                        binding.txtMarkFragmentNamePlus10.setTextColor(
                            getColor(
                                requireContext(),
                                R.color.colorPrimary))
                    } else if (j == 10 && i != j) {
                        binding.txtMarkFragmentNamePlus11.setTextColor(
                            getColor(
                                requireContext(),
                                R.color.colorPrimary))
                    } else if (j == 11 && i != j) {
                        binding.txtMarkFragmentNamePlus12.setTextColor(
                            getColor(
                                requireContext(),
                                R.color.colorPrimary))
                    }
                    linearMonth[j].setBackgroundColor(
                        getColor(
                            requireContext(),
                            R.color.colorWhite))
                    textNameMonth[j].setTextColor(getColor(requireContext(), R.color.colorPrimary))
                    textNumberMonth[j].setTextColor(getColor(requireContext(), R.color.colorWhite))
                    relativeMonth[j].setBackgroundResource(R.drawable.ic_ellipse_purple)

                }
            }
        }
        //endregion

    }

    private fun init() {
        bindMonth()
        setNameTest()

        markRecyclerAdapter = MarkRecyclerAdapter(marks, nameTest)
        initRecyclerAndAdapter()

        setYear(Common.getCurrentYear())
        val adapter = YearSpinnerAdapter(requireContext(), years)
        binding.spinnerMarkFragmentYear.adapter = adapter
    }

    private fun initRecyclerAndAdapter() {
        binding.apply {
            recyclerMarkFragment.setHasFixedSize(true)
            recyclerMarkFragment.adapter = markRecyclerAdapter
            recyclerMarkFragment.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    private fun observeMarkDataState() {
        viewModel.markDataState.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Loading -> {
                    binding.apply {
                        swipeRefreshMarkFragmentStudent.isRefreshing = true
                        shimmerAnimationMarkFragment.isVisible = true
                        recyclerMarkFragment.isVisible = false
                        txtMarkFragmentChoseDate.isVisible = false
                        imgMarkFragmentChoseDate.isVisible = false
                    }
                }
                is DataState.Success -> {

                    marks = it.data as ArrayList<Mark>

                    markRecyclerAdapter.setData(it.data)

                    binding.apply {
                        swipeRefreshMarkFragmentStudent.isRefreshing = false
                        shimmerAnimationMarkFragment.isVisible = false
                        recyclerMarkFragment.isVisible = true
                        txtMarkFragmentChoseDate.isVisible = false
                        imgMarkFragmentChoseDate.isVisible = false
                    }

                    viewModel.setStateEvent(MarkStateEvent.None)
                }
                is DataState.Failure -> {

                    binding.apply {
                        swipeRefreshMarkFragmentStudent.isRefreshing = false
                        shimmerAnimationMarkFragment.isVisible = false
                        recyclerMarkFragment.isVisible = false
                        txtMarkFragmentChoseDate.isVisible = true
                        imgMarkFragmentChoseDate.isVisible = true
                        imgMarkFragmentChoseDate.setImageResource(R.drawable.ic_no_data)
                    }

                    viewModel.setStateEvent(MarkStateEvent.None)
                }
                is DataState.ExceptionState -> {
                    binding.apply {
                        swipeRefreshMarkFragmentStudent.isRefreshing = false
                        shimmerAnimationMarkFragment.isVisible = false
                        recyclerMarkFragment.isVisible = false
                        txtMarkFragmentChoseDate.isVisible = false
                        imgMarkFragmentChoseDate.isVisible = false
                    }
                    viewModel.setStateEvent(MarkStateEvent.None)
                    Common.showSnackBar(requireContext(), binding.root, "$it")
                }
            }
        }
    }

    private fun bindMonth() {
        setRelativeMonth()
        setTextNumberMonth()
        setTextNameMonth()
        setLinearMonth()
        setCardMonth()
    }

    private fun setRelativeMonth() {
        binding.apply {
            relativeMonth.add(relativeMarkFragmentShap1)
            relativeMonth.add(relativeMarkFragmentShap2)
            relativeMonth.add(relativeMarkFragmentShap3)
            relativeMonth.add(relativeMarkFragmentShap4)
            relativeMonth.add(relativeMarkFragmentShap5)
            relativeMonth.add(relativeMarkFragmentShap6)
            relativeMonth.add(relativeMarkFragmentShap7)
            relativeMonth.add(relativeMarkFragmentShap8)
            relativeMonth.add(relativeMarkFragmentShap9)
            relativeMonth.add(relativeMarkFragmentShap10)
            relativeMonth.add(relativeMarkFragmentShap11)
            relativeMonth.add(relativeMarkFragmentShap12)
        }
    }

    private fun setTextNumberMonth() {
        binding.apply {
            textNumberMonth.add(txtMarkFragmentNumber1)
            textNumberMonth.add(txtMarkFragmentNumber2)
            textNumberMonth.add(txtMarkFragmentNumber3)
            textNumberMonth.add(txtMarkFragmentNumber4)
            textNumberMonth.add(txtMarkFragmentNumber5)
            textNumberMonth.add(txtMarkFragmentNumber6)
            textNumberMonth.add(txtMarkFragmentNumber7)
            textNumberMonth.add(txtMarkFragmentNumber8)
            textNumberMonth.add(txtMarkFragmentNumber9)
            textNumberMonth.add(txtMarkFragmentNumber10)
            textNumberMonth.add(txtMarkFragmentNumber11)
            textNumberMonth.add(txtMarkFragmentNumber12)
        }
    }

    private fun setTextNameMonth() {
        binding.apply {
            textNameMonth.add(txtMarkFragmentName1)
            textNameMonth.add(txtMarkFragmentName2)
            textNameMonth.add(txtMarkFragmentName3)
            textNameMonth.add(txtMarkFragmentName4)
            textNameMonth.add(txtMarkFragmentName5)
            textNameMonth.add(txtMarkFragmentName6)
            textNameMonth.add(txtMarkFragmentName7)
            textNameMonth.add(txtMarkFragmentName8)
            textNameMonth.add(txtMarkFragmentName9)
            textNameMonth.add(txtMarkFragmentName10)
            textNameMonth.add(txtMarkFragmentName11)
            textNameMonth.add(txtMarkFragmentName12)
        }
    }

    private fun setLinearMonth() {
        binding.apply {
            linearMonth.add(linearMarkFragment1)
            linearMonth.add(linearMarkFragment2)
            linearMonth.add(linearMarkFragment3)
            linearMonth.add(linearMarkFragment4)
            linearMonth.add(linearMarkFragment5)
            linearMonth.add(linearMarkFragment6)
            linearMonth.add(linearMarkFragment7)
            linearMonth.add(linearMarkFragment8)
            linearMonth.add(linearMarkFragment9)
            linearMonth.add(linearMarkFragment10)
            linearMonth.add(linearMarkFragment11)
            linearMonth.add(linearMarkFragment12)
        }
    }

    private fun setCardMonth() {
        binding.apply {
            cardMonth.add(cardMarkFragment1)
            cardMonth.add(cardMarkFragment2)
            cardMonth.add(cardMarkFragment3)
            cardMonth.add(cardMarkFragment4)
            cardMonth.add(cardMarkFragment5)
            cardMonth.add(cardMarkFragment6)
            cardMonth.add(cardMarkFragment7)
            cardMonth.add(cardMarkFragment8)
            cardMonth.add(cardMarkFragment9)
            cardMonth.add(cardMarkFragment10)
            cardMonth.add(cardMarkFragment11)
            cardMonth.add(cardMarkFragment12)
        }
    }

    private fun setYear(nowYear: String) {
        for (i in nowYear.toInt() downTo 2019) {
            years.add("$i")
        }
    }

    private fun setNameTest() {
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