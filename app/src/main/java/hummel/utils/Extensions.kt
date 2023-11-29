package hummel.utils

import hummel.bean.ServerData
import net.lingala.zip4j.ZipFile
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.interaction.SlashCommandInteraction
import java.io.File

fun EmbedBuilder.error(sc: SlashCommandInteraction, data: ServerData, desc: String): EmbedBuilder {
	return setAuthor(sc.user).setTitle(Lang.MSG_ERROR.get(data)).setDescription(desc)
}

fun EmbedBuilder.access(sc: SlashCommandInteraction, data: ServerData, desc: String): EmbedBuilder {
	return setAuthor(sc.user).setTitle(Lang.MSG_ACCESS.get(data)).setDescription(desc)
}

fun EmbedBuilder.success(sc: SlashCommandInteraction, data: ServerData, desc: String): EmbedBuilder {
	return setAuthor(sc.user).setTitle(Lang.MSG_SUCCESS.get(data)).setDescription(desc)
}

fun ZipFile.addFolderContent(folder: File) {
	val files = folder.listFiles() ?: return

	for (file in files) {
		if (file.isDirectory) {
			addFolder(file)
		} else {
			addFile(file)
		}
	}
}