package com.roacult.madinatic.ui.profile

import android.os.Bundle
import android.view.View
import com.roacult.madinatic.R
import com.roacult.madinatic.base.BaseFragment
import com.roacult.madinatic.databinding.ProfileBinding
import com.roacult.madinatic.utils.states.ViewState

class ProfileFragment : BaseFragment<ProfileBinding>() {

    override val layutIdRes = R.layout.profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.state = ViewState.SUCCESS
    }
}