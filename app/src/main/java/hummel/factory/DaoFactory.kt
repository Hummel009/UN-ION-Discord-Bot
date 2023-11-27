package hummel.factory

import android.content.Context
import hummel.dao.OsDao
import hummel.dao.impl.AndroidDaoImpl

object DaoFactory {
	var context: Context? = null
	val dao: OsDao by lazy { AndroidDaoImpl(context!!) }
}