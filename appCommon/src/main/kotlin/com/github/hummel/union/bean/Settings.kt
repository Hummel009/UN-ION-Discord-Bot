package com.github.hummel.union.bean

import org.javacord.api.DiscordApi
import org.javacord.api.interaction.SlashCommandOption

data class Settings(
	val usage: String, val args: List<SlashCommandOption>, val api: DiscordApi
)