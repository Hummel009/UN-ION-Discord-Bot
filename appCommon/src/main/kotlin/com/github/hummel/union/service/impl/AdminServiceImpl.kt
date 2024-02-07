package com.github.hummel.union.service.impl

import com.github.hummel.union.bean.ServerData
import com.github.hummel.union.factory.ServiceFactory
import com.github.hummel.union.service.AccessService
import com.github.hummel.union.service.AdminService
import com.github.hummel.union.service.DataService
import com.github.hummel.union.utils.Lang
import com.github.hummel.union.utils.access
import com.github.hummel.union.utils.error
import com.github.hummel.union.utils.success
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.interaction.InteractionCreateEvent

class AdminServiceImpl : AdminService {
	private val dataService: DataService = ServiceFactory.dataService
	private val accessService: AccessService = ServiceFactory.accessService

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

	override fun addBirthday(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("add_birthday")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val embed = if (!accessService.fromAdminAtLeast(sc, serverData)) {
					EmbedBuilder().access(sc, serverData, Lang.NO_ACCESS[serverData])
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
							serverData.birthdays.add(ServerData.Birthday(userId, ServerData.Date(day, month)))
							EmbedBuilder().success(sc, serverData, "${Lang.ADDED_BIRTHDAY[serverData]}: <@$userId>.")
						} catch (e: Exception) {
							EmbedBuilder().error(sc, serverData, Lang.INVALID_FORMAT[serverData])
						}
					} else {
						EmbedBuilder().error(sc, serverData, Lang.INVALID_ARG[serverData])
					}
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()

				dataService.saveServerData(server, serverData)
			}.get()
		}
	}

	override fun addManager(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("add_manager")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val embed = if (!accessService.fromAdminAtLeast(sc, serverData)) {
					EmbedBuilder().access(sc, serverData, Lang.NO_ACCESS[serverData])
				} else {
					val arguments = sc.arguments[0].stringValue.get().split(" ")
					if (arguments.size == 1) {
						try {
							val roleId = arguments[0].toLong()
							if (!sc.server.get().getRoleById(roleId).isPresent) {
								throw Exception()
							}
							serverData.managers.add(ServerData.Role(roleId))
							EmbedBuilder().success(sc, serverData, "${Lang.ADDED_MANAGER[serverData]}: <@$roleId>.")
						} catch (e: Exception) {
							EmbedBuilder().error(sc, serverData, Lang.INVALID_FORMAT[serverData])
						}
					} else {
						EmbedBuilder().error(sc, serverData, Lang.INVALID_ARG[serverData])
					}
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()

				dataService.saveServerData(server, serverData)
			}.get()
		}
	}

	override fun addSecretChannel(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("add_secret_channel")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val embed = if (!accessService.fromAdminAtLeast(sc, serverData)) {
					EmbedBuilder().access(sc, serverData, Lang.NO_ACCESS[serverData])
				} else {
					val arguments = sc.arguments[0].stringValue.get().split(" ")
					if (arguments.size == 1) {
						try {
							val channelId = arguments[0].toLong()
							if (!sc.server.get().getChannelById(channelId).isPresent) {
								throw Exception()
							}
							serverData.secretChannels.add(ServerData.Channel(channelId))
							EmbedBuilder().success(sc, serverData, "${Lang.ADDED_CHANNEL[serverData]}: <@$channelId>.")
						} catch (e: Exception) {
							EmbedBuilder().error(sc, serverData, Lang.INVALID_FORMAT[serverData])
						}
					} else {
						EmbedBuilder().error(sc, serverData, Lang.INVALID_ARG[serverData])
					}
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()

				dataService.saveServerData(server, serverData)
			}.get()
		}
	}

	override fun clearBirthdays(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("clear_birthdays")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val embed = if (!accessService.fromAdminAtLeast(sc, serverData)) {
					EmbedBuilder().access(sc, serverData, Lang.NO_ACCESS[serverData])
				} else {
					if (sc.arguments.isEmpty()) {
						serverData.birthdays.clear()
						EmbedBuilder().success(sc, serverData, Lang.CLEARED_BIRTHDAYS[serverData])
					} else {
						val arguments = sc.arguments[0].stringValue.get().split(" ")
						if (arguments.size == 1) {
							try {
								val userId = arguments[0].toLong()
								serverData.birthdays.removeIf { it.id == userId }
								EmbedBuilder().success(
									sc, serverData, "${Lang.REMOVED_BIRTHDAY[serverData]}: <@$userId>"
								)
							} catch (e: Exception) {
								EmbedBuilder().error(sc, serverData, Lang.INVALID_FORMAT[serverData])
							}
						} else {
							EmbedBuilder().error(sc, serverData, Lang.INVALID_ARG[serverData])
						}
					}
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()

				dataService.saveServerData(server, serverData)
			}.get()
		}
	}

	override fun clearManagers(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("clear_managers")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val embed = if (!accessService.fromAdminAtLeast(sc, serverData)) {
					EmbedBuilder().access(sc, serverData, Lang.NO_ACCESS[serverData])
				} else {
					if (sc.arguments.isEmpty()) {
						serverData.managers.clear()
						EmbedBuilder().success(sc, serverData, Lang.CLEARED_MANAGERS[serverData])
					} else {
						val arguments = sc.arguments[0].stringValue.get().split(" ")
						if (arguments.size == 1) {
							try {
								val roleId = arguments[0].toLong()
								serverData.managers.removeIf { it.id == roleId }
								EmbedBuilder().success(
									sc, serverData, "${Lang.REMOVED_MANAGER[serverData]}: <@$roleId>"
								)
							} catch (e: Exception) {
								EmbedBuilder().error(sc, serverData, Lang.INVALID_FORMAT[serverData])
							}
						} else {
							EmbedBuilder().error(sc, serverData, Lang.INVALID_ARG[serverData])
						}
					}
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()

				dataService.saveServerData(server, serverData)
			}.get()
		}
	}

	override fun clearSecretChannels(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("clear_secret_channels")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val embed = if (!accessService.fromAdminAtLeast(sc, serverData)) {
					EmbedBuilder().access(sc, serverData, Lang.NO_ACCESS[serverData])
				} else {
					if (sc.arguments.isEmpty()) {
						serverData.secretChannels.clear()
						EmbedBuilder().success(sc, serverData, Lang.CLEARED_CHANNELS[serverData])
					} else {
						val arguments = sc.arguments[0].stringValue.get().split(" ")
						if (arguments.size == 1) {
							try {
								val channelId = arguments[0].toLong()
								serverData.secretChannels.removeIf { it.id == channelId }
								EmbedBuilder().success(
									sc, serverData, "${Lang.REMOVED_CHANNEL[serverData]}: <@$channelId>"
								)
							} catch (e: Exception) {
								EmbedBuilder().error(sc, serverData, Lang.INVALID_FORMAT[serverData])
							}
						} else {
							EmbedBuilder().error(sc, serverData, Lang.INVALID_ARG[serverData])
						}
					}
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()

				dataService.saveServerData(server, serverData)
			}.get()
		}
	}

	override fun clearMessages(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("clear_messages")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val embed = if (!accessService.fromAdminAtLeast(sc, serverData)) {
					EmbedBuilder().access(sc, serverData, Lang.NO_ACCESS[serverData])
				} else {
					dataService.wipeServerMessages(server)
					EmbedBuilder().success(sc, serverData, Lang.CLEARED_MESSAGES[serverData])
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun clearData(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("clear_data")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val embed = if (!accessService.fromAdminAtLeast(sc, serverData)) {
					EmbedBuilder().access(sc, serverData, Lang.NO_ACCESS[serverData])
				} else {
					dataService.wipeServerData(server)
					EmbedBuilder().success(sc, serverData, Lang.CLEARED_DATA[serverData])
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun setLanguage(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("set_language")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val embed = if (!accessService.fromAdminAtLeast(sc, serverData)) {
					EmbedBuilder().access(sc, serverData, Lang.NO_ACCESS[serverData])
				} else {
					val arguments = sc.arguments[0].stringValue.get().split(" ")
					if (arguments.size == 1) {
						try {
							val lang = arguments[0]
							if (lang != "ru" && lang != "en") {
								throw Exception()
							}
							serverData.lang = lang
							EmbedBuilder().success(sc, serverData, "${Lang.SET_LANGUAGE[serverData]}: $lang")
						} catch (e: Exception) {
							EmbedBuilder().error(sc, serverData, Lang.INVALID_ARG[serverData])
						}
					} else {
						EmbedBuilder().error(sc, serverData, Lang.INVALID_ARG[serverData])
					}
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()

				dataService.saveServerData(server, serverData)
			}.get()
		}
	}

	override fun setChance(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("set_chance")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val embed = if (!accessService.fromAdminAtLeast(sc, serverData)) {
					EmbedBuilder().access(sc, serverData, Lang.NO_ACCESS[serverData])
				} else {
					val arguments = sc.arguments[0].stringValue.get().split(" ")
					if (arguments.size == 1) {
						try {
							val chance = arguments[0].toInt()
							if (chance < 1) {
								throw Exception()
							}
							serverData.chance = chance
							EmbedBuilder().success(sc, serverData, "${Lang.SET_CHANCE[serverData]}: $chance.")
						} catch (e: Exception) {
							EmbedBuilder().error(sc, serverData, Lang.INVALID_FORMAT[serverData])
						}
					} else {
						EmbedBuilder().error(sc, serverData, Lang.INVALID_ARG[serverData])
					}
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()

				dataService.saveServerData(server, serverData)
			}.get()
		}
	}

	override fun nuke(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("nuke")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val embed = if (!accessService.fromAdminAtLeast(sc, serverData)) {
					EmbedBuilder().access(sc, serverData, Lang.NO_ACCESS[serverData])
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
							EmbedBuilder().success(sc, serverData, Lang.NUKE[serverData])
						} catch (e: Exception) {
							EmbedBuilder().error(sc, serverData, Lang.INVALID_FORMAT[serverData])
						}
					} else {
						EmbedBuilder().error(sc, serverData, Lang.INVALID_ARG[serverData])
					}
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}
}