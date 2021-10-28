package com.example.aleppocollage.ui.exam.teacher.examnew

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.FragmentExamBinding
import com.example.aleppocollage.databinding.FragmentExamNewBinding
import com.example.aleppocollage.ui.exam.teacher.get.ExamViewModel
import com.example.aleppocollage.ui.main.model.ProfileInfo
import com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModel
import com.example.aleppocollage.util.Common
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExamNewFragment : Fragment(R.layout.fragment_exam_new) {

    ///region ViewModel & Binding

    private val viewModel: ExamNewViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentExamNewBinding? = null
    private val binding get() = _binding!!

    ///endregion

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentExamNewBinding.bind(view)

        binding.apply {

            cardNewExamBack.setOnClickListener {
                findNavController().navigateUp()
            }

            cardNewExamProfile.setOnClickListener {
                val state = !(sharedViewModel.showProfileInfo.value!!.state)
                sharedViewModel.showProfileInfo.value =
                    ProfileInfo(userType = Common.getCurrentTypeUser(), teacher = Common.getCurrentTeacher()!!, state = state)
            }

        }
    }
}