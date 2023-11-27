package hummel.service

interface CryptService {
	fun encodeMessage(msg: String): String
	fun decodeMessage(msg: String): String
}