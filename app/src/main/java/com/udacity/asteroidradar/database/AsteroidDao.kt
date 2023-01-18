package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(asteroid: AsteroidEntities)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPotD(PictureOfDay: PotD)

    @Query("""
        DELETE FROM Asteroids WHERE closeApproachDate<('today')
    """)
    suspend fun clear()

    @Query("""
        DELETE FROM PotD
    """)
    suspend fun clearPotD()

    @Query("""
        SELECT * FROM Asteroids
        WHERE closeApproachDate> DATE('today')
        ORDER BY closeApproachDate
    """)
    fun getAsteroids(): Flow<List<Asteroid>>

    @Query("""
        SELECT * FROM Asteroids
        WHERE closeApproachDate = DATE('today')
        ORDER BY closeApproachDate
    """)
    fun getToday(): Flow<List<Asteroid>>

    @Query("""
        SELECT * from PotD
    """)
    fun getPicture(): Flow<PotD>
}