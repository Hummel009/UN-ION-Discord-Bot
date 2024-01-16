package hummel

import hummel.controller.DiscordController
import hummel.controller.impl.DiscordControllerImpl

class DiscordAdapter {
	private val controller: DiscordController = DiscordControllerImpl(Storage.token, Storage.ownerId.toLong(), Storage.context)

	fun launch() {
		controller.onCreate()
		controller.onStartCommand()
	}
}