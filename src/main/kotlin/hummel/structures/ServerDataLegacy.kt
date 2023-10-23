package hummel.structures

import org.javacord.api.DiscordApi

data class ServerDataLegacy(
	val serverID: String,
	val serverName: String,
	var chance: Int,
	val lastWish: ServerData.Date,
	var lang: String,
	val officers: MutableSet<RoleLegacy>,
	val generals: MutableSet<RoleLegacy>,
	val birthday: MutableSet<BirthdayLegacy>
) {
	data class RoleLegacy(var roleID: Long)

	data class BirthdayLegacy(var userID: Long, var day: Int, var month: Int)

	fun convert(api: DiscordApi): ServerData {
		val newData = ServerData(
			this.serverID,
			this.serverName,
			this.chance,
			this.lang,
			this.lastWish,
			HashSet(),
			HashSet(),
			HashSet()
		)
		val server = api.getServerById(newData.serverID).get()
		newData.officers.addAll(this.officers.map { (roleID) ->
			ServerData.Role(
				roleID, server.getRoleById(roleID).get().name
			)
		})
		newData.generals.addAll(this.generals.map { (roleID) ->
			ServerData.Role(
				roleID, server.getRoleById(roleID).get().name
			)
		})
		newData.birthdays.addAll(this.birthday.map { (userId, day, month) ->
			ServerData.Birthday(
				userId, server.getMemberById(userId).get().name, ServerData.Date(day, month)
			)
		})
		return newData
	}
}