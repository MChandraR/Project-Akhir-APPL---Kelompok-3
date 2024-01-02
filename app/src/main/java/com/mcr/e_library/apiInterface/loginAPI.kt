package com.mcr.e_library.apiInterface

import com.mcr.e_library.model.LoginModel
import com.mcr.e_library.model.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.POST

interface loginAPI {
    @POST("api/login")
    fun login(@Body body:LoginModel): Call<UserModel>


}