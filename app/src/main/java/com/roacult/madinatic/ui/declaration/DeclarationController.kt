package com.roacult.madinatic.ui.declaration

import com.airbnb.epoxy.TypedEpoxyController
import com.roacult.domain.entities.Declaration
import com.roacult.madinatic.declaration

class DeclarationController(
    private val viewModel: DeclarationViewModel
) : TypedEpoxyController<List<Declaration>>(){

    override fun buildModels(data: List<Declaration>?) {
        data?.forEach {
            declaration {
                id(it.id)
                declaration(it)
                viewModel(viewModel)
            }
        }
    }
}