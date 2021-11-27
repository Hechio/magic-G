package com.stevehechio.apps.magictheg.data.local.entities

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stevehechio.apps.magictheg.utils.AppConstants
import java.io.Serializable

/**
 * Created by stevehechio on 11/27/21
 */

@Entity(tableName = AppConstants.TABLE_NAME)
data class SetsEntity(

    @PrimaryKey
    val code: String,
    @Nullable
    val name: String,
    @Nullable
    val type: String,
    @Nullable
    val releaseDate: String
): Serializable
