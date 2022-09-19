package dev.codewithdk.ktor.models.response

/**
 * Created by DK on 16/11/20.
 */
interface Response  {
    val status: State
    val message: String
}

enum class State {
    SUCCESS, NOT_FOUND, FAILED, UNAUTHORIZED, INVALID
}
