package dev.codewithdk.ktor.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm

open class KtorBoilerplateJWT private constructor(secret: String) {
    private val algorithm = Algorithm.HMAC256(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .build()

    /**
     * Generates JWT token from [userId].
     */

    fun logout(userId: String, userType: String): String = JWT
        .create()
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .withClaim(ClAIM, userId)
        .withClaim(ClAIM_USERTYPE, userType)
        .sign(algorithm)


    fun sign(userId: String, userType: String): String = JWT
        .create()
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .withClaim(ClAIM, userId)
        .withClaim(ClAIM_USERTYPE, userType)
        .sign(algorithm)

    companion object {
        lateinit var instance: KtorBoilerplateJWT
            private set

        fun initialize(secret: String) {
            synchronized(this) {
                if (!this::instance.isInitialized) {
                    instance = KtorBoilerplateJWT(secret)
                }
            }
        }

        private const val ISSUER = "Ktor-Boilerplate-JWT-Issuer"
        private const val AUDIENCE = "http://localhost.com"
        const val ClAIM = "user_id"
        const val ClAIM_USERTYPE = "user_type"
    }
}