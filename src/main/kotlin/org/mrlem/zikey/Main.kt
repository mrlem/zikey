package org.mrlem.zikey

import javax.sound.midi.MidiSystem

fun main() {
    println("creating synth")
    val synthesizer = MidiSystem.getSynthesizer()
    synthesizer.open()

    println("loading default sound bank")
    val soundBank = synthesizer.defaultSoundbank
    synthesizer.loadAllInstruments(soundBank)

    println("setting instrument")
    val channels = synthesizer.channels
    channels[0].programChange(12)

    println("connecting keyboard to synth")
    MidiSystem.getTransmitter().receiver = synthesizer.receiver

    println("ready")
}
