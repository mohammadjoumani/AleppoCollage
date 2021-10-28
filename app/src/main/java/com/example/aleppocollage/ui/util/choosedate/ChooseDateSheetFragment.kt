package com.example.aleppocollage.ui.util.choosedate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.SheetFragmentChooseDateBinding
import com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.time.microseconds

@AndroidEntryPoint
class ChooseDateSheetFragment : BottomSheetDialogFragment() {

    ///region ViewModel & Binding

    private val viewModel: ChooseDateViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: SheetFragmentChooseDateBinding? = null
    private val binding get() = _binding!!

    ///endregion


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sheet_fragment_choose_date, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = SheetFragmentChooseDateBinding.bind(view)

        binding.apply {

//            31-12-2021
            btnChoseDate.setOnClickListener {

                findNavController().navigateUp()

                val month = (datepiker.date.month + 1)
                val day = (datepiker.date.date)
                val year = datepiker.date.year + 1900

                Log.d("datepiker", "$day-$month-$year")

                sharedViewModel.date.value = "$day-$month-$year"
            }
        }

    }
}

