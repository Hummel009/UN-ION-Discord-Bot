package hummel.service.impl

import hummel.bean.ServerData
import hummel.factory.DaoFactory
import hummel.service.ModerService
import hummel.utils.Lang
import hummel.utils.access
import hummel.utils.error
import hummel.utils.success
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.interaction.InteractionCreateEvent
import java.time.Month

class ModerServiceImpl : ModerService {
	private val dao = DaoFactory.dao

	private val ranges: Map<Int, IntRange> = mapOf(
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

	override fun setChance(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("set_chance")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromModerAtLeast(data)) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				} else {
					val arguments = sc.arguments[0].stringValue.get().split(" ")
					if (arguments.size == 1) {
						try {
							val chance = arguments[0].toInt()
							if (chance < 1) {
								throw Exception()
							}
							data.chance = chance
							EmbedBuilder().success(sc, data, "${Lang.SET_CHANCE.get(data)}: $chance.")
						} catch (e: Exception) {
							EmbedBuilder().error(sc, data, Lang.INVALID_FORMAT.get(data))
						}
					} else {
						EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
					}
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun addBirthday(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("add_birthday")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromModerAtLeast(data)) {
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
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun getServerMessages(event: InteractionCreateEvent, data: ServerData) {
		forkSendAndDelete(event, data, "messages", "bin")
	}

	override fun getServerData(event: InteractionCreateEvent, data: ServerData) {
		forkSendAndDelete(event, data, "data", "json")
	}

	override fun getServerBirthdays(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("get_birthdays")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromModerAtLeast(data)) {
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
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun getCommands(event: InteractionCreateEvent, data: ServerData, api: DiscordApi) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("get_commands")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromModerAtLeast(data)) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				} else {
					val text = buildString {
						api.globalApplicationCommands.get().joinTo(this, "\r\n") { "${it.name}: ${it.id}" }
					}
					EmbedBuilder().success(sc, data, text)
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	private fun forkSendAndDelete(
		event: InteractionCreateEvent, data: ServerData, fileName: String, fileExtension: String
	) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("get_$fileName")) {
			if (!event.fromModerAtLeast(data)) {
				sc.respondLater().thenAccept {
					val embed = EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
					sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
				}.get()
			} else {
				val destinationPath = "${data.serverID}/$fileName-backup.$fileExtension"
				sc.respondLater().thenAccept {
					val filePath = "${data.serverID}/$fileName.$fileExtension"
					val text = dao.readFromFile(filePath)
					dao.writeToFile(destinationPath, text)
					val backupFile = dao.getFile(destinationPath)
					sc.createFollowupMessageBuilder().addAttachment(backupFile).send().get()
				}.get()
				dao.removeFile(destinationPath)
			}
		}
	}

	private fun InteractionCreateEvent.fromModerAtLeast(data: ServerData): Boolean {
		val sc = interaction.asSlashCommandInteraction().get()
		val server = sc.server.get()
		val user = sc.user
		return user.isBotOwner || server.isAdmin(user) || user.getRoles(server).any { role ->
			data.generals.any { it.roleID == role.id }
		} || user.getRoles(server).any { role ->
			data.officers.any { it.roleID == role.id }
		}
	}
}