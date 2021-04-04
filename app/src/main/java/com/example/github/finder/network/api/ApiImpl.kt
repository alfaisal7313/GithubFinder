package com.example.github.finder.network.api

import com.example.github.finder.model.UserResponse
import io.reactivex.Single
import javax.inject.Inject

class ApiImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    override fun searchUser(
        key: String,
        orderBy: String,
        page: Int,
        countData: Int
    ): Single<UserResponse> {
        return apiService.searchUser(key, orderBy, page, countData)
    }
}