package hummel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
				Button(
					onClick = {
						launchService()
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

private fun launchService() {
	val adapter = DiscordAdapter()
	adapter.launch()
}

private fun stopService() {
	exitProcess(0)
}