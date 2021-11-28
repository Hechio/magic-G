package com.stevehechio.apps.magictheg.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stevehechio.apps.magictheg.data.local.entities.CardsRemoteKeys
import com.stevehechio.apps.magictheg.data.local.entities.RemoteKeys

/**
 * Created by stevehechio on 11/28/21
 */
@Dao
interface CardsRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<CardsRemoteKeys>)

    @Query("SELECT * FROM cards_remote_keys WHERE id = :id")
    suspend fun remoteKeysSetId(id: String): CardsRemoteKeys?

    @Query("DELETE FROM cards_remote_keys")
    suspend fun clearRemoteKeys()
}