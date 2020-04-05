package org.mrlem.zikey

import java.util.*

/**
 * Accessor for all translated strings.
 */
object Strings {

    private val resources = ResourceBundle.getBundle("strings", Locale.getDefault())

    operator fun get(key: String): String = resources.getString(key)

}
