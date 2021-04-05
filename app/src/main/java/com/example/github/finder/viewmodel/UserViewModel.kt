package com.example.github.finder.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.github.finder.model.Item
import com.example.github.finder.network.repository.UserRepository
import com.example.github.finder.ui.view.UserDataSource
import com.example.github.finder.utils.MainThreadExecutor
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    repository: UserRepository
) : ViewModel() {
    private val pageLimit = 10

    private val compositeDisposable = CompositeDisposable()
    private var userDataSource = UserDataSource(compositeDisposable, repository)
    private var pageConfig = PagedList.Config.Builder()
        .setPageSize(pageLimit)
        .setInitialLoadSizeHint(pageLimit * 2)
        .setEnablePlaceholders(false).build()

    private var userItem: MutableLiveData<PagedList<Item>> = MutableLiveData()

    fun setQuery(str: String) {
        dataSource().setQuery(str)
        val executor = MainThreadExecutor()
        val pages = PagedList.Builder(userDataSource, pageConfig)
            .setFetchExecutor(executor)
            .setNotifyExecutor(executor)
            .build()
        userItem.postValue(pages)
    }

    fun items() = userItem

    fun dataSource() = userDataSource

    override fun onCleared() {
        super.onCleared()
        if (compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        compositeDisposable.clear()
    }
}