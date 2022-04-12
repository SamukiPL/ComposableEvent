package me.samuki.composableevent

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.StateObject
import androidx.compose.runtime.snapshots.StateRecord
import androidx.compose.runtime.snapshots.readable
import androidx.compose.runtime.snapshots.withCurrent
import androidx.compose.runtime.snapshots.writable
import kotlinx.coroutines.CoroutineScope

fun <T> mutableEventOf() = MutableEvent<T>()

class MutableEvent<T> internal constructor(): Event<T>, StateObject {

    private var value: EventCartridge<T>
        get() = next.readable(this).value
        set(value) = next.withCurrent {
            next.writable(this) {
                this.value = value
            }
        }

    private var next: EventRecord<T> = EventRecord()

    override val firstStateRecord: StateRecord
        get() = next

    override fun prependStateRecord(value: StateRecord) {
        @Suppress("UNCHECKED_CAST")
        next = value as EventRecord<T>
    }

    private fun resetRecord() {
        prependStateRecord(EventRecord<T>())
    }

    @Composable
    override operator fun invoke(block: suspend CoroutineScope.(T) -> Unit) {
        remember(Unit) {
            resetRecord()
        }
        LaunchedEvent(value, block)
    }

    fun sendEvent(event: T) {
        this.value = EventCartridge(event)
    }
}
