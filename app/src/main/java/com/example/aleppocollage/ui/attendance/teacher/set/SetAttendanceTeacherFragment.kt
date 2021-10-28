package com.example.aleppocollage.ui.attendance.teacher.set

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.FragmentSetAttendanceTeacherBinding
import com.example.aleppocollage.model.attendance.domain.Attendance
import com.example.aleppocollage.ui.attendance.teacher.adapter.AttendanceTeacherRecyclerAdapter
import com.example.aleppocollage.ui.attendance.teacher.model.StateAttendance
import com.example.aleppocollage.ui.main.model.ProfileInfo
import com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModel
import com.example.aleppocollage.util.Common
import com.example.aleppocollage.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SetAttendanceTeacherFragment : Fragment(R.layout.fragment_set_attendance_teacher) {

    ///region Variables

    private val args: SetAttendanceTeacherFragmentArgs by navArgs()

    private var attendances: ArrayList<Attendance> = ArrayList()

    //endregion

    ///region ViewModel & Binding

    private val viewModel: SetAttendanceTeacherViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentSetAttendanceTeacherBinding? = null
    private val binding get() = _binding!!

    ///endregion

    private lateinit var attendanceTeacherRecyclerAdapter: AttendanceTeacherRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentSetAttendanceTeacherBinding.bind(view)

        init()

        binding.apply {

            cardSetAttendanceTeacherProfile.setOnClickListener {
                val state = !(sharedViewModel.showProfileInfo.value!!.state)
                sharedViewModel.showProfileInfo.value = ProfileInfo(userType = Common.getCurrentTypeUser(), teacher = Common.getCurrentTeacher()!!, state = state)
            }

            cardSetAttendanceTeacherBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnSetAttendanceTeacherSave.setOnClickListener {
                for (item in attendances) {

                    item.date = args.date

                    if (item.session0 ==  null ||
                        item.session1 == null||
                        item.session2 == null||
                        item.session3 == null||
                        item.session4 == null||
                        item.session5 == null||
                        item.session6 == null||
                        item.session7 == null ) {
                        Toasty.error(requireContext(), "يجب تحديد الحضور للجميع",Toast.LENGTH_SHORT,true).show()
                        return@setOnClickListener
                    }
                }

                observeSetAttendanceDataState()
                viewModel.setAttendanceStateEvent(SetAttendanceTeacherStateEvent.SetAttendance, attendances )
            }

        }

    }

    private fun init() {

        attendanceTeacherRecyclerAdapter = AttendanceTeacherRecyclerAdapter(
            {attendances -> listener(attendances)},
            attendances, requireContext()
        )

        initRecyclerAndAdapter()

        observeGetAttendanceDataState()
        viewModel.setGetAttendanceStateEvent(SetAttendanceTeacherStateEvent.Attendance, args.groupId, args.date )

    }

    private fun initRecyclerAndAdapter() {
        binding.apply {
            recyclerAttendanceTeacher.setHasFixedSize(true)
            recyclerAttendanceTeacher.adapter = attendanceTeacherRecyclerAdapter
            recyclerAttendanceTeacher.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun observeGetAttendanceDataState() {
        viewModel.attendancesDataState.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Loading -> {
                    binding.apply {
                        swipeRefreshSetAttendanceTeacher.isRefreshing = true
                    }
                }
                is DataState.Success -> {

                    attendances = it.data as ArrayList<Attendance>

                    if (attendances.size != 0 ) {

                        binding.apply {

                            swipeRefreshSetAttendanceTeacher.isRefreshing = false
                            attendanceTeacherRecyclerAdapter.setData(attendances)
                        }

                    }

                    viewModel.setGetAttendanceStateEvent(SetAttendanceTeacherStateEvent.NoneAttendance)
                }
                is DataState.Failure -> {

                    viewModel.setGetAttendanceStateEvent(SetAttendanceTeacherStateEvent.NoneAttendance)
                }
                is DataState.ExceptionState -> {

                    viewModel.setGetAttendanceStateEvent(SetAttendanceTeacherStateEvent.NoneAttendance)
                }
            }
        }
    }

    private fun observeSetAttendanceDataState() {
        viewModel.setAttendancesDataState.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Loading -> {
                    Log.d("DataState", "Loading")
                    sharedViewModel.stateLoading.value = true
                    sharedViewModel.textLoading.value = "جار حفظ الحضور"
                    findNavController().navigate(R.id.action_global_loadingDialog)
                }
                is DataState.Success -> {
                    Log.d("DataState", "Success")
                    sharedViewModel.stateLoading.value = false
                    sharedViewModel.stateAttendance.value = StateAttendance(args.date, args.groupId)
                    findNavController().navigateUp()
                    Toasty.success(requireContext(), it.data,Toast.LENGTH_SHORT,true).show()

                    viewModel.setAttendanceStateEvent(SetAttendanceTeacherStateEvent.NoneAttendance)
                }
                is DataState.Failure -> {
                    Log.d("DataState", "Failure")

                    viewModel.setAttendanceStateEvent(SetAttendanceTeacherStateEvent.NoneAttendance)
                }
                is DataState.ExceptionState -> {
                    Log.d("DataState", "ExceptionState")
                    Log.d("DataState", "$it")

                    viewModel.setAttendanceStateEvent(SetAttendanceTeacherStateEvent.NoneAttendance)
                }
            }
        }
    }

    private fun listener(attendances: List<Attendance>) {
        this.attendances = attendances as ArrayList<Attendance>
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}