package hummel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import kotlin.system.exitProcess

fun main(): Unit = application {
	var token: String by remember { mutableStateOf("TOKEN") }
	var ownerId: String by remember { mutableStateOf("0") }

	Window(
		onCloseRequest = ::exitApplication,
		state = WindowState(width = 900.dp, height = 400.dp, position = WindowPosition(Alignment.Center)),
		title = "Hundom",
		resizable = false
	) {
		MaterialTheme {
			Column(
				modifier = Modifier.fillMaxSize(),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				OutlinedTextField(value = token, onValueChange = { token = it }, label = { Text("Token") })
				OutlinedTextField(value = ownerId, onValueChange = { ownerId = it }, label = { Text("Owner ID") })

				Button(
					onClick = {
						launchService(token, ownerId)
					}, modifier = Modifier.padding(16.dp), colors = ButtonDefaults.buttonColors(
						contentColor = Color.White, backgroundColor = Color(0xFF57965C)
					)
				) {
					Text("Включить бота")
				}

				Button(
					onClick = {
						stopService()
					}, modifier = Modifier.padding(16.dp), colors = ButtonDefaults.buttonColors(
						contentColor = Color.White, backgroundColor = Color(0xFFC94F4F)
					)
				) {
					Text("Выключить бота")
				}
			}
		}
	}
}

private fun launchService(token: String, ownerId: String) {
	val adapter = DiscordAdapter(token, ownerId)
	adapter.launch()
}

private fun stopService() {
	exitProcess(0)
}