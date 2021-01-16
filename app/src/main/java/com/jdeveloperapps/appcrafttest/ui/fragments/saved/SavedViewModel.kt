package com.jdeveloperapps.appcrafttest.ui.fragments.saved

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdeveloperapps.appcrafttest.db.AlbumDao
import com.jdeveloperapps.appcrafttest.models.Album
import com.jdeveloperapps.appcrafttest.models.AlbumDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedViewModel @ViewModelInject constructor(
    private val albumDao: AlbumDao
) : ViewModel() {
    val listAlbums = albumDao.getAllAlbum()

    private val savedEventChannel = Channel<SavedEvent>()
    val savedEvent = savedEventChannel.receiveAsFlow()

    fun onTaskSwiped(album: Album) = viewModelScope.launch(Dispatchers.IO) {
        val listDetail = albumDao.getListAlbumDetailById(album.id)
        albumDao.deleteAlbum(album, listDetail)
        savedEventChannel.send(SavedEvent.ShowUndoDeleteMessage(album, listDetail))
    }

    fun undoDeleteClick(album: Album, listAlbumDetail: List<AlbumDetail>) = viewModelScope.launch {
        albumDao.insertAlbum(album, listAlbumDetail)
    }

    sealed class SavedEvent {
        data class ShowUndoDeleteMessage(
            val album: Album,
            val listAlbumDetail: List<AlbumDetail>
        ) : SavedEvent()
    }
}