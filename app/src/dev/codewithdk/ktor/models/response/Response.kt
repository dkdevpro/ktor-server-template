package dev.codewithdk.ktor.models.response


interface Response  {
    val status: State
    val message: String
}

enum class State {
    SUCCESS, NOT_FOUND, FAILED, UNAUTHORIZED, INVALID
}
