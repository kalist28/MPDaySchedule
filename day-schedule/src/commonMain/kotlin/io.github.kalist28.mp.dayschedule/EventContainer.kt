package io.github.kalist28.mp.dayschedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EventContainer(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    markerColor: Color = Color.Unspecified,
    containerColor: Color = Color.Unspecified,
    content: @Composable () -> Unit
) {
    val round = remember { 4.dp }
    val shape = remember { RoundedCornerShape(round) }
    Row (
        modifier
            .padding(round / 2)
            .background(
                color = containerColor,
                shape = shape
            )
            .background(markerColor.copy(0.2f))
            .clip(shape)
            .clickable(onClick = onClick)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(round)
                .background(
                    markerColor,
                    RoundedCornerShape(
                        topStart = round,
                        bottomStart = round
                    )
                )
        )

        content()
    }
}