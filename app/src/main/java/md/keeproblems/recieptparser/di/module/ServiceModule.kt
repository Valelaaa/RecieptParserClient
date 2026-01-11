package md.keeproblems.recieptparser.di.module

import android.os.Build
import androidx.annotation.RequiresApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import md.keeproblems.recieptparser.utils.service.DateTimeParserService
import java.util.Date
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@RequiresApi(Build.VERSION_CODES.O)
object ServiceModule {
    @Provides
    @Singleton
    fun providesDateTimeParserService(): DateTimeParserService = DateTimeParserService()
}