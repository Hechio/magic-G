package com.stevehechio.apps.magictheg.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.stevehechio.apps.magictheg.data.local.db.AppDatabase
import com.stevehechio.apps.magictheg.data.local.entities.CardsEntity
import com.stevehechio.apps.magictheg.data.local.entities.CardsRemoteKeys
import com.stevehechio.apps.magictheg.data.local.entities.RemoteKeys
import com.stevehechio.apps.magictheg.data.remote.api.CardsApiService
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by stevehechio on 11/28/21
 */

private const val CARDS_STARTING_PAGE_INDEX = 1

@ExperimentalPagingApi
class CardsRemoteMediator(
    private val setCode: String,
    private val service: CardsApiService,
    private val appDatabase: AppDatabase
): RemoteMediator<Int, CardsEntity>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CardsEntity>
    ): MediatorResult {
        val page = when (loadType){
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: CARDS_STARTING_PAGE_INDEX
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
            val apiResponse = service.fetchCards(setCode,page,state.config.pageSize)
            val cards = apiResponse.cardList
            val endOfPaginationReached = cards.isEmpty()
            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH){
                    appDatabase.cardsRemoteKeysDao().clearRemoteKeys()
                    appDatabase.cardsDao().deleteAll()
                }
                val prevKey = if (page == CARDS_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = cards.map {
                    CardsRemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                appDatabase.cardsRemoteKeysDao().insertAll(keys)
                appDatabase.cardsDao().insertAll(cards)
            }
            return MediatorResult.Success(endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }

    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, CardsEntity>): CardsRemoteKeys?{
        return state.pages.lastOrNull(){it.data.isNotEmpty()}?.data?.lastOrNull()?.let {
            appDatabase.cardsRemoteKeysDao().remoteKeysSetId(it.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, CardsEntity>): CardsRemoteKeys? {
        return state.pages.firstOrNull(){it.data.isNotEmpty()}?.data?.firstOrNull()?.let {
            appDatabase.cardsRemoteKeysDao().remoteKeysSetId(it.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, CardsEntity>)
            : CardsRemoteKeys?{
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                appDatabase.cardsRemoteKeysDao().remoteKeysSetId(id)
            }
        }
    }
}