package com.github.hummel.union

import java.io.File

fun main() {
	val inputFile = File("D:/messages.bin")
	val outputFile = File("D:/messages.txt")

	if (inputFile.exists()) {
		val inputLines = inputFile.readLines()

		val transformedLines = inputLines.map { msg ->
			try {
				val unicodeCodes = msg.split(" ").map { it.toInt() }
				val unicodeChars = unicodeCodes.map { it.toChar() }.toCharArray()
				String(unicodeChars)
			} catch (_: Exception) {
			}
		}

		outputFile.printWriter().use { writer ->
			transformedLines.forEach { writer.println(it) }
		}

		println("Messages.txt is ready.")
	} else {
		println("Messages.bin not found.")
	}
}