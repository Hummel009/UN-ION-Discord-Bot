package hummel.structures

data class ServerData(
	val serverID: String,
	val serverName: String,
	var chance: Int,
	val lastWish: Date,
	val officers: MutableSet<Role>,
	val generals: MutableSet<Role>,
	val birthday: MutableSet<Birthday>
) {
	data class Date(var day: Int, var month: Int)

	data class Role(var roleID: Long)

	data class Birthday(var userID: Long, var day: Int, var month: Int)
}