package md.keeproblems.recieptparser.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import md.keeproblems.recieptparser.data.entities.parser.ReceiptMapper
import md.keeproblems.recieptparser.data.entities.parser.impl.ReceiptMapperImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MapperModule {
    @Binds
    @Singleton
    internal abstract fun bindsReceiptMapper(receiptMapper: ReceiptMapperImpl): ReceiptMapper
}