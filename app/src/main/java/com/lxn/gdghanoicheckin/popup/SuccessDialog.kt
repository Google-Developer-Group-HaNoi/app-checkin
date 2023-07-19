package com.lxn.gdghanoicheckin.popup

import android.annotation.SuppressLint
import android.os.Build
import androidx.navigation.fragment.findNavController
import com.lxn.gdghanoicheckin.R
import com.lxn.gdghanoicheckin.core.BaseDialog
import com.lxn.gdghanoicheckin.databinding.DialogSuccessBinding
import com.lxn.gdghanoicheckin.network.model.response.CheckInResponse
import com.lxn.gdghanoicheckin.utils.TypeAccount
import com.lxn.gdghanoicheckin.utils.callWithLifecycleCreated
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

class SuccessDialog : BaseDialog<DialogSuccessBinding>(DialogSuccessBinding::inflate) {

    companion object {
        const val KEY_CHECK_IN_RESPONSE = "KEY_CHECK_IN_RESPONSE"
    }

    override fun onStart() {
        super.onStart()
        dialog?.setCanceledOnTouchOutside(false)
    }

    @SuppressLint("SetTextI18n")
    override fun setUpView() {
        callWithLifecycleCreated { scope ->
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
                    val typeAccount: String = when (response.typeAccount) {
                        TypeAccount.CTV.type -> TypeAccount.CTV.label
                        TypeAccount.None.type -> TypeAccount.None.label
                        TypeAccount.Standard.type -> TypeAccount.Standard.label
                        TypeAccount.Vip.type -> TypeAccount.Vip.label
                        TypeAccount.Ambassador.type -> TypeAccount.Ambassador.label
                        TypeAccount.Media.type -> TypeAccount.Media.label
                        else -> ""
                    }
                    binding.tvTypeAccount.text = context?.getString(R.string.account_type,typeAccount)
                }
            }
            binding.btnAccept.clicks().onEach {
                findNavController().navigate(R.id.action_successDialog_to_scanFragment)
            }.launchIn(scope)
        }

    }
}