package md.keeproblems.recieptparser.data.remote

import md.keeproblems.recieptparser.domain.models.ReceiptData

internal class FakeRemoteDataSource : RemoteDataSource<ReceiptData> {
    override suspend fun fetchAll(): List<ReceiptData> = emptyList()
    override suspend fun upload(item: ReceiptData) {}
}
