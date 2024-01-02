package com.mcr.e_library.util

import android.content.Context
import android.content.SharedPreferences
import com.mcr.e_library.model.UserModel

class userController(context:Context) {
    lateinit var  sp:SharedPreferences
    lateinit var  spEditor:SharedPreferences.Editor
    init {
        sp = context.getSharedPreferences("mcr", Context.MODE_PRIVATE)
        spEditor = context.getSharedPreferences("mcr", Context.MODE_PRIVATE).edit()
    }

     fun saveUserData(userData:UserModel.UserData){
        spEditor.putString("user_id",userData.user_id)
        spEditor.putString("username",userData.username)
        spEditor.putString("password",userData.password)
        spEditor.putString("nama",userData.nama)
        spEditor.putString("level",userData.level)
        spEditor.apply()
    }

     fun getUserData():UserModel.UserData{
        var userData:UserModel.UserData = UserModel.UserData()
        userData.user_id = sp.getString("user_id","")!!
        userData.username = sp.getString("username","")!!
        userData.password = sp.getString("password","")!!
        userData.nama = sp.getString("nama","")!!
        userData.level = sp.getString("level","")!!
        return userData
    }
}