package com.kendhammer.john.urbandictionarykotlin.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface UrbanApi {

    @GET("/define")
    fun getDefinitions(
        @Query("term") term: String,
        @Header("X-RapidAPI-Host") host: String,
        @Header("X-RapidAPI-Key") key: String
    ): Single<UrbanResponse>
}