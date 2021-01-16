package com.jdeveloperapps.appcrafttest.ui.fragments.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.jdeveloperapps.appcrafttest.R
import com.jdeveloperapps.appcrafttest.databinding.ItemDetailAlbumRecyclerBinding
import com.jdeveloperapps.appcrafttest.models.Album
import com.jdeveloperapps.appcrafttest.models.AlbumDetail

class DetailAlbumAdapter(private val listener: OnItemClickListener) : ListAdapter<AlbumDetail, DetailAlbumAdapter.AlbumDetailViewHolder>(
    DiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumDetailViewHolder {
        val binding = ItemDetailAlbumRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumDetailViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class AlbumDetailViewHolder(private val binding: ItemDetailAlbumRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val albumDetail = getItem(position)
                    listener.onItemClick(albumDetail)
                }
            }
        }

        fun bind(albumDetail: AlbumDetail) {
            binding.apply {
                imageView.load(albumDetail.thumbnailUrl) {
                    placeholder(R.drawable.ic_plaseholder)
                    crossfade(true)
                    error(R.drawable.ic_error)
                }
                textViewName.text = albumDetail.title
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<AlbumDetail>() {
        override fun areItemsTheSame(oldItem: AlbumDetail, newItem: AlbumDetail) =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: AlbumDetail, newItem: AlbumDetail) =
            oldItem == newItem

    }

    interface OnItemClickListener {
        fun onItemClick(albumDetail: AlbumDetail)
    }
}