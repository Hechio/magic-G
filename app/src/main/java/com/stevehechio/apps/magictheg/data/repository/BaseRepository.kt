package com.stevehechio.apps.magictheg.data.repository

import io.reactivex.disposables.Disposable

/**
 * Created by stevehechio on 11/29/21
 */
interface BaseRepository {
    fun addDisposable(disposable: Disposable)

    fun clear()
}