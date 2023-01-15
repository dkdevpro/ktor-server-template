package dev.codewithdk.ktor.routes

import dev.codewithdk.ktor.UserSession
import dev.codewithdk.ktor.controller.UserOnboardController
import dev.codewithdk.ktor.models.request.ForgotPasswordRequest
import dev.codewithdk.ktor.models.request.UserLogin
import dev.codewithdk.ktor.models.request.UserOnboardRequest
import dev.codewithdk.ktor.models.response.AuthResponse
import dev.codewithdk.ktor.models.response.State
import dev.codewithdk.ktor.models.response.generateHttpResponse
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun Route.unAuthenticatedRoute(userOnboardController: UserOnboardController) {

    route("/auth"){
        post("/register") {
            val userOnboardRequest = withContext(Dispatchers.IO) {
                call.receive<UserOnboardRequest>()
            }

            val authResponse = userOnboardController.register(userOnboardRequest)
            if(authResponse.status == State.SUCCESS) {
                call.sessions.set(authResponse.userId?.let { it1 -> UserSession(it1, setOf("customer")) })
            }
            val response = generateHttpResponse(authResponse)
            call.respond(response.code, response.body)
        }

        post("/login") {
            val userOnboardRequest = withContext(Dispatchers.IO) {
                call.receive<UserLogin>()
            }
            val authResponse = userOnboardController.login(
                userOnboardRequest.emailId,
                userOnboardRequest.password,
                userOnboardRequest.userType
            )

            if(authResponse.status == State.SUCCESS) {
                call.sessions.set(authResponse.userId?.let { it1 -> UserSession(it1, setOf("customer")) })
            }

            val response = generateHttpResponse(authResponse)
            call.respond(response.code, response.body)
        }

        post("/forgotPassword") {
            // check userID and emailID
            // Trigger temp password into email
            val forgotPasswordRequest = withContext(Dispatchers.IO) {
                call.receive<ForgotPasswordRequest>()
            }

            if(forgotPasswordRequest.emailId.isEmpty() || forgotPasswordRequest.userType.isEmpty()){
                throw BadRequestException("Invalid request")
            }
            val authResponse =
                userOnboardController.forgotPassword(forgotPasswordRequest.emailId, forgotPasswordRequest.userType)
            val response = generateHttpResponse(authResponse)
            call.respond(response.code, response.body)
        }
    }


}