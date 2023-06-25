package com.lxn.gdghanoicheckin.features.confirm

import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import com.lxn.gdghanoicheckin.core.*
import com.lxn.gdghanoicheckin.network.model.response.CheckInResponse
import com.lxn.gdghanoicheckin.repository.CheckInRepository
import com.lxn.gdghanoicheckin.utils.logError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ConfirmViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val checkInRepository: CheckInRepository
) : BaseViewModel<ConfirmViewModel.ConfirmQrState, ConfirmViewModel.ConfirmQrEvent, ConfirmViewModel.ConfirmQrEffect>() {

    override val createInitialState: ConfirmQrState = ConfirmQrState.Default

    init {
        val data = savedStateHandle.get<String>(ConfirmQrFragment.KEY)
        if (!data.isNullOrEmpty()) {
            sendEvent(ConfirmQrEvent.ConfirmQRCode(data))
        }
    }

    override fun handleEvent(event: ConfirmQrEvent) {
        when (event) {
            is ConfirmQrEvent.ConfirmQRCode -> {
                handleConfirmCheckIn(event)
            }
        }
    }

    private fun isValidEmail(qrCode: String?) =
        !qrCode.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(qrCode).matches()


    private fun handleConfirmCheckIn(event: ConfirmQrEvent.ConfirmQRCode) {
        val qrCode = event.qrCode
        if (!isValidEmail(qrCode)) {
            setState(ConfirmQrState.Error("QR Sai định dạng"))
        } else {
            checkInRepository.checkInQRCode(qrCode)
                .map { it ->
                    return@map when (it) {
                        is Either.Left -> {
                            ConfirmQrState.Success(it.a)
                        }
                        is Either.Right -> {
                            ConfirmQrState.Error(it.b.message ?: "")
                        }
                    }
                }.onStart { emit(ConfirmQrState.Loading) }
                .flowOn(Dispatchers.IO)
                .onEach {
                    updateState(it)
                }.catch {
                    logError(it.message.toString())
                }.launchIn(coroutineScope)
        }

    }


    sealed class ConfirmQrEvent : ViewEvent {
        data class ConfirmQRCode(val qrCode: String) : ConfirmQrEvent()
    }

    sealed class ConfirmQrState : ViewState {

        object Default : ConfirmQrState()
        data class Success(val body: CheckInResponse) : ConfirmQrState()
        data class Error(val message: String) : ConfirmQrState()

        object Loading : ConfirmQrState()
    }

    sealed class ConfirmQrEffect : ViewEffect
}
