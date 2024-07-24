package com.github.hummel.union.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.hummel.union.bean.BotData
import kotlin.system.exitProcess

class Main : ComponentActivity() {
	private val context: Context = this

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			MaterialTheme(
				colors = if (isSystemInDarkTheme()) darkColors() else lightColors()
			) {
				ComposableOnCreate()
			}
		}
	}

	@Composable
	@Suppress("FunctionName")
	fun ComposableOnCreate() {
		var token: String by remember { mutableStateOf("TOKEN") }
		var ownerId: String by remember { mutableStateOf("1186780521624244278") }

		Column(
			modifier = Modifier.fillMaxSize(),
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
						launchService(token, ownerId, context.filesDir.path, context)
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
}

@Suppress("RedundantSuppression", "unused")
fun launchService(token: String, ownerId: String, root: String, context: Any?) {
	context as Context
	BotData.token = token
	BotData.ownerId = ownerId
	BotData.root = root
	val serviceIntent = Intent(context, DiscordAdapter::class.java)
	context.startService(serviceIntent)
}