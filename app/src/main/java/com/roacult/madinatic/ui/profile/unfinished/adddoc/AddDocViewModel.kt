package com.roacult.madinatic.ui.profile.unfinished.adddoc

import com.google.gson.Gson
import com.roacult.domain.entities.Declaration
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.usecases.declaration.GetCategories
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.kero.team7.jstarter_domain.interactors.launchInteractor
import com.roacult.madinatic.R
import com.roacult.madinatic.base.BaseViewModel
import com.roacult.madinatic.base.State
import com.roacult.madinatic.ui.declaration.adddeclaration.CategorieView
import com.roacult.madinatic.ui.declaration.adddeclaration.toView
import com.roacult.madinatic.utils.AddDeclarationCallback
import com.roacult.madinatic.utils.StringProvider
import com.roacult.madinatic.utils.states.*

class AddDocViewModel(
    getCategories : GetCategories,
    private val gson : Gson,
    private val stringProvider: StringProvider
) : BaseViewModel<AddDocState>(AddDocState()) , AddDeclarationCallback {

    lateinit var declaration: Declaration

    init {
        scope.launchInteractor(getCategories, None()){
            it.either({
                val msg =when(it){
                    DeclarationFailure.InternetConnection -> stringProvider.getStringFromResource(R.string.internet_prblm)
                    else -> stringProvider.getStringFromResource(R.string.unknown_error)
                }
                setState { copy(errorMsg = Event(msg),categories = Fail(it)) }
            },{
                setState { copy(categories = Success(it.map { it.toView() })) }
            })
        }
    }

    fun saveData(json: String){
        declaration = gson.fromJson(json,Declaration::class.java)
    }

    fun save() {
        val docs = state.value!!.declarationDoc
        if(docs.isEmpty()) {
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.images_empty))) }
            return
        }

        setState { copy(addDeclaration = Loading()) }
    }

    override fun addDocClick() {
        val currentDoc = state.value!!.declarationDoc

        if(currentDoc.size >= 5){
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.max_doc_fail))) }
            return
        }

        setState { copy(addDocClickEvent = Event(None())) }
    }

    fun addDoc(filePath: String) {
        val list = state.value!!.declarationDoc.toMutableList()
        list.add(filePath)
        setState { copy(declarationDoc = list) }
    }
}

data class AddDocState(
    val errorMsg : Event<String>? = null,
    val categories : Async<List<CategorieView>> = Uninitialized,
    val addDocClickEvent : Event<None>? = null,
    val declarationDoc : List<String> = emptyList(),
    val addDeclaration : Async<None> = Uninitialized
) : State