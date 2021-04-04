package com.example.github.finder.ui.view

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.github.finder.model.Item
import com.example.github.finder.network.repository.UserRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class UserDataSourceFactory @Inject constructor(
    private val compositeDisposable: CompositeDisposable,
    private val repository: UserRepository
) : DataSource.Factory<Int, List<Item>>() {

    var userLiveData: MutableLiveData<UserDataSource> = MutableLiveData()

    override fun create(): DataSource<Int, List<Item>> {
        val userDataSource = UserDataSource(compositeDisposable, repository)
        userLiveData.postValue(userDataSource)
        return userDataSource
    }
}