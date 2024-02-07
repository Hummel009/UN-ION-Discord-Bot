package com.github.hummel.union.dao.impl

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.github.hummel.union.dao.FileDao
import com.github.hummel.union.dao.JsonDao
import com.github.hummel.union.factory.DaoFactory

class JsonDaoImpl : JsonDao {
	private val fileDao: FileDao = DaoFactory.fileDao

	override fun <D> readFromJson(filePath: String, clazz: Class<D>): D? {
		return try {
			val gson = Gson()
			val json = fileDao.readFromFile(filePath)
			gson.fromJson(String(json), clazz)
		} catch (ignored: Exception) {
			null
		}
	}

	override fun <D> writeToJson(filePath: String, serverData: D) {
		val gson = GsonBuilder().setPrettyPrinting().create()
		val json = gson.toJson(serverData)
		fileDao.createFile(filePath)
		fileDao.writeToFile(filePath, json.toByteArray())
	}
}