package com.example.github.finder.ui.view

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.github.finder.model.Item
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
    private val repository: UserRepository,
) : PageKeyedDataSource<Int, Item>() {
    private var currentPage = 1
    private var mQuery: String? = null

    fun setQuery(query: String) {
        this.mQuery = query
    }

    var loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val messageError: MutableLiveData<String> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Item>
    ) {
        Log.d("TAG", "setQuery: $mQuery")

        if (!mQuery.isNullOrEmpty()) {
            compositeDisposable.add(
                repository.searchUser(
                    key = mQuery.toString(),
                    orderBy = Constant.ORDER_BY_ASC,
                    page = 1,
                    countData = params.requestedLoadSize,
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showLoading() }
                    .doAfterTerminate { hideLoading() }
                    .subscribe(
                        { result ->
                            callback.onResult(result.items, 0, currentPage + 1)
                        },
                        { error -> setDisposableError(error) }
                    )
            )
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {}

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {
        currentPage = params.key

        if (!mQuery.isNullOrEmpty()) {
            compositeDisposable.add(
                repository.searchUser(
                    key = mQuery.toString(),
                    orderBy = Constant.ORDER_BY_ASC,
                    page = params.key,
                    countData = params.requestedLoadSize,
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showLoading() }
                    .doAfterTerminate { hideLoading() }
                    .subscribe(
                        { result ->
                            callback.onResult(result.items, currentPage + 1)
                        },
                        { error -> setDisposableError(error) }
                    )
            )
        }
    }

    private fun showLoading() {
        loadingVisibility.postValue(View.VISIBLE)
    }

    private fun hideLoading() {
        loadingVisibility.postValue(View.GONE)
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

        loadingVisibility.postValue(View.GONE)
    }
}