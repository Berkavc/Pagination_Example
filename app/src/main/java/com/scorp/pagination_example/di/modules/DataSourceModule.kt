package com.scorp.pagination_example.di.modules

import android.app.Application
import com.scorp.domain.DataSource
import com.scorp.pagination_example.PaginationExampleApplication
import com.scorp.pagination_example.di.qualifiers.DataSourceQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @DataSourceQualifier
    @Provides
    fun provideDataSource():DataSource{
        return DataSource()
    }
}