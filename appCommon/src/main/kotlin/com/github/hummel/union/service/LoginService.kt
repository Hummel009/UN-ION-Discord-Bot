package com.github.hummel.union.service

import com.github.hummel.union.controller.impl.DiscordControllerImpl

interface LoginService {
	fun loginBot(impl: DiscordControllerImpl)
	fun deleteCommands(impl: DiscordControllerImpl)
	fun registerCommands(impl: DiscordControllerImpl)
}