package com.stevehechio.apps.magictheg.data.local.entities

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.stevehechio.apps.magictheg.data.local.convertors.ColorListTypeConvertor
import com.stevehechio.apps.magictheg.utils.AppConstants
import java.io.Serializable

/**
 * Created by stevehechio on 11/27/21
 */
@Entity(tableName = AppConstants.CARDS_TABLE_NAME)
data class CardsEntity(
    @PrimaryKey
    val id: String,
    @Nullable
    val name: String,
    @TypeConverters(ColorListTypeConvertor::class)
    val colors: List<String>?,
    @Nullable
    val type: String,
    @Nullable
    val rarity: String,
    @Nullable
    @field:SerializedName("set") val setCode: String,
    @Nullable
    val setName: String,
    @Nullable
    val text: String,
    @Nullable
    val flavor: String,
    @Nullable
    val imageUrl: String?
): Serializable
