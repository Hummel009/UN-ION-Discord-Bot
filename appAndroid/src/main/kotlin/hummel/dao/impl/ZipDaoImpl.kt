package hummel.dao.impl

import hummel.dao.FileDao
import hummel.dao.ZipDao
import hummel.factory.DaoFactory
import hummel.utils.addFolderContent
import net.lingala.zip4j.ZipFile

class ZipDaoImpl : ZipDao {
	private val fileDao: FileDao = DaoFactory.fileDao

	override fun unzip(filePath: String) {
		val file = fileDao.getFile(filePath)
		val folder = fileDao.getFolder(null)
		ZipFile(file).extractAll(folder.path)
	}

	override fun zip(filePath: String) {
		val file = fileDao.getFile(filePath)
		val folder = fileDao.getFolder(null)
		ZipFile(file).addFolderContent(folder)
	}
}