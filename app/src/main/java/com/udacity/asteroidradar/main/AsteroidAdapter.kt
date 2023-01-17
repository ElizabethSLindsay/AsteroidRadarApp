package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.ListItemMainBinding

class AsteroidAdapter(
    private val clickItem: (Asteroid) -> Unit
) : ListAdapter<Asteroid, AsteroidAdapter.ItemViewHolder>(DiffCallback) {

    class ItemViewHolder(
        private var bindingListItem: ListItemMainBinding
    ) : RecyclerView.ViewHolder(bindingListItem.root) {

        fun bindItem(asteroid: Asteroid) {
            bindingListItem.asteroidListItem = asteroid
            if (asteroid.isPotentiallyHazardous) {
                bindingListItem.asteroidStatus.setImageResource(R.drawable.ic_status_potentially_hazardous)
            } else {
                bindingListItem.asteroidStatus.setImageResource(R.drawable.ic_status_normal)
            }
            bindingListItem.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
        return ItemViewHolder(
            ListItemMainBinding.inflate(adapterLayout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            clickItem(item)
        }
        holder.bindItem(item)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(
            oldItem: Asteroid,
            newItem: Asteroid
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Asteroid,
            newItem: Asteroid
        ): Boolean {
            return oldItem == newItem
        }

    }
}