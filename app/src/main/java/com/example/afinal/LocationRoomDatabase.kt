package com.example.afinal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Location::class], version = 3, exportSchema = false)
abstract class LocationRoomDatabase: RoomDatabase() {
    abstract fun LocationDao(): LocationDao

    companion object {
        @Volatile
        private var INSTANCE: LocationRoomDatabase? = null

        fun getDatabase(context: Context): LocationRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationRoomDatabase::class.java,
                    "location_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}