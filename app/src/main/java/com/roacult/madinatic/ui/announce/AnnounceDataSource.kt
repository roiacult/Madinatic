package com.roacult.madinatic.ui.announce

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.roacult.domain.entities.Announce
import com.roacult.domain.exceptions.AnnounceFailure
import com.roacult.domain.usecases.announce.AnnouncePage
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.madinatic.utils.PagingCallback

class AnnounceDataSourceFactory(
    private val viewModel: PagingCallback<AnnounceFailure, AnnouncePage>
) : DataSource.Factory<Int, Announce>() {

    val sourceLiveData = MutableLiveData<AnnounceDataSource>()

    override fun create(): DataSource<Int, Announce> {
        val dataSource = AnnounceDataSource(viewModel)
        sourceLiveData.postValue(dataSource)
        return dataSource
    }
}


class AnnounceDataSource (
    private val viewModel: PagingCallback<AnnounceFailure, AnnouncePage>
) : PageKeyedDataSource<Int, Announce>() {

    private var next: Int? = null
    private var previous: Int? = null


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Announce>
    ) {
        viewModel.loadInitial {
            if (it is Either.Right){
                val result = it.b
                next = result.next
                previous = result.previous
                callback.onResult(
                    result.announces,
                    0, result.count,
                    previous, next
                )
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Announce>) {

        if(next == null) {
            callback.onResult(emptyList(),null)
            return
        }
        viewModel.loadPage(next!!) {
            if (it is Either.Right){
                val result = it.b
                next = result.next
                previous = result.previous
                callback.onResult(result.announces, result.next)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Announce>) {

        if(previous == null) {
            callback.onResult(emptyList(),null)
            return
        }

        viewModel.loadPage(previous!!) {
            if(it is Either.Right){
                val result = it.b
                next = result.next
                previous = result.previous
                callback.onResult(result.announces,result.next)
            }
        }
    }
}