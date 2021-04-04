package com.example.github.finder.network.api

import com.example.github.finder.model.UserResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(ApiUrl.SEARCH_USER)
    fun searchUser(
        @Query(ApiUrl.QUERY_KEY) queryKeys: String,
        @Query(ApiUrl.ORDER_BY) orderBy: String? = null,
        @Query(ApiUrl.PAGE_NUMBER) page: Int? = 0,
        @Query(ApiUrl.COUNT_DATA) countData: Int? = 0,
    ): Single<UserResponse>
}