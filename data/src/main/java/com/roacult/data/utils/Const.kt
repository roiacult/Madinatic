package com.roacult.data.utils

//base url
const val BASE_URL = "https://madina-tic.ml/"

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
    const val ANNOUNCE_NESTED = "/api/announce_nested/"
}

//prefrences keys
object PREFRENCES {

    const val TOKEN = "com.roacult.kero:TOKEN"
    const val USER = "com.roacult.kero:USER"
}

object REMOTEATTACHMENT {
    const val PDF = "pdf"
    const val IMAGE = "image"
    const val OTHER = "other"
}

object REMOTEDECLARATIONSTATES {
    const val VALIDATED = "validated"
    const val NOT_VALIDATED = "not_validated"
    const val REFUSED = "refused"
    const val LACK_OF_INFO = "lack_of_info"
    const val UNDER_TREATMENT = "under_treatment"
    const val TREATED = "treated"
    const val ARCHIVED = "archived"
    const val DRAFT = "draft"
}

const val DATABASE_NAME = ""

const val TOKEN_PREFEXE = "token "
