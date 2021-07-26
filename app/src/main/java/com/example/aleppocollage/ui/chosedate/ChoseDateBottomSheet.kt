package com.example.aleppocollage.ui.chosedate

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.BottomSheetChoseDateBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.greenrobot.eventbus.EventBus
import java.util.*


class ChoseDateBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetChoseDateBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetChoseDateBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.datepiker.setCustomLocale(Locale.forLanguageTag("ar"))
        binding.btnChoseDate.setOnClickListener {
            val month = (binding.datepiker.date.month + 1)
            val day = (binding.datepiker.date.date)
            EventBus.getDefault().post("$day-$month")
            dismiss()
        }
        return view
    }
}

