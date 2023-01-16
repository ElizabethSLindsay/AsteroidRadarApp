package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(asteroid: AsteroidEntities)

    @Query("""
        DELETE FROM Asteroids
    """)
    suspend fun clear()

    @Query("""
        SELECT * FROM Asteroids
    """)
    fun getAsteroids(): Flow<List<Asteroid>>


}