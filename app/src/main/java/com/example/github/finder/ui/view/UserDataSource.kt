package com.example.github.finder.ui.view

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.github.finder.model.Item
import com.example.github.finder.model.UserResponse
import com.example.github.finder.network.repository.UserRepository
import com.example.github.finder.utils.Constant
import com.example.github.finder.model.ErrorResponse
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val compositeDisposable: CompositeDisposable,
    private val repository: UserRepository
) : PageKeyedDataSource<Int, List<Item>>() {
    private lateinit var mQuery: String

    var userData: MutableLiveData<List<Item>> = MutableLiveData()
    var loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val messageError: MutableLiveData<String> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, List<Item>>
    ) {
        fetchDataSearch(query = mQuery, countData = params.requestedLoadSize)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, List<Item>>) {}

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, List<Item>>) {
        fetchDataSearch(query = mQuery, page = params.key, countData = params.requestedLoadSize)
    }

    private fun showLoading() {
        loadingVisibility.postValue(View.VISIBLE)
    }

    private fun hideLoading() {
        loadingVisibility.postValue(View.GONE)
    }

    private fun setDisposableSuccess(result: UserResponse) {
        result.apply {
            if (totalCount > 0) {
                userData.postValue(items)
            } else {
                userData.postValue(arrayListOf())
            }
        }
    }

    private fun setDisposableError(error: Throwable) {
        val response = (error as HttpException).response()
        val adapter: TypeAdapter<ErrorResponse> =
            Gson().getAdapter(ErrorResponse::class.java)
        try {
            val errResponse = adapter.fromJson(response?.errorBody()?.string())
            messageError.postValue(errResponse.message.toString())
        } catch (errIoException: IOException) {
            messageError.postValue(errIoException.toString())
        }

        userData.postValue(arrayListOf())
        loadingVisibility.postValue(View.GONE)
    }

    fun fetchDataSearch(
        query: String,
        orderBy: String = Constant.ORDER_BY_ASC,
        page: Int = 1,
        countData: Int = 0
    ) {
        mQuery = query
        compositeDisposable.add(
            repository.searchUser(
                key = query,
                orderBy = orderBy,
                page = page,
                countData = countData,
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading() }
                .doOnTerminate { hideLoading() }
                .subscribe(
                    { result -> setDisposableSuccess(result) },
                    { error -> setDisposableError(error) }
                )
        )
    }
}