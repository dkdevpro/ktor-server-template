package dev.codewithdk.ktor.exception

object FailureMessages {
    const val MESSAGE_MISSING_CREDENTIALS = "Required 'email-id' or 'password' missing."
    const val MESSAGE_MISSING_NOTE_DETAILS = "Required 'title' or 'note' missing."

    const val MESSAGE_ACCESS_DENIED = "Access Denied!"
    const val MESSAGE_FAILED = "Something went wrong!"
}