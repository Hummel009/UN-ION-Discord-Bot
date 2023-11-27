package hummel.bean

data class ServerData(
	val serverID: String = "",
	val serverName: String = "",
	var chance: Int = 0,
	var lang: String = "",
	val lastWish: Date = Date(0, 0),
	val officers: MutableSet<Role> = mutableSetOf(),
	val generals: MutableSet<Role> = mutableSetOf(),
	val birthdays: MutableSet<Birthday> = mutableSetOf(),
	val channels: MutableSet<Channel> = mutableSetOf()
) {
	data class Date(var day: Int, var month: Int)

	data class Role(var roleID: Long)

	data class Channel(var channelID: Long)

	data class Birthday(var userID: Long, var date: Date)
}