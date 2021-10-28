package com.example.aleppocollage.ui.util.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.DialogFragmentLoadingBinding
import com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoadingDialogFragment : DialogFragment() {

    ///region ViewModel & Binding

    private val viewModel: LoadingViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: DialogFragmentLoadingBinding? = null
    private val binding get() = _binding!!

    ///endregion

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.popup_background_rounded)
        dialog!!.setCanceledOnTouchOutside(false)

        return inflater.inflate(R.layout.dialog_fragment_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = DialogFragmentLoadingBinding.bind(view)

        sharedViewModel.textLoading.observe(viewLifecycleOwner) {
            binding.txtDialogLoadingLoading.text = it

        }
        sharedViewModel.stateLoading.observe(viewLifecycleOwner) {
            if (!it) {
                findNavController().navigateUp()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
