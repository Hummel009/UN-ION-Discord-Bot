package hummel.service.impl

import hummel.bean.BotData
import hummel.bean.ServerData
import hummel.service.AccessService
import org.javacord.api.interaction.SlashCommandInteraction

class AccessServiceImpl(private val botData: BotData) : AccessService {
	override fun fromAdminAtLeast(sc: SlashCommandInteraction, serverData: ServerData): Boolean {
		val server = sc.server.get()
		val user = sc.user
		return user.getRoles(server).any { role ->
			serverData.managers.any { it.id == role.id }
		} || user.id == botData.ownerId || user.isBotOwnerOrTeamMember || server.isAdmin(user)
	}

	override fun fromOwnerAtLeast(sc: SlashCommandInteraction): Boolean {
		val user = sc.user
		return user.id == botData.ownerId || user.isBotOwnerOrTeamMember
	}
}