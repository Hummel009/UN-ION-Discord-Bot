package hummel.utils

import hummel.bean.ServerData
import net.lingala.zip4j.ZipFile
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.interaction.SlashCommandInteraction
import java.awt.Color
import java.io.File

fun EmbedBuilder.error(sc: SlashCommandInteraction, data: ServerData, desc: String): EmbedBuilder =
	setAuthor(sc.user).setTitle(Lang.MSG_ERROR[data]).setColor(Color.RED).setDescription(desc)

fun EmbedBuilder.access(sc: SlashCommandInteraction, data: ServerData, desc: String): EmbedBuilder =
	setAuthor(sc.user).setTitle(Lang.MSG_ACCESS[data]).setColor(Color.ORANGE).setDescription(desc)

fun EmbedBuilder.success(sc: SlashCommandInteraction, data: ServerData, desc: String): EmbedBuilder =
	setAuthor(sc.user).setTitle(Lang.MSG_SUCCESS[data]).setColor(Color.GREEN).setDescription(desc)

fun ZipFile.addFolderContent(folder: File) {
	folder.listFiles()?.forEach {
		if (it.isDirectory) {
			addFolder(it)
		} else {
			addFile(it)
		}
	}
}