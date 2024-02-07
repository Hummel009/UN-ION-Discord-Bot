package com.github.hummel.union.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.github.hummel.union.bean.BotData
import kotlin.system.exitProcess

fun main() {
	application {
		Window(
			onCloseRequest = ::exitApplication,
			state = WindowState(width = 900.dp, height = 400.dp, position = WindowPosition(Alignment.Center)),
			title = "Hundom",
			resizable = false
		) {
			MaterialTheme {
				ComposableOnCreate()
			}
		}
	}
}

@Composable
private fun ComposableOnCreate() {
	var token: String by remember { mutableStateOf("TOKEN") }
	var ownerId: String by remember { mutableStateOf("1186780521624244278") }

	Column(
		modifier = Modifier.background(Color.White).fillMaxSize(),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		OutlinedTextField(value = token, onValueChange = { token = it }, label = { Text("Token") })
		OutlinedTextField(value = ownerId, onValueChange = { ownerId = it }, label = { Text("Owner ID") })

		Button(
			onClick = {
				launchService(token, ownerId, "files", null)
			}, modifier = Modifier.padding(16.dp), colors = ButtonDefaults.buttonColors(
				contentColor = Color.White, backgroundColor = Color(0xFF57965C)
			)
		) {
			Text("Включить бота")
		}

		Button(
			onClick = {
				exitProcess(4)
			}, modifier = Modifier.padding(16.dp), colors = ButtonDefaults.buttonColors(
				contentColor = Color.White, backgroundColor = Color(0xFFC94F4F)
			)
		) {
			Text("Выключить бота")
		}
	}
}

@Suppress("UNUSED_PARAMETER", "RedundantSuppression")
fun launchService(token: String, ownerId: String, root: String, context: Any?) {
	BotData.token = token
	BotData.ownerId = ownerId
	BotData.root = root
	val adapter = DiscordAdapter()
	adapter.launch()
}