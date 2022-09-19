package dev.codewithdk.ktor.models.response

import io.ktor.http.HttpStatusCode

sealed class HttpResponse<T : Response> {
    abstract val body: T
    abstract val code: HttpStatusCode

    data class Ok<T : Response>(override val body: T) : HttpResponse<T>() {
        override val code: HttpStatusCode = HttpStatusCode.OK
    }

    data class NotFound<T : Response>(override val body: T) : HttpResponse<T>() {
        override val code: HttpStatusCode = HttpStatusCode.NotFound
    }

    data class BadRequest<T : Response>(override val body: T) : HttpResponse<T>() {
        override val code: HttpStatusCode = HttpStatusCode.BadRequest
    }

    data class Unauthorized<T : Response>(override val body: T) : HttpResponse<T>() {
        override val code: HttpStatusCode = HttpStatusCode.Unauthorized
    }

    data class InternalServerError<T : Response>(override val body: T) : HttpResponse<T>(){
        override val code: HttpStatusCode = HttpStatusCode.InternalServerError
    }

    companion object {
        fun <T : Response> ok(response: T) = Ok(body = response)

        fun <T : Response> notFound(response: T) = NotFound(body = response)

        fun <T : Response> badRequest(response: T) = BadRequest(body = response)

        fun <T : Response> unauth(response: T) = Unauthorized(body = response)

        fun <T : Response> invalidRequest(response: T) = InternalServerError(body = response)
    }
}

fun generateHttpResponse(response: Response): HttpResponse<Response> {
    return when (response.status) {
        State.SUCCESS -> HttpResponse.ok(response)
        State.NOT_FOUND -> HttpResponse.notFound(response)
        State.FAILED -> HttpResponse.badRequest(response)
        State.UNAUTHORIZED -> HttpResponse.unauth(response)
        State.INVALID -> HttpResponse.invalidRequest(response)
    }
}