package com.mcr.e_library.apiInterface

import com.mcr.e_library.model.PeminjamanModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface peminjamanAPI {
    @POST("api/pinjam")
    fun reqPeminjaman(@Body body:PeminjamanModel):Call<PeminjamanModel>

    @POST("api/peminjaman")
    fun getPeminjaman(@Body body:PeminjamanModel):Call<PeminjamanModel>

    @PUT("api/peminjaman")
    fun prosesPeminjaman(@Body body:PeminjamanModel):Call<PeminjamanModel>
}