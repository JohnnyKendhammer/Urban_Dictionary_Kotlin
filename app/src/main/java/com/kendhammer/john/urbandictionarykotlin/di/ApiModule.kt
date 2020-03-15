package com.kendhammer.john.urbandictionarykotlin.di

import com.kendhammer.john.urbandictionarykotlin.model.UrbanApi
import com.kendhammer.john.urbandictionarykotlin.model.UrbanService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {
    private val BASE_URL = "https://mashape-community-urban-dictionary.p.rapidapi.com"

    @Provides
    fun provideUrbanApi(): UrbanApi{
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(UrbanApi::class.java)
    }

    @Provides
    fun provideUrbanService(): UrbanService{
        return UrbanService()
    }
}