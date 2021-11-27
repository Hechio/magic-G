package com.stevehechio.apps.magictheg.data.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.stevehechio.apps.magictheg.data.local.entities.CardsEntity
import com.stevehechio.apps.magictheg.data.local.entities.SetsEntity
import java.io.Serializable

/**
 * Created by stevehechio on 11/27/21
 */
data class SetsApiResponse(
    @SerializedName("sets")
    @Expose
    val cardList: List<SetsEntity> = emptyList(),
    val nextPage: Int? = null
):Serializable
