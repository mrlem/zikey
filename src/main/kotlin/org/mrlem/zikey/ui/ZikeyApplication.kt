package org.mrlem.zikey.ui

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Label
import javafx.scene.control.SplitPane
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import org.mrlem.zikey.Strings
import org.mrlem.zikey.core.Status
import org.mrlem.zikey.core.ZikeyCore
import javax.sound.midi.Instrument

/**
 * Application UI.
 */
class ZikeyApplication : Application(), ZikeyCore.Listener {

    // ui components
    private var statusBar: Label? = null
    private var instrumentsList: InstrumentsList? = null

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    override fun start(stage: Stage) {
        stage.apply {
            title = Strings["app.name"]
            scene = Scene(createUi(), 640.0, 480.0).apply {
                stylesheets.add(ZikeyApplication::class.java.getResource("/ui-dark.css").toExternalForm())
            }
            show()
        }

        ZikeyCore.addListener(this)
        ZikeyCore.init()
    }

    override fun stop() {
        ZikeyCore.destroy()
    }

    ///////////////////////////////////////////////////////////////////////////
    // ZikeyCore listener
    ///////////////////////////////////////////////////////////////////////////

    override fun onStatusChanged(status: Status) {
        (status as? Status.Ready)
            ?.takeIf { it.defaultSoundBank }
            ?.run { showSoundBankAlert() }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal
    ///////////////////////////////////////////////////////////////////////////

    private fun createUi() = BorderPane().apply {
        top = ToolBar()
        center = SplitPane()
            .apply { setDividerPosition(0, 0.25) }
            .apply {
                items.addAll(
                    InstrumentsList().also { instrumentsList = it },
                    FeedbackPane()
                )
            }
        bottom = StatusBar()
            .also { statusBar = it }
    }

    private fun showSoundBankAlert() {
        Alert(Alert.AlertType.INFORMATION).apply {
            title = Strings["alert.default_soundbank.title"]
            headerText = Strings["alert.default_soundbank.header"]
            contentText = Strings["alert.default_soundbank.content"]
            showAndWait()
        }
    }

}
