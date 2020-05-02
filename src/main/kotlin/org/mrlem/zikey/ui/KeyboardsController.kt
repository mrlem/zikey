package org.mrlem.zikey.ui

import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback
import org.mrlem.zikey.Strings
import org.mrlem.zikey.core.Core
import javax.sound.midi.MidiDevice

class KeyboardsController : Core.Listener {

    @FXML
    private lateinit var keyboards: ComboBox<MidiDevice?>

    private val observableKeyboards = FXCollections.observableList(mutableListOf<MidiDevice>())

    @FXML
    fun initialize() {
        keyboards.apply {
            items = observableKeyboards
            cellFactory = Callback<ListView<MidiDevice?>, ListCell<MidiDevice?>> { KeyboardCell() }
            selectionModel.selectedItemProperty().addListener { _, old, new ->
                if (old != new) { changeKeyboard(new) }
            }
        }
        Core.addListener(this)
    }

    ///////////////////////////////////////////////////////////////////////////
    // ZikeyCore.Listener
    ///////////////////////////////////////////////////////////////////////////

    override fun onKeyboardsChanged(keyboards: List<MidiDevice>) {
        observableKeyboards.setAll(keyboards)
    }

    override fun onKeyboardChanged(keyboard: MidiDevice?) {
        // update selected entry
        keyboards.apply {
            if (selectionModel.selectedItem != keyboard) {
                keyboards.value = keyboard
                buttonCell = KeyboardCell(keyboard)
                if (keyboard != null) {
                    selectionModel.select(keyboard)
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal
    ///////////////////////////////////////////////////////////////////////////

    private fun changeKeyboard(keyboard: MidiDevice?) {
        Core.select(keyboard)
    }

    private class KeyboardCell(item: MidiDevice? = null) : ListCell<MidiDevice?>() {

        init {
            text = item?.name ?: Strings["keyboards.empty"]
        }
        override fun updateItem(item: MidiDevice?, empty: Boolean) {
            super.updateItem(item, empty)
            text = if (empty) {
                Strings["keyboards.empty"]
            } else {
                item.name
            }
        }

    }

    companion object {
        private val MidiDevice?.name get() = this?.deviceInfo?.name ?: Strings["keyboards.unknown"]
    }

}
