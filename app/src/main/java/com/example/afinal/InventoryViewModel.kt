package com.example.afinal

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class InventoryViewModel(private val locationDao: LocationDao): ViewModel() {

    val locations: LiveData<List<Location>> = locationDao.getLocations().asLiveData()

    fun addNewItem(name: String, image: String, description: String, latitude: Float, longitude: Float) {
        val newItem = getNewItemEntry(name, image, description, latitude, longitude)
        insertItem(newItem)
    }
    private fun insertItem(location: Location) {
        viewModelScope.launch {
            locationDao.insert(location)
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<Location>> {
        return locationDao.searchDatabase(searchQuery).asLiveData()
    }

    private fun getNewItemEntry(name: String, image: String, description: String, latitude: Float, longitude: Float): Location {
        return Location(
            name = name,
            image = image,
            description = description,
            latitude = latitude,
            longitude = longitude
        )
    }

    fun retrieveItem(id: Int): LiveData<Location> {
        return locationDao.getItem(id).asLiveData()
    }

    fun getItem(pos: Int): Location {
        return locations.value!![pos]
    }

    fun deleteItem(location: Location) {
        viewModelScope.launch {
            locationDao.delete(location)
        }
    }
}

class InventoryViewModelFactory(private val locationDao: LocationDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(locationDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}