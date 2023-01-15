package dev.codewithdk.ktor.data.model

import dev.codewithdk.ktor.data.entity.UserEntity


data class User(
    val userId: String,
    val emailId: String,
    val password: String,
    val userType: String,
    var tempPassword : String
) {
    companion object {
        fun fromEntity(entity: UserEntity) =
            User(entity.userId, entity.emailId, entity.password, entity.userType,entity.tempPassword)
    }
}

data class UserInternal(
    val primaryKey: Long,
    val emailId: String,
    val password: String,
    val userType: String,
) {
    companion object {
        fun fromEntity(entity: UserEntity) =
            UserInternal(entity.id.value, entity.emailId, entity.password, entity.userType)
    }
}