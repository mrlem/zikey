package org.mrlem.zikey.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.mrlem.zikey.Strings
import java.io.File
import javax.sound.midi.*

/**
 * Logic core class.
 */
object ZikeyCore {

    private var synthesizer = MidiSystem.getSynthesizer()
    private var listeners = mutableListOf<Listener>()

    private lateinit var readyStatus: Status.Ready
    private val errorStatus = Status.Error(Strings["error.nokeyboard"])

    private val keyboardMonitor = KeyboardMonitor()
    private val keyboardListener = object : KeyboardMonitor.Listener {
        override fun onKeyboardConnected(transmitter: Transmitter) {
            transmitter.receiver = synthesizer.receiver
            notifyStatus(readyStatus)
        }

        override fun onKeyboardDisconnected() {
            notifyStatus(errorStatus)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    fun init() {
        GlobalScope.launch {
            load()
        }
    }

    fun destroy() {
        // make sure the monitor stops, so app exits
        keyboardMonitor.listener = null
    }

    ///////////////////////////////////////////////////////////////////////////
    // Control methods
    ///////////////////////////////////////////////////////////////////////////

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun select(program: Int) {
        if (synthesizer.channels[0].program != program) {
            synthesizer.channels[0].programChange(program)
            notifyInstrumentChanged(program)
            ZikeyPrefs.lastProgram = program
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal
    ///////////////////////////////////////////////////////////////////////////

    private fun load() {
        notifyStatus(Status.Loading)

        // create synthesizer
        var soundBank = synthesizer.defaultSoundbank
        synthesizer = synthesizer.apply {
            try {
                MidiSystem.getSoundbank(File("/usr/share/sounds/sf2/FluidR3_GM.sf2")).let {
                    if (!isSoundbankSupported(it)) { throw Exception("unsupported soundbank, using default") }
                    unloadAllInstruments(defaultSoundbank)
                    loadAllInstruments(it)
                    soundBank = it
                }
            } catch (e: Exception) {
                // nothing
            }
            notifyInstrumentsChanged(availableInstruments.asList())

            open()

            // restore previously saved instrument, else first instrument
            select(ZikeyPrefs.lastProgram)
        }
        readyStatus = Status.Ready(soundBank == synthesizer.defaultSoundbank)

        // listen for devices
        keyboardMonitor.listener = keyboardListener
    }

    private fun notifyStatus(status: Status) {
        GlobalScope.launch(Dispatchers.Main) {
            listeners.forEach { it.onStatusChanged(status) }
        }
    }

    private fun notifyInstrumentChanged(program: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            listeners.forEach { it.onInstrumentChanged(program) }
        }
    }

    private fun notifyInstrumentsChanged(instruments: List<Instrument>) {
        GlobalScope.launch(Dispatchers.Main) {
            listeners.forEach { it.onInstrumentsChanged(instruments) }
        }
    }

    /**
     * Listener to core events.
     */
    interface Listener {
        fun onStatusChanged(status: Status) {}
        fun onInstrumentChanged(program: Int) {}
        fun onInstrumentsChanged(instruments: List<Instrument>) {}
    }

}
