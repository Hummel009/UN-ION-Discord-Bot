package hummel

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import hummel.factory.DaoFactory
import hummel.factory.ServiceFactory
import org.javacord.api.DiscordApiBuilder
import java.util.*

const val dataVer: Int = 3
val random: Random = Random()

class DiscordService(private var context: Context, params: WorkerParameters) : Worker(context, params) {
	override fun doWork(): Result {
		DaoFactory.context = context
		val fileDao = DaoFactory.fileDao
		val token = fileDao.readFromFile("token.txt")
		val api = DiscordApiBuilder().setToken(String(token)).setAllIntents().login().join()

		/**
		 * val loginService = ServiceFactory.loginService
		 * loginService.deleteCommands(api)
		 * loginService.registerCommands(api)
		 **/

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

		return Result.success()
	}
}