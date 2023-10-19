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
	functions.add("add_birthday USER_ID MONTH DAY")
	if (event.messageContent.startsWith("${prefix}add_birthday")) {
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
			event.channel.sendMessage("Wrong command usage.")
		}
	}
}

fun isBirthdayToday(data: ServerData): Pair<Boolean, Set<Long>> {
	val currentDate = LocalDate.now()
	val currentDay = currentDate.dayOfMonth
	val currentMonth = currentDate.monthValue
	val set = HashSet<Long>()
	var isBirthdayToday = false

	for ((userID, day, month) in data.birthday) {
		if (day == currentDay && month == currentMonth) {
			isBirthdayToday = true
			set.add(userID)
		}
	}
	return isBirthdayToday to set
}

fun clearServerBirthdays(event: MessageCreateEvent, data: ServerData) {
	functions.add("delete_birthday [USER_ID]")
	if (event.messageContent.startsWith("${prefix}delete_birthday")) {
		val parameters = event.messageContent.split(" ")
		when (parameters.size) {
			1 -> {
				data.birthday = HashSet()
				event.channel.sendMessage("Birthdays cleared.")
			}

			2 -> {
				try {
					val userID = parameters[1].toLong()
					val set = HashSet<Birthday>()
					for (birthday in data.birthday) {
						if (userID != birthday.userID) {
							set.add(birthday)
						}
					}
					data.birthday = set
					event.channel.sendMessage("Removed birthday of user <@$userID>")
				} catch (e: Exception) {
					event.channel.sendMessage("Invalid integers after !birthday.")
				}
			}

			else -> {
				event.channel.sendMessage("Wrong command usage.")
			}
		}
	}
}

fun sendBirthdayMessage(event: MessageCreateEvent, data: ServerData) {
	val currentDate = LocalDate.now()
	val currentDay = currentDate.dayOfMonth
	val currentMonth = currentDate.monthValue

	val (isBirthday, userIDs) = isBirthdayToday(data)

	if (isBirthday && currentDay != data.lastWish.day && currentMonth != data.lastWish.month) {
		userIDs.forEach { event.channel.sendMessage("<@$it>, с днём рождения!") }
		data.lastWish.day = currentDay
		data.lastWish.month = currentMonth
	}
}