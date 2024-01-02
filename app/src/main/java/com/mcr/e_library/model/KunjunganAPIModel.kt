package com.mcr.e_library.model

import com.google.gson.annotations.SerializedName

class KunjunganAPIModel(id:String="") {
    @SerializedName("id_anggota")
    var id_anggota:String = id
    @SerializedName("status")
    var status:String = ""
    @SerializedName("message")
    var message:String = ""
    @SerializedName("data")
    var data:ArrayList<KunjunganModel> = ArrayList()
}