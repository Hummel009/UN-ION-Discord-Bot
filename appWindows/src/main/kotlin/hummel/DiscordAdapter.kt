package hummel

import hummel.controller.DiscordController
import hummel.controller.impl.DiscordControllerImpl

class DiscordAdapter {
	private val controller: DiscordController = DiscordControllerImpl

	fun launch() {
		controller.onCreate()
		controller.onStartCommand()
	}
}