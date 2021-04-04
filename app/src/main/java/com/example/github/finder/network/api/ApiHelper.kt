package com.example.github.finder.network.api

import com.example.github.finder.model.UserResponse
import io.reactivex.Single

interface ApiHelper {
    fun searchUser(
        key: String,
        orderBy: String,
        page: Int,
        countData: Int
    ): Single<UserResponse>
}