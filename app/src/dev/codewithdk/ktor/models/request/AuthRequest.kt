package dev.codewithdk.ktor.models.request

/**
 * Created by DK on 16/11/20.
 */
data class AuthRequest(
    val emailId: String,
    val password: String,
    val userName: String,
    val mobile: String,
    val pincode: String,
    val userType : String
)