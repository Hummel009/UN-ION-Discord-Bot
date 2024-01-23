package hummel.dao.impl

import hummel.bean.BotData
import hummel.dao.FileDao
import hummel.utils.random
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

private const val NOT_EXIST = "File doesn't exist!"

class FileDaoImpl : FileDao {
	override fun createFile(filePath: String) {
		val file = getFile(filePath)
		if (!file.exists()) {
			file.createNewFile()
		}
	}

	override fun createFolder(folderPath: String) {
		val folder = getFolder(folderPath)
		if (!folder.exists()) {
			folder.mkdirs()
		}
	}

	override fun removeFile(filePath: String) {
		val file = getFile(filePath)
		if (file.exists()) {
			if (!file.delete()) {
				throw FileNotFoundException()
			}
		}
	}

	override fun removeFolder(folderPath: String) {
		val folder = getFolder(folderPath)
		if (folder.exists()) {
			folder.deleteRecursively()
		}
	}

	override fun getFile(filePath: String): File = File(BotData.root, filePath)

	override fun getFolder(folderPath: String): File = File(BotData.root, folderPath)

	override fun readFromFile(filePath: String): ByteArray {
		var byteArray: ByteArray
		val file = getFile(filePath)
		if (file.exists()) {
			FileInputStream(file).use {
				byteArray = ByteArray(it.available())
				it.read(byteArray)
			}
		} else {
			throw Exception(NOT_EXIST)
		}
		return byteArray
	}

	override fun writeToFile(filePath: String, byteArray: ByteArray) {
		val file = getFile(filePath)
		if (file.exists()) {
			FileOutputStream(file).use {
				it.write(byteArray)
			}
		} else {
			throw Exception(NOT_EXIST)
		}
	}

	override fun appendToFile(filePath: String, byteArray: ByteArray) {
		val file = getFile(filePath)
		if (file.exists()) {
			FileOutputStream(file, true).use {
				it.write(byteArray)
			}
		} else {
			throw Exception(NOT_EXIST)
		}
	}

	override fun getRandomLine(filePath: String): String? {
		val file = getFile(filePath)
		if (file.exists()) {
			val lines = file.readLines()
			if (lines.isNotEmpty()) {
				val randomLine = lines[random.nextInt(lines.size)]
				if (randomLine.isNotEmpty()) {
					return randomLine
				}
			}
			return null
		}
		throw Exception(NOT_EXIST)
	}
}