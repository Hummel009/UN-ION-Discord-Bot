package com.github.hummel.union.utils

import com.github.hummel.union.bean.ServerData
import com.github.hummel.union.lang.I18n
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.interaction.SlashCommandInteraction

fun EmbedBuilder.error(sc: SlashCommandInteraction, serverData: ServerData, desc: String): EmbedBuilder =
	setAuthor(sc.user).setTitle(I18n.of("msg_error", serverData)).setDescription(desc)

fun EmbedBuilder.access(sc: SlashCommandInteraction, serverData: ServerData, desc: String): EmbedBuilder =
	setAuthor(sc.user).setTitle(I18n.of("msg_access", serverData)).setDescription(desc)

fun EmbedBuilder.success(sc: SlashCommandInteraction, serverData: ServerData, desc: String): EmbedBuilder =
	setAuthor(sc.user).setTitle(I18n.of("msg_success", serverData)).setDescription(desc)

fun String.build(preprompt: String): String = trimIndent().replace("\t", "").replace("\n", " ").format(preprompt) + "\n"