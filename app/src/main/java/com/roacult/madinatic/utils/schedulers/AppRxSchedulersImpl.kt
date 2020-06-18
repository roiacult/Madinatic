package com.roacult.madinatic.utils.schedulers

import com.roacult.kero.team7.jstarter_domain.functional.AppRxSchedulers
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AppRxSchedulersImpl : AppRxSchedulers {
    override val io: Scheduler = Schedulers.io()
    override val main: Scheduler = AndroidSchedulers.mainThread()
    override val computation: Scheduler = Schedulers.computation()
}