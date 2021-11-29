package com.stevehechio.apps.magictheg.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stevehechio.apps.magictheg.data.local.entities.CardsBoosterEntity
import com.stevehechio.apps.magictheg.data.local.entities.CardsEntity
import io.reactivex.Observable

/**
 * Created by stevehechio on 11/27/21
 */

@Dao
interface CardsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cardsEntity: CardsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cardsEntities: List<CardsEntity>)

    @Query("DELETE FROM magic_cards_table")
    suspend fun deleteAll()

    /**Instead of returning a List<CardsEntity>, return PagingSource<Int, CardsEntity>.
    That way, the cads table becomes the source of data for Paging. */

    @Query("SELECT * FROM MAGIC_CARDS_TABLE WHERE setCode = :queryString")
    fun getAllCards(queryString: String): PagingSource<Int, CardsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBooster(cardsBoosterEntity: CardsBoosterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllBooster(cardsBoosterEntity: List<CardsBoosterEntity>)

    @Query("DELETE FROM MAGIC_CARDS_BOOSTER_TABLE")
    fun deleteAllBoosters()

    @Query("SELECT * FROM MAGIC_CARDS_BOOSTER_TABLE WHERE setCode = :setCode")
    fun getBoosterCards(setCode: String): Observable<List<CardsBoosterEntity>>
}