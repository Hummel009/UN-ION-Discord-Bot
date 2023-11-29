package hummel.dao.impl

import android.content.Context
import hummel.dao.FileDao
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class FileDaoImpl(private var context: Context) : FileDao {
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
			file.delete()
		}
	}

	override fun removeFolder(folderPath: String) {
		val folder = getFolder(folderPath)
		if (folder.exists()) {
			folder.deleteRecursively()
		}
	}

	override fun getFile(filePath: String): File = File(context.filesDir, filePath)

	override fun getFolder(folderPath: String?): File = folderPath?.let { File(context.filesDir, it) } ?: File("files")

	override fun readFromFile(filePath: String): ByteArray {
		var byteArray: ByteArray
		val file = getFile(filePath)
		FileInputStream(file).use {
			byteArray = ByteArray(it.available())
			it.read(byteArray)
		}
		return byteArray
	}

	override fun writeToFile(filePath: String, byteArray: ByteArray) {
		val file = getFile(filePath)
		FileOutputStream(file).use {
			it.write(byteArray)
		}
	}

	override fun appendToFile(filePath: String, byteArray: ByteArray) {
		val file = getFile(filePath)
		FileOutputStream(file, true).use {
			it.write(byteArray)
		}
	}

	override fun getRandomLine(filePath: String): String? {
		val file = getFile(filePath)
		val lines = file.readLines()
		if (lines.isNotEmpty()) {
			val random = Random()
			val randomLine = lines[random.nextInt(lines.size)]
			if (randomLine.isNotEmpty()) {
				return randomLine
			}
			return null
		} else {
			return null
		}
	}
}