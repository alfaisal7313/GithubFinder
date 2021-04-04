package com.example.github.finder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.github.finder.model.Item
import com.example.github.finder.network.repository.UserRepository
import com.example.github.finder.ui.view.UserDataSource
import com.example.github.finder.ui.view.UserDataSourceFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(repository: UserRepository) : ViewModel() {
    private val pageLimit = 10

    private val compositeDisposable = CompositeDisposable()
    private val userdataFactory = UserDataSourceFactory(compositeDisposable, repository)

    val userDataSource = UserDataSource(compositeDisposable, repository)

    var userList: LiveData<PagedList<List<Item>>>

    init {
        val pageConfig = PagedList.Config.Builder()
            .setPageSize(pageLimit)
            .setInitialLoadSizeHint(pageLimit + pageLimit)
            .setEnablePlaceholders(false)
            .build()

        userList = LivePagedListBuilder(userdataFactory, pageConfig).build()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}