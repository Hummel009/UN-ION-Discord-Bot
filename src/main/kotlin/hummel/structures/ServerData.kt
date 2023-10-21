package hummel.structures

data class ServerData(
	var serverID: String,
	var serverName: String,
	var chance: Int,
	var birthday: MutableSet<Birthday>,
	var lastWish: Date,
	var officers: MutableSet<Role>,
	var generals: MutableSet<Role>
)