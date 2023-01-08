package com.example.afinal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.afinal.databinding.LocationItemBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class DiscoveryRCVAdapter (
    private val options: FirestoreRecyclerOptions<ImageFirestore>,
    private val onItemClicked: (ImageFirestore) -> Unit
    ): FirestoreRecyclerAdapter<ImageFirestore, DiscoveryRCVAdapter.ViewHolder>(options){
            class ViewHolder(val binding: LocationItemBinding): RecyclerView.ViewHolder(binding.root) {
                    fun bind(item: ImageFirestore) {
                           binding.tagText.text = item.name
                            binding.nameText.text = item.uploader
                    }
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LocationItemBinding.inflate(layoutInflater, parent, false)
                val viewHolder = ViewHolder(binding)
                return viewHolder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ImageFirestore) {
                holder.bind(model)
                holder.itemView.setOnClickListener { onItemClicked(model) }
        }
}