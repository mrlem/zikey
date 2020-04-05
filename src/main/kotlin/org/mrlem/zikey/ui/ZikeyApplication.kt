package org.mrlem.zikey.ui

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import org.mrlem.zikey.Strings
import org.mrlem.zikey.core.Status
import org.mrlem.zikey.core.ZikeyCore

/**
 * Application UI.
 */
class ZikeyApplication : Application(), ZikeyCore.Listener {

    private var core: ZikeyCore? = null

    // ui components
    private var statusLabel: Label? = null

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    override fun start(stage: Stage) {
        stage.apply {
            title = Strings["app.name"]

            val root = StackPane()
            statusLabel = Label().also { root.children.add(it) }

            scene = Scene(root, 640.0, 480.0)
            show()
        }

        core = ZikeyCore(this)
    }

    override fun stop() {
        core?.destroy()
    }

    ///////////////////////////////////////////////////////////////////////////
    // ZikeyCore listener
    ///////////////////////////////////////////////////////////////////////////

    override fun onStatusChanged(status: Status) {
        statusLabel?.text = status.label
    }

}
