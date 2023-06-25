package com.lxn.gdghanoicheckin.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<VS : ViewState, VE : ViewEvent, VF : ViewEffect> : ViewModel() {

    private val _initState: VS by lazy { createInitialState }

    val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    val currentState: VS get() = _uiState.value
    abstract val createInitialState: VS

    private val _uiState: MutableStateFlow<VS> = MutableStateFlow(_initState)
    val uiState = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<VE> = MutableSharedFlow()
    val event = _event.asSharedFlow()

    private val _effect: Channel<VF> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        subscribeEvents()
    }

    protected fun setState(VS: VS) {
        _uiState.value = VS
    }

    protected fun updateState(VS: VS){
        _uiState.update { VS }
    }

    fun sendEvent(viewEvent: VE) {
        coroutineScope.launch {
            _event.emit(viewEvent)
        }
    }

    private fun subscribeEvents() {
        coroutineScope.launch {
            _event.collect(::handleEvent)
        }
    }

    override fun onCleared() {
        super.onCleared()
        _effect.close()
    }

    abstract fun handleEvent(event: VE)

}
