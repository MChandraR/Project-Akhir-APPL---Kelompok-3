package com.mcr.e_library.util

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class API{
    val serverUrl = "http://192.168.137.1:3000/"

    fun getUrl():String{
        return serverUrl
    }

    fun getClientAPI(): Retrofit {
        return Retrofit.Builder().baseUrl(serverUrl).addConverterFactory(GsonConverterFactory.create()).build()
    }
}