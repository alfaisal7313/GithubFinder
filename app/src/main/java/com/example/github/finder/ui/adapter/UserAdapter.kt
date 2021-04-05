package com.example.github.finder.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.github.finder.model.Item
import com.example.github.finder.ui.viewholder.FooterViewHolder
import com.example.github.finder.ui.viewholder.UserViewHolder


class UserAdapter : PagedListAdapter<Item, RecyclerView.ViewHolder>(UserDiffCallBack) {
    private val ITEM_LIST = 1
    private val ITEM_FOOTER = 2

    private var isLoading = false
    private var isError = false

    companion object {
        private val UserDiffCallBack = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.login == newItem.login
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }

        }
    }

    fun loadingList(result: Boolean = false) {
        isLoading = result
        notifyItemChanged(super.getItemCount())
    }

    fun errorList(result: Boolean = false) {
        isError = result
        notifyItemChanged(super.getItemCount())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_LIST) {
            UserViewHolder.create(parent)
        } else FooterViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM_LIST) {
            (holder as UserViewHolder).bind(getItem(position))
        } else (holder as FooterViewHolder).bind(isLoading)
    }

    override fun getItemCount(): Int {
        return if (super.getItemCount() != 0 && isLoading || isError) {
            super.getItemCount() + 1
        } else super.getItemCount() + 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) ITEM_LIST else ITEM_FOOTER
    }
}