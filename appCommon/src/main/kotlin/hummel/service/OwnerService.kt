package hummel.service

import org.javacord.api.event.interaction.InteractionCreateEvent

interface OwnerService {
	fun import(event: InteractionCreateEvent)
	fun export(event: InteractionCreateEvent)
	fun exit(event: InteractionCreateEvent)
}