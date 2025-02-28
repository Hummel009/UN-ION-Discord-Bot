package com.github.hummel.union.windows

import com.github.hummel.union.bean.BotData
import java.io.File
import java.io.FileReader
import java.util.*

fun main() {
	val propertiesFile = "start.properties"
	val properties = Properties()

	try {
		val file = File(propertiesFile)
		if (file.exists()) {
			properties.load(FileReader(file))
			val token = properties.getProperty("token")
			val ownerId = properties.getProperty("ownerId")
			if (token != null && ownerId != null) {
				launchService(token, ownerId, "files", null)
			} else {
				println("Не удалось прочитать необходимые свойства из файла.")
				requestUserInput()
			}
		} else {
			println("Файл $propertiesFile не найден.")
			requestUserInput()
		}
	} catch (e: Exception) {
		println("Ошибка при чтении файла: ${e.message}")
		requestUserInput()
	}
}

fun requestUserInput() {
	print("Введите токен: ")
	val token = readln()

	print("Введите ID владельца: ")
	val ownerId = readln()

	launchService(token, ownerId, "files", null)
}

@Suppress("UNUSED_PARAMETER", "RedundantSuppression", "unused")
fun launchService(token: String, ownerId: String, root: String, context: Any?) {
	BotData.token = token
	BotData.ownerId = ownerId
	BotData.root = root
	val adapter = DiscordAdapter()
	adapter.launch()
}