package com.stevehechio.apps.magictheg.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.stevehechio.apps.magictheg.data.local.dao.SetsDao
import com.stevehechio.apps.magictheg.data.local.db.AppDatabase
import com.stevehechio.apps.magictheg.data.local.entities.RemoteKeys
import com.stevehechio.apps.magictheg.data.local.entities.SetsEntity
import com.stevehechio.apps.magictheg.data.remote.api.SetsApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Created by stevehechio on 11/27/21
 */

private const val SETS_STARTING_PAGE_INDEX = 1

@ExperimentalPagingApi
class SetsRemoteMediator (
    private val service: SetsApiService,
    private val appDatabase: AppDatabase
): RemoteMediator<Int, SetsEntity>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SetsEntity>
    ): MediatorResult {
        val page = when (loadType){
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: SETS_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
       try {
           val apiResponse = service.fetchSets(page,state.config.pageSize)
           val sets = apiResponse.cardList
           val endOfPaginationReached = sets.isEmpty()
           appDatabase.withTransaction {
               if (loadType == LoadType.REFRESH){
                   appDatabase.remoteKeysDao().clearRemoteKeys()
                   appDatabase.setsDao().deleteAll()
               }
               val prevKey = if (page == SETS_STARTING_PAGE_INDEX) null else page - 1
               val nextKey = if (endOfPaginationReached) null else page + 1
               val keys = sets.map {
                   RemoteKeys(setCode = it.code, prevKey = prevKey, nextKey = nextKey)
               }
               appDatabase.remoteKeysDao().insertAll(keys)
               appDatabase.setsDao().insertAll(sets)
           }
           return MediatorResult.Success(endOfPaginationReached)
       } catch (exception: IOException) {
           return MediatorResult.Error(exception)
       } catch (exception: HttpException) {
           return MediatorResult.Error(exception)
       }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, SetsEntity>):RemoteKeys?{
        return state.pages.lastOrNull(){it.data.isNotEmpty()}?.data?.lastOrNull()?.let {
            appDatabase.remoteKeysDao().remoteKeysSetCode(it.code)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, SetsEntity>): RemoteKeys? {
        return state.pages.firstOrNull(){it.data.isNotEmpty()}?.data?.firstOrNull()?.let {
            appDatabase.remoteKeysDao().remoteKeysSetCode(it.code)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, SetsEntity>)
    :RemoteKeys?{
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.code?.let { setCode ->
                appDatabase.remoteKeysDao().remoteKeysSetCode(setCode)
            }
        }
    }

}