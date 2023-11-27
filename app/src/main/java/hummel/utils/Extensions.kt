package hummel.utils

import hummel.bean.ServerData
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.interaction.SlashCommandInteraction
import java.awt.Color

fun EmbedBuilder.error(sc: SlashCommandInteraction, data: ServerData, desc: String): EmbedBuilder {
	return setAuthor(sc.user).setTitle(Lang.MSG_ERROR.get(data)).setColor(Color.RED).setDescription(desc)
}

fun EmbedBuilder.access(sc: SlashCommandInteraction, data: ServerData, desc: String): EmbedBuilder {
	return setAuthor(sc.user).setTitle(Lang.MSG_ACCESS.get(data)).setColor(Color.ORANGE).setDescription(desc)
}

fun EmbedBuilder.success(sc: SlashCommandInteraction, data: ServerData, desc: String): EmbedBuilder {
	return setAuthor(sc.user).setTitle(Lang.MSG_SUCCESS.get(data)).setColor(Color.GREEN).setDescription(desc)
}