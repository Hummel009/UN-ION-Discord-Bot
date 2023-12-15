package hummel

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import androidx.core.app.NotificationCompat
import hummel.factory.DaoFactory
import hummel.factory.ServiceFactory
import hummel.union.R
import org.javacord.api.DiscordApi
import org.javacord.api.DiscordApiBuilder
import java.util.*

const val dataVer: Int = 3
val random: Random = Random()

class DiscordService : Service() {
	private lateinit var wakeLock: WakeLock
	private lateinit var api: DiscordApi

	override fun onCreate() {
		super.onCreate()
		DaoFactory.context = this
		wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
			newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Hundom::MyWakeLock")
		}

		val fileDao = DaoFactory.fileDao
		val token = fileDao.readFromFile("token.txt")
		api = DiscordApiBuilder().setToken(String(token)).setAllIntents().login().join()

		/**
		 * val loginService = ServiceFactory.loginService
		 * loginService.deleteCommands(api)
		 * loginService.registerCommands(api)
		 **/
	}

	@SuppressLint("ForegroundServiceType", "WakelockTimeout")
	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		wakeLock.acquire()
		val notification = createNotification()
		startForeground(1, notification)
		api.addInteractionCreateListener {
			val dataService = ServiceFactory.dataService
			val userService = ServiceFactory.userService
			val adminService = ServiceFactory.adminService
			val ownerService = ServiceFactory.ownerService

			val server = it.interaction.server.get()
			val data = dataService.loadData(server)

			userService.answer(it, data)
			userService.choice(it, data)
			userService.random(it, data)
			userService.complete(it, data)
			userService.info(it, data)

			adminService.addBirthday(it, data)
			adminService.addManager(it, data)
			adminService.addSecretChannel(it, data)
			adminService.clearBirthdays(it, data)
			adminService.clearManagers(it, data)
			adminService.clearSecretChannels(it, data)
			adminService.clearMessages(it, data)
			adminService.clearData(it, data)
			adminService.setLanguage(it, data)
			adminService.setChance(it, data)
			adminService.nuke(it, data)

			ownerService.commands(it, data, api)
			ownerService.import(it, data)
			ownerService.export(it, data)
			ownerService.exit(it, data)
			ownerService.shutdown(it, data)

			dataService.saveData(server, data)
		}

		api.addMessageCreateListener {
			val dataService = ServiceFactory.dataService
			val botService = ServiceFactory.botService

			val server = it.server.get()
			val data = dataService.loadData(server)

			botService.addRandomEmoji(it, data)
			botService.saveAllowedMessage(it, data)
			botService.sendRandomMessage(it, data)
			botService.sendBirthdayMessage(it, data)

			dataService.saveData(server, data)
		}

		return START_STICKY
	}

	override fun onBind(intent: Intent?): IBinder? = null

	override fun onDestroy() {
		super.onDestroy()
		wakeLock.release()
	}

	private fun createNotification(): Notification {
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

		return notificationBuilder.build()
	}

}