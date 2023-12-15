package hummel.factory

import android.content.Context
import hummel.dao.FileDao
import hummel.dao.JsonDao
import hummel.dao.ZipDao
import hummel.dao.impl.FileDaoImpl
import hummel.dao.impl.JsonDaoImpl
import hummel.dao.impl.ZipDaoImpl

@Suppress("unused", "RedundantSuppression")
object DaoFactory {
	var context: Context? = null
	val fileDao: FileDao by lazy { FileDaoImpl(context!!) }
	val jsonDao: JsonDao by lazy { JsonDaoImpl() }
	val zipDao: ZipDao by lazy { ZipDaoImpl() }
}