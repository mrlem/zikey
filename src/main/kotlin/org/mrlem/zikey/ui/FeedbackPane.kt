package org.mrlem.zikey.ui

import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.text.TextAlignment

// TODO - critical - implement real feedback pane

class FeedbackPane : Label("Feedback") {

    init {
        styleClass.add("pane")
        maxWidth = Double.POSITIVE_INFINITY
        maxHeight = Double.POSITIVE_INFINITY
        textAlignment = TextAlignment.CENTER
        HBox.setHgrow(this, Priority.ALWAYS)
    }

}
