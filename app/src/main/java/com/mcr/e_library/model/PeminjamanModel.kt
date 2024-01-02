package com.mcr.e_library.model

import com.google.gson.annotations.SerializedName

class PeminjamanModel(id:String,idBuku:String) {
    @SerializedName("id_peminjaman")
    var id_peminjaman = ""
    @SerializedName("id_anggota")
    var id_anggota:String = id
    @SerializedName("id_buku")
    var id_buku:String = idBuku
    @SerializedName("status")
    var status:String = ""
    @SerializedName("message")
    var message:String = ""
}