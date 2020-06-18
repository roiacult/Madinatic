package com.roacult.domain.utils

abstract class SyncInteractor<P, R> {
    abstract operator fun invoke(executeParam: P): R
}