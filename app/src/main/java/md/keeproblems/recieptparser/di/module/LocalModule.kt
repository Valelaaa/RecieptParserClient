package md.keeproblems.recieptparser.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.builtins.serializer
import md.keeproblems.recieptparser.data.local.DataStoreManager
import md.keeproblems.recieptparser.data.local.LocalSharedPrefsDataSource
import md.keeproblems.recieptparser.domain.models.ReceiptData
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    internal fun provideUserReceiptLocalDataSource(
        dataStore: DataStoreManager
    ): LocalSharedPrefsDataSource<ReceiptData> {
        return LocalSharedPrefsDataSource(
            key = "user_receipts",
            dataStore = dataStore,
            serializer = ReceiptData.serializer()
        )
    }
}
