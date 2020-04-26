package org.mrlem.zikey.core

import kotlinx.coroutines.*
import javax.sound.midi.MidiSystem
import javax.sound.midi.MidiUnavailableException
import javax.sound.midi.Transmitter

/**
 * Component that monitor MIDI keyboard's presence.
 */
class KeyboardMonitor {

    private var monitor: Job? = null
    var listener: Listener? = null
        set(value) {
            field = value
            monitor = if (listener == null) {
                monitor?.cancel()
                null
            } else {
                startMonitor()
            }
        }

    ///////////////////////////////////////////////////////////////////////////
    // Internal
    ///////////////////////////////////////////////////////////////////////////

    private fun startMonitor() = GlobalScope.launch(Dispatchers.Default) {
        while (true) {
            try {
                val transmitter = MidiSystem.getTransmitter()
                listener?.onKeyboardConnected(transmitter)
            } catch (e: MidiUnavailableException) {
                listener?.onKeyboardDisconnected()
            }
            delay(pollingRate)
        }
    }

    companion object {
        private const val pollingRate = 1000L // in milliseconds
    }

    interface Listener {

        fun onKeyboardConnected(transmitter: Transmitter)
        fun onKeyboardDisconnected()

    }

}
