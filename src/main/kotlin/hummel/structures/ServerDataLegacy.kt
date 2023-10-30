package hummel.structures

data class ServerDataLegacy(
	val serverID: String,
	val serverName: String,
	var chance: Int,
	var lang: String,
	val lastWish: ServerData.Date,
	val officers: MutableSet<RoleLegacy>,
	val generals: MutableSet<RoleLegacy>,
	val birthdays: MutableSet<BirthdayLegacy>
) {
	data class RoleLegacy(var roleID: Long, var roleName: String)

	data class BirthdayLegacy(var userID: Long, val userName: String, var date: ServerData.Date)

	fun convert(): ServerData {
		return ServerData(
			serverID,
			serverName,
			chance,
			lang,
			lastWish,
			officers.map { (roleID, _) -> ServerData.Role(roleID) }.toMutableSet(),
			generals.map { (roleID, _) -> ServerData.Role(roleID) }.toMutableSet(),
			birthdays.map { (userID, _, date) -> ServerData.Birthday(userID, date) }.toMutableSet()
		)
	}
}