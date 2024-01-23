package hummel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
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
import androidx.fragment.app.FragmentActivity
import hummel.bean.BotData
import kotlin.system.exitProcess

class Main : FragmentActivity() {
	private val context: Context = this

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			MaterialTheme {
				ComposableOnCreate()
			}
		}
	}

	@Composable
	private fun ComposableOnCreate() {
		var token: String by remember { mutableStateOf("MTE0NzQ0OTUyMDU2NTgwMTAwMQ.G5nB9t.2aHp5hqUASugAN93sui4eGlYwyRC6zyIk9yrWI") }
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
					launchService(token, ownerId, context.filesDir.path, context)
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
}

@Suppress("UNUSED_PARAMETER", "KotlinRedundantDiagnosticSuppress")
fun launchService(token: String, ownerId: String, root: String, context: Any?) {
	context as Context
	BotData.token = token
	BotData.ownerId = ownerId
	BotData.root = root
	val serviceIntent = Intent(context, DiscordAdapter::class.java)
	context.startForegroundService(serviceIntent)
}