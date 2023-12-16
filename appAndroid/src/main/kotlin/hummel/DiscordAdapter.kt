package hummel

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import hummel.controller.DiscordController
import hummel.controller.impl.DiscordControllerImpl
import hummel.factory.DaoFactory

class DiscordAdapter : Service() {
	private lateinit var wakeLock: WakeLock
	private val controller: DiscordController = DiscordControllerImpl
	private val context: Context = this

	override fun onCreate() {
		DaoFactory.context = context
		wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
			newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Hundom::MyWakeLock")
		}
		controller.onCreate()
	}

	@SuppressLint("WakelockTimeout")
	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		wakeLock.acquire()
		controller.onStartCommand()
		return START_NOT_STICKY
	}

	override fun onDestroy() {
		wakeLock.release()
	}

	override fun onBind(intent: Intent?): IBinder? = null
}