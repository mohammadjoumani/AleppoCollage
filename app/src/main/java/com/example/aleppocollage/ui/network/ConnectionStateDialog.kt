package com.example.aleppocollage.ui.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.DialogConnectionStateBinding

class ConnectionStateDialog : DialogFragment() {

    private var _binding: DialogConnectionStateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogConnectionStateBinding.inflate(inflater, container, false)
        val view = binding.root
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.popup_background_rounded)
        dialog!!.setCanceledOnTouchOutside(false)

        return view
    }
}