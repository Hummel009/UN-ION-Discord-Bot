package hummel.dao.impl

import android.content.Context
import hummel.dao.OsDao
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.util.*

class AndroidDaoImpl(private val context: Context) : OsDao {
	override fun createFolder(folderPath: String) {
		val folder = getFolder(folderPath)
		if (!folder.exists()) {
			folder.mkdirs()
		}
	}

	override fun createFile(filePath: String) {
		val file = getFile(filePath)
		if (!file.exists()) {
			file.createNewFile()
		}
	}

	override fun removeFolder(folderPath: String) {
		val folder = getFolder(folderPath)
		if (folder.exists()) {
			folder.deleteRecursively()
		}
	}

	override fun removeFile(filePath: String) {
		val file = getFile(filePath)
		if (file.exists()) {
			file.delete()
		}
	}

	override fun writeToFile(filePath: String, text: String) {
		val file = getFile(filePath)
		FileOutputStream(file).use {
			it.write(text.toByteArray(StandardCharsets.UTF_8))
		}
	}

	override fun readFromFile(filePath: String): String {
		var byteArray: ByteArray
		val file = getFile(filePath)
		FileInputStream(file).use {
			byteArray = ByteArray(it.available())
			it.read(byteArray)
		}
		return String(byteArray)
	}

	override fun appendToFile(filePath: String, text: String) {
		val file = getFile(filePath)
		FileOutputStream(file, true).use {
			it.write(text.toByteArray(StandardCharsets.UTF_8))
		}
	}

	override fun getFile(filePath: String): File = File(context.filesDir, filePath)

	override fun getFolder(folderPath: String): File = File(context.filesDir, folderPath)

	override fun getRandomLine(filePath: String): String? {
		val file = getFile(filePath)
		val lines = file.readLines()
		if (lines.isNotEmpty()) {
			val rand = Random()
			val randomLine = lines[rand.nextInt(lines.size)]
			if (randomLine.isNotEmpty()) {
				return randomLine
			}
			return null
		} else {
			return null
		}
	}
}