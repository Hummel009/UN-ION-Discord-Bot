package hummel.functions

import hummel.structures.ServerData
import hummel.utils.Lang
import hummel.utils.access
import hummel.utils.isBotOwner
import hummel.utils.success
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.interaction.InteractionCreateEvent
import kotlin.system.exitProcess

fun exit(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains("exit")) {
		var exit = false

		sc.respondLater().thenAccept {
			val embed = if (!event.isBotOwner()) {
				EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
			} else {
				exit = true
				EmbedBuilder().success(sc, data, Lang.EXIT.get(data))
			}
			sc.createFollowupMessageBuilder().addEmbed(embed).send()
		}.get()

		if (exit) {
			exitProcess(0)
		}
	}
}

fun shutdown(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains("shutdown")) {
		var shutdown = false

		sc.respondLater().thenAccept {
			val embed = if (!event.isBotOwner()) {
				EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
			} else {
				shutdown = true
				EmbedBuilder().success(sc, data, Lang.SHUTDOWN.get(data))
			}
			sc.createFollowupMessageBuilder().addEmbed(embed).send()
		}.get()

		if (shutdown) {
			Runtime.getRuntime().exec("rundll32.exe powrprof.dll,SetSuspendState 0,1,0")
		}
	}
}