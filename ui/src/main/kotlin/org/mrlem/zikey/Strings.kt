package org.mrlem.zikey

import org.mrlem.zikey.core.Status
import java.util.*

/**
 * Accessor for all translated strings.
 */
object Strings {

    private val resources = ResourceBundle.getBundle("strings", Locale.getDefault())

    operator fun get(key: String): String = resources.getString(key)

}

val Status.label: String
    get() = when (this) {
        is Status.Loading -> Strings["status.loading"]
        is Status.Ready -> Strings["status.ready"]
        is Status.Error -> when (reason) {
            Status.Error.Reason.NO_KEYBOARD -> Strings["error.nokeyboard"]
        }
            .let { Strings["status.error"].format(it) }
    }