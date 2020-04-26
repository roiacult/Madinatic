package com.team7.data.utils

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.roacult.kero.team7.jstarter_domain.exception.Failure
import com.roacult.kero.team7.jstarter_domain.functional.Either
import org.koin.core.scope.Scope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun <T, F : Failure, R> Call<R>.lambdaEnqueue(
    onFailure: (t: Throwable) -> Either<F, T>,
    onSuccess: (response: Response<R>) -> Either<F, T>
): Either<F, T> =
    suspendCoroutine {
        this.enqueue(object : Callback<R> {
            override fun onFailure(call: Call<R>, t: Throwable) {
                it.resume(onFailure(t))
            }

            override fun onResponse(call: Call<R>, response: Response<R>) {
                it.resume(onSuccess(response))
            }
        })
    }
inline fun <T : Any> T.doIt(block: T.() -> Unit) {
    block(this)
}

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

//fun List<LocalProductImage>.serializeLocalImages(gson: Gson): String {
//    return try {
//        gson.toJson(this)
//    } catch (e: JsonSyntaxException) {
//        return ""
//    }
//}
//
//fun String.deserializeLocalImages(gson: Gson): List<LocalProductImage> {
//    val listType = object : TypeToken<List<LocalProductImage>>() {}.type
//    return try {
//        gson.fromJson(this, listType) ?: arrayListOf()
//    } catch (e: JsonSyntaxException) {
//        arrayListOf()
//    }
//}

//fun String.deserializeRemoteImages(gson: Gson): List<RemoteProductImage> {
//    val listType = object : TypeToken<List<RemoteProductImage>>() {}.type
//    return try {
//        gson.fromJson(this, listType) ?: arrayListOf()
//    } catch (e: JsonSyntaxException) {
//        arrayListOf()
//    }
//}

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
fun Context.isConnected(): Boolean =
    (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo?.isConnectedOrConnecting == true

fun String.formatToken()= "Token $this"
