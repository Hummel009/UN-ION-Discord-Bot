package hummel.functions

import hummel.structures.ServerData
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
					sc.createImmediateResponder().setContent("Invalid argument format.").respond()
				}
			} else {
				sc.createImmediateResponder().setContent("Invalid arguments provided.").respond()
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
		val arguments = sc.arguments[0].stringValue.get().split(" ")
		if (arguments.size == 1) {
			try {
				val roleID = arguments[0].toLong()
				(if (roleName == "general") data.generals else data.officers).add(ServerData.Role(roleID))
				sc.createImmediateResponder().setContent("Added manager role: @$roleID.").respond()
			} catch (e: Exception) {
				sc.createImmediateResponder().setContent("Invalid argument format").respond()
			}
		} else {
			sc.createImmediateResponder().setContent("Invalid arguments provided.").respond()
		}
	}
}