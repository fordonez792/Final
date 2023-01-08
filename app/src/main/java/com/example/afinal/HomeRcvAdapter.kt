package com.example.afinal

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.afinal.databinding.FragmentHomeBinding
import com.example.afinal.databinding.SingleLocationBinding

class HomeRcvAdapter(
    private val onItemClicked: (Location)->Unit
): ListAdapter<Location, HomeRcvAdapter.LocationViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(oldLocation: Location, newLocation: Location): Boolean {
                return oldLocation.id == newLocation.id
            }

            override fun areContentsTheSame(oldLocation: Location, newLocation: Location): Boolean {
                return oldLocation == newLocation
            }
        }
    }

    class LocationViewHolder(val binding: SingleLocationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Location) {
            binding.label.text = item.name
            binding.image.load(Uri.parse(item.image))
        }
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
        holder.itemView.setOnClickListener { onItemClicked(currentItem) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SingleLocationBinding.inflate(layoutInflater, parent, false)
        val viewHolder = LocationViewHolder(binding)
        return viewHolder
    }
}