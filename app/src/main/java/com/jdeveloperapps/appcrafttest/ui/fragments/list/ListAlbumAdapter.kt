package com.jdeveloperapps.appcrafttest.ui.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jdeveloperapps.appcrafttest.databinding.ItemListAlbumRecyclerBinding
import com.jdeveloperapps.appcrafttest.models.Album

class ListAlbumAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Album, ListAlbumAdapter.AlbumViewHolder>(
        DiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding =
            ItemListAlbumRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class AlbumViewHolder(private val binding: ItemListAlbumRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val album = getItem(position)
                    listener.onItemClick(album)
                }
            }
        }

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

    interface OnItemClickListener {
        fun onItemClick(album: Album)
    }
}