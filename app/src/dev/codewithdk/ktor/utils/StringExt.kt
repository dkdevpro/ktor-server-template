package dev.codewithdk.ktor.utils

/**
 * Created by DK on 17/11/20.
 */
fun String.isAlphaNumeric() = matches("[a-zA-Z0-9]+".toRegex())