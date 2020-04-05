package org.mrlem.zikey.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.sound.midi.MidiSystem

/**
 * Logic core class.
 */
class ZikeyCore(private val listener: Listener) {

    private var synthesizer = MidiSystem.getSynthesizer()

    init {
        GlobalScope.launch {
            load()
        }
    }

    fun destroy() {
        synthesizer
            ?.takeIf { it.isOpen }
            ?.close()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal
    ///////////////////////////////////////////////////////////////////////////

    private fun load() {
        notifyStatus(Status.Loading)

        // create synthesizer
        synthesizer = synthesizer.apply {
            open()
            loadAllInstruments(defaultSoundbank)
            channels[0].programChange(12)
        }

        // connecting keyboard to synth
        MidiSystem.getTransmitter().receiver = synthesizer.receiver

        notifyStatus(Status.Ready)
    }

    private fun notifyStatus(status: Status) {
        GlobalScope.launch(Dispatchers.Main) {
            listener.onStatusChanged(status)
        }
    }

    /**
     * Listener to core events.
     */
    interface Listener {
        fun onStatusChanged(status: Status)
    }

}
