package hummel

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.fragment.app.FragmentActivity
import androidx.work.*
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

class MainActivity : FragmentActivity() {
	private val context: Context = this

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			MaterialTheme {
				Column(
					modifier = Modifier
						.fillMaxSize()
						.background(Color(0xFF2B2D30)),
					verticalArrangement = Arrangement.Center,
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Button(
						onClick = {
							initializeWorkManager()
						}, modifier = Modifier.padding(16.dp), colors = ButtonDefaults.buttonColors(
							contentColor = Color(0xFFCED0D6), backgroundColor = Color(0xFF57965C)
						)
					) {
						Text("Включить бота")
					}

					Button(
						onClick = {
							exitProcess(0)
						}, modifier = Modifier.padding(16.dp), colors = ButtonDefaults.buttonColors(
							contentColor = Color(0xFFCED0D6), backgroundColor = Color(0xFFC94F4F)
						)
					) {
						Text("Выключить бота")
					}
				}
			}
		}
	}

	private fun initializeWorkManager() {
		val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

		val myWorkRequest =
			PeriodicWorkRequestBuilder<DiscordService>(10, TimeUnit.MINUTES).setConstraints(constraints).build()

		WorkManager.getInstance(context).enqueueUniquePeriodicWork(
			"MyWork", ExistingPeriodicWorkPolicy.KEEP, myWorkRequest
		)
	}
}