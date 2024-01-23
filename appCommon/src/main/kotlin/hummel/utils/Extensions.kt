package hummel.utils

import hummel.bean.ServerData
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.interaction.SlashCommandInteraction

fun EmbedBuilder.error(sc: SlashCommandInteraction, serverData: ServerData, desc: String): EmbedBuilder =
	setAuthor(sc.user).setTitle(Lang.MSG_ERROR[serverData]).setDescription(desc)

fun EmbedBuilder.access(sc: SlashCommandInteraction, serverData: ServerData, desc: String): EmbedBuilder =
	setAuthor(sc.user).setTitle(Lang.MSG_ACCESS[serverData]).setDescription(desc)

fun EmbedBuilder.success(sc: SlashCommandInteraction, serverData: ServerData, desc: String): EmbedBuilder =
	setAuthor(sc.user).setTitle(Lang.MSG_SUCCESS[serverData]).setDescription(desc)