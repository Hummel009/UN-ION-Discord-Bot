package com.github.hummel.union.service

import org.javacord.api.event.interaction.InteractionCreateEvent

interface UserService {
	fun info(event: InteractionCreateEvent)
	fun complete(event: InteractionCreateEvent)
	fun clearContext(event: InteractionCreateEvent)
}