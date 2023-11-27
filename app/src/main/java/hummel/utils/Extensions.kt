package hummel.utils

import hummel.bean.ServerData
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.interaction.SlashCommandInteraction

fun EmbedBuilder.error(sc: SlashCommandInteraction, data: ServerData, desc: String): EmbedBuilder {
	return setAuthor(sc.user).setTitle(Lang.MSG_ERROR.get(data)).setDescription(desc)
}

fun EmbedBuilder.access(sc: SlashCommandInteraction, data: ServerData, desc: String): EmbedBuilder {
	return setAuthor(sc.user).setTitle(Lang.MSG_ACCESS.get(data)).setDescription(desc)
}

fun EmbedBuilder.success(sc: SlashCommandInteraction, data: ServerData, desc: String): EmbedBuilder {
	return setAuthor(sc.user).setTitle(Lang.MSG_SUCCESS.get(data)).setDescription(desc)
}