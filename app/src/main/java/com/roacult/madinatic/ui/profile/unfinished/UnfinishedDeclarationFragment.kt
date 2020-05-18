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
import com.roacult.madinatic.ui.profile.unfinished.update_declaration.UpdateDeclarationFragment
import com.roacult.madinatic.utils.extensions.getGoogleMapUrl
import com.roacult.madinatic.utils.navigation.FragmentNavigation
import com.roacult.madinatic.utils.states.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class UnfinishedDeclarationFragment : FullScreenFragment<AllDeclarationsBinding>() {

    override val layutIdRes = R.layout.all_declarations

    private val viewModel: UnfinishedDeclarationViewModel by viewModel()
    private val controller by lazy {
        DeclarationController(viewModel,getString(R.string.all_dec_updated),getString(R.string.update))
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

        vm.observe(this){
            Timber.v("observing vm state $it")
            Timber.v("refresh --> ${it.refresh}")
            it.refresh?.getContentIfNotHandled()?.let(::refresh)
        }
    }

    private fun refresh(none: None) {
        Timber.v("refresh is consumed here ....")
        viewModel.invalidate(checkTime = false)
    }

    private fun moreDetailsEvent(json: String) {
        vm.navigate(FragmentNavigation(
            destinationClass = UpdateDeclarationFragment::class.java,
            fragmentArguments = Bundle().apply {
                putString(UpdateDeclarationFragment.DECLARATION,json)
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
                controller.viewState = ViewState.FAIL
                binding.refresh.isRefreshing = false
            }
            is Success -> {
                controller.viewState= ViewState.SUCCESS
                binding.refresh.isRefreshing = false
            }
        }
    }

    private fun initViews() {

        binding.toolbar.title = getString(R.string.update_dec)
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