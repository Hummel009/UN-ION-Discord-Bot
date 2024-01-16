package hummel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
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

class Main : FragmentActivity() {
	private val context: Context = this

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			MaterialTheme {
				composableOnCreate()
			}
		}
	}

	@Composable
	private fun composableOnCreate() {
		var token: String by remember { mutableStateOf("TOKEN") }
		var ownerId: String by remember { mutableStateOf("1186780521624244278") }

		Column(
			modifier = Modifier.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			OutlinedTextField(value = token, onValueChange = { token = it }, label = { Text("Token") })
			OutlinedTextField(value = ownerId, onValueChange = { ownerId = it }, label = { Text("Owner ID") })

			Button(
				onClick = {
					BotData.token = token
					BotData.ownerId = ownerId
					BotData.root = context.filesDir.path
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

	private fun launchService() {
		val serviceIntent = Intent(context, DiscordAdapter::class.java)
		startService(serviceIntent)
	}

	private fun stopService() {
		val serviceIntent = Intent(context, DiscordAdapter::class.java)
		stopService(serviceIntent)
		finish()
	}
}