package md.keeproblems.recieptparser.di.module

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import md.keeproblems.recieptparser.data.apiservices.ParserApiService
import md.keeproblems.recieptparser.utils.RetrofitProvider
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    internal fun provideReceiptApi(
    ): ParserApiService{
        return RetrofitProvider.retrofit
        .create<ParserApiService>()
    }
}