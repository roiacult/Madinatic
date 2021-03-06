package com.roacult.madinatic.ui

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.roacult.domain.entities.Declaration
import com.roacult.madinatic.DeclarationBindingModel_
import com.roacult.madinatic.DeclarationErrorBindingModel_
import com.roacult.madinatic.EmptyStateBindingModel_
import com.roacult.madinatic.ProgressBindingModel_
import com.roacult.madinatic.utils.DeclarationControllerCallback
import com.roacult.madinatic.utils.states.ViewState

class DeclarationController(
    private val viewModel: DeclarationControllerCallback,
    private val msg: String,
    private val button: String
) : PagedListEpoxyController<Declaration>(){

    @AutoModel lateinit var progress : ProgressBindingModel_
    @AutoModel lateinit var errorView : DeclarationErrorBindingModel_
    @AutoModel lateinit var emptyView : EmptyStateBindingModel_

    var viewState = ViewState.LOADING
    set(value) {
        field = value
        requestModelBuild()
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        if(models.isEmpty()) {
            if(viewState==ViewState.FAIL) errorView.addTo(this)
            else if (viewState != ViewState.LOADING) emptyView.msg(msg).addTo(this)
        }
        super.addModels(models)
        if(viewState == ViewState.LOADING) progress.addTo(this)
    }

    override fun buildItemModel(currentPosition: Int, item: Declaration?): EpoxyModel<*> {
        return DeclarationBindingModel_()
            .viewModel(viewModel)
            .button(button)
            .declaration(item!!)
            .id(item.id)
    }
}