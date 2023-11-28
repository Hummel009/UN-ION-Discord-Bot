package hummel.factory

import android.content.Context
import hummel.dao.OsDao
import hummel.dao.impl.OsDaoImpl

object DaoFactory {
	var context: Context? = null
	val osDao: OsDao by lazy { OsDaoImpl(context!!) }
}