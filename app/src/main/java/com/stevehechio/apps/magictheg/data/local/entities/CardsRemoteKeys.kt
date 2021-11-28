package com.stevehechio.apps.magictheg.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by stevehechio on 11/28/21
 */

@Entity(tableName = "cards_remote_keys")
data class CardsRemoteKeys(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)