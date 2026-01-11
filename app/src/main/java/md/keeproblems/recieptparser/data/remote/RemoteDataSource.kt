package md.keeproblems.recieptparser.data.remote

import md.keeproblems.recieptparser.data.local.WithId

interface RemoteDataSource<T : WithId> {
    suspend fun fetchAll(): List<T>
    suspend fun upload(item: T)
}
