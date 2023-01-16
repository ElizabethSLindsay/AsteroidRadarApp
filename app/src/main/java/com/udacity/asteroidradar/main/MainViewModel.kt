package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.AsteroidEntities
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel( private val dao: AsteroidDao) : ViewModel() {

    var allAsteroids: LiveData<List<Asteroid>> = dao.getAsteroids().asLiveData()


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

    fun getAsteroidData() {
        viewModelScope.launch {
            try {
                val asteroidList = parseAsteroidsJsonResult(JSONObject(AsteroidApi.retrofitService.getAsteroids("" ,"", "DEMO_KEY")))
                dao.clear()
                for (asteroid in asteroidList) {
                    addAsteroid(asteroid)
                }

            } catch (e:Exception) {
                Log.d("Failed Asteroids", "Error: ${e.localizedMessage}")
            }


            /*_status.value = AsteroidApiStatus.LOADING
            try {
                val asteroidList = parseAsteroidsJsonResult(JSONObject(AsteroidApi.retrofitService.getAsteroids("" ,"", "DEMO_KEY")))
                _status.value = AsteroidApiStatus.DONE
                for (asteroid in asteroidList) {
                    addAsteroid(asteroid)
                }
            } catch (e:Exception) {
                _status.value = AsteroidApiStatus.ERROR
            }*/
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