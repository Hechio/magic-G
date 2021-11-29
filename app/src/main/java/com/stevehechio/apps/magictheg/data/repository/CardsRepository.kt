package com.stevehechio.apps.magictheg.data.repository

import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.stevehechio.apps.magictheg.data.Resource
import com.stevehechio.apps.magictheg.data.local.db.AppDatabase
import com.stevehechio.apps.magictheg.data.local.entities.CardsBoosterEntity
import com.stevehechio.apps.magictheg.data.local.entities.CardsEntity
import com.stevehechio.apps.magictheg.data.remote.api.CardsApiService
import com.stevehechio.apps.magictheg.utils.NetworkUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by stevehechio on 11/28/21
 */
class CardsRepository @Inject constructor(
    val application: Context,
    val service: CardsApiService,
    val appDatabase: AppDatabase
): BaseRepository{
    private val compositeDisposable = CompositeDisposable()
    @ExperimentalPagingApi
   fun getCardsResults(setCode: String): Flow<PagingData<CardsEntity>> {

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE,enablePlaceholders = false),
            remoteMediator = CardsRemoteMediator(setCode,service,appDatabase),
            pagingSourceFactory = {
                    appDatabase.cardsDao().getAllCards("%${setCode.replace(' ','%')}%")
            }
        ).flow

    }

    fun fetchBoosterCards(setCode: String): Observable<Resource<List<CardsBoosterEntity>>> {
        return if (NetworkUtil.isNetworkAvailable(application)){
            getRemoteBooster(setCode)
        }else {
            getLocalBoosterData(setCode)
        }
    }

    private fun getLocalBoosterData(code: String): Observable<Resource<List<CardsBoosterEntity>>> {
        return Observable.create() { emiter ->
            emiter.onNext(Resource.Loading())
            val disposable = appDatabase.cardsDao().getBoosterCards(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    emiter.onNext( Resource.Success(it))
                    Log.i("movies res", "Success Execution! $it")
                }, {
                    emiter.onNext(Resource.Failure(it.localizedMessage))
                    Log.i("movies res", "Success Execution! $it")
                })
            addDisposable(disposable)
        }
    }

    private fun getRemoteBooster(code: String): Observable<Resource<List<CardsBoosterEntity>>> {
        return Observable.create() { emiter ->
            emiter.onNext(Resource.Loading())
            val disposable = service.fetchBoosterCards(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    emiter.onNext( Resource.Success(it.cardList))
                    Log.i("movies res", "Success Execution! $it")
                }, {
                    emiter.onNext(Resource.Failure(it.localizedMessage))
                    Log.i("movies res", "Success Execution! $it")
                })
            addDisposable(disposable)
        }
    }


    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }

    override fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun clear() {
        compositeDisposable.clear()
    }

}