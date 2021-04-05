package com.example.github.finder.utils

interface ResponseData {
    fun setError(msg: String? = null)
    fun showLoading()
    fun hideLoading()
}