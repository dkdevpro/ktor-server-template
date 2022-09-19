package dev.codewithdk.ktor.controller

import dev.codewithdk.ktor.auth.KtorMinimalistJWT
import dev.codewithdk.ktor.enum.UserType
import dev.codewithdk.ktor.exception.BadRequestException
import dev.codewithdk.ktor.exception.UnauthorizedActivityException
import dev.codewithdk.ktor.models.request.ChangPasswordRequest
import dev.codewithdk.ktor.models.request.CreatePasswordRequest
import dev.codewithdk.ktor.models.request.UserOnboardRequest
import dev.codewithdk.ktor.models.response.AuthResponse
import dev.codewithdk.ktor.models.response.UserResponse
import dev.codewithdk.ktor.utils.*
import dev.codewithdk.ktor.data.dao.UserDao
import java.util.*
import javax.inject.Inject

class UserOnboardController @Inject constructor(private val userDao: UserDao) {

    private val jwt = KtorMinimalistJWT.instance

    fun register(userOnboardRequest: UserOnboardRequest): AuthResponse {
        return try {
            validateCredentialsOrThrowException(
                userOnboardRequest.emailId,
                userOnboardRequest.password,
                userOnboardRequest.userType
            )

            if (!userDao.isEmailIdAlreadyExists(userOnboardRequest.emailId, userOnboardRequest.userType)) {
                throw BadRequestException("Email-Id already exists")
            }

            val user = userDao.addUser(
                userOnboardRequest.emailId, hash(userOnboardRequest.password),
                userOnboardRequest.userName, userOnboardRequest.mobile, userOnboardRequest.userType
            )

            AuthResponse.success(
                jwt.sign(user.userId, userOnboardRequest.userType),
                "Registration successful",
                user.userId,
                UUID.randomUUID().toString().replace("-", "")
            )
        } catch (bre: BadRequestException) {
            AuthResponse.failed(bre.message.toString())
        }
    }

    fun login(emailId: String, password: String, userType: String): AuthResponse {
        return try {
            validateloginOrThrowException(emailId, password, userType)

            val password = hash(password)

            val user = userDao.getByUsernameAndPassword(emailId, password, userType)
                ?: throw UnauthorizedActivityException("Invalid credentials")

            AuthResponse.success(
                jwt.sign(user.userId, userType), "Login successful",
                user.userId, UUID.randomUUID().toString().replace("-", ""), user.tempPassword == password
            )

        } catch (uae: UnauthorizedActivityException) {
            AuthResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            AuthResponse.failed(bre.message.toString())
        }
    }

    fun logout(userId: String,userType: String): AuthResponse {
        return try {

            if (userType.isEmpty() || (userType!= UserType.CUSTOMER.type && userType!= UserType.ADMIN.type)) {
                throw BadRequestException("User type invalid")
            }

            jwt.logout(userId, userType)

            AuthResponse.success("Logout success")

        } catch (uae: UnauthorizedActivityException) {
            AuthResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            AuthResponse.failed(bre.message.toString())
        }
    }

    fun getUsersById(id: String): UserResponse {
        return try {
            val user = userDao.getUserById(id)

            UserResponse.success(user)
        } catch (uae: UnauthorizedActivityException) {
            UserResponse.unauthorized(uae.message)
        }
    }


    fun updatePassword(
        userId: String,
        request: ChangPasswordRequest
    ): AuthResponse {
        return try {
            when {
                (request.currentPassword.isEmpty()) -> throw BadRequestException(
                    "current password is empty"
                )
                (request.newPassword.isEmpty()) -> throw BadRequestException("new password is empty")
            }


            val password = hash(request.currentPassword)

            if (!userDao.isUserPasswordExists(userId, password) && !userDao.isUserTempPasswordExists(userId, password)) {
                throw BadRequestException("Password doesn't exist")
            }

            val newPassword = hash(request.newPassword)

            userDao.updatePasswordById(userId, newPassword)

            AuthResponse.success("Update successful!")
        } catch (bre: BadRequestException) {
            AuthResponse.failed(bre.message)
        }
    }

    fun createPassword(
        id: String,
        request: CreatePasswordRequest
    ): AuthResponse {
        return try {

            when {
                (request.password.isEmpty()) -> throw BadRequestException("Password is empty")
                (request.password.length !in (8..50)) -> throw BadRequestException("Password should have at least 8 characters")
            }

            if (!userDao.isUserExists(id,request.userType)) {
                throw BadRequestException("User doesn't exist")
            }

            if (userDao.isUserPasswordExists(id, request.password)) {
                throw BadRequestException("Password exist for this user")
            }

            val password = hash(request.password)

            userDao.updatePasswordById(id, password)

            AuthResponse.success("Update successful!")
        } catch (bre: BadRequestException) {
            AuthResponse.failed(bre.message)
        }
    }

    fun forgotPassword(
        emailId: String,
        userType: String
    ): AuthResponse {
        return try {
            val userByEmail = userDao.getUserByEmail(emailId, userType)
            if (userByEmail != null) {
                val tmpPwd = generatePassword(length = 4, useLower = true, useDigits = true)
                userDao.updateTemporaryPasswordById(userByEmail.userId, hash(tmpPwd))
                //TODO: Need to send a mail
                AuthResponse.success(
                    jwt.sign(userByEmail.userId, userType), "Password reset link sent to you email",
                    userByEmail.userId, UUID.randomUUID().toString().replace("-", ""), true, tmpPwd
                )
            } else {
                AuthResponse.failed("No user found")
            }
        } catch (bre: BadRequestException) {
            AuthResponse.failed(bre.message.toString())
        }
    }


    private fun validateCredentialsOrThrowException(
        emailId: String,
        password: String,
        userType: String
    ) {
        val message = when {
            (emailId.isBlank() or password.isBlank()) -> "Username or password should not be blank"
            (emailId.length !in (6..30)) -> "emailId should be of min 6 character in length"
            (!(isValidEmail(emailId))) -> "Invalid email"
            (password.length !in (8..50)) -> "Password should be of min 8 character in length"
            (userType.isBlank()) -> "userType should not be blank"
            (!((userType == UserType.ADMIN.type) or (userType == UserType.CUSTOMER.type))) -> "usertype field must be ADMIN or CUSTOMER"
            else -> return
        }
        throw BadRequestException(message)
    }


    private fun validateloginOrThrowException(emailId: String, password: String, userType: String) {
        print("enter into validate credentials")

        val message = when {
            (emailId.isBlank() or password.isBlank()) -> "Username or password should not be blank"
            //(emailId.length !in (6..30)) -> "emailId should be of min 6 character in length"
            (!(isValidEmail(emailId))) -> "Invalid email"
            //(password.length !in (8..50)) -> "Password should be of min 8 character in length"
            (userType.isBlank()) -> "userType should not be blank"
            (!((userType == UserType.ADMIN.type) or (userType == UserType.CUSTOMER.type))) -> "usertype field must be admin or customer"
            else -> return
        }
        throw BadRequestException(message)
    }
}