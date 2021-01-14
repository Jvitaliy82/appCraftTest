package com.jdeveloperapps.appcrafttest.ui.fragments.saved

import android.os.Bundle
import android.view.View
import android.widget.ListAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jdeveloperapps.appcrafttest.R
import com.jdeveloperapps.appcrafttest.adapters.ListAlbumAdapter
import com.jdeveloperapps.appcrafttest.databinding.FragmentSavedBinding
import com.jdeveloperapps.appcrafttest.db.AlbumDao
import com.jdeveloperapps.appcrafttest.models.Album
import com.jdeveloperapps.appcrafttest.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

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

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val album = adapter.currentList[viewHolder.adapterPosition]
                    viewModel.onTaskSwiped(album)
                }
            }).attachToRecyclerView(recyclerView)
        }

        viewModel.listAlbums.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.savedEvent.collect { event ->
                when(event) {
                    is SavedViewModel.SavedEvent.ShowUndoDeleteMessage -> {
                        Snackbar.make(
                            requireView(),
                            resources.getString(R.string.album_deleted),
                            Snackbar.LENGTH_LONG
                        ).setAction(resources.getString(android.R.string.cancel)) {
                            viewModel.undoDeleteClick(event.album)
                        }.show()
                    }
                }.exhaustive
            }
        }

    }

    override fun onItemClick(album: Album) {

    }
}