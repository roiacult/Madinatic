package com.roacult.madinatic.ui.declaration

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.roacult.domain.entities.Declaration
import com.roacult.madinatic.DeclarationBindingModel_

class DeclarationController(
    private val viewModel: DeclarationViewModel
) : PagedListEpoxyController<Declaration>(){

    override fun buildItemModel(currentPosition: Int, item: Declaration?): EpoxyModel<*> {
        return DeclarationBindingModel_()
            .viewModel(viewModel)
            .declaration(item!!)
            .id(item.id)
    }
}