package com.example.github.finder.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("documentation_url")
    val documentationUrl: String? = null,
    @SerializedName("message")
    val message: String? = null
)