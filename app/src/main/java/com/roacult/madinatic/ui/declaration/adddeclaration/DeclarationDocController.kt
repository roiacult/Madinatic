package com.roacult.madinatic.ui.declaration.adddeclaration

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import com.roacult.madinatic.AddDocHeaderBindingModel_
import com.roacult.madinatic.addDoc

class DeclarationDocController(
    private val viewModel: AddDeclarationViewModel
) : TypedEpoxyController<List<String>>() {

    @AutoModel lateinit var header : AddDocHeaderBindingModel_

    override fun buildModels(data: List<String>?) {

        header.viewModel(viewModel).addTo(this)

        data?.forEach {
            addDoc {
                id(it)
                image(it)
            }
        }
    }
}