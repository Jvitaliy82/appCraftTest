package com.jdeveloperapps.appcrafttest.ui.fragments.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.jdeveloperapps.appcrafttest.R
import com.jdeveloperapps.appcrafttest.databinding.FragmentDetailBinding
import com.jdeveloperapps.appcrafttest.databinding.FragmentListBinding
import com.jdeveloperapps.appcrafttest.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailViewModel>()
    private val adapter = DetailAlbumAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailBinding.bind(view)

        binding.apply {
            recyclerView.adapter = adapter
            fab.setOnClickListener { viewModel.onFabClicked() }
        }

        viewModel.listPhoto.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            binding.nameAlbum.text = viewModel.album!!.title
            viewModel.detailEvent.collect { event ->
                when (event) {
                    is DetailViewModel.DetailEvent.ShowProgressBar -> {
                        showProgressBar(event.visible)
                    }
                    is DetailViewModel.DetailEvent.ShowMessage -> {
                        showMessage(event.message)
                    }
                    is DetailViewModel.DetailEvent.ShowMessageSaved -> {
                        Snackbar.make(
                            requireView(),
                            resources.getString(R.string.album_saved),
                            Snackbar.LENGTH_LONG
                        ).setAction(resources.getString(android.R.string.cancel)) {
                            viewModel.undoSavedClick(event.album)
                        }.show()
                    }
                }.exhaustive
            }
        }

        viewModel.getPhotos()
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