package org.mrlem.zikey.ui

import javafx.collections.FXCollections
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback
import javax.sound.midi.Instrument

/**
 * Component that displays the list of currently available instruments.
 */
class InstrumentsList : ListView<Instrument>() {

    private val observableInstruments = FXCollections.observableList(mutableListOf<Instrument>())

    init {
        placeholder = Label("No instrument")
        items = observableInstruments
        cellFactory = Callback<ListView<Instrument>, ListCell<Instrument>> { InstrumentCell() }
        minWidth = 200.0
        maxWidth = 400.0
        maxHeight = Double.POSITIVE_INFINITY
    }

    /**
     * Update the list of displayed instruments.
     */
    fun update(instruments: List<Instrument>) {
        observableInstruments.setAll(instruments)
    }

    private class InstrumentCell : ListCell<Instrument>() {
        override fun updateItem(item: Instrument?, empty: Boolean) {
            super.updateItem(item, empty)
            text = item?.name ?: ""
        }
    }

}
