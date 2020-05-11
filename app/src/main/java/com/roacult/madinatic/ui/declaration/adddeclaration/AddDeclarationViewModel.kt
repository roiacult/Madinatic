package com.roacult.madinatic.ui.declaration.adddeclaration

import com.roacult.domain.entities.Declaration
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
import com.roacult.madinatic.utils.states.*

class AddDeclarationViewModel(
    private val getCategories : GetCategories,
    private val submitDeclaration: SubmitDeclaration,
    private val stringProvider: StringProvider
)  :BaseViewModel<AddDeclarationState>(AddDeclarationState()) {

    val images = arrayListOf("","","","","","")
    val files = ArrayList<String>()
    var adrress : Address? = null

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

    fun save(title: String, desc: String, categorie : CategorieView?) {

        if(title.isEmpty()){
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.title_empty))) }
            return
        }

        if(categorie == null || categorie.idc == HINT_VIEW_ID) {
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.categorie_empty))) }
            return
        }

        if(desc.isEmpty()){
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.desc_empty))) }
            return
        }

        if(images.isEmpty()) {
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.images_empty))) }
            return
        }

        if(adrress == null) {
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.location_empty))) }
            return
        }

        setState { copy(addDeclaration = Loading()) }
        scope.launchInteractor(submitDeclaration, SubmitionParams(
            declaration = Declaration("",
                title,desc,
                "",
                adrress!!.name,
                adrress!!.geoCord(),
                categorie.idc,
                null,
                null,
                null
            ), submitionFiles = files,
            submitionImages = images
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

}

data class AddDeclarationState(
    val errorMsg : Event<String>? = null,
    val addDeclaration : Async<None> = Uninitialized,
    val categories : Async<List<CategorieView>> = Uninitialized
) : State

data class Address(
    val name : String,
    val lat: Double,
    val long: Double
) {
    fun geoCord()= "[$lat,$long]"
}