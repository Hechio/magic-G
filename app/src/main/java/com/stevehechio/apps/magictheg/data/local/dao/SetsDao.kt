package com.stevehechio.apps.magictheg.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stevehechio.apps.magictheg.data.local.entities.SetsEntity


/**
 * Created by stevehechio on 11/27/21
 */

@Dao
interface SetsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(setsEntity: SetsEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(setsEntities: List<SetsEntity>)

    @Query("DELETE FROM magic_sets_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM magic_sets_table")
    fun getAllSets(): PagingSource<Int,SetsEntity>
}