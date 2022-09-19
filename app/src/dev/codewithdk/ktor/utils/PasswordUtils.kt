package dev.codewithdk.ktor.utils

import kotlin.random.Random

private const val LOWER = "abcdefghijklmnopqrstuvwxyz"
private const val UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
private const val DIGITS = "0123456789"
private const val PUNCTUATION = "!@#$%&*()_+-=[]|,./?><"

fun generatePassword(length: Int, useLower : Boolean = false,
             useUpper : Boolean = false, useDigits : Boolean = false, usePunctuation : Boolean = false): String {
    // Argument Validation.
    if (length <= 0) {
        return ""
    }

    // Variables.
    val password = StringBuilder(length)
    val random = Random(System.nanoTime())

    // Collect the categories to use.
    val charCategories = ArrayList<String>()
    if (useLower) {
        charCategories.add(LOWER)
    }
    if (useUpper) {
        charCategories.add(UPPER)
    }
    if (useDigits) {
        charCategories.add(DIGITS)
    }
    if (usePunctuation) {
        charCategories.add(PUNCTUATION)
    }

    // Build the password.
    for (i in 0 until length) {
        val charCategory = charCategories[random.nextInt(charCategories.size)]
        val position: Int = random.nextInt(charCategory.length)
        password.append(charCategory[position])
    }
    return String(password)
}
