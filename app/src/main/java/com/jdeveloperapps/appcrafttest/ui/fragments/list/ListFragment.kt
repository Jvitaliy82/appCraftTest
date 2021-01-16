package com.jdeveloperapps.appcrafttest.ui.fragments.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jdeveloperapps.appcrafttest.R
import com.jdeveloperapps.appcrafttest.adapters.ListAlbumAdapter
import com.jdeveloperapps.appcrafttest.databinding.FragmentListBinding
import com.jdeveloperapps.appcrafttest.models.Album
import com.jdeveloperapps.appcrafttest.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list), ListAlbumAdapter.OnItemClickListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ListViewModel>()
    private val adapter = ListAlbumAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentListBinding.bind(view)

        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(
                DividerItemDecoration(
                    activity,
                    LinearLayoutManager.VERTICAL
                )
            )
        }

        viewModel.albums.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.listEvent.collect { event ->
                when (event) {
                    is ListViewModel.ListEvent.ShowProgressBar -> {
                        showProgressBar(event.visible)
                    }
                    is ListViewModel.ListEvent.ShowMessage -> {
                        showMessage(event.message)
                    }
                    is ListViewModel.ListEvent.NavigateToDetailFragment -> {
                        val action = ListFragmentDirections
                            .actionListFragmentToDetailFragment(event.album, false)
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }

        viewModel.getAlbums()
    }

    override fun onItemClick(album: Album) {
        viewModel.onItemSelected(album)
    }

    private fun showProgressBar(visible: Boolean) {
        binding.progressBar.isVisible = visible
    }

    private fun showMessage(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}