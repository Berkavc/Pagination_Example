package com.scorp.pagination_example.di.modules

import android.app.Application
import com.scorp.pagination_example.PaginationExampleApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @ApplicationContext
    @Provides
    fun provideContext(): Application {
        return PaginationExampleApplication()
    }
}