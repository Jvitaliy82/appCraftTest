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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DetailViewModel @ViewModelInject constructor(
    private val api: AlbumsApi,
    private val albumDao: AlbumDao,
    @Assisted state: SavedStateHandle
) : ViewModel() {
    val album = state.get<Album>("album")
    private val fromSaved = state.get<Boolean>("fromSaved")

    val listPhoto: MutableLiveData<List<AlbumDetail>> = MutableLiveData()

    private val detailEventChannel = Channel<DetailEvent>()
    val detailEvent = detailEventChannel.receiveAsFlow()

    fun getPhotos() = viewModelScope.launch(Dispatchers.IO) {
        detailEventChannel.send(DetailEvent.ShowProgressBar(true))
        if (fromSaved!!) {
            val listAlbumDetail = albumDao.getListAlbumDetailById(album!!.id)
            listPhoto.postValue(listAlbumDetail)
            detailEventChannel.send(DetailEvent.ShowSavedButton(!fromSaved))
        } else {
            try {
                val response = api.getPhotos(album!!.id)
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        listPhoto.postValue(body)
                        detailEventChannel.send(DetailEvent.ShowSavedButton(!fromSaved))
                    }
                } else {
                    detailEventChannel.send(DetailEvent.ShowMessage("Неудалось загрузить"))
                }
            } catch (e: Exception) {
                detailEventChannel.send(DetailEvent.ShowMessage("Ошибка загрузки: ${e.message}"))
            }
        }
        detailEventChannel.send(DetailEvent.ShowProgressBar(false))
    }

    fun onFabClicked() = viewModelScope.launch {
        albumDao.insertAlbum(album!!, listPhoto.value!!)
        detailEventChannel.send(DetailEvent.ShowMessageSaved(album, listPhoto.value!!))
    }

    fun undoSavedClick(album: Album, listAlbum: List<AlbumDetail>) = viewModelScope.launch {
        albumDao.deleteAlbum(album, listAlbum)
    }

    fun showImageZoom(albumDetail: AlbumDetail) = viewModelScope.launch {
        detailEventChannel.send(DetailEvent.NavigateToImageZoom(albumDetail))
    }

    sealed class DetailEvent {
        data class ShowProgressBar(val visible: Boolean) : DetailEvent()
        data class ShowMessage(val message: String) : DetailEvent()
        data class ShowMessageSaved(val album: Album, val listAlbum: List<AlbumDetail>) : DetailEvent()
        data class ShowSavedButton(val visible: Boolean) : DetailEvent()
        data class NavigateToImageZoom(val albumDetail: AlbumDetail) : DetailEvent()
    }
}