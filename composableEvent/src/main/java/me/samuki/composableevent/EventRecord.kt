package me.samuki.composableevent

import androidx.compose.runtime.snapshots.StateRecord

internal class EventRecord<T> : StateRecord() {
    var value: EventCartridge<T> = EventCartridge()

    override fun assign(value: StateRecord) {
        @Suppress("UNCHECKED_CAST")
        this.value = (value as EventRecord<T>).value
    }

    override fun create(): StateRecord = EventRecord<T>()
}
