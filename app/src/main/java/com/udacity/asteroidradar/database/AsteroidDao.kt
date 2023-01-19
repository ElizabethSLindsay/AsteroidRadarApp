package com.udacity.asteroidradar.database

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPotD(PictureOfDay: PotD)

    @Query(
        """
        DELETE FROM Asteroids WHERE date(closeApproachDate) < date('now')
    """
    )
    suspend fun clear()

    @Query(
        """
        DELETE FROM PotD
    """
    )
    suspend fun clearPotD()

    @Query(
        """
        SELECT * FROM Asteroids
        WHERE display = 1
        ORDER BY closeApproachDate
    """
    )
    fun getAsteroids(): Flow<List<Asteroid>>

    @Query(
        """
        UPDATE Asteroids
        SET display = CASE
            WHEN date(closeApproachDate) = date('now') THEN 1
            ELSE 0
            END
    """
    )
    fun filterToday()

    @Query(
        """
        UPDATE Asteroids
        SET display = CASE
            WHEN date(closeApproachDate) >= date('now') THEN 1
            ELSE 0
            END
    """
    )
    fun filterAll()

    @Query(
        """
        SELECT * from PotD
    """
    )
    fun getPicture(): Flow<PotD>
}