package com.roacult.madinatic.ui.profile.unfinished

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.roacult.domain.entities.GeoCoordination
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.madinatic.R
import com.roacult.madinatic.base.FullScreenFragment
import com.roacult.madinatic.databinding.AllDeclarationsBinding
import com.roacult.madinatic.ui.DeclarationController
import com.roacult.madinatic.ui.declaration.declarationdetails.DeclarationDetailsFragment
import com.roacult.madinatic.ui.profile.unfinished.adddoc.AddDocFragment
import com.roacult.madinatic.utils.extensions.getGoogleMapUrl
import com.roacult.madinatic.utils.navigation.FragmentNavigation
import com.roacult.madinatic.utils.states.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UnfinishedDeclarationFragment : FullScreenFragment<AllDeclarationsBinding>() {

    override val layutIdRes = R.layout.all_declarations

    private val viewModel: UnfinishedDeclarationViewModel by viewModel()
    private val controller by lazy {
        DeclarationController(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        viewModel.observe(this) {
            it.errorMsg?.getContentIfNotHandled()?.let(::onError)
            it.declarations?.let(controller::submitList)
            it.gpsCoordination?.getContentIfNotHandled()?.let(::openMap)
            it.moreDetailsClickEvent?.getContentIfNotHandled()?.let(::moreDetailsEvent)
            handleDeclarationState(it.declarationState)
        }
    }

    private fun moreDetailsEvent(json: String) {
        vm.navigate(FragmentNavigation(
            destinationClass = AddDocFragment::class.java,
            fragmentArguments = Bundle().apply {
                putString(AddDocFragment.DECLARATION,json)
            }
        ))
    }

    private fun openMap(geoCoordination: GeoCoordination) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(geoCoordination.getGoogleMapUrl())))
    }

    private fun handleDeclarationState(declarationState: Async<None>) {
        when(declarationState) {
            is Loading -> controller.viewState = ViewState.LOADING
            is Fail<*, *> -> {
                if(declarationState.getContentIfNotHandlled() != null)
                    viewModel.invalidate(checkTime = true)
                controller.viewState = ViewState.EMPTY
                binding.refresh.isRefreshing = false
            }
            is Success -> {
                controller.viewState= ViewState.SUCCESS
                binding.refresh.isRefreshing = false
            }
        }
    }

    private fun initViews() {

        binding.toolbar.setNavigationIcon(R.drawable.back)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        val manager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.declarations.layoutManager = manager
        binding.declarations.setController(controller)
        binding.refresh.setOnRefreshListener {
            viewModel.invalidate()
        }
    }
}