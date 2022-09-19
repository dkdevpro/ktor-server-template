package dev.codewithdk.ktor.utils

import java.util.regex.Pattern

fun isValidEmail(email: String?): Boolean {
    val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$"
    val pat: Pattern = Pattern.compile(emailRegex)
    return if (email == null) false else pat.matcher(email).matches()
}

