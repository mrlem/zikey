package org.mrlem.zikey.core

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.sound.midi.*

/**
 * Component that monitor MIDI keyboard's presence.
 */
class KeyboardMonitor {

    private var keyboard: MidiDevice? = null

    var listener: Listener? = null
        set(value) {
            field = value?.also { startMonitor() }
        }

    ///////////////////////////////////////////////////////////////////////////
    // Internal
    ///////////////////////////////////////////////////////////////////////////

    private fun startMonitor() = GlobalScope.launch {
        while (listener != null) {
            val previousKeyboard = keyboard

            // find first input device
            val newKeyboard = MidiSystem.getMidiDeviceInfo()
                .map { MidiSystem.getMidiDevice(it) }
                .filterNot { it is Sequencer || it is Synthesizer }
                .filterNot { it.maxTransmitters == 0 }
                .firstOrNull()

            // take new device (if any) & open it (if not done already)
            keyboard = newKeyboard
                ?.apply { if (!isOpen) { open() } }

            // notify transmitter change
            when {
                previousKeyboard != null && newKeyboard == null ->
                    listener?.onKeyboardDisconnected()
                previousKeyboard == null && newKeyboard != null ->
                    listener?.onKeyboardConnected(newKeyboard.transmitter)
                previousKeyboard?.deviceInfo?.name != newKeyboard?.deviceInfo?.name -> {
                    listener?.onKeyboardDisconnected()
                    listener?.onKeyboardConnected(newKeyboard!!.transmitter)
                }
            }

            delay(pollingRate)
        }

        // close device, otherwise app won't exit
        keyboard
            ?.takeIf { it.isOpen }
            ?.run { close() }
    }

    companion object {
        private const val pollingRate = 1000L // in milliseconds
    }

    interface Listener {

        fun onKeyboardConnected(transmitter: Transmitter)
        fun onKeyboardDisconnected()

    }

}
