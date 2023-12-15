package hummel.bean

data class ServerData(
	val dataVer: Int,
	val serverId: String,
	val serverName: String,
	var chance: Int,
	var lang: String,
	val lastWish: Date,
	val secretChannels: MutableSet<Channel>,
	val managers: MutableSet<Role>,
	val birthdays: MutableSet<Birthday>
) {
	data class Date(var day: Int, var month: Int)

	data class Role(var id: Long)

	data class Channel(var id: Long)

	data class Birthday(var id: Long, var date: Date)
}