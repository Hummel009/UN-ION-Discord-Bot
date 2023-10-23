package hummel.functions

import hummel.structures.ServerData
import hummel.utils.*
import org.javacord.api.entity.message.embed.EmbedBuilder
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
		if (!event.isOfficerMessage(data)) {
			val embed = EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
			sc.respondLater().thenAccept {
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}
		} else {
			val arguments = sc.arguments[0].stringValue.get().split(" ")
			if (arguments.size == 3) {
				try {
					val userID = arguments[0].toLong()
					val month = if (arguments[1].toInt() in 1..12) arguments[1].toInt() else throw Exception()
					val range = ranges[month] ?: throw Exception()
					val day = if (arguments[2].toInt() in range) arguments[2].toInt() else throw Exception()
					val name = sc.server.get().getMemberById(userID).get().name
					data.birthdays.add(ServerData.Birthday(userID, name, ServerData.Date(day, month)))
					val embed = EmbedBuilder().success(sc, data, "${Lang.ADDED_BIRTHDAY.get(data)}: @$userID.")
					sc.respondLater().thenAccept {
						sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
					}
				} catch (e: Exception) {
					val embed = EmbedBuilder().error(sc, data, Lang.INVALID_FORMAT.get(data))
					sc.respondLater().thenAccept {
						sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
					}
				}
			} else {
				val embed = EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
				sc.respondLater().thenAccept {
					sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
				}
			}
		}
	}
}

fun isBirthdayToday(data: ServerData): Pair<Boolean, Set<Long>> {
	val currentDate = LocalDate.now()
	val currentDay = currentDate.dayOfMonth
	val currentMonth = currentDate.monthValue
	val userIDs = HashSet<Long>()
	var isBirthday = false

	for ((userID, _, date) in data.birthdays) {
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
		if (!event.isGeneralMessage(data)) {
			val embed = EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
			sc.respondLater().thenAccept {
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}
		} else {
			if (sc.arguments.isEmpty()) {
				data.birthdays.clear()
				val embed = EmbedBuilder().success(sc, data, Lang.CLEARED_BIRTHDAYS.get(data))
				sc.respondLater().thenAccept {
					sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
				}
			} else {
				val arguments = sc.arguments[0].stringValue.get().split(" ")
				if (arguments.size == 1) {
					try {
						val userID = arguments[0].toLong()
						data.birthdays.removeIf { it.userID == userID }
						val embed = EmbedBuilder().success(sc, data, "${Lang.REMOVED_BIRTHDAY.get(data)}: @$userID")
						sc.respondLater().thenAccept {
							sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
						}
					} catch (e: Exception) {
						val embed = EmbedBuilder().error(sc, data, Lang.INVALID_FORMAT.get(data))
						sc.respondLater().thenAccept {
							sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
						}
					}
				} else {
					val embed = EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
					sc.respondLater().thenAccept {
						sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
					}
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
		userIDs.forEach { event.channel.sendMessage("<@$it>, ${Lang.HAPPY_BIRTHDAY.get(data)}!") }
		data.lastWish.day = currentDay
		data.lastWish.month = currentMonth
	}
}