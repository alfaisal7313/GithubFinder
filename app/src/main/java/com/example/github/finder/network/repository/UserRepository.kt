package com.example.github.finder.network.repository

import com.example.github.finder.model.UserResponse
import com.example.github.finder.network.api.ApiHelper
import io.reactivex.Single
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiHelper: ApiHelper) {
    fun searchUser(
        key: String,
        orderBy: String,
        page: Int,
        countData: Int
    ): Single<UserResponse> {
        return apiHelper.searchUser(key, orderBy, page, countData)
    }
}