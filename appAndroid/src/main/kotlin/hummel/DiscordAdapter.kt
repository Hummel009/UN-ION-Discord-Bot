package hummel

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import hummel.controller.DiscordController
import hummel.controller.impl.DiscordControllerImpl

class DiscordAdapter : Service() {
	private lateinit var wakeLock: WakeLock
	private val controller: DiscordController = DiscordControllerImpl()

	override fun onCreate() {
		wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
			newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Hundom::MyWakeLock")
		}
		controller.onCreate()
	}

	@SuppressLint("WakelockTimeout")
	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		wakeLock.acquire()
		ServiceCompat.startForeground(
			this,
			1,
			NotificationCompat.Builder(this, "HundomId1").build(),
			ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
		)
		controller.onStartCommand()
		return START_STICKY
	}

	override fun onDestroy() {
		ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
		wakeLock.release()
	}

	override fun onBind(intent: Intent?): IBinder? = null
}