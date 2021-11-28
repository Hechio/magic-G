package com.stevehechio.apps.magictheg.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.stevehechio.apps.magictheg.data.local.db.AppDatabase
import com.stevehechio.apps.magictheg.data.local.entities.CardsEntity
import com.stevehechio.apps.magictheg.data.remote.api.CardsApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by stevehechio on 11/28/21
 */
class CardsRepository @Inject constructor(
    val service: CardsApiService,
    val appDatabase: AppDatabase
) {

    @ExperimentalPagingApi
    fun getCardsResults(setCode: String): Flow<PagingData<CardsEntity>>{
        val pagingSourceFactory = {
            appDatabase.cardsDao().getAllCards(setCode)
        }
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE,enablePlaceholders = false),
            remoteMediator = CardsRemoteMediator(setCode,service,appDatabase),
            pagingSourceFactory = pagingSourceFactory
        ).flow

    }


    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}