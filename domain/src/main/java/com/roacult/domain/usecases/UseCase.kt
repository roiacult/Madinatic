package com.roacult.domain.usecases

import com.roacult.kero.team7.jstarter_domain.exception.Failure
import com.roacult.kero.team7.jstarter_domain.functional.AppRxSchedulers
import com.roacult.kero.team7.jstarter_domain.functional.Either
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor

abstract class ObservableEitherInteractor<R, in P, F : Failure>(private val schedulers: AppRxSchedulers) {
    protected abstract fun buildObservable(p: P): Observable<Either<F, R>>

    open fun observe(p: P, handler: (Either<F, R>) -> Unit): Disposable {
        return buildObservable(p).subscribeOn(schedulers.io).observeOn(schedulers.main)
                .subscribe(handler)
    }
}

abstract class PagingInteractor<R, in P> (private val schedulers: AppRxSchedulers) {

    private val publisher: PublishProcessor<R> = PublishProcessor.create()
    private var disposables = CompositeDisposable()

    abstract fun buildObservableForPage(page: P): Observable<R>

    fun fetshPage(page: P) {
        val disposable = buildObservableForPage(page)
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.main)
            .subscribe(publisher::onNext, publisher::onError)
        disposables.add(disposable)
    }

    fun observe(onNext: (R) -> Unit, onError: (Throwable) -> Unit): Disposable {

        return publisher.toObservable()
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.main)
            .subscribe(onNext, onError)
    }

    fun dispose() {
        if (! disposables.isDisposed)
            disposables.dispose()
    }
}

abstract class PagingEitherInteractor<R, in P, F : Failure> (private val schedulers: AppRxSchedulers) {

    private val publisher: PublishProcessor<Either<F, R>> = PublishProcessor.create()
    private var disposables = CompositeDisposable()

    abstract fun buildObservableForPage(param: P): Observable<Either<F, R>>

    fun fetshPage(param: P) {
        val disposable = buildObservableForPage(param)
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.main)
            .subscribe(publisher::onNext, publisher::onError)
        disposables.add(disposable)
    }

    fun observe(handler: (Either<F, R>) -> Unit): Disposable {

        return publisher.toObservable()
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.main)
            .subscribe(handler)
    }

    fun dispose() {
        if (! disposables.isDisposed)
            disposables.dispose()
    }
}