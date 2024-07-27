package io.github.kalist28.mp.dayschedule

import kotlinx.datetime.LocalTime

interface Event {
    val from: LocalTime
    val to: LocalTime
}

fun Event(
    from: LocalTime,
    to: LocalTime
) = object : Event {
    override val from: LocalTime = from
    override val to: LocalTime = to
}
