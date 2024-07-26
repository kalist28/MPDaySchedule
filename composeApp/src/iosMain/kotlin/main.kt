import androidx.compose.ui.window.ComposeUIViewController
import io.kalistratov.mp.dayshedule.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
