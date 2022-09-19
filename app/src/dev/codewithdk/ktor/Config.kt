package dev.codewithdk.ktor

import io.ktor.server.config.*

@Suppress("PropertyName")
class Config constructor(config: ApplicationConfig) {
    val SECRET_KEY = config.property("key.secret").getString()

    val DATABASE_HOST = config.property("database.host").getString()
    val DATABASE_PORT = config.property("database.port").getString()
    val DATABASE_NAME = config.property("database.name").getString()
    val DATABASE_USER = config.property("database.user").getString()
    val DATABASE_PASSWORD = config.property("database.password").getString()
}




