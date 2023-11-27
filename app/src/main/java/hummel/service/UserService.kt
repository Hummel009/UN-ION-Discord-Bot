package hummel.service

import hummel.bean.ServerData
import org.javacord.api.event.interaction.InteractionCreateEvent

interface UserService {
	fun eightBall(event: InteractionCreateEvent, data: ServerData)
	fun choice(event: InteractionCreateEvent, data: ServerData)
	fun complete(event: InteractionCreateEvent, data: ServerData)
	fun random(event: InteractionCreateEvent, data: ServerData)
}