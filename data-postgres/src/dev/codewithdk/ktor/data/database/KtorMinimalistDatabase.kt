package dev.codewithdk.ktor.data.database

import dev.codewithdk.ktor.data.database.table.UsersTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


fun initDatabase(
    host: String,
    port: String,
    databaseName: String,
    user: String,
    password: String
) {
    val tables = arrayOf(UsersTable)

    Database.connect(
        url = "jdbc:postgresql://$host:$port/$databaseName",
        driver = "org.postgresql.Driver",
        user = user,
        password = password
    )

    transaction {
        SchemaUtils.createMissingTablesAndColumns(*tables)
    }
}