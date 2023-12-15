package hummel.dao

interface ZipDao {
	fun unzip(filePath: String)
	fun zip(filePath: String)
}