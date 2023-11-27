package hummel.service.impl

import hummel.service.CryptService

class CryptServiceImpl : CryptService {
	override fun encodeMessage(msg: String): String {
		return msg.codePoints().toArray().joinToString(" ")
	}

	override fun decodeMessage(msg: String): String {
		val unicodeCodes = msg.split(" ").map { it.toInt() }
		val unicodeChars = unicodeCodes.map { it.toChar() }.toCharArray()
		return String(unicodeChars)
	}
}