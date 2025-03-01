package com.github.hummel.union.service.impl

import com.github.hummel.union.bean.BotData
import com.github.hummel.union.bean.ServerData
import com.github.hummel.union.service.AccessService
import org.javacord.api.interaction.SlashCommandInteraction

class AccessServiceImpl : AccessService {
	override fun fromManagerAtLeast(sc: SlashCommandInteraction, serverData: ServerData): Boolean {
		val server = sc.server.get()
		val user = sc.user
		return user.getRoles(server).any { role ->
			serverData.managers.any { it.id == role.id }
		} || user.id == BotData.ownerId.toLong() || user.isBotOwnerOrTeamMember || server.isAdmin(user)
	}

	override fun fromOwnerAtLeast(sc: SlashCommandInteraction): Boolean {
		val user = sc.user
		return user.id == BotData.ownerId.toLong() || user.isBotOwnerOrTeamMember
	}
}