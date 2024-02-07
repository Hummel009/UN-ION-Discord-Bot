package com.github.hummel.union.service

import org.javacord.api.event.interaction.InteractionCreateEvent

interface UserService {
	fun answer(event: InteractionCreateEvent)
	fun choice(event: InteractionCreateEvent)
	fun complete(event: InteractionCreateEvent)
	fun random(event: InteractionCreateEvent)
	fun info(event: InteractionCreateEvent)
}