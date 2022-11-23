package org.mrlem.zikey.core

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.sound.midi.*

/**
 * Component that monitor MIDI keyboard's presence.
 */
class KeyboardMonitor {

    private var keyboards: List<MidiDevice>? = null

    var listener: Listener? = null
        set(value) {
            field = value?.also { startMonitor() }
        }

    ///////////////////////////////////////////////////////////////////////////
    // Internal
    ///////////////////////////////////////////////////////////////////////////

    private fun startMonitor() = GlobalScope.launch {
        while (listener != null) {
            // find all input devices
            val newKeyboards = MidiSystem.getMidiDeviceInfo()
                .map { MidiSystem.getMidiDevice(it) }
                .filterNot { it is Sequencer || it is Synthesizer }
                .filterNot { it.maxTransmitters == 0 }

            // notify change, if any
            if (newKeyboards != keyboards) {
                keyboards = newKeyboards
                listener?.onKeyboardsChanged(newKeyboards)
            }

            // wait
            delay(pollingRate)
        }
    }

    companion object {
        private const val pollingRate = 1000L // in milliseconds
    }

    interface Listener {

        fun onKeyboardsChanged(keyboards: List<MidiDevice>)

    }

}
