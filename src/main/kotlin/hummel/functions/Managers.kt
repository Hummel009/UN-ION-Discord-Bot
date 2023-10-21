package hummel.functions

import hummel.structures.ServerData
import hummel.utils.defErrEmbed
import hummel.utils.isGeneralMessage
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
		if (!event.isGeneralMessage(data)) {
			sc.respondLater().thenAccept {
				val embed = EmbedBuilder().defErrEmbed(sc, "You do not have permission to use this command.")
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}
		} else {
			if (sc.arguments.isEmpty()) {
				(if (roleName == "general") data.generals else data.officers).clear()
				sc.createImmediateResponder().setContent("Manager roles cleared.").respond()
			} else {
				val arguments = sc.arguments[0].stringValue.get().split(" ")
				if (arguments.size == 1) {
					try {
						val roleID = arguments[0].toLong()
						(if (roleName == "general") data.generals else data.officers).removeIf { it.roleID == roleID }
						sc.createImmediateResponder().setContent("Removed manager role: @$roleID").respond()
					} catch (e: Exception) {
						sc.respondLater().thenAccept {
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

fun addOfficer(event: InteractionCreateEvent, data: ServerData) {
	addManager(event, data, "officer")
}

fun addGeneral(event: InteractionCreateEvent, data: ServerData) {
	addManager(event, data, "general")
}

fun addManager(event: InteractionCreateEvent, data: ServerData, roleName: String) {
	val sc = event.slashCommandInteraction.get()

	if (sc.fullCommandName.contains("add_$roleName")) {
		if (!event.isGeneralMessage(data)) {
			sc.respondLater().thenAccept {
				val embed = EmbedBuilder().defErrEmbed(sc, "You do not have permission to use this command.")
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}
		} else {
			val arguments = sc.arguments[0].stringValue.get().split(" ")
			if (arguments.size == 1) {
				try {
					val roleID = arguments[0].toLong()
					(if (roleName == "general") data.generals else data.officers).add(ServerData.Role(roleID))
					sc.createImmediateResponder().setContent("Added manager role: @$roleID.").respond()
				} catch (e: Exception) {
					sc.respondLater().thenAccept {
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