package hummel

import android.app.Service
import android.content.Intent
import android.os.IBinder
import hummel.factory.DaoFactory
import hummel.factory.ServiceFactory
import org.javacord.api.DiscordApi
import org.javacord.api.DiscordApiBuilder

class DiscordService : Service() {
	private lateinit var api: DiscordApi

	override fun onCreate() {
		super.onCreate()
		DaoFactory.context = this
		val dao = DaoFactory.dao
		val token = dao.readFromFile("token.txt")
		api = DiscordApiBuilder().setToken(String(token)).setAllIntents().login().join()

		val loginService = ServiceFactory.loginService
		loginService.registerCommands(api)
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		api.addInteractionCreateListener {
			val dataService = ServiceFactory.dataService
			val userService = ServiceFactory.userService
			val moderService = ServiceFactory.moderService
			val adminService = ServiceFactory.adminService
			val ownerService = ServiceFactory.ownerService

			val server = it.interaction.server.get()
			val data = dataService.loadData(server)

			userService.eightBall(it, data)
			userService.choice(it, data)
			userService.random(it, data)
			userService.complete(it, data)

			moderService.setChance(it, data)
			moderService.addBirthday(it, data)
			moderService.getServerMessages(it, data)
			moderService.getServerData(it, data)
			moderService.getServerBirthdays(it, data)
			moderService.getCommands(it, data, api)

			adminService.clearServerMessages(it, data)
			adminService.clearServerBirthdays(it, data)
			adminService.clearServerGenerals(it, data)
			adminService.clearServerOfficers(it, data)
			adminService.clearSecretChannels(it, data)
			adminService.addSecretChannel(it, data)
			adminService.addOfficer(it, data)
			adminService.addGeneral(it, data)
			adminService.nuke(it, data)
			adminService.setLanguage(it, data)

			ownerService.exit(it, data)
			ownerService.shutdown(it, data)
			ownerService.import(it, data)

			dataService.saveData(server, data)
		}

		api.addMessageCreateListener {
			val dataService = ServiceFactory.dataService
			val botService = ServiceFactory.botService

			val server = it.server.get()
			val data = dataService.loadData(server)

			botService.saveAllowedMessage(it, data)
			botService.sendRandomMessage(it, data)
			botService.sendBirthdayMessage(it, data)

			dataService.saveData(server, data)
		}

		return START_STICKY
	}

	override fun onBind(intent: Intent?): IBinder? {
		return null
	}
}