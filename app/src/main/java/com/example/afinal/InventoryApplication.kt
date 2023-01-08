package com.example.afinal

import android.app.Application
import com.example.afinal.LocationRoomDatabase


class InventoryApplication: Application() {
    val database: LocationRoomDatabase by lazy {
        LocationRoomDatabase.getDatabase(this)
    }
}