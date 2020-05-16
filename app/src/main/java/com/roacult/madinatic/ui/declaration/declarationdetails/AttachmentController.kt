package com.roacult.madinatic.ui.declaration.declarationdetails

import com.airbnb.epoxy.TypedEpoxyController
import com.roacult.domain.entities.Attachment
import com.roacult.madinatic.attachment

class DeclarationDetailsController(
    private val viewModel: DeclarationDetailsViewModel
) : TypedEpoxyController<List<Attachment>>() {

    override fun buildModels(data: List<Attachment>?) {
        data?.forEach {
            attachment {
                id(it.attachmentId)
                attachment(it)
                viewModel(viewModel)
            }
        }
    }
}