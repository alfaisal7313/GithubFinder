package com.example.github.finder.utils

interface ResponseData<T> {
    fun setData(result: T)
    fun setError(msg: String? = null)
    fun showLoading()
    fun hideLoading()
}