package com.stevehechio.apps.magictheg.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.stevehechio.apps.magictheg.data.local.db.AppDatabase
import com.stevehechio.apps.magictheg.data.local.entities.SetsEntity
import com.stevehechio.apps.magictheg.data.remote.api.SetsApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by stevehechio on 11/27/21
 */
class SetsRepository @Inject constructor(
    val service: SetsApiService,
    val appDatabase: AppDatabase
) {
    @ExperimentalPagingApi
    fun getSetResults(): Flow<PagingData<SetsEntity>> {
        val pagingSourceFactory = {

            appDatabase.setsDao().getAllSets()

        }
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = SetsRemoteMediator(service, appDatabase),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
    companion object {
        const val NETWORK_PAGE_SIZE = 40
    }
}