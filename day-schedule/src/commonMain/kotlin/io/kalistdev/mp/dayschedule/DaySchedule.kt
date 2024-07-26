package io.kalistdev.mp.dayschedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.jvm.JvmInline
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds
import io.kalistdev.mp.dayschedule.Event as EventModel

@Immutable
object DayScheduleDefaults {

    private val timeFormat = LocalTime.Format {
        hour()
        char(':')
        minute()
    }

    fun config() = DayScheduleConfig(
        hourHeight = 30.dp,
        topSpaceHeight = 30.dp,
        bottomSpaceHeight = 30.dp
    )

    fun divider() = @Composable {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Black)
        )
    }

    fun currentHourLabel() = @Composable { now: LocalTime ->
        Text(now.format(timeFormat), color = Color.Red)
    }

    fun currentHourPointer() = @Composable {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.5.dp)
                .background(Color.Red)
        )
    }
}

@Preview
@Composable
fun DayScheduleLayout(
    events: List<EventModel>,
    modifier: Modifier = Modifier,
    config: DayScheduleConfig = DayScheduleDefaults.config(),
    divider: @Composable () -> Unit = DayScheduleDefaults.divider(),
    currentHourLabel: @Composable (LocalTime) -> Unit = DayScheduleDefaults.currentHourLabel(),
    currentHourPointer: @Composable () -> Unit = DayScheduleDefaults.currentHourPointer(),
    hourLabel: @Composable (Int) -> Unit = {
        Text(
            "$it:00", modifier = Modifier
                .padding(horizontal = 8.dp)
        )
    },
    event: @Composable (EventModel) -> Unit
) {
    val hoursRange = 0..24

    var timeNow by remember { mutableStateOf(dateNow()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(30.seconds)
            timeNow = dateNow()
        }
    }

    Layout(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        measurePolicy = getMeasurePolicy(
            config = config
        ),
        content = {

            hoursRange.forEach { hour ->
                Box(
                    modifier = Modifier.parentData(
                        component = DayScheduleComponent.Hour(hour)
                    )
                ) { hourLabel(hour) }
            }

            hoursRange.forEach { _ ->
                Box(
                    modifier = Modifier.parentData(
                        component = DayScheduleComponent.Divider
                    )
                ) { divider() }
            }

            events.forEach { event ->
                Box(
                    modifier = Modifier.parentData(
                        component = DayScheduleComponent.Event(event)
                    )
                ) { event(event) }
            }

            Box(
                modifier = Modifier.parentData(
                    component = DayScheduleComponent.CurrentHour(timeNow)
                )
            ) { currentHourLabel(timeNow) }

            Box(
                modifier = Modifier.parentData(
                    component = DayScheduleComponent.CurrentHourPointer(timeNow)
                )
            ) { currentHourPointer() }
        }
    )
}

private fun getMeasurePolicy(
    config: DayScheduleConfig
) = MeasurePolicy { measurables, constraints ->

    val hourHeightPx = config.hourHeight.roundToPx()
    val hoursHeightPx = hourHeightPx * 24

    val topSpaceHeightPx = config.topSpaceHeight.roundToPx()
    val bottomSpaceHeightPx = config.bottomSpaceHeight.roundToPx()

    val heightPx = hoursHeightPx + topSpaceHeightPx + bottomSpaceHeightPx
    val widthPx = constraints.maxWidth

    var maxHourWidth = 0

    var currentHour: Pair<LocalTime, Placeable>? = null
    var currentHourPointer: Placeable? = null
    val hours = mutableListOf<Placeable>()
    val dividers = mutableListOf<Placeable>()
    val events = mutableListOf<Pair<Placeable, EventModel>>()

    val sheetConstraints = constraints.run { copy(maxWidth = widthPx - maxHourWidth) }

    measurables.forEach { measurable ->
        when (val data = measurable.parentData) {
            is DayScheduleComponent.Hour -> measurable
                .measure(constraints)
                .also { maxHourWidth = maxOf(maxHourWidth, it.width) }
                .let(hours::add)

            is DayScheduleComponent.Divider -> measurable
                .measure(sheetConstraints)
                .let(dividers::add)

            is DayScheduleComponent.CurrentHourPointer -> {
                currentHourPointer = measurable
                    .measure(sheetConstraints)
            }

            is DayScheduleComponent.CurrentHour -> {
                currentHour = data.time to measurable
                    .measure(constraints)
                    .also { maxHourWidth = maxOf(maxHourWidth, it.width) }
            }

            is DayScheduleComponent.Event -> processEvent(
                range = data.event,
                hourHeight = hourHeightPx,
                hourWidth = maxHourWidth,
                measurable = measurable,
                constraints = constraints
            ).let(events::add)
        }
    }

    val currentHourStartOffset = currentHour?.first?.run { allMinutes / 60f } ?: 0f
    val currentHourOffset = (currentHourStartOffset * hourHeightPx + topSpaceHeightPx).roundToInt()

    val checkInsideCurrentHourPlaceable: (Int, Placeable) -> Boolean = { offset, placeable ->
        val currentHourPlacement = currentHour?.second

        val rootFromOffset = currentHourOffset - (currentHourPlacement?.height?.half() ?: 0)
        val rootToOffset = rootFromOffset + (currentHourPlacement?.height ?: 0)

        val rootFromTo = rootFromOffset..<rootToOffset

        val placeableFromOffset = offset - placeable.height.half()
        val placeableToOffset = placeableFromOffset + placeable.height

        val fromContains = placeableFromOffset in rootFromTo
        val toContains = placeableToOffset in rootFromTo

        fromContains || toContains
    }

    layout(
        width = widthPx,
        height = heightPx
    ) {
        hours.forEachIndexed { index, placeable ->
            placeable.run {
                val offset = (maxHourWidth - width).let { if (it > 0) it / 2 else 0 }
                val yOffset = (hourHeightPx * index) + topSpaceHeightPx

                if (!checkInsideCurrentHourPlaceable(yOffset, this)) place(
                    x = maxOf(0, offset),
                    y = yOffset - (height / 2)
                )
            }
        }

        dividers.forEachIndexed { index, placeable ->
            placeable.run {
                place(
                    x = maxHourWidth,
                    y = (hourHeightPx * index) - (height / 2) + topSpaceHeightPx
                )
            }
        }

        events.forEach { (placeable, event) ->
            val startOffset = event.from.allMinutes / 60f
            val y = (startOffset * hourHeightPx + topSpaceHeightPx).roundToInt()
            val x = maxHourWidth
            placeable.place(x, y)
        }

        currentHour?.run {
            val xOffset = (maxHourWidth - second.width).half()
            val yOffset = currentHourOffset - (second.height / 2)
            second.place(
                x = xOffset,
                y = yOffset
            )
        }

        currentHourPointer?.run {
            place(
                x = maxHourWidth,
                y = currentHourOffset - (height / 2)
            )
        }
    }
}

internal interface DayScheduleComponent {
    data object Divider : DayScheduleComponent

    data class CurrentHour(val time: LocalTime) : DayScheduleComponent

    data class CurrentHourPointer(val time: LocalTime) : DayScheduleComponent

    data class Event(val event: EventModel) : DayScheduleComponent

    @JvmInline
    value class Hour(val value: Int) : DayScheduleComponent
}

private fun processEvent(
    range: EventModel,
    hourHeight: Int,
    hourWidth: Int,
    measurable: Measurable,
    constraints: Constraints
): Pair<Placeable, EventModel> {
    val minutes = range.durationInMinutes
    val height = ((minutes / 60f) * hourHeight).roundToInt()
    val width = constraints.maxWidth - hourWidth
    val placeable = measurable
        .measure(
            constraints.copy(
                minWidth = width,
                maxWidth = width,
                minHeight = height,
                maxHeight = height
            )
        )
    return placeable to range
}

private fun Modifier.parentData(component: DayScheduleComponent) = then(
    object : ParentDataModifier {
        override fun Density.modifyParentData(parentData: Any?) = component
    }
)

private fun Int.half() = if (this > 0) this / 2 else 0

private fun dateNow() = Clock.System.now()
    .toLocalDateTime(TimeZone.currentSystemDefault())
    .time

@Preview
@Composable
private fun DayPreview() {
    DayScheduleLayout(
        events = listOf(
            EventModel(
                LocalTime(1, 0),
                LocalTime(2, 0),
            ),
            EventModel(
                LocalTime(4, 0),
                LocalTime(5, 0),
            ),
            EventModel(
                LocalTime(6, 0),
                LocalTime(7, 30),
            ),
            EventModel(
                LocalTime(8, 0),
                LocalTime(10, 0),
            ),
            EventModel(
                LocalTime(11, 30),
                LocalTime(12, 0),
            )
        )
    ) {
        EventContainer({}) {

        }
    }
}