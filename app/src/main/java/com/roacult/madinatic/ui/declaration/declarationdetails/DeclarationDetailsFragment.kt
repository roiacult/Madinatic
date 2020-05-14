package com.roacult.madinatic.ui.declaration.declarationdetails

import android.os.Bundle
import android.view.View
import com.roacult.madinatic.R
import com.roacult.madinatic.base.FullScreenFragment
import com.roacult.madinatic.databinding.DeclarationDettailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeclarationDetailsFragment : FullScreenFragment<DeclarationDettailsBinding>() {

    override val layutIdRes = R.layout.declaration_dettails

    private val viewModel: DeclarationDetailsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.declaration = viewModel.formatJson(arguments!!.getString(DECLARATION,""))
    }

    companion object {
        const val DECLARATION = "com.roacult.madinatic:Declaration"
    }
}