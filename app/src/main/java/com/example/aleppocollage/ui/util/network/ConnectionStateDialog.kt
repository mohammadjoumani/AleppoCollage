package com.example.aleppocollage.ui.util.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.DialogFragmentConnectionStateBinding
import com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConnectionStateDialog : DialogFragment() {

    ///region ViewModel & Binding

    private val viewModel: ConnectionViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: DialogFragmentConnectionStateBinding? = null
    private val binding get() = _binding!!

    ///endregion

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.bg_rounded_dialog)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setCancelable(false)
        return inflater.inflate(R.layout.dialog_fragment_connection_state, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = DialogFragmentConnectionStateBinding.bind(view)

        sharedViewModel.isDissconnect.observe(viewLifecycleOwner) {
            if (!it) {
                findNavController().navigateUp()
            }
        }
    }
}