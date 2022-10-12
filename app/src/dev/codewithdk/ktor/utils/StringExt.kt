package dev.codewithdk.ktor.utils

fun String.isAlphaNumeric() = matches("[a-zA-Z0-9]+".toRegex())