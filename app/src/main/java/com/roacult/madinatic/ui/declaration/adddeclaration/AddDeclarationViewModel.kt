package com.roacult.madinatic.ui.declaration.adddeclaration

import com.roacult.domain.entities.Categorie
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.usecases.declaration.GetCategories
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.kero.team7.jstarter_domain.interactors.launchInteractor
import com.roacult.madinatic.R
import com.roacult.madinatic.base.BaseViewModel
import com.roacult.madinatic.base.State
import com.roacult.madinatic.utils.StringProvider
import com.roacult.madinatic.utils.states.*

class AddDeclarationViewModel(
    private val getCategories : GetCategories,
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

    fun save(title: String, desc: String, categorie : CategorieView) {

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
)