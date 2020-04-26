package org.mrlem.zikey.ui

import javafx.scene.control.ListCell
import javax.sound.midi.Instrument

/**
 * Instruments list cell.
 */
class InstrumentCell : ListCell<Instrument>() {

    override fun updateItem(item: Instrument?, empty: Boolean) {
        super.updateItem(item, empty)
        text = item?.name ?: ""
    }

}
