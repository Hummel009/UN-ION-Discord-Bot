package hummel.utils

import hummel.rand
import org.javacord.api.event.message.MessageCreateEvent
import java.nio.file.Files
import java.nio.file.Path

fun MessageCreateEvent.isAllowedMessage(): Boolean {
	val start = setOf("!", "?")

	if (start.any { messageContent.startsWith(it) }) {
		return false
	}

	return isAllowedCommand()
}

fun MessageCreateEvent.isAllowedCommand(): Boolean {
	val contain = setOf("@", "http", "\r", "\n")

	if (contain.any { messageContent.contains(it) }) {
		return false
	}

	if (messageAuthor.isYourself || messageAuthor.isBotUser) {
		return false
	}

	return messageContent.length >= 2
}

fun MessageCreateEvent.isGeneralMessage(): Boolean = messageAuthor.isServerAdmin

fun MessageCreateEvent.isOfficerMessage(): Boolean = messageAuthor.canManageMessagesInTextChannel()

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