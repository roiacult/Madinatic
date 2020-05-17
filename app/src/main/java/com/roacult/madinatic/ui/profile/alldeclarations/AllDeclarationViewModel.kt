package com.roacult.madinatic.ui.profile.alldeclarations

import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.google.gson.Gson
import com.roacult.domain.entities.Declaration
import com.roacult.domain.entities.GeoCoordination
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.usecases.declaration.DeclarationPage
import com.roacult.domain.usecases.profile.UserDeclarationPage
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.kero.team7.jstarter_domain.interactors.launchInteractor
import com.roacult.madinatic.R
import com.roacult.madinatic.base.BaseViewModel
import com.roacult.madinatic.base.State
import com.roacult.madinatic.ui.DeclarationDataSourceFactory
import com.roacult.madinatic.utils.DeclarationControllerCallback
import com.roacult.madinatic.utils.PagingCallback
import com.roacult.madinatic.utils.StringProvider
import com.roacult.madinatic.utils.getPagedListConfig
import com.roacult.madinatic.utils.states.*

class AllDeclarationViewModel(
    private val getDeclarationPage: UserDeclarationPage,
    private val gson: Gson,
    private val stringProvider: StringProvider
) : BaseViewModel<AllDeclarationState>(AllDeclarationState()) ,
    PagingCallback<DeclarationFailure, DeclarationPage>,
    DeclarationControllerCallback {

    private var lastTimeInvalidated: Long = System.currentTimeMillis()/1000

    private val dataSourceFactory: DeclarationDataSourceFactory by lazy {
        DeclarationDataSourceFactory(this)
    }

    private val liveData by lazy {
        LivePagedListBuilder(dataSourceFactory, getPagedListConfig()).build()
    }

    private val observer by lazy {
        Observer<PagedList<Declaration>> {
            setState { copy(declarations = it) }
        }
    }

    init { liveData.observeForever(observer) }

    override fun loadInitial(callback: (Either<DeclarationFailure,DeclarationPage>)->Unit){
        return loadPage(1,callback)
    }

    override fun loadPage(page: Int,callback: (Either<DeclarationFailure,DeclarationPage>)->Unit){
        setState { copy(declarationState = Loading()) }
        scope.launchInteractor(getDeclarationPage,page){
            callback(it)
            it.either({
                val msg =when(it){
                    DeclarationFailure.InternetConnection -> stringProvider.getStringFromResource(R.string.internet_prblm)
                    else -> stringProvider.getStringFromResource(R.string.unknown_error)
                }
                setState { copy(declarationState = Fail(it),errorMsg = Event(msg)) }
            },{
                setState { copy(declarationState = Success(None())) }
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
    
    override fun readMoreClicked(declaration : Declaration) {
        val json = gson.toJson(declaration)
        setState { copy(moreDetailsClickEvent = Event(json)) }
    }

    override fun gpsClicked(localisation : GeoCoordination) {
        setState { copy(gpsCoordination = Event(localisation)) }
    }

    override fun onCleared() {
        liveData.removeObserver(observer)
        super.onCleared()
    }
}

data class AllDeclarationState(
    val errorMsg : Event<String>? = null,
    val declarations : PagedList<Declaration>? = null,
    val declarationState: Async<None> = Uninitialized,
    val gpsCoordination: Event<GeoCoordination>? = null,
    val moreDetailsClickEvent : Event<String>? = null
) : State