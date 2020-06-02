package com.roacult.madinatic.ui.announce

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.roacult.domain.usecases.announce.AnnounceFilter
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.madinatic.R
import com.roacult.madinatic.base.BaseFragment
import com.roacult.madinatic.databinding.AnnonceBinding
import com.roacult.madinatic.utils.states.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AnnounceFragment : BaseFragment<AnnonceBinding>() {
    override val layutIdRes = R.layout.annonce

    private val viewModel: AnnounceViewModel by viewModel()
    private val controller by lazy { AnnounceController(viewModel,getString(R.string.empty_announce)) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        viewModel.observe(this) {
            it.errorMsg?.getContentIfNotHandled()?.let(::onError)
            it.announces?.let(controller::submitList)
            handleAnnounceState(it.announceState)
        }
    }

    private fun handleAnnounceState(announceState: Async<None>) {
        when(announceState) {
            is Loading -> controller.viewState = ViewState.LOADING
            is Fail<*, *> -> {
                if(announceState.getContentIfNotHandlled() != null)
                    viewModel.invalidate(checkTime = true)
                controller.viewState = ViewState.FAIL
                binding.refresh.isRefreshing = false
            }
            is Success -> {
                controller.viewState = ViewState.SUCCESS
                binding.refresh.isRefreshing = false
            }
        }
    }

    private fun initViews() {
        binding.controller = controller
        binding.refresh.setOnRefreshListener { viewModel.invalidate() }
        binding.filter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.filter = when(position){
                    1 -> AnnounceFilter.UPCOMING
                    2 -> AnnounceFilter.OLD
                    3 -> AnnounceFilter.ALL
                    else  -> AnnounceFilter.CURRENT
                }
                viewModel.invalidate()
            }
        }
    }
}