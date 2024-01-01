package io.aeroflux.notify

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData

class NotifyViewModel(private val repository: NotifyRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val data: LiveData<List<Notification>> = repository.allNotif.asLiveData()
}

class NotifyViewModelFactory(private val repository: NotifyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotifyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotifyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}