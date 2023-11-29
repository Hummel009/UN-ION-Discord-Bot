package hummel.service

import org.javacord.api.DiscordApi

interface LoginService {
	fun registerCommands(api: DiscordApi)
	fun deleteCommands(api: DiscordApi)
}