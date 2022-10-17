package dev.codewithdk.ktor.data.entity

import dev.codewithdk.ktor.data.database.table.UsersTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class UserEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserEntity>(UsersTable)

    var userId by UsersTable.userId
    var emailId by UsersTable.emailId
    var password by UsersTable.password
    var tempPassword by UsersTable.tempPassword
    var userName by UsersTable.userName
    var mobile by UsersTable.mobileNumber
    var userType by UsersTable.userType
}