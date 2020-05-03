package com.roacult.madinatic.ui.profile

import android.os.Bundle
import android.view.View
import com.roacult.madinatic.R
import com.roacult.madinatic.base.FullScreenFragment
import com.roacult.madinatic.databinding.ProfileChangeInfoBinding
import com.roacult.madinatic.utils.extensions.toUser
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditInfoFragment : FullScreenFragment<ProfileChangeInfoBinding>() {
    override val layutIdRes = R.layout.profile_change_info

    private val viewModel : EditInfoViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

    }

    private fun initViews() {
        saveData()
    }

    private fun saveData() {
        val user = arguments!!.toUser()
        viewModel.setData(user)
    }
}