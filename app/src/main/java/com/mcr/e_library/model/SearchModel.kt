package com.mcr.e_library.model

import com.google.gson.annotations.SerializedName

class SearchModel(keys:String) {
    @SerializedName("key")
    val key:String = keys
        get() {
            return field
        }
}