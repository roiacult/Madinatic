package com.roacult.madinatic.ui.declaration.declarationdetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.google.android.material.appbar.AppBarLayout
import com.roacult.domain.entities.AttachmentType
import com.roacult.madinatic.R
import com.roacult.madinatic.base.FullScreenFragment
import com.roacult.madinatic.databinding.DeclarationDettailsBinding
import com.roacult.madinatic.utils.AppBarStateChangeListener
import com.roacult.madinatic.utils.extensions.getGoogleMapUrl
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeclarationDetailsFragment : FullScreenFragment<DeclarationDettailsBinding>() {

    override val layutIdRes = R.layout.declaration_dettails

    private val appBarStateListener = object : AppBarStateChangeListener() {
        override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
            when (state) {
                State.COLLAPSED -> {
                    setToolbarTitle(false)
                }
                State.EXPANDED -> {
                    setToolbarTitle(true)
                }
                State.IDLE -> {
                    setToolbarTitle(true)
                }
            }
        }
    }

    private val viewModel: DeclarationDetailsViewModel by viewModel()
    private val slideShowAdapter by lazy {
        DeclarationSlideShowAdapter(context!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun setToolbarTitle(show: Boolean) {
        if (show) {
            binding.collapse.title = ""
            return
        }
        viewModel.withState {
            binding.collapse.title = viewModel.declaration.title
        }
    }

    private fun initViews() {
        viewModel.formatJson(arguments!!.getString(DECLARATION,""))
        binding.declaration = viewModel.declaration
        slideShowAdapter.setImages(viewModel.declaration.attachment.filter { it.filetype==AttachmentType.IMAGE }.map {
            it.src
        })
        binding.toolbar.setNavigationIcon(R.drawable.back)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        binding.appbar.addOnOffsetChangedListener(appBarStateListener)
        binding.slideShow.adapter = slideShowAdapter
        binding.indicator.setViewPager(binding.slideShow)
        binding.floatingActionButton2.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.declaration.coordination.getGoogleMapUrl())))
        }
    }

    companion object {
        const val DECLARATION = "com.roacult.madinatic:Declaration"
    }
}