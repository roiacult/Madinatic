package com.roacult.madinatic.ui.profile

import android.os.Build
import android.os.Bundle
import android.view.View
import com.roacult.madinatic.R
import com.roacult.madinatic.base.FullScreenFragment
import com.roacult.madinatic.databinding.ProfileChangeInfoBinding

class EditInfoFragment : FullScreenFragment<ProfileChangeInfoBinding>() {
    override val layutIdRes = R.layout.profile_change_info

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

    }

    private fun initViews() {

    }
}