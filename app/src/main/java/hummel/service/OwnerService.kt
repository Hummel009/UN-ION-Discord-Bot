package hummel.service

import hummel.bean.ServerData
import org.javacord.api.event.interaction.InteractionCreateEvent

interface OwnerService {
	fun exit(event: InteractionCreateEvent, data: ServerData)
	fun shutdown(event: InteractionCreateEvent, data: ServerData)
	fun import(event: InteractionCreateEvent, data: ServerData)
}