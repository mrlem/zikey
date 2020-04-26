package org.mrlem.zikey.ui

import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Label
import org.mrlem.zikey.Strings
import org.mrlem.zikey.core.Status
import org.mrlem.zikey.core.ZikeyCore

/**
 * Root UI controller.
 */
class RootController : Label(), ZikeyCore.Listener {

    @FXML
    private lateinit var status: Label

    @FXML
    fun initialize() {
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
