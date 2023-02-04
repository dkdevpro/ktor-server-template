package dev.codewithdk.ktor

import dev.codewithdk.ktor.auth.KtorBoilerplateJWT
import dev.codewithdk.ktor.data.database.initDatabase
import dev.codewithdk.ktor.di.DaggerControllerComp
import dev.codewithdk.ktor.plugins.*
import dev.codewithdk.ktor.routes.unAuthenticatedRoute
import dev.codewithdk.ktor.routes.userOnboard
import dev.codewithdk.ktor.utils.KeyProvider
import io.ktor.client.plugins.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.swagger.codegen.v3.generators.html.StaticHtmlCodegen
import kotlin.time.Duration.Companion.seconds

data class UserSession(val userId: String, val roles: Set<String> = emptySet()) : Principal
data class OriginalRequestURI(val uri: String)

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    with(Config(environment.config)) {
        initWithSecret(SECRET_KEY)
        initDatabase(
            host = "localhost",
            port = "5432",
            databaseName = "ktor-boilerplate",
            user = "postgres",
            password = "postgres"
        )
    }

    //configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configAuthentication()
    configureStatusPages()

    install(RateLimit){
        global {
            rateLimiter(limit = 10, refillPeriod = 60.seconds)
        }
    }

    install(Sessions) {
        cookie<UserSession>("ktor_session_cookie", storage = SessionStorageMemory())
        cookie<OriginalRequestURI>("original_request_cookie")
    }

    install(RoleBasedAuthorization) {
        getRoles { (it as UserSession).roles }
    }

    val controllers = DaggerControllerComp.create()

    routing {

        route("/v1.0") {
            // Need to add role based approach
            route("/customer") {
                intercept(ApplicationCallPipeline.Setup){
                    val header = call.request.header("X-API-Key")
                }
                withRole("customer") {
                    userOnboard(controllers.userOnboardController())
                }

            }
            unAuthenticatedRoute(controllers.userOnboardController())
        }

        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml") {
            version = "4.15.5"
        }
        openAPI(path="openapi", swaggerFile = "openapi/documentation.yaml") {
            codegen = StaticHtmlCodegen()
        }
    }
}

fun initWithSecret(secretKey: String) {
    KtorBoilerplateJWT.initialize(secretKey)
    KeyProvider.initialize(secretKey)
}

