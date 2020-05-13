package com.roacult.data.utils

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.roacult.domain.entities.AttachmentType
import com.roacult.domain.entities.DeclarationState
import org.koin.core.scope.Scope
import retrofit2.Retrofit

fun List<String>.serializeList(gson: Gson): String {
    return try {
        gson.toJson(this)
    } catch (e: JsonSyntaxException) {
        return ""
    }
}

fun String.deserializeList(gson: Gson): List<String> {
    val listType = object : TypeToken<List<String>>() {}.type
    return try {
        gson.fromJson(this, listType) ?: arrayListOf()
    } catch (e: JsonSyntaxException) {
        arrayListOf()
    }
}

fun HashMap<String, Int>.serializeHashMap(gson: Gson): String {
    return try {
        gson.toJson(this)
    } catch (e: JsonSyntaxException) {
        ""
    }
}

fun String.deserializeHashMap(gson: Gson): HashMap<String, Int> {
    val listType = object : TypeToken<HashMap<String, Int>>() {}.type
    return try {
        gson.fromJson(this, listType) ?: hashMapOf()
    } catch (e: JsonSyntaxException) {
        hashMapOf()
    }
}

fun <E> List<E>.toArrayList(): ArrayList<E> {
    return ArrayList<E>().apply {
        addAll(this@toArrayList)
    }
}

fun <T>Scope.createService(serviceClass:Class<T>):T{
    val retrofit :Retrofit = get()
    return retrofit.create(serviceClass)
}

fun String.toAttachmentType() : AttachmentType {
    return when(this) {
        REMOTEATTACHMENT.PDF -> AttachmentType.PDF
        REMOTEATTACHMENT.IMAGE -> AttachmentType.IMAGE
        else -> AttachmentType.OTHER
    }
}

fun AttachmentType.toRemote() : String {
    return when(this) {
        AttachmentType.PDF -> REMOTEATTACHMENT.PDF
        AttachmentType.IMAGE -> REMOTEATTACHMENT.IMAGE
        AttachmentType.OTHER -> REMOTEATTACHMENT.OTHER
    }
}


fun DeclarationState.toRemote() : String {
    return when(this) {
        DeclarationState.VALIDATED -> REMOTEDECLARATIONSTATES.VALIDATED
        DeclarationState.NOT_VALIDATED -> REMOTEDECLARATIONSTATES.NOT_VALIDATED
        DeclarationState.REFUSED -> REMOTEDECLARATIONSTATES.REFUSED
        DeclarationState.LACK_OF_INFO -> REMOTEDECLARATIONSTATES.LACK_OF_INFO
        DeclarationState.UNDER_TREATMENT -> REMOTEDECLARATIONSTATES.UNDER_TREATMENT
        DeclarationState.TREATED -> REMOTEDECLARATIONSTATES.TREATED
        DeclarationState.ARCHIVED -> REMOTEDECLARATIONSTATES.ARCHIVED
    }
}

fun String.toDeclarationState() : DeclarationState {
    return when(this) {
        REMOTEDECLARATIONSTATES.VALIDATED -> DeclarationState.VALIDATED
        REMOTEDECLARATIONSTATES.NOT_VALIDATED -> DeclarationState.NOT_VALIDATED
        REMOTEDECLARATIONSTATES.REFUSED -> DeclarationState.REFUSED
        REMOTEDECLARATIONSTATES.LACK_OF_INFO -> DeclarationState.LACK_OF_INFO
        REMOTEDECLARATIONSTATES.UNDER_TREATMENT -> DeclarationState.UNDER_TREATMENT
        REMOTEDECLARATIONSTATES.TREATED -> DeclarationState.TREATED
        REMOTEDECLARATIONSTATES.ARCHIVED -> DeclarationState.ARCHIVED
        else -> DeclarationState.NOT_VALIDATED
    }
}