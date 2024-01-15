package hummel.service

import hummel.controller.impl.DiscordControllerImpl

interface LoginService {
	fun loginBot(impl: DiscordControllerImpl)
	fun configureFactory(impl: DiscordControllerImpl)
	fun deleteCommands(impl: DiscordControllerImpl)
	fun registerCommands(impl: DiscordControllerImpl)
}