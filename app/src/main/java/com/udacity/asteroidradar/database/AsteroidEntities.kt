package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Asteroids")
data class AsteroidEntities(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean,
    val display: Boolean = true
)

@Entity(tableName = "PotD")
data class PotD(
    @PrimaryKey
    val mediaType: String,
    val title: String,
    val url: String
)