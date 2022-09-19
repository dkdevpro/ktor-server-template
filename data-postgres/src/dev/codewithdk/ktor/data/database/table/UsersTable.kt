package dev.codewithdk.ktor.data.database.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

object UsersTable : LongIdTable() {

    val userName = varchar("user_name", length = 60)
    val userId = varchar("user_id", length = 60)
    val emailId = varchar("email_id", length = 60)
    val mobileNumber = varchar("mobile", length = 60)
    val password = text("pwd").default("")
    val tempPassword = text("tmp_pwd").default("")
    val userType = varchar("user_type", length = 10)
    var created = datetime("created").default(DateTime.now())
    var updated = datetime("updated").default(DateTime.now())
}