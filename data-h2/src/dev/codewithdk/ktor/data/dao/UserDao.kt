package dev.codewithdk.ktor.data.dao

import dev.codewithdk.ktor.data.entity.UserEntity
import dev.codewithdk.ktor.data.model.User
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import javax.inject.Inject


class UserDao @Inject constructor() {

    fun addUser(
        emailId: String,
        password: String,
        userName: String,
        mobile: String,
        userType: String
    ): User = transaction {
        UserEntity.new {
            this.userId = UUID.randomUUID().toString().replace("-", "")
            this.emailId = emailId
            this.password = password
            this.userName = userName
            this.mobile = mobile
            this.userType = userType
        }
    }.let { User.fromEntity(it) }

    fun createUser(
        emailId: String,
        userName: String,
        mobile: String,
        userType: String
    ): User = transaction {
        UserEntity.new {
            this.userId = UUID.randomUUID().toString().replace("-", "")
            this.emailId = emailId
            this.userName = userName
            this.mobile = mobile
            this.userType = userType
        }
    }.let { User.fromEntity(it) }
}