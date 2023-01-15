package dev.codewithdk.ktor.routes

import dev.codewithdk.ktor.UserSession
import dev.codewithdk.ktor.controller.UserOnboardController
import dev.codewithdk.ktor.exception.BadRequestException
import dev.codewithdk.ktor.exception.FailureMessages
import dev.codewithdk.ktor.exception.UnauthorizedActivityException
import dev.codewithdk.ktor.models.request.ChangPasswordRequest
import dev.codewithdk.ktor.models.request.CreatePasswordRequest
import dev.codewithdk.ktor.models.response.generateHttpResponse
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun Route.crudNotesy(userOnboardController: UserOnboardController) {

    authenticate {
        /**
         * Demonstrates CRUD operation for simple notes taking app
         * Supported format : Text, Image, Audio and Video
         */
        route("/notesy") {
            post("/create") {
                val userId = call.parameters["user_id"] ?: throw BadRequestException("Userid must not be empty")
                val changPasswordRequest = withContext(Dispatchers.IO) {
                    call.receive<ChangPasswordRequest>()
                }

                call.principal<UserSession>()
                    ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)

                val authResponse = userOnboardController.updatePassword(userId, changPasswordRequest)
                val response = generateHttpResponse(authResponse)

                call.respond(response.code, response.body)
            }

            post("/{id}/delete") {
                val userId = call.parameters["user_id"] ?: throw BadRequestException("User id must not be empty")
                val createPasswordRequest = withContext(Dispatchers.IO) {
                    call.receive<CreatePasswordRequest>()
                }

                call.principal<UserSession>()
                    ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)


                val authResponse = userOnboardController.createPassword(userId, createPasswordRequest)
                val response = generateHttpResponse(authResponse)

                call.respond(response.code, response.body)
            }

            post("/{id}/update") {
                // update user details
            }
            get("/{id}") {
                val userId = call.parameters["user_id"] ?: throw BadRequestException("Userid must not be empty")
                call.principal<UserSession>()
                    ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)

                val allUserView = userOnboardController.getUsersById(userId)

                print("allUserView==== $allUserView")
                val res = generateHttpResponse(allUserView)
                call.respond(res.code, res.body)
            }
        }

    }
}