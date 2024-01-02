package com.mcr.e_library.apiInterface

import com.mcr.e_library.model.KunjunganAPIModel
import com.mcr.e_library.model.KunjunganModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface kehadiranAPI {
    @POST("api/kunjungans")
    fun getDataKunjungan(@Body body:KunjunganAPIModel):Call<KunjunganAPIModel>

    @POST("api/kunjungan")
    fun addKunjungan(@Body body:KunjunganModel):Call<KunjunganModel>
}