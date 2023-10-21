package hummel.functions

import hummel.structures.Role
import hummel.structures.ServerData
import org.javacord.api.event.interaction.InteractionCreateEvent

fun clearServerOfficers(event: InteractionCreateEvent, data: ServerData) {
	clearServerRoles(event, data, "officer")
}

fun clearServerGenerals(event: InteractionCreateEvent, data: ServerData) {
	clearServerRoles(event, data, "general")
}

fun clearServerRoles(event: InteractionCreateEvent, data: ServerData, roleName: String) {
	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains("clear_${roleName}s")) {
		if (sc.arguments.isEmpty()) {
			if (roleName == "general") {
				data.generals = HashSet()
			} else {
				data.officers = HashSet()
			}
			sc.createImmediateResponder().setContent("Manager roles cleared.").respond()
		} else {
			val arguments = sc.arguments[0].stringValue.get().split(" ")
			when (arguments.size) {
				1 -> {
					try {
						val roleID = arguments[0].toLong()
						val set = HashSet<Role>()
						val searchSet = if (roleName == "general") data.generals else data.officers
						for (role in searchSet) {
							if (roleID != role.roleID) {
								set.add(role)
							}
						}
						if (roleName == "general") {
							data.generals = set
						} else {
							data.officers = set
						}
						sc.createImmediateResponder().setContent("Removed manager role: @$roleID").respond()
					} catch (e: Exception) {
						sc.createImmediateResponder().setContent("Invalid argument format.").respond()
					}
				}

				else -> {
					sc.createImmediateResponder().setContent("Wrong command usage.").respond()
				}
			}
		}
	}
}

fun addOfficer(event: InteractionCreateEvent, data: ServerData) {
	addRole(event, data, "officer")
}

fun addGeneral(event: InteractionCreateEvent, data: ServerData) {
	addRole(event, data, "general")
}

fun addRole(event: InteractionCreateEvent, data: ServerData, roleName: String) {
	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains("add_$roleName")) {
		val arguments = sc.arguments[0].stringValue.get().split(" ")
		if (arguments.size == 1) {
			try {
				val roleID = arguments[0].toLong()
				if (roleName == "general") {
					data.generals.add(Role(roleID))
				} else {
					data.officers.add(Role(roleID))
				}
				sc.createImmediateResponder().setContent("Added manager role: @$roleID.").respond()
			} catch (e: NumberFormatException) {
				sc.createImmediateResponder().setContent("Invalid argument format").respond()
			}
		} else {
			sc.createImmediateResponder().setContent("No arguments provided.").respond()
		}
	}
}