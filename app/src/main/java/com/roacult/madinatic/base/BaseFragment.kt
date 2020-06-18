package com.roacult.madinatic.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.roacult.madinatic.ui.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

abstract class BaseFragment<D : ViewDataBinding> : Fragment() {

    protected lateinit var binding: D

    abstract val layutIdRes: Int
    private var mActivity: BaseActivity? = null

    protected val vm : MainViewModel by sharedViewModel()

    fun hideKeyboeard() {
        mActivity?.hideKeyboard()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm.showBottomNav()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            mActivity = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    fun showToast(message: String) {
        mActivity?.showToast(message)
    }

    fun onError(resId: Int) {
        mActivity?.onError(resId)
    }

    fun onError(message: String) {
        mActivity?.onError(message)
    }

    fun showMessage(message: String) {
        mActivity?.showMessage(message)
    }

    fun showMessage(resId: Int) {
        mActivity?.showMessage(resId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, layutIdRes, container, false)
        return binding.root
    }
}