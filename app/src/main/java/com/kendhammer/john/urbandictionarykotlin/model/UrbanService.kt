package com.kendhammer.john.urbandictionarykotlin.model

import com.kendhammer.john.urbandictionarykotlin.di.DaggerApiComponent
import io.reactivex.Single
import javax.inject.Inject

const val HOST = "mashape-community-urban-dictionary.p.rapidapi.com"
const val KEY = "490de763b7msh1bc10737a48884ap15fb6cjsn5e8c5220b6db"

class UrbanService {


    @Inject
    lateinit var api: UrbanApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getDefinitions(term: String): Single<UrbanResponse> {
        return api.getDefinitions(term, HOST, KEY)
    }
}