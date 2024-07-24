package com.github.hummel.union.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
			title = "Hundroid",
			resizable = false
		) {
			MaterialTheme(colors = darkColors()) {
				ComposableOnCreate()
			}
		}
	}
}

@Composable
@Suppress("FunctionName")
private fun ComposableOnCreate() {
	var token: String by remember { mutableStateOf("TOKEN") }
	var ownerId: String by remember { mutableStateOf("1186780521624244278") }

	Column(
		modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		TextField(
			value = token,
			onValueChange = { token = it },
			modifier = Modifier.fillMaxWidth().padding(16.dp),
			label = {
				Text("Token")
			},
			colors = TextFieldDefaults.textFieldColors(
				textColor = MaterialTheme.colors.onBackground
			)
		)

		Spacer(modifier = Modifier.height(16.dp))

		TextField(
			value = ownerId,
			onValueChange = { ownerId = it },
			modifier = Modifier.fillMaxWidth().padding(16.dp),
			label = {
				Text("Owner ID")
			},
			colors = TextFieldDefaults.textFieldColors(
				textColor = MaterialTheme.colors.onBackground
			)
		)

		Spacer(modifier = Modifier.height(16.dp))

		Row(
			modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly
		) {
			Button(
				onClick = {
					launchService(token, ownerId, "files", null)
				}, colors = ButtonDefaults.buttonColors(
					contentColor = Color.White, backgroundColor = Color(0xFF57965C)
				)
			) {
				Text("Включить")
			}

			Button(
				onClick = {
					exitProcess(0)
				}, colors = ButtonDefaults.buttonColors(
					contentColor = Color.White, backgroundColor = Color(0xFFC94F4F)
				)
			) {
				Text("Выключить")
			}
		}
	}
}

@Suppress("UNUSED_PARAMETER", "RedundantSuppression", "unused")
fun launchService(token: String, ownerId: String, root: String, context: Any?) {
	BotData.token = token
	BotData.ownerId = ownerId
	BotData.root = root
	val adapter = DiscordAdapter()
	adapter.launch()
}