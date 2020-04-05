package org.mrlem.zikey.ui

import javafx.geometry.Insets
import javafx.scene.control.Label

class StatusBar : Label() {

    init {
        id = "status"
        maxWidth = Double.POSITIVE_INFINITY
        padding = Insets(4.0)
    }

}
