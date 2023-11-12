package hummel.functions

import hummel.structures.ServerData
import hummel.utils.*
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.interaction.InteractionCreateEvent
import org.javacord.api.event.message.MessageCreateEvent
import java.time.LocalDate
import java.time.Month

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
		sc.respondLater().thenAccept {
			val embed = if (!event.isOfficerMessage(data)) {
				EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
			} else {
				val arguments = sc.arguments[0].stringValue.get().split(" ")
				if (arguments.size == 3) {
					try {
						val userID = arguments[0].toLong()
						val month = if (arguments[1].toInt() in 1..12) arguments[1].toInt() else throw Exception()
						val range = ranges[month] ?: throw Exception()
						val day = if (arguments[2].toInt() in range) arguments[2].toInt() else throw Exception()
						if (!sc.server.get().getMemberById(userID).isPresent) {
							throw Exception()
						}
						data.birthdays.add(ServerData.Birthday(userID, ServerData.Date(day, month)))
						EmbedBuilder().success(sc, data, "${Lang.ADDED_BIRTHDAY.get(data)}: @$userID.")
					} catch (e: Exception) {
						EmbedBuilder().error(sc, data, Lang.INVALID_FORMAT.get(data))
					}
				} else {
					EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
				}
			}
			sc.createFollowupMessageBuilder().addEmbed(embed).send()
		}.get()
	}
}

fun isBirthdayToday(data: ServerData): Pair<Boolean, Set<Long>> {
	val currentDate = LocalDate.now()
	val currentDay = currentDate.dayOfMonth
	val currentMonth = currentDate.monthValue
	val userIDs = HashSet<Long>()
	var isBirthday = false

	for ((userID, date) in data.birthdays) {
		if (date.day == currentDay && date.month == currentMonth) {
			isBirthday = true
			userIDs.add(userID)
		}
	}
	return isBirthday to userIDs
}

fun clearServerBirthdays(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()

	if (sc.fullCommandName.contains("clear_birthdays")) {
		sc.respondLater().thenAccept {
			val embed = if (!event.isGeneralMessage(data)) {
				EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
			} else {
				if (sc.arguments.isEmpty()) {
					data.birthdays.clear()
					EmbedBuilder().success(sc, data, Lang.CLEARED_BIRTHDAYS.get(data))
				} else {
					val arguments = sc.arguments[0].stringValue.get().split(" ")
					if (arguments.size == 1) {
						try {
							val userID = arguments[0].toLong()
							data.birthdays.removeIf { it.userID == userID }
							EmbedBuilder().success(sc, data, "${Lang.REMOVED_BIRTHDAY.get(data)}: @$userID")
						} catch (e: Exception) {
							EmbedBuilder().error(sc, data, Lang.INVALID_FORMAT.get(data))
						}
					} else {
						EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
					}
				}
			}
			sc.createFollowupMessageBuilder().addEmbed(embed).send()
		}.get()
	}
}

fun getServerBirthdays(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()

	if (sc.fullCommandName.contains("get_birthdays")) {
		sc.respondLater().thenAccept {
			val embed = if (!event.isOfficerMessage(data)) {
				EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
			} else {
				data.birthdays.removeIf { !sc.server.get().getMemberById(it.userID).isPresent }
				val text = if (data.birthdays.isEmpty()) Lang.NO_BIRTHDAYS.get(data) else buildString {
					data.birthdays.sortedWith(compareBy({ it.date.month }, { it.date.day })).joinTo(this, "\r\n") {
						val userName = sc.server.get().getMemberById(it.userID).get().name
						"$userName: ${it.date.day} ${Month.of(it.date.month)}"
					}
				}
				EmbedBuilder().success(sc, data, text)
			}
			sc.createFollowupMessageBuilder().addEmbed(embed).send()
		}.get()
	}
}

fun sendBirthdayMessage(event: MessageCreateEvent, data: ServerData) {
	val currentDate = LocalDate.now()
	val currentDay = currentDate.dayOfMonth
	val currentMonth = currentDate.monthValue

	val (isBirthday, userIDs) = isBirthdayToday(data)

	if (isBirthday && (currentDay != data.lastWish.day || currentMonth != data.lastWish.month)) {
		userIDs.forEach { event.channel.sendMessage("<@$it>, ${Lang.HAPPY_BIRTHDAY.get(data)}!") }
		data.lastWish.day = currentDay
		data.lastWish.month = currentMonth
	}
}