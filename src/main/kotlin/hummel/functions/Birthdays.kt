package hummel.functions

import hummel.structures.ServerData
import hummel.utils.defErrEmbed
import hummel.utils.defSuccessEmbed
import hummel.utils.isGeneralMessage
import hummel.utils.isOfficerMessage
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
			sc.respondLater().thenAccept {
				val embed = EmbedBuilder().defErrEmbed(sc, "You do not have permission to use this command.")
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}
		} else {
			val arguments = sc.arguments[0].stringValue.get().split(" ")
			if (arguments.size == 3) {
				sc.respondLater().thenAccept {
					try {
						val userID = arguments[0].toLong()
						val month = if (arguments[1].toInt() in 1..12) arguments[1].toInt() else throw Exception()
						val range = ranges[month] ?: throw Exception()
						val day = if (arguments[2].toInt() in range) arguments[2].toInt() else throw Exception()
						data.birthday.add(ServerData.Birthday(userID, day, month))
						val embed = EmbedBuilder().defSuccessEmbed(sc, "Added birthday: @$userID.")
						sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
					} catch (e: Exception) {
						val embed = EmbedBuilder().defErrEmbed(sc, "Invalid argument format.")
						sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
					}
				}
			} else {
				sc.respondLater().thenAccept {
					val embed = EmbedBuilder().defErrEmbed(sc, "Invalid arguments provided.")
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

	for ((userID, day, month) in data.birthday) {
		if (day == currentDay && month == currentMonth) {
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
			sc.respondLater().thenAccept {
				val embed = EmbedBuilder().defErrEmbed(sc, "You do not have permission to use this command.")
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}
		} else {
			if (sc.arguments.isEmpty()) {
				sc.respondLater().thenAccept {
					data.birthday.clear()
					val embed = EmbedBuilder().defSuccessEmbed(sc, "Birthdays cleared.")
					sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
				}
			} else {
				val arguments = sc.arguments[0].stringValue.get().split(" ")
				if (arguments.size == 1) {
					sc.respondLater().thenAccept {
						try {
							val userID = arguments[0].toLong()
							data.birthday.removeIf { it.userID == userID }
							val embed = EmbedBuilder().defSuccessEmbed(sc, "Removed birthday: @$userID")
							sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
						} catch (e: Exception) {
							val embed = EmbedBuilder().defErrEmbed(sc, "Invalid argument format.")
							sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
						}
					}
				} else {
					sc.respondLater().thenAccept {
						val embed = EmbedBuilder().defErrEmbed(sc, "Invalid arguments provided.")
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
		userIDs.forEach { event.channel.sendMessage("<@$it>, с днём рождения!") }
		data.lastWish.day = currentDay
		data.lastWish.month = currentMonth
	}
}