package hummel.service

import hummel.bean.ServerData
import org.javacord.api.event.message.MessageCreateEvent

interface BotService {
	fun saveAllowedMessage(event: MessageCreateEvent, data: ServerData)
	fun sendRandomMessage(event: MessageCreateEvent, data: ServerData)
	fun sendBirthdayMessage(event: MessageCreateEvent, data: ServerData)
}