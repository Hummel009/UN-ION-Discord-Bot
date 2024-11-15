package com.github.hummel.union.service

import com.github.hummel.union.bean.ServerData
import org.javacord.api.interaction.SlashCommandInteraction

interface AccessService {
	fun fromManagerAtLeast(sc: SlashCommandInteraction, serverData: ServerData): Boolean
	fun fromOwnerAtLeast(sc: SlashCommandInteraction): Boolean
}