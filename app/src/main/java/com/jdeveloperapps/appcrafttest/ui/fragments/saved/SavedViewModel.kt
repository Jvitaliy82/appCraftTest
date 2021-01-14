package com.jdeveloperapps.appcrafttest.ui.fragments.saved

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.jdeveloperapps.appcrafttest.db.AlbumDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class SavedViewModel @ViewModelInject constructor(
    private val albumDao: AlbumDao
): ViewModel() {

    val listAlbums = albumDao.getAllAlbum()

    private val savedEventChannel = Channel<SavedEvent>()
    val savedEvent = savedEventChannel.receiveAsFlow()

    sealed class SavedEvent {

    }
}