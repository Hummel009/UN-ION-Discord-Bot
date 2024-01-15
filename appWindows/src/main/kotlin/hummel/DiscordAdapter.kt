package hummel

import hummel.controller.DiscordController
import hummel.controller.impl.DiscordControllerImpl

class DiscordAdapter(token: String, ownerId: String) {
	private val controller: DiscordController = DiscordControllerImpl(token, ownerId.toLong(), "files")

	fun launch() {
		controller.onCreate()
		controller.onStartCommand()
	}
}