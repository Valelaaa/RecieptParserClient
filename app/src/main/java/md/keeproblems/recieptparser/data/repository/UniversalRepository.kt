package md.keeproblems.recieptparser.data.repository

import md.keeproblems.recieptparser.data.local.LocalDataSource
import md.keeproblems.recieptparser.data.local.WithId
import md.keeproblems.recieptparser.data.remote.RemoteDataSource

class UniversalRepository<T : WithId>(
    private val local: LocalDataSource<T>,
    private val remote: RemoteDataSource<T>?
) {

    suspend fun getAll(forceRemote: Boolean = false): List<T> {
        return if (forceRemote && remote != null) {
            val data = remote.fetchAll()
            local.saveAll(data)
            data
        } else {
            local.getAll()
        }
    }

    suspend fun add(item: T) {
        local.save(item)
        remote?.upload(item)
    }
}