package hummel.dao

import java.io.File

interface OsDao {
	fun createFolder(folderPath: String)
	fun createFile(filePath: String)
	fun removeFolder(folderPath: String)
	fun removeFile(filePath: String)
	fun getFile(filePath: String): File
	fun getFolder(folderPath: String): File
	fun readFromFile(filePath: String): ByteArray
	fun writeToFile(filePath: String, byteArray: ByteArray)
	fun appendToFile(filePath: String, byteArray: ByteArray)
	fun getRandomLine(filePath: String): String?
	fun unzipFile(filePath: String)
}