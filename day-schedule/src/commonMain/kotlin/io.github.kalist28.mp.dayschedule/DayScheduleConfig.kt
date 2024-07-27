package io.github.kalist28.mp.dayschedule

import androidx.compose.ui.unit.Dp

/**
 * Config for [DayScheduleLayout].
 *
 * @property hourHeight height of one hour.
 * @property topSpaceHeight space before hours.
 * @property bottomSpaceHeight space after hours.
 */
data class DayScheduleConfig(
    val hourHeight: Dp,
    val topSpaceHeight: Dp,
    val bottomSpaceHeight: Dp,
)