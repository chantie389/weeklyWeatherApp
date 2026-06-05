package com.example.weeklyweatherapp

import SplashScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weeklyweatherapp.ui.theme.WeeklyWeatherAppTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.text.KeyboardOptions


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WeeklyWeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        WeatherApp( modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
    @Composable
    fun WeatherApp(modifier: Modifier = Modifier) {
        var currentScreen by remember { mutableStateOf(ScreenType.SPLASH) }
        val weatherData = remember { SplashScreen() }
        // state to hold user details entered on splash screen
        var userName by remember { mutableStateOf("") }
        var studentNumber by remember { mutableStateOf("") }

        when (currentScreen) {
            ScreenType.SPLASH -> ShowSplash(

                onEnter = { currentScreen = ScreenType.INPUT},
                onExit = { finish()}
            )
            ScreenType.INPUT -> ShowInputScreen(
                data = weatherData,
                onViewDetails = { currentScreen = ScreenType.DETAILS
                }
            )
            ScreenType.DETAILS -> ShowDetailsScreen(
                data = weatherData,
                onBack = { currentScreen = ScreenType.INPUT}

            )
        }
    }

fun finish() {
    TODO("Not yet implemented")
}
// SPLASH SCREEN UI
    @Composable
    fun ShowSplash(onEnter: () -> Unit, onExit: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE3F2FD))
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "App Logo",
                modifier = Modifier.padding(bottom = 40.dp)
            )

            Text(
                text = "Weekly Weather Manager",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2),
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Text(text = " nhlelo lebissi", fontSize = 18.sp, modifier = Modifier.padding(bottom = 10.dp))
            Text(text = "Student number:ST123456", fontSize = 18.sp, modifier = Modifier.padding(bottom = 50.dp))

            Button(
                onClick = onEnter,
                modifier = Modifier.fillMaxWidth(0.7f).padding(bottom = 15.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(Color(0xFF2196F3))
            ) { Text("Enter App", color = Color.White, fontSize = 18.sp) }

            Button(
                onClick = onExit,
                modifier = Modifier.fillMaxWidth(0.7f),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(Color(0xFFF44336))
            ) { Text("Exit", color = Color.White, fontSize = 18.sp) }
        }
    }
    // INPUT SCREEN + LOGIC — WRITES TO YOUR PARALLEL ARRAYS

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowInputScreen(data: SplashScreen, onViewDetails: () -> Unit) {
        val scrollState = rememberScrollState()
        val minInputs = remember { Array(7) { mutableStateOf("") } }
        val maxInputs = remember { Array(7) { mutableStateOf("") } }
        val condInputs = remember { Array(7) { mutableStateOf("") } }
        var errorMessage by remember { mutableStateOf("") }
        var averageResult by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .verticalScroll(scrollState)
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Enter Weekly Weather Data",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp)
            )

            // LOOP: Create inputs
            data.days.forEachIndexed { index, day ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(day.replaceFirstChar { it.uppercaseChar() }, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                        androidx.compose.foundation.layout.Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = minInputs[index].value,
                                { minInputs[index].value = it },
                                label = { Text("Min °C") },
                                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = maxInputs[index].value,
                                { maxInputs[index].value = it },
                                label = { Text("Max °C") },
                                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = condInputs[index].value,
                                { condInputs[index].value = it },
                                label = { Text("Condition") },
                                modifier = Modifier.weight(1.5f)
                            )
                        }
                    }
                }
            }

            // CALCULATE AVERAGE + LOOP
            Button(
                onClick = {
                    errorMessage = ""
                    averageResult = ""
                    var valid = true

                    //ERROR HANDLING + SAVE TO YOUR ARRAYS
                    for (i in 0..6) {
                        val minStr = minInputs[i].value.trim()
                        val maxStr = maxInputs[i].value.trim()
                        val condStr = condInputs[i].value.trim()

                        if (minStr.isEmpty() || maxStr.isEmpty() || condStr.isEmpty()) {
                            errorMessage = "Error: All fields required"
                            valid = false; break
                        }
                        if (!minStr.matches(Regex("-?\\d+")) || !maxStr.matches(Regex("-?\\d+"))) {
                            errorMessage = "Error: Enter numbers only"
                            valid = false; break
                        }
                        val minVal = minStr.toInt()
                        val maxVal = maxStr.toInt()
                        if (minVal < -50 || minVal > 60 || maxVal < -50 || maxVal > 60) {
                            errorMessage = "Error: Temp -50°C to 60°C only"
                            valid = false; break
                        }

                        // WRITE DIRECTLY TO YOUR PARALLEL ARRAYS
                        data.minTemp[i] = minVal
                        data.maxTemp[i] = maxVal
                        data.conditions[i] = condStr
                    }

                    if (!valid) return@Button

                    // LOOP FOR AVERAGE
                    var totalSum = 0.0
                    for (i in 0..6) {
                        totalSum += (data.minTemp[i] + data.maxTemp[i]) / 2.0
                    }
                    val weeklyAvg = totalSum / 7
                    averageResult = String.format("Weekly Average Temperature: %.1f°C", weeklyAvg)
                },
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(Color(0xFF4CAF50))
            ) { Text("Calculate Weekly Average", color = Color.White) }
            if (averageResult.isNotEmpty()){
                Text(
                    text = averageResult,
                    fontSize = 18.sp,
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // VIEW DETAILS
            Button(
                onClick = onViewDetails,
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(Color(0xFF2196F3))
            ) { Text("View Detailed Daily Weather", color = Color.White) }

            // CLEAR DATA
            Button(
                onClick = {
                    minInputs.forEach { it.value = "" }
                    maxInputs.forEach { it.value = "" }
                    condInputs.forEach { it.value = "" }
                    data.minTemp.fill(0); data.maxTemp.fill(0); data.conditions.fill(null)
                    averageResult = ""; errorMessage = ""
                },
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(Color(0xFFFF9800))
            ) { Text("Clear All Data", color = Color.White) }

            // EXIT
            Button(
                onClick = { finishAffinity() },
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 30.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(Color(0xFFF44336))
            ) { Text("Exit", color = Color.White) }

            // SHOW MESSAGES
            if (averageResult.isNotEmpty()) Text(averageResult, color = Color(0xFF2E7D32), modifier = Modifier.padding(10.dp))
            if (errorMessage.isNotEmpty()) Text(errorMessage, color = Color.Red, modifier = Modifier.padding(10.dp))
        }
    }

private fun ColumnScope.finishAffinity() {
    TODO("Not yet implemented")
}

// DETAILED SCREEN — READS FROM YOUR PARALLEL ARRAYS
    @Composable
    fun ShowDetailsScreen(data: SplashScreen, onBack: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE8F5E9))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Detailed Daily Weather",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(15.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // LOOP TO DISPLAY ALL DATA
                data.days.forEachIndexed { i, day ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(day.replaceFirstChar { it.uppercaseChar() }, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(text = "Min: ${data.minTemp[i]}°C")
                            Text(text = "Max: ${data.maxTemp[i]}°C")
                            Text(text = "Condition: ${data.conditions[i]}")
                        }
                    }
                }
            }

            // BACK NAVIGATION
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(Color(0xFFC8E6C9))
            ) { Text("← Back to Main Screen", color = Color.Black) }
        }
    }