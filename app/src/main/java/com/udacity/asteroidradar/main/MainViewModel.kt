package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.PotDAPI
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.api.parsePotD
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.AsteroidEntities
import com.udacity.asteroidradar.database.PotD
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel( private val dao: AsteroidDao) : ViewModel() {

    var allAsteroids: LiveData<List<Asteroid>> = dao.getAsteroids().asLiveData()
    var picture: LiveData<PotD> = dao.getPicture().asLiveData()



    private fun addAsteroid(asteroid: Asteroid) {
        viewModelScope.launch {
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
            dao.insert(newAsteroid)
        }
    }

    fun filterAsteroids(filter: String) {
        Log.d("Filter", filter)
        if (filter == "Week") {
            allAsteroids = dao.getAsteroids().asLiveData()
        } else if (filter == "Today") {
            allAsteroids = dao.getToday().asLiveData()
        }
    }

    fun getAsteroidData() {
        viewModelScope.launch {
            try {
                val asteroidList = parseAsteroidsJsonResult(
                    JSONObject(
                        AsteroidApi.retrofitService.getAsteroids(
                            "",
                            "",
                            "SNrG4C86m2Zxhx9b7HAAnOGdJQqB6BzYRLlTi0fp"
                        )
                    )
                )
                dao.clear()
                Log.d("Asteroids", asteroidList.toString())
                for (asteroid in asteroidList) {
                    addAsteroid(asteroid)
                }

            } catch (e: Exception) {
                Log.d("Failed Asteroids", "Error: ${e.localizedMessage}")
            }
        }
    }

    fun getPictureOfTheDay() {
        viewModelScope.launch {
            try {
                val potd: PictureOfDay = parsePotD(JSONObject(PotDAPI.retrofitService.getPotD("SNrG4C86m2Zxhx9b7HAAnOGdJQqB6BzYRLlTi0fp")))
                dao.clearPotD()

                dao.insertPotD(PotD(title = potd.title, mediaType = potd.mediaType, url = potd.url))
            } catch (e: Exception) {
                Log.d("Failed Picture", "Error: ${e.localizedMessage}")
                dao.clearPotD()
                dao.insertPotD(PotD("","",""))
            }
        }
    }
}

class AsteroidViewModelFactory(
    private val dao: AsteroidDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}