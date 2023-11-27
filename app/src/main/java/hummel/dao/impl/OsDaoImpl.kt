package hummel.dao.impl

import android.content.Context
import hummel.dao.OsDao
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import java.util.zip.ZipInputStream

class OsDaoImpl(private val context: Context) : OsDao {
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

	override fun writeToFile(filePath: String, byteArray: ByteArray) {
		val file = getFile(filePath)
		FileOutputStream(file).use {
			it.write(byteArray)
		}
	}

	override fun readFromFile(filePath: String): ByteArray {
		var byteArray: ByteArray
		val file = getFile(filePath)
		FileInputStream(file).use {
			byteArray = ByteArray(it.available())
			it.read(byteArray)
		}
		return byteArray
	}

	override fun appendToFile(filePath: String, byteArray: ByteArray) {
		val file = getFile(filePath)
		FileOutputStream(file, true).use {
			it.write(byteArray)
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

	override fun unzipFile(filePath: String) {
		val zipFile = getFile(filePath)
		val destinationFolder = (zipFile.parentFile ?: return).absolutePath

		try {
			val buffer = ByteArray(1024)
			val zipInputStream = ZipInputStream(FileInputStream(zipFile))

			var zipEntry = zipInputStream.nextEntry
			while (zipEntry != null) {
				val newFile = File(destinationFolder, zipEntry.name)
				val directory = zipEntry.isDirectory

				if (directory) {
					newFile.mkdirs()
				} else {
					(newFile.parentFile ?: return).mkdirs()
					val fileOutputStream = FileOutputStream(newFile)

					var len: Int
					while (zipInputStream.read(buffer).also { len = it } > 0) {
						fileOutputStream.write(buffer, 0, len)
					}

					fileOutputStream.close()
				}

				zipEntry = zipInputStream.nextEntry
			}

			zipInputStream.closeEntry()
			zipInputStream.close()
		} catch (_: Exception) {
		}
	}
}