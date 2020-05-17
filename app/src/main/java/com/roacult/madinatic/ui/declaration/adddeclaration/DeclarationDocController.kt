package com.roacult.madinatic.ui.declaration.adddeclaration

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import com.roacult.madinatic.AddDocHeaderBindingModel_
import com.roacult.madinatic.addDoc
import com.roacult.madinatic.addImage
import com.roacult.madinatic.utils.AddDeclarationCallback

class DeclarationDocController(
    private val viewModel: AddDeclarationCallback
) : TypedEpoxyController<List<String>>() {

    @AutoModel lateinit var header : AddDocHeaderBindingModel_

    override fun buildModels(data: List<String>?) {

        header.viewModel(viewModel).addTo(this)
        data?.forEach {

            if(it.endsWith("jpg") ||
                it.endsWith("png") ||
                it.endsWith("gif")) {

                addImage {
                    id(it)
                    image(it)
                }

            }
            else {
                addDoc {
                    id(it)
                    path(it)
                }
            }
        }
    }
}