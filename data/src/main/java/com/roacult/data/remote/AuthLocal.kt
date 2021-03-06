package com.roacult.data.remote

import android.content.SharedPreferences
import com.google.gson.Gson
import com.roacult.data.local.entities.UserLocalEntity
import com.roacult.data.utils.PREFRENCES
import com.roacult.data.utils.TOKEN_PREFEXE

class AuthLocal (
    private val prefrence : SharedPreferences
) {

    fun storeToken(token: String) {
        prefrence.edit().apply {
            putString(PREFRENCES.TOKEN,token)
            apply()
        }
    }

    fun storeUser(userEntity: UserLocalEntity, gson: Gson) {
        val userSerialized = gson.toJson(userEntity)
        prefrence.edit().apply {
            putString(PREFRENCES.USER,userSerialized)
            apply()
        }
    }

    fun getToken(): String {
        return TOKEN_PREFEXE+prefrence.getString(PREFRENCES.TOKEN,"")!!
    }

    fun getUserState(): Boolean {
        return prefrence.getString(PREFRENCES.TOKEN,"") != ""
    }

    fun logout() {
        prefrence.edit().apply {
            clear()
            apply()
        }
    }
}