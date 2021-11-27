package com.stevehechio.apps.magictheg.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by stevehechio on 11/27/21
 */

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val setCode: String,
    val prevKey: Int?,
    val nextKey: Int?
)
