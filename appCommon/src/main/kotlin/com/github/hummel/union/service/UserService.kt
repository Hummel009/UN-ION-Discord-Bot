package com.github.hummel.union.service

import org.javacord.api.event.interaction.InteractionCreateEvent

interface UserService {
	fun complete(event: InteractionCreateEvent)
	fun info(event: InteractionCreateEvent)
	fun aiAnswer(event: InteractionCreateEvent)
	fun aiClear(event: InteractionCreateEvent)
}