package com.stevehechio.apps.magictheg.data.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.stevehechio.apps.magictheg.data.local.entities.CardsBoosterEntity
import com.stevehechio.apps.magictheg.data.local.entities.CardsEntity
import java.io.Serializable

/**
 * Created by stevehechio on 11/27/21
 */
data class CardsApiResponse(
    @SerializedName("cards")
    @Expose
    val cardList: List<CardsEntity>,

    val error: String?
):Serializable

data class CardBoosterApiResponse(
    @SerializedName("cards")
    @Expose
    val cardList: List<CardsBoosterEntity>,

    val error: String?
):Serializable
