package com.jdeveloperapps.appcrafttest.ui.fragments.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdeveloperapps.appcrafttest.api.AlbumsApi
import com.jdeveloperapps.appcrafttest.models.Album
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ListViewModel @ViewModelInject constructor(private val api: AlbumsApi) : ViewModel() {

    val albums: MutableLiveData<List<Album>> = MutableLiveData()

    private val listEventChannel = Channel<ListEvent>()
    val listEvent = listEventChannel.receiveAsFlow()

    fun getAlbums() = viewModelScope.launch {
        listEventChannel.send(ListEvent.ShowProgressBar(true))
        try {
            val response = api.getAlbums()
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    albums.postValue(body)
                }
            } else {
                listEventChannel.send(ListEvent.ShowMessage("Неудалось загрузить"))
            }
        } catch (e: Exception) {
            listEventChannel.send(ListEvent.ShowMessage("Ошибка загрузки: ${e.message}"))
        }
        listEventChannel.send(ListEvent.ShowProgressBar(false))
    }

    fun onItemSelected(album: Album) = viewModelScope.launch {
        listEventChannel.send(ListEvent.NavigateToDetailFragment(album))
    }

    sealed class ListEvent {
        data class ShowProgressBar(val visible: Boolean) : ListEvent()
        data class ShowMessage(val message: String) : ListEvent()
        data class NavigateToDetailFragment(val album: Album) : ListEvent()
    }

}