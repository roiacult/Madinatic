package com.roacult.madinatic.ui.declaration

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.roacult.domain.entities.Declaration
import com.roacult.kero.team7.jstarter_domain.functional.Either
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
) : PageKeyedDataSource<Int,Declaration>() {

    private var next: Int? = null
    private var previous: Int? = null


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Declaration>
    ) {
        runBlocking {
            val either = viewModel.loadInitial()
            if(either is Either.Left)
                callback.onRetryableError(either.a)
            else {
                val result = (either as Either.Right).b
                next = result.next
                previous = result.previous
                callback.onResult(result.declarations,previous,next)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Declaration>) {

        if(next == null) {
            callback.onResult(emptyList(),null)
            return
        }

        runBlocking {
            val either = viewModel.loadPage(next!!)
            if(either is Either.Left)
                callback.onRetryableError(either.a)
            else {
                val result = (either as Either.Right).b
                next = result.next
                previous = result.previous
                callback.onResult(result.declarations,result.next)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Declaration>) {

        if(previous == null) {
            callback.onResult(emptyList(),null)
            return
        }

        runBlocking {
            val either = viewModel.loadPage(previous!!)
            if(either is Either.Left)
                callback.onRetryableError(either.a)
            else {
                val result = (either as Either.Right).b
                next = result.next
                previous = result.previous
                callback.onResult(result.declarations,result.next)
            }
        }
    }
}