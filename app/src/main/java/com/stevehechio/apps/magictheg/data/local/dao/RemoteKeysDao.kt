package com.stevehechio.apps.magictheg.data.local.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stevehechio.apps.magictheg.data.local.entities.RemoteKeys

/**
 * Created by stevehechio on 11/27/21
 */


@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE setCode = :code")
    suspend fun remoteKeysSetCode(code: String): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}