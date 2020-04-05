package org.mrlem.zikey.ui

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.ToolBar
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority

class ToolBar : ToolBar() {

    init {
        Pane()
            .also { HBox.setHgrow(it, Priority.ALWAYS) }
            .also { items.add(it) }
        // TODO - critical - implement real sound bank selector
        Label("Sound bank")
            .apply { alignment = Pos.CENTER_RIGHT }
            .also { items.add(it) }
    }

}
