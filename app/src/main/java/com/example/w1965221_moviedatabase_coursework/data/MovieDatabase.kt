package com.example.w1965221_moviedatabase_coursework.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Movie::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun MovieDao(): MovieDao
}