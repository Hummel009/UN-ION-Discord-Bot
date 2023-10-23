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
		val server = api.getServerById(this.serverID).get()
		val newData = ServerData(
			this.serverID,
			this.serverName,
			this.chance,
			this.lang,
			this.lastWish,
			this.officers.map { (roleID) ->
				val name = try {
					server.getRoleById(roleID).get().name
				} catch (e: Exception) {
					"This role is from another server and should be removed."
				}
				ServerData.Role(roleID, name)
			}.toMutableSet(),
			this.generals.map { (roleID) ->
				val name = try {
					server.getRoleById(roleID).get().name
				} catch (e: Exception) {
					"This role is from another server and should be removed."
				}
				ServerData.Role(roleID, name)
			}.toMutableSet(),
			this.birthday.map { (userId, day, month) ->
				val name = try {
					server.getMemberById(userId).get().name
				} catch (e: Exception) {
					"This person is from another server and should be removed."
				}
				ServerData.Birthday(userId, name, ServerData.Date(day, month))
			}.toMutableSet()
		)
		return newData
	}
}