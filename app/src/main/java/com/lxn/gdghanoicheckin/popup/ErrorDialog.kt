package com.lxn.gdghanoicheckin.popup

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.lxn.gdghanoicheckin.R
import com.lxn.gdghanoicheckin.core.BaseDialog
import com.lxn.gdghanoicheckin.databinding.DialogErrorBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import reactivecircus.flowbinding.android.view.clicks

class ErrorDialog : BaseDialog<DialogErrorBinding>(DialogErrorBinding::inflate) {


    companion object {
        const val KEY_CHECK_IN_ERROR = "KEY_CHECK_IN_ERROR"
    }

    override fun setUpView() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                arguments?.let {
                    val message: String? = it.getString(KEY_CHECK_IN_ERROR)
                    message?.let { response ->
                        binding.tvDescription.text = response
                    }
                }
                binding.btnAccept.clicks().onEach {
                    findNavController().navigate(R.id.action_errorDialog_to_scanFragment)
                }.launchIn(this)
            }
        }
    }

}