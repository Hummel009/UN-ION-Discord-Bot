package hummel.structures

data class ServerData(
	val serverID: String,
	val serverName: String,
	var chance: Int,
	var lang: String,
	val lastWish: Date,
	val officers: MutableSet<Role>,
	val generals: MutableSet<Role>,
	val birthdays: MutableSet<Birthday>
) {
	data class Date(var day: Int, var month: Int)

	data class Role(var roleID: Long)

	data class Birthday(var userID: Long, var date: Date)
}