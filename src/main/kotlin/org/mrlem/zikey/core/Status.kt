package org.mrlem.zikey.core

import org.mrlem.zikey.Strings

/**
 * Core status.
 */
sealed class Status {

    /**
     * Status when core is loading.
     */
    object Loading : Status() {
        override val label = Strings["status.loading"]
    }

    /**
     * Status when core is ready.
     */
    data class Ready(val defaultSoundBank: Boolean = false) : Status() {
        override val label = Strings["status.ready"]
    }

    /**
     * Status when core encountered an error.
     *
     * @property message the error message.
     */
    data class Error(val message: String) : Status() {
        override val label = Strings["status.error"].format(message)
    }

    /**
     * Status label (translated).
     */
    abstract val label: String

}
