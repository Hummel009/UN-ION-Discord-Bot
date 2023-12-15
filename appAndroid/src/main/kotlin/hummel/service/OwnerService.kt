package hummel.service

import hummel.bean.ServerData
import org.javacord.api.DiscordApi
import org.javacord.api.event.interaction.InteractionCreateEvent

interface OwnerService {
	fun commands(event: InteractionCreateEvent, data: ServerData, api: DiscordApi)
	fun import(event: InteractionCreateEvent, data: ServerData)
	fun export(event: InteractionCreateEvent, data: ServerData)

	@Deprecated("Use mobile app instead")
	fun exit(event: InteractionCreateEvent, data: ServerData)

	@Deprecated("Use mobile app instead")
	fun shutdown(event: InteractionCreateEvent, data: ServerData)
}