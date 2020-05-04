package com.roacult.madinatic.ui.profile

import android.os.Bundle
import android.view.View
import com.roacult.madinatic.R
import com.roacult.madinatic.base.FullScreenFragment
import com.roacult.madinatic.databinding.UpdatePasswordBinding

class UpdatePasswordFragment : FullScreenFragment<UpdatePasswordBinding>(){

    override val layutIdRes = R.layout.update_password

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {

    }
}