package dev.codewithdk.ktor.plugins

import dev.codewithdk.ktor.AuthorizationException
import dev.codewithdk.ktor.exception.BadRequestException
import dev.codewithdk.ktor.exception.UnauthorizedActivityException
import dev.codewithdk.ktor.models.response.Response
import dev.codewithdk.ktor.models.response.State
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configStatusPages() {
    install(StatusPages) {
        exception<BadRequestException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, getResponse(State.FAILED, cause.message ?: "Bad request"))
        }

        exception<Throwable> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, cause.message ?: "Internal Server Error")
        }

        exception<UnauthorizedActivityException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized, cause.message)
        }
        exception<AuthorizationException> { call, cause ->
            call.respond(HttpStatusCode.Forbidden)
        }

        status(HttpStatusCode.InternalServerError) { call, cause ->
            call.respond(HttpStatusCode.InternalServerError)
        }

        status(HttpStatusCode.NotFound) { call, cause ->
            call.respond(HttpStatusCode.NotFound)
        }

        status(HttpStatusCode.Unauthorized) { call, cause ->
            call.respond(HttpStatusCode.Unauthorized)
        }
    }
}

fun getResponse(state: State, message: String): Response {
    return object : Response {
        override var status: State = state
        override var message: String = message
    }
}