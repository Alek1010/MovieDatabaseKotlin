package com.example.w1965221_moviedatabase_coursework.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query




@Dao
interface MovieDao {

    // Inserts a list of movies into the database.
    // If a movie with the same primary key already exists, it will be replaced.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>)

    // Inserts a single movie into the database.
    // Same conflict strategy: replace if the primary key already exists.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    // Searches for movies where the 'actors' field contains the given actor's name.
    // The query is case-insensitive by using LOWER() on both sides.
    @Query("SELECT * FROM movies WHERE LOWER(actors) LIKE '%' || LOWER(:actor) || '%'")
    suspend fun findMoviesByActor(actor: String): List<Movie>

    // Retrieves all movies from the database.
    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<Movie>

    // Deletes all records from the movies table.
    @Query("DELETE FROM movies")
    suspend fun clearAll()
}