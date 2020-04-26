package org.mrlem.zikey.ui

import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ComboBox
import org.mrlem.zikey.core.ZikeyCore
import javax.sound.midi.MidiDevice

class KeyboardsController : ZikeyCore.Listener {

    @FXML
    private lateinit var keyboards: ComboBox<MidiDevice.Info>

    private val observableKeyboards = FXCollections.observableList(mutableListOf<MidiDevice.Info>())

    @FXML
    fun initialize() {
        // TODO
    }

}
