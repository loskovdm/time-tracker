package io.github.loskovdm.timetracker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform