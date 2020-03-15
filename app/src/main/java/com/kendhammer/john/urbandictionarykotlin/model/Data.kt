package com.kendhammer.john.urbandictionarykotlin.model

import com.google.gson.annotations.SerializedName

data class Definition(
    val word: String?,
    val definition: String?,
    val example: String?,
    val author: String?,
    @SerializedName("written_on")
    val writtenOn: String?,
    @SerializedName("thumbs_up")
    var thumbsUp: Int?,
    @SerializedName("thumbs_down")
    var thumbsDown: Int?,
    @SerializedName("sound_urls")
    val soundUrls: List<String>?
)

data class UrbanResponse(
    val list: List<Definition>
)