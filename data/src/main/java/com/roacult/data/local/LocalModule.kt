package com.roacult.data.local

import android.content.Context
import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.roacult.data.remote.AuthLocal
import org.koin.dsl.module

val localModule = module {

    single <SharedPreferences>{
        val context = get<Context>()
        context.getSharedPreferences("prefrences",Context.MODE_PRIVATE)
    }

    single { RxSharedPreferences.create(get()) }

//    single{
//        Room.databaseBuilder(get(),Database::class.java,DATABASE_NAME)
//            .build()
//    }

//    single{
//        val db = get<Database>()
//        db.provideAddProductDao()
//    }

    single { AuthLocal(get()) }
    single { MainLocal(get(),get(),get()) }
}