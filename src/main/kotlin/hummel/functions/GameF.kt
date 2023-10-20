package hummel.functions

import org.javacord.api.event.interaction.InteractionCreateEvent

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

fun eightBall(event: InteractionCreateEvent) {
	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains("8ball")) {
		val arguments = sc.arguments[0].stringValue.get().split(" ")
		if (arguments.isNotEmpty()) {
			sc.createImmediateResponder().setContent(answers.random()).respond()
		} else {
			sc.createImmediateResponder().setContent("No arguments provided.").respond()
		}
	}
}

fun randomChoice(event: InteractionCreateEvent) {
	val sc = event.slashCommandInteraction.get()
	if (sc.fullCommandName.contains("choice")) {
		val arguments = sc.arguments[0].stringValue.get().split(" ")
		if (arguments.isNotEmpty()) {
			sc.createImmediateResponder().setContent(arguments.random()).respond()
		} else {
			sc.createImmediateResponder().setContent("No arguments provided.").respond()
		}
	}
}