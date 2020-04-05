package org.mrlem.zikey.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
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
        var soundbankLoaded = false
        synthesizer = synthesizer.apply {
            try {
                val soundBank = MidiSystem.getSoundbank(File("/usr/share/sounds/sf2/FluidR3_GM.sf2"))
                if (!isSoundbankSupported(soundBank)) { throw Exception("unsupported soundbank, using default") }
                unloadAllInstruments(defaultSoundbank)
                loadAllInstruments(soundBank)
                soundbankLoaded = true
            } catch (e: Exception) {
                // nothing
            }

            open()
            channels[0].programChange(12)
        }

        // connecting keyboard to synth
        try {
            MidiSystem.getTransmitter().receiver = synthesizer.receiver
            notifyStatus(Status.Ready(defaultSoundBank = !soundbankLoaded))
        } catch (e: Exception) {
            e.printStackTrace()
            notifyStatus(Status.Error("$e"))
        }
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
