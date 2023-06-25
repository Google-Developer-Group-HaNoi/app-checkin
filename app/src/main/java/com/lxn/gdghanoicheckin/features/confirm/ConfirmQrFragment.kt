package com.lxn.gdghanoicheckin.features.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.lxn.gdghanoicheckin.R
import com.lxn.gdghanoicheckin.databinding.FragmentConfirmQrBinding
import com.lxn.gdghanoicheckin.popup.ErrorDialog
import com.lxn.gdghanoicheckin.popup.SuccessDialog
import com.lxn.gdghanoicheckin.utils.callWithLifecycle
import com.lxn.gdghanoicheckin.utils.callWithLifecycleCreated
import com.lxn.gdghanoicheckin.utils.logError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConfirmQrFragment : Fragment() {

    private lateinit var fragmentConfirmQrBinding: FragmentConfirmQrBinding

    private val viewModel : ConfirmViewModel by viewModels()

    companion object{
        const val KEY = "KEY"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentConfirmQrBinding = FragmentConfirmQrBinding.inflate(inflater,container,false)
        return fragmentConfirmQrBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callWithLifecycleCreated {
            viewModel.uiState.onEach { state ->
                when (state) {
                    is ConfirmViewModel.ConfirmQrState.Error -> {
                        popLoadingDialog()
                        findNavController().navigate(R.id.errorDialog, bundleOf(ErrorDialog.KEY_CHECK_IN_ERROR to state.message))
                    }
                    ConfirmViewModel.ConfirmQrState.Loading -> {
                        findNavController().navigate(R.id.loadingDialog)
                    }
                    is ConfirmViewModel.ConfirmQrState.Success -> {
                        popLoadingDialog()
                        findNavController().navigate(R.id.successDialog, bundleOf(SuccessDialog.KEY_CHECK_IN_RESPONSE to state.body))
                    }
                    else -> {
                    }
                }
            }.catch {
                logError(it.message.toString())
            }.launchIn(it)
        }
    }

    private suspend fun popLoadingDialog(){
        val currentFragment = findNavController().currentDestination
        currentFragment?.let {
            delay(100)
            if (it.displayName.contains("loadingDialog")){
                findNavController().popBackStack()
            }
        }
    }
}