package io.kalistdev.mp.dayschedule

import kotlinx.datetime.LocalTime

val LocalTime.allMinutes get() = hour * 60 + minute

val Event.durationInMinutes: Int get() = to.allMinutes - from.allMinutes