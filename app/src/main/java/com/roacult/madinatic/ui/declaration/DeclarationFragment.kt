package com.roacult.madinatic.ui.declaration

import android.os.Bundle
import android.view.View
import androidx.core.view.get
import com.roacult.madinatic.R
import com.roacult.madinatic.base.BaseFragment
import com.roacult.madinatic.databinding.DeclarationBinding
import com.roacult.madinatic.ui.declaration.adddeclaration.AddDeclaration
import com.roacult.madinatic.utils.navigation.FragmentNavigation

class DeclarationFragment : BaseFragment<DeclarationBinding>() {
    override val layutIdRes = R.layout.declaration

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.toolbar.menu[0].setOnMenuItemClickListener {
            vm.navigate(FragmentNavigation(destinationClass = AddDeclaration::class.java))
            true
        }
    }
}