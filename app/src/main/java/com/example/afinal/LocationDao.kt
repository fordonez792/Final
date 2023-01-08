package com.example.afinal

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao{
    @Query("SELECT * from Location ORDER BY name ASC")
    fun getLocations(): Flow<List<Location>>

    @Query("SELECT * from Location WHERE id = :id")
    fun getItem(id: Int): Flow<Location>

    @Query("SELECT * from Location WHERE name LIKE :search")
    fun searchDatabase(search: String): Flow<List<Location>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(location: Location)

    @Update
    suspend fun update(location: Location)

    @Delete
    suspend fun delete(location: Location)
}