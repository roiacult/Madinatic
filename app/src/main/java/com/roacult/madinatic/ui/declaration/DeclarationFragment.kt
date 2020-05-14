package com.roacult.madinatic.ui.declaration

import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.roacult.madinatic.R
import com.roacult.madinatic.base.BaseFragment
import com.roacult.madinatic.databinding.DeclarationBinding
import com.roacult.madinatic.ui.declaration.adddeclaration.AddDeclarationFragmentV2
import com.roacult.madinatic.utils.navigation.FragmentNavigation
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeclarationFragment : BaseFragment<DeclarationBinding>() {
    override val layutIdRes = R.layout.declaration

    private val viewModel: DeclarationViewModel by viewModel()
    private val controller by lazy {
        DeclarationController(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        viewModel.observe(this) {
            it.errorMsg?.getContentIfNotHandled()?.let(::onError)
            it.declarations?.let(controller::submitList)
            //TODO observe declaration state HERE
        }
    }

    private fun initViews() {
        binding.toolbar.menu[0].setOnMenuItemClickListener {
            vm.navigate(FragmentNavigation(destinationClass = AddDeclarationFragmentV2::class.java))
            true
        }

        binding.toolbar.title = getString(R.string.app_name)
        val manager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.declarations.layoutManager = manager
        binding.declarations.setController(controller)
    }
}