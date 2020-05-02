package org.mrlem.zikey.ui

import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback
import org.mrlem.zikey.core.Core
import javax.sound.midi.Instrument

class InstrumentsController : Core.Listener {

    @FXML
    private lateinit var instruments: ListView<Instrument>

    private val observableInstruments = FXCollections.observableList(mutableListOf<Instrument>())

    @FXML
    fun initialize() {
        instruments.apply {
            items = observableInstruments
            cellFactory = Callback<ListView<Instrument>, ListCell<Instrument>> { InstrumentCell() }
            selectionModel.selectedItemProperty().addListener { _, old, new ->
                if (old != new) { changeInstrument(new) }
            }
        }

        Core.addListener(this)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Core.Listener
    ///////////////////////////////////////////////////////////////////////////

    override fun onInstrumentChanged(program: Int) {
        val instrument = observableInstruments.firstOrNull { it.patch.program == program }

        // update selected entry
        instruments.apply {
            if (selectionModel.selectedItem != instrument) {
                selectionModel.select(instrument)
                scrollTo(instrument)
            }
        }
    }

    override fun onInstrumentsChanged(instruments: List<Instrument>) {
        observableInstruments.setAll(instruments)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal
    ///////////////////////////////////////////////////////////////////////////

    private fun changeInstrument(instrument: Instrument) {
        Core.select(instrument.patch.program)
    }

    private class InstrumentCell : ListCell<Instrument>() {

        override fun updateItem(item: Instrument?, empty: Boolean) {
            super.updateItem(item, empty)
            text = item?.name ?: ""
        }

    }

}
