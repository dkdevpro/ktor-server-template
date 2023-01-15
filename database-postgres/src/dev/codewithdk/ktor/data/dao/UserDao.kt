package dev.codewithdk.ktor.data.dao

import dev.codewithdk.ktor.data.database.table.UsersTable
import dev.codewithdk.ktor.data.entity.UserEntity
import dev.codewithdk.ktor.data.model.User
import dev.codewithdk.ktor.data.model.UserInternal
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
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
            this.userId = UUID.randomUUID().toString().replace("-","")
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
            this.userId = UUID.randomUUID().toString().replace("-","")
            this.emailId = emailId
            this.userName = userName
            this.mobile = mobile
            this.userType = userType
        }
    }.let { User.fromEntity(it) }

    fun getOrdersByUser(userPrimaryKey: Long): List<User> = transaction {
        UserEntity.find { UsersTable.id eq userPrimaryKey }
            .sortedByDescending { it.id }
            .map { User.fromEntity(it) }
    }

    fun getUserById(userId: String): User? = transaction {
        UserEntity.find { UsersTable.userId eq  userId}.limit(1).firstOrNull()
    }.let { it?.let { it1 -> User.fromEntity(it1) } }

    fun getUserInternalById(userId: String): UserInternal = transaction {
        UserEntity.find { UsersTable.userId eq  userId}.limit(1).first()
    }.let { it.let { it1 -> UserInternal.fromEntity(it1) } }

    fun getByUsernameAndPassword(emailId: String, password: String, userType: String): User? = transaction {
        UserEntity.find {
            (UsersTable.emailId eq emailId) and (UsersTable.password eq password) and (UsersTable.userType eq userType)
        }.firstOrNull()
    }?.let { User.fromEntity(it) }

    fun updatePasswordById(
        userId: String,
        password: String
    ): Int = transaction {
        UsersTable.update({ UsersTable.userId eq userId }) {
            it[UsersTable.password] = password
        }
    }

    fun updateTemporaryPasswordById(
        userId: String,
        password: String
    ): Int = transaction {
        UsersTable.update({ UsersTable.userId eq userId }) {
            it[tempPassword] = password
        }
    }

    fun getUserByEmail(emailId: String, userType: String): User? = transaction {
        UserEntity.find { (UsersTable.emailId eq emailId) and (UsersTable.userType eq  userType)}.limit(1).firstOrNull()
    }.let {
        if (it != null) {
            User.fromEntity(it)
        }else {
            null
        }
    }

    fun getAllUsers(): List<User> = transaction {
        UserEntity.all()
            .sortedByDescending { it.id }
            .map { User.fromEntity(it) }
    }

    fun isEmailIdAlreadyExists(emailId: String, userType: String): Boolean {
        return transaction {
            UserEntity.find { (UsersTable.emailId eq emailId) and (UsersTable.userType eq  userType)}.firstOrNull()
        } == null
    }

    fun isUserPasswordExists(userId: String, password: String): Boolean {
        return transaction {
            UserEntity.find { (UsersTable.userId eq userId) and (UsersTable.password eq password)}.firstOrNull() != null
        }
    }

    fun isUserTempPasswordExists(userId: String, tempPwd: String): Boolean {
        return transaction {
            UserEntity.find { (UsersTable.userId eq userId) and (UsersTable.tempPassword eq tempPwd)}.firstOrNull() != null
        }
    }

    fun isUserExists(userId: String, userType: String): Boolean {
        return transaction {
            UserEntity.find { (UsersTable.userId eq userId) and (UsersTable.userType eq userType)}.firstOrNull() != null
        }
    }

    fun isCustomer(userPrimaryKey: Long): Boolean = transaction {
            UserEntity.find { (UsersTable.id eq userPrimaryKey)}.firstOrNull() != null
        }
    }
