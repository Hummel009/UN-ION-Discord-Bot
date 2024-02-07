package com.github.hummel.union.windows

import com.github.hummel.union.controller.DiscordController
import com.github.hummel.union.controller.impl.DiscordControllerImpl

class DiscordAdapter {
	private val controller: DiscordController = DiscordControllerImpl()

	fun launch() {
		controller.onCreate()
		controller.onStartCommand()
	}
}