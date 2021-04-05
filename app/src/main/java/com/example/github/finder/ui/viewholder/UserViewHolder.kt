package com.example.github.finder.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.github.finder.R
import com.example.github.finder.databinding.ItemListUserBinding
import com.example.github.finder.model.Item

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val viewContent = ItemListUserBinding.bind(itemView)

    fun bind(item: Item?) {
        viewContent.itemTitle.text = item?.login

        Glide.with(itemView.context)
            .load(item)
            .load(item?.avatarUrl)
            .circleCrop()
            .placeholder(ContextCompat.getDrawable(itemView.context, R.drawable.item_placeholder))
            .into(viewContent.itemImage)
    }

    companion object {
        fun create(parent: ViewGroup): UserViewHolder {
            val view = LayoutInflater.from (parent.context)
                .inflate(R.layout.item_list_user, parent, false)
            return UserViewHolder(view)
        }
    }
}