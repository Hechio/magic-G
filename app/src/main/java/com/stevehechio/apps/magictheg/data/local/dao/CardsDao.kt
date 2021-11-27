package com.stevehechio.apps.magictheg.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stevehechio.apps.magictheg.data.local.entities.CardsEntity

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

    @Query("SELECT * FROM MAGIC_CARDS_TABLE WHERE setCode LIKE :queryString")
    fun getAllCards(queryString: String): PagingSource<Int, CardsEntity>

}