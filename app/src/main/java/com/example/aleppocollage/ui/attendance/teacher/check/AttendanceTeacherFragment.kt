package com.example.aleppocollage.ui.attendance.teacher.check

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.FragmentAttendanceTeacherBinding
import com.example.aleppocollage.model.attendance.domain.Attendance
import com.example.aleppocollage.model.deservedgroup.domain.DeservedGroup
import com.example.aleppocollage.ui.mark.student.adapter.YearSpinnerAdapter
import com.example.aleppocollage.ui.exam.teacher.get.adapter.DeservedGroupsSpinnerAdapter
import com.example.aleppocollage.ui.main.model.ProfileInfo
import com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModel
import com.example.aleppocollage.util.Common
import com.example.aleppocollage.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentAttendanceTeacherBinding.bind(view)

        init()

        binding.apply {

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

            spinnerAttendanceTeacherFragmentDepartment.onItemSelectedListener =
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
                    Toasty.error(requireContext(), getString(R.string.strShouldSelectYearAndGroup), Toast.LENGTH_SHORT, true).show()
                    return@setOnClickListener
                }

                observeAttendanceDataState()
                viewModel.setAttendanceStateEvent(AttendanceTeacherStateEvent.Attendance, groupId, date )

            }

        }

    }

    private fun init() {

        teacherId = Common.getCurrentTeacher()!!.id

        setYear(Common.getCurrentYear())
        val adapterYear = YearSpinnerAdapter(requireContext(), years)
        binding.spinnerAttendanceTeacherYear.adapter = adapterYear

        val adapterDepartment = DeservedGroupsSpinnerAdapter(requireContext(), groups)
        binding.spinnerAttendanceTeacherFragmentDepartment.adapter = adapterDepartment

        observeVariables()

    }

    private fun observeGroupDataState() {
        viewModel.groupsDataState.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Loading -> {
                    binding.apply {
                        spinnerAttendanceTeacherFragmentDepartment.isEnabled = false
                        aviAttendanceTeacherSpinnerDepartment.isVisible = true
                        txtAttenanceTeacherDepartmentSpinner.isVisible = false
                    }
                }
                is DataState.Success -> {

                    groups = it.data as ArrayList<DeservedGroup>

                    if (groups.size > 0) {

                        binding.apply {
                            spinnerAttendanceTeacherFragmentDepartment.isEnabled = true
                            aviAttendanceTeacherSpinnerDepartment.isVisible = false
                            txtAttenanceTeacherDepartmentSpinner.isVisible = false
                        }

                    } else {
                        binding.apply {
                            spinnerAttendanceTeacherFragmentDepartment.isEnabled = false
                            aviAttendanceTeacherSpinnerDepartment.isVisible = false
                            txtAttenanceTeacherDepartmentSpinner.isVisible = false
                        }

                    }

                    val adapterGroup = DeservedGroupsSpinnerAdapter(requireContext(), groups)
                    binding.spinnerAttendanceTeacherFragmentDepartment.adapter = adapterGroup

                    viewModel.setGroupStateEvent(AttendanceTeacherStateEvent.NoneGroup)
                }
                is DataState.Failure -> {

                    groups.clear()
                    groupId = -1

                    val adapterGroup = DeservedGroupsSpinnerAdapter(requireContext(), groups)
                    binding.spinnerAttendanceTeacherFragmentDepartment.adapter = adapterGroup

                    binding.apply {
                        spinnerAttendanceTeacherFragmentDepartment.isEnabled = false
                        aviAttendanceTeacherSpinnerDepartment.isVisible = false
                        txtAttenanceTeacherDepartmentSpinner.isVisible = true
                    }

                    viewModel.setGroupStateEvent(AttendanceTeacherStateEvent.NoneGroup)
                }
                is DataState.ExceptionState -> {

                    groups.clear()
                    groupId = -1

                    binding.apply {
                        spinnerAttendanceTeacherFragmentDepartment.isEnabled = false
                        aviAttendanceTeacherSpinnerDepartment.isVisible = false
                        txtAttenanceTeacherDepartmentSpinner.isVisible = false
                        txtAttenanceTeacherDepartmentSpinner.text = getString(R.string.strThereIsProblem)
                    }

                    viewModel.setGroupStateEvent(AttendanceTeacherStateEvent.NoneGroup)
                }
            }
        }
    }

    private fun observeAttendanceDataState() {
        viewModel.attendancesDataState.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Loading -> {
                    binding.apply {
                        swipeRefreshAttendanceTeacher.isRefreshing = true
                    }
                }
                is DataState.Success -> {
                    attendances = it.data as ArrayList<Attendance>

                    if (attendances.size != 0 && attendances[0].session0 == null) {

                        val action = AttendanceTeacherFragmentDirections.actionAbsenceTeacherFragmentToSetAttendanceTeacherFragment(groupId, date)
                        findNavController().navigate(action)

                        groupId = -1
                        date = ""

                    } else {

                        binding.apply {
                            swipeRefreshAttendanceTeacher.isRefreshing = false
                            relativeLayout.isVisible = true
                            relativeLayout.isVisible = true
                            imgAttendanceTeacherChooseDate.isVisible = false
                            linearAttendanceTeacherInformation.isVisible = true

                            txtAttendanceTeacherDate.text = attendances[0].date
//                            txtAttendanceTeacherYear.text = attendances[0].date
                            txtAttendanceTeacherDepartment.text = groupName
                            txtAttendanceTeacherNumberOfStudent.text = attendances.size.toString()
                            txtAttendanceTeacherNumberOfStudentTrue.text = attendances.size.toString()
                            txtAttendanceTeacherNumberOfStudentFalse.text = "0"
                            txtAttendanceTeacherNumberOfStudentPresent.text = presentNumber.toString()
                            txtAttendanceTeacherNumberOfStudentAttendance.text = attendanceNumber.toString()
                        }

                    }

                    viewModel.setAttendanceStateEvent(AttendanceTeacherStateEvent.NoneAttendance)
                }
                is DataState.Failure -> {
                    viewModel.setAttendanceStateEvent(AttendanceTeacherStateEvent.NoneAttendance)
                }
                is DataState.ExceptionState -> {

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

        sharedViewModel.stateAttendance.observe(viewLifecycleOwner) {
            if (!it.equals(null)) {

                var group: DeservedGroup? = null
                for (i in groups){
                    if (i.id == it.groupId){
                        group = i
                        groupName = i.groupName
                    }
                }

                groupId = it.groupId
                date = it.date

                binding.spinnerAttendanceTeacherYear.setSelection(groups.indexOf(group))
                observeAttendanceDataState()
                observeAttendanceDataState()
                viewModel.setAttendanceStateEvent(AttendanceTeacherStateEvent.Attendance, groupId, date )

            }
        }

    }

    private fun setYear(nowYear: String) {
        for (i in nowYear.toInt() downTo 2019) {
            years.add("$i")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        sharedViewModel.date = MutableLiveData()
        sharedViewModel.stateAttendance = MutableLiveData()

    }
}