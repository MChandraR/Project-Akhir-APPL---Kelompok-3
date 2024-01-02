package com.mcr.e_library.model

import com.google.gson.annotations.SerializedName

class UserBookModel(id_anggota:String) {
    @SerializedName("status")
    var status:String = ""

    @SerializedName("message")
    var message:String = ""

    @SerializedName("id_anggota")
    var id_anggota:String = id_anggota

    @SerializedName("data")
    var dataBuku:java.util.ArrayList<BookModels> = ArrayList()

    class BookModels{
        @SerializedName("id_buku")
        var id_buku:String = "id_buku"

        @SerializedName("judul")
        var judul:String = "judul"

        @SerializedName("pengarang")
        var pengarang:String = "pengarang"

        @SerializedName("tahun_terbit")
        var tahun_terbit:Int = 0

        @SerializedName("penerbit")
        var penerbit:String = "penerbit"

        @SerializedName("kategori")
        var kategori:String = "kategori"

        @SerializedName("id_pengembalian")
        var id_pengembalian:String = ""

        @SerializedName("id_peminjaman")
        var id_peminjaman:String = ""

        @SerializedName("status")
        var status:String = "null"

    }
}