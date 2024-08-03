package com.example.testtaskaura.ui.utils

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

internal fun <T : Any?> Flow<T>.bind(
    lifecycleOwner: LifecycleOwner,
    onError: suspend (Throwable) -> Unit = {
        Log.e("Error", it.stackTraceToString())
    },
    onNext: suspend (T) -> Unit = {},
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
            catch { onError(it) }.collect(onNext)
        }
    }
}
