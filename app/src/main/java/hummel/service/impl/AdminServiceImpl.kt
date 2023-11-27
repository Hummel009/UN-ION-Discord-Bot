package hummel.service.impl

import hummel.bean.ServerData
import hummel.factory.DaoFactory
import hummel.service.AdminService
import hummel.utils.Lang
import hummel.utils.access
import hummel.utils.error
import hummel.utils.success
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.interaction.InteractionCreateEvent

class AdminServiceImpl : AdminService {
	private val dao = DaoFactory.dao

	override fun clearServerMessages(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("clear_messages")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromAdminAtLeast(data)) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				} else {
					val filePath = "${data.serverID}/messages.bin"
					dao.removeFile(filePath)
					dao.createFile(filePath)
					EmbedBuilder().success(sc, data, Lang.CLEARED_MESSAGES.get(data))
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun clearServerBirthdays(event: InteractionCreateEvent, data: ServerData) {
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
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun clearServerGenerals(event: InteractionCreateEvent, data: ServerData) {
		clearServerManagers(event, data, "general")
	}

	override fun clearServerOfficers(event: InteractionCreateEvent, data: ServerData) {
		clearServerManagers(event, data, "officer")
	}

	override fun clearSecretChannels(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("clear_secret_channels")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromAdminAtLeast(data)) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				} else {
					if (sc.arguments.isEmpty()) {
						data.channels.clear()
						EmbedBuilder().success(sc, data, Lang.CLEARED_CHANNELS.get(data))
					} else {
						val arguments = sc.arguments[0].stringValue.get().split(" ")
						if (arguments.size == 1) {
							try {
								val channelID = arguments[0].toLong()
								data.channels.removeIf { it.channelID == channelID }
								EmbedBuilder().success(sc, data, "${Lang.REMOVED_CHANNEL.get(data)}: @$channelID")
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

	override fun addOfficer(event: InteractionCreateEvent, data: ServerData) {
		addManager(event, data, "officer")
	}

	override fun addGeneral(event: InteractionCreateEvent, data: ServerData) {
		addManager(event, data, "general")
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
							val channelID = arguments[0].toLong()
							if (!sc.server.get().getChannelById(channelID).isPresent) {
								throw Exception()
							}
							data.channels.add(ServerData.Channel(channelID))
							EmbedBuilder().success(sc, data, "${Lang.ADDED_CHANNEL.get(data)}: @$channelID.")
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

	private fun clearServerManagers(event: InteractionCreateEvent, data: ServerData, roleName: String) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("clear_${roleName}s")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromAdminAtLeast(data)) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				} else {
					if (sc.arguments.isEmpty()) {
						(if (roleName == "general") data.generals else data.officers).clear()
						EmbedBuilder().success(sc, data, Lang.CLEARED_MANAGERS.get(data))
					} else {
						val arguments = sc.arguments[0].stringValue.get().split(" ")
						if (arguments.size == 1) {
							try {
								val roleID = arguments[0].toLong()
								(if (roleName == "general") data.generals else data.officers).removeIf { it.roleID == roleID }
								EmbedBuilder().success(sc, data, "${Lang.REMOVED_MANAGER.get(data)}: @$roleID")
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

	private fun addManager(event: InteractionCreateEvent, data: ServerData, roleName: String) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("add_$roleName")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromAdminAtLeast(data)) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				} else {
					val arguments = sc.arguments[0].stringValue.get().split(" ")
					if (arguments.size == 1) {
						try {
							val roleID = arguments[0].toLong()
							if (!sc.server.get().getRoleById(roleID).isPresent) {
								throw Exception()
							}
							(if (roleName == "general") data.generals else data.officers).add(ServerData.Role(roleID))
							EmbedBuilder().success(sc, data, "${Lang.ADDED_MANAGER.get(data)}: @$roleID.")
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
			data.generals.any { it.roleID == role.id }
		}
	}
}