package hummel.functions

import hummel.structures.ServerData
import hummel.utils.*
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.interaction.InteractionCreateEvent

fun clearServerOfficers(event: InteractionCreateEvent, data: ServerData) {
	clearServerManagers(event, data, "officer")
}

fun clearServerGenerals(event: InteractionCreateEvent, data: ServerData) {
	clearServerManagers(event, data, "general")
}

fun clearServerManagers(event: InteractionCreateEvent, data: ServerData, roleName: String) {
	val sc = event.slashCommandInteraction.get()

	if (sc.fullCommandName.contains("clear_${roleName}s")) {
		sc.respondLater().thenAccept {
			val embed = if (!event.isGeneralMessage(data)) {
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
			sc.createFollowupMessageBuilder().addEmbed(embed).send()
		}
	}
}

fun addOfficer(event: InteractionCreateEvent, data: ServerData) {
	addManager(event, data, "officer")
}

fun addGeneral(event: InteractionCreateEvent, data: ServerData) {
	addManager(event, data, "general")
}

fun addManager(event: InteractionCreateEvent, data: ServerData, roleName: String) {
	val sc = event.slashCommandInteraction.get()

	if (sc.fullCommandName.contains("add_$roleName")) {
		sc.respondLater().thenAccept {
			val embed = if (!event.isGeneralMessage(data)) {
				EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
			} else {
				val arguments = sc.arguments[0].stringValue.get().split(" ")
				if (arguments.size == 1) {
					try {
						val roleID = arguments[0].toLong()
						if (sc.server.get().getRoleById(roleID) == null) {
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
			sc.createFollowupMessageBuilder().addEmbed(embed).send()
		}
	}
}