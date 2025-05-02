package com.example.w1965221_moviedatabase_coursework.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query



@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Query("SELECT * FROM movies WHERE LOWER(actors) LIKE '%' || LOWER(:actor) || '%'")
    suspend fun findMoviesByActor(actor: String): List<Movie>
}