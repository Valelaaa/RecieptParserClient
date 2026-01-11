package md.keeproblems.recieptparser.data.local

interface LocalDataSource<T : WithId> {
    suspend fun getAll(): List<T>
    suspend fun save(item: T): Boolean
    suspend fun saveAll(items: List<T>): Boolean
    suspend fun delete(id: String)
    suspend fun clear()
}
