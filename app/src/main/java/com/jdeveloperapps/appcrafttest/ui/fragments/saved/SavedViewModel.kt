package com.jdeveloperapps.appcrafttest.ui.fragments.saved

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdeveloperapps.appcrafttest.db.AlbumDao
import com.jdeveloperapps.appcrafttest.models.Album
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SavedViewModel @ViewModelInject constructor(
    private val albumDao: AlbumDao
): ViewModel() {
    val listAlbums = albumDao.getAllAlbum()

    private val savedEventChannel = Channel<SavedEvent>()
    val savedEvent = savedEventChannel.receiveAsFlow()

    fun onTaskSwiped(album: Album) = viewModelScope.launch {
        albumDao.deleteAlbum(album)
        savedEventChannel.send(SavedEvent.ShowUndoDeleteMessage(album))
    }

    fun undoDeleteClick(album: Album) = viewModelScope.launch {
        albumDao.insertAlbum(album)
    }

    sealed class SavedEvent {
        data class ShowUndoDeleteMessage(val album: Album) : SavedEvent()
    }
}