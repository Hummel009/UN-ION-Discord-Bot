package hummel.service

import hummel.bean.ServerData
import org.javacord.api.DiscordApi
import org.javacord.api.event.interaction.InteractionCreateEvent

interface ModerService {
	fun setChance(event: InteractionCreateEvent, data: ServerData)
	fun addBirthday(event: InteractionCreateEvent, data: ServerData)
	fun getServerMessages(event: InteractionCreateEvent, data: ServerData)
	fun getServerData(event: InteractionCreateEvent, data: ServerData)
	fun getServerBirthdays(event: InteractionCreateEvent, data: ServerData)
	fun getCommands(event: InteractionCreateEvent, data: ServerData, api: DiscordApi)
}