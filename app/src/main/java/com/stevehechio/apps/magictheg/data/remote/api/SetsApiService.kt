package com.stevehechio.apps.magictheg.data.remote.api

import com.stevehechio.apps.magictheg.data.remote.model.SetsApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by stevehechio on 11/27/21
 */
interface SetsApiService {
    //page=1&pageSize=50

    @GET("sets/")
    suspend fun fetchSets(
        @Query("page") page: Int,
        @Query("pageSize") itemsPerPage: Int
    ):SetsApiResponse


}