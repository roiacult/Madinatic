package com.roacult.madinatic.ui.profile.unfinished.update_declaration

import com.google.gson.Gson
import com.roacult.domain.entities.Declaration
import com.roacult.domain.entities.DeclarationState
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.usecases.declaration.DeleteDeclaration
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

class UpdateDeclarationViewModel(
    getCategories : GetCategories,
    private val updateDeclaration : UpdateDeclaration,
    private val deleteDeclaration: DeleteDeclaration,
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
                val newList = it.map { it.toView() }.toMutableList()
                newList.add(0, CategorieView(
                    HINT_VIEW_ID,
                    stringProvider.getStringFromResource(R.string.type_dec),
                    ""
                ))
                setState { copy(categories = Success(newList)) }
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

    fun save(declarationState: DeclarationState) {

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
            DeclarationUpdate(declaration.id,title,categorie,description,declarationState,address.name,address.lat,address.long),
            docs.map { it.toAttachment() }
        )){
            it.either({
                val msg =when(it){
                    DeclarationFailure.InternetConnection -> stringProvider.getStringFromResource(R.string.internet_prblm)
                    else -> stringProvider.getStringFromResource(R.string.unknown_error)
                }
                setState { copy(errorMsg = Event(msg),addDeclaration = Fail(it)) }
            },{
                setState { copy(addDeclaration = Success(OperationType.UPDATING)) }
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

    override fun onDocLongClick(doc: String): Boolean {
        if(doc.startsWith("http")) return false
        setState { copy(deleteDocConfirmation = Event(doc)) }
        return false
    }

    fun addDoc(filePath: String) {
        val list = state.value!!.declarationDoc.toMutableList()
        list.add(filePath)
        setState { copy(declarationDoc = list) }
    }

    fun deleteDoc(doc: String){
        val list = state.value!!.declarationDoc.toMutableList()
        list.remove(doc)
        setState { copy(declarationDoc = list) }
    }

    fun deleteDeclaration() {

        setState { copy(addDeclaration = Loading()) }
        scope.launchInteractor(deleteDeclaration,declaration.id){
            it.either({
                val msg =when(it){
                    DeclarationFailure.InternetConnection -> stringProvider.getStringFromResource(R.string.internet_prblm)
                    else -> stringProvider.getStringFromResource(R.string.unknown_error)
                }
                setState { copy(errorMsg = Event(msg),addDeclaration = Fail(it)) }
            },{
                setState { copy(addDeclaration = Success(OperationType.DELETE)) }
            })
        }
    }
}

data class AddDocState(
    val errorMsg : Event<String>? = null,
    val categories : Async<List<CategorieView>> = Uninitialized,
    val addDocClickEvent : Event<None>? = null,
    val declarationDoc : List<String> = emptyList(),
    val deleteDocConfirmation: Event<String>? = null,
    val addDeclaration : Async<OperationType> = Uninitialized
) : State

data class Address(
    val name: String,
    val lat: Double,
    val long: Double
)

enum class OperationType{
    UPDATING,DELETE
}