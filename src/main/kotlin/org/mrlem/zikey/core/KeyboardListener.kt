package org.mrlem.zikey.core

import javax.sound.midi.Transmitter

interface KeyboardListener {

    fun onKeyboardConnected(transmitter: Transmitter)
    fun onKeyboardDisconnected()

}
