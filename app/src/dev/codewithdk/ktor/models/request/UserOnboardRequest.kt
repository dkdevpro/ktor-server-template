package dev.codewithdk.ktor.models.request

/**
 * Created by DK on 16/11/20.
 */
data class UserOnboardRequest(
    val emailId: String,
    val password: String,
    val userName: String,
    val mobile: String,
    val pincode: String,
    val userType : String
)

data class UserLogin(
    val emailId: String,
    val password: String,
    val userType : String
)

data class UserLogout(
    val userType : String
)


data class ForgotPasswordRequest(
    val emailId: String,
    val userType : String
)

data class ChangPasswordRequest(
    val currentPassword: String,
    val newPassword: String,
    val userType : String
)

data class CreatePasswordRequest(
    val password: String,
    val userType : String
)