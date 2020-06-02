package com.roacult.madinatic.ui.announce

import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.roacult.domain.entities.Announce
import com.roacult.domain.exceptions.AnnounceFailure
import com.roacult.domain.usecases.announce.AnnounceFilter
import com.roacult.domain.usecases.announce.AnnouncePage
import com.roacult.domain.usecases.announce.AnnouncePageParam
import com.roacult.domain.usecases.announce.GetAnnouncePage
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.kero.team7.jstarter_domain.interactors.launchInteractor
import com.roacult.madinatic.R
import com.roacult.madinatic.base.BaseViewModel
import com.roacult.madinatic.base.State
import com.roacult.madinatic.utils.*
import com.roacult.madinatic.utils.states.*

class AnnounceViewModel(
    private val getAnnouncePage: GetAnnouncePage,
    private val stringProvider: StringProvider
) : BaseViewModel<AnnounceState>(AnnounceState()) , PagingCallback<AnnounceFailure,AnnouncePage>{

    private var lastTimeInvalidated: Long = System.currentTimeMillis()/1000

    var filter: AnnounceFilter = AnnounceFilter.CURRENT

    private val dataSourceFactory: AnnounceDataSourceFactory by lazy {
        AnnounceDataSourceFactory(this)
    }

    private val liveData by lazy {
        LivePagedListBuilder(dataSourceFactory, getPagedListConfig()).build()
    }

    private val observer by lazy {
        Observer<PagedList<Announce>> {
            setState { copy(announces = it) }
        }
    }

    init { liveData.observeForever(observer) }

    override fun loadInitial(callback: (Either<AnnounceFailure, AnnouncePage>)->Unit){
        return loadPage(1,callback)
    }

    override fun loadPage(page: Int,callback: (Either<AnnounceFailure, AnnouncePage>)->Unit){
        setState { copy(announceState = Loading()) }
        scope.launchInteractor(getAnnouncePage,
            AnnouncePageParam(page,filter,getDateTimeFromTimeStamp(DATE_FORMAT))
        ){
            callback(it)
            it.either({
                val msg =when(it){
                    AnnounceFailure.InternetConnection -> stringProvider.getStringFromResource(R.string.internet_prblm)
                    else -> stringProvider.getStringFromResource(R.string.unknown_error)
                }
                setState { copy(announceState = Fail(it),errorMsg = Event(msg)) }
            },{
                setState { copy(announceState = Success(None())) }
            })
        }
    }

    fun invalidate(checkTime: Boolean = false) {
        if(checkTime) {
            val currentTime = System.currentTimeMillis()/1000
            if((currentTime - lastTimeInvalidated) < 60) {
                return
            }
        }
        lastTimeInvalidated = System.currentTimeMillis()/1000
        dataSourceFactory.sourceLiveData.value?.invalidate()
    }

    fun contactEmail(announce: Announce){

    }

    fun contactPhone(announce: Announce){

    }

}

data class AnnounceState(
    val errorMsg: Event<String>? = null,
    val announceState: Async<None> = Uninitialized,
    val announces : PagedList<Announce>? = null
) : State