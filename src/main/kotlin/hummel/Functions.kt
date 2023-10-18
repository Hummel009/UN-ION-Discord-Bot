package hummel

import org.javacord.api.event.message.MessageCreateEvent
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun sendMessage(event: MessageCreateEvent, data: ServerInfo) {
	val path = Paths.get("${data.serverID}/messages.bin")

	if (rand.nextInt(data.chance) == 0) {
		val randomLine = path.getRandomLine()
		randomLine?.let {
			event.channel.sendMessage(it)
		}
	}
}

fun saveMessage(event: MessageCreateEvent, data: ServerInfo) {
	val path = Paths.get("${data.serverID}/messages.bin")
	val ints = event.messageContent.codePoints().toArray()
	Files.write(
		path, ints.joinToString(" ").toByteArray(StandardCharsets.UTF_8), StandardOpenOption.APPEND
	)
	Files.write(path, "\r\n".toByteArray(StandardCharsets.UTF_8), StandardOpenOption.APPEND)
}

fun registerClearDatabaseFunc(event: MessageCreateEvent, data: ServerInfo) {
	if (event.messageContent == "!clear database") {
		val path = Paths.get("${data.serverID}/messages.bin")
		Files.write(path, byteArrayOf())
		event.channel.sendMessage("Database cleared.")
	}
}

fun registerBackupDatabaseFunc(event: MessageCreateEvent, data: ServerInfo) {
	if (event.messageContent == "!backup database") {
		val path = Paths.get("${data.serverID}/messages.bin")

		val timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
		val destinationPath = Paths.get("${data.serverID}/messages-$timeStamp.bin")

		try {
			Files.copy(path, destinationPath)
			val backupFile = File(destinationPath.toString())
			val future = event.channel.sendMessage(backupFile)
			future.get()
			Files.delete(destinationPath)
		} catch (e: Exception) {
			event.channel.sendMessage("Error backuping database.")
		}
	}
}

fun registerMessageChanceFunc(event: MessageCreateEvent, data: ServerInfo) {
	if (event.messageContent.startsWith("!chance")) {
		val parameters = event.messageContent.split(" ")
		if (parameters.size == 2) {
			try {
				data.chance = parameters[1].toInt()
				event.channel.sendMessage("Chance changed to ${data.chance}.")
			} catch (e: NumberFormatException) {
				event.channel.sendMessage("Invalid integer format after !chance.")
			}
		} else {
			event.channel.sendMessage("No integer provided after !chance.")
		}
	}
}

fun registerGetInfoFunc(event: MessageCreateEvent, data: ServerInfo) {
	if (event.messageContent == "!getinfo") {
		event.channel.sendMessage(
			"""
			FULL SERVER INFO:
			>> Server name: ${data.serverName};
			>> Server id: ${data.serverID};
			>> Message chance: ${data.chance};
			""".trimIndent()
		)
	}
}

val ranges: Map<Int, IntRange> = mapOf(
	1 to 1..31,
	2 to 1..29,
	3 to 1..31,
	4 to 1..30,
	5 to 1..31,
	6 to 1..30,
	7 to 1..31,
	8 to 1..31,
	9 to 1..30,
	10 to 1..31,
	11 to 1..30,
	12 to 1..31,
)

fun registerAddBirthdayFunc(event: MessageCreateEvent, data: ServerInfo) {
	if (event.messageContent.startsWith("!birthday")) {
		val parameters = event.messageContent.split(" ")
		if (parameters.size == 4) {
			try {
				val userID = parameters[1].toLong()
				val month = if (parameters[2].toInt() in 1..12) parameters[2].toInt() else throw Exception()
				val range = ranges[month] ?: throw Exception()
				val day = if (parameters[3].toInt() in range) parameters[3].toInt() else throw Exception()
				data.birthday.add(Birthday(userID, day, month))
				event.channel.sendMessage("Added birthday: @$userID, \"$day.$month\".")
			} catch (e: Exception) {
				event.channel.sendMessage("Invalid integers after !birthday.")
			}
		} else {
			event.channel.sendMessage("No integers provided after !birthday.")
		}
	}
}

fun isBirthdayToday(data: ServerInfo): Pair<Boolean, Long> {
	val currentDate = LocalDate.now()
	val currentDay = currentDate.dayOfMonth
	val currentMonth = currentDate.monthValue

	for ((userID, day, month) in data.birthday) {
		if (day == currentDay && month == currentMonth) {
			return true to userID
		}
	}
	return false to 0
}

fun sendBirthdayMessage(event: MessageCreateEvent, userID: Long) {
	event.channel.sendMessage("<@$userID>, с днём рождения!")
}

fun registerClearBirthdaysFunc(event: MessageCreateEvent, data: ServerInfo) {
	if (event.messageContent == "!clear birthdays") {
		data.birthday = HashSet()
		event.channel.sendMessage("Birthdays cleared.")
	}
}