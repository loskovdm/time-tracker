package io.github.loskovdm.domain.util

private val EMAIL_REGEX =
    Regex("""^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$""")

fun isValidEmail(email: String): Boolean = EMAIL_REGEX.matches(email.trim())
