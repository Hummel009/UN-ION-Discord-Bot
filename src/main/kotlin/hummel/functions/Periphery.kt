package hummel.functions

import hummel.structures.ServerData
import hummel.utils.Lang
import hummel.utils.access
import hummel.utils.isBotOwner
import hummel.utils.success
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.interaction.InteractionCreateEvent
import kotlin.concurrent.thread
import kotlin.system.exitProcess

fun exit(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains("exit")) {
		if (!event.isBotOwner()) {
			val embed = EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
			sc.respondLater().thenAccept {
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}
		} else {
			val embed = EmbedBuilder().success(sc, data, Lang.EXIT.get(data))
			sc.respondLater().thenAccept {
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}
			thread {
				Thread.sleep(10000)
				exitProcess(0)
			}
		}
	}
}

fun shutdown(event: InteractionCreateEvent, data: ServerData) {
	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains("shutdown")) {
		if (!event.isBotOwner()) {
			val embed = EmbedBuilder().access(sc, data, Lang.NO_ACCESS.get(data))
			sc.respondLater().thenAccept {
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}
		} else {
			val embed = EmbedBuilder().success(sc, data, Lang.SHUTDOWN.get(data))
			sc.respondLater().thenAccept {
				sc.createFollowupMessageBuilder().addEmbed(embed).send().get()
			}
			thread {
				Thread.sleep(10000)
				Runtime.getRuntime().exec("rundll32.exe powrprof.dll,SetSuspendState 0,1,0")
			}
		}
	}
}