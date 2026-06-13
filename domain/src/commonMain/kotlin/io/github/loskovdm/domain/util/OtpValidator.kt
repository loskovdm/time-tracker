package io.github.loskovdm.domain.util

const val OTP_CODE_LENGTH = 8

private val OTP_REGEX = Regex("^\\d{$OTP_CODE_LENGTH}$")

fun isValidOtp(code: String): Boolean = OTP_REGEX.matches(code.trim())
