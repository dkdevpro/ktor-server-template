package dev.codewithdk.ktor.models.response

import dev.codewithdk.ktor.data.model.User


data class UserResponse(
    override val status: State,
    override val message: String,
    val user: User? = null
) : Response {

    companion object {

        fun failed(message: String) = UserResponse(
            State.FAILED,
            message
        )

        fun unauthorized(message: String) = UserResponse(
            State.UNAUTHORIZED,
            message
        )

        fun invalid(message: String) = UserResponse(
            State.INVALID,
            message
        )

        fun success(user : User?) = UserResponse(
            State.SUCCESS,
            "success",
            user
        )

        fun succes(message: String) = UserResponse(
            State.SUCCESS,
            message,
        )
    }
}