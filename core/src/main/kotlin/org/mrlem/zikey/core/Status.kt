package org.mrlem.zikey.core

/**
 * Core status.
 */
sealed class Status {

    /**
     * Status when core is loading.
     */
    object Loading : Status()

    /**
     * Status when core is ready.
     */
    data class Ready(val defaultSoundBank: Boolean = false) : Status() {
    }

    /**
     * Status when core encountered an error.
     *
     * @property reason the cause for this error.
     */
    data class Error(val reason: Reason) : Status() {

        enum class Reason {
            NO_KEYBOARD,
        }

    }

}
