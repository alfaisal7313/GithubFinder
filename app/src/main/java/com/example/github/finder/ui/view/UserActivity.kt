package com.example.github.finder.ui.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.finder.model.Item
import com.example.github.finder.ui.adapter.UserAdapter
import com.example.github.finder.utils.Network
import com.example.github.finder.utils.ResponseData
import com.example.github.finder.viewmodel.UserViewModel
import com.example.github.finder.R
import com.example.github.finder.databinding.UserActivityBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserActivity : AppCompatActivity(), ResponseData<List<Item>> {
    private lateinit var bindingView: UserActivityBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var userAdapter: UserAdapter

    @Inject
    lateinit var network: Network

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = UserActivityBinding.inflate(layoutInflater)
        setContentView(bindingView.root)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        setupUi()
        setupObservable()
    }

    private fun setupUi() {
        bindingView.apply {
            inputValue.addTextChangedListener {
                searchData(it.toString())
            }

            btnActionSearch.setOnClickListener {
                searchData(it.toString())
            }
        }
    }

    private fun setupObservable() {
        userViewModel.apply {
            userDataSource.userData.observe(this@UserActivity, Observer {
                setData(it)
            })

            userDataSource.messageError.observe(this@UserActivity, Observer {
                setError(it)
            })

            userDataSource.loadingVisibility.observe(this@UserActivity, Observer {
                if (it.equals(View.VISIBLE)) {
                    showLoading()
                } else hideLoading()
            })

        }
    }

    private fun searchData(str: String) {
        if (network.isNetWork()) {
            if (str.isNotEmpty()) {
                userViewModel.userDataSource.fetchDataSearch(str)
            } else {
                setError(getString(R.string.msg_data_not_found))
            }
        } else setError(getString(R.string.msg_connection_failed))
    }

    override fun setData(result: List<Item>) {
        userAdapter = UserAdapter(result)
        val linearLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        bindingView.rvList.apply {
            layoutManager = linearLayoutManager
            adapter = userAdapter
        }
    }

    override fun setError(msg: String?) {
        Snackbar.make(bindingView.root, msg.toString(), Snackbar.LENGTH_LONG).show()
    }

    override fun showLoading() {
        bindingView.progressView.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        bindingView.progressView.visibility = View.GONE
    }
}