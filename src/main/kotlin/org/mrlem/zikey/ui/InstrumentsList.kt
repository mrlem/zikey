package org.mrlem.zikey.ui

import javafx.collections.FXCollections
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback
import org.mrlem.zikey.core.ZikeyCore
import javax.sound.midi.Instrument

/**
 * Component that displays the list of currently available instruments.
 */
class InstrumentsList : ListView<Instrument>(), ZikeyCore.Listener {

    private val observableInstruments = FXCollections.observableList(mutableListOf<Instrument>())

    init {
        placeholder = Label("No instrument")
        items = observableInstruments
        cellFactory = Callback<ListView<Instrument>, ListCell<Instrument>> { InstrumentCell() }
        minWidth = 200.0
        maxWidth = 400.0
        maxHeight = Double.POSITIVE_INFINITY

        ZikeyCore.addListener(this)
        selectionModel.selectedItemProperty().addListener { _, old, new ->
            if (old != new) {
                ZikeyCore.select(new.patch.program)
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // ZikeyCore.Listener
    ///////////////////////////////////////////////////////////////////////////

    override fun onInstrumentChanged(program: Int) {
        val instrument = observableInstruments.firstOrNull { it.patch.program == program }
        if (selectionModel.selectedItem != instrument) {
            selectionModel.select(instrument)
            scrollTo(instrument)
        }
    }

    override fun onInstrumentsChanged(instruments: List<Instrument>) {
        observableInstruments.setAll(instruments)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal
    ///////////////////////////////////////////////////////////////////////////

    private class InstrumentCell : ListCell<Instrument>() {
        override fun updateItem(item: Instrument?, empty: Boolean) {
            super.updateItem(item, empty)
            text = item?.name ?: ""
        }
    }

}
