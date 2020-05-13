package com.roacult.madinatic.ui.declaration

import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import com.roacult.domain.entities.Declaration
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.madinatic.utils.PAGE_SIZE
import kotlinx.coroutines.runBlocking

class DeclarationDataSourceFactory(
    private val viewModel: DeclarationViewModel
) : DataSource.Factory<Int,Declaration>() {
    override fun create(): DataSource<Int, Declaration> {
        return DeclarationDataSource(viewModel)
    }
}

class DeclarationDataSource(
    private val viewModel: DeclarationViewModel
) : PositionalDataSource<Declaration>() {

    private var page = 1


    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<Declaration>
    ) {
        runBlocking {
            val either = viewModel.loadInitial()
            if(either is Either.Left)
                callback.onRetryableError(either.a)
            else {
                val result = (either as Either.Right).b
                page = result.next
                callback.onResult(result.declarations,result.next*PAGE_SIZE,result.count)
            }
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Declaration>) {
        runBlocking {
            val either = viewModel.loadPage(page)
            if(either is Either.Left)
                callback.onRetryableError(either.a)
            else {
                val result = (either as Either.Right).b
                page = result.next
                callback.onResult(result.declarations)
            }
        }
    }
}