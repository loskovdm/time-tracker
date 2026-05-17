package io.github.loskovdm.domain.error

sealed class TrackTimeError {
    object SecondActiveTimeEntry : TrackTimeError()
}