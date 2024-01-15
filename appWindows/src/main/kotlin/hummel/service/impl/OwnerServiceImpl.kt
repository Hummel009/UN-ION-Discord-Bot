package hummel.service.impl

import hummel.dao.FileDao
import hummel.dao.ZipDao
import hummel.factory.DaoFactory
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
	private val fileDao: FileDao = DaoFactory.fileDao
	private val zipDao: ZipDao = DaoFactory.zipDao

	override fun import(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()

		if (sc.fullCommandName.contains("import")) {
			sc.respondLater().thenAccept {
				val serverData = dataService.loadServerData(sc.server.get())
				val embed = if (!accessService.fromOwnerAtLeast(sc)) {
					EmbedBuilder().access(sc, serverData, Lang.NO_ACCESS[serverData])
				} else {
					val byteArray = sc.arguments[0].attachmentValue.get().asByteArray().join()
					fileDao.createFile("bot.zip")
					fileDao.writeToFile("bot.zip", byteArray)
					zipDao.unzipFile("bot.zip", "")
					fileDao.removeFile("bot.zip")
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
				if (!accessService.fromOwnerAtLeast(sc)) {
					val serverData = dataService.loadServerData(sc.server.get())
					val embed = EmbedBuilder().access(sc, serverData, Lang.NO_ACCESS[serverData])
					sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
				} else {
					zipDao.zipFolder("", "bot.zip")
					val file = fileDao.getFile("bot.zip")
					sc.createFollowupMessageBuilder().addAttachment(file).send().get()
					fileDao.removeFile("bot.zip")
				}
			}.get()
		}
	}

	override fun exit(event: InteractionCreateEvent) {
		val sc = event.slashCommandInteraction.get()
		if (sc.fullCommandName.contains("exit")) {
			var exit = false
			val serverData = dataService.loadServerData(sc.server.get())

			sc.respondLater().thenAccept {
				val embed = if (!accessService.fromOwnerAtLeast(sc)) {
					EmbedBuilder().access(sc, serverData, Lang.NO_ACCESS[serverData])
				} else {
					exit = true
					EmbedBuilder().success(sc, serverData, Lang.EXIT[serverData])
				}
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}.get()

			if (exit) {
				exitProcess(0)
			}
		}
	}
}