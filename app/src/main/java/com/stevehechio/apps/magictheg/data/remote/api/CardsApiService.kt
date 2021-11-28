package com.stevehechio.apps.magictheg.data.remote.api

import com.stevehechio.apps.magictheg.data.remote.model.CardsApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by stevehechio on 11/28/21
 */
interface CardsApiService {
    //https://api.magicthegathering.io/v1/cards?set=10E&page=1&pageSize=50

    @GET("cards")
    fun fetchCards(
        @Query("set") setCode: String,
        @Query("page") page: Int,
        @Query("pageSize") itemsPerPage: Int
    ): CardsApiResponse

    //https://api.magicthegathering.io/v1/sets/10E/booster


    @GET("sets/{set_code}/booster")
    fun fetchBoosterCards(
        @Path("set_code")setCode: String
    ): CardsApiResponse
}