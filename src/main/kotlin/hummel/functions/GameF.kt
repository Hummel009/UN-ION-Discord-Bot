package hummel.functions

import hummel.functions
import hummel.prefix
import org.javacord.api.event.message.MessageCreateEvent

val answers: Set<String> = setOf(
	"Да.",
	"Можешь не сомневаться в этом)",
	"Определённо да!",
	"Я уверен в этом на все 100%",
	"Нет.",
	"Я считаю, что нет)",
	"Определённо нет!",
	"Точно нет, я уверен на 100%"
)

fun eightBall(event: MessageCreateEvent) {
	if (event.messageContent == "${prefix}8ball") {
		functions.add("8ball")
		event.channel.sendMessage(answers.random())
	}
}

fun randomChoice(event: MessageCreateEvent) {
	if (event.messageContent.startsWith("${prefix}choice")) {
		functions.add("choice ITEM-1 ITEM-2 ITEM-N")
		val parameters = event.messageContent.split(" ")
		if (parameters.size >= 2) {
			try {
				var answer: String
				while (true) {
					answer = parameters.random()
					if (answer != parameters[0]) {
						break
					}
				}
				event.channel.sendMessage(answer)
			} catch (e: NumberFormatException) {
				event.channel.sendMessage("Invalid word format after !choice.")
			}
		} else {
			event.channel.sendMessage("No integer provided after !choice.")
		}
	}
}