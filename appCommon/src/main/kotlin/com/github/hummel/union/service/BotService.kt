package com.github.hummel.union.service

import org.javacord.api.event.message.MessageCreateEvent

interface BotService {
	fun addRandomEmoji(event: MessageCreateEvent)
	fun saveAllowedMessage(event: MessageCreateEvent)
	fun sendRandomMessage(event: MessageCreateEvent)
	fun sendBirthdayMessage(event: MessageCreateEvent)
}