package com.example.aleppocollage.ui.mark.maxmark

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.FragmentMarkStudentBinding
import com.example.aleppocollage.databinding.FragmentMaxMarkBinding
import com.example.aleppocollage.ui.main.model.ProfileInfo
import com.example.aleppocollage.ui.mark.student.MarkViewModel
import com.example.aleppocollage.ui.mark.teacher.MarkTeacherFragmentArgs
import com.example.aleppocollage.ui.mark.teacher.MarkTeacherStateEvent
import com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModel
import com.example.aleppocollage.util.Common
import com.example.aleppocollage.util.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MaxMarkFragment : Fragment(R.layout.fragment_max_mark) {

    private val args: MaxMarkFragmentArgs by navArgs()
    private var maxMark = -1
    private var description = ""

    ///region ViewModel & Binding

    private val viewModel: MaxMarkViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentMaxMarkBinding? = null
    private val binding get() = _binding!!

    ///endregion

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentMaxMarkBinding.bind(view)

        binding.apply {
            cardMaxMarkBack.setOnClickListener {
                findNavController().navigateUp()
            }

            cardMaxMarkProfile.setOnClickListener {
                val state = !(sharedViewModel.showProfileInfo.value!!.state)
                sharedViewModel.showProfileInfo.value = ProfileInfo(userType = Common.getCurrentTypeUser(), teacher = Common.getCurrentTeacher()!!, state = state)
            }

            btnMaxMarkSave.setOnClickListener {
                maxMark = Integer.parseInt(edtMaxMarkValue.text.toString())
                description = edtMaxMarkDescription.text.toString()

                if (maxMark == -1){
                    Common.showToast(requireActivity(), getString(R.string.strMarkRequire),"warning")
                 return@setOnClickListener
                }
                if (description == "") {
                    Common.showToast(requireActivity(), getString(R.string.strDescriptionRequire),"warning")
                    return@setOnClickListener
                }
                observeDataState()
                viewModel.setStateEvent(
                    MaxMarkEvent.Max,
                    id = args.exam.id,
                    maxMark = maxMark,
                    description = description
                )
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (sharedViewModel.showProfileInfo.value!!.state) {
                    sharedViewModel.showProfileInfo.value = ProfileInfo(userType = Common.getCurrentTypeUser(), teacher = Common.getCurrentTeacher()!!, state = false)
                } else{
                    findNavController().navigateUp()
                }
            }
        })

    }

    private fun observeDataState() {
        viewModel.maxMarkDataState.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Loading -> {
                    sharedViewModel.stateLoading.value = true
                    sharedViewModel.textLoading.value = getString(R.string.strSaveMark)
                    findNavController().navigate(R.id.action_global_loadingDialog)
                }
                is DataState.Success -> {
                    sharedViewModel.stateLoading.value = false
                    findNavController().navigateUp()
                    Common.showToast(requireActivity(), getString(R.string.strSuccessSave), "success" )
                    viewModel.setStateEvent(MaxMarkEvent.None)
                }
                is DataState.Failure -> {
                    sharedViewModel.stateLoading.value = false
                    Common.showToast(requireActivity(), it.message ,"error")
                    viewModel.setStateEvent(MaxMarkEvent.None)
                }
                is DataState.ExceptionState -> {
                    sharedViewModel.stateLoading.value = false
                    Common.showSnackBar(requireContext(), binding.root, getString(R.string.strThereIsProblem))
                    viewModel.setStateEvent(MaxMarkEvent.None)
                }
                is DataState.Connection -> {
                    sharedViewModel.stateLoading.value = false
                    Common.showSnackBar(requireContext(), binding.root, getString(R.string.strThereIsProblem))
                    viewModel.setStateEvent(MaxMarkEvent.None)
                }
            }
        }
    }
}