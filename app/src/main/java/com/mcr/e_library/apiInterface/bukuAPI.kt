package com.mcr.e_library.apiInterface

import com.mcr.e_library.model.BookModel
import com.mcr.e_library.model.SearchModel
import com.mcr.e_library.model.UserBookModel
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface bukuAPI {
    @GET("api/buku")
    fun getBuku(): Call<ArrayList<BookModel>>

    @POST("api/buku")
    fun getUserBuku(@Body body:UserBookModel): Call<UserBookModel>

    @POST("api/buku/search")
    fun searchBuku(@Body body:SearchModel): Call<ArrayList<BookModel>>
}