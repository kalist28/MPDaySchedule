import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.kalistratov.mp.daySchedule.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow("MPDaySchedule") {
        App()
    }
}
