package hummel.service

import hummel.bean.ServerData
import org.javacord.api.event.interaction.InteractionCreateEvent

interface AdminService {
	fun clearServerMessages(event: InteractionCreateEvent, data: ServerData)
	fun clearServerBirthdays(event: InteractionCreateEvent, data: ServerData)
	fun clearServerGenerals(event: InteractionCreateEvent, data: ServerData)
	fun clearServerOfficers(event: InteractionCreateEvent, data: ServerData)
	fun clearSecretChannels(event: InteractionCreateEvent, data: ServerData)
	fun addOfficer(event: InteractionCreateEvent, data: ServerData)
	fun addGeneral(event: InteractionCreateEvent, data: ServerData)
	fun addSecretChannel(event: InteractionCreateEvent, data: ServerData)
	fun nuke(event: InteractionCreateEvent, data: ServerData)
	fun setLanguage(event: InteractionCreateEvent, data: ServerData)
}