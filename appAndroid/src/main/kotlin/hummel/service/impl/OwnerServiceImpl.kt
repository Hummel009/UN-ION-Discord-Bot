package hummel.service.impl

import hummel.bean.ServerData
import hummel.dao.FileDao
import hummel.dao.ZipDao
import hummel.factory.DaoFactory
import hummel.service.OwnerService
import hummel.utils.Lang
import hummel.utils.access
import hummel.utils.success
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.interaction.InteractionCreateEvent
import kotlin.system.exitProcess

class OwnerServiceImpl : OwnerService {
	private val fileDao: FileDao = DaoFactory.fileDao
	private val zipDao: ZipDao = DaoFactory.zipDao

	override fun commands(event: InteractionCreateEvent, data: ServerData, api: DiscordApi) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("commands")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromOwnerAtLeast()) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS[data])
				} else {
					val text = buildString {
						api.globalApplicationCommands.get().joinTo(this, "\r\n") { "${it.name}: ${it.id}" }
					}
					EmbedBuilder().success(sc, data, text)
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun import(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("import")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromOwnerAtLeast()) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS[data])
				} else {
					val byteArray = sc.arguments[0].attachmentValue.get().asByteArray().join()
					fileDao.writeToFile("bot.zip", byteArray)
					zipDao.unzip("bot.zip")
					fileDao.removeFile("bot.zip")
					EmbedBuilder().success(sc, data, Lang.IMPORT[data])
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun export(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("export")) {
			sc.respondLater().thenAccept {
				if (!event.fromOwnerAtLeast()) {
					val embed = EmbedBuilder().access(sc, data, Lang.NO_ACCESS[data])
					sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
				} else {
					zipDao.zip("bot.zip")
					val file = fileDao.getFile("bot.zip")
					sc.createFollowupMessageBuilder().addAttachment(file).send().get()
					fileDao.removeFile("bot.zip")
				}
			}.get()
		}
	}

	@Deprecated("Use mobile app instead")
	override fun exit(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()
		if (sc.fullCommandName.contains("exit")) {
			var exit = false

			sc.respondLater().thenAccept {
				val embed = if (!event.fromOwnerAtLeast()) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS[data])
				} else {
					exit = true
					EmbedBuilder().success(sc, data, Lang.EXIT[data])
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()

			if (exit) {
				exitProcess(0)
			}
		}
	}

	@Deprecated("Use mobile app instead")
	override fun shutdown(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()
		if (sc.fullCommandName.contains("shutdown")) {
			sc.respondLater().thenAccept {
				val embed = EmbedBuilder().access(sc, data, Lang.NO_ACCESS[data])
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	private fun InteractionCreateEvent.fromOwnerAtLeast(): Boolean {
		val sc = interaction.asSlashCommandInteraction().get()
		val user = sc.user
		return user.isBotOwnerOrTeamMember
	}
}