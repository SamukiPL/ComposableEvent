package me.samuki.composableevent

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope

interface Event<T> {
    @Composable
    operator fun invoke(block: suspend CoroutineScope.(T) -> Unit)
}
