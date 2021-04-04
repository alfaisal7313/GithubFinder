package com.example.github.finder.di

import com.example.github.finder.network.api.ApiHelper
import com.example.github.finder.network.api.ApiImpl
import com.example.github.finder.network.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideHelper(apiHelper: ApiImpl): ApiHelper {
        return apiHelper
    }
}