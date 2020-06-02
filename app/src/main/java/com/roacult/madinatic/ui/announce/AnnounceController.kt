package com.roacult.madinatic.ui.announce

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.roacult.domain.entities.Announce
import com.roacult.madinatic.*
import com.roacult.madinatic.utils.states.ViewState

class AnnounceController(
    private val viewModel: AnnounceViewModel,
    private val msg: String
) : PagedListEpoxyController<Announce>() {

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
            if(viewState== ViewState.FAIL) errorView.addTo(this)
            else if (viewState != ViewState.LOADING) emptyView.msg(msg).addTo(this)
        }
        super.addModels(models)
        if(viewState == ViewState.LOADING) progress.addTo(this)
    }

    override fun buildItemModel(currentPosition: Int, item: Announce?): EpoxyModel<*> {
        return AnnounceBindingModel_()
            .id(item!!.aid)
            .viewModel(viewModel)
            .announce(item)
    }
}