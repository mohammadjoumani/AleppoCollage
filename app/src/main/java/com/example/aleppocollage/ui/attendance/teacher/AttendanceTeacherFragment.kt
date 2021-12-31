package com.example.aleppocollage.ui.attendance.teacher

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.FragmentAttendanceTeacherBinding
import com.example.aleppocollage.model.attendance.domain.Attendance
import com.example.aleppocollage.model.deservedgroup.domain.DeservedGroup
import com.example.aleppocollage.ui.attendance.teacher.adapter.AttendanceTeacherRecyclerAdapter
import com.example.aleppocollage.ui.mark.student.adapter.YearSpinnerAdapter
import com.example.aleppocollage.ui.exam.teacher.adapter.DeservedGroupsSpinnerAdapter
import com.example.aleppocollage.ui.main.model.ProfileInfo
import com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModel
import com.example.aleppocollage.util.Common
import com.example.aleppocollage.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AttendanceTeacherFragment : Fragment(R.layout.fragment_attendance_teacher) {

    ///region Variables

    private var presentNumber = 0

    private var attendanceNumber = 0

    private var groupName = ""

    private var year = ""

    private var date = ""

    private var groupId = -1

    private var teacherId = -1

    private var years: ArrayList<String> = ArrayList()

    private var attendances: ArrayList<Attendance> = ArrayList()

    private var groups: ArrayList<DeservedGroup> = ArrayList()

    //endregion

    ///region ViewModel & Binding

    private val viewModel: AttendanceTeacherViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentAttendanceTeacherBinding? = null
    private val binding get() = _binding!!

    ///endregion

    private lateinit var deservedGroupsSpinnerAdapter: DeservedGroupsSpinnerAdapter

    private lateinit var attendanceTeacherRecyclerAdapter: AttendanceTeacherRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentAttendanceTeacherBinding.bind(view)

        attendanceTeacherRecyclerAdapter = AttendanceTeacherRecyclerAdapter(
            {attendances -> listener(attendances)},
            attendances, requireContext()
        )

        init()

        binding.apply {

            swipeRefreshAttendanceTeacher.setColorSchemeColors(getColor(requireContext(), R.color.colorPrimary))

            swipeRefreshAttendanceTeacher.setOnRefreshListener {
                observeGroupDataState()
                viewModel.setGroupStateEvent(AttendanceTeacherStateEvent.Group, teacherId, year)
            }

            spinnerAttendanceTeacherYear.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }

                    override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        year = years[position] + "-" + "${years[position].toInt() + 1}"
                        observeGroupDataState()
                        viewModel.setGroupStateEvent(AttendanceTeacherStateEvent.Group, teacherId, year)
                    }
                }

            spinnerAttendanceTeacherGroup.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }

                    override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        groupId = groups[position].id
                    }
                }

            cardAttendanceTeacherProfile.setOnClickListener {
                val state = !(sharedViewModel.showProfileInfo.value!!.state)
                sharedViewModel.showProfileInfo.value = ProfileInfo(userType = Common.getCurrentTypeUser(), teacher = Common.getCurrentTeacher()!!, state = state)
            }

            cardAttendanceTeacherBack.setOnClickListener {
                findNavController().navigateUp()
            }

            cardAttendanceTeacherChooseDate.setOnClickListener {
                findNavController().navigate(R.id.action_global_chooseDateSheetFragment)
            }

            imgAttendanceTeacherClearDate.setOnClickListener {
                date = ""
                imgAttendanceTeacherClearDate.isVisible = false
                txtAttendanceTeacherChooseDate. text = getString(R.string.strChooseDate)
                txtAttendanceTeacherChooseDate.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorGray))
            }

            btnAttendanceTeacherRecordTest.setOnClickListener {
                if (groupId == -1 || date.isEmpty()) {
                    Common.showToast(requireActivity(), getString(R.string.strShouldSelectYearAndGroup),"warning")
                    return@setOnClickListener
                }

                observeAttendanceDataState()
                viewModel.setAttendanceStateEvent(AttendanceTeacherStateEvent.Attendance, groupId, date )
            }

            btnAttendanceTeacherSave.setOnClickListener {
                observeSetAttendanceDataState()
                viewModel.setAttendanceStateEvent(AttendanceTeacherStateEvent.SetAttendance, attendances, date )
            }

        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (sharedViewModel.showProfileInfo.value!!.state) {
                    sharedViewModel.showProfileInfo.value = ProfileInfo(userType = Common.getCurrentTypeUser(), teacher = Common.getCurrentTeacher()!!, state = false)
                } else{
                    if (viewModel.stateCallback.value == true)
                        viewModel.stateCallback.value = false
                    else
                        findNavController().navigateUp()
                }
            }
        })

    }

    private fun init() {

        teacherId = Common.getCurrentTeacher()!!.id

        setYear(Common.getCurrentYear())
        val adapterYear = YearSpinnerAdapter(requireContext(), years)
        binding.spinnerAttendanceTeacherYear.adapter = adapterYear

        deservedGroupsSpinnerAdapter= DeservedGroupsSpinnerAdapter(requireContext(), groups)
        binding.spinnerAttendanceTeacherGroup.adapter = deservedGroupsSpinnerAdapter

        observeVariables()
        setDate()
        initRecyclerAndAdapter()
    }

    private fun initRecyclerAndAdapter() {
        binding.apply {
            recyclerAttendanceTeacher.setHasFixedSize(true)
            recyclerAttendanceTeacher.adapter = attendanceTeacherRecyclerAdapter
            recyclerAttendanceTeacher.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun observeGroupDataState() {
        viewModel.groupsDataState.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Loading -> {
                    binding.apply {
                        swipeRefreshAttendanceTeacher.isRefreshing = true
                        spinnerAttendanceTeacherGroup.isEnabled = false
                        aviAttendanceTeacherSpinnerDepartment.isVisible = true
                        txtAttenanceTeacherDepartmentSpinner.isVisible = false
                    }
                }
                is DataState.Success -> {
                    groups = it.data as ArrayList<DeservedGroup>
                    binding.apply {
                        swipeRefreshAttendanceTeacher.isRefreshing = false
                        if (groups.size > 0) {
                            deservedGroupsSpinnerAdapter= DeservedGroupsSpinnerAdapter(requireContext(), groups)
                            spinnerAttendanceTeacherGroup.adapter = deservedGroupsSpinnerAdapter
                            spinnerAttendanceTeacherGroup.isEnabled = true
                            aviAttendanceTeacherSpinnerDepartment.isVisible = false
                            txtAttenanceTeacherDepartmentSpinner.isVisible = false
                        } else {
                            spinnerAttendanceTeacherGroup.isEnabled = false
                            aviAttendanceTeacherSpinnerDepartment.isVisible = false
                            txtAttenanceTeacherDepartmentSpinner.isVisible = true
                            txtAttenanceTeacherDepartmentSpinner.text = getString(R.string.strThereIsNotGroups)
                        }
                    }
                    viewModel.setGroupStateEvent(AttendanceTeacherStateEvent.NoneGroup)
                }
                is DataState.Failure -> {
                    groups.clear()
                    groupId = -1
                    deservedGroupsSpinnerAdapter.clear()
                    binding.apply {
                        swipeRefreshAttendanceTeacher.isRefreshing = false
                        spinnerAttendanceTeacherGroup.isEnabled = false
                        aviAttendanceTeacherSpinnerDepartment.isVisible = false
                        txtAttenanceTeacherDepartmentSpinner.isVisible = true
                        txtAttenanceTeacherDepartmentSpinner.text = getString(R.string.strThereIsNotGroups)

                    }
                    viewModel.setGroupStateEvent(AttendanceTeacherStateEvent.NoneGroup)
                }
                is DataState.ExceptionState -> {
                    groups.clear()
                    groupId = -1
                    deservedGroupsSpinnerAdapter.clear()
                    binding.apply {
                        swipeRefreshAttendanceTeacher.isRefreshing = false
                        spinnerAttendanceTeacherGroup.isEnabled = false
                        aviAttendanceTeacherSpinnerDepartment.isVisible = false
                        txtAttenanceTeacherDepartmentSpinner.isVisible = true
                        txtAttenanceTeacherDepartmentSpinner.text = getString(R.string.strThereIsNotGroups)
                    }
                    Common.showSnackBar(requireContext(), binding.root, getString(R.string.strThereIsProblem))
                    viewModel.setGroupStateEvent(AttendanceTeacherStateEvent.NoneGroup)
                }
                is DataState.Connection -> {
                    groups.clear()
                    groupId = -1
                    deservedGroupsSpinnerAdapter.clear()
                    binding.apply {
                        swipeRefreshAttendanceTeacher.isRefreshing = false
                        spinnerAttendanceTeacherGroup.isEnabled = false
                        aviAttendanceTeacherSpinnerDepartment.isVisible = false
                        txtAttenanceTeacherDepartmentSpinner.isVisible = true
                        txtAttenanceTeacherDepartmentSpinner.text = getString(R.string.strThereIsNotGroups)
                    }
                    Common.showSnackBar(requireContext(), binding.root, getString(R.string.strThereIsProblem))
                    viewModel.setGroupStateEvent(AttendanceTeacherStateEvent.NoneGroup)
                }
            }
        }
    }

    private fun observeAttendanceDataState() {
        viewModel.attendancesDataState.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is DataState.Loading -> {
                        swipeRefreshAttendanceTeacher.isRefreshing = true
                    }
                    is DataState.Success -> {
                        attendances = it.data as ArrayList<Attendance>
                        swipeRefreshAttendanceTeacher.isRefreshing = false
                        if (attendances.size != 0 ) {
                            viewModel.stateCallback.value = true
                            attendanceTeacherRecyclerAdapter.setData(attendances)
                        } else {
                            Common.showToast(requireActivity(), getString(R.string.strNotFoundData),"error")
                        }
                        viewModel.setAttendanceStateEvent(AttendanceTeacherStateEvent.NoneAttendance)
                    }
                    is DataState.Failure -> {
                        swipeRefreshAttendanceTeacher.isRefreshing = false
                        Common.showToast(requireActivity(), getString(R.string.strNotFoundData), "error")
                        viewModel.setAttendanceStateEvent(AttendanceTeacherStateEvent.NoneAttendance)
                    }
                    is DataState.ExceptionState -> {
                        swipeRefreshAttendanceTeacher.isRefreshing = false
                        viewModel.stateCallback.value = false
                        Common.showSnackBar(requireContext(), binding.root, getString(R.string.strThereIsProblem))
                        viewModel.setAttendanceStateEvent(AttendanceTeacherStateEvent.NoneAttendance)
                    }
                    is DataState.Connection -> {
                        swipeRefreshAttendanceTeacher.isRefreshing = false
                        viewModel.stateCallback.value = false
                        Common.showSnackBar(requireContext(), binding.root, getString(R.string.strThereIsProblem))
                        viewModel.setAttendanceStateEvent(AttendanceTeacherStateEvent.NoneAttendance)

                    }
                }
            }
        }
    }

    private fun observeSetAttendanceDataState() {
        viewModel.setAttendancesDataState.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Loading -> {
                    sharedViewModel.stateLoading.value = true
                    sharedViewModel.textLoading.value = getString(R.string.strSaveAttendance)
                    findNavController().navigate(R.id.action_global_loadingDialog)
                }
                is DataState.Success -> {
                    sharedViewModel.stateLoading.value = false
                    viewModel.stateCallback.value = false
                    Common.showToast(requireActivity(), it.data, "success" )
                    viewModel.setAttendanceStateEvent(AttendanceTeacherStateEvent.NoneAttendance)
                }
                is DataState.Failure -> {
                    sharedViewModel.stateLoading.value = false
                    Common.showToast(requireActivity(), it.message ,"error")
                    viewModel.setAttendanceStateEvent(AttendanceTeacherStateEvent.NoneAttendance)
                }
                is DataState.ExceptionState -> {
                    sharedViewModel.stateLoading.value = false
                    Common.showSnackBar(requireContext(), binding.root, getString(R.string.strThereIsProblem))
                    viewModel.setAttendanceStateEvent(AttendanceTeacherStateEvent.NoneAttendance)
                }
                is DataState.Connection -> {
                    sharedViewModel.stateLoading.value = false
                    Common.showSnackBar(requireContext(), binding.root, getString(R.string.strThereIsProblem))
                    viewModel.setAttendanceStateEvent(AttendanceTeacherStateEvent.NoneAttendance)

                }
            }
        }
    }

    private fun observeVariables() {

        sharedViewModel.date.observe(viewLifecycleOwner) {

            if (!it.equals(null)) {
                date = it
                binding.apply {
                    imgAttendanceTeacherClearDate.isVisible = true
                    txtAttendanceTeacherChooseDate. text = it
                    txtAttendanceTeacherChooseDate.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorBlack))
                }
            }

        }

        viewModel.stateCallback.observe(viewLifecycleOwner) {
            if (!it) binding.swipeRefreshAttendanceTeacher.isRefreshing = it
            binding.apply {
                swipeRefreshAttendanceTeacher.isVisible = !it
                relativeAttendanceTeacher.isVisible = it
                btnAttendanceTeacherSave.isVisible = it
            }
        }
    }

    private fun listener(attendances: List<Attendance>) {
        this.attendances = attendances as ArrayList<Attendance>
    }

    private fun setYear(nowYear: String) {
        for (i in nowYear.toInt() downTo 2019) {
            years.add("$i")
        }
    }

    private fun setDate() {
        date = Common.getCurrentDate()
        binding.apply {
            txtAttendanceTeacherChooseDate.text = date
            txtAttendanceTeacherChooseDate.setTextColor(getColor(requireContext(), R.color.colorBlack))
            imgAttendanceTeacherClearDate.isVisible = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        sharedViewModel.date = MutableLiveData()
//        sharedViewModel.stateAttendance = MutableLiveData()
        years.clear()
    }
}
