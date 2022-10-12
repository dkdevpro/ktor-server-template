package dev.codewithdk.ktor.models.request


data class AuthRequest(
    val emailId: String,
    val password: String,
    val userName: String,
    val mobile: String,
    val pincode: String,
    val userType : String
)