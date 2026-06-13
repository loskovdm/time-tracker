package io.github.loskovdm.domain.util

private const val MIN_PASSWORD_LENGTH = 6

fun isValidPassword(password: String): Boolean = password.length >= MIN_PASSWORD_LENGTH
