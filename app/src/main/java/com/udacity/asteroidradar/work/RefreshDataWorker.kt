package com.udacity.asteroidradar.work

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getDatabaseInstance
import com.udacity.asteroidradar.main.MainViewModel
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, parameters: WorkerParameters) : CoroutineWorker(appContext, parameters) {
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        //add variable that will get data from internet: Val getDataFromApi = getAsteroidData(database)
        val database = getDatabaseInstance(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            repository.refreshAsteroids()
            Result.success()

        } catch (e: HttpException) {
            Result.retry()
        }

    }
}