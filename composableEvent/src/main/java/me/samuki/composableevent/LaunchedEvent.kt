package me.samuki.composableevent

import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
internal fun <T> LaunchedEvent(
    cartridge: EventCartridge<T>,
    block: suspend CoroutineScope.(T) -> Unit
) {
    val scope = rememberCoroutineScope()
    remember(cartridge.hashCode()) {
        LaunchedEventImpl(cartridge = cartridge, scope = scope, block = block)
    }
}

internal class LaunchedEventImpl<T>(
    private val cartridge: EventCartridge<T>,
    private val scope: CoroutineScope,
    private val block: suspend CoroutineScope.(T) -> Unit
) : RememberObserver {
    private var job: Job? = null

    override fun onAbandoned() {
        job?.cancel()
        job = null
    }

    override fun onForgotten() {
        job?.cancel()
        job = null
    }

    override fun onRemembered() {
        job = cartridge.value?.let { value ->
            scope.launch {
                block(value)
            }
        }
    }
}
