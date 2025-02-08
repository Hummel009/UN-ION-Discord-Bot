package com.github.hummel.union.service

import org.javacord.api.event.interaction.InteractionCreateEvent

interface ManagerService {
	fun addBirthday(event: InteractionCreateEvent)
	fun addManager(event: InteractionCreateEvent)
	fun addSecretChannel(event: InteractionCreateEvent)
	fun addMutedChannel(event: InteractionCreateEvent)
	fun clearBirthdays(event: InteractionCreateEvent)
	fun clearManagers(event: InteractionCreateEvent)
	fun clearSecretChannels(event: InteractionCreateEvent)
	fun clearMutedChannels(event: InteractionCreateEvent)
	fun clearBank(event: InteractionCreateEvent)
	fun clearData(event: InteractionCreateEvent)
	fun setLanguage(event: InteractionCreateEvent)
	fun setChanceMessage(event: InteractionCreateEvent)
	fun setChanceEmoji(event: InteractionCreateEvent)
	fun setChanceAI(event: InteractionCreateEvent)
}