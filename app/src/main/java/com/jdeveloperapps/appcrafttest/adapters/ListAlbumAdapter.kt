package com.jdeveloperapps.appcrafttest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jdeveloperapps.appcrafttest.databinding.ItemAlbumRecyclerBinding
import com.jdeveloperapps.appcrafttest.models.Album

class ListAlbumAdapter : ListAdapter<Album, ListAlbumAdapter.AlbumViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemAlbumRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class AlbumViewHolder(private val binding: ItemAlbumRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            binding.apply {
                textViewName.text = album.title
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Album, newItem: Album) =
            oldItem == newItem

    }
}