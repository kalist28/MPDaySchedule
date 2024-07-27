package io.kalistratov.mp.daySchedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.kalistdev.mp.dayschedule.DayScheduleLayout
import io.kalistdev.mp.dayschedule.Event
import io.kalistdev.mp.dayschedule.EventContainer
import kotlinx.datetime.LocalTime

@Composable
internal fun App() = Box(Modifier.fillMaxSize().background(Color.White)) {

    val events = listOf(
        Event(
            LocalTime(hour = 1, minute = 0),
            LocalTime(hour = 2, minute = 0),
        ), // or implement interface
        object : Event {
            override val from: LocalTime = LocalTime(hour = 1, minute = 0)
            override val to: LocalTime = LocalTime(hour = 2, minute = 0)
        }
    )

    DayScheduleLayout(
        events = events
    ) { event ->
        EventContainer(
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxSize(),
            markerColor = Color.Blue,
            containerColor = Color.Blue.copy(0.2f)
        ) {
            Text(
                event.run { listOf(from, to)
                    .joinToString { it.toString() } },
                color = Color.White
            )
        }
    }
}

internal expect fun openUrl(url: String?)