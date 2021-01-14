package com.jdeveloperapps.appcrafttest.ui.fragments.saved

import android.os.Bundle
import android.view.View
import android.widget.ListAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jdeveloperapps.appcrafttest.R
import com.jdeveloperapps.appcrafttest.adapters.ListAlbumAdapter
import com.jdeveloperapps.appcrafttest.databinding.FragmentSavedBinding
import com.jdeveloperapps.appcrafttest.db.AlbumDao
import com.jdeveloperapps.appcrafttest.models.Album
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedFragment (): Fragment(R.layout.fragment_saved), ListAlbumAdapter.OnItemClickListener {

    private val viewModel by viewModels<SavedViewModel>()

    private var _binding : FragmentSavedBinding? = null
    val binding get() = _binding!!

    private val adapter = ListAlbumAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSavedBinding.bind(view)

        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(
                DividerItemDecoration(
                    activity,
                    LinearLayoutManager.VERTICAL
                )
            )
        }

        viewModel.listAlbums.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.
//        }

    }

    override fun onItemClick(album: Album) {

    }
}