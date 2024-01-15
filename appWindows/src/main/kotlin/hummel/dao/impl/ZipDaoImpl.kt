package hummel.dao.impl

import hummel.dao.FileDao
import hummel.dao.ZipDao
import hummel.factory.DaoFactory
import net.lingala.zip4j.ZipFile

class ZipDaoImpl : ZipDao {
	private val fileDao: FileDao = DaoFactory.fileDao

	override fun unzipFile(filePath: String, folderPath: String) {
		try {
			val file = fileDao.getFile(filePath)
			val folder = fileDao.getFolder(folderPath)
			ZipFile(file.path).extractAll(folder.path)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	override fun zipFolder(folderPath: String, filePath: String) {
		try {
			val folder = fileDao.getFolder(folderPath)
			val file = fileDao.getFile(filePath)
			ZipFile(file.path).compressAll(folder.path)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	private fun ZipFile.compressAll(folderPath: String) {
		val folder = fileDao.getFolder(folderPath)
		folder.listFiles()?.forEach {
			if (it.isDirectory) {
				addFolder(it)
			} else {
				addFile(it)
			}
		}
	}
}