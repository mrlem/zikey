package org.mrlem.zikey.ui

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import org.mrlem.zikey.Strings
import org.mrlem.zikey.core.Core
import java.util.*

/**
 * Application lifecycle handler.
 */
class ZikeyApplication : Application(), Core.Listener {

    override fun start(stage: Stage) {
        val root: Parent = FXMLLoader.load(
            javaClass.getResource("/ui.fxml"),
            ResourceBundle.getBundle("strings", Locale.getDefault())
        )

        stage.apply {
            title = Strings["app.name"]
            scene = Scene(root, 640.0, 480.0).apply {
                stylesheets.add(ZikeyApplication::class.java.getResource("/theme-dark.css").toExternalForm())
            }
            show()
        }

        Core.init()
    }

    override fun stop() {
        Core.destroy()
    }

}
