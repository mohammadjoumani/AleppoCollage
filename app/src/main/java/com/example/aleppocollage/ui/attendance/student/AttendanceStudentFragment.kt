package com.example.aleppocollage.ui.attendance.student

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.FragmentAttendanceStudentBinding
import com.example.aleppocollage.ui.main.model.ProfileInfo
import com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModel
import com.example.aleppocollage.util.Common
import com.example.aleppocollage.util.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AttendanceStudentFragment : Fragment(R.layout.fragment_attendance_student) {

    ///region Variables

    private var date = ""

    private val imagesSessions: ArrayList<ImageView> = ArrayList()

    private val textSessions: ArrayList<TextView> = ArrayList()

    //endregion

    ///region ViewModel & Binding

    private val viewModel: AttendanceStudentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentAttendanceStudentBinding? = null
    private val binding get() = _binding!!

    ///endregion

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAttendanceStudentBinding.bind(view)

        init()

        binding.apply {

            cardAbsenceStudentFragmentBack.setOnClickListener {
                findNavController().navigateUp()
            }

            cardAbsenceStudentFragmentProfile.setOnClickListener {

                val state = !(sharedViewModel.showProfileInfo.value!!.state)
                sharedViewModel.showProfileInfo.value = ProfileInfo(userType = Common.getCurrentTypeUser(),Common.getCurrentStudent()!!, state = state)
            }

            cardAttendanceStudentChooseDate.setOnClickListener {
                findNavController().navigate(R.id.action_global_chooseDateSheetFragment)
            }

            btnAttendanceStudentRecordTest.setOnClickListener {
                if (date.isNullOrEmpty()) {
                    Common.showToast(requireActivity(), getString(R.string.strPleaseEnterDate), "warning" )
                    return@setOnClickListener
                }

                observeAttendanceDataState()
                viewModel.setStateEvent(
                    AttendanceStudentStateEvent.Attendacne,
                    studentId = Common.getCurrentStudent()!!.id,
                    date = date
                )
            }

            imgAttendanceStudentClearDate.setOnClickListener {
                date = ""
                imgAttendanceStudentClearDate.isVisible = false
                txtAttendanceStudentChooseDate. text = getString(R.string.strChooseDate)
                txtAttendanceStudentChooseDate.setTextColor(getColor(requireContext(), R.color.colorGray))
            }

            swipeRefreshAttendanceStudentStudentStudent.setColorSchemeColors(getColor(requireContext(), R.color.colorPrimary))

            swipeRefreshAttendanceStudentStudentStudent.setOnRefreshListener {
                observeAttendanceDataState()
                viewModel.setStateEvent(
                    AttendanceStudentStateEvent.Attendacne,
                    studentId = Common.getCurrentStudent()!!.id,
                    date = date
                )

//                date = ""
//                imgAttendanceStudentClearDate.isVisible = false
//                txtAttendanceStudentChooseDate.text = getString(R.string.strChooseDate)
//                txtAttendanceStudentChooseDate.setTextColor(getColor(requireContext(), R.color.colorGray))
//                constraintAttendanceStudentSession.isVisible = false
//                txtAttendanceStudentNoteTitle.isVisible = false
//                cardAttendanceStudentNoteContent.isVisible = false
//                imgAttendanceStudentChooseDate.isVisible = true
//                swipeRefreshAttendanceStudentStudentStudent.isRefreshing = false
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
    }

    private fun init() {
        observeVariables()
        initRelativeSessions()
        initTextSessions()
        getCurrentData()
    }

    private fun observeVariables() {
        sharedViewModel.date.observe(viewLifecycleOwner) {
            if (!it.equals(null)) {
                date = it
                binding.apply {
                    imgAttendanceStudentClearDate.isVisible = true
                    txtAttendanceStudentChooseDate. text = it
                    txtAttendanceStudentChooseDate.setTextColor(getColor(requireContext(), R.color.colorBlack))
                }
            }

        }
    }

    private fun observeAttendanceDataState() {
        viewModel.attendanceDataState.observe(viewLifecycleOwner) {
            binding.apply {
                when(it) {
                    is DataState.Loading -> {
                        swipeRefreshAttendanceStudentStudentStudent.isRefreshing = true
                        constraintAttendanceStudentSession.isVisible = false
                        imgAttendanceStudentChooseDate.isVisible = true
                        txtAttendanceStudentNoteTitle.isVisible = false
                        cardAttendanceStudentNoteContent.isVisible = false
                    }
                    is DataState.Success -> {
                        val sessions = listOf(
                            it.data.session0,
                            it.data.session1,
                            it.data.session2,
                            it.data.session3,
                            it.data.session4,
                            it.data.session5,
                            it.data.session5,
                            it.data.session6,
                            it.data.session7
                        )
//                    sessions.add(it.data.session0)
//                    sessions.add(it.data.session1)
//                    sessions.add(it.data.session2)
//                    sessions.add(it.data.session3)
//                    sessions.add(it.data.session4)
//                    sessions.add(it.data.session5)
//                    sessions.add(it.data.session5)
//                    sessions.add(it.data.session6)
//                    sessions.add(it.data.session7)
//                    sessions.add(it.data.session8)
//                    sessions.add(it.data.session9)

                        swipeRefreshAttendanceStudentStudentStudent.isRefreshing = false
                        constraintAttendanceStudentSession.isVisible = true
                        imgAttendanceStudentChooseDate.isVisible = false
                        txtAttendanceStudentNoteTitle.isVisible = true
                        cardAttendanceStudentNoteContent.isVisible = true

                        if (it.data.note == "" || it.data.note == null) {
                            binding.apply {
                                txtAttendanceStudentNotes.text = getString(R.string.strNotNote)
                                txtAttendanceStudentNotes.setTextColor(getColor(requireContext(), R.color.colorGray))
                            }

                        } else {
                            binding.apply {
                                txtAttendanceStudentNotes.text = it.data.note
                                txtAttendanceStudentNotes.setTextColor(getColor(requireContext(), R.color.colorBlack))
                            }
                        }

                        for (i in 0..7) {
                            when {
                                sessions[i] == "P" -> {
                                    if (i == 0){
                                        imagesSessions[i].setImageResource(R.drawable.ic_right_start_green_dark)
                                        textSessions[i].setBackgroundColor(getColor(requireContext(),R.color.colorGreenDark))
                                        binding.apply {
                                            viewAttendanceStudentSession1.setBackgroundColor(getColor(requireContext(), R.color.colorGreenDark))
                                        }
                                    } else if(i == 7) {
                                        textSessions[i].setBackgroundColor(getColor(requireContext(),R.color.colorGreenLight))
                                        imagesSessions[i].setImageResource(R.drawable.ic_left_finish_green_light)
                                        binding.apply {
                                            viewAttendanceStudentSession8.setBackgroundColor(getColor(requireContext(), R.color.colorGreenLight))
                                        }

                                    } else if (i % 2 == 0){
                                        textSessions[i].setBackgroundColor(getColor(requireContext(),R.color.colorGreenDark))
                                        imagesSessions[i].setImageResource(R.drawable.ic_left_green_dark)

                                    } else if (i % 2 != 0) {
                                        textSessions[i].setBackgroundColor(getColor(requireContext(),R.color.colorGreenLight))
                                        imagesSessions[i].setImageResource(R.drawable.ic_right_green_light)

                                    }

                                }
                                sessions[i] == "A" -> {
                                    textSessions[i].setBackgroundColor(getColor(requireContext(),R.color.colorRed))

                                    if (i == 0){

                                        imagesSessions[i].setImageResource(R.drawable.ic_right_start_red)
                                        binding.apply {
                                            viewAttendanceStudentSession1.setBackgroundColor(getColor(requireContext(), R.color.colorRed))
                                        }


                                    } else if(i == 7) {

                                        imagesSessions[i].setImageResource(R.drawable.ic_left_finish_red)
                                        binding.apply {
                                            viewAttendanceStudentSession8.setBackgroundColor(getColor(requireContext(), R.color.colorRed))
                                        }

                                    } else if (i % 2 == 0){

                                        imagesSessions[i].setImageResource(R.drawable.ic_left_red)

                                    } else if (i % 2 != 0) {

                                        imagesSessions[i].setImageResource(R.drawable.ic_right_red)

                                    }
                                }
                                sessions[i] == "N"  -> {
                                    textSessions[i].setBackgroundColor(getColor(requireContext(),R.color.colorPrimary))
                                    if (i == 0){

                                        imagesSessions[i].setImageResource(R.drawable.ic_right_start_primary)
                                        binding.apply {
                                            viewAttendanceStudentSession1.setBackgroundColor(getColor(requireContext(), R.color.colorPrimary))
                                        }


                                    } else if(i == 7) {

                                        imagesSessions[i].setImageResource(R.drawable.ic_left_finish_primary)
                                        binding.apply {
                                            viewAttendanceStudentSession8.setBackgroundColor(getColor(requireContext(), R.color.colorPrimary))
                                        }

                                    } else if (i % 2 == 0){

                                        imagesSessions[i].setImageResource(R.drawable.ic_left_primary)

                                    } else if (i % 2 != 0) {

                                        imagesSessions[i].setImageResource(R.drawable.ic_right_primary)

                                    }
                                }
                                sessions[i] == null ->{
                                    textSessions[i].setBackgroundColor(getColor(requireContext(),R.color.colorPrimary))
                                    if (i == 0){

                                        imagesSessions[i].setImageResource(R.drawable.ic_right_start_primary)
                                        binding.apply {
                                            viewAttendanceStudentSession1.setBackgroundColor(getColor(requireContext(), R.color.colorPrimary))
                                        }


                                    } else if(i == 7) {

                                        imagesSessions[i].setImageResource(R.drawable.ic_left_finish_primary)
                                        binding.apply {
                                            viewAttendanceStudentSession8.setBackgroundColor(getColor(requireContext(), R.color.colorPrimary))
                                        }

                                    } else if (i % 2 == 0){

                                        imagesSessions[i].setImageResource(R.drawable.ic_left_primary)

                                    } else if (i % 2 != 0) {

                                        imagesSessions[i].setImageResource(R.drawable.ic_right_primary)

                                    }
                                }
                            }
                        }

                        /*
                        if (it.data.session0 == null ||
                            it.data.session1 == null ||
                            it.data.session2 == null ||
                            it.data.session3 == null ||
                            it.data.session4 == null ||
                            it.data.session5 == null ||
                            it.data.session5 == null ||
                            it.data.session6 == null ||
                            it.data.session7 == null
                        ) {

                            Common.showToast(requireActivity(), getString(R.string.strNotFoundData), "error")
    //                        Toasty.error(requireContext(), getString(R.string.strNotFoundData), Toast.LENGTH_SHORT, true).show()
                                binding.apply {
                                swipeRefreshAttendanceStudentStudentStudent.isRefreshing = false
                                constraintAttendanceStudentSession.isVisible = false
                                imgAttendanceStudentChooseDate.isVisible = true
                                txtAttendanceStudentNoteTitle.isVisible = false
                                cardAttendanceStudentNoteContent.isVisible = false
                            }

                        } else {

                        }
    */
                        viewModel.setStateEvent(AttendanceStudentStateEvent.None)
                    }
                    is DataState.Failure -> {
                        swipeRefreshAttendanceStudentStudentStudent.isRefreshing = false
                        constraintAttendanceStudentSession.isVisible = false
                        imgAttendanceStudentChooseDate.isVisible = true
                        txtAttendanceStudentNoteTitle.isVisible = false
                        cardAttendanceStudentNoteContent.isVisible = false

                        Common.showToast(requireActivity(), getString(R.string.strNotFoundData), "error")
                        viewModel.setStateEvent(AttendanceStudentStateEvent.None)
                    }
                    is DataState.ExceptionState -> {
                        swipeRefreshAttendanceStudentStudentStudent.isRefreshing = false
                        constraintAttendanceStudentSession.isVisible = false
                        imgAttendanceStudentChooseDate.isVisible = true
                        txtAttendanceStudentNoteTitle.isVisible = false
                        cardAttendanceStudentNoteContent.isVisible = false

                        Common.showSnackBar(requireContext(), binding.root, getString(R.string.strThereIsProblem))
                        viewModel.setStateEvent(AttendanceStudentStateEvent.None)
                    }
                    is DataState.Connection -> {
                        swipeRefreshAttendanceStudentStudentStudent.isRefreshing = false
                        constraintAttendanceStudentSession.isVisible = false
                        imgAttendanceStudentChooseDate.isVisible = true
                        txtAttendanceStudentNoteTitle.isVisible = false
                        cardAttendanceStudentNoteContent.isVisible = false

                        Common.showSnackBar(requireContext(), binding.root, getString(R.string.strThereIsProblem))
                        viewModel.setStateEvent(AttendanceStudentStateEvent.None)
                    }
                }
            }
        }
    }

    private fun initRelativeSessions() {
        binding.apply {
            imagesSessions.add(imgAttendanceStudentSession1)
            imagesSessions.add(imgAttendanceStudentSession2)
            imagesSessions.add(imgAttendanceStudentSession3)
            imagesSessions.add(imgAttendanceStudentSession4)
            imagesSessions.add(imgAttendanceStudentSession5)
            imagesSessions.add(imgAttendanceStudentSession6)
            imagesSessions.add(imgAttendanceStudentSession7)
            imagesSessions.add(imgAttendanceStudentSession8)
        }
    }

    private fun initTextSessions() {
        binding.apply {
            textSessions.add(txtAttendanceStudentNameSession1)
            textSessions.add(txtAttendanceStudentNameSession2)
            textSessions.add(txtAttendanceStudentNameSession3)
            textSessions.add(txtAttendanceStudentNameSession4)
            textSessions.add(txtAttendanceStudentNameSession5)
            textSessions.add(txtAttendanceStudentNameSession6)
            textSessions.add(txtAttendanceStudentNameSession7)
            textSessions.add(txtAttendanceStudentNameSession8)
        }
    }

    private fun getCurrentData() {
        binding.apply {
            date = Common.getCurrentDate()
            txtAttendanceStudentChooseDate.text = date
            txtAttendanceStudentChooseDate.setTextColor(getColor(requireContext(), R.color.colorBlack))
            imgAttendanceStudentClearDate.isVisible = true
        }
        observeAttendanceDataState()
        viewModel.setStateEvent(
            AttendanceStudentStateEvent.Attendacne,
            studentId = Common.getCurrentStudent()!!.id,
            date = date
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        sharedViewModel.date = MutableLiveData()
    }

}