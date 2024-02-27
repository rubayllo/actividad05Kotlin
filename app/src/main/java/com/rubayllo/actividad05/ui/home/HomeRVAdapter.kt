package com.rubayllo.actividad05.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rubayllo.actividad05.databinding.ItemDealsBinding
import com.rubayllo.actividad05.data.network.model.DealsModel

class HomeRVAdapter(): ListAdapter<DealsModel, HomeRVAdapter.HomeViewHolder> (BaseItemCallback) {

    inner class HomeViewHolder(val binding: ItemDealsBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDealsBinding.inflate(inflater, parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = getItem(position)
        // TODO pintar datos
        holder.binding.tvTitle.text = item.title
        holder.binding.tvNormalPrice.text = item.normalPrice
        holder.binding.tvSalePrice.text = item.salePrice

        Glide.with(holder.itemView.context).load(item.thumb).into(holder.binding.ivGameImage)
    }
}

object BaseItemCallback: DiffUtil.ItemCallback<DealsModel>() {
    override fun areItemsTheSame(oldItem: DealsModel, newItem: DealsModel): Boolean {
        return oldItem.dealID == newItem.dealID
    }

    override fun areContentsTheSame(oldItem: DealsModel, newItem: DealsModel): Boolean {
        return oldItem == newItem
    }
}