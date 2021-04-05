package com.example.github.finder.ui.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.finder.R
import com.example.github.finder.databinding.UserActivityBinding
import com.example.github.finder.ui.adapter.UserAdapter
import com.example.github.finder.utils.Network
import com.example.github.finder.utils.ResponseData
import com.example.github.finder.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserActivity : AppCompatActivity(), ResponseData {
    private lateinit var bindingView: UserActivityBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var userAdapter: UserAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var network: Network

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = UserActivityBinding.inflate(layoutInflater)
        setContentView(bindingView.root)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userAdapter = UserAdapter()
        linearLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )

        setupUi()
        setupObservable()
    }

    private fun setupUi() {
        bindingView.apply {
            inputValue.addTextChangedListener {
                searchData(it.toString())
            }

            btnActionSearch.setOnClickListener {
                searchData(inputValue.text.toString())
            }

            rvList.apply {
                layoutManager = linearLayoutManager
                adapter = userAdapter
            }
        }
    }

    private fun setupObservable() {
        userViewModel.apply {
            items().observe(this@UserActivity, { item ->
                userAdapter.submitList(item)
            })

            dataSource().messageError.observe(this@UserActivity, {
                setError(it)
            })

            dataSource().loadingVisibility.observe(this@UserActivity, {
                if (it.equals(View.VISIBLE)) {
                    showLoading()
                } else hideLoading()
            })
        }
    }

    private fun searchData(str: String) {
        if (network.isNetWork()) {
            if (str.isNotEmpty()) {
                userViewModel.setQuery(str)
            } else {
                setError(getString(R.string.msg_data_not_found))
            }
        } else setError(getString(R.string.msg_connection_failed))
    }

    override fun setError(msg: String?) {
        userAdapter.errorList(true)
        Snackbar.make(bindingView.root, msg.toString(), Snackbar.LENGTH_LONG).show()
    }

    override fun showLoading() {
        userAdapter.loadingList(true)
        if (bindingView.rvList.adapter?.itemCount == 0) {
            bindingView.loader.progressView.visibility = View.VISIBLE
        }
    }

    override fun hideLoading() {
        bindingView.loader.progressView.visibility = View.GONE
    }
}