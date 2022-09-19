package dev.codewithdk.ktor.plugins

import dev.codewithdk.ktor.data.dao.UserDao
import dev.codewithdk.ktor.UserSession
import dev.codewithdk.ktor.auth.KtorMinimalistJWT
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configAuthentication() {
    install(Authentication) {
        jwt {
            verifier(KtorMinimalistJWT.instance.verifier)
            validate {
                val userId = it.payload.getClaim(KtorMinimalistJWT.ClAIM).asString()
                val userType = it.payload.getClaim(KtorMinimalistJWT.ClAIM_USERTYPE).asString()
                when {
                    UserDao().isUserExists(userId,userType) -> {
                        UserSession(userId, setOf(userType))
                    } else -> {
                    null
                }
                }
            }

            /*skipWhen {
                    call -> call.principal<UserSession>() == null
            }*/

        }

//        session<UserSession>("authorization") {
//            challenge {
//                println("No valid session found for this route, redirecting to login form")
//                call.sessions.set(OriginalRequestURI(call.request.uri))
//                //call.respondRedirect("/login")
//            }
//            validate { session: UserSession ->
//                println( "User ${session.userId} logged in by existing session" )
//                session
//            }
//        }
    }
}