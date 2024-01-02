package com.mcr.e_library.model

import com.google.gson.annotations.SerializedName

class UserModel {
    @SerializedName("status")
    var status = ""
        get() {
            return field
        }
        set(value) {
            field = value
        }

    @SerializedName("message")
    var message = ""
        get() {
            return field
        }
        set(value) {
            field = value
        }

    @SerializedName("data")
    var data:UserData = UserData()
        get() {
            return  field
        }

        set(value) {
            field = value
        }

    public class UserData{
        @SerializedName("user_id")
        var user_id = ""
            get() {
                return field
            }
            set(value) {
                field = value
            }

        @SerializedName("username")
        var username = ""
            get() {
                return field
            }
            set(value) {
                field = value
            }

        @SerializedName("password")
        var password = ""
            get() {
                return field
            }
            set(value) {
                field = value
            }

        @SerializedName("nama")
        var nama = ""
            get() {
                return field
            }
            set(value) {
                field = value
            }

        @SerializedName("level")
        var level = ""
            get() {
                return field
            }
            set(value) {
                field = value
            }

        @SerializedName("id_anggota")
        var id_anggota = ""
            get() {
                return field
            }
            set(value) {
                field = value
            }
    }
}