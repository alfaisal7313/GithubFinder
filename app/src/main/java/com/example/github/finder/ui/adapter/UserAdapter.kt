package com.example.github.finder.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.github.finder.model.Item
import com.example.github.finder.ui.viewholder.UserViewHolder
import com.example.github.finder.R

class UserAdapter(private val itemList: List<Item>) :
    PagedListAdapter<Item, RecyclerView.ViewHolder>(UserDiffCallBack) {

    companion object {
        val UserDiffCallBack = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.login == newItem.login
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as UserViewHolder).bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return if (itemList.isNotEmpty()) itemList.size else 0
    }
}