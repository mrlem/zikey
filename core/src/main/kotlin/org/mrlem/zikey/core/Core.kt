package org.mrlem.zikey.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import javax.sound.midi.*

/**
 * Logic core class.
 */
object Core {

    private var synthesizer = MidiSystem.getSynthesizer()
    private var keyboard: MidiDevice? = null
    private var listeners = mutableListOf<Listener>()

    private lateinit var readyStatus: Status.Ready
    private val errorStatus = Status.Error(Status.Error.Reason.NO_KEYBOARD)

    private val keyboardMonitor = KeyboardMonitor()
    private val keyboardListener = object : KeyboardMonitor.Listener {
        override fun onKeyboardsChanged(keyboards: List<MidiDevice>) {
            if (!keyboards.contains(keyboard)) {
                notifyKeyboardChanged(null)
            }

            notifyKeyboardsChanged(keyboards)

            // force selection if required
            val nothingSelected = keyboard == null
            val noChoice = keyboards.size <= 1
            if (nothingSelected || noChoice) select(keyboards.firstOrNull())

            // update status
            val nothingToSelect = keyboards.isEmpty()
            if (nothingToSelect) {
                notifyStatus(errorStatus)
            } else {
                notifyStatus(readyStatus)
            }
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
        // make sure the keyboard gets closed, so app exits
        keyboard?.close()

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
            Prefs.lastProgram = program
        }
    }

    fun select(keyboard: MidiDevice?) {
        if (keyboard != this.keyboard) {
            // close previous keyboard
            this.keyboard?.apply {
                close()
            }

            // open new keyboard
            this.keyboard = keyboard?.apply {
                if (!isOpen) open()
            }

            keyboard?.transmitter?.receiver = synthesizer.receiver
        }

        notifyKeyboardChanged(keyboard)
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
            select(Prefs.lastProgram)
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

    private fun notifyKeyboardChanged(keyboard: MidiDevice?) {
        GlobalScope.launch(Dispatchers.Main) {
            listeners.forEach { it.onKeyboardChanged(keyboard) }
        }
    }

    private fun notifyKeyboardsChanged(keyboards: List<MidiDevice>) {
        GlobalScope.launch(Dispatchers.Main) {
            listeners.forEach { it.onKeyboardsChanged(keyboards) }
        }
    }

    /**
     * Listener to core events.
     */
    interface Listener {
        fun onStatusChanged(status: Status) {}
        fun onInstrumentChanged(program: Int) {}
        fun onInstrumentsChanged(instruments: List<Instrument>) {}
        fun onKeyboardChanged(keyboard: MidiDevice?) {}
        fun onKeyboardsChanged(keyboards: List<MidiDevice>) {}
    }

}
