package com.mcr.e_library.model

import com.google.gson.annotations.SerializedName

class LoginModel(username:String, password:String) {
    @SerializedName("username")
    var username = username
        get() {
            return field
        }

    @SerializedName("password")
    var password = password
        get() {
            return field
        }
}