package com.github.hummel.union.dao.impl

import com.github.hummel.union.dao.FileDao
import com.github.hummel.union.dao.ZipDao
import com.github.hummel.union.factory.DaoFactory
import net.lingala.zip4j.ZipFile
import java.io.File

class ZipDaoImpl : ZipDao {
	private val fileDao: FileDao = DaoFactory.fileDao

	override fun unzipFile(filePath: String, folderPath: String) {
		try {
			val file = fileDao.getFile(filePath)
			val folder = fileDao.getFolder(folderPath)
			ZipFile(file.path).extractAll(folder.path)
		} catch (ignored: Exception) {
		}
	}

	override fun zipFolder(folderPath: String, filePath: String) {
		try {
			val folder = fileDao.getFolder(folderPath)
			val file = fileDao.getFile(filePath)
			ZipFile(file.path).compressAll(folder)
		} catch (ignored: Exception) {
		}
	}

	private fun ZipFile.compressAll(folder: File) {
		folder.listFiles()?.forEach {
			if (it.isDirectory) {
				addFolder(it)
			} else {
				addFile(it)
			}
		}
	}
}