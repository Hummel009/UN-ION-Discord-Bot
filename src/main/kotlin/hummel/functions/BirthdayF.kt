package hummel.functions

import hummel.functions
import hummel.prefix
import hummel.structures.Birthday
import hummel.structures.ServerData
import org.javacord.api.event.message.MessageCreateEvent
import java.time.LocalDate

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

fun addBirthday(event: MessageCreateEvent, data: ServerData) {
	if (event.messageContent.startsWith("${prefix}add_birthday")) {
		functions.add("add_birthday USER_ID MONTH DAY")
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

fun isBirthdayToday(data: ServerData): Pair<Boolean, Long> {
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

fun clearServerBirthdays(event: MessageCreateEvent, data: ServerData) {
	if (event.messageContent == "${prefix}clear_birthdays") {
		functions.add("clear_birthdays")
		data.birthday = HashSet()
		event.channel.sendMessage("Birthdays cleared.")
	}
}