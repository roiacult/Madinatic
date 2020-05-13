package com.roacult.madinatic.ui.declaration

import androidx.paging.PagedList
import com.roacult.domain.entities.Declaration
import com.roacult.domain.entities.GeoCoordination
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.usecases.declaration.DeclarationPage
import com.roacult.domain.usecases.declaration.GetDeclarationPage
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.kero.team7.jstarter_domain.interactors.launchInteractor
import com.roacult.madinatic.base.BaseViewModel
import com.roacult.madinatic.base.State
import com.roacult.madinatic.utils.states.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DeclarationViewModel(
    private val getDeclarationPage: GetDeclarationPage
) : BaseViewModel<DeclarationViewState>(DeclarationViewState()) {

   suspend fun loadInitial() : Either<Throwable,DeclarationPage> {
       return loadPage(1)
   }

    suspend fun loadPage(page: Int) : Either<Throwable,DeclarationPage>  = suspendCoroutine {coroutine->
        setState { copy(declarationState = Loading()) }
        scope.launchInteractor(getDeclarationPage,page){
            it.either({
                //TODO handle all type of failures here

                setState { copy(declarationState = Fail(it)) }
                if(it == DeclarationFailure.PageNotFoundFailure)
                    coroutine.resume(Either.Right(DeclarationPage(0,1, emptyList())))
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
}

data class DeclarationViewState(
    val errorMsg : Event<String>? = null,
    val declarationState: Async<None> = Uninitialized,
    val declarations : PagedList<Declaration>? = null
) : State