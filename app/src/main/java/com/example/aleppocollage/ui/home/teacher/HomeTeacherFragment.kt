package com.example.aleppocollage.ui.home.teacher

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.FragmentHomeTeacherBinding
import com.example.aleppocollage.model.user.domain.Teacher
import com.example.aleppocollage.ui.home.student.HomeStudentFragmentDirections
import com.example.aleppocollage.ui.main.model.ProfileInfo
import com.example.aleppocollage.ui.util.loading.LoadingViewModel
import com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModel
import com.example.aleppocollage.util.Common
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import io.paperdb.Paper
import kotlinx.coroutines.delay

@AndroidEntryPoint
class HomeTeacherFragment : Fragment(R.layout.fragment_home_teacher) {

    ///region ViewModel & Binding

    private val viewModel: HomeTeacherViewModel by viewModels()
    private val loadingViewModel: LoadingViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentHomeTeacherBinding? = null
    private val binding get() = _binding!!

    ///endregion

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHomeTeacherBinding.bind(view)

        binding.apply {

            cardHomeTeacherNotification.setOnClickListener {

                val action = HomeTeacherFragmentDirections.actionHomeTeacherFragmentToNotificationFragment()
                findNavController().navigate(action)
            }

            cardHomeTeacherProfile.setOnClickListener {
                val state = !(sharedViewModel.showProfileInfo.value!!.state)
                sharedViewModel.showProfileInfo.value = ProfileInfo(userType = Common.getCurrentTypeUser(), teacher = Common.getCurrentTeacher()!!, state = state)
            }

            cardHomeTeacherLogout.setOnClickListener {

                lifecycleScope.launchWhenCreated {

                    sharedViewModel.stateLoading.value = true
                    sharedViewModel.textLoading.value = getString(R.string.strLogout)
                    findNavController().navigate(R.id.action_global_loadingDialog)

                    delay(1000)

                    sharedViewModel.stateLoading.value = false

                    val action = HomeTeacherFragmentDirections.actionHomeTeacherFragmentToRegisterFragment()
                    findNavController().navigate(action)

                    Common.logout()

                    sharedViewModel.stateStartApp.value = R.id.RegisterFragment

                }

            }

            relativeHomeTeacherPayment.setOnClickListener {
                val action = HomeTeacherFragmentDirections.actionHomeTeacherFragmentToPaymentFragment()
                findNavController().navigate(action)
            }

            relativeHomeTeacherTest.setOnClickListener {
                val action = HomeTeacherFragmentDirections.actionHomeTeacherFragmentToTestFragment()
                findNavController().navigate(action)
            }

            relativeHomeTeacherAttendance.setOnClickListener {

                val teacher = Common.getCurrentTeacher()
                if (teacher!!.name.contains("موجه", ignoreCase = false)) {
                    val action =
                        HomeTeacherFragmentDirections.actionHomeTeacherFragmentToAbsenceTeacherFragment()
                    findNavController().navigate(action)
                } else {
                    Toasty.warning(requireContext(), getString(R.string.strNoPermissionForTeacher), Toast.LENGTH_SHORT, true).show()
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}