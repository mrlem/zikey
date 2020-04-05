package org.mrlem.zikey.ui

import javafx.geometry.Insets
import javafx.scene.control.Label
import org.mrlem.zikey.core.Status
import org.mrlem.zikey.core.ZikeyCore

class StatusBar : Label(), ZikeyCore.Listener {

    init {
        id = "status"
        maxWidth = Double.POSITIVE_INFINITY
        padding = Insets(4.0)

        ZikeyCore.addListener(this)
    }

    ///////////////////////////////////////////////////////////////////////////
    // ZikeyCore.Listener
    ///////////////////////////////////////////////////////////////////////////

    override fun onStatusChanged(status: Status) {
        text = status.label
    }

}
