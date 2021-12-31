package com.example.aleppocollage.ui.util.entermark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.DialogFragmentLoadingBinding
import com.example.aleppocollage.databinding.SheetFragmentEnterMarkBinding
import com.example.aleppocollage.ui.mark.teacher.MarkTeacherFragmentArgs
import com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModel
import com.example.aleppocollage.util.Common
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EnterMarkSheetFragment : BottomSheetDialogFragment() {

    private val args: EnterMarkSheetFragmentArgs by navArgs()

    ///region ViewModel & Binding

    private val viewModel: EnterMarkViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: SheetFragmentEnterMarkBinding? = null
    private val binding get() = _binding!!

    ///endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sheet_fragment_enter_mark, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = SheetFragmentEnterMarkBinding.bind(view)

        binding.apply {

            txtEnterMarkNameStudent.text = args.name

            btnEnterMark.setOnClickListener {
                val value = edtEnterMarkValue.text.toString()
                if (value == "") {
                    Common.showToast(requireActivity(),"أدخل العلامة","error")
                    return@setOnClickListener
                }
                if (value.toInt() > args.maxMark){
                    Common.showToast(requireActivity(),"يجب أن لا تتجاوز العلامة التامة","warning")
                    return@setOnClickListener
                }
                sharedViewModel.markValue.value = value.toInt()
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
