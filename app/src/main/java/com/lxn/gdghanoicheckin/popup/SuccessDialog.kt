package com.lxn.gdghanoicheckin.popup

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.lxn.gdghanoicheckin.R
import com.lxn.gdghanoicheckin.core.BaseDialog
import com.lxn.gdghanoicheckin.databinding.DialogSuccessBinding
import com.lxn.gdghanoicheckin.network.model.response.CheckInResponse
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import reactivecircus.flowbinding.android.view.clicks

class SuccessDialog : BaseDialog<DialogSuccessBinding>(DialogSuccessBinding::inflate) {

    companion object {
        const val KEY_CHECK_IN_RESPONSE = "KEY_CHECK_IN_RESPONSE"
    }

    override fun onStart() {
        super.onStart()
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun setUpView() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                arguments?.let {
                    val checkInResponse: CheckInResponse? = if (Build.VERSION.SDK_INT >= 33) {
                        it.getParcelable(KEY_CHECK_IN_RESPONSE, CheckInResponse::class.java)
                    } else {
                        it.getParcelable(KEY_CHECK_IN_RESPONSE)
                    }
                    checkInResponse?.let { response ->
                        binding.tvTitle.text = response.titleEvent
                        binding.tvDescription.text =
                            requireContext().getText(R.string.text_check_in_success)
                    }
                }
                binding.btnAccept.clicks().onEach {
                    findNavController().navigate(R.id.action_successDialog_to_scanFragment)
                }.launchIn(this)
            }
        }
    }
}