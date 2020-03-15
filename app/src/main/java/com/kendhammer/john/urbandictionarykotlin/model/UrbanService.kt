package com.kendhammer.john.urbandictionarykotlin.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val HOST = "mashape-community-urban-dictionary.p.rapidapi.com"
const val KEY = "490de763b7msh1bc10737a48884ap15fb6cjsn5e8c5220b6db"

class UrbanService {

    private val BASE_URL = "https://mashape-community-urban-dictionary.p.rapidapi.com"

    private val api: UrbanApi

    init {
        api = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(UrbanApi::class.java)
    }

    fun getDefinitions(term: String): Single<UrbanResponse> {
        return api.getDefinitions(term, HOST, KEY)
    }
}