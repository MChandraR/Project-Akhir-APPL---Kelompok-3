package com.mcr.e_library.model

import com.google.gson.annotations.SerializedName

class KunjunganModel(id:String, stat:String, ket:String) {

    @SerializedName("message")
    var message:String = ""

    @SerializedName("id_anggota")
    var id_anggota:String = id

    @SerializedName("id_kunjungan")
    var id_kunjungan:String = ""

    @SerializedName("status")
    var status:String = stat

    @SerializedName("keterangan")
    var keterangan:String = ket

    @SerializedName("waktu_kunjungan")
    var waktu_kunjungan:String = ""
}