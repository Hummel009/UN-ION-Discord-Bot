package hummel.service

import hummel.bean.ServerData
import org.javacord.api.interaction.SlashCommandInteraction

interface AccessService {
	fun fromAdminAtLeast(sc: SlashCommandInteraction, serverData: ServerData): Boolean
	fun fromOwnerAtLeast(sc: SlashCommandInteraction): Boolean
}