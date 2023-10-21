package hummel.utils

import hummel.rand
import hummel.structures.ServerData
import org.javacord.api.event.interaction.InteractionCreateEvent
import org.javacord.api.event.message.MessageCreateEvent
import java.nio.file.Files
import java.nio.file.Path

fun MessageCreateEvent.isAllowedMessage(): Boolean {
	val contain = setOf("@", "http", "\r", "\n")
	val start = setOf("!", "?")

	if (start.any { messageContent.startsWith(it) } || contain.any { messageContent.contains(it) }) {
		return false
	}

	if (messageAuthor.isYourself || messageAuthor.isBotUser) {
		return false
	}

	return messageContent.length >= 2
}

fun InteractionCreateEvent.isGeneralMessage(data: ServerData): Boolean {
	val server = interaction.asSlashCommandInteraction().get().server.get()
	val user = interaction.asSlashCommandInteraction().get().user
	if (server.isAdmin(user)) {
		return true
	}
	val roles = user.getRoles(server)
	var flag = false
	for (role in roles) {
		if (data.generals.map { it.roleID }.contains(role.id)) {
			flag = true
			break
		}
	}
	return flag
}

fun InteractionCreateEvent.isOfficerMessage(data: ServerData): Boolean {
	val server = interaction.asSlashCommandInteraction().get().server.get()
	val user = interaction.asSlashCommandInteraction().get().user
	if (server.isAdmin(user)) {
		return true
	}
	val roles = user.getRoles(server)
	var flag = false
	for (role in roles) {
		if (data.officers.map { it.roleID }.contains(role.id)) {
			flag = true
			break
		}
	}
	return flag
}

fun Path.getRandomLine(): String? {
	val lines = Files.readAllLines(this)
	if (lines.isNotEmpty()) {
		val randomLine = lines[rand.nextInt(lines.size)]
		if (randomLine.isNotEmpty()) {
			val unicodeCodes = randomLine.split(" ").map { it.toInt() }
			val unicodeChars = unicodeCodes.map { it.toChar() }.toCharArray()
			return String(unicodeChars)
		}
		return null
	} else {
		return null
	}
}