package com.github.hummel.union.service

import org.javacord.api.event.interaction.InteractionCreateEvent

interface AdminService {
	fun addBirthday(event: InteractionCreateEvent)
	fun addManager(event: InteractionCreateEvent)
	fun addSecretChannel(event: InteractionCreateEvent)
	fun clearBirthdays(event: InteractionCreateEvent)
	fun clearManagers(event: InteractionCreateEvent)
	fun clearSecretChannels(event: InteractionCreateEvent)
	fun clearMessages(event: InteractionCreateEvent)
	fun clearData(event: InteractionCreateEvent)
	fun setLanguage(event: InteractionCreateEvent)
	fun setChanceEmoji(event: InteractionCreateEvent)
	fun setChanceMessage(event: InteractionCreateEvent)
	fun nuke(event: InteractionCreateEvent)
}