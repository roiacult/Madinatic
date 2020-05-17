package com.roacult.madinatic.utils

import com.roacult.kero.team7.jstarter_domain.functional.Either

interface PagingCallback<F,R> {

    fun loadPage(page: Int,callback: (Either<F, R>)->Unit)

    fun loadInitial(callback: (Either<F,R>)->Unit)
}