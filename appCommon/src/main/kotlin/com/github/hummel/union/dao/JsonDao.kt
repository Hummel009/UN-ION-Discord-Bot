package com.github.hummel.union.dao

interface JsonDao {
	fun <D> readFromJson(filePath: String, clazz: Class<D>): D?
	fun <D> writeToJson(filePath: String, serverData: D)
}