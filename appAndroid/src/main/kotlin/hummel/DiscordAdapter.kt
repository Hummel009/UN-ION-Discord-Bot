package hummel

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import androidx.core.app.NotificationCompat
import hummel.controller.DiscordController
import hummel.controller.impl.DiscordControllerImpl
import hummel.union.R

class DiscordAdapter : Service() {
	private lateinit var wakeLock: WakeLock
	private val controller: DiscordController = DiscordControllerImpl(token, ownerId.toLong(), this)

	override fun onCreate() {
		wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
			newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Hundom::MyWakeLock")
		}
		controller.onCreate()
	}

	@SuppressLint("ForegroundServiceType", "WakelockTimeout")
	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		wakeLock.acquire()
		val channelId = "Hummel009id1"
		val channelName = "Hummel009channel1"
		val notificationBuilder = NotificationCompat.Builder(this, channelId).run {
			setContentTitle("Foreground Service")
			setContentText("Your service is running")
			setSmallIcon(R.drawable.ic_launcher_background)
			setPriority(NotificationCompat.PRIORITY_MAX)
		}
		val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
		val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		notificationManager.createNotificationChannel(channel)
		val notification = notificationBuilder.build()
		startForeground(1, notification)
		controller.onStartCommand()
		return START_STICKY
	}

	override fun onDestroy() {
		stopForeground(STOP_FOREGROUND_REMOVE)
		wakeLock.release()
	}

	override fun onBind(intent: Intent?): IBinder? = null
}