package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidEntities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {

    private val job = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + job)

    private fun addAsteroid(asteroid: Asteroid) {
        viewModelScope.launch(Dispatchers.IO) {
            val newAsteroid = AsteroidEntities(
                id = asteroid.id,
                codename = asteroid.codename,
                closeApproachDate = asteroid.closeApproachDate,
                absoluteMagnitude = asteroid.absoluteMagnitude,
                estimatedDiameter = asteroid.estimatedDiameter,
                relativeVelocity = asteroid.relativeVelocity,
                distanceFromEarth = asteroid.distanceFromEarth,
                isPotentiallyHazardous = asteroid.isPotentiallyHazardous
            )
            database.asteroidDao().insert(newAsteroid)
        }
    }

    suspend fun refreshAsteroids() {
        val asteroidList = parseAsteroidsJsonResult(
            JSONObject(
                AsteroidApi.retrofitService.getAsteroids(
                    "",
                    "",
                    "DEMO_KEY"
                )
            )
        )
        database.asteroidDao().clear()
        for (asteroid in asteroidList) {
            addAsteroid(asteroid)
        }
    }
}