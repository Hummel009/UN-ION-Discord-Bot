package hummel.controller.impl

import hummel.controller.DiscordController
import hummel.factory.ServiceFactory
import org.javacord.api.DiscordApi

class DiscordControllerImpl(val token: String, val ownerId: Long, val context: Any) : DiscordController {
	lateinit var api: DiscordApi

	override fun onCreate() {
		val loginService = ServiceFactory.loginService
		loginService.loginBot(this)
		loginService.configureFactory(this)
		loginService.deleteCommands(this)
		loginService.registerCommands(this)
	}

	override fun onStartCommand() {
		val userService = ServiceFactory.userService
		val adminService = ServiceFactory.adminService
		val ownerService = ServiceFactory.ownerService
		val botService = ServiceFactory.botService

		api.addInteractionCreateListener {
			userService.answer(it)
			userService.choice(it)
			userService.random(it)
			userService.complete(it)
			userService.info(it)

			adminService.addBirthday(it)
			adminService.addManager(it)
			adminService.addSecretChannel(it)
			adminService.clearBirthdays(it)
			adminService.clearManagers(it)
			adminService.clearSecretChannels(it)
			adminService.clearMessages(it)
			adminService.clearData(it)
			adminService.setLanguage(it)
			adminService.setChance(it)
			adminService.nuke(it)

			ownerService.import(it)
			ownerService.export(it)
			ownerService.exit(it)
		}

		api.addMessageCreateListener {
			botService.addRandomEmoji(it)
			botService.saveAllowedMessage(it)
			botService.sendRandomMessage(it)
			botService.sendBirthdayMessage(it)
		}
	}
}