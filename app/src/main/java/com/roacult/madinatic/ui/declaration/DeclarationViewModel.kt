package com.roacult.madinatic.ui.declaration

import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.roacult.domain.entities.Declaration
import com.roacult.domain.entities.GeoCoordination
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.usecases.declaration.DeclarationPage
import com.roacult.domain.usecases.declaration.GetDeclarationPage
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.kero.team7.jstarter_domain.interactors.launchInteractor
import com.roacult.madinatic.R
import com.roacult.madinatic.base.BaseViewModel
import com.roacult.madinatic.base.State
import com.roacult.madinatic.utils.StringProvider
import com.roacult.madinatic.utils.getPagedListConfig
import com.roacult.madinatic.utils.states.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DeclarationViewModel(
    private val getDeclarationPage: GetDeclarationPage,
    private val stringProvider: StringProvider
) : BaseViewModel<DeclarationViewState>(DeclarationViewState()) {

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

   suspend fun loadInitial() : Either<Throwable,DeclarationPage> {
       return loadPage(1)
   }

    suspend fun loadPage(page: Int) : Either<Throwable,DeclarationPage>  = suspendCoroutine {coroutine->
        setState { copy(declarationState = Loading()) }
        scope.launchInteractor(getDeclarationPage,page){
            it.either({
                //TODO handle all type of failures here

                val msg =when(it){
                    DeclarationFailure.InternetConnection -> stringProvider.getStringFromResource(R.string.internet_prblm)
                    else -> stringProvider.getStringFromResource(R.string.unknown_error)
                }
                setState { copy(declarationState = Fail(it),errorMsg = Event(msg)) }
                if(it == DeclarationFailure.PageNotFoundFailure)
                    coroutine.resume(Either.Right(DeclarationPage(0,1,null, emptyList())))
                else coroutine.resume(Either.Left(Throwable()))
            },{
                setState { copy(declarationState = Success(None())) }
                coroutine.resume(Either.Right(it))
            })
        }
    }

    fun readMoreClicked(declaration : Declaration) {
        //TODO
    }

    fun gpsClicked(localisation : GeoCoordination) {
        //TODO
    }

    override fun onCleared() {
        liveData.removeObserver(observer)
        super.onCleared()
    }
}

data class DeclarationViewState(
    val errorMsg : Event<String>? = null,
    val declarationState: Async<None> = Uninitialized,
    val declarations : PagedList<Declaration>? = null
) : State