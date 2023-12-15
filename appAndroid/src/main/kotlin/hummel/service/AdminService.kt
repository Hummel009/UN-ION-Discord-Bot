package hummel.service

import hummel.bean.ServerData
import org.javacord.api.event.interaction.InteractionCreateEvent

interface AdminService {
	fun addBirthday(event: InteractionCreateEvent, data: ServerData)
	fun addManager(event: InteractionCreateEvent, data: ServerData)
	fun addSecretChannel(event: InteractionCreateEvent, data: ServerData)
	fun clearBirthdays(event: InteractionCreateEvent, data: ServerData)
	fun clearManagers(event: InteractionCreateEvent, data: ServerData)
	fun clearSecretChannels(event: InteractionCreateEvent, data: ServerData)
	fun clearMessages(event: InteractionCreateEvent, data: ServerData)
	fun clearData(event: InteractionCreateEvent, data: ServerData)
	fun setLanguage(event: InteractionCreateEvent, data: ServerData)
	fun setChance(event: InteractionCreateEvent, data: ServerData)
	fun nuke(event: InteractionCreateEvent, data: ServerData)
}