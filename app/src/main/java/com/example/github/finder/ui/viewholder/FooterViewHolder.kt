package com.example.github.finder.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.github.finder.R
import com.example.github.finder.databinding.ItemPaggingFooterBinding

class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val view = ItemPaggingFooterBinding.bind(itemView)

    fun bind(isShow: Boolean){
        view.itemLoader.progressView.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    companion object {
        fun create(parent: ViewGroup): FooterViewHolder {
            val view = LayoutInflater.from (parent.context)
                .inflate(R.layout.item_pagging_footer, parent, false)
            return FooterViewHolder(view)
        }
    }
}