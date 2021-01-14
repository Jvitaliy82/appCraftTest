package com.jdeveloperapps.appcrafttest.ui.fragments.detail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdeveloperapps.appcrafttest.api.AlbumsApi
import com.jdeveloperapps.appcrafttest.db.AlbumDao
import com.jdeveloperapps.appcrafttest.models.Album
import com.jdeveloperapps.appcrafttest.models.AlbumDetail
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DetailViewModel @ViewModelInject constructor(
    private val api: AlbumsApi,
    private val albumDao: AlbumDao,
    @Assisted state: SavedStateHandle
) : ViewModel() {
    val album = state.get<Album>("album")

    val listPhoto: MutableLiveData<List<AlbumDetail>> = MutableLiveData()

    private val detailEventChannel = Channel<DetailEvent>()
    val detailEvent = detailEventChannel.receiveAsFlow()

    fun getPhotos() = viewModelScope.launch {
        detailEventChannel.send(DetailEvent.ShowProgressBar(true))
        try {
            val response = api.getPhotos(album!!.id)
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    listPhoto.postValue(body)
                }
            } else {
                detailEventChannel.send(DetailEvent.ShowMessage("Неудалось загрузить"))
            }
        } catch (e: Exception) {
            detailEventChannel.send(DetailEvent.ShowMessage("Ошибка загрузки: ${e.message}"))
        }
        detailEventChannel.send(DetailEvent.ShowProgressBar(false))
    }

    fun onFabClicked() = viewModelScope.launch {
        albumDao.insertAlbum(album!!)
        detailEventChannel.send(DetailEvent.ShowMessageSaved(album))
    }

    fun undoSavedClick(album: Album) = viewModelScope.launch {
        albumDao.deleteAlbum(album)
    }

    sealed class DetailEvent {
        data class ShowProgressBar(val visible: Boolean) : DetailEvent()
        data class ShowMessage(val message: String) : DetailEvent()
        data class ShowMessageSaved(val album: Album) : DetailEvent()
    }
}