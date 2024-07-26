package io.kalistratov.mp.dayshedule

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
internal fun App() {
    DayScheduleLayout(
        events = listOf(
            Event(
                LocalTime(1, 0),
                LocalTime(2, 0),
            ),
            Event(
                LocalTime(4, 0),
                LocalTime(5, 0),
            ),
            Event(
                LocalTime(6, 0),
                LocalTime(7, 30),
            ),
            Event(
                LocalTime(8, 0),
                LocalTime(10, 0),
            ),
            Event(
                LocalTime(11, 30),
                LocalTime(12, 0),
            )
        )
    ) { event ->
        EventContainer(
            {},
            modifier = Modifier.fillMaxSize(),
            markerColor = Color.Blue,
            containerColor = Color.Blue.copy(0.2f)
        ) {
            Text(event.run { listOf(from, to).joinToString { it.toString() } }, color = Color.White)
        }
    }
}

internal expect fun openUrl(url: String?)