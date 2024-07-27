package io.github.kalist28.mp.dayschedule

import kotlinx.datetime.LocalTime

val LocalTime.allMinutes get() = hour * 60 + minute

val Event.durationInMinutes: Int get() = to.allMinutes - from.allMinutes