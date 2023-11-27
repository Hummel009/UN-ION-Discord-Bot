package hummel.service.impl

import hummel.bean.ServerData
import hummel.factory.DaoFactory
import hummel.service.OwnerService
import hummel.utils.Lang
import hummel.utils.access
import hummel.utils.success
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.interaction.InteractionCreateEvent
import kotlin.system.exitProcess

class OwnerServiceImpl : OwnerService {
	private val dao = DaoFactory.dao

	override fun exit(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()
		if (sc.fullCommandName.contains("exit")) {
			var exit = false

			sc.respondLater().thenAccept {
				val embed = if (!event.fromOwnerAtLeast()) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				} else {
					exit = true
					EmbedBuilder().success(sc, data, Lang.EXIT.get(data))
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()

			if (exit) {
				exitProcess(0)
			}
		}
	}

	override fun shutdown(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()
		if (sc.fullCommandName.contains("shutdown")) {
			var shutdown = false

			sc.respondLater().thenAccept {
				val embed = if (!event.fromOwnerAtLeast()) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				} else {
					shutdown = true
					EmbedBuilder().success(sc, data, Lang.SHUTDOWN.get(data))
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()

			if (shutdown) {
				Runtime.getRuntime().exec("rundll32.exe powrprof.dll,SetSuspendState 0,1,0")
			}
		}
	}

	override fun import(event: InteractionCreateEvent, data: ServerData) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("import")) {
			sc.respondLater().thenAccept {
				val embed = if (!event.fromOwnerAtLeast()) {
					EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
				} else {
					val byteArray = sc.arguments[0].attachmentValue.get().asByteArray().join()
					dao.writeToFile("bot.zip", byteArray)
					dao.unzipFile("bot.zip")
					dao.removeFile("bot.zip")
					EmbedBuilder().success(sc, data, Lang.IMPORT.get(data))
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	private fun InteractionCreateEvent.fromOwnerAtLeast(): Boolean {
		val sc = interaction.asSlashCommandInteraction().get()
		val user = sc.user
		return user.isBotOwner
	}
}