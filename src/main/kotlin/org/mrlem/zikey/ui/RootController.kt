package org.mrlem.zikey.ui

import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Label
import org.mrlem.zikey.Strings
import org.mrlem.zikey.core.Status
import org.mrlem.zikey.core.Core

/**
 * Root UI controller.
 */
class RootController : Label(), Core.Listener {

    @FXML
    private lateinit var status: Label

    @FXML
    fun initialize() {
        Core.addListener(this)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Core.Listener
    ///////////////////////////////////////////////////////////////////////////

    override fun onStatusChanged(status: Status) {
        this.status.text = status.label

        // show alert
        (status as? Status.Ready)
            ?.takeIf { it.defaultSoundBank }
            ?.run { showSoundBankAlert() }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal
    ///////////////////////////////////////////////////////////////////////////

    private fun showSoundBankAlert() {
        Alert(Alert.AlertType.INFORMATION).apply {
            title = Strings["alert.default_soundbank.title"]
            headerText = Strings["alert.default_soundbank.header"]
            contentText = Strings["alert.default_soundbank.content"]
            showAndWait()
        }
    }

}
