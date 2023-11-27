package hummel.dao

import java.io.File

interface OsDao {
	fun createFolder(folderPath: String)
	fun createFile(filePath: String)
	fun removeFolder(folderPath: String)
	fun removeFile(filePath: String)
	fun getFile(filePath: String): File
	fun getFolder(folderPath: String): File
	fun readFromFile(filePath: String): String
	fun writeToFile(filePath: String, text: String)
	fun appendToFile(filePath: String, text: String)
	fun getRandomLine(filePath: String): String?
}