import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.kalistratov.mp.dayshedule.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow("MPDayShedule") {
        App()
    }
}
