package org.mrlem.zikey.ui

import javafx.collections.FXCollections
import javafx.scene.control.ListView

// TODO - critical - implement real instruments list

class InstrumentsList : ListView<String>() {

    init {
        items = FXCollections.observableList(mutableListOf("Default instrument"))
        minWidth = 200.0
        maxWidth = 400.0
        maxHeight = Double.POSITIVE_INFINITY
    }

}
