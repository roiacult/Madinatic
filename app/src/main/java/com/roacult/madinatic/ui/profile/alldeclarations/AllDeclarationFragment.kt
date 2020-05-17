package com.roacult.madinatic.ui.profile.alldeclarations

import android.os.Bundle
import android.view.View
import com.roacult.madinatic.R
import com.roacult.madinatic.base.FullScreenFragment
import com.roacult.madinatic.databinding.AllDeclarationsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllDeclarationFragment : FullScreenFragment<AllDeclarationsBinding>() {

    override val layutIdRes = R.layout.all_declarations

    private val viewModel : AllDeclarationViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {

    }
}