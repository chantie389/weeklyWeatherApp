import android.R

data class SplashScreen (
    val days: Array<String> = arrayOf("monday","tuesday","wednesday","thursday","friday","saturday","sunday"),
    val minTemp: IntArray = IntArray(7),// parallel array 1
    val maxTemp: IntArray = IntArray(7), // parallel array 2
    val conditions: Array<String?> = Array<String?>(7) { null} // parallel array 3
)
enum class ScreenType {
     SPLASH, // First screen
     INPUT, // Main input screen
    DETAILS // Results screen
}
