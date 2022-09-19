package dev.codewithdk.ktor.models.response

/**
 * Created by DK on 16/11/20.
 */
data class AuthResponse(
    override val status: State,
    override val message: String,
    val token: String? = null,
    val userId: String? = null,
    val tempPwd: String? = null,
    val apiKey: String? = null,
    val isTempPwd: Boolean? = null,
) : Response {

    companion object {

        fun failed(message: String) = AuthResponse(
            State.FAILED,
            message
        )

        fun failed(message: String, errorCode : String) = AuthResponse(
            State.FAILED,
            message,
            errorCode
        )


        fun unauthorized(message: String) = AuthResponse(
            State.UNAUTHORIZED,
            message
        )

        fun invalid(message: String) = AuthResponse(
            State.INVALID,
            message
        )

        fun success(token: String, message: String, userId: String, apiKey: String?, isTempPwd: Boolean? = false, tempPwd: String? = null) = AuthResponse(
            status = State.SUCCESS,
            message = message,
            token = token,
            userId = userId,
            apiKey = apiKey,
            isTempPwd = isTempPwd,
            tempPwd = tempPwd
        )

        fun success(message: String) =AuthResponse(
            State.SUCCESS,
            message,
        )

        fun success(message: String, tempPwd : String) =AuthResponse(
            State.SUCCESS,
            message,
            tempPwd = tempPwd
        )
    }
}

