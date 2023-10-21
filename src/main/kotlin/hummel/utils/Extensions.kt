package hummel.utils

import hummel.rand
import hummel.structures.ServerData
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.interaction.InteractionCreateEvent
import org.javacord.api.event.message.MessageCreateEvent
import org.javacord.api.interaction.SlashCommandInteraction
import java.awt.Color
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
	return server.isAdmin(user) || user.getRoles(server).any { role ->
		data.generals.any { it.roleID == role.id }
	}
}

fun InteractionCreateEvent.isOfficerMessage(data: ServerData): Boolean {
	val server = interaction.asSlashCommandInteraction().get().server.get()
	val user = interaction.asSlashCommandInteraction().get().user
	return server.isAdmin(user) || user.getRoles(server).any { role ->
		data.officers.any { it.roleID == role.id }
	}
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

fun EmbedBuilder.defErrEmbed(sc: SlashCommandInteraction, desc: String): EmbedBuilder {
	return setAuthor(sc.user).setTitle("Error").setColor(Color.RED).setDescription(desc)
}