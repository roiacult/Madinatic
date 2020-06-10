package com.roacult.madinatic

import android.app.Application
import com.pusher.pushnotifications.PushNotifications
import com.roacult.madinatic.di.getModules
import com.roacult.madinatic.utils.PUSHER_INSTANCE_ID
import com.roacult.madinatic.utils.StringProvider
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class MadinaticApp : Application() ,StringProvider{


    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Timber.v("starting koin from application ...")
        startKoin {
            // use AndroidLogger as Koin Logger - default Level.INFO
            androidLogger()
            // use the Android context given there
            androidContext(this@MadinaticApp)
            //Module list
            modules(getModules())
        }
        PushNotifications.start(this,PUSHER_INSTANCE_ID)
    }

    override fun getStringFromResource(resource: Int): String {
        return getString(resource)
    }
}