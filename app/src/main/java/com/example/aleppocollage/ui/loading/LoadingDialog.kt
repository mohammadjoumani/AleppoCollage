package com.example.aleppocollage.ui.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.DialogConnectionStateBinding
import com.example.aleppocollage.databinding.DialogLoadingBinding

class LoadingDialog : DialogFragment() {

    private var _binding: DialogLoadingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogLoadingBinding.inflate(inflater, container, false)
        val view = binding.root
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.popup_background_rounded)
        dialog!!.setCanceledOnTouchOutside(false)

        return view
    }
}