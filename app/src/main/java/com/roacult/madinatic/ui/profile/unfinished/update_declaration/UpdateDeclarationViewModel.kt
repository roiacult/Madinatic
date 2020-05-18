package com.roacult.madinatic.ui.profile.unfinished.update_declaration

import com.google.gson.Gson
import com.roacult.domain.entities.Declaration
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.usecases.declaration.GetCategories
import com.roacult.domain.usecases.profile.UpdateDeclaration
import com.roacult.domain.usecases.profile.AddDocumentsParams
import com.roacult.domain.usecases.profile.DeclarationUpdate
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.kero.team7.jstarter_domain.interactors.launchInteractor
import com.roacult.madinatic.R
import com.roacult.madinatic.base.BaseViewModel
import com.roacult.madinatic.base.State
import com.roacult.madinatic.ui.declaration.adddeclaration.CategorieView
import com.roacult.madinatic.ui.declaration.adddeclaration.HINT_VIEW_ID
import com.roacult.madinatic.ui.declaration.adddeclaration.toView
import com.roacult.madinatic.utils.AddDeclarationCallback
import com.roacult.madinatic.utils.StringProvider
import com.roacult.madinatic.utils.extensions.toAttachment
import com.roacult.madinatic.utils.states.*
import timber.log.Timber

class UpdateDeclarationViewModel(
    getCategories : GetCategories,
    private val updateDeclaration : UpdateDeclaration,
    private val gson : Gson,
    private val stringProvider: StringProvider
) : BaseViewModel<AddDocState>(AddDocState()) , AddDeclarationCallback {

    lateinit var declaration: Declaration
    private var firstTime = true

    var title = ""
    var description = ""
    var address = Address("",0.0,0.0)
    var categorie: String =""

    init {
        scope.launchInteractor(getCategories, None()){
            it.either({
                val msg =when(it){
                    DeclarationFailure.InternetConnection -> stringProvider.getStringFromResource(R.string.internet_prblm)
                    else -> stringProvider.getStringFromResource(R.string.unknown_error)
                }
                setState { copy(errorMsg = Event(msg),categories = Fail(it)) }
            },{
                Timber.v("categories Success $it")
                setState { copy(categories = Success(it.map { it.toView() })) }
            })
        }
    }

    fun saveData(json: String){
        if(!firstTime) return

        firstTime = false
        declaration = gson.fromJson(json,Declaration::class.java)

        title = declaration.title
        description = declaration.desc
        address = Address(declaration.address,declaration.coordination.lat,declaration.coordination.long)
        categorie = declaration.categorie

    }

    fun save() {

        if(title.isEmpty()){
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.title_empty))) }
            return
        }

        if(categorie == HINT_VIEW_ID) {
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.categorie_empty))) }
            return
        }

        if(description.isEmpty()){
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.desc_empty))) }
            return
        }

        val docs = state.value!!.declarationDoc

        setState { copy(addDeclaration = Loading()) }
        scope.launchInteractor(updateDeclaration, AddDocumentsParams(
            DeclarationUpdate(declaration.id,title,categorie,description,address.name,address.lat,address.long),
            docs.map { it.toAttachment() }
        )){
            it.either({
                val msg =when(it){
                    DeclarationFailure.InternetConnection -> stringProvider.getStringFromResource(R.string.internet_prblm)
                    else -> stringProvider.getStringFromResource(R.string.unknown_error)
                }
                setState { copy(errorMsg = Event(msg),addDeclaration = Fail(it)) }
            },{
                setState { copy(addDeclaration = Success(it)) }
            })
        }
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

data class Address(
    val name: String,
    val lat: Double,
    val long: Double
)