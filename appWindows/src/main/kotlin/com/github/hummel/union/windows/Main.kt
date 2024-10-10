package com.github.hummel.union.windows

import com.github.hummel.union.bean.BotData

fun main() {
	print("Enter Token: ")
	val token = readln()

	print("Enter Owner ID: ")
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