package com.mcr.e_library.model

import com.google.gson.annotations.SerializedName

class BookModel(id_buku:String, judul:String, pengarang:String, tahun_terbit:Int, penerbit:String, kategori:String) {
    @SerializedName("id_buku")
     var id_buku:String = id_buku
        get() {
            return  field
        }

    @SerializedName("judul")
    var judul:String = judul
        get() {
            return  field
        }

    @SerializedName("pengarang")
    var pengarang:String = pengarang
        get() {
            return  field
        }

    @SerializedName("tahun_terbit")
    var tahun_terbit:Int = tahun_terbit
        get() {
            return  field
        }

    @SerializedName("penerbit")
    var penerbit:String = penerbit
        get() {
            return  field
        }

    @SerializedName("kategori")
    var kategori:String = kategori
        get() {
            return  field
        }
}