package com.roacult.madinatic.base

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding

abstract class FullScreenFragment<D : ViewDataBinding> : BaseFragment<D>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm.disableBottomNav()
    }
}