package com.github.hummel.union.utils

import com.github.hummel.union.bean.ServerData
import com.github.hummel.union.lang.I18n
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.interaction.SlashCommandInteraction

fun EmbedBuilder.error(sc: SlashCommandInteraction, serverData: ServerData, desc: String): EmbedBuilder =
	setAuthor(sc.user).setTitle(I18n.of("MSG_ERROR", serverData)).setDescription(desc)

fun EmbedBuilder.access(sc: SlashCommandInteraction, serverData: ServerData, desc: String): EmbedBuilder =
	setAuthor(sc.user).setTitle(I18n.of("MSG_ACCESS", serverData)).setDescription(desc)

fun EmbedBuilder.success(sc: SlashCommandInteraction, serverData: ServerData, desc: String): EmbedBuilder =
	setAuthor(sc.user).setTitle(I18n.of("MSG_SUCCESS", serverData)).setDescription(desc)