package hummel.functions

import hummel.structures.Birthday
import hummel.structures.ServerData
import org.javacord.api.event.interaction.InteractionCreateEvent
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

fun addBirthday(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains("add_birthday")) {
		val arguments = sc.arguments[0].stringValue.get().split(" ")
		if (arguments.size == 3) {
			try {
				val userID = arguments[0].toLong()
				val month = if (arguments[1].toInt() in 1..12) arguments[1].toInt() else throw Exception()
				val range = ranges[month] ?: throw Exception()
				val day = if (arguments[2].toInt() in range) arguments[2].toInt() else throw Exception()
				data.birthday.add(Birthday(userID, day, month))
				sc.createImmediateResponder().setContent("Added birthday: @$userID, \"$day.$month\".").respond()
			} catch (e: NumberFormatException) {
				sc.createImmediateResponder().setContent("Invalid argument format").respond()
			}
		} else {
			sc.createImmediateResponder().setContent("No arguments provided.").respond()
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

fun clearServerBirthdays(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains("clear_birthdays")) {
		if (sc.arguments.isEmpty()) {
			data.birthday = HashSet()
			sc.createImmediateResponder().setContent("Birthdays cleared.").respond()
		} else {
			val arguments = sc.arguments[0].stringValue.get().split(" ")
			when (arguments.size) {
				1 -> {
					try {
						val userID = arguments[0].toLong()
						val set = HashSet<Birthday>()
						for (birthday in data.birthday) {
							if (userID != birthday.userID) {
								set.add(birthday)
							}
						}
						data.birthday = set
						sc.createImmediateResponder().setContent("Removed birthday of user @$userID").respond()
					} catch (e: Exception) {
						sc.createImmediateResponder().setContent("Invalid argument format.").respond()
					}
				}

				else -> {
					sc.createImmediateResponder().setContent("Wrong command usage.").respond()
				}
			}
		}
	}
}

fun sendBirthdayMessage(event: MessageCreateEvent, data: ServerData) {
	val currentDate = LocalDate.now()
	val currentDay = currentDate.dayOfMonth
	val currentMonth = currentDate.monthValue

	val (isBirthday, userIDs) = isBirthdayToday(data)

	if (isBirthday && (currentDay != data.lastWish.day || currentMonth != data.lastWish.month)) {
		userIDs.forEach { event.channel.sendMessage("<@$it>, с днём рождения!") }
		data.lastWish.day = currentDay
		data.lastWish.month = currentMonth
	}
}