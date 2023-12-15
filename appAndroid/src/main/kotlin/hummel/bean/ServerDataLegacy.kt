package hummel.bean

import hummel.utils.version

data class ServerDataLegacy(
	val serverID: String,
	val serverName: String,
	var chance: Int,
	var lang: String,
	val lastWish: ServerData.Date,
	val officers: MutableSet<Role>,
	val generals: MutableSet<Role>,
	val birthdays: MutableSet<Birthday>
) {
	data class Role(var roleID: Long)

	data class Birthday(var userID: Long, var date: ServerData.Date)

	fun convert(): ServerData {
		return ServerData(
			dataVer = version,
			serverId = serverID,
			serverName = serverName,
			chance = chance,
			lang = lang,
			lastWish = lastWish,
			secretChannels = mutableSetOf(),
			managers = generals.map { (id) -> ServerData.Role(id) }.toMutableSet(),
			birthdays = birthdays.map { (id, date) -> ServerData.Birthday(id, date) }.toMutableSet()
		)
	}
}