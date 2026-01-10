package md.keeproblems.recieptparser.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import md.keeproblems.recieptparser.domain.usecases.GetProductsUseCase
import md.keeproblems.recieptparser.domain.usecases.impl.GetProductsUseCaseImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {
    @Binds
    @Singleton
    internal abstract fun bindGetProductsUseCase(
        getProductsUseCase: GetProductsUseCaseImpl
    ): GetProductsUseCase
}