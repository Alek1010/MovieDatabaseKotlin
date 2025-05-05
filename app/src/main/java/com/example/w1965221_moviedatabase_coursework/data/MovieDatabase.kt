package com.example.w1965221_moviedatabase_coursework.data

import androidx.room.Database
import androidx.room.RoomDatabase
// database class.
// - entities = [Movie::class] specifies the list of database entities (tables) used in this database.
// - version = 2 specifies the current version of the database schema. Increment this when making schema changes.
@Database(entities = [Movie::class], version = 2)
abstract class MovieDatabase : RoomDatabase() {

    // Abstract method to get the DAO (Data Access Object) for Movie operations.
    // Room will generate the implementation of this method at compile time.
    abstract fun MovieDao(): MovieDao
}