package hummel.utils

import hummel.rand
import java.nio.file.Files
import java.nio.file.Path

fun String.isMessageForbidden(): Boolean {
	val start = setOf("!", "?")
	val contain = setOf("@", "http", "\r", "\n")

	if (start.any { startsWith(it) } || contain.any { contains(it) }) {
		return true
	}

	return this.length < 2
}

fun Path.getRandomLine(): String? {
	val lines = Files.readAllLines(this)
	if (lines.isNotEmpty()) {
		val randomLine = lines[rand.nextInt(lines.size)]
		if (!randomLine.isMessageForbidden()) {
			val unicodeCodes = randomLine.split(" ").map { it.toInt() }
			val unicodeChars = unicodeCodes.map { it.toChar() }.toCharArray()
			return String(unicodeChars)
		}
		return null
	} else {
		return null
	}
}