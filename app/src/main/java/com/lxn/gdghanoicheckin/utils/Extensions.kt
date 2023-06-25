package com.lxn.gdghanoicheckin.utils

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.*

fun logError(data: Any) {
    Log.e("Namlxcntt", "Data Log -> $data")
}

fun Fragment.launchWithMain(call: suspend (coroutinesScope: CoroutineScope) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        withContext(Dispatchers.Main){
            call.invoke(this)
        }
    }
}

fun Fragment.callWithLifecycle(
    state: Lifecycle.State,
    call: suspend (coroutinesScope: CoroutineScope) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        if (!this.isActive) return@launch
        repeatOnLifecycle(state) {
            call.invoke(this)
        }
    }
}

fun Fragment.callWithLifecycleStarted(call: suspend (coroutinesScope: CoroutineScope) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        if (!this.isActive) return@launch
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            call.invoke(this)
        }
    }
}

fun Fragment.callWithLifecycleCreated(call: suspend (coroutinesScope: CoroutineScope) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        if (!this.isActive) return@launch
        repeatOnLifecycle(Lifecycle.State.CREATED) {
            call.invoke(this)
        }
    }
}

