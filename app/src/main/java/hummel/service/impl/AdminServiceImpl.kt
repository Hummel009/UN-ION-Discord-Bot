package hummel.service.impl

import hummel.bean.ServerData
import hummel.dao.FileDao
import hummel.factory.DaoFactory
import hummel.service.AdminService
import hummel.utils.Lang
import hummel.utils.access
import hummel.utils.error
import hummel.utils.success
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.interaction.InteractionCreateEvent

class AdminServiceImpl : AdminService {
	private val fileDao: FileDao = DaoFactory.fileDao

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

	override fun addBirthday(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("add_birthday")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromAdminAtLeast(data)) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				} else {
					val arguments = sc.arguments[0].stringValue.get().split(" ")
					if (arguments.size == 3) {
						try {
							val userId = arguments[0].toLong()
							val month = if (arguments[1].toInt() in 1..12) arguments[1].toInt() else throw Exception()
							val range = ranges[month] ?: throw Exception()
							val day = if (arguments[2].toInt() in range) arguments[2].toInt() else throw Exception()
							if (!sc.server.get().getMemberById(userId).isPresent) {
								throw Exception()
							}
							data.birthdays.add(ServerData.Birthday(userId, ServerData.Date(day, month)))
							EmbedBuilder().success(sc, data, "${Lang.ADDED_BIRTHDAY.get(data)}: @$userId.")
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

	override fun addManager(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("add_manager")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromAdminAtLeast(data)) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				} else {
					val arguments = sc.arguments[0].stringValue.get().split(" ")
					if (arguments.size == 1) {
						try {
							val roleId = arguments[0].toLong()
							if (!sc.server.get().getRoleById(roleId).isPresent) {
								throw Exception()
							}
							data.managers.add(ServerData.Role(roleId))
							EmbedBuilder().success(sc, data, "${Lang.ADDED_MANAGER.get(data)}: @$roleId.")
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

	override fun addSecretChannel(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("add_secret_channel")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromAdminAtLeast(data)) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				} else {
					val arguments = sc.arguments[0].stringValue.get().split(" ")
					if (arguments.size == 1) {
						try {
							val channelId = arguments[0].toLong()
							if (!sc.server.get().getChannelById(channelId).isPresent) {
								throw Exception()
							}
							data.secretChannels.add(ServerData.Channel(channelId))
							EmbedBuilder().success(sc, data, "${Lang.ADDED_CHANNEL.get(data)}: @$channelId.")
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

	override fun clearBirthdays(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("clear_birthdays")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromAdminAtLeast(data)) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				} else {
					if (sc.arguments.isEmpty()) {
						data.birthdays.clear()
						EmbedBuilder().success(sc, data, Lang.CLEARED_BIRTHDAYS.get(data))
					} else {
						val arguments = sc.arguments[0].stringValue.get().split(" ")
						if (arguments.size == 1) {
							try {
								val userId = arguments[0].toLong()
								data.birthdays.removeIf { it.id == userId }
								EmbedBuilder().success(sc, data, "${Lang.REMOVED_BIRTHDAY.get(data)}: @$userId")
							} catch (e: Exception) {
								EmbedBuilder().error(sc, data, Lang.INVALID_FORMAT.get(data))
							}
						} else {
							EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
						}
					}
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun clearManagers(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("clear_managers")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromAdminAtLeast(data)) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				} else {
					if (sc.arguments.isEmpty()) {
						data.managers.clear()
						EmbedBuilder().success(sc, data, Lang.CLEARED_MANAGERS.get(data))
					} else {
						val arguments = sc.arguments[0].stringValue.get().split(" ")
						if (arguments.size == 1) {
							try {
								val roleId = arguments[0].toLong()
								data.managers.removeIf { it.id == roleId }
								EmbedBuilder().success(sc, data, "${Lang.REMOVED_MANAGER.get(data)}: @$roleId")
							} catch (e: Exception) {
								EmbedBuilder().error(sc, data, Lang.INVALID_FORMAT.get(data))
							}
						} else {
							EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
						}
					}
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun clearSecretChannels(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("clear_secret_channels")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromAdminAtLeast(data)) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				} else {
					if (sc.arguments.isEmpty()) {
						data.secretChannels.clear()
						EmbedBuilder().success(sc, data, Lang.CLEARED_CHANNELS.get(data))
					} else {
						val arguments = sc.arguments[0].stringValue.get().split(" ")
						if (arguments.size == 1) {
							try {
								val channelId = arguments[0].toLong()
								data.secretChannels.removeIf { it.id == channelId }
								EmbedBuilder().success(sc, data, "${Lang.REMOVED_CHANNEL.get(data)}: @$channelId")
							} catch (e: Exception) {
								EmbedBuilder().error(sc, data, Lang.INVALID_FORMAT.get(data))
							}
						} else {
							EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
						}
					}
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun clearMessages(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("clear_messages")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromAdminAtLeast(data)) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				} else {
					val filePath = "${data.serverId}/messages.bin"
					fileDao.removeFile(filePath)
					fileDao.createFile(filePath)
					EmbedBuilder().success(sc, data, Lang.CLEARED_MESSAGES.get(data))
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun clearData(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("clear_data")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromAdminAtLeast(data)) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				} else {
					val filePath = "${data.serverId}/data.json"
					fileDao.removeFile(filePath)
					fileDao.createFile(filePath)
					EmbedBuilder().success(sc, data, Lang.CLEARED_DATA.get(data))
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun setLanguage(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("set_language")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromAdminAtLeast(data)) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				} else {
					val arguments = sc.arguments[0].stringValue.get().split(" ")
					if (arguments.size == 1) {
						try {
							val lang = arguments[0]
							if (lang != "ru" && lang != "en") {
								throw Exception()
							}
							data.lang = lang
							EmbedBuilder().success(sc, data, "${Lang.SET_LANGUAGE.get(data)}: $lang")
						} catch (e: Exception) {
							EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
						}
					} else {
						EmbedBuilder().error(sc, data, Lang.INVALID_ARG.get(data))
					}
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun setChance(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("set_chance")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromAdminAtLeast(data)) {
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

	override fun nuke(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("nuke")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromAdminAtLeast(data)) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				} else {
					val arguments = sc.arguments[0].stringValue.get().split(" ")
					if (arguments.size == 1) {
						try {
							val range = arguments[0].toInt()
							if (range !in 2..200) {
								throw Exception()
							}
							val messageIds = sc.channel.get().getMessages(range).get().map { it.id }.toLongArray()
							sc.channel.get().bulkDelete(*messageIds).get()
							EmbedBuilder().success(sc, data, Lang.NUKE.get(data))
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

	private fun InteractionCreateEvent.fromAdminAtLeast(data: ServerData): Boolean {
		val sc = interaction.asSlashCommandInteraction().get()
		val server = sc.server.get()
		val user = sc.user
		return user.isBotOwner || server.isAdmin(user) || user.getRoles(server).any { role ->
			data.managers.any { it.id == role.id }
		}
	}
}