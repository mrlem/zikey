package org.mrlem.zikey.ui

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.SplitPane
import javafx.scene.layout.*
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
    private var statusBar: Label? = null

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

        core = ZikeyCore(this)
    }

    override fun stop() {
        core?.destroy()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal
    ///////////////////////////////////////////////////////////////////////////

    private fun createUi() = BorderPane().also { root ->
        ToolBar().also { root.top = it }
        SplitPane()
            .apply { setDividerPosition(0, 0.25) }
            .apply { items.addAll(InstrumentsList(), FeedbackPane()) }
            .also { root.center = it }
        StatusBar()
            .also { statusBar = it }
            .also { root.bottom = it }
    }

    ///////////////////////////////////////////////////////////////////////////
    // ZikeyCore listener
    ///////////////////////////////////////////////////////////////////////////

    override fun onStatusChanged(status: Status) {
        statusBar?.text = status.label
    }

}
