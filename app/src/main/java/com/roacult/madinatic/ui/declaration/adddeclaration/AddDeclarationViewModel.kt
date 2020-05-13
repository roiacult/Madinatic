package com.roacult.madinatic.ui.declaration.adddeclaration

import com.roacult.domain.entities.Declaration
import com.roacult.domain.entities.DeclarationState
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.usecases.declaration.GetCategories
import com.roacult.domain.usecases.declaration.SubmitDeclaration
import com.roacult.domain.usecases.declaration.SubmitionParams
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.kero.team7.jstarter_domain.interactors.launchInteractor
import com.roacult.madinatic.R
import com.roacult.madinatic.base.BaseViewModel
import com.roacult.madinatic.base.State
import com.roacult.madinatic.utils.StringProvider
import com.roacult.madinatic.utils.extensions.toAttachment
import com.roacult.madinatic.utils.states.*

class AddDeclarationViewModel(
    getCategories : GetCategories,
    private val submitDeclaration: SubmitDeclaration,
    private val stringProvider: StringProvider
)  :BaseViewModel<AddDeclarationState>(AddDeclarationState()) {

    var adrress : Address? = null
    var title = ""
    var desc = ""
    var categorie : CategorieView? = null

    init {
        scope.launchInteractor(getCategories,None()){
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

    fun save() {

        if(title.isEmpty()){
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.title_empty))) }
            return
        }

        if(categorie == null || categorie!!.idc == HINT_VIEW_ID) {
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.categorie_empty))) }
            return
        }

        if(desc.isEmpty()){
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.desc_empty))) }
            return
        }

        val docs = state.value!!.declarationDoc
        if(docs.isEmpty()) {
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.images_empty))) }
            return
        }

        if(adrress == null) {
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.location_empty))) }
            return
        }

        setState { copy(addDeclaration = Loading()) }
        scope.launchInteractor(submitDeclaration, Declaration("", title,desc, "", adrress!!.name,
            adrress!!.geoCord(),
            categorie!!.idc,
            DeclarationState.NOT_VALIDATED,
            null,
            null,
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

    fun addDocClick() {
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

data class AddDeclarationState(
    val errorMsg : Event<String>? = null,
    val addDeclaration : Async<None> = Uninitialized,
    val categories : Async<List<CategorieView>> = Uninitialized,
    val addDocClickEvent : Event<None>? = null,
    val declarationDoc : List<String> = emptyList()
) : State

data class Address(
    val name : String,
    val lat: Double,
    val long: Double
) {
    fun geoCord()= "[$lat,$long]"
}