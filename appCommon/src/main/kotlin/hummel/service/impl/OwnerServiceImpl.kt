package hummel.service.impl

import hummel.factory.ServiceFactory
import hummel.service.AccessService
import hummel.service.DataService
import hummel.service.OwnerService
import hummel.utils.Lang
import hummel.utils.access
import hummel.utils.success
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.interaction.InteractionCreateEvent
import kotlin.system.exitProcess

class OwnerServiceImpl : OwnerService {
	private val dataService: DataService = ServiceFactory.dataService
	private val accessService: AccessService = ServiceFactory.accessService

	override fun import(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("import")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val embed = if (!accessService.fromOwnerAtLeast(sc)) {
					EmbedBuilder().access(sc, serverData, Lang.NO_ACCESS[serverData])
				} else {
					val byteArray = sc.arguments[0].attachmentValue.get().asByteArray().join()
					dataService.importBotData(byteArray)
					EmbedBuilder().success(sc, serverData, Lang.IMPORT[serverData])
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()
		}
	}

	override fun export(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("export")) {
			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				if (!accessService.fromOwnerAtLeast(sc)) {
					val embed = EmbedBuilder().access(sc, serverData, Lang.NO_ACCESS[serverData])
					sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
				} else {
					dataService.exportBotData(sc)
				}
			}.get()
		}
	}

	override fun exit(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()
		if (sc.fullCommandName.contains("exit")) {
			var exit = false

			sc.respondLater().thenAccept {
				val server = sc.server.get()
				val serverData = dataService.loadServerData(server)

				val embed = if (!accessService.fromOwnerAtLeast(sc)) {
					EmbedBuilder().access(sc, serverData, Lang.NO_ACCESS[serverData])
				} else {
					exit = true
					EmbedBuilder().success(sc, serverData, Lang.EXIT[serverData])
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()

			if (exit) {
				exitProcess(4)
			}
		}
	}
}