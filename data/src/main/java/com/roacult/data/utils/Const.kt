package com.roacult.data.utils

//TODO add base url here
const val BASE_URL = "http://157.230.19.233/"

//rest api roots
object APIROOTS {
    const val LOGIN = "/api/login/"
    const val RESET_PASSWORD = "/api/password/reset/"
    const val USER_WITH_TOKEN = "/api/user/"
    const val REGISTRATION = "/api/registration/"
    const val UPDATEPASSWORD = "/api/password/change/"
    const val DECLARATION = "/api/declarations/"
    const val DECLARATIONTYPE = "/api/declarations_types/"
    const val DECLARATIONDOC = "/api/documents/"
}

//prefrences keys
object PREFRENCES {

    const val TOKEN = "com.roacult.kero:TOKEN"
    const val USER = "com.roacult.kero:USER"
}

const val DATABASE_NAME = ""

const val TOKEN_PREFEXE = "token "
