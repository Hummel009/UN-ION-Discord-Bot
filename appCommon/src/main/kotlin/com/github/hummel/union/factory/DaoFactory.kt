package com.github.hummel.union.factory

import com.github.hummel.union.dao.FileDao
import com.github.hummel.union.dao.JsonDao
import com.github.hummel.union.dao.ZipDao
import com.github.hummel.union.dao.impl.FileDaoImpl
import com.github.hummel.union.dao.impl.JsonDaoImpl
import com.github.hummel.union.dao.impl.ZipDaoImpl

@Suppress("unused", "RedundantSuppression")
object DaoFactory {
	val zipDao: ZipDao by lazy { ZipDaoImpl() }
	val jsonDao: JsonDao by lazy { JsonDaoImpl() }
	val fileDao: FileDao by lazy { FileDaoImpl() }
}