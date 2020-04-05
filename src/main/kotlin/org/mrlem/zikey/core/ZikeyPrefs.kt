package org.mrlem.zikey.core

import java.util.prefs.Preferences

object ZikeyPrefs {

    private var prefs = Preferences.userNodeForPackage(ZikeyCore::class.java)

    var lastProgram: Int
        get() = prefs.getInt("lastProgram", 0)
        set(value) = prefs.putInt("lastProgram", value)

}
