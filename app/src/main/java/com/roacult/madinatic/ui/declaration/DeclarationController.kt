package com.roacult.madinatic.ui.declaration

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.roacult.domain.entities.Declaration
import com.roacult.madinatic.DeclarationBindingModel_
import com.roacult.madinatic.ProgressBindingModel_
import com.roacult.madinatic.utils.states.ViewState

class DeclarationController(
    private val viewModel: DeclarationViewModel
) : PagedListEpoxyController<Declaration>(){

    @AutoModel lateinit var progress : ProgressBindingModel_

    var viewState = ViewState.LOADING
    set(value) {
        field = value
        requestModelBuild()
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        super.addModels(models)
        if(viewState == ViewState.LOADING) progress.addTo(this)
    }

    override fun buildItemModel(currentPosition: Int, item: Declaration?): EpoxyModel<*> {
        return DeclarationBindingModel_()
            .viewModel(viewModel)
            .declaration(item!!)
            .id(item.id)
    }
}