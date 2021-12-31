package com.example.aleppocollage.ui.home.student

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.aleppocollage.R
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.aleppocollage.databinding.FragmentHomeStudentBinding
import com.example.aleppocollage.ui.main.model.ProfileInfo
import com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModel
import com.example.aleppocollage.util.Common
import com.example.aleppocollage.util.DataState
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import com.github.mikephil.charting.data.PieData

import com.github.mikephil.charting.data.PieDataSet

@AndroidEntryPoint
class HomeStudentFragment : Fragment(R.layout.fragment_home_student) {

    ///region Variables

    private var backPressTime: Long = 0

    var pieEntries: ArrayList<PieEntry> = ArrayList()
    var label = "type"

    private val sessions: ArrayList<String?> = ArrayList()

    //endregion

    ///region ViewModel & Binding

    private val viewModel: HomeStudentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentHomeStudentBinding? = null
    private val binding get() = _binding!!

    ///endregion


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHomeStudentBinding.bind(view)


        binding.apply {

            cardHomeStudentLogout.setOnClickListener {

                lifecycleScope.launchWhenCreated {

                    sharedViewModel.stateLoading.value = true
                    sharedViewModel.textLoading.value = getString(R.string.strLogout)
                    findNavController().navigate(R.id.action_global_loadingDialog)

                    delay(1000)

                    sharedViewModel.stateLoading.value = false

                    val action =
                        HomeStudentFragmentDirections.actionHomeStudentFragmentToRegisterFragment()
                    findNavController().navigate(action)

                    Common.logout()
                    sharedViewModel.stateStartApp.value = R.id.RegisterFragment
                }

            }

            cardHomeStudentNotification.setOnClickListener {
                val action =
                    HomeStudentFragmentDirections.actionHomeStudentFragmentToNotificationFragment()
                findNavController().navigate(action)
            }

            cardHomeStudentProfile.setOnClickListener {
                val state = !(sharedViewModel.showProfileInfo.value!!.state)
                sharedViewModel.showProfileInfo.value = ProfileInfo(
                    userType = Common.getCurrentTypeUser(),
                    Common.getCurrentStudent()!!,
                    state = state)
            }

            linearHomeStudentPayment.setOnClickListener {
                val action =
                    HomeStudentFragmentDirections.actionHomeStudentFragmentToPaymentStudentFragment()
                findNavController().navigate(action)
            }

            linearHomeStudentMarks.setOnClickListener {
                val action = HomeStudentFragmentDirections.actionHomeStudentFragmentToMarkFragment()
                findNavController().navigate(action)
            }

            linearHomeStudentAttendance.setOnClickListener {
                val action =
                    HomeStudentFragmentDirections.actionHomeStudentFragmentToAttendanceStudentFragment()
                findNavController().navigate(action)
            }

        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (sharedViewModel.showProfileInfo.value!!.state) {
                        sharedViewModel.showProfileInfo.value = ProfileInfo(
                            userType = Common.getCurrentTypeUser(),
                            student = Common.getCurrentStudent()!!,
                            state = false)
                    } else {
                        if (backPressTime + 2000 > System.currentTimeMillis()) {
                            activity?.finish()
                            return
                        } else {
                            Common.showToast(requireActivity(), getString(R.string.strPressBackAgainToExit), "warning")
                        }
                        backPressTime = System.currentTimeMillis()
                    }
                }
            })

    }

    private fun getAttendance() {
        val date = Common.getCurrentDate()
        val studentId = Common.getCurrentStudent()!!.id
        observeAttendanceDataState()
        viewModel.setStateEvent(HomeStudentStateEvent.Attendacne, studentId, date)
    }

    private fun observeAttendanceDataState() {
        viewModel.attendanceDataState.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Loading -> {

                }
                is DataState.Success -> {
                    sessions.add(it.data.session0)
                    sessions.add(it.data.session1)
                    sessions.add(it.data.session2)
                    sessions.add(it.data.session3)
                    sessions.add(it.data.session4)
                    sessions.add(it.data.session5)
                    sessions.add(it.data.session5)
                    sessions.add(it.data.session6)
                    sessions.add(it.data.session7)

                    viewModel.setStateEvent(HomeStudentStateEvent.NoneAttendacne)
                }
                is DataState.Failure -> {
                    viewModel.setStateEvent(HomeStudentStateEvent.NoneAttendacne)
                    Common.showSnackBar(requireContext(), binding.root, it.message)
                }
                is DataState.ExceptionState -> {
                    viewModel.setStateEvent(HomeStudentStateEvent.NoneAttendacne)
                    Common.showSnackBar(requireContext(), binding.root, "$it")
                }
            }
        }
    }
}

/**
 *         val colors: ArrayList<Int> = ArrayList()
colors.add(getColor(requireContext(), R.color.colorPrimary))
colors.add(getColor(requireContext(), R.color.colorRed))
colors.add(getColor(requireContext(), R.color.colorGreenLight))

pieEntries.add(PieEntry(200f, "Toys"))
pieEntries.add(PieEntry(230f, "Snacks"))
pieEntries.add(PieEntry(100f, "Clothes"))
//        pieEntries.add(PieEntry(500f, "Phone"))

val pieDataSet = PieDataSet(pieEntries, "")

//        pieDataSet.valueTextSize = 12f

pieDataSet.colors = colors

val pieData = PieData(pieDataSet)
pieData.setValueTextSize(11f)
pieData.setValueTextColor(getColor(requireContext(), R.color.colorWhite))
pieData.setValueTypeface(Typeface.create(" ", Typeface.BOLD))
//        pieData.setDrawValues(true)

binding.apply {
pieChartHomeStudentAttendance.description.isEnabled = false
pieChartHomeStudentAttendance.isRotationEnabled = false
pieChartHomeStudentAttendance.holeRadius = 0f
pieChartHomeStudentAttendance.animateY(1400)
pieChartHomeStudentAttendance.transparentCircleRadius = 0f
pieChartHomeStudentAttendance.setUsePercentValues(true)
pieChartHomeStudentAttendance.legend.isEnabled = false
pieChartHomeStudentAttendance.setDrawEntryLabels(false)
//            pieChartHomeStudentAttendance.highlightValues(null)
pieChartHomeStudentAttendance.data = pieData
pieChartHomeStudentAttendance.invalidate()

//            pieChartHomeStudentAttendance.setEntryLabelTextSize(12f)
//            pieChartHomeStudentAttendance.setEntryLabelTypeface( Typeface.createFromAsset(requireContext().assets, font/))

}
 */