package dev.codewithdk.ktor.exception


class BadRequestException(override val message: String) : Exception(message)

class UnauthorizedActivityException(override val message: String) : Exception(message)
