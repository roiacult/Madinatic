package com.roacult.madinatic.ui

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.roacult.domain.entities.Declaration
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.usecases.declaration.DeclarationPage
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.madinatic.utils.PagingCallback

class DeclarationDataSourceFactory(
    private val viewModel: PagingCallback<DeclarationFailure,DeclarationPage>
) : DataSource.Factory<Int,Declaration>() {

    val sourceLiveData = MutableLiveData<DeclarationDataSource>()

    override fun create(): DataSource<Int, Declaration> {
        val dataSource =
            DeclarationDataSource(viewModel)
        sourceLiveData.postValue(dataSource)
        return dataSource
    }
}

class DeclarationDataSource(
    private val viewModel: PagingCallback<DeclarationFailure,DeclarationPage>
) : PageKeyedDataSource<Int,Declaration>() {

    private var next: Int? = null
    private var previous: Int? = null


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Declaration>
    ) {
        viewModel.loadInitial {
            if (it is Either.Right){
                val result = it.b
                next = result.next
                previous = result.previous
                callback.onResult(
                    result.declarations,
                    0, result.count,
                    previous, next
                )
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Declaration>) {

        if(next == null) {
            callback.onResult(emptyList(),null)
            return
        }
         viewModel.loadPage(next!!) {
             if (it is Either.Right){
                 val result = it.b
                 next = result.next
                 previous = result.previous
                 callback.onResult(result.declarations, result.next)
             }
         }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Declaration>) {

        if(previous == null) {
            callback.onResult(emptyList(),null)
            return
        }

        viewModel.loadPage(previous!!) {
            if(it is Either.Right){
                val result = it.b
                next = result.next
                previous = result.previous
                callback.onResult(result.declarations,result.next)
            }
        }
    }
}