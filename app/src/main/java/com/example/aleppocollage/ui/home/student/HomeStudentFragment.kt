package com.example.aleppocollage.ui.home.student

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.FragmentHomeStudentBinding
import com.example.aleppocollage.ui.main.model.ProfileInfo
import com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModel
import com.example.aleppocollage.util.Common
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class HomeStudentFragment : Fragment(R.layout.fragment_home_student) {

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

                    val action = HomeStudentFragmentDirections.actionHomeStudentFragmentToRegisterFragment()
                    findNavController().navigate(action)

                    Common.logout()
                    sharedViewModel.stateStartApp.value = R.id.RegisterFragment
                }

            }

            cardHomeStudentNotification.setOnClickListener {
                val action = HomeStudentFragmentDirections.actionHomeStudentFragmentToNotificationFragment()
                findNavController().navigate(action)
            }

            cardHomeStudentProfile.setOnClickListener {

                val state = !(sharedViewModel.showProfileInfo.value!!.state)
                sharedViewModel.showProfileInfo.value = ProfileInfo(userType = Common.getCurrentTypeUser(),Common.getCurrentStudent()!!, state = state)
            }

            relativeHomeStudentPayment.setOnClickListener {
                val action = HomeStudentFragmentDirections.actionHomeStudentFragmentToPaymentStudentFragment()
                findNavController().navigate(action)
            }

            relativeHomeStudentMarks.setOnClickListener {
                val action = HomeStudentFragmentDirections.actionHomeStudentFragmentToMarkFragment()
                findNavController().navigate(action)
            }

            relativeHomeStudentAttendance.setOnClickListener {
                val action = HomeStudentFragmentDirections.actionHomeStudentFragmentToAbsenceStudentFragment()
                findNavController().navigate(action)
            }

        }

    }
}