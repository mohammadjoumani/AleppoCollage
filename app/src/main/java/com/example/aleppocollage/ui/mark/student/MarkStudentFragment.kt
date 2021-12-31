package com.example.aleppocollage.ui.mark.student

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.FragmentMarkStudentBinding
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
class MarkStudentFragment : Fragment(R.layout.fragment_mark_student) {

    ///region ViewModel & Binding

    private val viewModel: MarkViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentMarkStudentBinding? = null
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
    private var year = -1
    private var month = -1

    //endregion

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentMarkStudentBinding.bind(view)

        init()

        binding.apply {

            cardMarkStudentBack.setOnClickListener {
                findNavController().navigateUp()
            }

            cardMarkStudentProfile.setOnClickListener {

                val state = !(sharedViewModel.showProfileInfo.value!!.state)
                sharedViewModel.showProfileInfo.value = ProfileInfo(userType = Common.getCurrentTypeUser(),Common.getCurrentStudent()!!, state = state)
            }

            swipeRefreshMarkStudent.setColorSchemeColors(getColor(requireContext(), R.color.colorPrimary))

            swipeRefreshMarkStudent.setOnRefreshListener {
                observeMarkDataState()
                viewModel.setStateEvent(
                    MarkStateEvent.Mark,
                    studentId = Common.getCurrentStudent()!!.id,
                    groupId = Common.getCurrentStudent()!!.groupID,
                    month = month,
                    studyYear = "$year-${year + 1}"
                )
//                for (i in 0..11) {
//                    linearMonth[i].setBackgroundColor(
//                        getColor(
//                            requireContext(),
//                            R.color.colorWhite))
//                    textNameMonth[i].setTextColor(getColor(requireContext(), R.color.colorPrimary))
//                    textNumberMonth[i].setTextColor(getColor(requireContext(), R.color.colorWhite))
//                    relativeMonth[i].setBackgroundResource(R.drawable.ic_ellipse_purple)
//                    recyclerMarkStudent.visibility = View.GONE
//                    imgMarkStudentChoseDate.visibility = View.VISIBLE
//                    imgMarkStudentChoseDate.setImageResource(R.drawable.ic_aplus)
//                    swipeRefreshMarkStudentStudent.isRefreshing = false
//                }
            }

            spinnerMarkStudentYear.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }
                    override fun onItemSelected(
                        adapterView: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long) {
                        year = years[position].toInt()
                    }
                }

        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (sharedViewModel.showProfileInfo.value!!.state) {
                    sharedViewModel.showProfileInfo.value = ProfileInfo(userType = Common.getCurrentTypeUser(), student = Common.getCurrentStudent()!!, state = false)
                } else{
                    findNavController().navigateUp()
                }
            }
        })

        ///region Choose Month

        for (i in 0..11) {
            cardMonth[i].setOnClickListener {
                month = i + 1
                observeMarkDataState()
                viewModel.setStateEvent(
                    MarkStateEvent.Mark,
                    studentId = Common.getCurrentStudent()!!.id,
                    groupId = Common.getCurrentStudent()!!.groupID,
                    month = month,
                    studyYear = "$year-${year + 1}"
                )

                cardMonth[i].animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.anim_bubble_animation)

                when (i) {
                    0 -> {
                        binding.txtMarkStudentNamePlus1.setTextColor(
                            getColor(
                                requireContext(),
                                R.color.colorWhite))
                    }
                    9 -> {
                        binding.txtMarkStudentNamePlus10.setTextColor(
                            getColor(
                                requireContext(),
                                R.color.colorWhite))
                    }
                    10 -> {
                        binding.txtMarkStudentNamePlus11.setTextColor(
                            getColor(
                                requireContext(),
                                R.color.colorWhite))
                    }
                    11 -> {
                        binding.txtMarkStudentNamePlus12.setTextColor(
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
                        binding.txtMarkStudentNamePlus1.setTextColor(getColor(requireContext(), R.color.colorPrimary))
                    } else if (j == 9 && i != j) {
                        binding.txtMarkStudentNamePlus10.setTextColor(getColor(requireContext(), R.color.colorPrimary))
                    } else if (j == 10 && i != j) {
                        binding.txtMarkStudentNamePlus11.setTextColor(getColor(requireContext(), R.color.colorPrimary))
                    } else if (j == 11 && i != j) {
                        binding.txtMarkStudentNamePlus12.setTextColor(getColor(requireContext(), R.color.colorPrimary))
                    }
                    linearMonth[j].setBackgroundColor(getColor(requireContext(), R.color.colorWhite))
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
        binding.spinnerMarkStudentYear.adapter = adapter

        getCurrentData()
    }

    private fun initRecyclerAndAdapter() {
        binding.apply {
            recyclerMarkStudent.setHasFixedSize(true)
            recyclerMarkStudent.adapter = markRecyclerAdapter
            recyclerMarkStudent.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    private fun observeMarkDataState() {
        viewModel.markDataState.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is DataState.Loading -> {
                        swipeRefreshMarkStudent.isRefreshing = true
                        shimmerAnimationMarkStudent.isVisible = true
                        recyclerMarkStudent.isVisible = false
                        imgMarkStudentChoseDate.isVisible = false
                    }
                    is DataState.Success -> {
                        marks = it.data as ArrayList<Mark>
                        markRecyclerAdapter.setData(it.data)
                        swipeRefreshMarkStudent.isRefreshing = false
                        shimmerAnimationMarkStudent.isVisible = false
                        recyclerMarkStudent.isVisible = true
                        imgMarkStudentChoseDate.isVisible = false

                        viewModel.setStateEvent(MarkStateEvent.None)
                    }
                    is DataState.Failure -> {
                        swipeRefreshMarkStudent.isRefreshing = false
                        shimmerAnimationMarkStudent.isVisible = false
                        recyclerMarkStudent.isVisible = false
                        imgMarkStudentChoseDate.isVisible = true

                        Common.showToast(requireActivity(), getString(R.string.strNotFoundMark), "error")
                        viewModel.setStateEvent(MarkStateEvent.None)
                    }
                    is DataState.ExceptionState -> {
                        swipeRefreshMarkStudent.isRefreshing = false
                        shimmerAnimationMarkStudent.isVisible = false
                        recyclerMarkStudent.isVisible = false
                        imgMarkStudentChoseDate.isVisible = true

                        Common.showSnackBar(requireContext(), binding.root, getString(R.string.strThereIsProblem))
                        viewModel.setStateEvent(MarkStateEvent.None)
                    }
                    is DataState.Connection -> {
                        swipeRefreshMarkStudent.isRefreshing = false
                        shimmerAnimationMarkStudent.isVisible = false
                        recyclerMarkStudent.isVisible = false
                        imgMarkStudentChoseDate.isVisible = true

                        Common.showSnackBar(requireContext(), binding.root, getString(R.string.strThereIsProblem))
                        viewModel.setStateEvent(MarkStateEvent.None)
                    }
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
            relativeMonth.add(relativeMarkStudentShap1)
            relativeMonth.add(relativeMarkStudentShap2)
            relativeMonth.add(relativeMarkStudentShap3)
            relativeMonth.add(relativeMarkStudentShap4)
            relativeMonth.add(relativeMarkStudentShap5)
            relativeMonth.add(relativeMarkStudentShap6)
            relativeMonth.add(relativeMarkStudentShap7)
            relativeMonth.add(relativeMarkStudentShap8)
            relativeMonth.add(relativeMarkStudentShap9)
            relativeMonth.add(relativeMarkStudentShap10)
            relativeMonth.add(relativeMarkStudentShap11)
            relativeMonth.add(relativeMarkStudentShap12)
        }
    }

    private fun setTextNumberMonth() {
        binding.apply {
            textNumberMonth.add(txtMarkStudentNumber1)
            textNumberMonth.add(txtMarkStudentNumber2)
            textNumberMonth.add(txtMarkStudentNumber3)
            textNumberMonth.add(txtMarkStudentNumber4)
            textNumberMonth.add(txtMarkStudentNumber5)
            textNumberMonth.add(txtMarkStudentNumber6)
            textNumberMonth.add(txtMarkStudentNumber7)
            textNumberMonth.add(txtMarkStudentNumber8)
            textNumberMonth.add(txtMarkStudentNumber9)
            textNumberMonth.add(txtMarkStudentNumber10)
            textNumberMonth.add(txtMarkStudentNumber11)
            textNumberMonth.add(txtMarkStudentNumber12)
        }
    }

    private fun setTextNameMonth() {
        binding.apply {
            textNameMonth.add(txtMarkStudentName1)
            textNameMonth.add(txtMarkStudentName2)
            textNameMonth.add(txtMarkStudentName3)
            textNameMonth.add(txtMarkStudentName4)
            textNameMonth.add(txtMarkStudentName5)
            textNameMonth.add(txtMarkStudentName6)
            textNameMonth.add(txtMarkStudentName7)
            textNameMonth.add(txtMarkStudentName8)
            textNameMonth.add(txtMarkStudentName9)
            textNameMonth.add(txtMarkStudentName10)
            textNameMonth.add(txtMarkStudentName11)
            textNameMonth.add(txtMarkStudentName12)
        }
    }

    private fun setLinearMonth() {
        binding.apply {
            linearMonth.add(linearMarkStudent1)
            linearMonth.add(linearMarkStudent2)
            linearMonth.add(linearMarkStudent3)
            linearMonth.add(linearMarkStudent4)
            linearMonth.add(linearMarkStudent5)
            linearMonth.add(linearMarkStudent6)
            linearMonth.add(linearMarkStudent7)
            linearMonth.add(linearMarkStudent8)
            linearMonth.add(linearMarkStudent9)
            linearMonth.add(linearMarkStudent10)
            linearMonth.add(linearMarkStudent11)
            linearMonth.add(linearMarkStudent12)
        }
    }

    private fun setCardMonth() {
        binding.apply {
            cardMonth.add(cardMarkStudent1)
            cardMonth.add(cardMarkStudent2)
            cardMonth.add(cardMarkStudent3)
            cardMonth.add(cardMarkStudent4)
            cardMonth.add(cardMarkStudent5)
            cardMonth.add(cardMarkStudent6)
            cardMonth.add(cardMarkStudent7)
            cardMonth.add(cardMarkStudent8)
            cardMonth.add(cardMarkStudent9)
            cardMonth.add(cardMarkStudent10)
            cardMonth.add(cardMarkStudent11)
            cardMonth.add(cardMarkStudent12)
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

    private fun getCurrentData() {
        year = Common.getCurrentYear().toInt()
        month = Common.getCurrentMonth().toInt()
        observeMarkDataState()
        viewModel.setStateEvent(
                    MarkStateEvent.Mark,
                    Common.getCurrentStudent()!!.id,
                    Common.getCurrentStudent()!!.groupID,
                    month,
                    "$year-${year + 1}")

        when (month - 1) {
            0 -> {
                binding.txtMarkStudentNamePlus1.setTextColor(
                    getColor(
                        requireContext(),
                        R.color.colorWhite))
            }

            9 -> {
                binding.txtMarkStudentNamePlus10.setTextColor(
                    getColor(
                        requireContext(),
                        R.color.colorWhite))
            }

            10 -> {
                binding.txtMarkStudentNamePlus11.setTextColor(
                    getColor(
                        requireContext(),
                        R.color.colorWhite))
            }

            11 -> {
                binding.txtMarkStudentNamePlus12.setTextColor(
                    getColor(
                        requireContext(),
                        R.color.colorWhite))
            }
        }
        linearMonth[month - 1].setBackgroundColor(getColor(requireContext(), R.color.colorPrimary))
        textNameMonth[month - 1].setTextColor(getColor(requireContext(), R.color.colorWhite))
        textNumberMonth[month - 1].setTextColor(getColor(requireContext(), R.color.colorPrimary))
        relativeMonth[month - 1].setBackgroundResource(R.drawable.ic_ellipse_white)

        binding.spinnerMarkStudentYear.setSelection(years.indexOf(year.toString()))
    }

    override fun onDestroy() {
        super.onDestroy()
        years.clear()
    }
}