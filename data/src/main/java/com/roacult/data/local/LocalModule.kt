package com.roacult.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.roacult.data.utils.DATABASE_NAME
import org.koin.dsl.module

val localModule = module {

    single <SharedPreferences>{
        val context = get<Context>()
        context.getSharedPreferences("prefrences",Context.MODE_PRIVATE)
    }

//    single{
//        Room.databaseBuilder(get(),Database::class.java,DATABASE_NAME)
//            .build()
//    }

//    single{
//        val db = get<Database>()
//        db.provideAddProductDao()
//    }
}