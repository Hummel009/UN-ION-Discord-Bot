package hummel.structures

data class ServerData(
	var serverID: String, var serverName: String, var chance: Int, var birthday: MutableSet<Birthday> = HashSet()
)