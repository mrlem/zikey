package org.mrlem.zikey.ui

import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback
import org.mrlem.zikey.Strings
import org.mrlem.zikey.core.Status
import org.mrlem.zikey.core.ZikeyCore
import javax.sound.midi.Instrument

/**
 * Root UI controller.
 */
class RootController : Label(), ZikeyCore.Listener {

    @FXML
    private lateinit var status: Label
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

        ZikeyCore.addListener(this)
    }

    ///////////////////////////////////////////////////////////////////////////
    // ZikeyCore.Listener
    ///////////////////////////////////////////////////////////////////////////

    override fun onStatusChanged(status: Status) {
        this.status.text = status.label

        // show alert
        (status as? Status.Ready)
            ?.takeIf { it.defaultSoundBank }
            ?.run { showSoundBankAlert() }
    }

    override fun onInstrumentChanged(program: Int) {
        val instrument = observableInstruments.firstOrNull { it.patch.program == program }

        // update instruments list
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

    ///////////////////////////////////////////////////////////////////////////
    // UI events
    ///////////////////////////////////////////////////////////////////////////

    private fun changeInstrument(instrument: Instrument) {
        ZikeyCore.select(instrument.patch.program)
    }

    private fun showSoundBankAlert() {
        // TODO - use FXML here too
        Alert(Alert.AlertType.INFORMATION).apply {
            title = Strings["alert.default_soundbank.title"]
            headerText = Strings["alert.default_soundbank.header"]
            contentText = Strings["alert.default_soundbank.content"]
            showAndWait()
        }
    }

}
