package com.github.hummel.union.dao

interface ZipDao {
	fun unzipFile(filePath: String, folderPath: String)
	fun zipFolder(folderPath: String, filePath: String)
}