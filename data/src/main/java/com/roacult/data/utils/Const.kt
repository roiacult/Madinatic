package com.roacult.data.utils

//TODO add base url here
const val BASE_URL = "http://127.0.0.1:8000"

//rest api roots
object API_ROOTS {
    const val LOGIN = "/rest-auth/login/"
    const val RESET_PASSWORD = "/rest-auth/password/reset/"
    const val USER_WITH_TOKEN = "/api/userwithtoken/"
    const val REGISTRATION = "/"
}


//prefrences keys
object PREFRENCES {

    const val TOKEN = "com.roacult.kero:TOKEN"
    const val USER = "com.roacult.kero:USER"
}

const val DATABASE_NAME = ""

const val TOKEN_PREFEXE = "token "
